package com.remindly.app.data.local

import android.os.Build
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.remindly.app.data.local.entity.ReminderEntity
import com.remindly.app.domain.model.RepeatType
import com.remindly.app.domain.model.SoundMode
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
class ReminderDaoTest {

    private lateinit var database: RemindlyDatabase
    private lateinit var dao: com.remindly.app.data.local.dao.ReminderDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindlyDatabase::class.java,
        ).allowMainThreadQueries().build()
        dao = database.reminderDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun sampleReminder(title: String = "Call mom", dateTime: Long? = System.currentTimeMillis()) = ReminderEntity(
        title = title,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis(),
        dateTime = dateTime,
        repeatType = RepeatType.NONE,
        soundMode = SoundMode.RING_ONCE,
    )

    @Test
    fun `insert then read returns the same reminder`() = runBlocking {
        val id = dao.insert(sampleReminder())
        val loaded = dao.getById(id)
        assertEquals("Call mom", loaded?.title)
    }

    @Test
    fun `observeAll emits inserted reminders ordered by dateTime`() = runBlocking {
        dao.insert(sampleReminder("Later", dateTime = 2000L))
        dao.insert(sampleReminder("Sooner", dateTime = 1000L))

        val all = dao.observeAll().first()
        assertEquals(2, all.size)
        assertEquals("Sooner", all.first().title)
    }

    @Test
    fun `delete removes the reminder`() = runBlocking {
        val id = dao.insert(sampleReminder())
        dao.deleteById(id)
        assertNull(dao.getById(id))
    }

    @Test
    fun `search matches title case-insensitively`() = runBlocking {
        dao.insert(sampleReminder("Buy groceries"))
        dao.insert(sampleReminder("Call mom"))

        val results = dao.search("groceries").first()
        assertEquals(1, results.size)
        assertTrue(results.first().title.contains("groceries"))
    }

    @Test
    fun `update persists changed fields`() = runBlocking {
        val id = dao.insert(sampleReminder())
        val entity = dao.getById(id)!!
        dao.update(entity.copy(isCompleted = true, completedAt = 123L))

        val updated = dao.getById(id)
        assertTrue(updated!!.isCompleted)
        assertEquals(123L, updated.completedAt)
    }

    @Test
    fun `reassignCategory moves reminders to fallback category`() = runBlocking {
        val id = dao.insert(sampleReminder().copy(categoryId = 5L))
        dao.reassignCategory(categoryId = 5L, fallbackCategoryId = 1L)

        val moved = dao.getById(id)
        assertEquals(1L, moved?.categoryId)
    }
}
