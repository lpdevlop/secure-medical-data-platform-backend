package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class GrantAccessResponse {
    private String requestId;
    private String doctorName;
    private String patientName;
    private String status;
    private Instant approvedAt;
    private Instant expiresAt;
}