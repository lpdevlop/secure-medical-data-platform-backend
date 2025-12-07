package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String nic;
    private String password;
}