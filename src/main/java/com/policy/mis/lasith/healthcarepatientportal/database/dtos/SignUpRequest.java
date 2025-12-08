package com.policy.mis.lasith.healthcarepatientportal.database.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    private String fullName;

    private String nic;

    private String password;

    private String role;

    private String dateOfBirth;

    private String gender;

    private String contactNumber;

    private String address;

    private String emergencyContact;
}