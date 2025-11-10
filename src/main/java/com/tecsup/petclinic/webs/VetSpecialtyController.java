package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.services.VetSpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VetSpecialtyController {

    private VetSpecialtyService vetSpecialtyService;

    public VetSpecialtyController(VetSpecialtyService vetSpecialtyService) {
        this.vetSpecialtyService = vetSpecialtyService;
    }

    @PostMapping(value = "/vets/{vetId}/specialties/{specialtyId}")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<VetSpecialtyDTO> addSpecialtyToVet(@PathVariable Integer vetId, @PathVariable Integer specialtyId) {
        try {
            VetSpecialtyDTO vetSpecialtyDTO = vetSpecialtyService.addSpecialtyToVet(vetId, specialtyId);
            return ResponseEntity.status(HttpStatus.CREATED).body(vetSpecialtyDTO);
        } catch (VetNotFoundException | SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/vets/{vetId}/specialties/{specialtyId}")
    ResponseEntity<String> removeSpecialtyFromVet(@PathVariable Integer vetId, @PathVariable Integer specialtyId) {
        try {
            vetSpecialtyService.removeSpecialtyFromVet(vetId, specialtyId);
            return ResponseEntity.ok("Specialty removed from Vet");
        } catch (VetNotFoundException | SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/vets/{vetId}/specialties")
    ResponseEntity<List<Integer>> getSpecialtiesByVet(@PathVariable Integer vetId) {
        try {
            List<Integer> specialties = vetSpecialtyService.getSpecialtiesByVetId(vetId);
            return ResponseEntity.ok(specialties);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}