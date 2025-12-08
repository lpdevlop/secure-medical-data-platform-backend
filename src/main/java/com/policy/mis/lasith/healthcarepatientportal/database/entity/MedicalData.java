package com.policy.mis.lasith.healthcarepatientportal.database.entity;

import com.policy.mis.lasith.healthcarepatientportal.database.enums.MedicalDataType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "medical_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class MedicalData {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User patient;

    private String name;

    private String docType;

    private String storageUrl;
    @Enumerated(EnumType.STRING)

    private MedicalDataType type;

    private String prescriptionNote;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] pdfData;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;


}