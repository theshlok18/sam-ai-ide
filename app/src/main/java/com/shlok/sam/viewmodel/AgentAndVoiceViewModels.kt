package com.shlok.sam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shlok.sam.agent.AgentEngine
import com.shlok.sam.agent.AgentState
import com.shlok.sam.voice.VoiceEngine
import com.shlok.sam.voice.VoiceState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AgentViewModel(
    private val agentEngine: AgentEngine
) : ViewModel() {

    val agentState: StateFlow<AgentState> = agentEngine.agentState

    fun startTask(prompt: String) {
        viewModelScope.launch {
            agentEngine.startTask(prompt)
        }
    }

    fun approveTask() {
        viewModelScope.launch {
            agentEngine.approveAndExecuteAll()
        }
    }

    fun rejectTask() {
        agentEngine.rejectTask()
    }

    fun reset() {
        agentEngine.reset()
    }
}

class VoiceViewModel(
    private val voiceEngine: VoiceEngine
) : ViewModel() {

    val voiceState: StateFlow<VoiceState> = voiceEngine.voiceState
    val transcript: StateFlow<String> = voiceEngine.transcript
    val amplitude: StateFlow<Float> = voiceEngine.amplitude

    fun toggleListening() {
        if (voiceState.value == VoiceState.LISTENING) {
            voiceEngine.stopListening()
        } else {
            voiceEngine.startListening()
        }
    }

    fun speak(text: String) {
        voiceEngine.speak(text)
    }

    override fun onCleared() {
        super.onCleared()
        voiceEngine.destroy()
    }
}
