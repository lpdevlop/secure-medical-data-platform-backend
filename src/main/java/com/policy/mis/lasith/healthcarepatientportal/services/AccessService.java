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

import java.time.Duration;
import java.time.Instant;
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
                .orElseThrow(() -> new RuntimeException("Invalid patient secure ID"));

        AccessRequest request = AccessRequest.builder()
                .requester(doctor)
                .patient(patient)
                .reason(accessRequest.getReason())
                .status(AccessRequest.RequestStatus.PENDING)
                .requestedAt(Instant.now())
                .build();

        accessRequestRepository.save(request);

        return AccessResponse.builder()
                .requestId(request.getId())
                .doctorName(doctor.getFullName())
                .patientName(patient.getFullName())
                .status(request.getStatus().name())
                .requestedAt(request.getRequestedAt())
                .build();

    }

    public GrantAccessResponse approveAccess(ApproveAccessDTO dto) {

        User patient = userRepository.findBySecureId(dto.getId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getRole().equals(UserRoles.PATIENT)) {
            throw new RuntimeException("User is not authorized as PATIENT");
        }

        Optional<AccessRequest> request = accessRequestRepository.findById(UUID.fromString(dto.getRequestId()));


        if (!request.get().getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You cannot approve someone else's access request.");
        }

        if (request.get().getStatus() != AccessRequest.RequestStatus.PENDING) {
            throw new RuntimeException("This request is already processed.");
        }

        Instant now = Instant.now();
        Instant expires = now.plusSeconds(dto.getAccessDurationMinutes() * 60);

        request.get().setStatus(AccessRequest.RequestStatus.APPROVED);
        request.get().setRespondedAt(now);

        accessRequestRepository.save(request.get());

        return GrantAccessResponse.builder()
                .requestId(request.get().getId())
                .doctorName(request.get().getRequester().getFullName())
                .patientName(request.get().getPatient().getFullName())
                .status(request.get().getStatus().name())
                .approvedAt(now)
                .expiresAt(expires)
                .build();
    }


    public AccessGrant grantConsent(String patientId, ConsentGrantPayload payload) {
        User patient = userRepository.findBySecureId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        User doctor = userRepository.findBySecureId(payload.getDoctorSecureId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        AccessGrant grant = new AccessGrant();
        grant.setDoctor(doctor);
        grant.setPatient(patient);
        grant.setStartAt(Instant.now());
        grant.setExpiresAt(Instant.now().plus(Duration.ofHours(1)));
        grant.setActive(true);
        grant.setExpired(true);


        return accessGrantRepository.save(grant);
    }

    public AccessGrant revokeConsent(String patientId, ConsentGrantPayload payload) {
        User patient = userRepository.findBySecureId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
        User doctor = userRepository.findBySecureId(payload.getDoctorSecureId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        AccessGrant grant = accessGrantRepository.findByPatientAndDoctorAndActive(
                        doctor.getSecureId(), patient.getSecureId())
                .orElseThrow(() -> new RuntimeException("Active grant not found"));

        grant.setActive(false);
        grant.setExpired(true);
        grant.setExpiresAt(Instant.now());
        grant.setRevokeTime(Instant.now());

        return accessGrantRepository.save(grant);
    }

    public List<AccessGrant> getActiveConsents(String patientId) {

        return accessGrantRepository.findByActive(patientId);
    }
}
