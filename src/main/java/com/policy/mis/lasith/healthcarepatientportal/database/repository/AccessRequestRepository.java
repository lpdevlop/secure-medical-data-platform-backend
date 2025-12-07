package com.policy.mis.lasith.healthcarepatientportal.database.repository;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessRequestRepository extends JpaRepository<AccessRequest,Long> {

    Optional<AccessRequest> findById(UUID fromString);
}
