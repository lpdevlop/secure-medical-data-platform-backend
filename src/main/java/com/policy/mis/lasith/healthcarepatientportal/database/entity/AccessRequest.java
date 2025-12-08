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
    @GeneratedValue
    private UUID id;


    @ManyToOne(optional = false)
    private User requesterDoctor;


    @ManyToOne(optional = false)
    private User patient;


    @Enumerated(EnumType.STRING)
    private RequestStatus status;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;


    public enum RequestStatus { PENDING, APPROVED,EXPIRED, DENIED, CANCELLED }
}