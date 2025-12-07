package com.policy.mis.lasith.healthcarepatientportal.database.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveAccessDTO {

    private String requestId;
    private String Id;
    private Long accessDurationMinutes;

}
