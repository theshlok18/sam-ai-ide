package com.shlok.sam.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.ui.theme.*
import com.shlok.sam.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToAiModels: () -> Unit,
    onNavigateToPermissions: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val currentProvider by viewModel.selectedProvider.collectAsState()
    val preferredLanguage by viewModel.preferredLanguage.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "S.A.M. // SYSTEM PREFERENCES",
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
            // AI Configuration Group
            item {
                Text(text = "AI ENGINE & INFERENCE", style = MaterialTheme.typography.labelMedium, color = CobaltPrimary)
            }

            item {
                SettingsActionCard(
                    title = "AI Providers & API Vault",
                    subtitle = "Active: $currentProvider (Gemini, OpenAI, Claude, Ollama)",
                    icon = Icons.Default.Tune,
                    onClick = onNavigateToAiModels
                )
            }

            // Language Selection
            item {
                Text(text = "CONVERSATIONAL LANGUAGE", style = MaterialTheme.typography.labelMedium, color = CobaltPrimary)
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, HairlineBorder),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        LanguageRadioOption("Auto-Detect (Marathi, Hindi & English)", "AUTO", preferredLanguage) { viewModel.setLanguage("AUTO") }
                        LanguageRadioOption("मराठी (Marathi First)", "MR", preferredLanguage) { viewModel.setLanguage("MR") }
                        LanguageRadioOption("हिंदी (Hindi First)", "HI", preferredLanguage) { viewModel.setLanguage("HI") }
                        LanguageRadioOption("English (Standard)", "EN", preferredLanguage) { viewModel.setLanguage("EN") }
                    }
                }
            }

            // Security & Permissions
            item {
                Text(text = "SYSTEM & PERMISSIONS", style = MaterialTheme.typography.labelMedium, color = CobaltPrimary)
            }

            item {
                SettingsActionCard(
                    title = "Android Permissions & Clearances",
                    subtitle = "Microphone, Notifications, Storage",
                    icon = Icons.Default.Security,
                    onClick = onNavigateToPermissions
                )
            }

            // About Developer & Architecture
            item {
                Text(text = "ABOUT S.A.M.", style = MaterialTheme.typography.labelMedium, color = CobaltPrimary)
            }

            item {
                SettingsActionCard(
                    title = "Developer & Architecture Profile",
                    subtitle = "Developed by Shlok • BTech AI Systems",
                    icon = Icons.Default.Person,
                    onClick = onNavigateToProfile
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest),
                    border = BorderStroke(1.dp, HairlineBorderVariant),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("S.A.M. v1.0.0-RELEASE", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        Text("Smart Autonomous Machine • Zero-Glow Precision Studio", style = MaterialTheme.typography.labelSmall, color = MintEmerald)
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, HairlineBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = CobaltPrimary, modifier = Modifier.size(22.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = TextOnSurface)
                    Text(text = subtitle, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
        }
    }
}

@Composable
fun LanguageRadioOption(label: String, value: String, selected: String, onSelect: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected == value,
            onClick = onSelect,
            colors = RadioButtonDefaults.colors(selectedColor = CobaltPrimary)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = TextOnSurface)
    }
}
