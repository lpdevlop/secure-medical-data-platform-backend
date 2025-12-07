package com.policy.mis.lasith.healthcarepatientportal.controllers;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.*;
import com.policy.mis.lasith.healthcarepatientportal.database.enums.UserRoles;
import com.policy.mis.lasith.healthcarepatientportal.services.AccessService;
import org.springframework.context.annotation.Role;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/api/access")
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }


    @PostMapping("/request")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<AccessResponse> access(@RequestBody AccessRequestDTO accessRequest) {

        AccessResponse response = accessService.createAccessRequest(accessRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/grant")
    @PreAuthorize("hasRole('PATIENT')")
    public ResponseEntity<GrantAccessResponse> grantAccess(@RequestBody ApproveAccessDTO dto) {
        GrantAccessResponse response = accessService.approveAccess(dto);
        return ResponseEntity.ok(response);
    }

}
