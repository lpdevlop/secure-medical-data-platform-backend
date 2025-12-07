package com.policy.mis.lasith.healthcarepatientportal.services;

import com.policy.mis.lasith.healthcarepatientportal.database.dtos.MedicalDocumentResponse;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.MedicalDataRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MedicalService {

    private final MedicalDataRepository medicalDataRepository;

    private final UserRepository userRepository;

    public MedicalService(MedicalDataRepository medicalDataRepository, UserRepository userRepository) {
        this.medicalDataRepository = medicalDataRepository;
        this.userRepository = userRepository;
    }


    public List<MedicalDocumentResponse> getMedicalHistory(UUID patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return medicalDataRepository.findByPatientAndType(patient, "HISTORY")
                .stream()
                .map(MedicalDocumentResponse::fromEntity)
                .toList();
    }

    public List<MedicalDocumentResponse> getPrescriptionHistory(UUID patientId) {
        User patient = userRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        return medicalDataRepository.findByPatientAndType(patient, "PRESCRIPTION")
                .stream()
                .map(MedicalDocumentResponse::fromEntity)
                .toList();
    }

}
