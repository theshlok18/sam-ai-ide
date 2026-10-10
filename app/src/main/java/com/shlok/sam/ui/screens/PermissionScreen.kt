package com.shlok.sam.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.shlok.sam.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current

    var hasAudioPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
    }
    var hasNotificationPermission by remember {
        mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED)
    }

    val audioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasAudioPermission = granted }
    )

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasNotificationPermission = granted }
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "SECURITY & PERMISSIONS (API 35)",
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
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, HairlineBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("DEVICE INTEGRATION CLEARANCE", style = MaterialTheme.typography.labelSmall, color = CobaltPrimary)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "Grant system clearances to enable Voice Assistant audio input, IDE file workspace access, and agent notification alerts.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            item {
                PermissionItemCard(
                    title = "Microphone & Speech Input",
                    description = "Required for Voice Assistant STT waveform processing in Marathi, Hindi & English",
                    icon = Icons.Default.Mic,
                    isGranted = hasAudioPermission,
                    onRequest = { audioLauncher.launch(Manifest.permission.RECORD_AUDIO) }
                )
            }

            item {
                PermissionItemCard(
                    title = "System Notifications",
                    description = "Required for Autonomous Agent completion alerts and build status updates",
                    icon = Icons.Default.Notifications,
                    isGranted = hasNotificationPermission,
                    onRequest = { notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
                )
            }

            item {
                PermissionItemCard(
                    title = "Internal Workspace Storage",
                    description = "Sandboxed app storage for project files, AST trees and Gradle builds",
                    icon = Icons.Default.Folder,
                    isGranted = true, // App sandboxed filesDir is always granted
                    onRequest = {}
                )
            }
        }
    }
}

@Composable
fun PermissionItemCard(
    title: String,
    description: String,
    icon: ImageVector,
    isGranted: Boolean,
    onRequest: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, if (isGranted) MintEmerald.copy(alpha = 0.5f) else HairlineBorder),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(if (isGranted) MintEmerald.copy(alpha = 0.15f) else DarkSurfaceContainer, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = if (isGranted) MintEmerald else TextSecondary)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextOnSurface)
                    Text(text = description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
            if (isGranted) {
                Text(text = "GRANTED", style = MaterialTheme.typography.labelSmall, color = MintEmerald, fontWeight = FontWeight.Bold)
            } else {
                Button(
                    onClick = onRequest,
                    colors = ButtonDefaults.buttonColors(containerColor = CobaltPrimary),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text("Grant", color = DarkSurfaceLowest, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}
