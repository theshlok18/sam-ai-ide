package com.shlok.sam.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.data.model.Project
import com.shlok.sam.data.model.ProjectType
import com.shlok.sam.ui.theme.*
import com.shlok.sam.viewmodel.ProjectsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewProjectScreen(
    viewModel: ProjectsViewModel,
    onNavigateBack: () -> Unit,
    onProjectCreated: (Project) -> Unit
) {
    var projectName by remember { mutableStateOf("") }
    var projectDesc by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(ProjectType.KOTLIN_ANDROID) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "NEW PROJECT WIZARD",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextOnSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextOnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, HairlineBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "PROJECT DETAILS", style = MaterialTheme.typography.labelSmall, color = CobaltPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = projectName,
                            onValueChange = { projectName = it },
                            label = { Text("Project Name") },
                            placeholder = { Text("e.g. SmartCameraAI") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkSurfaceContainer,
                                unfocusedContainerColor = DarkSurfaceContainer,
                                focusedBorderColor = CobaltPrimary,
                                unfocusedBorderColor = HairlineBorder,
                                focusedTextColor = TextOnSurface,
                                unfocusedTextColor = TextOnSurface
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = projectDesc,
                            onValueChange = { projectDesc = it },
                            label = { Text("Description") },
                            placeholder = { Text("e.g. Real-time edge AI object detector") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkSurfaceContainer,
                                unfocusedContainerColor = DarkSurfaceContainer,
                                focusedBorderColor = CobaltPrimary,
                                unfocusedBorderColor = HairlineBorder,
                                focusedTextColor = TextOnSurface,
                                unfocusedTextColor = TextOnSurface
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }

            item {
                Text(text = "SELECT STACK TEMPLATE", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            }

            item {
                TemplateOption(
                    type = ProjectType.KOTLIN_ANDROID,
                    title = "Kotlin & Jetpack Compose",
                    subtitle = "Native Android App with Material 3 & Architecture Components",
                    icon = Icons.Default.Android,
                    isSelected = selectedType == ProjectType.KOTLIN_ANDROID,
                    accent = CobaltPrimary,
                    onSelect = { selectedType = ProjectType.KOTLIN_ANDROID }
                )
            }

            item {
                TemplateOption(
                    type = ProjectType.PYTHON_FASTAPI,
                    title = "Python FastAPI Backend",
                    subtitle = "High-performance REST API with Pydantic & Uvicorn",
                    icon = Icons.Default.DataObject,
                    isSelected = selectedType == ProjectType.PYTHON_FASTAPI,
                    accent = MintEmerald,
                    onSelect = { selectedType = ProjectType.PYTHON_FASTAPI }
                )
            }

            item {
                TemplateOption(
                    type = ProjectType.REACT_NATIVE,
                    title = "React Native (TypeScript)",
                    subtitle = "Cross-platform mobile application bundle",
                    icon = Icons.Default.PhoneAndroid,
                    isSelected = selectedType == ProjectType.REACT_NATIVE,
                    accent = CyberViolet,
                    onSelect = { selectedType = ProjectType.REACT_NATIVE }
                )
            }

            item {
                TemplateOption(
                    type = ProjectType.WEB_VITE,
                    title = "Web Frontend (Vite)",
                    subtitle = "Modern lightweight web app bundle",
                    icon = Icons.Default.Language,
                    isSelected = selectedType == ProjectType.WEB_VITE,
                    accent = PrimaryDim,
                    onSelect = { selectedType = ProjectType.WEB_VITE }
                )
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (projectName.isNotBlank()) {
                            viewModel.createProject(projectName, selectedType, projectDesc) { created ->
                                onProjectCreated(created)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = CobaltPrimary),
                    enabled = projectName.isNotBlank(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = DarkSurfaceLowest)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Scaffold & Initialize Workspace", color = DarkSurfaceLowest, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun TemplateOption(
    type: ProjectType,
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    accent: Color,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(containerColor = if (isSelected) DarkSurfaceContainer else DarkSurface),
        border = BorderStroke(1.dp, if (isSelected) accent else HairlineBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(accent.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = accent)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = TextOnSurface)
                Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, contentDescription = "Selected", tint = accent)
            }
        }
    }
}
