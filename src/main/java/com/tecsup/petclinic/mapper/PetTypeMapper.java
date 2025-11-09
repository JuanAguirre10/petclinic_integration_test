package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.PetTypeDTO;
import com.tecsup.petclinic.entities.PetType;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PetTypeMapper {

    PetTypeMapper INSTANCE = Mappers.getMapper(PetTypeMapper.class);

    PetType mapToEntity(PetTypeDTO petTypeDTO);

    PetTypeDTO mapToDto(PetType petType);

    List<PetTypeDTO> mapToDtoList(List<PetType> petTypeList);

    List<PetType> mapToEntityList(List<PetTypeDTO> petTypeDTOList);

}