package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String fullName;
    private String role;
}