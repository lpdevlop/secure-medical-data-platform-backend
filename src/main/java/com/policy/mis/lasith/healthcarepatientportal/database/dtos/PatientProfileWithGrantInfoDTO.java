package com.policy.mis.lasith.healthcarepatientportal.database.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientProfileWithGrantInfoDTO {
    private String patientSecureId;
    private String fullName;
    private String email;
    private String phone;
    private String address;

    private boolean accessGranted;      // true if doctor has access
    private String accessExpiresAt;     // expiration timestamp
}