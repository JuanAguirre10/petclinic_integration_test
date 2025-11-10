package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
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
public class SpecialtyControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllSpecialties() throws Exception {
        this.mockMvc.perform(get("/specialties"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testFindSpecialtyOK() throws Exception {
        String NAME = "radiology";
        this.mockMvc.perform(get("/specialties/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is(NAME)));
    }

    @Test
    public void testFindSpecialtyKO() throws Exception {
        mockMvc.perform(get("/specialties/666"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateSpecialty() throws Exception {
        String NAME = "neurology";
        SpecialtyDTO newSpecialtyDTO = SpecialtyDTO.builder()
                .name(NAME)
                .build();
        this.mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialtyDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(NAME)));
    }

    @Test
    public void testDeleteSpecialty() throws Exception {
        String NAME = "ToDeleteSpecialty";
        SpecialtyDTO newSpecialtyDTO = SpecialtyDTO.builder()
                .name(NAME)
                .build();
        ResultActions mvcActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialtyDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");
        mockMvc.perform(delete("/specialties/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteSpecialtyKO() throws Exception {
        mockMvc.perform(delete("/specialties/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateSpecialty() throws Exception {
        String NAME = "OriginalSpecialty";
        String UP_NAME = "UpdatedSpecialty";
        SpecialtyDTO newSpecialtyDTO = SpecialtyDTO.builder()
                .name(NAME)
                .build();
        ResultActions mvcActions = mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialtyDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Integer id = JsonPath.parse(response).read("$.id");
        SpecialtyDTO upSpecialtyDTO = SpecialtyDTO.builder()
                .id(id)
                .name(UP_NAME)
                .build();
        mockMvc.perform(put("/specialties/" + id)
                        .content(om.writeValueAsString(upSpecialtyDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/specialties/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is(UP_NAME)));
        mockMvc.perform(delete("/specialties/" + id))
                .andExpect(status().isOk());
    }

}