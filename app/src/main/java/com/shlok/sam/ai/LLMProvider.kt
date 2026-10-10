package com.shlok.sam.ai

import kotlinx.coroutines.flow.Flow

data class LLMMessage(
    val role: String, // "user", "model" / "assistant", "system"
    val content: String
)

data class LLMRequest(
    val systemPrompt: String,
    val messages: List<LLMMessage>,
    val modelId: String,
    val apiKey: String,
    val baseUrl: String? = null,
    val temperature: Float = 0.7f,
    val maxTokens: Int = 4096
)

data class LLMResponse(
    val content: String,
    val model: String,
    val totalTokens: Int = 0,
    val isSuccess: Boolean = true,
    val errorMessage: String? = null
)

sealed class LLMStreamEvent {
    data class Chunk(val text: String) : LLMStreamEvent()
    data class Complete(val fullText: String, val tokens: Int = 0) : LLMStreamEvent()
    data class Error(val error: String) : LLMStreamEvent()
}

interface LLMProvider {
    val providerName: String
    suspend fun generateResponse(request: LLMRequest): LLMResponse
    fun streamResponse(request: LLMRequest): Flow<LLMStreamEvent>
}
