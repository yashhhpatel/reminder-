package com.remindly.app.domain.repository

import com.remindly.app.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun observeAll(): Flow<List<Category>>
    suspend fun getById(id: Long): Category?
    suspend fun create(name: String, colorArgb: Int): Long
    suspend fun update(category: Category)
    suspend fun delete(category: Category)
    suspend fun ensureDefaultsSeeded()
}
