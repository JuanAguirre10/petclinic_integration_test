package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.mapper.PetTypeMapper;
import com.tecsup.petclinic.services.PetTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class PetTypeController {

    private PetTypeService petTypeService;
    private PetTypeMapper mapper;

    public PetTypeController(PetTypeService petTypeService, PetTypeMapper mapper) {
        this.petTypeService = petTypeService;
        this.mapper = mapper;
    }

    @GetMapping(value = "/pettypes")
    public ResponseEntity<List<PetTypeDTO>> findAllPetTypes() {
        List<PetTypeDTO> petTypes = petTypeService.findAll();
        log.info("petTypes: " + petTypes);
        return ResponseEntity.ok(petTypes);
    }

    @PostMapping(value = "/pettypes")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<PetTypeDTO> create(@RequestBody PetTypeDTO petTypeDTO) {
        PetTypeDTO newPetTypeDTO = petTypeService.create(petTypeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPetTypeDTO);
    }

    @GetMapping(value = "/pettypes/{id}")
    ResponseEntity<PetTypeDTO> findById(@PathVariable Integer id) {
        PetTypeDTO petTypeDto = null;
        try {
            petTypeDto = petTypeService.findById(id);
        } catch (PetTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(petTypeDto);
    }

    @PutMapping(value = "/pettypes/{id}")
    ResponseEntity<PetTypeDTO> update(@RequestBody PetTypeDTO petTypeDTO, @PathVariable Integer id) {
        PetTypeDTO updatePetTypeDto = null;
        try {
            updatePetTypeDto = petTypeService.findById(id);
            updatePetTypeDto.setName(petTypeDTO.getName());
            petTypeService.update(updatePetTypeDto);
        } catch (PetTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatePetTypeDto);
    }

    @DeleteMapping(value = "/pettypes/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            petTypeService.delete(id);
            return ResponseEntity.ok(" Delete ID :" + id);
        } catch (PetTypeNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}