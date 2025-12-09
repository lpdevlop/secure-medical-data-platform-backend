package com.policy.mis.lasith.healthcarepatientportal.database.repository;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.MedicalData;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.User;
import com.policy.mis.lasith.healthcarepatientportal.database.enums.MedicalDataType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MedicalDataRepository extends JpaRepository<MedicalData,Long> {



    List<MedicalData> findByPatientAndType(User patient, MedicalDataType type);

}
