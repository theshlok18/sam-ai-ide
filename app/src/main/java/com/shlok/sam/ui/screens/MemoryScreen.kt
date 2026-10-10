package com.shlok.sam.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.data.model.MemoryCategory
import com.shlok.sam.ui.theme.*
import com.shlok.sam.viewmodel.MemoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryScreen(
    viewModel: MemoryViewModel,
    onNavigateBack: () -> Unit
) {
    val memories by viewModel.memories.collectAsState()
    var inputKey by remember { mutableStateOf("") }
    var inputValue by remember { mutableStateOf("") }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "S.A.M. // MEMORY CORE & RULES",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextOnSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextOnSurface)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearAll() }) {
                        Icon(imageVector = Icons.Outlined.DeleteSweep, contentDescription = "Clear All", tint = StatusError)
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
            // New Rule / Memory Entry Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, HairlineBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "INJECT AUTONOMOUS RULE / KNOWLEDGE", style = MaterialTheme.typography.labelSmall, color = CobaltPrimary)
                        Spacer(modifier = Modifier.height(10.dp))
                        OutlinedTextField(
                            value = inputKey,
                            onValueChange = { inputKey = it },
                            label = { Text("Memory Topic / Constraint") },
                            placeholder = { Text("e.g. Architecture Preference") },
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
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            label = { Text("Rule Instruction") },
                            placeholder = { Text("e.g. Always use Jetpack Compose and Cybernetic Dark theme") },
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
                        Button(
                            onClick = {
                                if (inputKey.isNotBlank() && inputValue.isNotBlank()) {
                                    viewModel.addMemory(inputKey, inputValue, MemoryCategory.ARCHITECTURE)
                                    inputKey = ""
                                    inputValue = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = CobaltPrimary),
                            enabled = inputKey.isNotBlank() && inputValue.isNotBlank()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = DarkSurfaceLowest)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save into Vector Memory", color = DarkSurfaceLowest)
                        }
                    }
                }
            }

            item {
                Text(text = "PERSISTED SYSTEM MEMORIES & CONSTRAINTS", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
            }

            if (memories.isEmpty()) {
                item {
                    Text("No custom rules stored. Using default S.A.M. system instructions.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                }
            } else {
                items(memories) { mem ->
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = mem.key, style = MaterialTheme.typography.labelMedium, color = MintEmerald)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(text = mem.value, style = MaterialTheme.typography.bodyMedium, color = TextOnSurface)
                            }
                            IconButton(onClick = { viewModel.deleteMemory(mem) }) {
                                Icon(Icons.Outlined.Delete, contentDescription = "Delete", tint = TextMuted)
                            }
                        }
                    }
                }
            }
        }
    }
}
