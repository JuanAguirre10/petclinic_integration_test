package com.tecsup.petclinic.mapper;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.entities.Visit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring", nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface VisitMapper {

    VisitMapper INSTANCE = Mappers.getMapper(VisitMapper.class);

    @Mapping(source = "visitDate", target = "visitDate")
    @Mapping(source = "pet.id", target = "petId")
    Visit mapToEntity(VisitDTO visitDTO);

    default LocalDate stringToLocalDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }

    @Mapping(source = "visitDate", target = "visitDate")
    @Mapping(source = "pet.id", target = "petId")
    VisitDTO mapToDto(Visit visit);

    default String localDateToString(LocalDate date) {
        if (date != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return date.format(formatter);
        } else {
            return "";
        }
    }

    List<VisitDTO> mapToDtoList(List<Visit> visitList);

    List<Visit> mapToEntityList(List<VisitDTO> visitDTOList);

}