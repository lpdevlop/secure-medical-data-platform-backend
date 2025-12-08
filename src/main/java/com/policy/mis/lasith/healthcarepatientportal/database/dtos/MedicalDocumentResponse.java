package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class MedicalDocumentResponse {
    private UUID id;
    private String name;
    private String docType;
    private Long requestExpireTime;
    private String type;
    private String storageUrl;
    private Instant createdAt;


}