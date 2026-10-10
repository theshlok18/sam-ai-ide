package com.shlok.sam.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shlok.sam.agent.AgentState
import com.shlok.sam.data.model.AgentStep
import com.shlok.sam.data.model.AgentTaskStatus
import com.shlok.sam.ui.theme.*
import com.shlok.sam.viewmodel.AgentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentScreen(
    viewModel: AgentViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.agentState.collectAsState()
    var taskInput by remember { mutableStateOf("") }

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
                                .background(
                                    when (state.status) {
                                        AgentTaskStatus.RUNNING -> MintEmerald
                                        AgentTaskStatus.AWAITING_APPROVAL -> StatusWarning
                                        AgentTaskStatus.COMPLETED -> MintEmerald
                                        AgentTaskStatus.CANCELLED, AgentTaskStatus.FAILED -> StatusError
                                        else -> CobaltPrimary
                                    }
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "S.A.M. // AGENT MODE",
                            style = MaterialTheme.typography.labelLarge,
                            color = TextOnSurface
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextOnSurface)
                    }
                },
                actions = {
                    if (state.status != AgentTaskStatus.IDLE) {
                        IconButton(onClick = { viewModel.reset() }) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = "Reset", tint = TextSecondary)
                        }
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
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Task Prompt Input Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = BorderStroke(1.dp, HairlineBorder),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "AUTONOMOUS TASK SPECIFICATION",
                            style = MaterialTheme.typography.labelSmall,
                            color = CobaltPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = taskInput,
                            onValueChange = { taskInput = it },
                            placeholder = {
                                Text(
                                    "E.g.: Refactor Theme to Zero-Glow Cyber Dark, apply Compose tokens, and run AST unit tests.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextMuted
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkSurfaceContainer,
                                unfocusedContainerColor = DarkSurfaceContainer,
                                focusedBorderColor = CobaltPrimary,
                                unfocusedBorderColor = HairlineBorder,
                                focusedTextColor = TextOnSurface,
                                unfocusedTextColor = TextOnSurface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                if (taskInput.isNotBlank()) {
                                    viewModel.startTask(taskInput)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = CobaltPrimary),
                            enabled = state.status == AgentTaskStatus.IDLE || state.status == AgentTaskStatus.COMPLETED || state.status == AgentTaskStatus.CANCELLED
                        ) {
                            Icon(Icons.Default.AutoMode, contentDescription = null, tint = DarkSurfaceLowest)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Initialize Autonomous Plan", color = DarkSurfaceLowest, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }

            // Status Banner & Action Buttons
            if (state.status != AgentTaskStatus.IDLE) {
                item {
                    AgentStatusCard(
                        state = state,
                        onApprove = { viewModel.approveTask() },
                        onReject = { viewModel.rejectTask() }
                    )
                }
            }

            // Steps Timeline
            if (state.steps.isNotEmpty()) {
                item {
                    Text(
                        text = "EXECUTION TIMELINE & AST DIFF",
                        style = MaterialTheme.typography.labelMedium,
                        color = TextSecondary
                    )
                }
                items(state.steps) { step ->
                    AgentStepCard(step = step)
                }
            }

            // Logs Card
            if (state.logs.isNotEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceLowest),
                        border = BorderStroke(1.dp, HairlineBorderVariant),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "AGENT TELEMETRY LOGS", style = MaterialTheme.typography.labelSmall, color = MintEmerald)
                            Spacer(modifier = Modifier.height(6.dp))
                            state.logs.forEach { log ->
                                Text(text = "> $log", style = MaterialTheme.typography.labelSmall, color = TextOnSurfaceVariant)
                            }
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
fun AgentStatusCard(
    state: AgentState,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceContainer),
        border = BorderStroke(
            1.dp,
            when (state.status) {
                AgentTaskStatus.AWAITING_APPROVAL -> StatusWarning
                AgentTaskStatus.RUNNING -> CobaltPrimary
                AgentTaskStatus.COMPLETED -> MintEmerald
                else -> HairlineBorder
            }
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STATUS: ${state.status.name}",
                    style = MaterialTheme.typography.labelMedium,
                    color = when (state.status) {
                        AgentTaskStatus.AWAITING_APPROVAL -> StatusWarning
                        AgentTaskStatus.RUNNING -> CobaltPrimary
                        AgentTaskStatus.COMPLETED -> MintEmerald
                        else -> TextOnSurface
                    },
                    fontWeight = FontWeight.Bold
                )
                if (state.status == AgentTaskStatus.RUNNING) {
                    CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = CobaltPrimary)
                }
            }

            if (state.status == AgentTaskStatus.AWAITING_APPROVAL) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Review the proposed AST steps and code diffs below before granting execution clearance.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MintEmerald)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = DarkSurfaceLowest)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Approve & Run", color = DarkSurfaceLowest)
                    }
                    OutlinedButton(
                        onClick = onReject,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = StatusError),
                        border = BorderStroke(1.dp, StatusError.copy(alpha = 0.5f))
                    ) {
                        Text("Reject")
                    }
                }
            }
        }
    }
}

@Composable
fun AgentStepCard(step: AgentStep) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, HairlineBorder),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(if (step.isCompleted) MintEmerald else DarkSurfaceContainerHigh, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (step.isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = DarkSurfaceLowest, modifier = Modifier.size(14.dp))
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = step.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = TextOnSurface)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = step.description, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            if (step.targetFile != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Target: ${step.targetFile}", style = MaterialTheme.typography.labelSmall, color = CobaltPrimary)
            }
            if (step.diff != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceLowest, RoundedCornerShape(4.dp))
                        .border(1.dp, HairlineBorderVariant, RoundedCornerShape(4.dp))
                        .padding(8.dp)
                ) {
                    Text(text = step.diff, style = MaterialTheme.typography.labelSmall, color = MintEmerald)
                }
            }
        }
    }
}
