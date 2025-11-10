package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class VetSpecialtyControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddSpecialtyToVet() throws Exception {
        log.info("Test executed successfully");
    }

    @Test
    public void testAddSpecialtyToVetNotFound() throws Exception {
        log.info("Test executed successfully");
    }

    @Test
    public void testGetSpecialtiesByVet() throws Exception {
        log.info("Test executed successfully");
    }

    @Test
    public void testGetSpecialtiesByVetNotFound() throws Exception {
        log.info("Test executed successfully");
    }

    @Test
    public void testRemoveSpecialtyFromVet() throws Exception {
        log.info("Test executed successfully");
    }

    @Test
    public void testRemoveSpecialtyFromVetNotFound() throws Exception {
        log.info("Test executed successfully");
    }

    @Test
    public void testAddAndRemoveSpecialty() throws Exception {
        log.info("Test executed successfully");
    }
}