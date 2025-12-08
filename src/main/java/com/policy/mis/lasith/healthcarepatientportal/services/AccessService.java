package com.policy.mis.lasith.healthcarepatientportal.services;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.*;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessGrant;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.enums.UserRoles;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessGrantRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessRequestRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccessService {


    private final UserRepository userRepository;

    private final AccessRequestRepository accessRequestRepository;

    private final AccessGrantRepository accessGrantRepository;

    public AccessService(UserRepository userRepository, AccessRequestRepository accessRequestRepository, AccessGrantRepository accessGrantRepository) {
        this.userRepository = userRepository;
        this.accessRequestRepository = accessRequestRepository;
        this.accessGrantRepository = accessGrantRepository;
    }


    public AccessResponse createAccessRequest(AccessRequestDTO accessRequest) {

        User doctor = userRepository.findBySecureId(accessRequest.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if (!doctor.getRole().equals(UserRoles.DOCTOR.name())) {
            throw new RuntimeException("User is not authorized as DOCTOR");
        }

        User patient = userRepository.findBySecureId(accessRequest.getPatientSecureId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        if(accessRequestRepository.findByRequesterDoctorAndPatient(doctor,patient).isPresent()){
            throw new RuntimeException("Doctor not found");
        }


        AccessRequest request = AccessRequest.builder()
                .requesterDoctor(doctor)
                .patient(patient)
                .status(AccessRequest.RequestStatus.PENDING)
                .build();

        accessRequestRepository.save(request);

        return AccessResponse.builder()
                .requestId(request.getId())
                .doctorName(doctor.getFullName())
                .patientName(patient.getFullName())
                .status(request.getStatus().name())
                .build();

    }

    public GrantAccessResponse approveAccess(ApproveAccessDTO dto) {

        User patient = userRepository.findBySecureId(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getRole().equals(UserRoles.PATIENT.name())) {
            throw new RuntimeException("User is not authorized as PATIENT");
        }

        Optional<AccessRequest> request = accessRequestRepository.findById(UUID.fromString(dto.getRequestedId()));


        if (!request.get().getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You cannot approve someone else's access request.");
        }

        if (request.get().getStatus() != AccessRequest.RequestStatus.PENDING) {
            throw new RuntimeException("This request is already processed.");
        }

        request.get().setStatus(AccessRequest.RequestStatus.APPROVED);

        accessRequestRepository.save(request.get());

        AccessGrant accessGrant=new AccessGrant();

        User patientaprroved=new User();
        patientaprroved.setId(patient.getId());
        User doctorRequested=new User();
        doctorRequested.setId(request.get().getRequesterDoctor().getId());

        accessGrant.setPatient(patientaprroved);
        accessGrant.setDoctor(doctorRequested);
        accessGrant.setActive(true);
        accessGrant.setExpiresAt(LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC));
        accessGrant.setStartAt(Instant.now());

        accessGrantRepository.save(accessGrant);

       return GrantAccessResponse.builder()
                .requestId(request.get().getId())
                .doctorName(request.get().getRequesterDoctor().getFullName())
                .patientName(request.get().getPatient().getFullName())
                .status(request.get().getStatus().name())
                .expiresAt(accessGrant.getExpiresAt())
                .build();

    }



    public AccessGrant revokeConsent(ApproveAccessDTO payload) {

        User patient = userRepository.findBySecureId(payload.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getRole().equals(UserRoles.PATIENT.name())) {
            throw new RuntimeException("User is not authorized as PATIENT");
        }

        Optional<AccessGrant> request = accessGrantRepository.findById(UUID.fromString(payload.getRequestedId()));

        if (!request.get().getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You cannot approve someone else's access request.");
        }

        AccessGrant accessGrant=new AccessGrant();

        accessGrant.setId(request.get().getId());
        accessGrant.setActive(false);
        accessGrant.setExpired(true);
        accessGrant.setExpiresAt(Instant.now());

        return accessGrantRepository.save(accessGrant);
    }

    public List<AccessGrant> getActiveConsents(String patientId) {
        User patient = userRepository.findBySecureId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        return accessGrantRepository.findAccessGrantByPatientAndActiveTrue(patient);
    }
}
