package com.logging.mcp.service;

import com.logging.mcp.entity.AuditLog;
import com.logging.mcp.repository.AuditLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository repository;

    public AuditLogService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AuditLog save(String prompt, String username, String response) {
        AuditLog log = new AuditLog(prompt, username, response);
        return repository.save(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> queryLogs(String username, String searchTerm) {
        if (searchTerm != null && !searchTerm.isBlank()) {
            return repository.searchLogs(username, searchTerm);
        }
        return repository.findByUsernameOrderByTimestampDesc(username);
    }

    @Transactional(readOnly = true)
    public long getInteractionCount(String username) {
        return repository.countByUsername(username);
    }
}
