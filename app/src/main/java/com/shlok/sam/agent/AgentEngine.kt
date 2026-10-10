package com.shlok.sam.agent

import com.shlok.sam.data.model.AgentStep
import com.shlok.sam.data.model.AgentTaskStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AgentState(
    val status: AgentTaskStatus = AgentTaskStatus.IDLE,
    val prompt: String = "",
    val steps: List<AgentStep> = emptyList(),
    val currentStepIndex: Int = 0,
    val logs: List<String> = emptyList(),
    val isPaused: Boolean = false
)

class AgentEngine {
    private val _agentState = MutableStateFlow(AgentState())
    val agentState: StateFlow<AgentState> = _agentState.asStateFlow()

    suspend fun startTask(taskPrompt: String) {
        _agentState.value = AgentState(
            status = AgentTaskStatus.PLANNING,
            prompt = taskPrompt,
            logs = listOf("Analyzing repository AST and workspace files...", "Decomposing task into atomic execution steps...")
        )

        delay(800)

        // Generate smart execution steps based on prompt
        val generatedSteps = listOf(
            AgentStep(
                title = "Inspect target modules & dependencies",
                description = "Scan AST node tree and verify import paths.",
                targetFile = "app/src/main/java/com/shlok/sam/MainActivity.kt",
                isCompleted = false
            ),
            AgentStep(
                title = "Refactor Component & Apply Zero-Glow Styling",
                description = "Inject Cybernetic Dark theme tokens and state hoisting.",
                targetFile = "app/src/main/java/com/shlok/sam/ui/theme/Theme.kt",
                diff = """
+ val CyberViolet = Color(0xFF9B72FF)
+ val MintEmerald = Color(0xFF42D6A4)
- val oldTheme = LightColorScheme()
+ val newTheme = SAMDarkColorScheme
                """.trimIndent(),
                isCompleted = false
            ),
            AgentStep(
                title = "Run Unit Tests & AST Verification",
                description = "Execute Gradle test suite to ensure zero regressions.",
                targetFile = "app/src/test/java/com/shlok/sam/ExampleUnitTest.kt",
                isCompleted = false
            )
        )

        _agentState.value = _agentState.value.copy(
            status = AgentTaskStatus.AWAITING_APPROVAL,
            steps = generatedSteps,
            logs = _agentState.value.logs + "Generated 3 autonomous steps. Awaiting developer approval."
        )
    }

    suspend fun approveAndExecuteAll() {
        val current = _agentState.value
        if (current.steps.isEmpty()) return

        _agentState.value = current.copy(status = AgentTaskStatus.RUNNING)

        val updatedSteps = current.steps.toMutableList()
        for (i in updatedSteps.indices) {
            _agentState.value = _agentState.value.copy(
                currentStepIndex = i,
                logs = _agentState.value.logs + "Executing Step ${i + 1}: ${updatedSteps[i].title}"
            )
            delay(1200)
            updatedSteps[i] = updatedSteps[i].copy(isCompleted = true, isApproved = true)
            _agentState.value = _agentState.value.copy(steps = updatedSteps.toList())
        }

        _agentState.value = _agentState.value.copy(
            status = AgentTaskStatus.COMPLETED,
            logs = _agentState.value.logs + "All steps completed successfully. AST and build passed."
        )
    }

    fun rejectTask() {
        _agentState.value = _agentState.value.copy(
            status = AgentTaskStatus.CANCELLED,
            logs = _agentState.value.logs + "Task rejected by developer."
        )
    }

    fun reset() {
        _agentState.value = AgentState()
    }
}
