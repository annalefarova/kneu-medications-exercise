package com.kneu.medications.controller;

import com.kneu.medications.dto.CreatePrescriptionRequest;
import com.kneu.medications.dto.PrescriptionDto;

import com.kneu.medications.service.PrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PrescriptionController {

  private final PrescriptionService prescriptionService;

  public PrescriptionController(PrescriptionService prescriptionService) {
    this.prescriptionService = prescriptionService;
  }

  @PostMapping("/prescription")
  public ResponseEntity<PrescriptionDto> createPrescription(
      @RequestBody CreatePrescriptionRequest request) {
    return new ResponseEntity<>(prescriptionService.createPrescription(request), HttpStatus.OK);
  }

  @GetMapping("/prescriptions")
  public List<PrescriptionDto> getAllPrescriptions(
      @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
    return prescriptionService.listPrescriptions(page, size);
  }

  @GetMapping("/prescription/{id}")
  public PrescriptionDto getPrescription(@PathVariable UUID id) {
    return prescriptionService.getPrescription(id);
  }
}
