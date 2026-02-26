package com.kneu.medications.dto;

public record CreatePrescriptionRequest(
        String medicationId,
        String intakeTime,
        String label
) {
}
