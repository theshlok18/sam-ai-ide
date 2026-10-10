package com.shlok.sam.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shlok.sam.ui.screens.*
import com.shlok.sam.viewmodel.*

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Chat : Screen("chat")
    object Ide : Screen("ide")
    object Agent : Screen("agent")
    object Voice : Screen("voice")
    object Projects : Screen("projects")
    object NewProject : Screen("new_project")
    object AiModels : Screen("ai_models")
    object Memory : Screen("memory")
    object Permissions : Screen("permissions")
    object LivePreview : Screen("live_preview")
    object Settings : Screen("settings")
    object DeveloperProfile : Screen("developer_profile")
}

@Composable
fun SAMNavGraph(
    navController: NavHostController,
    isOnboardingDone: Boolean,
    homeViewModel: HomeViewModel,
    chatViewModel: ChatViewModel,
    ideViewModel: IdeViewModel,
    agentViewModel: AgentViewModel,
    voiceViewModel: VoiceViewModel,
    projectsViewModel: ProjectsViewModel,
    settingsViewModel: SettingsViewModel,
    memoryViewModel: MemoryViewModel
) {
    NavHost(
        navController = navController,
        startDestination = if (isOnboardingDone) Screen.Home.route else Screen.Onboarding.route
    ) {
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onCompleteOnboarding = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToChat = { navController.navigate(Screen.Chat.route) },
                onNavigateToIde = { navController.navigate(Screen.Ide.route) },
                onNavigateToAgent = { navController.navigate(Screen.Agent.route) },
                onNavigateToProjects = { navController.navigate(Screen.Projects.route) },
                onNavigateToVoice = { navController.navigate(Screen.Voice.route) },
                onNavigateToModels = { navController.navigate(Screen.AiModels.route) },
                onNavigateToMemory = { navController.navigate(Screen.Memory.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToProfile = { navController.navigate(Screen.DeveloperProfile.route) }
            )
        }
        composable(Screen.Chat.route) {
            ChatScreen(
                viewModel = chatViewModel,
                onNavigateBack = { navController.popBackStack() },
                onOpenInIde = { codeSnippet ->
                    ideViewModel.updateEditorContent(codeSnippet)
                    navController.navigate(Screen.Ide.route)
                }
            )
        }
        composable(Screen.Ide.route) {
            IdeScreen(
                viewModel = ideViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToLivePreview = { navController.navigate(Screen.LivePreview.route) }
            )
        }
        composable(Screen.Agent.route) {
            AgentScreen(
                viewModel = agentViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Voice.route) {
            VoiceAssistantScreen(
                viewModel = voiceViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Projects.route) {
            ProjectsScreen(
                viewModel = projectsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToNewProject = { navController.navigate(Screen.NewProject.route) },
                onOpenProjectInIde = { proj ->
                    ideViewModel.openProject(proj)
                    navController.navigate(Screen.Ide.route)
                }
            )
        }
        composable(Screen.NewProject.route) {
            NewProjectScreen(
                viewModel = projectsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onProjectCreated = { created ->
                    ideViewModel.openProject(created)
                    navController.navigate(Screen.Ide.route) {
                        popUpTo(Screen.Projects.route)
                    }
                }
            )
        }
        composable(Screen.AiModels.route) {
            AiModelsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Memory.route) {
            MemoryScreen(
                viewModel = memoryViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Permissions.route) {
            PermissionScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.LivePreview.route) {
            LivePreviewScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToAiModels = { navController.navigate(Screen.AiModels.route) },
                onNavigateToPermissions = { navController.navigate(Screen.Permissions.route) },
                onNavigateToProfile = { navController.navigate(Screen.DeveloperProfile.route) }
            )
        }
        composable(Screen.DeveloperProfile.route) {
            DeveloperProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
