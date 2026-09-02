package com.remindly.app.data.local.entity

import com.remindly.app.data.local.converter.csvToIntSet
import com.remindly.app.data.local.converter.toCsv
import com.remindly.app.domain.model.Category
import com.remindly.app.domain.model.Reminder

fun ReminderEntity.toDomain(): Reminder = Reminder(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    dateTime = dateTime,
    repeatType = repeatType,
    repeatDays = repeatDaysCsv.csvToIntSet(),
    notificationEnabled = notificationEnabled,
    soundMode = soundMode,
    placeEnabled = placeEnabled,
    latitude = latitude,
    longitude = longitude,
    placeName = placeName,
    placeRadiusMeters = placeRadiusMeters,
    categoryId = categoryId,
    color = color,
    isCompleted = isCompleted,
    completedAt = completedAt,
    isArchived = isArchived,
    isPremium = isPremium,
    snoozeUntil = snoozeUntil,
)

fun Reminder.toEntity(): ReminderEntity = ReminderEntity(
    id = id,
    title = title,
    description = description,
    createdAt = createdAt,
    updatedAt = updatedAt,
    dateTime = dateTime,
    repeatType = repeatType,
    repeatDaysCsv = repeatDays.toCsv(),
    notificationEnabled = notificationEnabled,
    soundMode = soundMode,
    placeEnabled = placeEnabled,
    latitude = latitude,
    longitude = longitude,
    placeName = placeName,
    placeRadiusMeters = placeRadiusMeters,
    categoryId = categoryId,
    color = color,
    isCompleted = isCompleted,
    completedAt = completedAt,
    isArchived = isArchived,
    isPremium = isPremium,
    snoozeUntil = snoozeUntil,
)

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    name = name,
    colorArgb = colorArgb,
    isBuiltIn = isBuiltIn,
    sortOrder = sortOrder,
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    colorArgb = colorArgb,
    isBuiltIn = isBuiltIn,
    sortOrder = sortOrder,
)
