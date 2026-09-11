package com.remindly.app.ui.screens.settings

data class LanguageOption(val code: String, val displayName: String)

val SupportedLanguages = listOf(
    LanguageOption("en", "English"),
    LanguageOption("zh", "中文"),
    LanguageOption("hi", "हिन्दी"),
    LanguageOption("fr", "Français"),
    LanguageOption("ar", "العربية"),
    LanguageOption("it", "Italiano"),
    LanguageOption("es", "Español"),
    LanguageOption("bn", "বাংলা"),
    LanguageOption("ru", "Русский"),
    LanguageOption("pt", "Português"),
    LanguageOption("de", "Deutsch"),
    LanguageOption("th", "ไทย"),
    LanguageOption("ja", "日本語"),
    LanguageOption("ko", "한국어"),
    LanguageOption("vi", "Tiếng Việt"),
    LanguageOption("tr", "Türkçe"),
    LanguageOption("in", "Bahasa Indonesia"),
    LanguageOption("ur", "اردو"),
)

fun languageDisplayName(code: String): String =
    SupportedLanguages.firstOrNull { it.code == code }?.displayName ?: SupportedLanguages.first().displayName
