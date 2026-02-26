package com.kneu.medications.repository;

import com.kneu.medications.model.Prescription;
import com.kneu.medications.dto.PrescriptionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface PrescriptionRepository extends JpaRepository<Prescription, UUID> {

    @Query("SELECT new com.kneu.medications.dto.PrescriptionDto(p.id, p.intakeTime, p.label, p.medication.id, p.timestamp) FROM Prescription p")
    Page<PrescriptionDto> findAllPrescriptionDtos(Pageable pageable);
}
