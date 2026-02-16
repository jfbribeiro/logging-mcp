# 📋 Audit Logging MCP Server

A **Model Context Protocol (MCP) server** that stores every LLM interaction in PostgreSQL for auditing and compliance tracking. Built with Spring Boot, it lets any MCP-compatible LLM client log prompts, responses, and usernames — then query them later.

---

## ✨ Features

- **🔧 3 MCP Tools** exposed to LLM clients for logging and querying interactions
- **🐘 PostgreSQL** persistence with Flyway-managed schema migrations
- **🌐 SSE Transport** — connect via HTTP Server-Sent Events at `/mcp/messages`
- **🐳 Docker-ready** — spin up the database with a single command

---

## 🛠️ MCP Tools

These are the tools that LLM clients (like Claude Desktop) can call:

| Tool | Description |
|------|-------------|
| **logInteraction** | Save a prompt, username, and response to the audit database |
| **queryAuditLogs** | Search logs by username and optional keyword across prompts/responses |
| **getAuditStats** | Get the total interaction count for a given user |

---

## 🗄️ Database Schema

A single `audit_log` table with indexes on `username` and `timestamp`:

| Column | Type | Description |
|--------|------|-------------|
| `id` | `BIGSERIAL` | Auto-incrementing primary key |
| `prompt` | `TEXT` | The user's prompt sent to the LLM |
| `username` | `VARCHAR(255)` | Who made the request |
| `timestamp` | `TIMESTAMP` | When the interaction occurred |
| `response` | `TEXT` | The LLM's response |

---

## 🚀 Getting Started

### Prerequisites

- Java 21
- Maven
- Docker

### 1. Start the database

```bash
docker compose up -d
```

### 2. Run the server

```bash
mvn spring-boot:run
```

The MCP server will start on **port 8080** with the SSE endpoint available at `/mcp/messages`.

---

## ⚙️ Configuration

Default settings in `application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/audit_db
    username: postgres
    password: postgres
  ai:
    mcp:
      server:
        name: audit-logging-mcp
        type: SYNC
        sse-message-endpoint: /mcp/messages
```

---

## 🏗️ Project Structure

```
src/main/java/com/logging/mcp/
├── config/          # MCP tool registration
├── entity/          # JPA entity (AuditLog)
├── repository/      # Spring Data JPA repository
├── service/         # Business logic
└── tool/            # MCP tool definitions (@Tool annotated)
```

---

## 🧰 Tech Stack

- **Spring Boot 3.4.1** + Java 21
- **Spring AI MCP** (1.0.0-M6) — STDIO & SSE transports
- **Spring Data JPA** + **Flyway** — persistence & migrations
- **PostgreSQL 17** — via Docker Compose
