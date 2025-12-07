package com.policy.mis.lasith.healthcarepatientportal.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "docter")
@Getter
@Setter
public class DoctorProfile {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private boolean isEnable;


}
