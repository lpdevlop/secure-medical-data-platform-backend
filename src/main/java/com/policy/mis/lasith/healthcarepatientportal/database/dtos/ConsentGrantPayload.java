package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsentGrantPayload {
    private String doctorSecureId;
    private Instant expiresAt;
}