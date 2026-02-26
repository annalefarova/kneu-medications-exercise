package com.kneu.medications.service;

import com.kneu.medications.dto.CreatePrescriptionRequest;
import com.kneu.medications.dto.PrescriptionDto;
import com.kneu.medications.enums.PrescriptionValidationError;
import com.kneu.medications.exception.EventProcessingException;
import com.kneu.medications.mapper.PrescriptionMapper;
import com.kneu.medications.model.*;
import com.kneu.medications.repository.MedicationRepository;
import com.kneu.medications.repository.PrescriptionRepository;
import com.kneu.medications.validator.PrescriptionValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalTime;
import java.util.*;

@Service
public class PrescriptionService {
    private static final Logger logger = LoggerFactory.getLogger(PrescriptionService.class);

    private final PrescriptionRepository prescriptionRepository;
    private final MedicationRepository medicationRepository;
    private final PrescriptionMapper prescriptionMapper;

    public PrescriptionService(PrescriptionRepository prescriptionRepository,
                               MedicationRepository medicationRepository,
                               PrescriptionMapper mapper) {
        this.prescriptionRepository = prescriptionRepository;
        this.medicationRepository = medicationRepository;
        this.prescriptionMapper = mapper;
    }

    public PrescriptionDto createPrescription(CreatePrescriptionRequest request) {
        Collection<PrescriptionValidationError> validationErrors = PrescriptionValidator.prescriptionValidator(request);
        if (!CollectionUtils.isEmpty(validationErrors)) {
            String validationErrorsString = String.join("; ", validationErrors.stream().map(
                    PrescriptionValidationError::getName).toList());
            logger.error("Prescription can not be created because of following validation errors: {}", validationErrorsString);
            throw new IllegalArgumentException(validationErrorsString);
        }
        Optional<Medication> medication = Optional.of(medicationRepository.findById(UUID.fromString(request.medicationId()))
                .orElseThrow(
                        () -> new EventProcessingException(
                                PrescriptionValidationError.MEDICATION_NOT_FOUND.getName(),
                                HttpStatus.NOT_FOUND.value()
                        )
                )
        );

        Prescription prescription = new Prescription();
        prescription.setMedication(medication.get());
        prescription.setIntakeTime(LocalTime.parse(request.intakeTime()));
        prescription.setLabel(request.label());

        prescriptionRepository.save(prescription);

        return prescriptionMapper.toDto(prescription);
    }

    public PrescriptionDto getPrescription(UUID prescriptionId) {
        Prescription prescription = prescriptionRepository.findById(prescriptionId)
                .orElseThrow(
                        () -> new EventProcessingException(
                                PrescriptionValidationError.PRESCRIPTION_NOT_FOUND.getName(),
                                HttpStatus.NOT_FOUND.value()
                        )
                );
        return prescriptionMapper.toDto(prescription);
    }

    public List<PrescriptionDto> listPrescriptions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return prescriptionRepository.findAllPrescriptionDtos(pageable).getContent();
    }
}
