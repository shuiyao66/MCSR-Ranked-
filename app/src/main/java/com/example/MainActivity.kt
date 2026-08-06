package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.AppStrings
import com.example.data.LocalAppStrings
import com.example.ui.MainViewModel
import com.example.ui.components.AppDialogsAndSheets
import com.example.ui.components.CustomBackgroundContainer
import com.example.ui.screens.BrowserScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.MCSRRankedTheme

enum class MainNavTab {
    HOME,
    BROWSER,
    SETTINGS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: MainViewModel = viewModel()
            val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
            val themeConfig by viewModel.themeConfig.collectAsStateWithLifecycle()
            val userMessage by viewModel.userMessage.collectAsStateWithLifecycle()

            val appStrings = remember(currentLanguage) { AppStrings(currentLanguage) }
            val snackbarHostState = remember { SnackbarHostState() }

            var currentTab by remember { mutableStateOf(MainNavTab.HOME) }

            LaunchedEffect(userMessage) {
                userMessage?.let {
                    snackbarHostState.showSnackbar(it)
                    viewModel.clearUserMessage()
                }
            }

            CompositionLocalProvider(LocalAppStrings provides appStrings) {
                MCSRRankedTheme(themeConfig = themeConfig) {
                    CustomBackgroundContainer(themeConfig = themeConfig) {
                        Scaffold(
                            bottomBar = {
                                NavigationBar(
                                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                                    tonalElevation = 8.dp,
                                    windowInsets = WindowInsets.navigationBars
                                ) {
                                    NavigationBarItem(
                                        selected = currentTab == MainNavTab.HOME,
                                        onClick = { currentTab = MainNavTab.HOME },
                                        icon = { Icon(Icons.Default.Home, contentDescription = appStrings.homeTab) },
                                        label = { Text(appStrings.homeTab) },
                                        modifier = Modifier.testTag("nav_tab_home")
                                    )

                                    NavigationBarItem(
                                        selected = currentTab == MainNavTab.BROWSER,
                                        onClick = { currentTab = MainNavTab.BROWSER },
                                        icon = { Icon(Icons.Default.Language, contentDescription = appStrings.browserTab) },
                                        label = { Text(appStrings.browserTab) },
                                        modifier = Modifier.testTag("nav_tab_browser")
                                    )

                                    NavigationBarItem(
                                        selected = currentTab == MainNavTab.SETTINGS,
                                        onClick = { currentTab = MainNavTab.SETTINGS },
                                        icon = { Icon(Icons.Default.Settings, contentDescription = appStrings.settingsTab) },
                                        label = { Text(appStrings.settingsTab) },
                                        modifier = Modifier.testTag("nav_tab_settings")
                                    )
                                }
                            },
                            snackbarHost = { SnackbarHost(snackbarHostState) },
                            containerColor = Color.Transparent,
                            modifier = Modifier.fillMaxSize()
                        ) { innerPadding ->
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            ) {
                                AnimatedContent(
                                    targetState = currentTab,
                                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                                    label = "ScreenTransition"
                                ) { target ->
                                    when (target) {
                                        MainNavTab.HOME -> {
                                            HomeScreen(
                                                viewModel = viewModel,
                                                onNavigateToBrowser = { url ->
                                                    viewModel.openUrl(url)
                                                    currentTab = MainNavTab.BROWSER
                                                }
                                            )
                                        }
                                        MainNavTab.BROWSER -> {
                                            BrowserScreen(viewModel = viewModel)
                                        }
                                        MainNavTab.SETTINGS -> {
                                            SettingsScreen(viewModel = viewModel)
                                        }
                                    }
                                }

                                // Global sheets and dialogs
                                AppDialogsAndSheets(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
