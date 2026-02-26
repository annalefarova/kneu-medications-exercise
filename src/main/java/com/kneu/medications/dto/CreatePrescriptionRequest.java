package com.kneu.medications.dto;

import java.util.Optional;

public record CreatePrescriptionRequest(
        String medicationId,
        String intakeTime,
        Optional<String> label
) {
}
