package com.remindly.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.remindly.app.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders WHERE isArchived = 0 ORDER BY (dateTime IS NULL), dateTime ASC")
    fun observeAll(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE id = :id")
    fun observeById(id: Long): Flow<ReminderEntity?>

    @Query("SELECT * FROM reminders WHERE id = :id")
    suspend fun getById(id: Long): ReminderEntity?

    @Query(
        "SELECT * FROM reminders WHERE isArchived = 0 AND (" +
            "title LIKE '%' || :query || '%' OR " +
            "description LIKE '%' || :query || '%' OR " +
            "placeName LIKE '%' || :query || '%'" +
            ") ORDER BY (dateTime IS NULL), dateTime ASC"
    )
    fun search(query: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminders WHERE categoryId = :categoryId AND isArchived = 0")
    fun observeByCategory(categoryId: Long): Flow<List<ReminderEntity>>

    @Query("SELECT COUNT(*) FROM reminders WHERE categoryId = :categoryId AND isArchived = 0")
    fun countByCategory(categoryId: Long): Flow<Int>

    @Query("SELECT * FROM reminders WHERE dateTime IS NOT NULL AND isCompleted = 0 AND isArchived = 0")
    suspend fun getAllScheduledActive(): List<ReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminder: ReminderEntity): Long

    @Update
    suspend fun update(reminder: ReminderEntity)

    @Delete
    suspend fun delete(reminder: ReminderEntity)

    @Query("DELETE FROM reminders WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("UPDATE reminders SET categoryId = :fallbackCategoryId WHERE categoryId = :categoryId")
    suspend fun reassignCategory(categoryId: Long, fallbackCategoryId: Long)
}
