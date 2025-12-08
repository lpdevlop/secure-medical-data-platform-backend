package com.policy.mis.lasith.healthcarepatientportal.services;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalDocumentResponse;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalHistoryWithGrantInfo;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessGrant;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessGrantRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessRequestRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.MedicalDataRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;

@Service
public class MedicalService {

    private final MedicalDataRepository medicalDataRepository;

    private final UserRepository userRepository;

    private final AccessRequestRepository accessRequestRepository;

    private final AccessGrantRepository accessGrantRepository;

    public MedicalService(MedicalDataRepository medicalDataRepository, UserRepository userRepository, AccessRequestRepository accessRequestRepository, AccessGrantRepository accessGrantRepository) {
        this.medicalDataRepository = medicalDataRepository;
        this.userRepository = userRepository;
        this.accessRequestRepository = accessRequestRepository;
        this.accessGrantRepository = accessGrantRepository;
    }


    public List<MedicalHistoryWithGrantInfo> getMedicalHistory(String doctorId) {
         userRepository.findBySecureId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<AccessGrant> accessGrants =accessGrantRepository.findByDoctor_SecureIdAndActiveTrueAndIsExpiredFalseAndExpiresAtAfter(doctorId,Instant.now());

        return accessGrants.stream()
                .flatMap(grant -> medicalDataRepository
                        .findByPatientAndType(grant.getPatient(), "HISTORY")
                        .stream()
                        .map(medical -> new MedicalHistoryWithGrantInfo(
                                medical.getId(),
                                medical.getPatient().getSecureId(),
                                medical.getType(),
                                "Granted",
                                medical.getCreatedAt(),
                                grant.getExpiresAt()
                        ))
                )
                .toList();
    }

    public List<MedicalDocumentResponse> getPrescriptionHistory(Long patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return medicalDataRepository.findByPatientAndType(patient, "PRESCRIPTION")
                .stream()
                .map(MedicalDocumentResponse::fromEntity)
                .toList();
    }

}
