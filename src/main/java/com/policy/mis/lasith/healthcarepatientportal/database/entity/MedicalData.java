package com.policy.mis.lasith.healthcarepatientportal.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "medical_document")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicalData {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User patient;

    private String name;

    private String docType;

    private String storageUrl;

    private Instant createdAt;

    private String type;


    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pdfData;

}