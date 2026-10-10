package com.shlok.sam.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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

@Composable
fun OnboardingScreen(
    onCompleteOnboarding: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header badge
            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkSurfaceContainer)
                    .border(1.dp, HairlineBorder, RoundedCornerShape(20.dp))
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "SYSTEM INITIALIZATION // BOOT SEQUENCE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MintEmerald
                )
            }

            // Central S.A.M. Core Badge
            Column(horizontalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, Brush.sweepGradient(listOf(CobaltPrimary, CyberViolet, MintEmerald, CobaltPrimary)), CircleShape)
                        .background(DarkSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "S.A.M.",
                        tint = CobaltPrimary,
                        modifier = Modifier.size(56.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "S.A.M.",
                    style = MaterialTheme.typography.headlineLarge,
                    color = TextOnSurface,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp
                )
                Text(
                    text = "SMART AUTONOMOUS MACHINE",
                    style = MaterialTheme.typography.labelMedium,
                    color = CobaltPrimary,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest),
                    border = BorderStroke(1.dp, HairlineBorderVariant),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "DEVELOPED BY SHLOK • BTECH AI SYSTEMS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MintEmerald,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "High-mobility Android AI IDE, Autonomous Agent refactoring, and natural multi-turn intelligence supporting Marathi, Hindi, and English.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            // Bottom CTA
            Button(
                onClick = onCompleteOnboarding,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = CobaltPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Enter Autonomous Workspace", color = DarkSurfaceLowest, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, tint = DarkSurfaceLowest)
            }
        }
    }
}
