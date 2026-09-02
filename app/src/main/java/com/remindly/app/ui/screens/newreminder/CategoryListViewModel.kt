package com.remindly.app.ui.screens.newreminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.remindly.app.domain.model.Category
import com.remindly.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CategoryListViewModel(
    private val categoryRepository: CategoryRepository,
) : ViewModel() {

    val categories: StateFlow<List<Category>> = categoryRepository.observeAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
