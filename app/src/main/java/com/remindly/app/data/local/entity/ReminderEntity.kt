package com.remindly.app.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.remindly.app.domain.model.Category
import com.remindly.app.domain.model.RepeatType
import com.remindly.app.domain.model.SoundMode

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val dateTime: Long? = null,
    val repeatType: RepeatType = RepeatType.NONE,
    @ColumnInfo(name = "repeatDays")
    val repeatDaysCsv: String = "",
    val notificationEnabled: Boolean = true,
    val soundMode: SoundMode = SoundMode.RING_ONCE,
    val placeEnabled: Boolean = false,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val placeName: String? = null,
    val placeRadiusMeters: Float = 150f,
    val categoryId: Long = Category.DEFAULT_CATEGORY_ID,
    val color: Int? = null,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val isArchived: Boolean = false,
    val isPremium: Boolean = false,
    val snoozeUntil: Long? = null,
)
