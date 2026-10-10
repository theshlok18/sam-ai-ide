package com.shlok.sam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shlok.sam.ai.ConversationEngine
import com.shlok.sam.ai.LLMStreamEvent
import com.shlok.sam.data.local.ChatDao
import com.shlok.sam.data.model.ChatMessage
import com.shlok.sam.data.model.Conversation
import com.shlok.sam.data.model.MessageRole
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

class ChatViewModel(
    private val chatDao: ChatDao,
    private val conversationEngine: ConversationEngine
) : ViewModel() {

    val allConversations: StateFlow<List<Conversation>> = chatDao.getAllConversations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentConversationId = MutableStateFlow<String?>(null)
    val currentConversationId: StateFlow<String?> = _currentConversationId.asStateFlow()

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    fun selectConversation(conversationId: String) {
        _currentConversationId.value = conversationId
        viewModelScope.launch {
            chatDao.getMessagesForConversation(conversationId).collect {
                _messages.value = it
            }
        }
    }

    fun startNewConversation(initialTitle: String = "New Conversation") {
        viewModelScope.launch {
            val conv = Conversation(
                id = UUID.randomUUID().toString(),
                title = initialTitle
            )
            chatDao.insertConversation(conv)
            selectConversation(conv.id)
        }
    }

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        viewModelScope.launch {
            var convId = _currentConversationId.value
            if (convId == null) {
                val newConv = Conversation(
                    id = UUID.randomUUID().toString(),
                    title = if (userText.length > 25) userText.take(25) + "..." else userText
                )
                chatDao.insertConversation(newConv)
                convId = newConv.id
                _currentConversationId.value = convId
            }

            val userMsg = ChatMessage(
                conversationId = convId,
                role = MessageRole.USER,
                content = userText
            )
            chatDao.insertMessage(userMsg)

            val assistantMsgId = UUID.randomUUID().toString()
            val initialAssistantMsg = ChatMessage(
                id = assistantMsgId,
                conversationId = convId,
                role = MessageRole.ASSISTANT,
                content = "",
                isStreaming = true
            )
            chatDao.insertMessage(initialAssistantMsg)
            _isGenerating.value = true

            val currentHistory = chatDao.getMessagesListForConversation(convId)
            val accumulated = StringBuilder()

            try {
                conversationEngine.streamConversation(currentHistory, userText).collect { event ->
                    when (event) {
                        is LLMStreamEvent.Chunk -> {
                            accumulated.append(event.text)
                            chatDao.updateMessage(
                                initialAssistantMsg.copy(
                                    content = accumulated.toString(),
                                    isStreaming = true
                                )
                            )
                        }
                        is LLMStreamEvent.Complete -> {
                            chatDao.updateMessage(
                                initialAssistantMsg.copy(
                                    content = event.fullText.ifEmpty { accumulated.toString() },
                                    isStreaming = false
                                )
                            )
                            _isGenerating.value = false
                        }
                        is LLMStreamEvent.Error -> {
                            chatDao.updateMessage(
                                initialAssistantMsg.copy(
                                    content = "Error: ${event.error}",
                                    isStreaming = false,
                                    isError = true
                                )
                            )
                            _isGenerating.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                chatDao.updateMessage(
                    initialAssistantMsg.copy(
                        content = "Connection Error: ${e.localizedMessage}",
                        isStreaming = false,
                        isError = true
                    )
                )
                _isGenerating.value = false
            }
        }
    }

    fun deleteConversation(id: String) {
        viewModelScope.launch {
            chatDao.deleteConversation(id)
            chatDao.deleteMessagesForConversation(id)
            if (_currentConversationId.value == id) {
                _currentConversationId.value = null
                _messages.value = emptyList()
            }
        }
    }
}
