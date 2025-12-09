package com.policy.mis.lasith.healthcarepatientportal.services;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.EncryptedBackupsLogs;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.EncryptedLogsRepository;
import com.policy.mis.lasith.healthcarepatientportal.util.EncryptionUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EncryptedLogService {

    private final EncryptedLogsRepository logsRepository;

    public EncryptedLogService(EncryptedLogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
    public EncryptedBackupsLogs getLog(Long id) throws Exception {
        EncryptedBackupsLogs log = logsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Log not found"));
        log.setEncryptedData(EncryptionUtil.decrypt(log.getEncryptedData()));
        return log;
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('AUDITOR')")
    public List<EncryptedBackupsLogs> getAllLogs() {

        List<EncryptedBackupsLogs> logs = logsRepository.findAll();

        logs.forEach(log -> {
            try {
                log.setEncryptedData(EncryptionUtil.decrypt(log.getEncryptedData()));
            } catch (Exception e) {
                throw new RuntimeException("Failed to decrypt log entry ID: " + log.getId());
            }
        });

        return logs;
    }
}
