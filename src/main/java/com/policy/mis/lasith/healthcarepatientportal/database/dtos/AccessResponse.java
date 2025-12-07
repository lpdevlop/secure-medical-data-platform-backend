package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public class AccessResponse {

    private UUID requestId;
    private String doctorName;
    private String patientName;
    private String status;
    private Instant requestedAt;
}
