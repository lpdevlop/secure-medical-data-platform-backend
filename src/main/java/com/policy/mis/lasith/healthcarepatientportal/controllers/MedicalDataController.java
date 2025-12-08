package com.policy.mis.lasith.healthcarepatientportal.controllers;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalDocumentResponse;
import com.policy.mis.lasith.healthcarepatientportal.services.MedicalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medical")
@RequiredArgsConstructor
public class MedicalDataController {

    private final MedicalService medicalDataService;

    @GetMapping("/history/{patientId}")
    public ResponseEntity<List<MedicalDocumentResponse>> getMedicalHistory(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(medicalDataService.getMedicalHistory(patientId));
    }

    @GetMapping("/prescriptions/{patientId}")
    public ResponseEntity<List<MedicalDocumentResponse>> getPrescriptionHistory(
            @PathVariable Long patientId
    ) {
        return ResponseEntity.ok(medicalDataService.getPrescriptionHistory(patientId));
    }
}
