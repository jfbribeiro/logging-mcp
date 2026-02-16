package com.logging.mcp.tool;

import com.logging.mcp.entity.AuditLog;
import com.logging.mcp.service.AuditLogService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditToolService {

    private final AuditLogService auditLogService;

    public AuditToolService(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @Tool(description = "Log an LLM interaction (prompt, username, response, and token usage) to the audit database for compliance tracking")
    public String logInteraction(
            @ToolParam(description = "The user's prompt or question sent to the LLM") String prompt,
            @ToolParam(description = "The username of the person who made the request") String username,
            @ToolParam(description = "The LLM's response to the prompt") String response,
            @ToolParam(description = "The number of tokens used by the LLM for this interaction", required = false) Integer tokens) {

        AuditLog saved = auditLogService.save(prompt, username, response, tokens);
        return "Logged interaction with id: " + saved.getId() + " for user: " + saved.getUsername();
    }

    @Tool(description = "Query audit logs by username and optional search term. Returns matching interactions ordered by most recent first")
    public List<AuditLog> queryAuditLogs(
            @ToolParam(description = "The username to filter logs by") String username,
            @ToolParam(description = "Optional search term to filter by prompt or response content", required = false) String searchTerm) {

        return auditLogService.queryLogs(username, searchTerm);
    }

    @Tool(description = "Get the total number of logged interactions for a specific user")
    public String getAuditStats(
            @ToolParam(description = "The username to get stats for") String username) {

        long count = auditLogService.getInteractionCount(username);
        return "User '" + username + "' has " + count + " logged interaction(s).";
    }
}
