package com.remindly.app.ui.screens.newreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindly.app.domain.model.Category
import com.remindly.app.domain.repository.CategoryRepository
import com.remindly.app.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryListViewModel(
    private val categoryRepository: CategoryRepository,
    private val reminderRepository: ReminderRepository,
) : ViewModel() {

    val categories: StateFlow<List<Category>> = categoryRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val reminderCountByCategory: StateFlow<Map<Long, Int>> = reminderRepository.observeAll()
        .map { reminders -> reminders.groupingBy { it.categoryId }.eachCount() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun createCategory(name: String, colorArgb: Int, onCreated: (Long) -> Unit) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val id = categoryRepository.create(name.trim(), colorArgb)
            onCreated(id)
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch { categoryRepository.update(category) }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch { categoryRepository.delete(category) }
    }
}
