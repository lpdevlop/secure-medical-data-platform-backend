package com.policy.mis.lasith.healthcarepatientportal.auditconfig;

import com.policy.mis.lasith.healthcarepatientportal.database.entity.AuditLog;
import com.policy.mis.lasith.healthcarepatientportal.database.entity.EncryptedBackupsLogs;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.AuditLogRepository;
import com.policy.mis.lasith.healthcarepatientportal.database.repository.EncryptedLogsRepository;
import com.policy.mis.lasith.healthcarepatientportal.util.EncryptionUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.Arrays;

@Aspect
@Component
public class AuditAspect {

    @Autowired
    private AuditLogRepository auditRepo;

    private final EncryptedLogsRepository encryptedLogsRepository;

    public AuditAspect(EncryptedLogsRepository encryptedLogsRepository) {
        this.encryptedLogsRepository = encryptedLogsRepository;
    }

    @AfterReturning(
            pointcut = "execution(* com.policy.mis.lasith.healthcarepatientportal.services..*(..))",
            returning = "returnedObj"
    )
    public void logCreateOrUpdate(JoinPoint jp, Object returnedObj) {
        if (returnedObj == null) return;

        String methodName = jp.getSignature().getName().toLowerCase();

        if (
                methodName.startsWith("create") ||
                methodName.startsWith("save") ||
                methodName.startsWith("update") ||
                methodName.startsWith("approve") ||
                methodName.startsWith("revoke") ||
                methodName.startsWith("register") ||
                methodName.startsWith("add")||
                methodName.startsWith("add") ||
        methodName.startsWith("addMedicalData") ||
        methodName.startsWith("getMedicalHistory") ||
        methodName.startsWith("getRecordList") ||
        methodName.startsWith("getMyMedicalRecords") ||
        methodName.startsWith("getMyMedicalHistory")
        ) {

            AuditLog log = new AuditLog();
            log.setAction("CREATE/UPDATE");
            log.setEntityName(returnedObj.getClass().getSimpleName());
            log.setEntityId(extractId(returnedObj));
            log.setNewValue(safeToString(returnedObj));
            log.setUsername(getCurrentUser());
            String entityId = extractId(returnedObj);

            auditRepo.save(log);
            saveEncryptedLog(entityId, "CREATE/UPDATE", returnedObj);

        }
    }


    @AfterReturning(
            pointcut = "execution(* com.policy.mis.lasith.healthcarepatientportal.services..delete*(..)) && args(id)",
            returning = "returnedObj"
    )
    public void logDelete(Long id, Object returnedObj) {
        AuditLog log = new AuditLog();
        log.setAction("DELETE");
        log.setEntityName("Entity");
        log.setEntityId(String.valueOf(id));
        log.setUsername(getCurrentUser());

        auditRepo.save(log);
        saveEncryptedLog(String.valueOf(id), "DELETE", null);

    }


    @AfterReturning(
            pointcut = "execution(* com.policy.mis.lasith.healthcarepatientportal.services.LoginService.login(..))",
            returning = "loginResponse"
    )
    public void logLoginAttempt(JoinPoint jp, Object loginResponse) {
        AuditLog log = new AuditLog();
        log.setAction("LOGIN");
        log.setEntityName("UserLogin");
        log.setEntityId(extractUsernameFromArgs(jp));
        log.setNewValue(safeToString(loginResponse));
        log.setUsername(getCurrentUser());

        auditRepo.save(log);
        saveEncryptedLog(extractUsernameFromArgs(jp), "LOGIN", loginResponse);

    }

    private String getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null) ? auth.getName() : "SYSTEM";
    }

    private String extractId(Object entity) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            return String.valueOf(idField.get(entity));
        } catch (NoSuchFieldException e) {
            try {
                Field secureIdField = entity.getClass().getDeclaredField("secureId");
                secureIdField.setAccessible(true);
                return String.valueOf(secureIdField.get(entity));
            } catch (Exception ex) {
                return "UNKNOWN";
            }
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    private String extractUsernameFromArgs(JoinPoint jp) {
        return Arrays.stream(jp.getArgs())
                .filter(arg -> arg != null && arg.getClass().getSimpleName().equals("LoginRequest"))
                .findFirst()
                .map(arg -> {
                    try {
                        Field nic = arg.getClass().getDeclaredField("nic");
                        nic.setAccessible(true);
                        return String.valueOf(nic.get(arg));
                    } catch (Exception e) {
                        return "UNKNOWN_USER";
                    }
                }).orElse("UNKNOWN_USER");
    }

    private String safeToString(Object obj) {
        try {
            return obj.toString();
        } catch (Exception e) {
            return "UNAVAILABLE";
        }
    }

    private void saveEncryptedLog(String secureId, String action, Object entity) {
        try {
            String entityData = safeToString(entity);
            String encryptedData = EncryptionUtil.encrypt(entityData);
            String previousHash = encryptedLogsRepository.findTopByOrderByIdDesc().map(EncryptedBackupsLogs::getCurrentHash).orElse("0");
            EncryptedBackupsLogs log = new EncryptedBackupsLogs();
            log.setSecureId(secureId);
            log.setAction(action);
            log.setEncryptedData(encryptedData);
            log.computeHash(previousHash);
            encryptedLogsRepository.save(log);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
