package com.kneu.medications.mapper;

import com.kneu.medications.model.Prescription;
import com.kneu.medications.dto.PrescriptionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {

    @Mapping(source = "medication.id", target = "medicationId")
    PrescriptionDto toDto(Prescription entity);

}
