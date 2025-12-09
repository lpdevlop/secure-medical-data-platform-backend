package com.policy.mis.lasith.healthcarepatientportal.services;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.PatientProfileWithGrantInfoDTO;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessRequestRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository patientRepository;
    private final AccessRequestRepository accessRequestRepository;

    public List<PatientProfileWithGrantInfoDTO> getPatientProfile(String doctorId) {

        User doctor =patientRepository.findBySecureId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<AccessRequest> grants = accessRequestRepository.findAccessGrantByRequesterDoctorAndActiveTrue(doctor);

        return grants.stream()
                .map(grant -> { grant.getPatient();
                    return PatientProfileWithGrantInfoDTO.builder()
                            .patientSecureId(grant.getPatient().getSecureId())
                            .fullName(grant.getPatient().getFullName())
                            .email(grant.getPatient().getEmail())
                            .phone(grant.getPatient().getPhoneNumber())
                            .address(grant.getPatient().getAddress())
                            .accessGranted(true)
                            .accessExpiresAt(grant.getGrantExpiresAt().toString())
                            .build();
                })
                .toList();
    }
}
