package com.policy.mis.lasith.healthcarepatientportal.database.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "access_grant")
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

    private boolean active = true;


    private String scopeJson;


}