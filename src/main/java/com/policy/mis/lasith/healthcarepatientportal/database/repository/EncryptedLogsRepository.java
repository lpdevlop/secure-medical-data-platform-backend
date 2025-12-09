package com.policy.mis.lasith.healthcarepatientportal.database.repository;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.EncryptedBackupsLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EncryptedLogsRepository extends JpaRepository<EncryptedBackupsLogs,Long> {

    Optional<EncryptedBackupsLogs> findTopByOrderByIdDesc ();
}
