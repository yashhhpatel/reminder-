package com.remindly.app.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.remindly.app.domain.model.AppThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "remindly_settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val ONBOARDING_DONE = booleanPreferencesKey("onboarding_done")
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val IS_PREMIUM = booleanPreferencesKey("is_premium")
        val PRESET_ADD_TIME = booleanPreferencesKey("preset_add_time")
        val PRESET_ADD_PLACE = booleanPreferencesKey("preset_add_place")
        val PRESET_AFTER_CALL = booleanPreferencesKey("preset_after_call")
        val NOTIF_PERMISSION_ASKED = booleanPreferencesKey("notif_permission_asked")
        val OVERLAY_PERMISSION_ASKED = booleanPreferencesKey("overlay_permission_asked")
        val PRIVACY_CONSENT_DONE = booleanPreferencesKey("privacy_consent_done")
        val ANALYTICS_ENABLED = booleanPreferencesKey("analytics_enabled")
        val LOCATION_PRIVACY_ENABLED = booleanPreferencesKey("location_privacy_enabled")
        val PERSONALIZED_CONTENT_ENABLED = booleanPreferencesKey("personalized_content_enabled")
    }

    val onboardingDone: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.ONBOARDING_DONE] ?: false }

    suspend fun setOnboardingDone(done: Boolean) {
        context.dataStore.edit { it[Keys.ONBOARDING_DONE] = done }
    }

    val themeMode: Flow<AppThemeMode> = context.dataStore.data.map {
        runCatching { AppThemeMode.valueOf(it[Keys.THEME_MODE] ?: AppThemeMode.AUTO.name) }
            .getOrDefault(AppThemeMode.AUTO)
    }

    suspend fun setThemeMode(mode: AppThemeMode) {
        context.dataStore.edit { it[Keys.THEME_MODE] = mode.name }
    }

    val language: Flow<String> = context.dataStore.data.map { it[Keys.LANGUAGE] ?: "en" }

    suspend fun setLanguage(code: String) {
        context.dataStore.edit { it[Keys.LANGUAGE] = code }
    }

    val isPremium: Flow<Boolean> = context.dataStore.data.map { it[Keys.IS_PREMIUM] ?: false }

    suspend fun setPremium(value: Boolean) {
        context.dataStore.edit { it[Keys.IS_PREMIUM] = value }
    }

    val presetAddTime: Flow<Boolean> = context.dataStore.data.map { it[Keys.PRESET_ADD_TIME] ?: true }
    suspend fun setPresetAddTime(value: Boolean) {
        context.dataStore.edit { it[Keys.PRESET_ADD_TIME] = value }
    }

    val presetAddPlace: Flow<Boolean> = context.dataStore.data.map { it[Keys.PRESET_ADD_PLACE] ?: false }
    suspend fun setPresetAddPlace(value: Boolean) {
        context.dataStore.edit { it[Keys.PRESET_ADD_PLACE] = value }
    }

    val presetAfterCall: Flow<Boolean> = context.dataStore.data.map { it[Keys.PRESET_AFTER_CALL] ?: true }
    suspend fun setPresetAfterCall(value: Boolean) {
        context.dataStore.edit { it[Keys.PRESET_AFTER_CALL] = value }
    }

    val notificationPermissionAsked: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.NOTIF_PERMISSION_ASKED] ?: false }
    suspend fun setNotificationPermissionAsked(value: Boolean) {
        context.dataStore.edit { it[Keys.NOTIF_PERMISSION_ASKED] = value }
    }

    val overlayPermissionAsked: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.OVERLAY_PERMISSION_ASKED] ?: false }
    suspend fun setOverlayPermissionAsked(value: Boolean) {
        context.dataStore.edit { it[Keys.OVERLAY_PERMISSION_ASKED] = value }
    }

    val privacyConsentDone: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.PRIVACY_CONSENT_DONE] ?: false }
    suspend fun setPrivacyConsentDone(value: Boolean) {
        context.dataStore.edit { it[Keys.PRIVACY_CONSENT_DONE] = value }
    }

    val analyticsEnabled: Flow<Boolean> = context.dataStore.data.map { it[Keys.ANALYTICS_ENABLED] ?: false }
    suspend fun setAnalyticsEnabled(value: Boolean) {
        context.dataStore.edit { it[Keys.ANALYTICS_ENABLED] = value }
    }

    val locationPrivacyEnabled: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.LOCATION_PRIVACY_ENABLED] ?: true }
    suspend fun setLocationPrivacyEnabled(value: Boolean) {
        context.dataStore.edit { it[Keys.LOCATION_PRIVACY_ENABLED] = value }
    }

    val personalizedContentEnabled: Flow<Boolean> =
        context.dataStore.data.map { it[Keys.PERSONALIZED_CONTENT_ENABLED] ?: false }
    suspend fun setPersonalizedContentEnabled(value: Boolean) {
        context.dataStore.edit { it[Keys.PERSONALIZED_CONTENT_ENABLED] = value }
    }
}
