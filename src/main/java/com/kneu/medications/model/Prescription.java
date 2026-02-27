package com.kneu.medications.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Setter
@Getter
public class Prescription {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private LocalTime intakeTime;

  @Column(columnDefinition = "text")
  String label;

  @ManyToOne Medication medication;

  Instant timestamp;

  public Prescription() {
    this.timestamp = Instant.now();
  }
}
