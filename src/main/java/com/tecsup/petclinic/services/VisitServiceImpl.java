package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.mapper.VisitMapper;
import com.tecsup.petclinic.repositories.VisitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VisitServiceImpl implements VisitService {

    VisitRepository visitRepository;
    VisitMapper visitMapper;

    public VisitServiceImpl(VisitRepository visitRepository, VisitMapper visitMapper) {
        this.visitRepository = visitRepository;
        this.visitMapper = visitMapper;
    }

    @Override
    public VisitDTO create(VisitDTO visitDTO) {
        Visit newVisit = visitRepository.save(visitMapper.mapToEntity(visitDTO));
        return visitMapper.mapToDto(newVisit);
    }

    @Override
    public VisitDTO update(VisitDTO visitDTO) {
        Visit newVisit = visitRepository.save(visitMapper.mapToEntity(visitDTO));
        return visitMapper.mapToDto(newVisit);
    }

    @Override
    public void delete(Long id) throws VisitNotFoundException {
        VisitDTO visit = findById(id);
        visitRepository.delete(this.visitMapper.mapToEntity(visit));
    }

    @Override
    public VisitDTO findById(Long id) throws VisitNotFoundException {
        Optional<Visit> visit = visitRepository.findById(id);
        if (!visit.isPresent())
            throw new VisitNotFoundException("Record not found...!");
        return this.visitMapper.mapToDto(visit.get());
    }

    @Override
    public List<VisitDTO> findByPetId(Integer petId) {
        List<Visit> visits = visitRepository.findByPetId(petId);
        visits.forEach(visit -> log.info("" + visit));
        return visits.stream().map(this.visitMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<VisitDTO> findAll() {
        List<Visit> visits = visitRepository.findAll();
        return visits.stream().map(this.visitMapper::mapToDto).collect(Collectors.toList());
    }

}