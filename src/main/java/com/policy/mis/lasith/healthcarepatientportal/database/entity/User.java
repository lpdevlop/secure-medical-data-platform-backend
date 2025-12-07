package com.policy.mis.lasith.healthcarepatientportal.database.entity;


import com.policy.mis.lasith.healthcarepatientportal.database.enums.UserRoles;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "user_model")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private UserRoles role;

    private boolean isEnable;

    private Instant createdAt;

    private Instant updatedAt;

}
