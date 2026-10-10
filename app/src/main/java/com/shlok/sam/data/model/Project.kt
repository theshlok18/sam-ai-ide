package com.shlok.sam.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class ProjectType {
    KOTLIN_ANDROID,
    PYTHON_FASTAPI,
    REACT_NATIVE,
    WEB_VITE,
    RUST
}

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val projectType: ProjectType = ProjectType.KOTLIN_ANDROID,
    val rootPath: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
    val gitBranch: String = "main",
    val uncommittedChanges: Int = 0,
    val filesCount: Int = 0,
    val tags: String = "Mobile, AI"
)

data class ProjectFile(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long = 0,
    val extension: String = "",
    val children: List<ProjectFile> = emptyList(),
    val content: String? = null
)
