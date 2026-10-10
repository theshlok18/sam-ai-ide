package com.shlok.sam.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class MessageRole {
    USER, ASSISTANT, SYSTEM, TOOL
}

@Entity(tableName = "conversations")
data class Conversation(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val title: String = "New Conversation",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val modelId: String = "gemini-1.5-pro",
    val provider: String = "Google Gemini",
    val totalTokens: Int = 0,
    val isPinned: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val conversationId: String,
    val role: MessageRole,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val modelName: String? = null,
    val isStreaming: Boolean = false,
    val isError: Boolean = false,
    val codeSnippet: String? = null,
    val language: String? = null
)
