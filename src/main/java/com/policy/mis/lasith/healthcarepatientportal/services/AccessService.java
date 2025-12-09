package com.policy.mis.lasith.healthcarepatientportal.services;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.*;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.enums.UserRoles;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessRequestRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
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
                .requestId(request.getAccessRequestsecureId())
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

        Optional<AccessRequest> request = accessRequestRepository.findAccessRequestByAccessRequestsecureId(dto.getDoctorId());


        if (!request.get().getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You cannot approve someone else's access request.");
        }

        if (request.get().getStatus() != AccessRequest.RequestStatus.PENDING) {
            throw new RuntimeException("This request is already processed.");
        }


        AccessRequest accessGrant=new AccessRequest();

        accessGrant.setActive(true);
        accessGrant.setGrantExpiresAt(LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC));
        accessGrant.setGrantStartAt(Instant.now());
        accessGrant.setExpired(true);
        accessGrant.setStatus(AccessRequest.RequestStatus.APPROVED);

        accessRequestRepository.save(request.get());

        return GrantAccessResponse.builder()
                .requestId(request.get().getAccessRequestsecureId())
                .doctorName(request.get().getRequesterDoctor().getFullName())
                .patientName(request.get().getPatient().getFullName())
                .status(request.get().getStatus().name())
                .expiresAt(accessGrant.getGrantExpiresAt())
                .build();

    }



    public AccessRequest revokeConsent(ApproveAccessDTO payload) {

        User patient = userRepository.findBySecureId(payload.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getRole().equals(UserRoles.PATIENT.name())) {
            throw new RuntimeException("User is not authorized as PATIENT");
        }

        Optional<AccessRequest> request = accessRequestRepository.findAccessRequestByAccessRequestsecureId(payload.getDoctorId());

        if (!request.get().getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You cannot approve someone else's access request.");
        }
        AccessRequest accessRequest=new AccessRequest();
        accessRequest.setActive(false);
        accessRequest.setExpired(true);
        accessRequest.setGrantExpiresAt(Instant.now());
        return accessRequestRepository.save(accessRequest);
    }

    public List<ConsentDTO> getActiveConsents(String patientId) {
        User patient = userRepository.findBySecureId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<AccessRequest> accessRequests = accessRequestRepository.findAccessRequestByPatient(patient);

        List<ConsentDTO> consentList = new ArrayList<>();

        for (AccessRequest request : accessRequests) {
            consentList.add(new ConsentDTO(
                    request.getAccessRequestsecureId(),
                    request.getRequesterDoctor().getFullName(),
                    request.getRequesterDoctor().getId().toString(),
                    request.getStatus().name(),
                    null,
                    null,
                    request.getCreatedAt()
            ));
        }

        return consentList;
    }

}
