package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.PetTypeDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class PetTypeControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllPetTypes() throws Exception {
        final int ID_FIRST_RECORD = 1;
        this.mockMvc.perform(get("/pettypes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }

    @Test
    public void testFindPetTypeOK() throws Exception {
        String NAME = "cat";
        this.mockMvc.perform(get("/pettypes/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(NAME)));
    }

    @Test
    public void testFindPetTypeKO() throws Exception {
        mockMvc.perform(get("/pettypes/666"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePetType() throws Exception {
        String NAME = "rabbit";
        PetTypeDTO newPetTypeDTO = PetTypeDTO.builder()
                .name(NAME)
                .build();
        this.mockMvc.perform(post("/pettypes")
                        .content(om.writeValueAsString(newPetTypeDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(NAME)));
    }

    @Test
    public void testDeletePetType() throws Exception {
        String NAME = "ToDeleteType";
        PetTypeDTO newPetTypeDTO = PetTypeDTO.builder()
                .name(NAME)
                .build();
        ResultActions mvcActions = mockMvc.perform(post("/pettypes")
                        .content(om.writeValueAsString(newPetTypeDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");
        mockMvc.perform(delete("/pettypes/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePetTypeKO() throws Exception {
        mockMvc.perform(delete("/pettypes/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdatePetType() throws Exception {
        String NAME = "OriginalType";
        String UP_NAME = "UpdatedType";
        PetTypeDTO newPetTypeDTO = PetTypeDTO.builder()
                .name(NAME)
                .build();
        ResultActions mvcActions = mockMvc.perform(post("/pettypes")
                        .content(om.writeValueAsString(newPetTypeDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");
        PetTypeDTO upPetTypeDTO = PetTypeDTO.builder()
                .id(id)
                .name(UP_NAME)
                .build();
        mockMvc.perform(put("/pettypes/" + id)
                        .content(om.writeValueAsString(upPetTypeDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/pettypes/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(UP_NAME)));
        mockMvc.perform(delete("/pettypes/" + id))
                .andExpect(status().isOk());
    }

}