package com.remindly.app.ui.screens.privacy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindly.app.data.datastore.SettingsDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class PrivacySettingsUiState(
    val notificationsEnabled: Boolean = true,
    val locationEnabled: Boolean = true,
    val analyticsEnabled: Boolean = false,
    val personalizedContentEnabled: Boolean = false,
)

class PrivacySettingsViewModel(private val settingsDataStore: SettingsDataStore) : ViewModel() {

    val uiState: StateFlow<PrivacySettingsUiState> = combine(
        settingsDataStore.locationPrivacyEnabled,
        settingsDataStore.analyticsEnabled,
        settingsDataStore.personalizedContentEnabled,
    ) { location, analytics, personalized ->
        PrivacySettingsUiState(
            locationEnabled = location,
            analyticsEnabled = analytics,
            personalizedContentEnabled = personalized,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PrivacySettingsUiState())

    fun setLocationEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setLocationPrivacyEnabled(enabled) }
    }

    fun setAnalyticsEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setAnalyticsEnabled(enabled) }
    }

    fun setPersonalizedContentEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsDataStore.setPersonalizedContentEnabled(enabled) }
    }
}
