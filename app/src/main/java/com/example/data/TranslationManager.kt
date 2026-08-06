package com.example.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder

enum class TargetLanguage(val code: String, val displayName: String, val nativeName: String) {
    SIMPLIFIED_CHINESE("zh-CN", "Simplified Chinese", "简体中文"),
    TRADITIONAL_CHINESE("zh-TW", "Traditional Chinese", "繁體中文"),
    ENGLISH("en", "English", "English"),
    JAPANESE("ja", "Japanese", "日本語"),
    KOREAN("ko", "Korean", "한국어"),
    FRENCH("fr", "French", "Français"),
    GERMAN("de", "German", "Deutsch"),
    SPANISH("es", "Spanish", "Español");

    companion object {
        fun fromCode(code: String): TargetLanguage {
            val lower = code.lowercase()
            return values().firstOrNull { 
                it.code.lowercase() == lower || lower.startsWith(it.code.lowercase().take(2)) 
            } ?: SIMPLIFIED_CHINESE
        }
    }
}

class TranslationManager {
    private val client = OkHttpClient()

    /**
     * Generates a Google Translate Proxy Web URL for translating an entire webpage.
     */
    fun getTranslatedWebpageUrl(originalUrl: String, targetLanguage: TargetLanguage): String {
        return try {
            val encodedUrl = URLEncoder.encode(originalUrl, "UTF-8")
            "https://translate.google.com/translate?sl=auto&tl=${targetLanguage.code}&u=$encodedUrl"
        } catch (e: Exception) {
            originalUrl
        }
    }

    /**
     * Translates selected text using free translate endpoint.
     */
    suspend fun translateText(text: String, targetLanguage: TargetLanguage): TranslationResult = withContext(Dispatchers.IO) {
        if (text.isBlank()) {
            return@withContext TranslationResult.Success("", "auto", targetLanguage.code)
        }

        try {
            val encodedText = URLEncoder.encode(text, "UTF-8")
            val url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl=${targetLanguage.code}&dt=t&q=$encodedText"

            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Android Mobile)")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    return@withContext TranslationResult.Error("HTTP Error: ${response.code}")
                }
                val body = response.body?.string() ?: return@withContext TranslationResult.Error("Empty response")
                
                // Parse gtx JSON response: [[["translated_text", "original_text", null, null, 36]], null, "en"]
                val jsonArray = JSONArray(body)
                val sentencesArray = jsonArray.getJSONArray(0)
                val detectedLang = if (jsonArray.length() > 2) jsonArray.getString(2) else "auto"

                val translatedStringBuilder = StringBuilder()
                for (i in 0 until sentencesArray.length()) {
                    val sentence = sentencesArray.getJSONArray(i)
                    translatedStringBuilder.append(sentence.getString(0))
                }

                TranslationResult.Success(
                    translatedText = translatedStringBuilder.toString(),
                    detectedLanguage = detectedLang,
                    targetLanguage = targetLanguage.code
                )
            }
        } catch (e: Exception) {
            TranslationResult.Error(e.localizedMessage ?: "Translation failed")
        }
    }
}

sealed class TranslationResult {
    data class Success(val translatedText: String, val detectedLanguage: String, val targetLanguage: String) : TranslationResult()
    data class Error(val message: String) : TranslationResult()
}
