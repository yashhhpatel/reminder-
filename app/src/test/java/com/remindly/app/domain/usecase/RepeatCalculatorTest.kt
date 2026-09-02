package com.remindly.app.domain.usecase

import com.remindly.app.domain.model.RepeatType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Calendar

class RepeatCalculatorTest {

    private fun calendarOf(year: Int, month: Int, day: Int, hour: Int = 9, minute: Int = 0): Calendar =
        Calendar.getInstance().apply {
            set(year, month, day, hour, minute, 0)
            set(Calendar.MILLISECOND, 0)
        }

    @Test
    fun `none never repeats`() {
        val now = calendarOf(2026, Calendar.JANUARY, 1).timeInMillis
        assertNull(RepeatCalculator.nextOccurrence(now, RepeatType.NONE, emptySet()))
    }

    @Test
    fun `daily advances by exactly one day`() {
        val start = calendarOf(2026, Calendar.JANUARY, 1)
        val next = RepeatCalculator.nextOccurrence(start.timeInMillis, RepeatType.DAILY, emptySet())
        val expected = calendarOf(2026, Calendar.JANUARY, 2)
        assertEquals(expected.timeInMillis, next)
    }

    @Test
    fun `weekly advances by seven days`() {
        val start = calendarOf(2026, Calendar.MARCH, 3)
        val next = RepeatCalculator.nextOccurrence(start.timeInMillis, RepeatType.WEEKLY, emptySet())
        val expected = calendarOf(2026, Calendar.MARCH, 10)
        assertEquals(expected.timeInMillis, next)
    }

    @Test
    fun `monthly preserves day of month`() {
        val start = calendarOf(2026, Calendar.JANUARY, 15)
        val next = RepeatCalculator.nextOccurrence(start.timeInMillis, RepeatType.MONTHLY, emptySet())
        val expected = calendarOf(2026, Calendar.FEBRUARY, 15)
        assertEquals(expected.timeInMillis, next)
    }

    @Test
    fun `yearly advances by one year`() {
        val start = calendarOf(2026, Calendar.JANUARY, 15)
        val next = RepeatCalculator.nextOccurrence(start.timeInMillis, RepeatType.YEARLY, emptySet())
        val expected = calendarOf(2027, Calendar.JANUARY, 15)
        assertEquals(expected.timeInMillis, next)
    }

    @Test
    fun `weekday skips saturday and sunday`() {
        // Friday Jan 2, 2026 -> next weekday occurrence should be Monday Jan 5, 2026.
        val friday = calendarOf(2026, Calendar.JANUARY, 2)
        assertEquals(Calendar.FRIDAY, friday.get(Calendar.DAY_OF_WEEK))

        val next = RepeatCalculator.nextOccurrence(friday.timeInMillis, RepeatType.WEEKDAY, emptySet())
        val nextCal = Calendar.getInstance().apply { timeInMillis = next!! }
        assertEquals(Calendar.MONDAY, nextCal.get(Calendar.DAY_OF_WEEK))
    }

    @Test
    fun `custom picks the nearest configured day`() {
        // Start on a Monday, repeat on Wednesday + Friday -> next should be Wednesday (2 days later).
        val monday = calendarOf(2026, Calendar.JANUARY, 5)
        assertEquals(Calendar.MONDAY, monday.get(Calendar.DAY_OF_WEEK))

        val next = RepeatCalculator.nextOccurrence(
            monday.timeInMillis,
            RepeatType.CUSTOM,
            setOf(Calendar.WEDNESDAY, Calendar.FRIDAY),
        )
        val nextCal = Calendar.getInstance().apply { timeInMillis = next!! }
        assertEquals(Calendar.WEDNESDAY, nextCal.get(Calendar.DAY_OF_WEEK))
    }

    @Test
    fun `custom with no days returns null`() {
        val start = calendarOf(2026, Calendar.JANUARY, 5)
        assertNull(RepeatCalculator.nextOccurrence(start.timeInMillis, RepeatType.CUSTOM, emptySet()))
    }
}
