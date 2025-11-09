package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import com.tecsup.petclinic.exceptions.PetTypeNotFoundException;
import com.tecsup.petclinic.mapper.PetTypeMapper;
import com.tecsup.petclinic.repositories.PetTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PetTypeServiceImpl implements PetTypeService {

    PetTypeRepository petTypeRepository;
    PetTypeMapper petTypeMapper;

    public PetTypeServiceImpl(PetTypeRepository petTypeRepository, PetTypeMapper petTypeMapper) {
        this.petTypeRepository = petTypeRepository;
        this.petTypeMapper = petTypeMapper;
    }

    @Override
    public PetTypeDTO create(PetTypeDTO petTypeDTO) {
        PetType newPetType = petTypeRepository.save(petTypeMapper.mapToEntity(petTypeDTO));
        return petTypeMapper.mapToDto(newPetType);
    }

    @Override
    public PetTypeDTO update(PetTypeDTO petTypeDTO) {
        PetType newPetType = petTypeRepository.save(petTypeMapper.mapToEntity(petTypeDTO));
        return petTypeMapper.mapToDto(newPetType);
    }

    @Override
    public void delete(Integer id) throws PetTypeNotFoundException {
        PetTypeDTO petType = findById(id);
        petTypeRepository.delete(this.petTypeMapper.mapToEntity(petType));
    }

    @Override
    public PetTypeDTO findById(Integer id) throws PetTypeNotFoundException {
        Optional<PetType> petType = petTypeRepository.findById(id);
        if (!petType.isPresent())
            throw new PetTypeNotFoundException("Record not found...!");
        return this.petTypeMapper.mapToDto(petType.get());
    }

    @Override
    public List<PetTypeDTO> findByName(String name) {
        List<PetType> petTypes = petTypeRepository.findByName(name);
        petTypes.forEach(petType -> log.info("" + petType));
        return petTypes.stream().map(this.petTypeMapper::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<PetTypeDTO> findAll() {
        List<PetType> petTypes = petTypeRepository.findAll();
        return petTypes.stream().map(this.petTypeMapper::mapToDto).collect(Collectors.toList());
    }

}