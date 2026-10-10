package com.shlok.sam.ai

import com.shlok.sam.data.model.ChatMessage
import com.shlok.sam.data.model.MessageRole
import com.shlok.sam.data.preferences.PreferenceManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class ConversationEngine(
    private val preferenceManager: PreferenceManager
) {
    private val geminiProvider = GeminiProvider()
    private val openAIProvider = OpenAIProvider()
    private val claudeProvider = ClaudeProvider()
    private val offlineProvider = OfflineSimulationProvider()

    suspend fun getActiveProvider(): LLMProvider {
        val providerName = preferenceManager.selectedProvider.first()
        val geminiKey = preferenceManager.geminiApiKey.first()
        val openaiKey = preferenceManager.openaiApiKey.first()
        val claudeKey = preferenceManager.claudeApiKey.first()

        return when (providerName.uppercase()) {
            "GEMINI" -> if (geminiKey.isNotBlank()) geminiProvider else offlineProvider
            "OPENAI" -> if (openaiKey.isNotBlank()) openAIProvider else offlineProvider
            "CLAUDE" -> if (claudeKey.isNotBlank()) claudeProvider else offlineProvider
            else -> offlineProvider
        }
    }

    suspend fun buildLLMRequest(
        conversationHistory: List<ChatMessage>,
        userPrompt: String,
        systemInstruction: String = SystemPrompt.BASE_SYSTEM_PROMPT
    ): LLMRequest {
        val providerName = preferenceManager.selectedProvider.first()
        val modelId = preferenceManager.selectedModelId.first()
        val apiKey = when (providerName.uppercase()) {
            "GEMINI" -> preferenceManager.geminiApiKey.first()
            "OPENAI" -> preferenceManager.openaiApiKey.first()
            "CLAUDE" -> preferenceManager.claudeApiKey.first()
            else -> ""
        }

        // Context window optimization: take up to last 15 messages
        val recentMessages = conversationHistory.takeLast(15).map {
            LLMMessage(
                role = when (it.role) {
                    MessageRole.USER -> "user"
                    MessageRole.ASSISTANT -> "assistant"
                    MessageRole.SYSTEM -> "system"
                    MessageRole.TOOL -> "tool"
                },
                content = it.content
            )
        }.toMutableList()

        if (recentMessages.none { it.role == "user" && it.content == userPrompt }) {
            recentMessages.add(LLMMessage("user", userPrompt))
        }

        return LLMRequest(
            systemPrompt = systemInstruction,
            messages = recentMessages,
            modelId = modelId,
            apiKey = apiKey
        )
    }

    suspend fun streamConversation(
        history: List<ChatMessage>,
        userPrompt: String
    ): Flow<LLMStreamEvent> {
        val provider = getActiveProvider()
        val request = buildLLMRequest(history, userPrompt)
        return provider.streamResponse(request)
    }

    suspend fun generateSingleTurn(
        history: List<ChatMessage>,
        userPrompt: String
    ): LLMResponse {
        val provider = getActiveProvider()
        val request = buildLLMRequest(history, userPrompt)
        return provider.generateResponse(request)
    }
}
