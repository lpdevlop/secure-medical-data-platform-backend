package com.policy.mis.lasith.healthcarepatientportal.database.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "appointments")
public class AppointmentsModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


}
