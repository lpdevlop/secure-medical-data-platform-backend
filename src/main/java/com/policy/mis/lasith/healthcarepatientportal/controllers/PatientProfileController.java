package com.policy.mis.lasith.healthcarepatientportal.controllers;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.PatientProfileWithGrantInfoDTO;
import com.policy.mis.lasith.healthcarepatientportal.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctor/patient")
@RequiredArgsConstructor
public class PatientProfileController {
    private final ProfileService profileService;

    @GetMapping("/profile/{doctorId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<List<PatientProfileWithGrantInfoDTO>> getPatientProfile(
            @PathVariable String doctorId) {
        return ResponseEntity.ok(profileService.getPatientProfile(doctorId));

    }
}
