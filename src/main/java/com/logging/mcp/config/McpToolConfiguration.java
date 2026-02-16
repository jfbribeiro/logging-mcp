package com.logging.mcp.config;

import com.logging.mcp.tool.AuditToolService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpToolConfiguration {

    @Bean
    public ToolCallbackProvider auditToolCallbackProvider(AuditToolService auditToolService) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(auditToolService)
                .build();
    }
}
