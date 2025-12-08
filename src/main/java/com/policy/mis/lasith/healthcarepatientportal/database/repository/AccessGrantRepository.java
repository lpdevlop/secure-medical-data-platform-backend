package com.policy.mis.lasith.healthcarepatientportal.database.repository;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessGrant;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessGrantRepository extends JpaRepository<AccessGrant,Long> {


    List<AccessGrant> findByDoctor_SecureIdAndActiveTrueAndIsExpiredFalseAndExpiresAtAfter(
            String doctorSecureId, Instant now
    );

    List<AccessGrant> findAccessGrantByDoctorAndActiveTrue(User doctor);

    Optional<AccessGrant> findById(UUID fromString);

    List<AccessGrant> findAccessGrantByPatientAndActiveTrue(User patientId);

}
