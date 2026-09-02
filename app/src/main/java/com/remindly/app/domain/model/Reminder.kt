package com.remindly.app.domain.model

data class Reminder(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
    val dateTime: Long?,
    val repeatType: RepeatType = RepeatType.NONE,
    val repeatDays: Set<Int> = emptySet(),
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
) {
    val hasSchedule: Boolean get() = dateTime != null
}
