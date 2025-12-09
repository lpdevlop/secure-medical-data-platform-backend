package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import com.policy.mis.lasith.healthcarepatientportal.database.enums.MedicalDataType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MedicalDataRequestDTO {
    private String medicalSecureId;
    private String patientId;       // UUID of the patient
    private String name;            // Document name
    private String docType;
    // e.g., PDF, Image
    private MedicalDataType type;   // Enum: BLOOD_TEST, XRAY, etc.
    private String prescriptionNote;
    private byte[] pdfData;         // If small file, or you can use MultipartFile for large files
}
