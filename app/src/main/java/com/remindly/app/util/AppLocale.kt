package com.remindly.app.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/** Unwraps ContextWrapper layers (e.g. Compose's LocalContext) to find the hosting Activity. */
tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

/**
 * Real per-app language switching. AppCompatDelegate.setApplicationLocales() alone only
 * reliably re-applies the locale on API 33+ (the OS's own LocaleManager) or on an
 * AppCompatActivity; our MainActivity is a plain ComponentActivity and the app must support
 * API 26+, so on API <33 nothing would actually change without this. Instead we persist the
 * chosen language ourselves (a plain synchronous SharedPreferences read/write, safe to call
 * from attachBaseContext before any coroutine machinery is ready) and manually wrap every
 * Context — the Application's and each Activity's — with a Configuration carrying that Locale.
 * That covers Activity/Compose UI *and* applicationContext.getString() calls made from
 * BroadcastReceivers when building notification text.
 *
 * AppCompatDelegate.setApplicationLocales() is still called too, purely so the OS's own
 * per-app language system-settings page (API 33+) reflects the choice.
 */
object AppLocale {
    private const val PREFS_NAME = "remindly_locale_prefs"
    private const val KEY_LANGUAGE = "language_tag"

    /** Call when the user picks a new language. */
    fun apply(context: Context, code: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, code)
            .apply()
        val locales = if (code == "en") {
            LocaleListCompat.forLanguageTags("en")
        } else {
            LocaleListCompat.forLanguageTags(code)
        }
        AppCompatDelegate.setApplicationLocales(locales)
    }

    /** Call from attachBaseContext() on the Application and on every Activity. */
    fun wrap(base: Context): Context {
        val code = base.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LANGUAGE, null) ?: return base
        val locale = Locale(code)
        Locale.setDefault(locale)
        val config = Configuration(base.resources.configuration)
        config.setLocale(locale)
        return base.createConfigurationContext(config)
    }

    /** One-time migration for the language value that used to be saved to DataStore only
     *  (before this ever actually changed anything) — makes it take effect retroactively. */
    fun migrateIfNeeded(context: Context, dataStoreLanguageCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        if (prefs.contains(KEY_LANGUAGE)) return
        if (dataStoreLanguageCode != "en") apply(context, dataStoreLanguageCode)
    }
}
