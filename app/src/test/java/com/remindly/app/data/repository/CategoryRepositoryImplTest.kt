package com.remindly.app.data.repository

import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.remindly.app.data.local.RemindlyDatabase
import com.remindly.app.domain.model.Category
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class CategoryRepositoryImplTest {

    private lateinit var database: RemindlyDatabase
    private lateinit var repository: CategoryRepositoryImpl

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindlyDatabase::class.java,
        ).allowMainThreadQueries().build()
        repository = CategoryRepositoryImpl(database.categoryDao(), database.reminderDao())
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `ensureDefaultsSeeded populates the five built-in categories once`() = runBlocking {
        repository.ensureDefaultsSeeded()
        repository.ensureDefaultsSeeded()

        val categories = repository.observeAll().first()
        assertEquals(5, categories.size)
        assertTrue(categories.all { it.isBuiltIn })
    }

    @Test
    fun `create adds a custom non-built-in category`() = runBlocking {
        val id = repository.create("Travel", 0xFF00FF00.toInt())
        val category = repository.getById(id)

        assertEquals("Travel", category?.name)
        assertEquals(false, category?.isBuiltIn)
    }

    @Test
    fun `deleting a built-in category is a no-op`() = runBlocking {
        repository.ensureDefaultsSeeded()
        val builtIn = repository.getById(Category.DEFAULT_CATEGORY_ID)!!

        repository.delete(builtIn)

        assertEquals(builtIn, repository.getById(Category.DEFAULT_CATEGORY_ID))
    }

    @Test
    fun `deleting a custom category reassigns its reminders to the default category`() = runBlocking {
        repository.ensureDefaultsSeeded()
        val customId = repository.create("Travel", 0xFF00FF00.toInt())
        val reminderId = database.reminderDao().insert(
            com.remindly.app.data.local.entity.ReminderEntity(
                title = "Book flight",
                createdAt = 0L,
                updatedAt = 0L,
                categoryId = customId,
            )
        )

        val category = repository.getById(customId)!!
        repository.delete(category)

        val reminder = database.reminderDao().getById(reminderId)
        assertEquals(Category.DEFAULT_CATEGORY_ID, reminder?.categoryId)
        assertNull(repository.getById(customId))
    }
}
