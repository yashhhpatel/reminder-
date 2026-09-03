package com.remindly.app.data.repository

import com.remindly.app.data.local.dao.CategoryDao
import com.remindly.app.data.local.dao.ReminderDao
import com.remindly.app.data.local.entity.CategoryEntity
import com.remindly.app.data.local.entity.toDomain
import com.remindly.app.data.local.entity.toEntity
import com.remindly.app.domain.model.Category
import com.remindly.app.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao,
    private val reminderDao: ReminderDao,
) : CategoryRepository {

    override fun observeAll(): Flow<List<Category>> =
        categoryDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): Category? = categoryDao.getById(id)?.toDomain()

    override suspend fun create(name: String, colorArgb: Int): Long =
        categoryDao.insert(CategoryEntity(name = name, colorArgb = colorArgb, isBuiltIn = false, sortOrder = 100))

    override suspend fun update(category: Category) {
        if (category.isBuiltIn) return
        categoryDao.update(category.toEntity())
    }

    override suspend fun delete(category: Category) {
        if (category.isBuiltIn) return
        reminderDao.reassignCategory(category.id, Category.DEFAULT_CATEGORY_ID)
        categoryDao.delete(category.toEntity())
    }

    override suspend fun ensureDefaultsSeeded() {
        if (categoryDao.count() > 0) return
        categoryDao.insertAll(DefaultCategories.seed())
    }
}

object DefaultCategories {
    fun seed(): List<CategoryEntity> = listOf(
        CategoryEntity(id = Category.DEFAULT_CATEGORY_ID, name = "My reminders", colorArgb = 0xFF6A3DE8.toInt(), isBuiltIn = true, sortOrder = 0),
        CategoryEntity(name = "Work", colorArgb = 0xFF3D7BE8.toInt(), isBuiltIn = true, sortOrder = 1),
        CategoryEntity(name = "Personal", colorArgb = 0xFFE5484D.toInt(), isBuiltIn = true, sortOrder = 2),
        CategoryEntity(name = "My health", colorArgb = 0xFF6BC24A.toInt(), isBuiltIn = true, sortOrder = 3),
        CategoryEntity(name = "Finance", colorArgb = 0xFF4B3FA8.toInt(), isBuiltIn = true, sortOrder = 4),
    )
}
