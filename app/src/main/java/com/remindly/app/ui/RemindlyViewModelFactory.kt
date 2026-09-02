package com.remindly.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.remindly.app.AppContainer
import com.remindly.app.ui.screens.home.HomeViewModel
import com.remindly.app.ui.screens.newreminder.CategoryListViewModel
import com.remindly.app.ui.screens.newreminder.ReminderEditorViewModel
import com.remindly.app.ui.screens.premium.PremiumViewModel
import com.remindly.app.ui.screens.privacy.PrivacySettingsViewModel
import com.remindly.app.ui.screens.search.SearchViewModel
import com.remindly.app.ui.screens.settings.SettingsViewModel

class RemindlyViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) ->
                HomeViewModel(container.reminderRepository, container.categoryRepository, container.premiumRepository) as T

            modelClass.isAssignableFrom(ReminderEditorViewModel::class.java) ->
                ReminderEditorViewModel(container.reminderRepository, container.categoryRepository) as T

            modelClass.isAssignableFrom(CategoryListViewModel::class.java) ->
                CategoryListViewModel(container.categoryRepository) as T

            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(container.reminderRepository, container.categoryRepository) as T

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(container.settingsDataStore, container.premiumRepository) as T

            modelClass.isAssignableFrom(PremiumViewModel::class.java) ->
                PremiumViewModel(container.premiumRepository) as T

            modelClass.isAssignableFrom(PrivacySettingsViewModel::class.java) ->
                PrivacySettingsViewModel(container.settingsDataStore) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
