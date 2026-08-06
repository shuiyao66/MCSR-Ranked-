package com.example.ui

import android.app.Application
import android.content.Context
import android.net.Uri
import android.webkit.CookieManager
import android.webkit.WebView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID

data class WebTabState(
    val id: String = UUID.randomUUID().toString(),
    val url: String = AppPreferencesRepository.URL_MCSR_RANKED,
    val title: String = "MCSR Ranked",
    val progress: Int = 0,
    val canGoBack: Boolean = false,
    val canGoForward: Boolean = false,
    val isLoading: Boolean = false,
    val iconUrl: String? = null
)

class MainViewModel(application: Application) : AndroidViewModel(application) {
    val preferencesRepository = AppPreferencesRepository(application)
    private val database = AppDatabase.getDatabase(application)
    private val bookmarkDao = database.bookmarkDao()
    private val historyDao = database.historyDao()
    private val webTabDao = database.webTabDao()
    private val downloadDao = database.downloadDao()
    val translationManager = TranslationManager()

    // State Flows
    val currentLanguage: StateFlow<AppLanguage> = preferencesRepository.languageState
    val themeConfig: StateFlow<AppThemeConfig> = preferencesRepository.themeConfigState
    val startupUrl: StateFlow<String> = preferencesRepository.startupUrlState
    val isDesktopMode: StateFlow<Boolean> = preferencesRepository.desktopModeState

    // Bookmarks, History & Downloads from Room
    val bookmarksList: StateFlow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val historyList: StateFlow<List<HistoryEntity>> = historyDao.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val downloadsList: StateFlow<List<DownloadEntity>> = downloadDao.getAllDownloads()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Tabs Management
    private val _tabsList = MutableStateFlow<List<WebTabState>>(
        listOf(WebTabState(url = preferencesRepository.startupUrlState.value))
    )
    val tabsList: StateFlow<List<WebTabState>> = _tabsList.asStateFlow()

    private val _activeTabId = MutableStateFlow(_tabsList.value.first().id)
    val activeTabId: StateFlow<String> = _activeTabId.asStateFlow()

    val activeTab: StateFlow<WebTabState?> = combine(_tabsList, _activeTabId) { tabs, activeId ->
        tabs.find { it.id == activeId } ?: tabs.firstOrNull()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // UI Dialog & Sheet States
    private val _showTabsSheet = MutableStateFlow(false)
    val showTabsSheet: StateFlow<Boolean> = _showTabsSheet.asStateFlow()

    private val _showBookmarksSheet = MutableStateFlow(false)
    val showBookmarksSheet: StateFlow<Boolean> = _showBookmarksSheet.asStateFlow()

    private val _showHistorySheet = MutableStateFlow(false)
    val showHistorySheet: StateFlow<Boolean> = _showHistorySheet.asStateFlow()

    private val _showDownloadsSheet = MutableStateFlow(false)
    val showDownloadsSheet: StateFlow<Boolean> = _showDownloadsSheet.asStateFlow()

    private val _showTranslateDialog = MutableStateFlow(false)
    val showTranslateDialog: StateFlow<Boolean> = _showTranslateDialog.asStateFlow()

    private val _showTextTranslateDialog = MutableStateFlow(false)
    val showTextTranslateDialog: StateFlow<Boolean> = _showTextTranslateDialog.asStateFlow()

    private val _selectedTextForTranslation = MutableStateFlow("")
    val selectedTextForTranslation: StateFlow<String> = _selectedTextForTranslation.asStateFlow()

    private val _translationResultState = MutableStateFlow<TranslationResult?>(null)
    val translationResultState: StateFlow<TranslationResult?> = _translationResultState.asStateFlow()

    private val _isTranslating = MutableStateFlow(false)
    val isTranslating: StateFlow<Boolean> = _isTranslating.asStateFlow()

    // Message Toast/Snackbar State
    private val _userMessage = MutableStateFlow<String?>(null)
    val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

    fun clearUserMessage() { _userMessage.value = null }

    // Navigation & Tab Actions
    fun openUrl(url: String, openInNewTab: Boolean = false) {
        val targetUrl = sanitizeUrl(url)
        if (openInNewTab || _tabsList.value.isEmpty()) {
            val newTab = WebTabState(url = targetUrl)
            _tabsList.value = _tabsList.value + newTab
            _activeTabId.value = newTab.id
        } else {
            val currentId = _activeTabId.value
            _tabsList.value = _tabsList.value.map {
                if (it.id == currentId) it.copy(url = targetUrl, title = targetUrl, isLoading = true) else it
            }
        }
    }

    fun addNewTab(url: String = preferencesRepository.startupUrlState.value) {
        val newTab = WebTabState(url = sanitizeUrl(url))
        _tabsList.value = _tabsList.value + newTab
        _activeTabId.value = newTab.id
    }

    fun selectTab(tabId: String) {
        _activeTabId.value = tabId
    }

    fun closeTab(tabId: String) {
        val currentTabs = _tabsList.value
        if (currentTabs.size <= 1) {
            // Re-initialize tab with startup URL instead of empty list
            val newTab = WebTabState(url = preferencesRepository.startupUrlState.value)
            _tabsList.value = listOf(newTab)
            _activeTabId.value = newTab.id
            return
        }

        val remainingTabs = currentTabs.filter { it.id != tabId }
        _tabsList.value = remainingTabs
        if (_activeTabId.value == tabId) {
            _activeTabId.value = remainingTabs.last().id
        }
    }

    fun closeAllTabs() {
        val newTab = WebTabState(url = preferencesRepository.startupUrlState.value)
        _tabsList.value = listOf(newTab)
        _activeTabId.value = newTab.id
    }

    fun updateActiveTabState(
        url: String? = null,
        title: String? = null,
        progress: Int? = null,
        canGoBack: Boolean? = null,
        canGoForward: Boolean? = null,
        isLoading: Boolean? = null
    ) {
        val currentActiveId = _activeTabId.value
        _tabsList.value = _tabsList.value.map { tab ->
            if (tab.id == currentActiveId) {
                tab.copy(
                    url = url ?: tab.url,
                    title = title ?: tab.title,
                    progress = progress ?: tab.progress,
                    canGoBack = canGoBack ?: tab.canGoBack,
                    canGoForward = canGoForward ?: tab.canGoForward,
                    isLoading = isLoading ?: tab.isLoading
                )
            } else tab
        }

        // Auto record history when URL or title updates
        if (url != null && title != null && url.startsWith("http")) {
            viewModelScope.launch {
                historyDao.insertHistory(HistoryEntity(title = title, url = url))
            }
        }
    }

    // Bookmark Actions
    fun toggleBookmarkCurrentPage() {
        val currentTab = activeTab.value ?: return
        viewModelScope.launch {
            val isBookmarked = bookmarkDao.isBookmarked(currentTab.url).first()
            if (isBookmarked) {
                bookmarkDao.deleteBookmarkByUrl(currentTab.url)
                _userMessage.value = "已从书签中移除 / Removed from bookmarks"
            } else {
                bookmarkDao.insertBookmark(BookmarkEntity(title = currentTab.title, url = currentTab.url))
                _userMessage.value = "已添加到书签 / Added to bookmarks"
            }
        }
    }

    fun deleteBookmark(id: Int) {
        viewModelScope.launch { bookmarkDao.deleteBookmarkById(id) }
    }

    fun clearHistory() {
        viewModelScope.launch {
            historyDao.clearHistory()
            _userMessage.value = preferencesRepository.languageState.value.let { AppStrings(it).successCleared }
        }
    }

    // Translation Actions
    fun translateCurrentWebpage(targetLanguage: TargetLanguage) {
        val currentTab = activeTab.value ?: return
        val translatedUrl = translationManager.getTranslatedWebpageUrl(currentTab.url, targetLanguage)
        openUrl(translatedUrl, openInNewTab = false)
        _showTranslateDialog.value = false
    }

    fun translateSelectedText(text: String, targetLanguage: TargetLanguage) {
        _selectedTextForTranslation.value = text
        _showTextTranslateDialog.value = true
        _isTranslating.value = true
        _translationResultState.value = null

        viewModelScope.launch {
            val result = translationManager.translateText(text, targetLanguage)
            _translationResultState.value = result
            _isTranslating.value = false
        }
    }

    // Theme Customization
    fun updateThemeConfig(config: AppThemeConfig) {
        preferencesRepository.saveThemeConfig(config)
    }

    fun setLanguage(language: AppLanguage) {
        preferencesRepository.saveLanguage(language)
    }

    fun setStartupUrl(url: String) {
        preferencesRepository.saveStartupUrl(url)
    }

    fun toggleDesktopMode() {
        preferencesRepository.setDesktopMode(!isDesktopMode.value)
    }

    // Sheet / Dialog visibility setters
    fun setShowTabsSheet(show: Boolean) { _showTabsSheet.value = show }
    fun setShowBookmarksSheet(show: Boolean) { _showBookmarksSheet.value = show }
    fun setShowHistorySheet(show: Boolean) { _showHistorySheet.value = show }
    fun setShowDownloadsSheet(show: Boolean) { _showDownloadsSheet.value = show }
    fun setShowTranslateDialog(show: Boolean) { _showTranslateDialog.value = show }
    fun setShowTextTranslateDialog(show: Boolean) { _showTextTranslateDialog.value = show }

    // Clear Cache & Cookies
    fun clearBrowserCache(webView: WebView?) {
        webView?.clearCache(true)
        _userMessage.value = preferencesRepository.languageState.value.let { AppStrings(it).successCleared }
    }

    fun clearBrowserCookies() {
        CookieManager.getInstance().removeAllCookies(null)
        CookieManager.getInstance().flush()
        _userMessage.value = preferencesRepository.languageState.value.let { AppStrings(it).successCleared }
    }

    // Download Management
    fun addDownloadRecord(fileName: String, url: String, filePath: String, fileSize: Long) {
        viewModelScope.launch {
            downloadDao.insertDownload(
                DownloadEntity(
                    fileName = fileName,
                    url = url,
                    filePath = filePath,
                    fileSize = fileSize,
                    status = "COMPLETED"
                )
            )
        }
    }

    fun deleteDownloadRecord(id: Int) {
        viewModelScope.launch { downloadDao.deleteDownload(id) }
    }

    private fun sanitizeUrl(input: String): String {
        val trimmed = input.trim()
        return when {
            trimmed.startsWith("http://") || trimmed.startsWith("https://") -> trimmed
            trimmed.contains(".") && !trimmed.contains(" ") -> "https://$trimmed"
            else -> "https://www.google.com/search?q=" + Uri.encode(trimmed)
        }
    }
}
