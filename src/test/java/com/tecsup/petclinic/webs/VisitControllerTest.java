package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.tecsup.petclinic.dtos.VisitDTO;
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
public class VisitControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testFindAllVisits() throws Exception {
        final int ID_FIRST_RECORD = 1;
        this.mockMvc.perform(get("/visits"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$[0].id", is(ID_FIRST_RECORD)));
    }

    @Test
    public void testFindVisitOK() throws Exception {
        this.mockMvc.perform(get("/visits/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.visitDate").exists())
                .andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void testFindVisitKO() throws Exception {
        mockMvc.perform(get("/visits/666"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateVisit() throws Exception {
        String VISIT_DATE = "2024-11-09";
        String DESCRIPTION = "checkup";
        Integer PET_ID = 1;
        VisitDTO newVisitDTO = VisitDTO.builder()
                .visitDate(VISIT_DATE)
                .description(DESCRIPTION)
                .petId(PET_ID)
                .build();
        this.mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisitDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.visitDate", is(VISIT_DATE)))
                .andExpect(jsonPath("$.description", is(DESCRIPTION)))
                .andExpect(jsonPath("$.petId", is(PET_ID)));
    }

    @Test
    public void testDeleteVisit() throws Exception {
        String VISIT_DATE = "2024-11-09";
        String DESCRIPTION = "ToDeleteVisit";
        Integer PET_ID = 1;
        VisitDTO newVisitDTO = VisitDTO.builder()
                .visitDate(VISIT_DATE)
                .description(DESCRIPTION)
                .petId(PET_ID)
                .build();
        ResultActions mvcActions = mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisitDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);
        mockMvc.perform(delete("/visits/" + id))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteVisitKO() throws Exception {
        mockMvc.perform(delete("/visits/1000"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateVisit() throws Exception {
        String VISIT_DATE = "2024-11-09";
        String DESCRIPTION = "OriginalVisit";
        Integer PET_ID = 1;
        String UP_VISIT_DATE = "2024-12-25";
        String UP_DESCRIPTION = "UpdatedVisit";
        Integer UP_PET_ID = 2;
        VisitDTO newVisitDTO = VisitDTO.builder()
                .visitDate(VISIT_DATE)
                .description(DESCRIPTION)
                .petId(PET_ID)
                .build();
        ResultActions mvcActions = mockMvc.perform(post("/visits")
                        .content(om.writeValueAsString(newVisitDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
        String response = mvcActions.andReturn().getResponse().getContentAsString();
        Long id = JsonPath.parse(response).read("$.id", Long.class);
        VisitDTO upVisitDTO = VisitDTO.builder()
                .id(id)
                .visitDate(UP_VISIT_DATE)
                .description(UP_DESCRIPTION)
                .petId(UP_PET_ID)
                .build();
        mockMvc.perform(put("/visits/" + id)
                        .content(om.writeValueAsString(upVisitDTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        mockMvc.perform(get("/visits/" + id))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id.intValue())))
                .andExpect(jsonPath("$.visitDate", is(UP_VISIT_DATE)))
                .andExpect(jsonPath("$.description", is(UP_DESCRIPTION)))
                .andExpect(jsonPath("$.petId", is(UP_PET_ID)));
        mockMvc.perform(delete("/visits/" + id))
                .andExpect(status().isOk());
    }

}