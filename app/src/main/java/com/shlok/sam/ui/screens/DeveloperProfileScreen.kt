package com.shlok.sam.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperProfileScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "ARCHITECT // CREATOR PROFILE",
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterVertically
        ) {
            // Profile Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, HairlineBorder),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .border(2.dp, Brush.sweepGradient(listOf(CobaltPrimary, CyberViolet, MintEmerald, CobaltPrimary)), CircleShape)
                                .background(DarkSurfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "S",
                                style = MaterialTheme.typography.headlineLarge,
                                color = CobaltPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 36.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Shlok Sathe",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextOnSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "BTech Student • CSE & AI Systems Architect",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MintEmerald
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Creator and lead architect of S.A.M. (Smart Autonomous Machine), an on-device AI pairing assistant, autonomous coding engine, and high-mobility IDE.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            // Architecture Principles Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, HairlineBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "CORE ARCHITECTURAL PILLARS", style = MaterialTheme.typography.labelSmall, color = CobaltPrimary)
                        Spacer(modifier = Modifier.height(12.dp))
                        PillarRow("Autonomous Agent Mode", "Zero-prompt AST refactoring with human-in-the-loop approvals")
                        Spacer(modifier = Modifier.height(8.dp))
                        PillarRow("Multi-Lingual Engine", "Fluent natural conversation in Marathi, Hindi, and English")
                        Spacer(modifier = Modifier.height(8.dp))
                        PillarRow("Local-First Security", "Android Keystore encryption with zero telemetry leakage")
                        Spacer(modifier = Modifier.height(8.dp))
                        PillarRow("Zero-Glow UI Aesthetic", "Cybernetic Dark palette tailored for developer focus")
                    }
                }
            }
        }
    }
}

@Composable
fun PillarRow(title: String, desc: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = MintEmerald, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextOnSurface)
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}
