package com.kneu.medications.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PrescriptionValidationError {
  MEDICATION_NOT_FOUND("Medication was not found by provided id"),
  PRESCRIPTION_NOT_FOUND("Prescription was not found by provided id"),
  MISSING_MEDICATION_ID("Medication id is required"),
  INVALID_INTAKE_TIME_FORMAT("Intake time should be in format HH:mm"),
  MISSING_INTAKE_TIME("Intake time is required");

  private final String name;

  PrescriptionValidationError(String name) {
    this.name = name;
  }

  @JsonValue
  public String getName() {
    return this.name;
  }
}
