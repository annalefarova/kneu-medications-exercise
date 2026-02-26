package com.kneu.medications.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalTime intakeTime;

    @Column(columnDefinition = "text")
    String label;

    @ManyToOne
    Medication medication;

}
