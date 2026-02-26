package com.kneu.medications.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

public record PrescriptionDto(
        UUID id,
        @JsonFormat(pattern = "HH:mm")
        LocalTime intakeTime,
        String label,
        UUID medicationId,
        Instant timestamp
) {
}
