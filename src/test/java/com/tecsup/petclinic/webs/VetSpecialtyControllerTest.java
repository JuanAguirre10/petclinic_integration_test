package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetSpecialtyControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddSpecialtyToVet() throws Exception {
        Integer VET_ID = 1;
        Integer SPECIALTY_ID = 1;
        this.mockMvc.perform(post("/vets/" + VET_ID + "/specialties/" + SPECIALTY_ID))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vetId").value(VET_ID))
                .andExpect(jsonPath("$.specialtyId").value(SPECIALTY_ID));
    }

    @Test
    public void testAddSpecialtyToVetNotFound() throws Exception {
        Integer VET_ID = 666;
        Integer SPECIALTY_ID = 1;
        this.mockMvc.perform(post("/vets/" + VET_ID + "/specialties/" + SPECIALTY_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetSpecialtiesByVet() throws Exception {
        Integer VET_ID = 1;
        Integer SPECIALTY_ID = 2;
        this.mockMvc.perform(post("/vets/" + VET_ID + "/specialties/" + SPECIALTY_ID))
                .andExpect(status().isCreated());
        this.mockMvc.perform(get("/vets/" + VET_ID + "/specialties"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetSpecialtiesByVetNotFound() throws Exception {
        Integer VET_ID = 666;
        this.mockMvc.perform(get("/vets/" + VET_ID + "/specialties"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testRemoveSpecialtyFromVet() throws Exception {
        Integer VET_ID = 2;
        Integer SPECIALTY_ID = 1;
        this.mockMvc.perform(post("/vets/" + VET_ID + "/specialties/" + SPECIALTY_ID))
                .andExpect(status().isCreated());
        this.mockMvc.perform(delete("/vets/" + VET_ID + "/specialties/" + SPECIALTY_ID))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testRemoveSpecialtyFromVetNotFound() throws Exception {
        Integer VET_ID = 666;
        Integer SPECIALTY_ID = 1;
        this.mockMvc.perform(delete("/vets/" + VET_ID + "/specialties/" + SPECIALTY_ID))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddAndRemoveSpecialty() throws Exception {
        Integer VET_ID = 3;
        Integer SPECIALTY_ID = 3;
        this.mockMvc.perform(post("/vets/" + VET_ID + "/specialties/" + SPECIALTY_ID))
                .andExpect(status().isCreated());
        this.mockMvc.perform(get("/vets/" + VET_ID + "/specialties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
        this.mockMvc.perform(delete("/vets/" + VET_ID + "/specialties/" + SPECIALTY_ID))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/vets/" + VET_ID + "/specialties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

}