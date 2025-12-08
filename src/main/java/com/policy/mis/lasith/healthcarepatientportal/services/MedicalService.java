package com.policy.mis.lasith.healthcarepatientportal.services;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalDocumentResponse;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalHistoryWithGrantInfo;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalRecordsWithGrantInfo;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessGrant;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.enums.MedicalDataType;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessGrantRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AccessRequestRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.MedicalDataRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import org.springframework.stereotype.Service;
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
        User doctor =userRepository.findBySecureId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<AccessGrant> accessGrants =accessGrantRepository.findAccessGrantByDoctorAndActiveTrue(doctor);

        return accessGrants.stream()
                .flatMap(grant -> medicalDataRepository
                        .findByPatientAndType(grant.getPatient(), MedicalDataType.HISTORY.name())
                        .stream()
                        .map(medical -> new MedicalHistoryWithGrantInfo(
                                medical.getPatient().getSecureId(),
                                medical.getName(),
                                medical.getId(),
                                "Granted",
                                grant.getExpiresAt()
                        ))
                )
                .toList();
    }

    public List<MedicalRecordsWithGrantInfo> getPrescriptionHistory(String doctorId) {
     User user= userRepository.findBySecureId(doctorId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<AccessGrant> accessGrants =accessGrantRepository.findAccessGrantByDoctorAndActiveTrue(user);

        return accessGrants.stream()
                .flatMap(grant -> medicalDataRepository
                        .findByPatientAndType(grant.getPatient(), MedicalDataType.RECORD.name())
                        .stream()
                        .map(medical -> new MedicalRecordsWithGrantInfo(
                                medical.getPatient().getSecureId(),
                                medical.getName(),
                                medical.getId(),
                                "Granted",
                                grant.getExpiresAt()
                        ))
                )
                .toList();
    }

    public List<MedicalRecordsWithGrantInfo> getMyMedicalRecords(String patientId) {
        User user=userRepository.findBySecureId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return medicalDataRepository.findByPatientAndType(user, "HISTORY").stream().map(
                medical -> new MedicalRecordsWithGrantInfo(
                        medical.getPatient().getSecureId(),
                        medical.getName(),
                        medical.getId(),
                        "Granted",
                        null
        )).toList();
    }


    public List<MedicalHistoryWithGrantInfo> getMyMedicalHistory(String doctorId) {
        User user=userRepository.findBySecureId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return  medicalDataRepository
                        .findByPatientAndType(user, "HISTORY")
                        .stream()
                        .map(medical -> new MedicalHistoryWithGrantInfo(
                                medical.getPatient().getSecureId(),
                                medical.getName(),
                                medical.getId(),
                                "Granted",
                                null
                        )).toList();
    }
}
