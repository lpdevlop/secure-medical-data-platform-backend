package com.policy.mis.lasith.healthcarepatientportal.controllers;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.EncryptedBackupsLogs;
import com.policy.mis.lasith.healthcarepatientportal.services.EncryptedLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final EncryptedLogService auditLogService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
    public ResponseEntity<EncryptedBackupsLogs> getAuditLogById(@PathVariable Long id) throws Exception {
        EncryptedBackupsLogs log = auditLogService.getLog(id);
        return ResponseEntity.ok(log);
    }

    @GetMapping("/getall")
    @PreAuthorize("hasAnyRole('AUDITOR')")
    public ResponseEntity< List<EncryptedBackupsLogs> > getAll() throws Exception {
        List<EncryptedBackupsLogs> log = auditLogService.getAllLogs();
        return ResponseEntity.ok(log);
    }
}