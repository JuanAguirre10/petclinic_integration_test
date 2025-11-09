package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VetDTO;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.mapper.VetMapper;
import com.tecsup.petclinic.services.VetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VetController {

    private VetService vetService;
    private VetMapper mapper;

    public VetController(VetService vetService, VetMapper mapper) {
        this.vetService = vetService;
        this.mapper = mapper;
    }

    @GetMapping(value = "/vets")
    public ResponseEntity<List<VetDTO>> findAllVets() {
        List<VetDTO> vets = vetService.findAll();
        log.info("vets: " + vets);
        return ResponseEntity.ok(vets);
    }

    @PostMapping(value = "/vets")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<VetDTO> create(@RequestBody VetDTO vetDTO) {
        VetDTO newVetDTO = vetService.create(vetDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVetDTO);
    }

    @GetMapping(value = "/vets/{id}")
    ResponseEntity<VetDTO> findById(@PathVariable Integer id) {
        VetDTO vetDto = null;
        try {
            vetDto = vetService.findById(id);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vetDto);
    }

    @PutMapping(value = "/vets/{id}")
    ResponseEntity<VetDTO> update(@RequestBody VetDTO vetDTO, @PathVariable Integer id) {
        VetDTO updateVetDto = null;
        try {
            updateVetDto = vetService.findById(id);
            updateVetDto.setFirstName(vetDTO.getFirstName());
            updateVetDto.setLastName(vetDTO.getLastName());
            vetService.update(updateVetDto);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateVetDto);
    }

    @DeleteMapping(value = "/vets/{id}")
    ResponseEntity<String> delete(@PathVariable Integer id) {
        try {
            vetService.delete(id);
            return ResponseEntity.ok(" Delete ID :" + id);
        } catch (VetNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}