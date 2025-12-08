package com.policy.mis.lasith.healthcarepatientportal.controllers;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalHistoryWithGrantInfo;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalRecordsWithGrantInfo;
import com.policy.mis.lasith.healthcarepatientportal.services.MedicalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/medical")
@RequiredArgsConstructor
public class MedicalDataController {

    private final MedicalService medicalDataService;

    @GetMapping("/history/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<MedicalHistoryWithGrantInfo>> getMedicalHistory(@PathVariable String doctorId
    ) {
        return ResponseEntity.ok(medicalDataService.getMedicalHistory(doctorId));
    }

    @GetMapping("/records/{doctorId}")
    public ResponseEntity<List<MedicalRecordsWithGrantInfo>> getPrescriptionHistory(
            @PathVariable String doctorId
    ) {
        return ResponseEntity.ok(medicalDataService.getPrescriptionHistory(doctorId));
    }


    @GetMapping("/historys/{patientId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<MedicalHistoryWithGrantInfo>> getMyMedicalHistoryForPatient(@PathVariable String patientId) {
        return ResponseEntity.ok(medicalDataService.getMyMedicalHistory(patientId));
    }

    @GetMapping("/record/{patientId}")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<List<MedicalRecordsWithGrantInfo>> getMyMedicalRecords(@PathVariable String patientId) {
        return ResponseEntity.ok(medicalDataService.getMyMedicalRecords(patientId));
    }
}
