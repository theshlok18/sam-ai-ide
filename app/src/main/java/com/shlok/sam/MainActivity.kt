package com.shlok.sam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.shlok.sam.navigation.SAMNavGraph
import com.shlok.sam.ui.theme.DarkBackground
import com.shlok.sam.ui.theme.SAMTheme
import com.shlok.sam.viewmodel.*

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = application as SAMApplication

        val homeViewModel by viewModels<HomeViewModel> {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return HomeViewModel(app.database.projectDao()) as T
                }
            }
        }

        val chatViewModel by viewModels<ChatViewModel> {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return ChatViewModel(app.database.chatDao(), app.conversationEngine) as T
                }
            }
        }

        val ideViewModel by viewModels<IdeViewModel> {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return IdeViewModel(app.workspaceManager, app.buildService) as T
                }
            }
        }

        val agentViewModel by viewModels<AgentViewModel> {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return AgentViewModel(app.agentEngine) as T
                }
            }
        }

        val voiceViewModel by viewModels<VoiceViewModel> {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return VoiceViewModel(app.voiceEngine) as T
                }
            }
        }

        val projectsViewModel by viewModels<ProjectsViewModel> {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return ProjectsViewModel(app.database.projectDao(), app.workspaceManager) as T
                }
            }
        }

        val settingsViewModel by viewModels<SettingsViewModel> {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return SettingsViewModel(app.preferenceManager) as T
                }
            }
        }

        val memoryViewModel by viewModels<MemoryViewModel> {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MemoryViewModel(app.database.memoryDao()) as T
                }
            }
        }

        setContent {
            val isOnboardingDone by app.preferenceManager.isOnboardingCompleted.collectAsState(initial = true)
            val navController = rememberNavController()

            SAMTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    SAMNavGraph(
                        navController = navController,
                        isOnboardingDone = isOnboardingDone,
                        homeViewModel = homeViewModel,
                        chatViewModel = chatViewModel,
                        ideViewModel = ideViewModel,
                        agentViewModel = agentViewModel,
                        voiceViewModel = voiceViewModel,
                        projectsViewModel = projectsViewModel,
                        settingsViewModel = settingsViewModel,
                        memoryViewModel = memoryViewModel
                    )
                }
            }
        }
    }
}
