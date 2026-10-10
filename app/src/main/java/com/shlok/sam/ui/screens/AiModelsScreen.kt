package com.shlok.sam.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.ui.theme.*
import com.shlok.sam.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiModelsScreen(
    viewModel: SettingsViewModel,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val currentProvider by viewModel.selectedProvider.collectAsState()
    val currentModel by viewModel.selectedModelId.collectAsState()
    val geminiKey by viewModel.geminiKey.collectAsState()
    val openaiKey by viewModel.openaiKey.collectAsState()
    val claudeKey by viewModel.claudeKey.collectAsState()

    var inputGeminiKey by remember(geminiKey) { mutableStateOf(geminiKey) }
    var inputOpenaiKey by remember(openaiKey) { mutableStateOf(openaiKey) }
    var inputClaudeKey by remember(claudeKey) { mutableStateOf(claudeKey) }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "AI PROVIDERS & KEY VAULT",
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = MintEmerald)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("DEVICE-LOCAL SECURE VAULT", style = MaterialTheme.typography.labelMedium, color = MintEmerald)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            "API keys are stored exclusively in your local Android Keystore / encrypted preferences. No keys are ever transmitted to third-party tracking servers.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }

            // Google Gemini Card
            item {
                ProviderKeyCard(
                    providerName = "Google Gemini",
                    description = "Gemini 2.5 Flash, 1.5 Pro, 1.5 Flash (Ultra-fast low-latency inference)",
                    apiKey = inputGeminiKey,
                    onKeyChange = { inputGeminiKey = it },
                    isSelected = currentProvider == "GEMINI",
                    onSelect = {
                        viewModel.setProvider("GEMINI")
                        viewModel.setModelId("gemini-1.5-flash")
                    },
                    onSaveKey = {
                        viewModel.setGeminiKey(inputGeminiKey)
                        Toast.makeText(context, "Gemini Key Saved", Toast.LENGTH_SHORT).show()
                    },
                    accent = CobaltPrimary
                )
            }

            // OpenAI Card
            item {
                ProviderKeyCard(
                    providerName = "OpenAI",
                    description = "GPT-4o, GPT-4o-mini, o1-mini reasoning engine",
                    apiKey = inputOpenaiKey,
                    onKeyChange = { inputOpenaiKey = it },
                    isSelected = currentProvider == "OPENAI",
                    onSelect = {
                        viewModel.setProvider("OPENAI")
                        viewModel.setModelId("gpt-4o-mini")
                    },
                    onSaveKey = {
                        viewModel.setOpenaiKey(inputOpenaiKey)
                        Toast.makeText(context, "OpenAI Key Saved", Toast.LENGTH_SHORT).show()
                    },
                    accent = MintEmerald
                )
            }

            // Anthropic Claude Card
            item {
                ProviderKeyCard(
                    providerName = "Anthropic Claude",
                    description = "Claude 3.5 Sonnet & Claude 3.5 Haiku (Deep code synthesis)",
                    apiKey = inputClaudeKey,
                    onKeyChange = { inputClaudeKey = it },
                    isSelected = currentProvider == "CLAUDE",
                    onSelect = {
                        viewModel.setProvider("CLAUDE")
                        viewModel.setModelId("claude-3-5-sonnet-20241022")
                    },
                    onSaveKey = {
                        viewModel.setClaudeKey(inputClaudeKey)
                        Toast.makeText(context, "Claude Key Saved", Toast.LENGTH_SHORT).show()
                    },
                    accent = CyberViolet
                )
            }

            // Offline Local Neural Core
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            viewModel.setProvider("LOCAL")
                            viewModel.setModelId("sam-neural-local")
                        },
                    colors = CardDefaults.cardColors(containerColor = if (currentProvider == "LOCAL") DarkSurfaceContainer else DarkSurface),
                    border = BorderStroke(1.dp, if (currentProvider == "LOCAL") MintEmerald else HairlineBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("S.A.M. Local Neural Engine (Offline/Direct)", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold, color = TextOnSurface)
                            Text("Zero-API instant fallback for offline code pairing & Marathi/Hindi conversations", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                        if (currentProvider == "LOCAL") {
                            Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = MintEmerald)
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProviderKeyCard(
    providerName: String,
    description: String,
    apiKey: String,
    onKeyChange: (String) -> Unit,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onSaveKey: () -> Unit,
    accent: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = if (isSelected) DarkSurfaceContainer else DarkSurface),
        border = BorderStroke(1.dp, if (isSelected) accent else HairlineBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = isSelected,
                        onClick = onSelect,
                        colors = RadioButtonDefaults.colors(selectedColor = accent)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = providerName, style = MaterialTheme.typography.headlineSmall, fontSize = 16.sp, color = TextOnSurface)
                }
            }
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(start = 8.dp))
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = apiKey,
                onValueChange = onKeyChange,
                label = { Text("API Key") },
                placeholder = { Text("Paste $providerName API Key...") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = DarkSurfaceLowest,
                    unfocusedContainerColor = DarkSurfaceLowest,
                    focusedBorderColor = accent,
                    unfocusedBorderColor = HairlineBorder,
                    focusedTextColor = TextOnSurface,
                    unfocusedTextColor = TextOnSurface
                ),
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onSaveKey,
                    colors = ButtonDefaults.buttonColors(containerColor = accent),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Save Key", color = DarkSurfaceLowest, style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
