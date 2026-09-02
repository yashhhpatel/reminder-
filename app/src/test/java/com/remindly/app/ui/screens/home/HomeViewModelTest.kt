package com.remindly.app.ui.screens.home

import com.remindly.app.domain.model.Category
import com.remindly.app.domain.model.Reminder
import com.remindly.app.domain.repository.CategoryRepository
import com.remindly.app.domain.repository.PremiumRepository
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
    val remindersFlow = MutableStateFlow<List<Reminder>>(emptyList())
    val saved = mutableListOf<Reminder>()

    override fun observeAll(): Flow<List<Reminder>> = remindersFlow.asStateFlow()
    override fun observeById(id: Long): Flow<Reminder?> = MutableStateFlow(remindersFlow.value.find { it.id == id })
    override fun search(query: String): Flow<List<Reminder>> = remindersFlow.asStateFlow()
    override fun observeByCategory(categoryId: Long): Flow<List<Reminder>> = remindersFlow.asStateFlow()
    override fun countByCategory(categoryId: Long): Flow<Int> = MutableStateFlow(0)
    override suspend fun getById(id: Long): Reminder? = remindersFlow.value.find { it.id == id }

    override suspend fun save(reminder: Reminder): Long {
        val id = if (reminder.id == 0L) (saved.size + 1).toLong() else reminder.id
        val toSave = reminder.copy(id = id)
        saved.add(toSave)
        remindersFlow.value = remindersFlow.value + toSave
        return id
    }

    override suspend fun delete(reminder: Reminder) {
        remindersFlow.value = remindersFlow.value.filterNot { it.id == reminder.id }
    }

    override suspend fun setCompleted(id: Long, completed: Boolean) {
        remindersFlow.value = remindersFlow.value.map { if (it.id == id) it.copy(isCompleted = completed) else it }
    }

    override suspend fun snooze(id: Long, untilEpochMillis: Long) {}
    override suspend fun restoreAllScheduledAlarms() {}
}

private class FakeCategoryRepository : CategoryRepository {
    var seedCalled = false
    override fun observeAll(): Flow<List<Category>> = MutableStateFlow(emptyList())
    override suspend fun getById(id: Long): Category? = null
    override suspend fun create(name: String, colorArgb: Int): Long = 1L
    override suspend fun update(category: Category) {}
    override suspend fun delete(category: Category) {}
    override suspend fun ensureDefaultsSeeded() { seedCalled = true }
}

private class FakePremiumRepository : PremiumRepository {
    override val isPremium: Flow<Boolean> = MutableStateFlow(false)
    override suspend fun setPremium(value: Boolean) {}
    override suspend fun restorePurchases(): Boolean = false
}

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()
    private lateinit var reminderRepository: FakeReminderRepository
    private lateinit var categoryRepository: FakeCategoryRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        reminderRepository = FakeReminderRepository()
        categoryRepository = FakeCategoryRepository()
        viewModel = HomeViewModel(reminderRepository, categoryRepository, FakePremiumRepository())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ensures default categories are seeded on init`() = runTest(dispatcher) {
        dispatcher.scheduler.advanceUntilIdle()
        assertTrue(categoryRepository.seedCalled)
    }

    @Test
    fun `quickAdd with a recognizable phrase schedules a reminder`() = runTest(dispatcher) {
        viewModel.quickAdd("Call mom tomorrow")
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, reminderRepository.saved.size)
        assertTrue(reminderRepository.saved.first().dateTime != null)
    }

    @Test
    fun `quickAdd with plain text saves with no schedule`() = runTest(dispatcher) {
        viewModel.quickAdd("Buy milk")
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals("Buy milk", reminderRepository.saved.first().title)
        assertEquals(null, reminderRepository.saved.first().dateTime)
    }

    @Test
    fun `blank quickAdd input is ignored`() = runTest(dispatcher) {
        viewModel.quickAdd("   ")
        dispatcher.scheduler.advanceUntilIdle()

        assertTrue(reminderRepository.saved.isEmpty())
    }

    @Test
    fun `uiState splits reminders into upcoming and completed`() = runTest(dispatcher) {
        viewModel.quickAddWithTime("Task A", System.currentTimeMillis())
        dispatcher.scheduler.advanceUntilIdle()
        val savedId = reminderRepository.saved.first().id
        viewModel.setCompleted(reminderRepository.saved.first(), true)
        dispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.completed.any { it.id == savedId })
        assertTrue(state.upcoming.none { it.id == savedId })
    }
}
