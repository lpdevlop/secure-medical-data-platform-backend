package com.policy.mis.lasith.healthcarepatientportal.controllers;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.*;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessGrant;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.services.AccessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/access")
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }


    @PostMapping(value = "/request", produces = "application/json")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<?> access(@RequestBody AccessRequestDTO accessRequest) {

        AccessResponse response = accessService.createAccessRequest(accessRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(true);
    }

    @PostMapping("/grant")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<GrantAccessResponse> grantAccess(@RequestBody ApproveAccessDTO dto) {
        GrantAccessResponse response = accessService.approveAccess(dto);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/revoke")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<AccessGrant> revokeConsent(@RequestBody ApproveAccessDTO dto) {
        return ResponseEntity.ok(accessService.revokeConsent(dto));
    }

    @GetMapping("/active")
    public ResponseEntity<List<AccessGrant>> getActiveConsents(@AuthenticationPrincipal User currentPatient) {
        return ResponseEntity.ok(accessService.getActiveConsents(currentPatient.getSecureId()));
    }

}
