package com.policy.mis.lasith.healthcarepatientportal.database.repository;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessRequestRepository extends JpaRepository<AccessRequest,Long> {


    Optional<AccessRequest> findByRequesterDoctorAndPatient(User doctor, User patient);

    List<AccessRequest> findAccessRequestByPatient(User patient);

    List<AccessRequest> findAccessGrantByRequesterDoctorAndActiveTrue(User doctor);

    Optional<AccessRequest> findAccessRequestByAccessRequestsecureId(String secureId);
}
