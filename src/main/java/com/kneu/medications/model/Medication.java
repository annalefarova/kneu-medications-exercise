package com.kneu.medications.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "medications")
@Getter
@Setter
public class Medication {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String name;

  private String dosage;

  protected Medication() {}

  public Medication(String name, String dosage) {
    this.name = name;
    this.dosage = dosage;
  }
}
