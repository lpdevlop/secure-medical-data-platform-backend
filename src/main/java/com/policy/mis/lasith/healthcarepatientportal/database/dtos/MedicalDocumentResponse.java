package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.MedicalData;
import lombok.Builder;
import lombok.Data;

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

    public static MedicalDocumentResponse fromEntity(MedicalData data) {
        return MedicalDocumentResponse.builder()
                .id(data.getId())
                .name(data.getName())
                .docType(data.getDocType())
                .type(data.getType())
                .storageUrl(data.getStorageUrl())
                .createdAt(data.getCreatedAt())
                .build();
    }
}