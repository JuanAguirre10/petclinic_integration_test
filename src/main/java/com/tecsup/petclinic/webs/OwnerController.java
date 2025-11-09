package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mapper.OwnerMapper;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class OwnerController {

    private OwnerService ownerService;
    private OwnerMapper mapper;

    public OwnerController(OwnerService ownerService, OwnerMapper mapper) {
        this.ownerService = ownerService;
        this.mapper = mapper;
    }

    @GetMapping(value = "/owners")
    public ResponseEntity<List<OwnerDTO>> findAllOwners() {
        List<OwnerDTO> owners = ownerService.findAll();
        log.info("owners: " + owners);
        return ResponseEntity.ok(owners);
    }

    @PostMapping(value = "/owners")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<OwnerDTO> create(@RequestBody OwnerDTO ownerDTO) {
        OwnerDTO newOwnerDTO = ownerService.create(ownerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newOwnerDTO);
    }

    @GetMapping(value = "/owners/{id}")
    ResponseEntity<OwnerDTO> findById(@PathVariable Long id) {
        OwnerDTO ownerDto = null;
        try {
            ownerDto = ownerService.findById(id);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ownerDto);
    }

    @PutMapping(value = "/owners/{id}")
    ResponseEntity<OwnerDTO> update(@RequestBody OwnerDTO ownerDTO, @PathVariable Long id) {
        OwnerDTO updateOwnerDto = null;
        try {
            updateOwnerDto = ownerService.findById(id);
            updateOwnerDto.setFirstName(ownerDTO.getFirstName());
            updateOwnerDto.setLastName(ownerDTO.getLastName());
            updateOwnerDto.setAddress(ownerDTO.getAddress());
            updateOwnerDto.setCity(ownerDTO.getCity());
            updateOwnerDto.setTelephone(ownerDTO.getTelephone());
            ownerService.update(updateOwnerDto);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateOwnerDto);
    }

    @DeleteMapping(value = "/owners/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            ownerService.delete(id);
            return ResponseEntity.ok(" Delete ID :" + id);
        } catch (OwnerNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}