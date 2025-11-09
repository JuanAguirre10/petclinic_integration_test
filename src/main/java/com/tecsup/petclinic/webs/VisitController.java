package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.mapper.VisitMapper;
import com.tecsup.petclinic.services.VisitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class VisitController {

    private VisitService visitService;
    private VisitMapper mapper;

    public VisitController(VisitService visitService, VisitMapper mapper) {
        this.visitService = visitService;
        this.mapper = mapper;
    }

    @GetMapping(value = "/visits")
    public ResponseEntity<List<VisitDTO>> findAllVisits() {
        List<VisitDTO> visits = visitService.findAll();
        log.info("visits: " + visits);
        return ResponseEntity.ok(visits);
    }

    @PostMapping(value = "/visits")
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<VisitDTO> create(@RequestBody VisitDTO visitDTO) {
        VisitDTO newVisitDTO = visitService.create(visitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newVisitDTO);
    }

    @GetMapping(value = "/visits/{id}")
    ResponseEntity<VisitDTO> findById(@PathVariable Long id) {
        VisitDTO visitDto = null;
        try {
            visitDto = visitService.findById(id);
        } catch (VisitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(visitDto);
    }

    @PutMapping(value = "/visits/{id}")
    ResponseEntity<VisitDTO> update(@RequestBody VisitDTO visitDTO, @PathVariable Long id) {
        VisitDTO updateVisitDto = null;
        try {
            updateVisitDto = visitService.findById(id);
            updateVisitDto.setVisitDate(visitDTO.getVisitDate());
            updateVisitDto.setDescription(visitDTO.getDescription());
            updateVisitDto.setPetId(visitDTO.getPetId());
            visitService.update(updateVisitDto);
        } catch (VisitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateVisitDto);
    }

    @DeleteMapping(value = "/visits/{id}")
    ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            visitService.delete(id);
            return ResponseEntity.ok(" Delete ID :" + id);
        } catch (VisitNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}