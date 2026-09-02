package com.remindly.app.domain.repository

import com.remindly.app.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun observeAll(): Flow<List<Reminder>>
    fun observeById(id: Long): Flow<Reminder?>
    fun search(query: String): Flow<List<Reminder>>
    fun observeByCategory(categoryId: Long): Flow<List<Reminder>>
    fun countByCategory(categoryId: Long): Flow<Int>
    suspend fun getById(id: Long): Reminder?
    suspend fun save(reminder: Reminder): Long
    suspend fun delete(reminder: Reminder)
    suspend fun setCompleted(id: Long, completed: Boolean)
    suspend fun snooze(id: Long, untilEpochMillis: Long)
    suspend fun restoreAllScheduledAlarms()
}
