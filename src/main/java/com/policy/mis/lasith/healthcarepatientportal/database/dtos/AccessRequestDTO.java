package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessRequestDTO {

    private String patientSecureId;
    private String reason;
    private String doctorId;
}
