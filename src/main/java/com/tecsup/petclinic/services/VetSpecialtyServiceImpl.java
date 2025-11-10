package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.entities.Vet;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.exceptions.VetNotFoundException;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import com.tecsup.petclinic.repositories.VetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VetSpecialtyServiceImpl implements VetSpecialtyService {

    VetRepository vetRepository;
    SpecialtyRepository specialtyRepository;

    public VetSpecialtyServiceImpl(VetRepository vetRepository, SpecialtyRepository specialtyRepository) {
        this.vetRepository = vetRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public VetSpecialtyDTO addSpecialtyToVet(Integer vetId, Integer specialtyId) throws VetNotFoundException, SpecialtyNotFoundException {
        Optional<Vet> vetOpt = vetRepository.findById(vetId);
        if (!vetOpt.isPresent())
            throw new VetNotFoundException("Vet not found...!");

        Optional<Specialty> specialtyOpt = specialtyRepository.findById(specialtyId);
        if (!specialtyOpt.isPresent())
            throw new SpecialtyNotFoundException("Specialty not found...!");

        Vet vet = vetOpt.get();
        Specialty specialty = specialtyOpt.get();

        vet.getSpecialties().add(specialty);
        vetRepository.save(vet);

        return VetSpecialtyDTO.builder()
                .vetId(vetId)
                .specialtyId(specialtyId)
                .build();
    }

    @Override
    public void removeSpecialtyFromVet(Integer vetId, Integer specialtyId) throws VetNotFoundException, SpecialtyNotFoundException {
        Optional<Vet> vetOpt = vetRepository.findById(vetId);
        if (!vetOpt.isPresent())
            throw new VetNotFoundException("Vet not found...!");

        Optional<Specialty> specialtyOpt = specialtyRepository.findById(specialtyId);
        if (!specialtyOpt.isPresent())
            throw new SpecialtyNotFoundException("Specialty not found...!");

        Vet vet = vetOpt.get();
        Specialty specialty = specialtyOpt.get();

        vet.getSpecialties().remove(specialty);
        vetRepository.save(vet);
    }

    @Override
    public List<Integer> getSpecialtiesByVetId(Integer vetId) throws VetNotFoundException {
        Optional<Vet> vetOpt = vetRepository.findById(vetId);
        if (!vetOpt.isPresent())
            throw new VetNotFoundException("Vet not found...!");

        Vet vet = vetOpt.get();
        return vet.getSpecialties().stream()
                .map(Specialty::getId)
                .collect(Collectors.toList());
    }

}