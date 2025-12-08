package com.policy.mis.lasith.healthcarepatientportal.services;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.PatientProfileWithGrantInfoDTO;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessGrant;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessGrantRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository patientRepository;
    private final AccessGrantRepository grantRepository;

    public List<PatientProfileWithGrantInfoDTO> getPatientProfile(String doctorId) {

        List<AccessGrant> grants = grantRepository
                .findByDoctor_SecureIdAndActiveTrueAndIsExpiredFalseAndExpiresAtAfter(
                        doctorId,
                        Instant.now()
                );

        return grants.stream()
                .map(grant -> { grant.getPatient();
                    return PatientProfileWithGrantInfoDTO.builder()
                            .patientSecureId(grant.getPatient().getSecureId())
                            .fullName(grant.getPatient().getFullName())
                            .email(grant.getPatient().getEmail())
                            .phone(grant.getPatient().getPhoneNumber())
                            .address(grant.getPatient().getAddress())
                            .accessGranted(true)
                            .accessExpiresAt(grant.getExpiresAt().toString())
                            .build();
                })
                .toList();
    }
}
