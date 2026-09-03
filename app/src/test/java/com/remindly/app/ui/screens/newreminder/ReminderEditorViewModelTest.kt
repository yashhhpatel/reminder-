package com.remindly.app.ui.screens.newreminder

import com.remindly.app.domain.model.Category
import com.remindly.app.domain.model.Reminder
import com.remindly.app.domain.repository.CategoryRepository
import com.remindly.app.domain.repository.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

private class FakeReminderRepository : ReminderRepository {
    val remindersById = mutableMapOf<Long, Reminder>()

    override fun observeAll(): Flow<List<Reminder>> = MutableStateFlow(remindersById.values.toList())
    override fun observeById(id: Long): Flow<Reminder?> = MutableStateFlow(remindersById[id])
    override fun search(query: String): Flow<List<Reminder>> = MutableStateFlow(emptyList())
    override fun observeByCategory(categoryId: Long): Flow<List<Reminder>> = MutableStateFlow(emptyList())
    override fun countByCategory(categoryId: Long): Flow<Int> = MutableStateFlow(0)
    override suspend fun getById(id: Long): Reminder? = remindersById[id]
    override suspend fun save(reminder: Reminder): Long {
        val id = if (reminder.id == 0L) (remindersById.size + 1).toLong() else reminder.id
        remindersById[id] = reminder.copy(id = id)
        return id
    }
    override suspend fun delete(reminder: Reminder) { remindersById.remove(reminder.id) }
    override suspend fun setCompleted(id: Long, completed: Boolean) {
        remindersById[id]?.let { remindersById[id] = it.copy(isCompleted = completed) }
    }
    override suspend fun snooze(id: Long, untilEpochMillis: Long) {}
    override suspend fun restoreAllScheduledAlarms() {}
}

private class FakeCategoryRepository : CategoryRepository {
    override fun observeAll(): Flow<List<Category>> = MutableStateFlow(emptyList())
    override suspend fun getById(id: Long): Category? =
        Category(id = id, name = "My reminders", colorArgb = 0xFF6A3DE8.toInt(), isBuiltIn = true)
    override suspend fun create(name: String, colorArgb: Int): Long = 1L
    override suspend fun update(category: Category) {}
    override suspend fun delete(category: Category) {}
    override suspend fun ensureDefaultsSeeded() {}
}

/**
 * Regression test for a real bug found via live emulator testing: Navigation Compose disposes
 * and rebuilds the New/Edit Reminder screen's composition every time a sub-screen (Repeat,
 * Category, Location) is pushed and popped, re-firing LaunchedEffect(reminderId) { load(...) }
 * even though reminderId hasn't changed — which used to silently wipe in-progress edits.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class ReminderEditorViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var viewModel: ReminderEditorViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        reminderRepository = FakeReminderRepository()
        viewModel = ReminderEditorViewModel(reminderRepository, FakeCategoryRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `re-loading the same new-reminder id preserves in-progress edits`() = runTest(dispatcher) {
        viewModel.load(0L)
        dispatcher.scheduler.advanceUntilIdle()

        viewModel.updateTitle("Call mom")
        viewModel.toggleAddTime(true)

        // Simulate returning from a sub-screen (Repeat/Category/Location), which re-fires
        // LaunchedEffect(reminderId) { load(reminderId) } with the same id.
        viewModel.load(0L)
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.state.value
        assertEquals("Call mom", state.title)
        assertTrue(state.addTimeEnabled)
    }

    @Test
    fun `re-loading the same existing-reminder id preserves in-progress edits`() = runTest(dispatcher) {
        val now = System.currentTimeMillis()
        reminderRepository.remindersById[42L] = Reminder(
            id = 42L,
            title = "Original title",
            createdAt = now,
            updatedAt = now,
            dateTime = null,
        )

        viewModel.load(42L)
        dispatcher.scheduler.advanceUntilIdle()
        viewModel.updateTitle("Edited title")

        viewModel.load(42L)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals("Edited title", viewModel.state.value.title)
    }

    @Test
    fun `forceReload discards in-progress edits and re-fetches from the repository`() = runTest(dispatcher) {
        val now = System.currentTimeMillis()
        reminderRepository.remindersById[7L] = Reminder(
            id = 7L,
            title = "Saved title",
            createdAt = now,
            updatedAt = now,
            dateTime = null,
        )

        viewModel.load(7L)
        dispatcher.scheduler.advanceUntilIdle()
        viewModel.updateTitle("Unsaved local edit")

        viewModel.load(7L, forceReload = true)
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals("Saved title", viewModel.state.value.title)
    }

    @Test
    fun `loading a different reminder id after another does reload`() = runTest(dispatcher) {
        val now = System.currentTimeMillis()
        reminderRepository.remindersById[1L] = Reminder(id = 1L, title = "First", createdAt = now, updatedAt = now, dateTime = null)
        reminderRepository.remindersById[2L] = Reminder(id = 2L, title = "Second", createdAt = now, updatedAt = now, dateTime = null)

        viewModel.load(1L)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals("First", viewModel.state.value.title)

        viewModel.load(2L)
        dispatcher.scheduler.advanceUntilIdle()
        assertEquals("Second", viewModel.state.value.title)
    }
}
