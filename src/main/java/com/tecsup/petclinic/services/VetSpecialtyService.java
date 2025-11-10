package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.VetSpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.exceptions.VetNotFoundException;

import java.util.List;

public interface VetSpecialtyService {

    VetSpecialtyDTO addSpecialtyToVet(Integer vetId, Integer specialtyId) throws VetNotFoundException, SpecialtyNotFoundException;

    void removeSpecialtyFromVet(Integer vetId, Integer specialtyId) throws VetNotFoundException, SpecialtyNotFoundException;

    List<Integer> getSpecialtiesByVetId(Integer vetId) throws VetNotFoundException;

}