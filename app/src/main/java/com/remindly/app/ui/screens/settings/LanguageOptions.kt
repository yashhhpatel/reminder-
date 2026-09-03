package com.remindly.app.ui.screens.settings

data class LanguageOption(val code: String, val displayName: String)

// Only "en" ships with translated strings today; the others are listed so the picker's UI
// matches the reference design and so DataStore already persists a real language code for
// them — Android's normal resource-fallback behavior means selecting one simply shows English
// until a matching values-<code>/strings.xml is added, exactly as it would for any new locale.
val SupportedLanguages = listOf(
    LanguageOption("en", "English"),
    LanguageOption("es", "Español"),
    LanguageOption("hi", "हिन्दी"),
    LanguageOption("fr", "Français"),
    LanguageOption("zh", "中文"),
    LanguageOption("ar", "العربية"),
    LanguageOption("ru", "Русский"),
    LanguageOption("pt", "Português"),
    LanguageOption("it", "Italiano"),
    LanguageOption("bn", "বাংলা"),
    LanguageOption("de", "Deutsch"),
    LanguageOption("ja", "日本語"),
    LanguageOption("ko", "한국어"),
)

fun languageDisplayName(code: String): String =
    SupportedLanguages.firstOrNull { it.code == code }?.displayName ?: SupportedLanguages.first().displayName
