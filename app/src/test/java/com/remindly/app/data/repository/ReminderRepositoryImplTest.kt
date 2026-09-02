package com.remindly.app.data.repository

import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.remindly.app.data.local.RemindlyDatabase
import com.remindly.app.domain.model.Reminder
import com.remindly.app.domain.repository.ReminderScheduler
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

private class FakeScheduler : ReminderScheduler {
    val scheduled = mutableListOf<Long>()
    val cancelled = mutableListOf<Long>()

    override fun schedule(reminder: Reminder) {
        scheduled.add(reminder.id)
    }

    override fun cancel(reminderId: Long) {
        cancelled.add(reminderId)
    }

    override fun rescheduleAll(reminders: List<Reminder>) {
        scheduled.addAll(reminders.map { it.id })
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.TIRAMISU])
class ReminderRepositoryImplTest {

    private lateinit var database: RemindlyDatabase
    private lateinit var scheduler: FakeScheduler
    private lateinit var repository: ReminderRepositoryImpl

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindlyDatabase::class.java,
        ).allowMainThreadQueries().build()
        scheduler = FakeScheduler()
        repository = ReminderRepositoryImpl(database.reminderDao(), scheduler)
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun newReminder(dateTime: Long? = System.currentTimeMillis() + 60_000) = Reminder(
        title = "Test reminder",
        createdAt = 0L,
        updatedAt = 0L,
        dateTime = dateTime,
    )

    @Test
    fun `saving a reminder with a schedule triggers the scheduler`() = runBlocking {
        val id = repository.save(newReminder())
        assertTrue(scheduler.scheduled.contains(id))
    }

    @Test
    fun `saving a reminder without a schedule cancels any existing alarm`() = runBlocking {
        val id = repository.save(newReminder(dateTime = null))
        assertTrue(scheduler.cancelled.contains(id))
    }

    @Test
    fun `deleting a reminder cancels its alarm and removes it`() = runBlocking {
        val id = repository.save(newReminder())
        val reminder = repository.getById(id)!!
        repository.delete(reminder)

        assertTrue(scheduler.cancelled.contains(id))
        assertEquals(null, repository.getById(id))
    }

    @Test
    fun `marking complete cancels the alarm`() = runBlocking {
        val id = repository.save(newReminder())
        repository.setCompleted(id, true)

        val reminder = repository.getById(id)!!
        assertTrue(reminder.isCompleted)
        assertTrue(scheduler.cancelled.contains(id))
    }

    @Test
    fun `snoozing reschedules with the new time`() = runBlocking {
        val id = repository.save(newReminder())
        val snoozeUntil = System.currentTimeMillis() + 600_000
        repository.snooze(id, snoozeUntil)

        val reminder = repository.getById(id)!!
        assertEquals(snoozeUntil, reminder.snoozeUntil)
    }

    @Test
    fun `restoreAllScheduledAlarms reschedules only active reminders`() = runBlocking {
        val activeId = repository.save(newReminder())
        val completedId = repository.save(newReminder())
        repository.setCompleted(completedId, true)
        scheduler.scheduled.clear()

        repository.restoreAllScheduledAlarms()

        assertTrue(scheduler.scheduled.contains(activeId))
        assertFalse(scheduler.scheduled.contains(completedId))
    }

    @Test
    fun `search finds reminders by partial title`() = runBlocking {
        repository.save(newReminder().copy(title = "Buy milk"))
        repository.save(newReminder().copy(title = "Call dentist"))

        val results = repository.search("milk").first()
        assertEquals(1, results.size)
        assertEquals("Buy milk", results.first().title)
    }
}
