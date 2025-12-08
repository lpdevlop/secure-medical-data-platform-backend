package com.policy.mis.lasith.healthcarepatientportal.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "access_grant")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class AccessGrant {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private User doctor;

    @ManyToOne(optional = false)
    private User patient;

    private Instant startAt;

    private Instant expiresAt;

    private boolean active ;

    private boolean isExpired;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;



}