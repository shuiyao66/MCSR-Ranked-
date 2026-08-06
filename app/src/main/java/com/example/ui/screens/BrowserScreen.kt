package com.example.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.ViewGroup
import android.webkit.*
import android.widget.FrameLayout
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.AppPreferencesRepository
import com.example.data.LocalAppStrings
import com.example.ui.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowserScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = LocalAppStrings.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }

    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val tabsList by viewModel.tabsList.collectAsStateWithLifecycle()
    val isDesktopMode by viewModel.isDesktopMode.collectAsStateWithLifecycle()
    val bookmarksList by viewModel.bookmarksList.collectAsStateWithLifecycle()

    var urlInputText by remember(activeTab?.url) { mutableStateOf(activeTab?.url ?: "") }
    var isEditingUrl by remember { mutableStateOf(false) }
    var activeWebView by remember { mutableStateOf<WebView?>(null) }
    var customView by remember { mutableStateOf<android.view.View?>(null) }
    var customViewCallback by remember { mutableStateOf<WebChromeClient.CustomViewCallback?>(null) }

    // File Upload Contract
    var fileChooserCallback by remember { mutableStateOf<ValueCallback<Array<Uri>>?>(null) }
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (fileChooserCallback != null) {
            val intent = result.data
            val uris = WebChromeClient.FileChooserParams.parseResult(result.resultCode, intent)
            fileChooserCallback?.onReceiveValue(uris)
            fileChooserCallback = null
        }
    }

    val isCurrentBookmarked = bookmarksList.any { it.url == activeTab?.url }

    // Hardware Back Button Handler
    BackHandler(enabled = customView != null || (activeWebView?.canGoBack() == true)) {
        if (customView != null) {
            customViewCallback?.onCustomViewHidden()
            customView = null
        } else if (activeWebView?.canGoBack() == true) {
            activeWebView?.goBack()
        }
    }

    Scaffold(
        topBar = {
            Surface(
                tonalElevation = 3.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column {
                    // Address Bar Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Home Icon
                        IconButton(
                            onClick = { viewModel.openUrl(AppPreferencesRepository.URL_MCSR_RANKED) },
                            modifier = Modifier.testTag("browser_home_btn")
                        ) {
                            Icon(Icons.Default.Home, contentDescription = "Home")
                        }

                        // Address Bar TextField / Search
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 12.dp)
                            ) {
                                Icon(
                                    imageVector = if (activeTab?.url?.startsWith("https") == true) Icons.Default.Lock else Icons.Default.Search,
                                    contentDescription = null,
                                    tint = if (activeTab?.url?.startsWith("https") == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                OutlinedTextField(
                                    value = if (isEditingUrl) urlInputText else (activeTab?.url ?: ""),
                                    onValueChange = {
                                        urlInputText = it
                                        isEditingUrl = true
                                    },
                                    singleLine = true,
                                    placeholder = {
                                        Text(
                                            text = strings.addressBarPlaceholder,
                                            style = MaterialTheme.typography.bodySmall,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    },
                                    textStyle = MaterialTheme.typography.bodySmall,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = Color.Transparent,
                                        unfocusedBorderColor = Color.Transparent,
                                        focusedContainerColor = Color.Transparent,
                                        unfocusedContainerColor = Color.Transparent
                                    ),
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                                    keyboardActions = KeyboardActions(
                                        onGo = {
                                            viewModel.openUrl(urlInputText)
                                            isEditingUrl = false
                                            focusManager.clearFocus()
                                        }
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .focusRequester(focusRequester)
                                        .testTag("address_bar_input")
                                )

                                if (isEditingUrl && urlInputText.isNotEmpty()) {
                                    IconButton(
                                        onClick = { urlInputText = "" },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        // Refresh / Cancel Icon
                        IconButton(
                            onClick = {
                                if (activeTab?.isLoading == true) {
                                    activeWebView?.stopLoading()
                                } else {
                                    activeWebView?.reload()
                                }
                            },
                            modifier = Modifier.testTag("browser_refresh_btn")
                        ) {
                            Icon(
                                imageVector = if (activeTab?.isLoading == true) Icons.Default.Close else Icons.Default.Refresh,
                                contentDescription = "Refresh"
                            )
                        }

                        // Tab Switcher Button with Badge
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    contentColor = MaterialTheme.colorScheme.onPrimary
                                ) {
                                    Text(text = "${tabsList.size}")
                                }
                            },
                            modifier = Modifier
                                .clickable { viewModel.setShowTabsSheet(true) }
                                .padding( horizontal = 6.dp)
                                .testTag("browser_tabs_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tab,
                                contentDescription = "Tabs Overview"
                            )
                        }
                    }

                    // Linear Loading Indicator
                    if (activeTab?.isLoading == true) {
                        LinearProgressIndicator(
                            progress = { (activeTab?.progress ?: 0) / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(3.dp),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            }
        },
        bottomBar = {
            // Modern Floating Bottom Control Bar
            Surface(
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { activeWebView?.goBack() },
                        enabled = activeTab?.canGoBack == true,
                        modifier = Modifier.testTag("browser_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }

                    IconButton(
                        onClick = { activeWebView?.goForward() },
                        enabled = activeTab?.canGoForward == true,
                        modifier = Modifier.testTag("browser_forward_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Forward")
                    }

                    IconButton(
                        onClick = { viewModel.toggleBookmarkCurrentPage() },
                        modifier = Modifier.testTag("browser_bookmark_btn")
                    ) {
                        Icon(
                            imageVector = if (isCurrentBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            tint = if (isCurrentBookmarked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = { viewModel.setShowTranslateDialog(true) },
                        modifier = Modifier.testTag("browser_translate_btn")
                    ) {
                        Icon(Icons.Default.Translate, contentDescription = "Translate")
                    }

                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, activeTab?.title)
                                putExtra(Intent.EXTRA_TEXT, activeTab?.url)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Page"))
                        },
                        modifier = Modifier.testTag("browser_share_btn")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }

                    IconButton(
                        onClick = { viewModel.addNewTab() },
                        modifier = Modifier.testTag("browser_new_tab_btn")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "New Tab")
                    }
                }
            }
        },
        containerColor = Color.Transparent,
        modifier = modifier
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (activeTab != null) {
                val currentTab = activeTab!!

                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )

                            // Configure WebSettings for full compatibility
                            settings.apply {
                                javaScriptEnabled = true
                                domStorageEnabled = true
                                databaseEnabled = true
                                setSupportZoom(true)
                                builtInZoomControls = true
                                displayZoomControls = false
                                loadWithOverviewMode = true
                                useWideViewPort = true
                                allowFileAccess = true
                                allowContentAccess = true
                                mediaPlaybackRequiresUserGesture = false
                                
                                userAgentString = if (isDesktopMode) {
                                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
                                } else {
                                    null // Default mobile UA
                                }
                            }

                            // Enable session cookies
                            CookieManager.getInstance().setAcceptCookie(true)
                            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    viewModel.updateActiveTabState(
                                        url = url,
                                        isLoading = true
                                    )
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    CookieManager.getInstance().flush()
                                    viewModel.updateActiveTabState(
                                        url = url,
                                        title = view?.title,
                                        canGoBack = view?.canGoBack(),
                                        canGoForward = view?.canGoForward(),
                                        isLoading = false
                                    )
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?
                                ): Boolean {
                                    val reqUrl = request?.url?.toString() ?: return false
                                    if (reqUrl.startsWith("http://") || reqUrl.startsWith("https://")) {
                                        return false
                                    }
                                    return try {
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(reqUrl))
                                        ctx.startActivity(intent)
                                        true
                                    } catch (e: Exception) {
                                        true
                                    }
                                }
                            }

                            webChromeClient = object : WebChromeClient() {
                                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                    super.onProgressChanged(view, newProgress)
                                    viewModel.updateActiveTabState(progress = newProgress)
                                }

                                override fun onReceivedTitle(view: WebView?, title: String?) {
                                    super.onReceivedTitle(view, title)
                                    viewModel.updateActiveTabState(title = title)
                                }

                                // Handle File Chooser for File Uploads
                                override fun onShowFileChooser(
                                    webView: WebView?,
                                    filePathCallback: ValueCallback<Array<Uri>>?,
                                    fileChooserParams: FileChooserParams?
                                ): Boolean {
                                    fileChooserCallback?.onReceiveValue(null)
                                    fileChooserCallback = filePathCallback
                                    val intent = fileChooserParams?.createIntent()
                                    return try {
                                        filePickerLauncher.launch(intent ?: Intent(Intent.ACTION_GET_CONTENT).apply { type = "*/*" })
                                        true
                                    } catch (e: Exception) {
                                        fileChooserCallback = null
                                        false
                                    }
                                }

                                // Fullscreen Video / Media View
                                override fun onShowCustomView(view: android.view.View?, callback: CustomViewCallback?) {
                                    customView = view
                                    customViewCallback = callback
                                }

                                override fun onHideCustomView() {
                                    customViewCallback?.onCustomViewHidden()
                                    customView = null
                                }
                            }

                            // Download Listener
                            setDownloadListener { downloadUrl, userAgent, contentDisposition, mimetype, contentLength ->
                                val fileName = URLUtil.guessFileName(downloadUrl, contentDisposition, mimetype)
                                val request = android.app.DownloadManager.Request(Uri.parse(downloadUrl)).apply {
                                    setMimeType(mimetype)
                                    addRequestHeader("User-Agent", userAgent)
                                    addRequestHeader("Cookie", CookieManager.getInstance().getCookie(downloadUrl))
                                    setTitle(fileName)
                                    setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                                    setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                                }
                                val dm = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as android.app.DownloadManager
                                dm.enqueue(request)

                                viewModel.addDownloadRecord(
                                    fileName = fileName,
                                    url = downloadUrl,
                                    filePath = Environment.DIRECTORY_DOWNLOADS + "/" + fileName,
                                    fileSize = contentLength
                                )
                            }

                            loadUrl(currentTab.url)
                            activeWebView = this
                        }
                    },
                    update = { webView ->
                        activeWebView = webView
                        if (webView.url != currentTab.url && !currentTab.isLoading) {
                            webView.loadUrl(currentTab.url)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Fullscreen custom view overlay for videos/canvas
            if (customView != null) {
                AndroidView(
                    factory = { ctx ->
                        FrameLayout(ctx).apply {
                            addView(
                                customView,
                                FrameLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                )
            }
        }
    }
}
