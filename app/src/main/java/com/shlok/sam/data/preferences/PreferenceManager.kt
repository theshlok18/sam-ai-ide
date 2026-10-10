package com.shlok.sam.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sam_settings")

class PreferenceManager(private val context: Context) {

    companion object {
        val KEY_PROVIDER = stringPreferencesKey("ai_provider")
        val KEY_MODEL_ID = stringPreferencesKey("ai_model_id")
        val KEY_GEMINI_KEY = stringPreferencesKey("gemini_api_key")
        val KEY_OPENAI_KEY = stringPreferencesKey("openai_api_key")
        val KEY_CLAUDE_KEY = stringPreferencesKey("claude_api_key")
        val KEY_OLLAMA_URL = stringPreferencesKey("ollama_host_url")
        val KEY_PREF_LANGUAGE = stringPreferencesKey("preferred_language")
        val KEY_ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val KEY_AUTONOMOUS_APPROVAL_REQUIRED = booleanPreferencesKey("agent_approval_required")
        val KEY_VOICE_AUTOPLAY = booleanPreferencesKey("voice_autoplay")
    }

    val selectedProvider: Flow<String> = context.dataStore.data.map { it[KEY_PROVIDER] ?: "GEMINI" }
    val selectedModelId: Flow<String> = context.dataStore.data.map { it[KEY_MODEL_ID] ?: "gemini-1.5-flash" }
    val geminiApiKey: Flow<String> = context.dataStore.data.map { it[KEY_GEMINI_KEY] ?: "" }
    val openaiApiKey: Flow<String> = context.dataStore.data.map { it[KEY_OPENAI_KEY] ?: "" }
    val claudeApiKey: Flow<String> = context.dataStore.data.map { it[KEY_CLAUDE_KEY] ?: "" }
    val ollamaHostUrl: Flow<String> = context.dataStore.data.map { it[KEY_OLLAMA_URL] ?: "http://localhost:11434" }
    val preferredLanguage: Flow<String> = context.dataStore.data.map { it[KEY_PREF_LANGUAGE] ?: "AUTO" }
    val isOnboardingCompleted: Flow<Boolean> = context.dataStore.data.map { it[KEY_ONBOARDING_COMPLETED] ?: false }
    val isApprovalRequired: Flow<Boolean> = context.dataStore.data.map { it[KEY_AUTONOMOUS_APPROVAL_REQUIRED] ?: true }
    val isVoiceAutoplay: Flow<Boolean> = context.dataStore.data.map { it[KEY_VOICE_AUTOPLAY] ?: true }

    suspend fun setProvider(provider: String) {
        context.dataStore.edit { it[KEY_PROVIDER] = provider }
    }

    suspend fun setModelId(modelId: String) {
        context.dataStore.edit { it[KEY_MODEL_ID] = modelId }
    }

    suspend fun setGeminiApiKey(key: String) {
        context.dataStore.edit { it[KEY_GEMINI_KEY] = key }
    }

    suspend fun setOpenaiApiKey(key: String) {
        context.dataStore.edit { it[KEY_OPENAI_KEY] = key }
    }

    suspend fun setClaudeApiKey(key: String) {
        context.dataStore.edit { it[KEY_CLAUDE_KEY] = key }
    }

    suspend fun setOllamaHostUrl(url: String) {
        context.dataStore.edit { it[KEY_OLLAMA_URL] = url }
    }

    suspend fun setPreferredLanguage(lang: String) {
        context.dataStore.edit { it[KEY_PREF_LANGUAGE] = lang }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { it[KEY_ONBOARDING_COMPLETED] = completed }
    }

    suspend fun setApprovalRequired(required: Boolean) {
        context.dataStore.edit { it[KEY_AUTONOMOUS_APPROVAL_REQUIRED] = required }
    }

    suspend fun setVoiceAutoplay(autoplay: Boolean) {
        context.dataStore.edit { it[KEY_VOICE_AUTOPLAY] = autoplay }
    }
}
