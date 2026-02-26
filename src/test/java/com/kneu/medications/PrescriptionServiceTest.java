package com.kneu.medications;

import com.kneu.medications.exception.EventProcessingException;
import com.kneu.medications.mapper.PrescriptionMapper;
import com.kneu.medications.dto.CreatePrescriptionRequest;
import com.kneu.medications.model.Medication;
import com.kneu.medications.model.Prescription;
import com.kneu.medications.dto.PrescriptionDto;
import com.kneu.medications.repository.MedicationRepository;
import com.kneu.medications.repository.PrescriptionRepository;
import com.kneu.medications.service.PrescriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
public class PrescriptionServiceTest {
    private final UUID MEDICATION_ID = UUID.randomUUID();
    private final UUID PRESCRIPTION_ID = UUID.randomUUID();
    private final String INTAKE_TIME = "10:30";
    private final String LABEL = "Take with food";

    @MockitoBean
    PrescriptionRepository prescriptionRepository;

    @MockitoBean
    PrescriptionMapper prescriptionMapper;

    @MockitoBean
    MedicationRepository medicationRepository;

    @MockitoBean
    RestTemplate restTemplate;

    @Autowired
    PrescriptionService prescriptionService;


    @Test
    void whenPrescriptionWithIdExistCorrectPrescriptionReturned() {
        Prescription prescription = createPrescription(PRESCRIPTION_ID, INTAKE_TIME, LABEL);

        when(prescriptionRepository.findById(PRESCRIPTION_ID)).thenReturn(Optional.of(prescription));
        when(prescriptionMapper.toDto(prescription)).thenReturn(new PrescriptionDto(prescription.getId(), prescription.getIntakeTime(), prescription.getLabel(), prescription.getMedication().getId()));

        PrescriptionDto prescriptionDto = prescriptionService.getPrescription(PRESCRIPTION_ID);
        assertEquals("Prescription id differs from asserted", PRESCRIPTION_ID, prescriptionDto.id());
        assertEquals("Intake time differs from asserted", INTAKE_TIME, prescriptionDto.intakeTime().toString());
        assertEquals("Label differs from asserted", LABEL, prescriptionDto.label());
    }

    @Test
    void whenPrescriptionInputValidPrescriptionCreatedSuccessfully() {
        CreatePrescriptionRequest request = new CreatePrescriptionRequest(MEDICATION_ID.toString(), INTAKE_TIME, LABEL);
        Prescription prescription = createPrescription(PRESCRIPTION_ID, INTAKE_TIME, LABEL);
        Medication medication = createMedication();

        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.of(medication));
        when(prescriptionMapper.toDto(any())).thenReturn(new PrescriptionDto(prescription.getId(), prescription.getIntakeTime(), prescription.getLabel(), prescription.getMedication().getId()));

        PrescriptionDto prescriptionDto = prescriptionService.createPrescription(request);
        assertEquals("Prescription id differs from asserted", PRESCRIPTION_ID, prescriptionDto.id());
        assertEquals("Intake time differs from asserted", INTAKE_TIME, prescriptionDto.intakeTime().toString());
        assertEquals("Label differs from asserted", LABEL, prescriptionDto.label());
    }

    @Test
    void whenMissingMedicationIdPrescriptionIsNotCreated() {
        CreatePrescriptionRequest invalidRequest = new CreatePrescriptionRequest(null, INTAKE_TIME, LABEL);
        assertThrows(IllegalArgumentException.class, () -> prescriptionService.createPrescription(invalidRequest));
    }

    @Test
    void whenMissingIntakeTimePrescriptionIsNotCreated() {
        CreatePrescriptionRequest invalidRequest = new CreatePrescriptionRequest(MEDICATION_ID.toString(), null, null);
        assertThrows(IllegalArgumentException.class, () -> prescriptionService.createPrescription(invalidRequest));
    }

    @Test
    void whenIntakeTimeInvalidPrescriptionIsNotCreated() {
        CreatePrescriptionRequest invalidRequest = new CreatePrescriptionRequest(MEDICATION_ID.toString(), "10", null);
        assertThrows(IllegalArgumentException.class, () -> prescriptionService.createPrescription(invalidRequest));
    }

    @Test
    void whenMedicationNotFoundPrescriptionIsNotCreated() {
        CreatePrescriptionRequest request = new CreatePrescriptionRequest(MEDICATION_ID.toString(), INTAKE_TIME, null);

        when(medicationRepository.findById(MEDICATION_ID)).thenReturn(Optional.empty());
        assertThrows(EventProcessingException.class, () -> prescriptionService.createPrescription(request));
    }

    private Prescription createPrescription(UUID id, String intakeTime, String label) {
        Prescription prescription = new Prescription();
        prescription.setId(id);
        prescription.setLabel(label);
        prescription.setMedication(createMedication());
        prescription.setIntakeTime(LocalTime.parse(intakeTime));
        return prescription;
    }

    private Medication createMedication() {
        String MEDICATION_NAME = "Aspirin";
        String MEDICATION_DOSAGE = "100mg";
        Medication medication = new Medication(MEDICATION_NAME, MEDICATION_DOSAGE);
        medication.setId(MEDICATION_ID);
        return medication;
    }
}
