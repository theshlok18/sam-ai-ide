package com.shlok.sam.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shlok.sam.data.model.Project
import com.shlok.sam.data.model.ProjectFile
import com.shlok.sam.ide.BuildService
import com.shlok.sam.ide.WorkspaceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class IdeViewModel(
    private val workspaceManager: WorkspaceManager,
    private val buildService: BuildService
) : ViewModel() {

    private val _currentProject = MutableStateFlow<Project?>(null)
    val currentProject: StateFlow<Project?> = _currentProject.asStateFlow()

    private val _fileTree = MutableStateFlow<List<ProjectFile>>(emptyList())
    val fileTree: StateFlow<List<ProjectFile>> = _fileTree.asStateFlow()

    private val _activeFile = MutableStateFlow<ProjectFile?>(null)
    val activeFile: StateFlow<ProjectFile?> = _activeFile.asStateFlow()

    private val _editorContent = MutableStateFlow("")
    val editorContent: StateFlow<String> = _editorContent.asStateFlow()

    private val _isBuilding = MutableStateFlow(false)
    val isBuilding: StateFlow<Boolean> = _isBuilding.asStateFlow()

    private val _buildLogs = MutableStateFlow<List<String>>(emptyList())
    val buildLogs: StateFlow<List<String>> = _buildLogs.asStateFlow()

    fun openProject(project: Project) {
        _currentProject.value = project
        viewModelScope.launch {
            val files = workspaceManager.getFileTree(project.rootPath)
            _fileTree.value = files
            val firstFile = findFirstSourceFile(files)
            if (firstFile != null) {
                openFile(firstFile)
            }
        }
    }

    fun openFile(file: ProjectFile) {
        if (file.isDirectory) return
        _activeFile.value = file
        viewModelScope.launch {
            val content = workspaceManager.readFileContent(file.path)
            _editorContent.value = content
        }
    }

    fun updateEditorContent(newContent: String) {
        _editorContent.value = newContent
    }

    fun saveCurrentFile() {
        val file = _activeFile.value ?: return
        viewModelScope.launch {
            workspaceManager.saveFileContent(file.path, _editorContent.value)
        }
    }

    fun runBuild() {
        val project = _currentProject.value ?: return
        _isBuilding.value = true
        _buildLogs.value = emptyList()

        viewModelScope.launch {
            buildService.executeBuild(project.rootPath, "debug").collect { logLine ->
                _buildLogs.value = _buildLogs.value + logLine
            }
            _isBuilding.value = false
        }
    }

    private fun findFirstSourceFile(files: List<ProjectFile>): ProjectFile? {
        for (f in files) {
            if (!f.isDirectory) return f
            val sub = findFirstSourceFile(f.children)
            if (sub != null) return sub
        }
        return null
    }
}
