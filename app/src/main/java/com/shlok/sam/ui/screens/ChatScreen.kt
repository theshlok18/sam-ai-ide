package com.shlok.sam.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.data.model.ChatMessage
import com.shlok.sam.data.model.MessageRole
import com.shlok.sam.ui.theme.*
import com.shlok.sam.viewmodel.ChatViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onNavigateBack: () -> Unit,
    onOpenInIde: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val messages by viewModel.messages.collectAsState()
    val isGenerating by viewModel.isGenerating.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

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
                                .background(if (isGenerating) CyberViolet else MintEmerald)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "S.A.M. // AI CHAT PAIR",
                                style = MaterialTheme.typography.labelLarge,
                                color = TextOnSurface
                            )
                            Text(
                                text = if (isGenerating) "Generating response..." else "Gemini 1.5 Pro / Neural Core",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isGenerating) CyberViolet else TextSecondary
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextOnSurface)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.startNewConversation() }) {
                        Icon(imageVector = Icons.Default.AddComment, contentDescription = "New Chat", tint = CobaltPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        bottomBar = {
            // Message Composer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface)
                    .border(BorderStroke(1.dp, HairlineBorder))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                // Quick prompt chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickChatChip("मराठीत बोला") { inputText = "नमस्कार, मला Android App बद्दल माहिती दे." }
                    QuickChatChip("Hindi") { inputText = "नमस्ते, इस कोड को ऑप्टिमाइज़ करें." }
                    QuickChatChip("Explain Code") { inputText = "Can you explain this Jetpack Compose architecture?" }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "Ask S.A.M. in Marathi, Hindi or English...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextMuted
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = DarkSurfaceContainer,
                            unfocusedContainerColor = DarkSurfaceContainer,
                            focusedBorderColor = CobaltPrimary,
                            unfocusedBorderColor = HairlineBorder,
                            focusedTextColor = TextOnSurface,
                            unfocusedTextColor = TextOnSurface
                        ),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 4
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank() && !isGenerating) {
                                val text = inputText
                                inputText = ""
                                viewModel.sendMessage(text)
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (inputText.isNotBlank() && !isGenerating) CobaltPrimary else DarkSurfaceContainerHigh,
                                CircleShape
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = if (inputText.isNotBlank() && !isGenerating) DarkSurfaceLowest else TextMuted
                        )
                    }
                }
            }
        }
    ) { padding ->
        if (messages.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .background(DarkSurfaceContainer, CircleShape)
                            .border(1.dp, HairlineBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = CobaltPrimary, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("S.A.M. Neural Assistant", style = MaterialTheme.typography.headlineSmall, color = TextOnSurface)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Ask any coding question, generate complete Android modules, or converse naturally in Marathi, Hindi & English.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(messages) { msg ->
                    MessageBubble(
                        message = msg,
                        onCopy = { text ->
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("SAM Response", text))
                            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                        },
                        onOpenCodeInIde = onOpenInIde
                    )
                }
            }
        }
    }
}

@Composable
fun MessageBubble(
    message: ChatMessage,
    onCopy: (String) -> Unit,
    onOpenCodeInIde: (String) -> Unit
) {
    val isUser = message.role == MessageRole.USER

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(DarkSurfaceContainerHigh)
                    .border(1.dp, HairlineBorder, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("S", style = MaterialTheme.typography.labelSmall, color = CobaltPrimary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(
            modifier = Modifier
                .widthIn(max = 320.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isUser) CobaltPrimary.copy(alpha = 0.2f) else DarkSurface)
                .border(1.dp, if (isUser) CobaltPrimary.copy(alpha = 0.5f) else HairlineBorder, RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            if (message.content.contains("```")) {
                // Split code blocks
                val parts = message.content.split("```")
                parts.forEachIndexed { index, part ->
                    if (index % 2 == 1) {
                        // Code block
                        val lines = part.trim().lines()
                        val lang = if (lines.isNotEmpty() && lines.first().matches(Regex("^[a-zA-Z0-9_-]+$"))) lines.first() else "kotlin"
                        val code = if (lines.isNotEmpty() && lines.first().matches(Regex("^[a-zA-Z0-9_-]+$"))) lines.drop(1).joinToString("\n") else part.trim()

                        CodeBlockView(language = lang, code = code, onCopy = onCopy, onOpenInIde = onOpenCodeInIde)
                    } else {
                        if (part.isNotBlank()) {
                            Text(
                                text = part.trim(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextOnSurface
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = message.content.ifEmpty { if (message.isStreaming) "Thinking..." else "" },
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (message.isError) StatusError else TextOnSurface
                )
            }

            if (!isUser && message.content.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(
                        onClick = { onCopy(message.content) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy", tint = TextMuted, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CodeBlockView(
    language: String,
    code: String,
    onCopy: (String) -> Unit,
    onOpenInIde: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest),
        border = BorderStroke(1.dp, HairlineBorderVariant),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceContainerHigh)
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = language.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MintEmerald
                )
                Row {
                    TextButton(onClick = { onCopy(code) }, contentPadding = PaddingValues(0.dp)) {
                        Text("Copy", color = TextSecondary, style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    TextButton(onClick = { onOpenInIde(code) }, contentPadding = PaddingValues(0.dp)) {
                        Text("Open in IDE", color = CobaltPrimary, style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
            Text(
                text = code,
                style = MaterialTheme.typography.labelMedium,
                color = PrimaryFixed,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Composable
fun QuickChatChip(label: String, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .clickable { onClick() },
        color = DarkSurfaceContainerHigh,
        border = BorderStroke(1.dp, HairlineBorderVariant)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = PrimaryDim,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
