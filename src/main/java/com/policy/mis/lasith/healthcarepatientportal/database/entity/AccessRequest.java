package com.policy.mis.lasith.healthcarepatientportal.database.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "access_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccessRequest {
    @Id
    @GeneratedValue
    private UUID id;


    @ManyToOne(optional = false)
    private User requester;


    @ManyToOne(optional = false)
    private User patient;


    private String reason;


    @Enumerated(EnumType.STRING)
    private RequestStatus status;


    private Instant requestedAt;
    private Instant respondedAt;

    public enum RequestStatus { PENDING, APPROVED, DENIED, CANCELLED }
}