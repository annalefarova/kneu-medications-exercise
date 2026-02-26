package com.kneu.medications;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kneu.medications.enums.PrescriptionValidationError;
import com.kneu.medications.dto.CreatePrescriptionRequest;
import com.kneu.medications.model.Medication;

import com.kneu.medications.model.Prescription;
import com.kneu.medications.repository.MedicationRepository;
import com.kneu.medications.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PrescriptionControllerTest {

    private UUID MEDICATION_ID;
    private final String INTAKE_TIME = "10:30";
    private final String LABEL = "Take with food";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicationRepository medicationRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    void setUp() {
        Medication medication = new Medication("Aspirin", "100mg");
        medicationRepository.save(medication);
        MEDICATION_ID = medication.getId();
    }

    @Test
    void whenInputValidPrescriptionCreatedSuccessfully() throws Exception {
        CreatePrescriptionRequest request = createPrescriptionRequest();

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/prescription")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.medicationId").value(MEDICATION_ID.toString()))
                .andExpect(jsonPath("$.intakeTime").value(INTAKE_TIME))
                .andExpect(jsonPath("$.label").value(LABEL));
    }

    @Test
    void whenPrescriptionIsMissingMedicationIdValidationErrorReturned() throws Exception {
        CreatePrescriptionRequest request = new CreatePrescriptionRequest(null, INTAKE_TIME, LABEL);

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/prescription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(PrescriptionValidationError.MISSING_MEDICATION_ID.getName()));
    }

    @Test
    void whenPrescriptionIsLinkedToNonExistingMedicationIdErrorReturned() throws Exception {
        CreatePrescriptionRequest request = createPrescriptionRequestWithInvalidMedicationId();

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/prescription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value(PrescriptionValidationError.MEDICATION_NOT_FOUND.getName()));
    }

    @Test
    void whenIntakeTimeIsMissingValidationErrorReturned() throws Exception {
        CreatePrescriptionRequest request = new CreatePrescriptionRequest(MEDICATION_ID.toString(), null, LABEL);

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/prescription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(PrescriptionValidationError.MISSING_INTAKE_TIME.getName()));
    }

    @Test
    void whenIntakeTimeIsInvalidValidationErrorReturned() throws Exception {
        CreatePrescriptionRequest request = new CreatePrescriptionRequest(MEDICATION_ID.toString(), "10", LABEL);

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/prescription")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.message").value(PrescriptionValidationError.INVALID_INTAKE_TIME_FORMAT.getName()));
    }

    @Test
    void getAllPrescriptionsOfDefaultFirstPage() throws Exception {
        createPrescription();

        mockMvc.perform(get("/prescriptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].medicationId").value(MEDICATION_ID.toString()))
                .andExpect(jsonPath("$[0].label").value(LABEL))
                .andExpect(jsonPath("$[0].intakeTime").value(INTAKE_TIME))
                .andExpect(jsonPath("$[0].id").isNotEmpty());
    }

    private CreatePrescriptionRequest createPrescriptionRequest() {
        return new CreatePrescriptionRequest(MEDICATION_ID.toString(), INTAKE_TIME, LABEL);
    }

    private CreatePrescriptionRequest createPrescriptionRequestWithInvalidMedicationId() {
        return new CreatePrescriptionRequest(UUID.randomUUID().toString(), INTAKE_TIME, LABEL);
    }

    private void createPrescription() {
        Prescription prescription = new Prescription();
        prescription.setLabel(LABEL);
        prescription.setMedication(medicationRepository.findAll().getFirst());
        prescription.setIntakeTime(LocalTime.parse(INTAKE_TIME));
        prescriptionRepository.save(prescription);
    }
}
