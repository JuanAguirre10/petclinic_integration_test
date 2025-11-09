package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mapper.SpecialtyMapper;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class SpecialtyController {

    private SpecialtyService specialtyService;
    private SpecialtyMapper mapper;

    public SpecialtyController(SpecialtyService specialtyService, SpecialtyMapper mapper) {
        this.specialtyService = specialtyService;
        this.mapper = mapper;
    }

    @GetMapping(value = "/specialties")
    public ResponseEntity<List<SpecialtyDTO>> findAllSpecialties() {
        List<SpecialtyDTO> specialties = specialtyService.findAll();
        log.info("specialties: " + specialties);
        return ResponseEntity.ok(specialties);
    }

    @PostMapping(value = "/specialties")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<SpecialtyDTO> create(@RequestBody SpecialtyDTO specialtyDTO) {
        SpecialtyDTO newSpecialtyDTO = specialtyService.create(specialtyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSpecialtyDTO);
    }

    @GetMapping(value = "/specialties/{id}")
    ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer id) {
        SpecialtyDTO specialtyDto = null;
        try {
            specialtyDto = specialtyService.findById(id);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(specialtyDto);
    }

    @PutMapping(value = "/specialties/{id}")
    ResponseEntity<SpecialtyDTO> update(@RequestBody SpecialtyDTO specialtyDTO, @PathVariable Integer id) {
        SpecialtyDTO updateSpecialtyDto = null;
        try {
            updateSpecialtyDto = specialtyService.findById(id);
            updateSpecialtyDto.setName(specialtyDTO.getName());
            specialtyService.update(updateSpecialtyDto);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateSpecialtyDto);
    }

    @DeleteMapping(value = "/specialties/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            specialtyService.delete(id);
            return ResponseEntity.ok(" Delete ID :" + id);
        } catch (SpecialtyNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}