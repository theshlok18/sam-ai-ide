package com.shlok.sam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shlok.sam.data.local.MemoryDao
import com.shlok.sam.data.local.ProjectDao
import com.shlok.sam.data.model.MemoryCategory
import com.shlok.sam.data.model.MemoryItem
import com.shlok.sam.data.model.Project
import com.shlok.sam.data.model.ProjectType
import com.shlok.sam.data.preferences.PreferenceManager
import com.shlok.sam.ide.WorkspaceManager
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectsViewModel(
    private val projectDao: ProjectDao,
    private val workspaceManager: WorkspaceManager
) : ViewModel() {

    val projects: StateFlow<List<Project>> = projectDao.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createProject(name: String, type: ProjectType, description: String = "", onCreated: (Project) -> Unit) {
        viewModelScope.launch {
            val project = workspaceManager.createProject(name, type, description)
            projectDao.insertProject(project)
            onCreated(project)
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            projectDao.deleteProject(project)
        }
    }
}

class MemoryViewModel(
    private val memoryDao: MemoryDao
) : ViewModel() {

    val memories: StateFlow<List<MemoryItem>> = memoryDao.getAllMemoryItems()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMemory(key: String, value: String, category: MemoryCategory = MemoryCategory.ARCHITECTURE) {
        viewModelScope.launch {
            val item = MemoryItem(key = key, value = value, category = category)
            memoryDao.insertMemoryItem(item)
        }
    }

    fun deleteMemory(item: MemoryItem) {
        viewModelScope.launch {
            memoryDao.deleteMemoryItem(item)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            memoryDao.clearAllMemory()
        }
    }
}

class SettingsViewModel(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val selectedProvider = preferenceManager.selectedProvider
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "GEMINI")

    val selectedModelId = preferenceManager.selectedModelId
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "gemini-1.5-flash")

    val geminiKey = preferenceManager.geminiApiKey
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val openaiKey = preferenceManager.openaiApiKey
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val claudeKey = preferenceManager.claudeApiKey
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val preferredLanguage = preferenceManager.preferredLanguage
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "AUTO")

    fun setProvider(p: String) { viewModelScope.launch { preferenceManager.setProvider(p) } }
    fun setModelId(m: String) { viewModelScope.launch { preferenceManager.setModelId(m) } }
    fun setGeminiKey(k: String) { viewModelScope.launch { preferenceManager.setGeminiApiKey(k) } }
    fun setOpenaiKey(k: String) { viewModelScope.launch { preferenceManager.setOpenaiApiKey(k) } }
    fun setClaudeKey(k: String) { viewModelScope.launch { preferenceManager.setClaudeApiKey(k) } }
    fun setLanguage(lang: String) { viewModelScope.launch { preferenceManager.setPreferredLanguage(lang) } }
}
