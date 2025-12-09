package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MedicalRecordsWithGrantInfo {
    String patientId;
    private String name;
    String medicalId;
    String type;
    Instant accessExpires;
}
