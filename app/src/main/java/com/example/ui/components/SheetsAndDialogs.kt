package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.*
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDialogsAndSheets(viewModel: MainViewModel) {
    val strings = LocalAppStrings.current

    val showTabsSheet by viewModel.showTabsSheet.collectAsStateWithLifecycle()
    val showBookmarksSheet by viewModel.showBookmarksSheet.collectAsStateWithLifecycle()
    val showHistorySheet by viewModel.showHistorySheet.collectAsStateWithLifecycle()
    val showDownloadsSheet by viewModel.showDownloadsSheet.collectAsStateWithLifecycle()
    val showTranslateDialog by viewModel.showTranslateDialog.collectAsStateWithLifecycle()
    val showTextTranslateDialog by viewModel.showTextTranslateDialog.collectAsStateWithLifecycle()

    // 1. Tabs Overview Sheet
    if (showTabsSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setShowTabsSheet(false) },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            TabsOverviewContent(
                viewModel = viewModel,
                onDismiss = { viewModel.setShowTabsSheet(false) }
            )
        }
    }

    // 2. Bookmarks Sheet
    if (showBookmarksSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setShowBookmarksSheet(false) },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            BookmarksContent(
                viewModel = viewModel,
                onDismiss = { viewModel.setShowBookmarksSheet(false) }
            )
        }
    }

    // 3. History Sheet
    if (showHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setShowHistorySheet(false) },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            HistoryContent(
                viewModel = viewModel,
                onDismiss = { viewModel.setShowHistorySheet(false) }
            )
        }
    }

    // 4. Downloads Sheet
    if (showDownloadsSheet) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.setShowDownloadsSheet(false) },
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            DownloadsContent(
                viewModel = viewModel,
                onDismiss = { viewModel.setShowDownloadsSheet(false) }
            )
        }
    }

    // 5. Webpage Translate Dialog
    if (showTranslateDialog) {
        WebpageTranslateDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setShowTranslateDialog(false) }
        )
    }

    // 6. Selected Text Translate Dialog
    if (showTextTranslateDialog) {
        TextTranslateDialog(
            viewModel = viewModel,
            onDismiss = { viewModel.setShowTextTranslateDialog(false) }
        )
    }
}

@Composable
fun TabsOverviewContent(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val strings = LocalAppStrings.current
    val tabsList by viewModel.tabsList.collectAsStateWithLifecycle()
    val activeTabId by viewModel.activeTabId.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${strings.tabsOverview} (${tabsList.size})",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            Row {
                TextButton(onClick = { viewModel.closeAllTabs() }) {
                    Text(text = strings.closeAllTabs, color = MaterialTheme.colorScheme.error)
                }

                IconButton(
                    onClick = {
                        viewModel.addNewTab()
                        onDismiss()
                    }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "New Tab")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 400.dp)
        ) {
            items(tabsList, key = { it.id }) { tab ->
                val isActive = tab.id == activeTabId
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .clickable {
                            viewModel.selectTab(tab.id)
                            onDismiss()
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = tab.title.ifEmpty { "New Tab" },
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { viewModel.closeTab(tab.id) },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Close Tab", modifier = Modifier.size(16.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = tab.url,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun BookmarksContent(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val strings = LocalAppStrings.current
    val bookmarks by viewModel.bookmarksList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = strings.bookmarks,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (bookmarks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无书签 / No Bookmarks",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(bookmarks, key = { it.id }) { item ->
                    ListItem(
                        headlineContent = { Text(item.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        supportingContent = { Text(item.url, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        leadingContent = {
                            Icon(Icons.Default.Bookmark, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        trailingContent = {
                            IconButton(onClick = { viewModel.deleteBookmark(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.openUrl(item.url)
                                onDismiss()
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun HistoryContent(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val strings = LocalAppStrings.current
    val history by viewModel.historyList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = strings.history,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )

            if (history.isNotEmpty()) {
                TextButton(onClick = { viewModel.clearHistory() }) {
                    Text(text = strings.clearCache, color = MaterialTheme.colorScheme.error)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (history.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无历史记录 / No History",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(history, key = { it.id }) { item ->
                    ListItem(
                        headlineContent = { Text(item.title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        supportingContent = { Text(item.url, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        leadingContent = {
                            Icon(Icons.Default.History, contentDescription = null)
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                viewModel.openUrl(item.url)
                                onDismiss()
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun DownloadsContent(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val strings = LocalAppStrings.current
    val downloads by viewModel.downloadsList.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = strings.downloads,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (downloads.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "暂无下载记录 / No Downloads",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
            ) {
                items(downloads, key = { it.id }) { item ->
                    ListItem(
                        headlineContent = { Text(item.fileName, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        supportingContent = { Text(item.status, style = MaterialTheme.typography.labelMedium) },
                        leadingContent = {
                            Icon(Icons.Default.DownloadDone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        trailingContent = {
                            IconButton(onClick = { viewModel.deleteDownloadRecord(item.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    )
                }
            }
        }
    }
}

@Composable
fun WebpageTranslateDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val strings = LocalAppStrings.current
    var selectedTarget by remember { mutableStateOf(TargetLanguage.SIMPLIFIED_CHINESE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = strings.translatePage, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(text = strings.selectTargetLanguage, style = MaterialTheme.typography.bodyMedium)

                LazyColumn(modifier = Modifier.height(260.dp)) {
                    items(TargetLanguage.values().toList()) { lang ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { selectedTarget = lang }
                                .padding(vertical = 10.dp, horizontal = 8.dp)
                        ) {
                            RadioButton(
                                selected = (selectedTarget == lang),
                                onClick = { selectedTarget = lang }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "${lang.nativeName} (${lang.displayName})")
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.translateCurrentWebpage(selectedTarget) }
            ) {
                Text(text = strings.translatePage)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = strings.cancel)
            }
        }
    )
}

@Composable
fun TextTranslateDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit
) {
    val strings = LocalAppStrings.current
    val selectedText by viewModel.selectedTextForTranslation.collectAsStateWithLifecycle()
    val result by viewModel.translationResultState.collectAsStateWithLifecycle()
    val isTranslating by viewModel.isTranslating.collectAsStateWithLifecycle()

    var textInput by remember(selectedText) { mutableStateOf(selectedText) }
    var targetLanguage by remember { mutableStateOf(TargetLanguage.SIMPLIFIED_CHINESE) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = strings.translateSelection, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = textInput,
                    onValueChange = { textInput = it },
                    label = { Text("Original Text") },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Target:", style = MaterialTheme.typography.labelLarge)
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = targetLanguage.nativeName,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                if (isTranslating) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else if (result is TranslationResult.Success) {
                    val res = result as TranslationResult.Success
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Translation:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = res.translatedText, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                } else if (result is TranslationResult.Error) {
                    Text(
                        text = (result as TranslationResult.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.translateSelectedText(textInput, targetLanguage) }
            ) {
                Text(text = "Translate")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = strings.cancel)
            }
        }
    )
}
