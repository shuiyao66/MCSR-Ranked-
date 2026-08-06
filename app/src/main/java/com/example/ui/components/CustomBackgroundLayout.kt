package com.example.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.AppThemeConfig

@Composable
fun CustomBackgroundContainer(
    themeConfig: AppThemeConfig,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (!themeConfig.backgroundImageUri.isNull_or_empty()) {
            val context = LocalContext.current
            val blurRadius = themeConfig.backgroundBlurRadiusDp.coerceIn(0f, 25f).dp
            val opacity = themeConfig.backgroundOpacity.coerceIn(0.1f, 1.0f)

            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(Uri.parse(themeConfig.backgroundImageUri))
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .then(if (blurRadius > 0.dp) Modifier.blur(blurRadius) else Modifier)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 1f - opacity * 0.7f))
            )
        }

        content()
    }
}

private fun String?.isNull_or_empty(): Boolean = this == null || this.trim().isEmpty()
