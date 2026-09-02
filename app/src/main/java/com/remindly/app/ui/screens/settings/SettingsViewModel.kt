package com.remindly.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindly.app.data.datastore.SettingsDataStore
import com.remindly.app.domain.model.AppThemeMode
import com.remindly.app.domain.repository.PremiumRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class SettingsUiState(
    val themeMode: AppThemeMode = AppThemeMode.AUTO,
    val language: String = "en",
    val isPremium: Boolean = false,
    val presetAddTime: Boolean = true,
    val presetAddPlace: Boolean = false,
    val presetAfterCall: Boolean = true,
)

class SettingsViewModel(
    private val settingsDataStore: SettingsDataStore,
    premiumRepository: PremiumRepository,
) : ViewModel() {

    val uiState: StateFlow<SettingsUiState> = combine(
        settingsDataStore.themeMode,
        settingsDataStore.language,
        premiumRepository.isPremium,
        settingsDataStore.presetAddTime,
        settingsDataStore.presetAddPlace,
        settingsDataStore.presetAfterCall,
    ) { values ->
        SettingsUiState(
            themeMode = values[0] as AppThemeMode,
            language = values[1] as String,
            isPremium = values[2] as Boolean,
            presetAddTime = values[3] as Boolean,
            presetAddPlace = values[4] as Boolean,
            presetAfterCall = values[5] as Boolean,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsUiState())

    fun setThemeMode(mode: AppThemeMode) {
        viewModelScope.launch { settingsDataStore.setThemeMode(mode) }
    }

    fun setLanguage(code: String) {
        viewModelScope.launch { settingsDataStore.setLanguage(code) }
    }

    fun setPresetAddTime(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setPresetAddTime(enabled) }
    }

    fun setPresetAddPlace(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setPresetAddPlace(enabled) }
    }

    fun setPresetAfterCall(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setPresetAfterCall(enabled) }
    }
}
