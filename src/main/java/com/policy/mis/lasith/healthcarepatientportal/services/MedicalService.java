package com.policy.mis.lasith.healthcarepatientportal.services;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalDataRequestDTO;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalHistoryWithGrantInfo;
import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalRecordsWithGrantInfo;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.AccessRequest;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.MedicalData;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.enums.MedicalDataType;
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


    public MedicalService(MedicalDataRepository medicalDataRepository, UserRepository userRepository, AccessRequestRepository accessRequestRepository) {
        this.medicalDataRepository = medicalDataRepository;
        this.userRepository = userRepository;
        this.accessRequestRepository = accessRequestRepository;
    }

    public MedicalData addMedicalData(MedicalDataRequestDTO dto) {
        User patient = userRepository.findBySecureId(dto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        MedicalData data = MedicalData.builder()
                .patient(patient)
                .name(dto.getName())
                .docType(dto.getDocType())
                .type(dto.getType())
                .prescriptionNote(dto.getPrescriptionNote())
                .pdfData(dto.getPdfData())
                .build();

        return medicalDataRepository.save(data);
    }

    public List<MedicalHistoryWithGrantInfo> getMedicalHistory(String doctorId) {
        User doctor =userRepository.findBySecureId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        List<AccessRequest> accessGrants =accessRequestRepository.findAccessGrantByRequesterDoctorAndActiveTrue(doctor);

        return accessGrants.stream()
                .flatMap(grant -> medicalDataRepository
                        .findByPatientAndType(grant.getPatient(), MedicalDataType.HISTORY)
                        .stream()
                        .map(medical -> new MedicalHistoryWithGrantInfo(
                                medical.getPatient().getSecureId(),
                                medical.getName(),
                                medical.getMedicalSecureId(),
                                "Granted",
                                grant.getGrantExpiresAt()
                        ))
                )
                .toList();
    }

    public List<MedicalRecordsWithGrantInfo> getRecordList(String doctorId) {
     User user= userRepository.findBySecureId(doctorId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        List<AccessRequest> accessGrants =accessRequestRepository.findAccessGrantByRequesterDoctorAndActiveTrue(user);

        return accessGrants.stream()
                .flatMap(grant -> medicalDataRepository
                        .findByPatientAndType(grant.getPatient(), MedicalDataType.RECORD)
                        .stream()
                        .map(medical -> new MedicalRecordsWithGrantInfo(
                                medical.getPatient().getSecureId(),
                                medical.getName(),
                                medical.getMedicalSecureId(),
                                "Granted",
                                grant.getGrantExpiresAt()
                        ))
                )
                .toList();
    }

    public List<MedicalRecordsWithGrantInfo> getMyMedicalRecords(String patientId) {
        User user=userRepository.findBySecureId(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return medicalDataRepository.findByPatientAndType(user, MedicalDataType.RECORD).stream().map(
                medical -> new MedicalRecordsWithGrantInfo(
                        medical.getPatient().getSecureId(),
                        medical.getName(),
                        medical.getMedicalSecureId(),
                        "Granted",
                        null
        )).toList();
    }


    public List<MedicalHistoryWithGrantInfo> getMyMedicalHistory(String doctorId) {
        User user=userRepository.findBySecureId(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        return  medicalDataRepository
                        .findByPatientAndType(user, MedicalDataType.HISTORY)
                        .stream()
                        .map(medical -> new MedicalHistoryWithGrantInfo(
                                medical.getPatient().getSecureId(),
                                medical.getName(),
                                medical.getMedicalSecureId(),
                                "Granted",
                                null
                        )).toList();
    }
}
