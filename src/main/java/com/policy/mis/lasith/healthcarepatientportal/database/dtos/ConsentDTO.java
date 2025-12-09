package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsentDTO {
    private String id;
    private String doctorName;
    private String doctorId;
    private String status;
    private Instant startAt;
    private Instant expiresAt;
    private Instant requestedAt;
    boolean isRevoked ;
}