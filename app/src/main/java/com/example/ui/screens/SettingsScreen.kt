package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = LocalAppStrings.current
    val scrollState = rememberScrollState()

    val currentLanguage by viewModel.currentLanguage.collectAsStateWithLifecycle()
    val themeConfig by viewModel.themeConfig.collectAsStateWithLifecycle()
    val startupUrl by viewModel.startupUrl.collectAsStateWithLifecycle()
    val isDesktopMode by viewModel.isDesktopMode.collectAsStateWithLifecycle()

    var showLanguagePicker by remember { mutableStateOf(false) }
    var showStartupPicker by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showUpdateDialog by remember { mutableStateOf(false) }
    var showClearCacheConfirm by remember { mutableStateOf(false) }
    var showClearCookiesConfirm by remember { mutableStateOf(false) }

    // Custom background image picker launcher
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.updateThemeConfig(themeConfig.copy(backgroundImageUri = uri.toString()))
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = strings.settingsTab,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent,
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 1. Language & General Settings
            SettingsSectionHeader(title = strings.appLanguageSetting, icon = Icons.Default.Language)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text(strings.appLanguageSetting) },
                        supportingContent = { Text("${currentLanguage.nativeName} (${currentLanguage.displayName})") },
                        leadingContent = { Icon(Icons.Default.Translate, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                        modifier = Modifier.clickable { showLanguagePicker = true }.testTag("setting_language")
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ListItem(
                        headlineContent = { Text(strings.defaultStartupWebsite) },
                        supportingContent = {
                            Text(
                                when (startupUrl) {
                                    AppPreferencesRepository.URL_MCSR_RANKED -> strings.mcsrRankedTitle
                                    AppPreferencesRepository.URL_RANKALYTICS -> strings.rankalyticsTitle
                                    else -> startupUrl
                                }
                            )
                        },
                        leadingContent = { Icon(Icons.Default.Launch, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                        modifier = Modifier.clickable { showStartupPicker = true }.testTag("setting_startup_url")
                    )
                }
            }

            // 2. Theme Customization Section
            SettingsSectionHeader(title = strings.themeSettings, icon = Icons.Default.Palette)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Primary Theme Color selector
                    Text(
                        text = strings.primaryColor,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(PrimaryThemeColor.values().toList()) { colorPreset ->
                            val isSelected = themeConfig.primaryThemeColor == colorPreset
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(colorPreset.primary)
                                    .then(
                                        if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
                                        else Modifier
                                    )
                                    .clickable {
                                        viewModel.updateThemeConfig(themeConfig.copy(primaryThemeColor = colorPreset))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                                }
                            }
                        }
                    }

                    HorizontalDivider()

                    // Dark Mode & AMOLED Mode Toggles
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = strings.darkMode, style = MaterialTheme.typography.bodyLarge)
                            Text(text = strings.amoledMode, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Switch(
                            checked = themeConfig.themeMode == ThemeMode.DARK,
                            onCheckedChange = { isDark ->
                                viewModel.updateThemeConfig(
                                    themeConfig.copy(themeMode = if (isDark) ThemeMode.DARK else ThemeMode.LIGHT)
                                )
                            },
                            modifier = Modifier.testTag("setting_dark_mode_switch")
                        )
                    }

                    if (themeConfig.themeMode == ThemeMode.DARK) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = strings.amoledMode, style = MaterialTheme.typography.bodyMedium)
                            Switch(
                                checked = themeConfig.isAmoledMode,
                                onCheckedChange = { isAmoled ->
                                    viewModel.updateThemeConfig(themeConfig.copy(isAmoledMode = isAmoled))
                                },
                                modifier = Modifier.testTag("setting_amoled_switch")
                            )
                        }
                    }

                    HorizontalDivider()

                    // Custom Background Image Options
                    Text(
                        text = strings.customBackground,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { photoPickerLauncher.launch("image/*") },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Image, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.selectImage)
                        }

                        if (themeConfig.backgroundImageUri != null) {
                            OutlinedButton(
                                onClick = { viewModel.updateThemeConfig(themeConfig.copy(backgroundImageUri = null)) }
                            ) {
                                Text(strings.removeBackground)
                            }
                        }
                    }

                    if (themeConfig.backgroundImageUri != null) {
                        // Blur Radius Slider
                        Column {
                            Text(
                                text = "${strings.blurRadius}: ${themeConfig.backgroundBlurRadiusDp.toInt()} dp",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Slider(
                                value = themeConfig.backgroundBlurRadiusDp,
                                onValueChange = { blur ->
                                    viewModel.updateThemeConfig(themeConfig.copy(backgroundBlurRadiusDp = blur))
                                },
                                valueRange = 0f..25f
                            )
                        }

                        // Opacity Slider
                        Column {
                            Text(
                                text = "${strings.backgroundOpacity}: ${(themeConfig.backgroundOpacity * 100).toInt()}%",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Slider(
                                value = themeConfig.backgroundOpacity,
                                onValueChange = { op ->
                                    viewModel.updateThemeConfig(themeConfig.copy(backgroundOpacity = op))
                                },
                                valueRange = 0.1f..1.0f
                            )
                        }
                    }
                }
            }

            // 3. Browser & Cache Settings
            SettingsSectionHeader(title = strings.browserOptions, icon = Icons.Default.Public)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text(strings.desktopMode) },
                        leadingContent = { Icon(Icons.Default.DesktopWindows, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = {
                            Switch(
                                checked = isDesktopMode,
                                onCheckedChange = { viewModel.toggleDesktopMode() }
                            )
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ListItem(
                        headlineContent = { Text(strings.downloads) },
                        leadingContent = { Icon(Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
                        modifier = Modifier.clickable { viewModel.setShowDownloadsSheet(true) }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ListItem(
                        headlineContent = { Text(strings.clearCache, color = MaterialTheme.colorScheme.error) },
                        leadingContent = { Icon(Icons.Default.CleaningServices, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        modifier = Modifier.clickable { showClearCacheConfirm = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ListItem(
                        headlineContent = { Text(strings.clearCookies, color = MaterialTheme.colorScheme.error) },
                        leadingContent = { Icon(Icons.Default.Cookie, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
                        modifier = Modifier.clickable { showClearCookiesConfirm = true }
                    )
                }
            }

            // 4. About & Legal Info
            SettingsSectionHeader(title = strings.aboutApp, icon = Icons.Default.Info)

            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text(strings.aboutApp) },
                        supportingContent = { Text(strings.version) },
                        leadingContent = { Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.clickable { showAboutDialog = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ListItem(
                        headlineContent = { Text(strings.privacyPolicy) },
                        leadingContent = { Icon(Icons.Default.PrivacyTip, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.clickable { showPrivacyDialog = true }
                    )

                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                    ListItem(
                        headlineContent = { Text(strings.checkUpdates) },
                        leadingContent = { Icon(Icons.Default.SystemUpdate, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        modifier = Modifier.clickable { showUpdateDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialog 1: Application Language Picker
    if (showLanguagePicker) {
        AlertDialog(
            onDismissRequest = { showLanguagePicker = false },
            title = { Text(strings.appLanguageSetting, fontWeight = FontWeight.Bold) },
            text = {
                Column(modifier = Modifier.height(320.dp)) {
                    LazyColumn {
                        items(AppLanguage.values().toList()) { lang ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        viewModel.setLanguage(lang)
                                        showLanguagePicker = false
                                    }
                                    .padding(vertical = 12.dp, horizontal = 12.dp)
                            ) {
                                RadioButton(
                                    selected = (currentLanguage == lang),
                                    onClick = {
                                        viewModel.setLanguage(lang)
                                        showLanguagePicker = false
                                    }
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(text = lang.nativeName, fontWeight = FontWeight.SemiBold)
                                    Text(text = lang.displayName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLanguagePicker = false }) { Text(strings.cancel) }
            }
        )
    }

    // Dialog 2: Startup Website Picker
    if (showStartupPicker) {
        AlertDialog(
            onDismissRequest = { showStartupPicker = false },
            title = { Text(strings.defaultStartupWebsite, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ListItem(
                        headlineContent = { Text(strings.mcsrRankedTitle) },
                        leadingContent = { RadioButton(selected = (startupUrl == AppPreferencesRepository.URL_MCSR_RANKED), onClick = null) },
                        modifier = Modifier.clickable {
                            viewModel.setStartupUrl(AppPreferencesRepository.URL_MCSR_RANKED)
                            showStartupPicker = false
                        }
                    )
                    ListItem(
                        headlineContent = { Text(strings.rankalyticsTitle) },
                        leadingContent = { RadioButton(selected = (startupUrl == AppPreferencesRepository.URL_RANKALYTICS), onClick = null) },
                        modifier = Modifier.clickable {
                            viewModel.setStartupUrl(AppPreferencesRepository.URL_RANKALYTICS)
                            showStartupPicker = false
                        }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showStartupPicker = false }) { Text(strings.cancel) }
            }
        )
    }

    // Dialog 3: About Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            icon = { Icon(Icons.Default.SportsEsports, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp)) },
            title = { Text("MCSR Ranked App", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = strings.version, style = MaterialTheme.typography.labelMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = strings.mcsrRankedDesc)
                    Text(text = strings.rankalyticsDesc)
                }
            },
            confirmButton = {
                Button(onClick = { showAboutDialog = false }) { Text(strings.confirm) }
            }
        )
    }

    // Dialog 4: Privacy Policy
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text(strings.privacyPolicy, fontWeight = FontWeight.Bold) },
            text = {
                Text(
                    text = "MCSR Ranked application values your privacy. Browsing data, cookies, bookmarks, and settings are stored locally on your device. No personal data is tracked or shared with third parties.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(onClick = { showPrivacyDialog = false }) { Text(strings.confirm) }
            }
        )
    }

    // Dialog 5: Check Updates
    if (showUpdateDialog) {
        AlertDialog(
            onDismissRequest = { showUpdateDialog = false },
            title = { Text(strings.checkUpdates, fontWeight = FontWeight.Bold) },
            text = { Text(strings.latestVersionMsg) },
            confirmButton = {
                Button(onClick = { showUpdateDialog = false }) { Text(strings.confirm) }
            }
        )
    }

    // Dialog 6: Clear Cache Confirm
    if (showClearCacheConfirm) {
        AlertDialog(
            onDismissRequest = { showClearCacheConfirm = false },
            title = { Text(strings.clearCache, fontWeight = FontWeight.Bold) },
            text = { Text("确定要清除浏览器缓存吗？/ Are you sure you want to clear browser cache?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearBrowserCache(null)
                        showClearCacheConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(strings.confirm)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCacheConfirm = false }) { Text(strings.cancel) }
            }
        )
    }

    // Dialog 7: Clear Cookies Confirm
    if (showClearCookiesConfirm) {
        AlertDialog(
            onDismissRequest = { showClearCookiesConfirm = false },
            title = { Text(strings.clearCookies, fontWeight = FontWeight.Bold) },
            text = { Text("确定要清除所有 Cookie 与登录会话吗？/ Clear all cookies and sessions?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearBrowserCookies()
                        showClearCookiesConfirm = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(strings.confirm)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCookiesConfirm = false }) { Text(strings.cancel) }
            }
        )
    }
}

@Composable
fun SettingsSectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
