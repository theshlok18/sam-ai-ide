package com.shlok.sam.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.ui.theme.*
import com.shlok.sam.viewmodel.VoiceViewModel
import com.shlok.sam.voice.VoiceState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceAssistantScreen(
    viewModel: VoiceViewModel,
    onNavigateBack: () -> Unit
) {
    val voiceState by viewModel.voiceState.collectAsState()
    val transcript by viewModel.transcript.collectAsState()
    val amplitude by viewModel.amplitude.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "voice_wave")
    val waveScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "S.A.M. // VOICE NEURAL ASSISTANT",
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterVertically,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Language support info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(1.dp, HairlineBorder),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text("मराठी (Marathi)", style = MaterialTheme.typography.labelSmall, color = MintEmerald)
                    Text("हिंदी (Hindi)", style = MaterialTheme.typography.labelSmall, color = CobaltPrimary)
                    Text("English", style = MaterialTheme.typography.labelSmall, color = CyberViolet)
                }
            }

            // Central Animated Waveform Orb
            Column(horizontalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(if (voiceState == VoiceState.LISTENING) waveScale * (0.8f + amplitude * 0.4f) else 1f),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .border(
                                2.dp,
                                when (voiceState) {
                                    VoiceState.LISTENING -> MintEmerald
                                    VoiceState.SPEAKING -> CobaltPrimary
                                    VoiceState.PROCESSING -> CyberViolet
                                    else -> HairlineBorder
                                },
                                CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .background(DarkSurfaceContainer, CircleShape)
                            .border(1.dp, HairlineBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (voiceState) {
                                VoiceState.LISTENING -> Icons.Default.Mic
                                VoiceState.SPEAKING -> Icons.Default.VolumeUp
                                VoiceState.PROCESSING -> Icons.Default.GraphicEq
                                else -> Icons.Default.MicNone
                            },
                            contentDescription = "Voice",
                            tint = when (voiceState) {
                                VoiceState.LISTENING -> MintEmerald
                                VoiceState.SPEAKING -> CobaltPrimary
                                VoiceState.PROCESSING -> CyberViolet
                                else -> TextSecondary
                            },
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = when (voiceState) {
                        VoiceState.LISTENING -> "LISTENING... SPEAK NOW"
                        VoiceState.PROCESSING -> "PROCESSING SPEECH WITH S.A.M. CORE..."
                        VoiceState.SPEAKING -> "SPEAKING RESPONSE..."
                        VoiceState.ERROR -> "MIC UNAVAILABLE / CHECK PERMISSION"
                        VoiceState.IDLE -> "TAP MICROPHONE TO SPEAK"
                    },
                    style = MaterialTheme.typography.labelMedium,
                    color = when (voiceState) {
                        VoiceState.LISTENING -> MintEmerald
                        VoiceState.SPEAKING -> CobaltPrimary
                        VoiceState.PROCESSING -> CyberViolet
                        VoiceState.ERROR -> StatusError
                        VoiceState.IDLE -> TextSecondary
                    },
                    fontWeight = FontWeight.Bold
                )
            }

            // Live Transcript Display
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest),
                border = BorderStroke(1.dp, HairlineBorderVariant),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text("LIVE TRANSCRIPT", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = transcript.ifEmpty { "Speech transcript will appear here in real time..." },
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (transcript.isEmpty()) TextMuted else TextOnSurface
                    )
                }
            }

            // Bottom Trigger Button
            IconButton(
                onClick = { viewModel.toggleListening() },
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(if (voiceState == VoiceState.LISTENING) StatusError else CobaltPrimary)
            ) {
                Icon(
                    imageVector = if (voiceState == VoiceState.LISTENING) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = "Mic",
                    tint = DarkSurfaceLowest,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
