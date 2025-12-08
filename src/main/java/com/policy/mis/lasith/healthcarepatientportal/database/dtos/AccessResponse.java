package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.Builder;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Builder
public class AccessResponse implements Serializable {

    private UUID requestId;
    private String doctorName;
    private String patientName;
    private String status;
    private Instant requestedAt;
}
