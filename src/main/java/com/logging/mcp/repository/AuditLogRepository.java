package com.logging.mcp.repository;

import com.logging.mcp.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUsernameOrderByTimestampDesc(String username);

    @Query("SELECT a FROM AuditLog a WHERE a.username = :username " +
           "OR LOWER(a.prompt) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "OR LOWER(a.response) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
           "ORDER BY a.timestamp DESC")
    List<AuditLog> searchLogs(@Param("username") String username,
                              @Param("searchTerm") String searchTerm);

    long countByUsername(String username);
}
