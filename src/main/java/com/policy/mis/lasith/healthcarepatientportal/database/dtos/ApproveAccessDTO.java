package com.policy.mis.lasith.healthcarepatientportal.database.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveAccessDTO {

    private Long doctorId;
    private String patientId;
    private String itemId;
}