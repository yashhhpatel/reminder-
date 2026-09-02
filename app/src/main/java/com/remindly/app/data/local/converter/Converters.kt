package com.remindly.app.data.local.converter

import androidx.room.TypeConverter
import com.remindly.app.domain.model.RepeatType
import com.remindly.app.domain.model.SoundMode

class Converters {
    @TypeConverter
    fun fromRepeatType(value: RepeatType): String = value.name

    @TypeConverter
    fun toRepeatType(value: String): RepeatType =
        runCatching { RepeatType.valueOf(value) }.getOrDefault(RepeatType.NONE)

    @TypeConverter
    fun fromSoundMode(value: SoundMode): String = value.name

    @TypeConverter
    fun toSoundMode(value: String): SoundMode =
        runCatching { SoundMode.valueOf(value) }.getOrDefault(SoundMode.RING_ONCE)
}

fun Set<Int>.toCsv(): String = joinToString(",")

fun String.csvToIntSet(): Set<Int> =
    if (isBlank()) emptySet() else split(",").mapNotNull { it.trim().toIntOrNull() }.toSet()
