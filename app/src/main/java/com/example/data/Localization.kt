package com.example.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import java.util.Locale

enum class AppLanguage(val code: String, val displayName: String, val nativeName: String) {
    SIMPLIFIED_CHINESE("zh-CN", "Simplified Chinese", "简体中文"),
    TRADITIONAL_CHINESE("zh-TW", "Traditional Chinese", "繁體中文"),
    ENGLISH("en", "English", "English"),
    JAPANESE("ja", "Japanese", "日本語"),
    KOREAN("ko", "Korean", "한국어"),
    FRENCH("fr", "French", "Français"),
    GERMAN("de", "German", "Deutsch"),
    SPANISH("es", "Spanish", "Español"),
    PORTUGUESE("pt", "Portuguese", "Português"),
    RUSSIAN("ru", "Russian", "Русский"),
    ITALIAN("it", "Italian", "Italiano"),
    TURKISH("tr", "Turkish", "Türkçe"),
    VIETNAMESE("vi", "Vietnamese", "Tiếng Việt"),
    THAI("th", "Thai", "ไทย"),
    INDONESIAN("id", "Indonesian", "Bahasa Indonesia");

    companion object {
        fun fromCode(code: String): AppLanguage {
            val lowercase = code.lowercase()
            return values().firstOrNull { 
                it.code.lowercase() == lowercase || lowercase.startsWith(it.code.lowercase().take(2))
            } ?: SIMPLIFIED_CHINESE
        }

        fun detectDefault(): AppLanguage {
            val systemLocale = Locale.getDefault()
            val languageCode = systemLocale.language
            val countryCode = systemLocale.country
            
            return when {
                languageCode == "zh" && (countryCode == "TW" || countryCode == "HK" || countryCode == "MO") -> TRADITIONAL_CHINESE
                languageCode == "zh" -> SIMPLIFIED_CHINESE
                languageCode == "en" -> ENGLISH
                languageCode == "ja" -> JAPANESE
                languageCode == "ko" -> KOREAN
                languageCode == "fr" -> FRENCH
                languageCode == "de" -> GERMAN
                languageCode == "es" -> SPANISH
                languageCode == "pt" -> PORTUGUESE
                languageCode == "ru" -> RUSSIAN
                languageCode == "it" -> ITALIAN
                languageCode == "tr" -> TURKISH
                languageCode == "vi" -> VIETNAMESE
                languageCode == "th" -> THAI
                languageCode == "id" -> INDONESIAN
                else -> SIMPLIFIED_CHINESE // Default requirement: zh-CN
            }
        }
    }
}

// Localized strings dictionary covering all UI labels
class AppStrings(val language: AppLanguage) {
    val appName: String get() = "MCSR Ranked"
    val homeTab: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "首页"
        AppLanguage.TRADITIONAL_CHINESE -> "首頁"
        AppLanguage.ENGLISH -> "Home"
        AppLanguage.JAPANESE -> "ホーム"
        AppLanguage.KOREAN -> "홈"
        AppLanguage.FRENCH -> "Accueil"
        AppLanguage.GERMAN -> "Startseite"
        AppLanguage.SPANISH -> "Inicio"
        AppLanguage.PORTUGUESE -> "Início"
        AppLanguage.RUSSIAN -> "Главная"
        AppLanguage.ITALIAN -> "Home"
        AppLanguage.TURKISH -> "Ana Sayfa"
        AppLanguage.VIETNAMESE -> "Trang chủ"
        AppLanguage.THAI -> "หน้าแรก"
        AppLanguage.INDONESIAN -> "Beranda"
    }

    val browserTab: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "浏览器"
        AppLanguage.TRADITIONAL_CHINESE -> "瀏覽器"
        AppLanguage.ENGLISH -> "Browser"
        AppLanguage.JAPANESE -> "ブラウザ"
        AppLanguage.KOREAN -> "브라우저"
        AppLanguage.FRENCH -> "Navigateur"
        AppLanguage.GERMAN -> "Browser"
        AppLanguage.SPANISH -> "Navegador"
        AppLanguage.PORTUGUESE -> "Navegador"
        AppLanguage.RUSSIAN -> "Браузер"
        AppLanguage.ITALIAN -> "Browser"
        AppLanguage.TURKISH -> "Tarayıcı"
        AppLanguage.VIETNAMESE -> "Trình duyệt"
        AppLanguage.THAI -> "เบราว์เซอร์"
        AppLanguage.INDONESIAN -> "Peramban"
    }

    val settingsTab: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "设置"
        AppLanguage.TRADITIONAL_CHINESE -> "設定"
        AppLanguage.ENGLISH -> "Settings"
        AppLanguage.JAPANESE -> "設定"
        AppLanguage.KOREAN -> "설정"
        AppLanguage.FRENCH -> "Paramètres"
        AppLanguage.GERMAN -> "Einstellungen"
        AppLanguage.SPANISH -> "Ajustes"
        AppLanguage.PORTUGUESE -> "Configurações"
        AppLanguage.RUSSIAN -> "Настройки"
        AppLanguage.ITALIAN -> "Impostazioni"
        AppLanguage.TURKISH -> "Ayarlar"
        AppLanguage.VIETNAMESE -> "Cài đặt"
        AppLanguage.THAI -> "การตั้งค่า"
        AppLanguage.INDONESIAN -> "Pengaturan"
    }

    val mcsrRankedTitle: String get() = "MCSR Ranked 官方网站"
    val mcsrRankedDesc: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "Minecraft 速刷竞技官方对战平台, 实时匹配, 赛季排行榜与积分赛"
        AppLanguage.TRADITIONAL_CHINESE -> "Minecraft 速刷競技官方對戰平台, 實時匹配, 賽季排行榜與積分賽"
        AppLanguage.ENGLISH -> "Official Minecraft Speedrun Ranked competitive platform, live matchmaking & leaderboards"
        AppLanguage.JAPANESE -> "マインクラフト スピードラン公式ランキングプラットフォーム、ライブ対戦とリーダーボード"
        AppLanguage.KOREAN -> "마인크래프트 스피드런 공식 경쟁 플랫폼, 실시간 매치메이킹 및 순위표"
        AppLanguage.FRENCH -> "Plateforme de compétition officielle Minecraft Speedrun, matchmaking en direct et classements"
        AppLanguage.GERMAN -> "Offizielle Minecraft Speedrun Ranked Wettbewerbsplattform, Live-Matchmaking und Ranglisten"
        AppLanguage.SPANISH -> "Plataforma oficial de competición Minecraft Speedrun, emparejamientos en vivo y clasificaciones"
        AppLanguage.PORTUGUESE -> "Plataforma oficial de competição Minecraft Speedrun, partidas ao vivo e classificações"
        AppLanguage.RUSSIAN -> "Официальная соревновательная платформа Minecraft Speedrun, живой подбор игроков и рейтинги"
        AppLanguage.ITALIAN -> "Piattaforma competitiva ufficiale Minecraft Speedrun, matchmaking dal vivo e classifiche"
        AppLanguage.TURKISH -> "Resmi Minecraft Speedrun Ranked yarışma platformu, canlı eşleşme ve lider tabloları"
        AppLanguage.VIETNAMESE -> "Nền tảng thi đấu chính thức Minecraft Speedrun Ranked, ghép trận trực tiếp và bảng xếp hạng"
        AppLanguage.THAI -> "แพลตฟอร์มการแข่งขันอย่างเป็นทางการ Minecraft Speedrun Ranked การจับคู่ออนไลน์และอันดับ"
        AppLanguage.INDONESIAN -> "Platform kompetisi resmi Minecraft Speedrun Ranked, matchmaking langsung dan papan peringkat"
    }

    val rankalyticsTitle: String get() = "Rankalytics 数据分析"
    val rankalyticsDesc: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "深度 Minecraft 速刷统计数据, 选手战绩分析, 胜率图表与历史记录"
        AppLanguage.TRADITIONAL_CHINESE -> "深度 Minecraft 速刷統計數據, 選手戰績分析, 勝率圖表與歷史記錄"
        AppLanguage.ENGLISH -> "Comprehensive Minecraft Speedrun stats, player analytics, win-rate charts & match logs"
        AppLanguage.JAPANESE -> "詳細なマインクラフトスピードラン統計、プレイヤー分析、勝率チャートとログ"
        AppLanguage.KOREAN -> "상세한 마인크래프트 스피드런 통계, 전적 분석, 승률 차트 및 전적 기록"
        AppLanguage.FRENCH -> "Statistiques détaillées Minecraft Speedrun, analyses des joueurs, graphiques de victoires"
        AppLanguage.GERMAN -> "Umfassende Minecraft Speedrun Statistiken, Spieleranalysen und Gewinnraten-Diagramme"
        AppLanguage.SPANISH -> "Estadísticas detalladas de Minecraft Speedrun, análisis de jugadores y gráficos de victorias"
        AppLanguage.PORTUGUESE -> "Estatísticas detalhadas de Minecraft Speedrun, análises de jogadores e gráficos de vitórias"
        AppLanguage.RUSSIAN -> "Подробная статистика Minecraft Speedrun, аналитика игроков и графики винрейта"
        AppLanguage.ITALIAN -> "Statistiche dettagliate Minecraft Speedrun, analisi dei giocatori e grafici delle vittorie"
        AppLanguage.TURKISH -> "Kapsamlı Minecraft Speedrun istatistikleri, oyuncu analizleri ve kazanma oranı grafikleri"
        AppLanguage.VIETNAMESE -> "Thống kê chi tiết Minecraft Speedrun, phân tích người chơi, biểu đồ tỷ lệ thắng"
        AppLanguage.THAI -> "สถิติอย่างละเอียด Minecraft Speedrun การวิเคราะห์ผู้เล่น กราฟอัตราการชนะ"
        AppLanguage.INDONESIAN -> "Statistik mendalam Minecraft Speedrun, analisis pemain, dan grafik tingkat kemenangan"
    }

    val launchWebsite: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "立即访问"
        AppLanguage.TRADITIONAL_CHINESE -> "立即訪問"
        AppLanguage.ENGLISH -> "Launch Website"
        AppLanguage.JAPANESE -> "アクセス"
        AppLanguage.KOREAN -> "바로가기"
        AppLanguage.FRENCH -> "Ouvrir le site"
        AppLanguage.GERMAN -> "Website öffnen"
        AppLanguage.SPANISH -> "Abrir sitio"
        AppLanguage.PORTUGUESE -> "Abrir site"
        AppLanguage.RUSSIAN -> "Открыть сайт"
        AppLanguage.ITALIAN -> "Apri sito"
        AppLanguage.TURKISH -> "Siteye Git"
        AppLanguage.VIETNAMESE -> "Truy cập ngay"
        AppLanguage.THAI -> "เข้าสู่เว็บไซต์"
        AppLanguage.INDONESIAN -> "Buka Situs"
    }

    val quickActions: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "快捷入口"
        AppLanguage.TRADITIONAL_CHINESE -> "快捷入口"
        AppLanguage.ENGLISH -> "Quick Actions"
        AppLanguage.JAPANESE -> "クイックアクション"
        AppLanguage.KOREAN -> "빠른 실행"
        AppLanguage.FRENCH -> "Actions rapides"
        AppLanguage.GERMAN -> "Schnellzugriff"
        AppLanguage.SPANISH -> "Acciones rápidas"
        AppLanguage.PORTUGUESE -> "Ações rápidas"
        AppLanguage.RUSSIAN -> "Быстрые действия"
        AppLanguage.ITALIAN -> "Azioni rapide"
        AppLanguage.TURKISH -> "Hızlı Eylemler"
        AppLanguage.VIETNAMESE -> "Lối tắt"
        AppLanguage.THAI -> "การดำเนินการด่วน"
        AppLanguage.INDONESIAN -> "Tindakan Cepat"
    }

    val bookmarks: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "书签"
        AppLanguage.TRADITIONAL_CHINESE -> "書籤"
        AppLanguage.ENGLISH -> "Bookmarks"
        AppLanguage.JAPANESE -> "ブックマーク"
        AppLanguage.KOREAN -> "북마크"
        AppLanguage.FRENCH -> "Favoris"
        AppLanguage.GERMAN -> "Lesezeichen"
        AppLanguage.SPANISH -> "Marcadores"
        AppLanguage.PORTUGUESE -> "Favoritos"
        AppLanguage.RUSSIAN -> "Закладки"
        AppLanguage.ITALIAN -> "Segnalibri"
        AppLanguage.TURKISH -> "Yer İmleri"
        AppLanguage.VIETNAMESE -> "Dấu trang"
        AppLanguage.THAI -> "บุ๊กมาร์ก"
        AppLanguage.INDONESIAN -> "Markah"
    }

    val history: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "历史记录"
        AppLanguage.TRADITIONAL_CHINESE -> "歷史記錄"
        AppLanguage.ENGLISH -> "History"
        AppLanguage.JAPANESE -> "履歴"
        AppLanguage.KOREAN -> "방문 기록"
        AppLanguage.FRENCH -> "Historique"
        AppLanguage.GERMAN -> "Verlauf"
        AppLanguage.SPANISH -> "Historial"
        AppLanguage.PORTUGUESE -> "Histórico"
        AppLanguage.RUSSIAN -> "История"
        AppLanguage.ITALIAN -> "Cronologia"
        AppLanguage.TURKISH -> "Geçmiş"
        AppLanguage.VIETNAMESE -> "Lịch sử"
        AppLanguage.THAI -> "ประวัติ"
        AppLanguage.INDONESIAN -> "Riwayat"
    }

    val downloads: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "下载管理"
        AppLanguage.TRADITIONAL_CHINESE -> "下載管理"
        AppLanguage.ENGLISH -> "Downloads"
        AppLanguage.JAPANESE -> "ダウンロード"
        AppLanguage.KOREAN -> "다운로드"
        AppLanguage.FRENCH -> "Téléchargements"
        AppLanguage.GERMAN -> "Downloads"
        AppLanguage.SPANISH -> "Descargas"
        AppLanguage.PORTUGUESE -> "Downloads"
        AppLanguage.RUSSIAN -> "Загрузки"
        AppLanguage.ITALIAN -> "Download"
        AppLanguage.TURKISH -> "İndirmeler"
        AppLanguage.VIETNAMESE -> "Tải xuống"
        AppLanguage.THAI -> "ดาวน์โหลด"
        AppLanguage.INDONESIAN -> "Unduhan"
    }

    val translatePage: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "翻译网页"
        AppLanguage.TRADITIONAL_CHINESE -> "翻譯網頁"
        AppLanguage.ENGLISH -> "Translate Page"
        AppLanguage.JAPANESE -> "ページを翻訳"
        AppLanguage.KOREAN -> "페이지 번역"
        AppLanguage.FRENCH -> "Traduire la page"
        AppLanguage.GERMAN -> "Seite übersetzen"
        AppLanguage.SPANISH -> "Traducir página"
        AppLanguage.PORTUGUESE -> "Traduzir página"
        AppLanguage.RUSSIAN -> "Перевести страницу"
        AppLanguage.ITALIAN -> "Traduci pagina"
        AppLanguage.TURKISH -> "Sayfayı Çevir"
        AppLanguage.VIETNAMESE -> "Dịch trang"
        AppLanguage.THAI -> "แปลหน้าเว็บ"
        AppLanguage.INDONESIAN -> "Terjemahkan Halaman"
    }

    val translateSelection: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "翻译选中文本"
        AppLanguage.TRADITIONAL_CHINESE -> "翻譯選中文本"
        AppLanguage.ENGLISH -> "Translate Selection"
        AppLanguage.JAPANESE -> "選択テキストを翻訳"
        AppLanguage.KOREAN -> "선택한 텍스트 번역"
        AppLanguage.FRENCH -> "Traduire la sélection"
        AppLanguage.GERMAN -> "Auswahl übersetzen"
        AppLanguage.SPANISH -> "Traducir selección"
        AppLanguage.PORTUGUESE -> "Traduzir seleção"
        AppLanguage.RUSSIAN -> "Перевести выделенное"
        AppLanguage.ITALIAN -> "Traduci selezione"
        AppLanguage.TURKISH -> "Seçimi Çevir"
        AppLanguage.VIETNAMESE -> "Dịch văn bản chọn"
        AppLanguage.THAI -> "แปลข้อความที่เลือก"
        AppLanguage.INDONESIAN -> "Terjemahkan Pilihan"
    }

    val selectTargetLanguage: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "选择目标语言"
        AppLanguage.TRADITIONAL_CHINESE -> "選擇目標語言"
        AppLanguage.ENGLISH -> "Select Target Language"
        AppLanguage.JAPANESE -> "翻訳先言語を選択"
        AppLanguage.KOREAN -> "목표 언어 선택"
        AppLanguage.FRENCH -> "Choisir la langue cible"
        AppLanguage.GERMAN -> "Zielsprache wählen"
        AppLanguage.SPANISH -> "Seleccionar idioma de destino"
        AppLanguage.PORTUGUESE -> "Selecionar idioma de destino"
        AppLanguage.RUSSIAN -> "Выберите язык перевода"
        AppLanguage.ITALIAN -> "Seleziona lingua di destinazione"
        AppLanguage.TURKISH -> "Hedef Dili Seçin"
        AppLanguage.VIETNAMESE -> "Chọn ngôn ngữ đích"
        AppLanguage.THAI -> "เลือกภาษาเป้าหมาย"
        AppLanguage.INDONESIAN -> "Pilih Bahasa Tujuan"
    }

    val addressBarPlaceholder: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "输入网址或搜索关键词..."
        AppLanguage.TRADITIONAL_CHINESE -> "輸入網址或搜尋關鍵字..."
        AppLanguage.ENGLISH -> "Search or enter web address..."
        AppLanguage.JAPANESE -> "検索またはWebアドレスを入力..."
        AppLanguage.KOREAN -> "검색어 또는 웹 주소 입력..."
        AppLanguage.FRENCH -> "Rechercher ou saisir une adresse web..."
        AppLanguage.GERMAN -> "Suchen oder Webadresse eingeben..."
        AppLanguage.SPANISH -> "Buscar o escribir dirección web..."
        AppLanguage.PORTUGUESE -> "Pesquisar ou digite o endereço..."
        AppLanguage.RUSSIAN -> "Поиск или адрес веб-сайта..."
        AppLanguage.ITALIAN -> "Cerca o inserisci indirizzo web..."
        AppLanguage.TURKISH -> "Ara veya web adresi yazın..."
        AppLanguage.VIETNAMESE -> "Tìm kiếm hoặc nhập địa chỉ web..."
        AppLanguage.THAI -> "ค้นหาหรือพิมพ์ที่อยู่เว็บ..."
        AppLanguage.INDONESIAN -> "Cari atau ketik alamat web..."
    }

    val newTab: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "新建标签页"
        AppLanguage.TRADITIONAL_CHINESE -> "新建標籤頁"
        AppLanguage.ENGLISH -> "New Tab"
        AppLanguage.JAPANESE -> "新しいタブ"
        AppLanguage.KOREAN -> "새 탭"
        AppLanguage.FRENCH -> "Nouvel onglet"
        AppLanguage.GERMAN -> "Neuer Tab"
        AppLanguage.SPANISH -> "Nueva pestaña"
        AppLanguage.PORTUGUESE -> "Nova aba"
        AppLanguage.RUSSIAN -> "Новая вкладка"
        AppLanguage.ITALIAN -> "Nuova scheda"
        AppLanguage.TURKISH -> "Yeni Sekme"
        AppLanguage.VIETNAMESE -> "Thẻ mới"
        AppLanguage.THAI -> "แท็บใหม่"
        AppLanguage.INDONESIAN -> "Tab Baru"
    }

    val tabsOverview: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "标签页管理"
        AppLanguage.TRADITIONAL_CHINESE -> "標籤頁管理"
        AppLanguage.ENGLISH -> "Tabs Overview"
        AppLanguage.JAPANESE -> "タブ一覧"
        AppLanguage.KOREAN -> "탭 개요"
        AppLanguage.FRENCH -> "Aperçu des onglets"
        AppLanguage.GERMAN -> "Tab-Übersicht"
        AppLanguage.SPANISH -> "Vista de pestañas"
        AppLanguage.PORTUGUESE -> "Visão geral das abas"
        AppLanguage.RUSSIAN -> "Обзор вкладок"
        AppLanguage.ITALIAN -> "Panoramica schede"
        AppLanguage.TURKISH -> "Sekmelere Genel Bakış"
        AppLanguage.VIETNAMESE -> "Tổng quan thẻ"
        AppLanguage.THAI -> "ภาพรวมแท็บ"
        AppLanguage.INDONESIAN -> "Ikhtisar Tab"
    }

    val closeAllTabs: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "关闭所有标签"
        AppLanguage.TRADITIONAL_CHINESE -> "關閉所有標籤"
        AppLanguage.ENGLISH -> "Close All Tabs"
        AppLanguage.JAPANESE -> "すべてのタブを閉じる"
        AppLanguage.KOREAN -> "모든 탭 닫기"
        AppLanguage.FRENCH -> "Fermer tous les onglets"
        AppLanguage.GERMAN -> "Alle Tabs schließen"
        AppLanguage.SPANISH -> "Cerrar todas las pestañas"
        AppLanguage.PORTUGUESE -> "Fechar todas as abas"
        AppLanguage.RUSSIAN -> "Закрыть все вкладки"
        AppLanguage.ITALIAN -> "Chiudi tutte le schede"
        AppLanguage.TURKISH -> "Tüm Sekmeleri Kapat"
        AppLanguage.VIETNAMESE -> "Đóng tất cả các thẻ"
        AppLanguage.THAI -> "ปิดแท็บทั้งหมด"
        AppLanguage.INDONESIAN -> "Tutup Semua Tab"
    }

    val themeSettings: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "主题与个性化"
        AppLanguage.TRADITIONAL_CHINESE -> "主題與個性化"
        AppLanguage.ENGLISH -> "Theme & Customization"
        AppLanguage.JAPANESE -> "テーマとカスタマイズ"
        AppLanguage.KOREAN -> "테마 및 사용자 지정"
        AppLanguage.FRENCH -> "Thème et personnalisation"
        AppLanguage.GERMAN -> "Design & Anpassung"
        AppLanguage.SPANISH -> "Tema y personalización"
        AppLanguage.PORTUGUESE -> "Tema e personalização"
        AppLanguage.RUSSIAN -> "Тема и персонализация"
        AppLanguage.ITALIAN -> "Tema e personalizzazione"
        AppLanguage.TURKISH -> "Tema ve Özelleştirme"
        AppLanguage.VIETNAMESE -> "Giao diện & Tùy chỉnh"
        AppLanguage.THAI -> "ธีมและการปรับแต่ง"
        AppLanguage.INDONESIAN -> "Tema & Kustomisasi"
    }

    val primaryColor: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "主题主色调"
        AppLanguage.TRADITIONAL_CHINESE -> "主題主色調"
        AppLanguage.ENGLISH -> "Primary Theme Color"
        AppLanguage.JAPANESE -> "メインテーマカラー"
        AppLanguage.KOREAN -> "기본 테마 색상"
        AppLanguage.FRENCH -> "Couleur principale"
        AppLanguage.GERMAN -> "Hauptfarbe"
        AppLanguage.SPANISH -> "Color primario"
        AppLanguage.PORTUGUESE -> "Cor primária"
        AppLanguage.RUSSIAN -> "Основной цвет"
        AppLanguage.ITALIAN -> "Colore primario"
        AppLanguage.TURKISH -> "Birincil Tema Rengi"
        AppLanguage.VIETNAMESE -> "Màu chủ đạo"
        AppLanguage.THAI -> "สีธีมหลัก"
        AppLanguage.INDONESIAN -> "Warna Utama Tema"
    }

    val darkMode: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "深色模式"
        AppLanguage.TRADITIONAL_CHINESE -> "深色模式"
        AppLanguage.ENGLISH -> "Dark Mode"
        AppLanguage.JAPANESE -> "ダークモード"
        AppLanguage.KOREAN -> "다크 모드"
        AppLanguage.FRENCH -> "Mode sombre"
        AppLanguage.GERMAN -> "Dunkelmodus"
        AppLanguage.SPANISH -> "Modo oscuro"
        AppLanguage.PORTUGUESE -> "Modo escuro"
        AppLanguage.RUSSIAN -> "Тёмная тема"
        AppLanguage.ITALIAN -> "Modalità scura"
        AppLanguage.TURKISH -> "Karanlık Mod"
        AppLanguage.VIETNAMESE -> "Chế độ tối"
        AppLanguage.THAI -> "โหมดมืด"
        AppLanguage.INDONESIAN -> "Mode Gelap"
    }

    val amoledMode: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "AMOLED 纯黑模式"
        AppLanguage.TRADITIONAL_CHINESE -> "AMOLED 純黑模式"
        AppLanguage.ENGLISH -> "AMOLED Black Mode"
        AppLanguage.JAPANESE -> "AMOLED ピュアブラック"
        AppLanguage.KOREAN -> "AMOLED 리얼 블랙 모드"
        AppLanguage.FRENCH -> "Mode Noir Pur AMOLED"
        AppLanguage.GERMAN -> "AMOLED Reine Schwarz-Modus"
        AppLanguage.SPANISH -> "Modo Negro Puro AMOLED"
        AppLanguage.PORTUGUESE -> "Modo Preto Puro AMOLED"
        AppLanguage.RUSSIAN -> "AMOLED Чисто черный режим"
        AppLanguage.ITALIAN -> "Modalità Nero Puro AMOLED"
        AppLanguage.TURKISH -> "AMOLED Saf Siyah Modu"
        AppLanguage.VIETNAMESE -> "Chế độ Đen Tuyền AMOLED"
        AppLanguage.THAI -> "โหมดดำสนิท AMOLED"
        AppLanguage.INDONESIAN -> "Mode Hitam Pekat AMOLED"
    }

    val customBackground: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "自定义背景图片"
        AppLanguage.TRADITIONAL_CHINESE -> "自訂背景圖片"
        AppLanguage.ENGLISH -> "Custom Background Image"
        AppLanguage.JAPANESE -> "カスタム背景画像"
        AppLanguage.KOREAN -> "사용자 지정 배경 이미지"
        AppLanguage.FRENCH -> "Image de fond personnalisée"
        AppLanguage.GERMAN -> "Benutzerdefiniertes Hintergrundbild"
        AppLanguage.SPANISH -> "Imagen de fondo personalizada"
        AppLanguage.PORTUGUESE -> "Imagem de fundo personalizada"
        AppLanguage.RUSSIAN -> "Пользовательский фон"
        AppLanguage.ITALIAN -> "Immagine di sfondo personalizzata"
        AppLanguage.TURKISH -> "Özel Arka Plan Resmi"
        AppLanguage.VIETNAMESE -> "Hình nền tùy chỉnh"
        AppLanguage.THAI -> "ภาพพื้นหลังที่กำหนดเอง"
        AppLanguage.INDONESIAN -> "Gambar Latar Kustom"
    }

    val selectImage: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "从相册选择图片"
        AppLanguage.TRADITIONAL_CHINESE -> "從相冊選擇圖片"
        AppLanguage.ENGLISH -> "Select Image from Gallery"
        AppLanguage.JAPANESE -> "ギャラリーから選択"
        AppLanguage.KOREAN -> "갤러리에서 이미지 선택"
        AppLanguage.FRENCH -> "Sélectionner depuis la galerie"
        AppLanguage.GERMAN -> "Bild aus Galerie wählen"
        AppLanguage.SPANISH -> "Seleccionar de la galería"
        AppLanguage.PORTUGUESE -> "Selecionar da galeria"
        AppLanguage.RUSSIAN -> "Выбрать из галереи"
        AppLanguage.ITALIAN -> "Seleziona dalla galleria"
        AppLanguage.TURKISH -> "Galeriden Resim Seç"
        AppLanguage.VIETNAMESE -> "Chọn hình từ thư viện"
        AppLanguage.THAI -> "เลือกภาพจากแกลเลอรี"
        AppLanguage.INDONESIAN -> "Pilih Gambar dari Galeri"
    }

    val removeBackground: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "清除背景"
        AppLanguage.TRADITIONAL_CHINESE -> "清除背景"
        AppLanguage.ENGLISH -> "Remove Background"
        AppLanguage.JAPANESE -> "背景を削除"
        AppLanguage.KOREAN -> "배경 제거"
        AppLanguage.FRENCH -> "Effacer l'arrière-plan"
        AppLanguage.GERMAN -> "Hintergrund entfernen"
        AppLanguage.SPANISH -> "Quitar fondo"
        AppLanguage.PORTUGUESE -> "Remover fundo"
        AppLanguage.RUSSIAN -> "Удалить фон"
        AppLanguage.ITALIAN -> "Rimuovi sfondo"
        AppLanguage.TURKISH -> "Arka Planı Kaldır"
        AppLanguage.VIETNAMESE -> "Xóa hình nền"
        AppLanguage.THAI -> "ลบพื้นหลัง"
        AppLanguage.INDONESIAN -> "Hapus Latar"
    }

    val blurRadius: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "背景模糊程度"
        AppLanguage.TRADITIONAL_CHINESE -> "背景模糊程度"
        AppLanguage.ENGLISH -> "Background Blur Radius"
        AppLanguage.JAPANESE -> "背景ブラー効果"
        AppLanguage.KOREAN -> "배경 블러 정도"
        AppLanguage.FRENCH -> "Flou d'arrière-plan"
        AppLanguage.GERMAN -> "Hintergrundunschärfe"
        AppLanguage.SPANISH -> "Desenfoque de fondo"
        AppLanguage.PORTUGUESE -> "Desfoque de fundo"
        AppLanguage.RUSSIAN -> "Размытие фона"
        AppLanguage.ITALIAN -> "Sfocatura sfondo"
        AppLanguage.TURKISH -> "Arka Plan Bulanıklığı"
        AppLanguage.VIETNAMESE -> "Độ mờ nền"
        AppLanguage.THAI -> "รัศมีความเบลอพื้นหลัง"
        AppLanguage.INDONESIAN -> "Tingkat Buram Latar"
    }

    val backgroundOpacity: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "背景透明度"
        AppLanguage.TRADITIONAL_CHINESE -> "背景透明度"
        AppLanguage.ENGLISH -> "Background Opacity"
        AppLanguage.JAPANESE -> "背景不透明度"
        AppLanguage.KOREAN -> "배경 투명도"
        AppLanguage.FRENCH -> "Opacité de l'arrière-plan"
        AppLanguage.GERMAN -> "Hintergrunddeckkraft"
        AppLanguage.SPANISH -> "Opacidad del fondo"
        AppLanguage.PORTUGUESE -> "Opacidade do fundo"
        AppLanguage.RUSSIAN -> "Прозрачность фона"
        AppLanguage.ITALIAN -> "Opacità sfondo"
        AppLanguage.TURKISH -> "Arka Plan Opaklığı"
        AppLanguage.VIETNAMESE -> "Độ trong suốt nền"
        AppLanguage.THAI -> "ความทึบของพื้นหลัง"
        AppLanguage.INDONESIAN -> "Opasitas Latar"
    }

    val appLanguageSetting: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "应用语言"
        AppLanguage.TRADITIONAL_CHINESE -> "應用語言"
        AppLanguage.ENGLISH -> "App Language"
        AppLanguage.JAPANESE -> "アプリの言語"
        AppLanguage.KOREAN -> "앱 언어"
        AppLanguage.FRENCH -> "Langue de l'application"
        AppLanguage.GERMAN -> "App-Sprache"
        AppLanguage.SPANISH -> "Idioma de la aplicación"
        AppLanguage.PORTUGUESE -> "Idioma do aplicativo"
        AppLanguage.RUSSIAN -> "Язык приложения"
        AppLanguage.ITALIAN -> "Lingua dell'app"
        AppLanguage.TURKISH -> "Uygulama Dili"
        AppLanguage.VIETNAMESE -> "Ngôn ngữ ứng dụng"
        AppLanguage.THAI -> "ภาษาของแอป"
        AppLanguage.INDONESIAN -> "Bahasa Aplikasi"
    }

    val defaultStartupWebsite: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "默认启动网页"
        AppLanguage.TRADITIONAL_CHINESE -> "預設啟動網頁"
        AppLanguage.ENGLISH -> "Default Startup Website"
        AppLanguage.JAPANESE -> "デフォルト起動ページ"
        AppLanguage.KOREAN -> "기본 시작 웹사이트"
        AppLanguage.FRENCH -> "Page web au démarrage"
        AppLanguage.GERMAN -> "Standard-Startseite"
        AppLanguage.SPANISH -> "Sitio web de inicio predeterminado"
        AppLanguage.PORTUGUESE -> "Site inicial padrão"
        AppLanguage.RUSSIAN -> "Стартовая страница"
        AppLanguage.ITALIAN -> "Sito web di avvio predefinito"
        AppLanguage.TURKISH -> "Varsayılan Başlangıç Sitesi"
        AppLanguage.VIETNAMESE -> "Trang web khởi động mặc định"
        AppLanguage.THAI -> "เว็บไซต์เริ่มต้นเริ่มต้น"
        AppLanguage.INDONESIAN -> "Situs Web Memulai Default"
    }

    val browserOptions: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "浏览器选项"
        AppLanguage.TRADITIONAL_CHINESE -> "瀏覽器選項"
        AppLanguage.ENGLISH -> "Browser Options"
        AppLanguage.JAPANESE -> "ブラウザオプション"
        AppLanguage.KOREAN -> "브라우저 옵션"
        AppLanguage.FRENCH -> "Options du navigateur"
        AppLanguage.GERMAN -> "Browser-Optionen"
        AppLanguage.SPANISH -> "Opciones de navegador"
        AppLanguage.PORTUGUESE -> "Opções do navegador"
        AppLanguage.RUSSIAN -> "Опции браузера"
        AppLanguage.ITALIAN -> "Opzioni browser"
        AppLanguage.TURKISH -> "Tarayıcı Seçenekleri"
        AppLanguage.VIETNAMESE -> "Tùy chọn trình duyệt"
        AppLanguage.THAI -> "ตัวเลือกเบราว์เซอร์"
        AppLanguage.INDONESIAN -> "Opsi Peramban"
    }

    val desktopMode: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "桌面版网页模式"
        AppLanguage.TRADITIONAL_CHINESE -> "桌面版網頁模式"
        AppLanguage.ENGLISH -> "Desktop View Mode"
        AppLanguage.JAPANESE -> "PC版サイト表示"
        AppLanguage.KOREAN -> "데스크톱 모드"
        AppLanguage.FRENCH -> "Mode Ordinateur"
        AppLanguage.GERMAN -> "Desktop-Ansicht"
        AppLanguage.SPANISH -> "Modo de escritorio"
        AppLanguage.PORTUGUESE -> "Modo para computador"
        AppLanguage.RUSSIAN -> "Версия для ПК"
        AppLanguage.ITALIAN -> "Modalità desktop"
        AppLanguage.TURKISH -> "Masaüstü Modu"
        AppLanguage.VIETNAMESE -> "Chế độ máy tính"
        AppLanguage.THAI -> "โหมดเดสก์ท็อป"
        AppLanguage.INDONESIAN -> "Mode Situs Desktop"
    }

    val clearCache: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "清除浏览器缓存"
        AppLanguage.TRADITIONAL_CHINESE -> "清除瀏覽器快取"
        AppLanguage.ENGLISH -> "Clear Browser Cache"
        AppLanguage.JAPANESE -> "キャッシュを消去"
        AppLanguage.KOREAN -> "캐시 삭제"
        AppLanguage.FRENCH -> "Vider le cache"
        AppLanguage.GERMAN -> "Cache leeren"
        AppLanguage.SPANISH -> "Borrar caché"
        AppLanguage.PORTUGUESE -> "Limpar cache"
        AppLanguage.RUSSIAN -> "Очистить кэш"
        AppLanguage.ITALIAN -> "Svuota cache"
        AppLanguage.TURKISH -> "Önbelleği Temizle"
        AppLanguage.VIETNAMESE -> "Xóa bộ nhớ đệm"
        AppLanguage.THAI -> "ล้างแคช"
        AppLanguage.INDONESIAN -> "Bersihkan Cache"
    }

    val clearCookies: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "清除 Cookie 与登录会话"
        AppLanguage.TRADITIONAL_CHINESE -> "清除 Cookie 與登錄會話"
        AppLanguage.ENGLISH -> "Clear Cookies & Sessions"
        AppLanguage.JAPANESE -> "クッキーとセッションを消去"
        AppLanguage.KOREAN -> "쿠키 및 세션 삭제"
        AppLanguage.FRENCH -> "Effacer les cookies et sessions"
        AppLanguage.GERMAN -> "Cookies & Sitzungen löschen"
        AppLanguage.SPANISH -> "Borrar cookies y sesiones"
        AppLanguage.PORTUGUESE -> "Limpar cookies e sessões"
        AppLanguage.RUSSIAN -> "Очистить куки и сессии"
        AppLanguage.ITALIAN -> "Cancella cookie e sessioni"
        AppLanguage.TURKISH -> "Çerezleri ve Oturumları Temizle"
        AppLanguage.VIETNAMESE -> "Xóa cookie và phiên đăng nhập"
        AppLanguage.THAI -> "ล้างคุกกี้และเซสชัน"
        AppLanguage.INDONESIAN -> "Bersihkan Cookie & Sesi"
    }

    val aboutApp: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "关于 MCSR Ranked"
        AppLanguage.TRADITIONAL_CHINESE -> "關於 MCSR Ranked"
        AppLanguage.ENGLISH -> "About MCSR Ranked"
        AppLanguage.JAPANESE -> "MCSR Ranked について"
        AppLanguage.KOREAN -> "MCSR Ranked 정보"
        AppLanguage.FRENCH -> "À propos de MCSR Ranked"
        AppLanguage.GERMAN -> "Über MCSR Ranked"
        AppLanguage.SPANISH -> "Acerca de MCSR Ranked"
        AppLanguage.PORTUGUESE -> "Sobre o MCSR Ranked"
        AppLanguage.RUSSIAN -> "О приложении MCSR Ranked"
        AppLanguage.ITALIAN -> "Informazioni su MCSR Ranked"
        AppLanguage.TURKISH -> "MCSR Ranked Hakkında"
        AppLanguage.VIETNAMESE -> "Về MCSR Ranked"
        AppLanguage.THAI -> "เกี่ยวกับ MCSR Ranked"
        AppLanguage.INDONESIAN -> "Tentang MCSR Ranked"
    }

    val privacyPolicy: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "隐私政策"
        AppLanguage.TRADITIONAL_CHINESE -> "隱私政策"
        AppLanguage.ENGLISH -> "Privacy Policy"
        AppLanguage.JAPANESE -> "プライバシーポリシー"
        AppLanguage.KOREAN -> "개인정보 처리방침"
        AppLanguage.FRENCH -> "Politique de confidentialité"
        AppLanguage.GERMAN -> "Datenschutz-Bestimmungen"
        AppLanguage.SPANISH -> "Política de privacidad"
        AppLanguage.PORTUGUESE -> "Política de privacidade"
        AppLanguage.RUSSIAN -> "Политика конфиденциальности"
        AppLanguage.ITALIAN -> "Informativa sulla privacy"
        AppLanguage.TURKISH -> "Gizlilik Politikası"
        AppLanguage.VIETNAMESE -> "Chính sách bảo mật"
        AppLanguage.THAI -> "นโยบายความเป็นส่วนตัว"
        AppLanguage.INDONESIAN -> "Kebijakan Privasi"
    }

    val checkUpdates: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "检查更新"
        AppLanguage.TRADITIONAL_CHINESE -> "檢查更新"
        AppLanguage.ENGLISH -> "Check for Updates"
        AppLanguage.JAPANESE -> "アップデートを確認"
        AppLanguage.KOREAN -> "업데이트 확인"
        AppLanguage.FRENCH -> "Vérifier les mises à jour"
        AppLanguage.GERMAN -> "Nach Updates suchen"
        AppLanguage.SPANISH -> "Buscar actualizaciones"
        AppLanguage.PORTUGUESE -> "Verificar atualizações"
        AppLanguage.RUSSIAN -> "Проверить обновления"
        AppLanguage.ITALIAN -> "Controlla aggiornamenti"
        AppLanguage.TURKISH -> "Güncelleştirmeleri Kontrol Et"
        AppLanguage.VIETNAMESE -> "Kiểm tra cập nhật"
        AppLanguage.THAI -> "ตรวจสอบการอัปเดต"
        AppLanguage.INDONESIAN -> "Periksa Pembaruan"
    }

    val version: String get() = "v1.0.0 (Build 2026)"
    val latestVersionMsg: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "当前已是最新版本！"
        AppLanguage.TRADITIONAL_CHINESE -> "當前已是最新版本！"
        AppLanguage.ENGLISH -> "You are already on the latest version!"
        AppLanguage.JAPANESE -> "最新バージョンをご利用中です！"
        AppLanguage.KOREAN -> "현재 최신 버전을 사용 중입니다!"
        AppLanguage.FRENCH -> "Vous utilisez déjà la dernière version !"
        AppLanguage.GERMAN -> "Sie verwenden bereits die neueste Version!"
        AppLanguage.SPANISH -> "¡Ya estás en la versión más reciente!"
        AppLanguage.PORTUGUESE -> "Você já está na versão mais recente!"
        AppLanguage.RUSSIAN -> "У вас уже установлена последняя версия!"
        AppLanguage.ITALIAN -> "Sei già all'ultima versione!"
        AppLanguage.TURKISH -> "Zaten en son sürümü kullanıyorsunuz!"
        AppLanguage.VIETNAMESE -> "Bạn đang ở phiên bản mới nhất!"
        AppLanguage.THAI -> "คุณใช้เวอร์ชันล่าสุดอยู่แล้ว!"
        AppLanguage.INDONESIAN -> "Anda sudah menggunakan versi terbaru!"
    }

    val confirm: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "确认"
        AppLanguage.TRADITIONAL_CHINESE -> "確認"
        AppLanguage.ENGLISH -> "Confirm"
        AppLanguage.JAPANESE -> "確認"
        AppLanguage.KOREAN -> "확인"
        AppLanguage.FRENCH -> "Confirmer"
        AppLanguage.GERMAN -> "Bestätigen"
        AppLanguage.SPANISH -> "Confirmar"
        AppLanguage.PORTUGUESE -> "Confirmar"
        AppLanguage.RUSSIAN -> "Подтвердить"
        AppLanguage.ITALIAN -> "Conferma"
        AppLanguage.TURKISH -> "Onayla"
        AppLanguage.VIETNAMESE -> "Xác nhận"
        AppLanguage.THAI -> "ยืนยัน"
        AppLanguage.INDONESIAN -> "Konfirmasi"
    }

    val cancel: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "取消"
        AppLanguage.TRADITIONAL_CHINESE -> "取消"
        AppLanguage.ENGLISH -> "Cancel"
        AppLanguage.JAPANESE -> "キャンセル"
        AppLanguage.KOREAN -> "취소"
        AppLanguage.FRENCH -> "Annuler"
        AppLanguage.GERMAN -> "Abbrechen"
        AppLanguage.SPANISH -> "Cancelar"
        AppLanguage.PORTUGUESE -> "Cancelar"
        AppLanguage.RUSSIAN -> "Отмена"
        AppLanguage.ITALIAN -> "Annulla"
        AppLanguage.TURKISH -> "İptal"
        AppLanguage.VIETNAMESE -> "Hủy"
        AppLanguage.THAI -> "ยกเลิก"
        AppLanguage.INDONESIAN -> "Batal"
    }

    val successCleared: String get() = when(language) {
        AppLanguage.SIMPLIFIED_CHINESE -> "清理完成！"
        AppLanguage.TRADITIONAL_CHINESE -> "清理完成！"
        AppLanguage.ENGLISH -> "Successfully cleared!"
        AppLanguage.JAPANESE -> "消去完了！"
        AppLanguage.KOREAN -> "삭제 완료!"
        AppLanguage.FRENCH -> "Nettoyage réussi !"
        AppLanguage.GERMAN -> "Erfolgreich gelöscht!"
        AppLanguage.SPANISH -> "¡Limpiado con éxito!"
        AppLanguage.PORTUGUESE -> "Limpo com sucesso!"
        AppLanguage.RUSSIAN -> "Успешно очищено!"
        AppLanguage.ITALIAN -> "Pulizia riuscita!"
        AppLanguage.TURKISH -> "Başarıyla temizlendi!"
        AppLanguage.VIETNAMESE -> "Đã xóa thành công!"
        AppLanguage.THAI -> "ล้างสำเร็จแล้ว!"
        AppLanguage.INDONESIAN -> "Berhasil dibersihkan!"
    }
}

val LocalAppStrings = staticCompositionLocalOf { AppStrings(AppLanguage.SIMPLIFIED_CHINESE) }
