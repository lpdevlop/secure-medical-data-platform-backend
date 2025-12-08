package com.policy.mis.lasith.healthcarepatientportal.database.repository;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessGrant;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccessGrantRepository extends JpaRepository<AccessGrant,Long> {

    boolean existsByPatientAndActiveTrueAndExpiresAtAfter(User patient, Instant now);

    List<AccessGrant> findByDoctor_SecureIdAndActiveTrueAndIsExpiredFalseAndExpiresAtAfter(
            String doctorSecureId, Instant now
    );

    Optional<AccessGrant> findByPatientAndDoctorAndActive(String doctor, String patientId);

    List<AccessGrant> findByActive(String patientId);
}
