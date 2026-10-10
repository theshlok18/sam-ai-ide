package com.shlok.sam

import android.app.Application
import com.shlok.sam.ai.ConversationEngine
import com.shlok.sam.data.local.AppDatabase
import com.shlok.sam.data.preferences.PreferenceManager
import com.shlok.sam.ide.BuildService
import com.shlok.sam.ide.WorkspaceManager
import com.shlok.sam.agent.AgentEngine
import com.shlok.sam.voice.VoiceEngine

class SAMApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val preferenceManager by lazy { PreferenceManager(this) }
    val conversationEngine by lazy { ConversationEngine(preferenceManager) }
    val workspaceManager by lazy { WorkspaceManager(this) }
    val buildService by lazy { BuildService() }
    val agentEngine by lazy { AgentEngine() }
    val voiceEngine by lazy { VoiceEngine(this) }

    override fun onCreate() {
        super.onCreate()
    }
}
