package com.remindly.app.data.repository

import com.remindly.app.data.local.dao.ReminderDao
import com.remindly.app.data.local.entity.toDomain
import com.remindly.app.data.local.entity.toEntity
import com.remindly.app.domain.model.Reminder
import com.remindly.app.domain.repository.ReminderRepository
import com.remindly.app.domain.repository.ReminderScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReminderRepositoryImpl(
    private val dao: ReminderDao,
    private val scheduler: ReminderScheduler,
) : ReminderRepository {

    override fun observeAll(): Flow<List<Reminder>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeById(id: Long): Flow<Reminder?> =
        dao.observeById(id).map { it?.toDomain() }

    override fun search(query: String): Flow<List<Reminder>> =
        dao.search(query).map { list -> list.map { it.toDomain() } }

    override fun observeByCategory(categoryId: Long): Flow<List<Reminder>> =
        dao.observeByCategory(categoryId).map { list -> list.map { it.toDomain() } }

    override fun countByCategory(categoryId: Long): Flow<Int> = dao.countByCategory(categoryId)

    override suspend fun getById(id: Long): Reminder? = dao.getById(id)?.toDomain()

    override suspend fun save(reminder: Reminder): Long {
        val now = System.currentTimeMillis()
        val toSave = reminder.copy(updatedAt = now, createdAt = if (reminder.id == 0L) now else reminder.createdAt)
        val id = dao.insert(toSave.toEntity())
        val saved = toSave.copy(id = if (toSave.id == 0L) id else toSave.id)
        if (saved.hasSchedule && !saved.isCompleted && saved.notificationEnabled) {
            scheduler.schedule(saved)
        } else {
            scheduler.cancel(saved.id)
        }
        return saved.id
    }

    override suspend fun delete(reminder: Reminder) {
        dao.delete(reminder.toEntity())
        scheduler.cancel(reminder.id)
    }

    override suspend fun setCompleted(id: Long, completed: Boolean) {
        val entity = dao.getById(id) ?: return
        val now = System.currentTimeMillis()
        val updated = entity.copy(
            isCompleted = completed,
            completedAt = if (completed) now else null,
            updatedAt = now,
        )
        dao.update(updated)
        if (completed) scheduler.cancel(id) else if (updated.dateTime != null) scheduler.schedule(updated.toDomain())
    }

    override suspend fun snooze(id: Long, untilEpochMillis: Long) {
        val entity = dao.getById(id) ?: return
        val updated = entity.copy(snoozeUntil = untilEpochMillis, updatedAt = System.currentTimeMillis())
        dao.update(updated)
        scheduler.schedule(updated.toDomain().copy(dateTime = untilEpochMillis))
    }

    override suspend fun restoreAllScheduledAlarms() {
        scheduler.rescheduleAll(dao.getAllScheduledActive().map { it.toDomain() })
    }
}
