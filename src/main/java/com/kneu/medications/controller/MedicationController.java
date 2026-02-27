package com.kneu.medications.controller;

import com.kneu.medications.model.Medication;
import com.kneu.medications.service.MedicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MedicationController {

  private final MedicationService medicationService;

  public MedicationController(MedicationService medicationService) {
    this.medicationService = medicationService;
  }

  @GetMapping("/medications")
  public List<Medication> getAllMedications(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return medicationService.listMedications(page, size);
  }
}
