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

        if(accessRequestRepository.findByRequesterDoctorAndPatientAndRevokedFalse(doctor,patient).isPresent()){
            throw new RuntimeException("Doctor not found");
        }


        AccessRequest request = AccessRequest.builder()
                .requesterDoctor(doctor)
                .patient(patient)
                .accessRequestsecureId(generateRandomARCode())
                .active(false)
                .isExpired(false)
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
        User doctor = userRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));


        Optional<AccessRequest> request = accessRequestRepository.findAccessRequestByRequesterDoctorAndAccessRequestsecureId(doctor,dto.getItemId());


        if (!request.get().getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You cannot approve someone else's access request.");
        }

        if (request.get().getStatus() != AccessRequest.RequestStatus.PENDING) {
            throw new RuntimeException("This request is already processed.");
        }


        request.get().setActive(true);
        request.get().setGrantExpiresAt(LocalDateTime.now().plusHours(1).toInstant(ZoneOffset.UTC));
        request.get().setGrantStartAt(Instant.now());
        request.get().setExpired(true);
        request.get().setStatus(AccessRequest.RequestStatus.GRANTED);

        accessRequestRepository.save(request.get());

        return GrantAccessResponse.builder()
                .requestId(request.get().getAccessRequestsecureId())
                .doctorName(request.get().getRequesterDoctor().getFullName())
                .patientName(request.get().getPatient().getFullName())
                .status(request.get().getStatus().name())
                .expiresAt(request.get().getGrantExpiresAt())
                .build();

    }



    public AccessRequest revokeConsent(ApproveAccessDTO payload) {

        User patient = userRepository.findBySecureId(payload.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        if (!patient.getRole().equals(UserRoles.PATIENT.name())) {
            throw new RuntimeException("User is not authorized as PATIENT");
        }
        User doctor = userRepository.findById(payload.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Optional<AccessRequest> request = accessRequestRepository.findAccessRequestByRequesterDoctorAndAccessRequestsecureId(doctor,payload.getItemId());

        if (!request.get().getPatient().getId().equals(patient.getId())) {
            throw new RuntimeException("You cannot approve someone else's access request.");
        }
        request.get().setActive(false);
        request.get().setExpired(true);
        request.get().setRevoked(true);
        request.get().setStatus(AccessRequest.RequestStatus.REVOKED);
        request.get().setGrantExpiresAt(Instant.now());
        return accessRequestRepository.save(request.get());
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
                    request.getCreatedAt(),
                    request.isRevoked()
            ));
        }

        return consentList;
    }
    public static String generateRandomARCode() {
        int num = (int)(Math.random() * 99999) + 1;
        return String.format("AR%05d", num);
    }
}
