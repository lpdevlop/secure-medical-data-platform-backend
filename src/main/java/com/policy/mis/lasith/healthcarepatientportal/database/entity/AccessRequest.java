package com.policy.mis.lasith.healthcarepatientportal.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "access_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AccessRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(optional = false)
    private User requesterDoctor;

    @Column(name = "secure_id", unique = true)
    private String accessRequestsecureId;

    @ManyToOne(optional = false)
    private User patient;


    @Enumerated(EnumType.STRING)
    private RequestStatus status;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;


    public enum RequestStatus { PENDING, APPROVED,EXPIRED, DENIED, CANCELLED,GRANTED,REVOKED
    }


    private Instant grantStartAt;

    private Instant grantExpiresAt;


    private boolean active ;

    private boolean isExpired;

    @Column(name = "is_revoked")
    private boolean revoked;


}