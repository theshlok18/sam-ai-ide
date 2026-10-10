package com.shlok.sam.ai

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OfflineSimulationProvider : LLMProvider {
    override val providerName: String = "S.A.M. Local Neural Core (Offline/Direct)"

    override suspend fun generateResponse(request: LLMRequest): LLMResponse {
        val prompt = request.messages.lastOrNull()?.content ?: ""
        val reply = generateSmartAnswer(prompt)
        return LLMResponse(content = reply, model = request.modelId, isSuccess = true)
    }

    override fun streamResponse(request: LLMRequest): Flow<LLMStreamEvent> = flow {
        val prompt = request.messages.lastOrNull()?.content ?: ""
        val fullReply = generateSmartAnswer(prompt)
        val words = fullReply.split(" ")
        val accumulated = StringBuilder()

        for (word in words) {
            val chunk = "$word "
            accumulated.append(chunk)
            emit(LLMStreamEvent.Chunk(chunk))
            delay(35) // Smooth human-like typing cadence
        }
        emit(LLMStreamEvent.Complete(accumulated.toString()))
    }

    private fun generateSmartAnswer(userPrompt: String): String {
        val lower = userPrompt.lowercase()

        // Developer info
        if (lower.contains("who developed") || lower.contains("who made") || lower.contains("shlok") || lower.contains("creator") || lower.contains("kon banavla") || lower.contains("kisne banaya")) {
            return "I was developed as part of **S.A.M. (Smart Autonomous Machine)** by **Shlok**, a BTech student interested in AI and software development. My architecture is designed to pair-program, automate Android workflows, and assist you with intelligent code execution."
        }

        // Marathi queries
        if (lower.contains("namaskar") || lower.contains("kasa ahes") || lower.contains("kashi ahes") || lower.contains("kay challay") || lower.contains("marathi")) {
            return "नमस्कार! मी **S.A.M.** आहे - तुमचा Smart Autonomous Machine AI assistant. मी एकदम मजेत आहे! आज आपण कोणत्या Android किंवा AI project वर काम करणार आहोत? सांगा, मी तुमची मदत करायला तयार आहे."
        }

        // Hindi queries
        if (lower.contains("kaise ho") || lower.contains("kya haal") || lower.contains("kya kar sakte ho") || lower.contains("namaste")) {
            return "नमस्ते! मैं **S.A.M.** हूँ - Smart Autonomous Machine. मैं बहुत बढ़िया हूँ। आप आज किस प्रोजेक्ट या कोडिंग टास्क पर काम करना चाहते हैं? मैं Android डेवलपमेंट, AI आर्किटेक्चर, और ऑटोमेशन में आपकी पूरी मदद कर सकता हूँ।"
        }

        // Android / Kotlin questions
        if (lower.contains("compose") || lower.contains("kotlin") || lower.contains("viewmodel") || lower.contains("room")) {
            return """Here is a clean, production-ready Kotlin Jetpack Compose implementation:

```kotlin
@Composable
fun SmartStatusCard(
    title: String,
    status: String,
    isActive: Boolean,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF11151D)
        ),
        border = BorderStroke(1.dp, Color(0xFF232B3B)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color(0xFFE2E2E9)
                )
                Text(
                    text = status,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isActive) Color(0xFF42D6A4) else Color(0xFF8D909F)
                )
            }
            IconButton(onClick = onAction) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Run",
                    tint = Color(0xFF5685FF)
                )
            }
        }
    }
}
```

This adheres to S.A.M.'s architectural zero-glow cybernetic dark theme and clean state hoisting principles."""
        }

        // Autonomous Agent / IDE questions
        if (lower.contains("agent") || lower.contains("refactor") || lower.contains("build") || lower.contains("fix")) {
            return "S.A.M. Agent mode is ready. I can inspect your workspace files, create an autonomous execution plan, run AST verifications, generate multi-file diffs, and await your one-tap approval before committing changes. Submit a task in Agent Mode or IDE Workspace to get started."
        }

        // Default natural response
        return "I am **S.A.M.** (Smart Autonomous Machine). I can assist you with full-stack Android development, real-time code editing, autonomous agent workflows, voice assistance, and AI system architecture. How can I help you build today?"
    }
}
