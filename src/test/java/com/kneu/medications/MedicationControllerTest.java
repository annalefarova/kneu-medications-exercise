package com.kneu.medications;

import com.kneu.medications.model.Medication;
import com.kneu.medications.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MedicationRepository medicationRepository;

    @BeforeAll
    void setUp() {
        Medication medication1 = new Medication("Aspirin", "100mg");
        Medication medication2 = new Medication("Metformin", "500mg");

        medicationRepository.save(medication1);
        medicationRepository.save(medication2);
    }

    @Test
    void getAllMedications() throws Exception {

        mockMvc.perform(get("/medications"))
            .andExpect(status().isOk())
            .andExpect(content().json("""
                [
                    {"name": "Aspirin", "dosage": "100mg"},
                    {"name": "Metformin", "dosage": "500mg"}
                ]
                """));
    }
}
