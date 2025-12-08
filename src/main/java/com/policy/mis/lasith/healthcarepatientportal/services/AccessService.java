package com.policy.mis.lasith.healthcarepatientportal.services;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.AccessRequestDTO;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.AccessResponse;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.ApproveAccessDTO;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.GrantAccessResponse;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.enums.UserRoles;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessRequestRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccessService {


    private final UserRepository userRepository;

    private final AccessRequestRepository accessRequestRepository;

    public AccessService(UserRepository userRepository, AccessRequestRepository accessRequestRepository) {
        this.userRepository = userRepository;
        this.accessRequestRepository = accessRequestRepository;
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

}
