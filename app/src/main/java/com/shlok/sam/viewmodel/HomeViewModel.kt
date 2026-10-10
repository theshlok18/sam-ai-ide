package com.shlok.sam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shlok.sam.data.local.ProjectDao
import com.shlok.sam.data.model.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

data class SystemTelemetry(
    val aiCoreStatus: String = "ONLINE / IDLE",
    val latencyMs: Int = 18,
    val memoryUsageMb: Int = 142,
    val activeTokens: Int = 1840,
    val securityClearance: String = "API 35 SAFE"
)

class HomeViewModel(
    projectDao: ProjectDao
) : ViewModel() {

    val recentProjects: StateFlow<List<Project>> = projectDao.getAllProjects()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _telemetry = MutableStateFlow(SystemTelemetry())
    val telemetry: StateFlow<SystemTelemetry> = _telemetry.asStateFlow()

    private val _activeFilter = MutableStateFlow("ALL")
    val activeFilter: StateFlow<String> = _activeFilter.asStateFlow()

    fun setFilter(filter: String) {
        _activeFilter.value = filter
    }
}
