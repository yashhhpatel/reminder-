package com.remindly.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindly.app.domain.model.Reminder
import com.remindly.app.domain.repository.CategoryRepository
import com.remindly.app.domain.repository.PremiumRepository
import com.remindly.app.domain.repository.ReminderRepository
import com.remindly.app.domain.usecase.QuickAddParser
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class HomeUiState(
    val upcoming: List<Reminder> = emptyList(),
    val completed: List<Reminder> = emptyList(),
    val isPremium: Boolean = false,
    val isLoading: Boolean = true,
) {
    val isEmpty: Boolean get() = upcoming.isEmpty() && completed.isEmpty()
}

class HomeViewModel(
    private val reminderRepository: ReminderRepository,
    private val categoryRepository: CategoryRepository,
    premiumRepository: PremiumRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = combine(
        reminderRepository.observeAll(),
        premiumRepository.isPremium,
    ) { reminders, isPremium ->
        HomeUiState(
            upcoming = reminders.filter { !it.isCompleted },
            completed = reminders.filter { it.isCompleted },
            isPremium = isPremium,
            isLoading = false,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HomeUiState())

    init {
        viewModelScope.launch {
            categoryRepository.ensureDefaultsSeeded()
        }
    }

    fun quickAdd(rawText: String) {
        if (rawText.isBlank()) return
        val parsed = QuickAddParser.parse(rawText)
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            reminderRepository.save(
                Reminder(
                    title = parsed.title,
                    createdAt = now,
                    updatedAt = now,
                    dateTime = parsed.dateTime,
                )
            )
        }
    }

    fun quickAddWithTime(rawText: String, dateTime: Long?) {
        if (rawText.isBlank()) return
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            reminderRepository.save(
                Reminder(
                    title = rawText.trim(),
                    createdAt = now,
                    updatedAt = now,
                    dateTime = dateTime,
                )
            )
        }
    }

    fun setCompleted(reminder: Reminder, completed: Boolean) {
        viewModelScope.launch {
            reminderRepository.setCompleted(reminder.id, completed)
        }
    }
}
