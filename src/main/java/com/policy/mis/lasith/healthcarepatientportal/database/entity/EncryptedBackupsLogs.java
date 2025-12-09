package com.policy.mis.lasith.healthcarepatientportal.database.entity;

import com.policy.mis.lasith.healthcarepatientportal.util.HashUtil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Table(name = "user_audit_log")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class EncryptedBackupsLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String secureId;
    private String action;
    private String encryptedData;

    private String previousHash;

    private String currentHash;

    @CreationTimestamp
    private Instant createdAt;


    public void computeHash(String previousHash) throws Exception {
        if (this.createdAt == null) {
            this.createdAt = Instant.now();
        }

        String data = secureId + action + encryptedData + createdAt.toString() + previousHash;
        this.currentHash = HashUtil.sha256(data);
        this.previousHash = previousHash;
    }
}
