package com.kneu.medications.validator;

import com.kneu.medications.enums.PrescriptionValidationError;
import com.kneu.medications.dto.CreatePrescriptionRequest;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
public class PrescriptionValidator {

    private static final String INTAKE_TIME_PATTERN = "HH:mm";

    public static Collection<PrescriptionValidationError> prescriptionValidator(CreatePrescriptionRequest request) {
        Collection<PrescriptionValidationError> validationErrors = new ArrayList<>();

        if (request.medicationId() == null) {
            validationErrors.add(PrescriptionValidationError.MISSING_MEDICATION_ID);
        }
        if (request.intakeTime() == null) {
            validationErrors.add(PrescriptionValidationError.MISSING_INTAKE_TIME);
        }
        if (request.intakeTime() != null && !isValidTime(request.intakeTime())) {
            validationErrors.add(PrescriptionValidationError.INVALID_INTAKE_TIME_FORMAT);
        }

        return validationErrors;
    }

    public static boolean isValidTime(String timeStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(INTAKE_TIME_PATTERN);
            LocalTime.parse(timeStr, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
