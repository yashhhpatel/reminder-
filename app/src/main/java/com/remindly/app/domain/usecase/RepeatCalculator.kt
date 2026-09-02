package com.remindly.app.domain.usecase

import com.remindly.app.domain.model.RepeatType
import java.util.Calendar

/**
 * Pure calculation of the next occurrence of a repeating reminder, given the epoch millis
 * of the occurrence that just fired. Returns null when the reminder does not repeat.
 */
object RepeatCalculator {

    fun nextOccurrence(
        currentDateTime: Long,
        repeatType: RepeatType,
        repeatDays: Set<Int>,
    ): Long? {
        if (repeatType == RepeatType.NONE) return null

        val calendar = Calendar.getInstance().apply { timeInMillis = currentDateTime }

        return when (repeatType) {
            RepeatType.NONE -> null
            RepeatType.DAILY -> {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                calendar.timeInMillis
            }
            RepeatType.WEEKDAY -> {
                do {
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                } while (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                    calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                )
                calendar.timeInMillis
            }
            RepeatType.WEEKLY -> {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
                calendar.timeInMillis
            }
            RepeatType.MONTHLY -> {
                calendar.add(Calendar.MONTH, 1)
                calendar.timeInMillis
            }
            RepeatType.YEARLY -> {
                calendar.add(Calendar.YEAR, 1)
                calendar.timeInMillis
            }
            RepeatType.CUSTOM -> nextCustomDay(calendar, repeatDays)
        }
    }

    /** repeatDays uses [Calendar.SUNDAY]..[Calendar.SATURDAY] (1..7). */
    private fun nextCustomDay(calendar: Calendar, repeatDays: Set<Int>): Long? {
        if (repeatDays.isEmpty()) return null
        for (i in 1..7) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
            if (calendar.get(Calendar.DAY_OF_WEEK) in repeatDays) {
                return calendar.timeInMillis
            }
        }
        return null
    }
}
