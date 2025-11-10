package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.OwnerDTO;
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
public class OwnerControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllOwners() throws Exception {
        this.mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testFindOwnerOK() throws Exception {
        this.mockMvc.perform(get("/owners/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName").exists())
                .andExpect(jsonPath("$.lastName").exists());
    }

    @Test
    public void testFindOwnerKO() throws Exception {
        mockMvc.perform(get("/owners/666"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateOwner() throws Exception {
        String FIRST_NAME = "John";
        String LAST_NAME = "Doe";
        String ADDRESS = "123 Main St";
        String CITY = "Lima";
        String TELEPHONE = "123456789";
        OwnerDTO newOwnerDTO = OwnerDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .city(CITY)
                .telephone(TELEPHONE)
                .build();
        this.mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(LAST_NAME)))
                .andExpect(jsonPath("$.address", is(ADDRESS)))
                .andExpect(jsonPath("$.city", is(CITY)))
                .andExpect(jsonPath("$.telephone", is(TELEPHONE)));
    }

    @Test
    public void testDeleteOwner() throws Exception {
        String FIRST_NAME = "ToDelete";
        String LAST_NAME = "OwnerTest";
        String ADDRESS = "456 Test St";
        String CITY = "TestCity";
        String TELEPHONE = "987654321";
        OwnerDTO newOwnerDTO = OwnerDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .city(CITY)
                .telephone(TELEPHONE)
                .build();
        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);
        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteOwnerKO() throws Exception {
        mockMvc.perform(delete("/owners/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateOwner() throws Exception {
        String FIRST_NAME = "UpdateTest";
        String LAST_NAME = "OwnerOriginal";
        String ADDRESS = "Original St";
        String CITY = "OriginalCity";
        String TELEPHONE = "111111111";
        String UP_FIRST_NAME = "UpdatedName";
        String UP_LAST_NAME = "UpdatedLastName";
        String UP_ADDRESS = "Updated St";
        String UP_CITY = "UpdatedCity";
        String UP_TELEPHONE = "222222222";
        OwnerDTO newOwnerDTO = OwnerDTO.builder()
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .address(ADDRESS)
                .city(CITY)
                .telephone(TELEPHONE)
                .build();
        ResultActions mvcActions = mockMvc.perform(post("/owners")
                        .content(om.writeValueAsString(newOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);
        OwnerDTO upOwnerDTO = OwnerDTO.builder()
                .id(id)
                .firstName(UP_FIRST_NAME)
                .lastName(UP_LAST_NAME)
                .address(UP_ADDRESS)
                .city(UP_CITY)
                .telephone(UP_TELEPHONE)
                .build();
        mockMvc.perform(put("/owners/" + id)
                        .content(om.writeValueAsString(upOwnerDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/owners/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.firstName", is(UP_FIRST_NAME)))
                .andExpect(jsonPath("$.lastName", is(UP_LAST_NAME)))
                .andExpect(jsonPath("$.address", is(UP_ADDRESS)))
                .andExpect(jsonPath("$.city", is(UP_CITY)))
                .andExpect(jsonPath("$.telephone", is(UP_TELEPHONE)));
        mockMvc.perform(delete("/owners/" + id))
                .andExpect(status().isOk());
    }

}