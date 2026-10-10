package com.shlok.sam.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.data.model.Project
import com.shlok.sam.ui.theme.*
import com.shlok.sam.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToChat: () -> Unit,
    onNavigateToIde: () -> Unit,
    onNavigateToAgent: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToVoice: () -> Unit,
    onNavigateToModels: () -> Unit,
    onNavigateToMemory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val telemetry by viewModel.telemetry.collectAsState()
    val projects by viewModel.recentProjects.collectAsState()
    val activeFilter by viewModel.activeFilter.collectAsState()

    // Pulse animation for AI Core
    val infiniteTransition = rememberInfiniteTransition(label = "core_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(MintEmerald)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "S.A.M. // NEURAL CORE",
                            style = MaterialTheme.typography.labelLarge,
                            color = PrimaryFixed
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .border(1.dp, HairlineBorder, CircleShape)
                                .background(DarkSurfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "S",
                                style = MaterialTheme.typography.labelMedium,
                                color = CobaltPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
                            tint = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Central Pulse Visualizer Card
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, HairlineBorder)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(110.dp)
                                .scale(pulseScale),
                            contentAlignment = Alignment.Center
                        ) {
                            // Outer ring
                            Box(
                                modifier = Modifier
                                    .size(110.dp)
                                    .border(1.5.dp, Brush.sweepGradient(listOf(CobaltPrimary, CyberViolet, MintEmerald, CobaltPrimary)), CircleShape)
                            )
                            // Inner glow
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .background(DarkSurfaceContainer, CircleShape)
                                    .border(1.dp, HairlineBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Memory,
                                        contentDescription = "AI Core",
                                        tint = CobaltPrimary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                    Text(
                                        text = "AUTONOMOUS",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MintEmerald,
                                        fontSize = 8.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "SMART AUTONOMOUS MACHINE",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextOnSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Developed by Shlok • BTech AI Systems",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Live Telemetry Grid
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DarkSurfaceLowest, RoundedCornerShape(8.dp))
                                .border(1.dp, HairlineBorderVariant, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            TelemetryItem(label = "LATENCY", value = "${telemetry.latencyMs}ms", color = MintEmerald)
                            TelemetryItem(label = "MEM LOAD", value = "${telemetry.memoryUsageMb}MB", color = CobaltPrimary)
                            TelemetryItem(label = "TOKENS", value = "${telemetry.activeTokens}", color = CyberViolet)
                            TelemetryItem(label = "STATUS", value = "READY", color = MintEmerald)
                        }
                    }
                }
            }

            // Quick Access Mode Grid
            item {
                Text(
                    text = "PRIMARY MODES",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ModeCard(
                        modifier = Modifier.weight(1f),
                        title = "AI Chat",
                        desc = "Natural Multi-turn & Code",
                        icon = Icons.Default.Chat,
                        accentColor = CobaltPrimary,
                        onClick = onNavigateToChat
                    )
                    ModeCard(
                        modifier = Modifier.weight(1f),
                        title = "AI IDE",
                        desc = "File Editor & Live Build",
                        icon = Icons.Default.Code,
                        accentColor = CyberViolet,
                        onClick = onNavigateToIde
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ModeCard(
                        modifier = Modifier.weight(1f),
                        title = "Agent Mode",
                        desc = "Autonomous Refactor",
                        icon = Icons.Default.AutoFixHigh,
                        accentColor = MintEmerald,
                        onClick = onNavigateToAgent
                    )
                    ModeCard(
                        modifier = Modifier.weight(1f),
                        title = "Voice Bot",
                        desc = "Marathi/Hindi/English",
                        icon = Icons.Default.Mic,
                        accentColor = PrimaryDim,
                        onClick = onNavigateToVoice
                    )
                }
            }

            // Quick Tools Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickToolChip(label = "Projects", icon = Icons.Outlined.Folder, onClick = onNavigateToProjects)
                    QuickToolChip(label = "AI Models", icon = Icons.Outlined.Tune, onClick = onNavigateToModels)
                    QuickToolChip(label = "Memory Core", icon = Icons.Outlined.Storage, onClick = onNavigateToMemory)
                }
            }

            // Recent Projects
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT WORKSPACES",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                    TextButton(onClick = onNavigateToProjects) {
                        Text("View All", color = CobaltPrimary, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            if (projects.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = BorderStroke(1.dp, HairlineBorder)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.FolderOpen, contentDescription = null, tint = TextMuted, modifier = Modifier.size(36.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No workspaces initialized yet", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = onNavigateToProjects,
                                colors = ButtonDefaults.buttonColors(containerColor = CobaltPrimary)
                            ) {
                                Text("Create First Project", color = DarkSurfaceLowest)
                            }
                        }
                    }
                }
            } else {
                items(projects.take(3)) { proj ->
                    ProjectRowCard(project = proj, onClick = onNavigateToIde)
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun TelemetryItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterVertically) {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(text = value, style = MaterialTheme.typography.labelMedium, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ModeCard(
    modifier: Modifier = Modifier,
    title: String,
    desc: String,
    icon: ImageVector,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, HairlineBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = accentColor)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, style = MaterialTheme.typography.headlineSmall, fontSize = 16.sp, color = TextOnSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}

@Composable
fun QuickToolChip(label: String, icon: ImageVector, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = DarkSurfaceContainer,
        border = BorderStroke(1.dp, HairlineBorderVariant)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = icon, contentDescription = label, tint = CobaltPrimary, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = TextOnSurfaceVariant)
        }
    }
}

@Composable
fun ProjectRowCard(project: Project, onClick: () -> Unit) {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(DarkSurfaceContainerHigh, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Terminal, contentDescription = null, tint = MintEmerald, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = project.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = TextOnSurface)
                    Text(text = "${project.projectType.name} • ${project.filesCount} files", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = TextMuted)
        }
    }
}
