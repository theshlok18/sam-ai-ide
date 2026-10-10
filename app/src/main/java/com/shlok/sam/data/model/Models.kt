package com.shlok.sam.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class MemoryCategory {
    ARCHITECTURE, USER_PREFERENCE, CODE_STYLE, CONSTRAINT, PROJECT_RULE
}

@Entity(tableName = "memory_items")
data class MemoryItem(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val key: String,
    val value: String,
    val category: MemoryCategory = MemoryCategory.ARCHITECTURE,
    val confidence: Float = 0.95f,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isEnabled: Boolean = true,
    val projectId: String? = null
)

enum class AgentTaskStatus {
    IDLE, PLANNING, AWAITING_APPROVAL, RUNNING, TESTING, COMPLETED, FAILED, CANCELLED
}

data class AgentStep(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val targetFile: String? = null,
    val diff: String? = null,
    val isCompleted: Boolean = false,
    val isApproved: Boolean = false,
    val outputLog: String? = null
)

@Entity(tableName = "agent_tasks")
data class AgentTask(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val taskPrompt: String,
    val status: AgentTaskStatus = AgentTaskStatus.IDLE,
    val currentStepIndex: Int = 0,
    val totalSteps: Int = 0,
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long? = null,
    val logs: String = "",
    val error: String? = null
)

enum class AIProviderType {
    GEMINI, OPENAI, CLAUDE, OLLAMA, CUSTOM
}

data class AIModel(
    val id: String,
    val name: String,
    val provider: AIProviderType,
    val contextWindow: String,
    val isVisionSupported: Boolean = true,
    val isStreamingSupported: Boolean = true,
    val isFunctionCallingSupported: Boolean = true,
    val description: String = "",
    val latencyTag: String = "Ultra-Fast",
    val isSelected: Boolean = false
)
