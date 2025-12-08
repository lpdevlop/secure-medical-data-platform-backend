package com.policy.mis.lasith.healthcarepatientportal.database.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveAccessDTO {

    private String requestedId;
    private String patientId;

}
