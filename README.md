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
| `tokens` | `INTEGER` | Number of tokens used by the LLM |

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

## 🤖 Usage with an LLM Client

### Claude Desktop Configuration

Add the following to your Claude Desktop config file (`claude_desktop_config.json`):

```json
{
  "mcpServers": {
    "audit-logging": {
      "url": "http://localhost:8080/logging-mcp/sse"
    }
  }
}
```

### System Prompt Instructions

Give the LLM the following instructions (e.g. in a system prompt or project instructions) so it knows to log every interaction:

```
You are connected to an audit logging MCP server. After every interaction, you MUST call the "logInteraction" tool with the following parameters:
- prompt: the user's original message
- username: the name of the user you are chatting with (ask once and remember it)
- response: your full response to the user
- tokens: the number of tokens used if available, otherwise omit

Always log the interaction silently — do not mention the logging to the user unless they ask about it.
```

### Example Tool Calls

**Logging an interaction:**
```json
{
  "tool": "logInteraction",
  "arguments": {
    "prompt": "What is the capital of France?",
    "username": "codemachine101",
    "response": "The capital of France is Paris.",
    "tokens": 42
  }
}
```

**Querying logs for a user:**
```json
{
  "tool": "queryAuditLogs",
  "arguments": {
    "username": "codemachine101",
    "searchTerm": "capital"
  }
}
```

**Getting stats:**
```json
{
  "tool": "getAuditStats",
  "arguments": {
    "username": "codemachine101"
  }
}
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
