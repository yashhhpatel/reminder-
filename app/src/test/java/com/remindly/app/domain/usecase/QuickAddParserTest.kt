package com.remindly.app.domain.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Calendar

class QuickAddParserTest {

    private val fixedNow: Long = Calendar.getInstance().apply {
        set(2026, Calendar.JANUARY, 5, 10, 0, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    @Test
    fun `plain text with no date phrase has no schedule`() {
        val result = QuickAddParser.parse("Buy groceries", now = fixedNow)
        assertEquals("Buy groceries", result.title)
        assertNull(result.dateTime)
    }

    @Test
    fun `tomorrow sets next day at default time`() {
        val result = QuickAddParser.parse("Call mom tomorrow", now = fixedNow)
        assertTrue(result.title.lowercase().contains("call mom"))
        assertNotNull(result.dateTime)

        val cal = Calendar.getInstance().apply { timeInMillis = result.dateTime!! }
        val expectedDay = Calendar.getInstance().apply { timeInMillis = fixedNow; add(Calendar.DAY_OF_YEAR, 1) }
        assertEquals(expectedDay.get(Calendar.DAY_OF_YEAR), cal.get(Calendar.DAY_OF_YEAR))
    }

    @Test
    fun `explicit time is parsed and stripped from title`() {
        val result = QuickAddParser.parse("Meeting at 7 PM", now = fixedNow)
        assertNotNull(result.dateTime)
        val cal = Calendar.getInstance().apply { timeInMillis = result.dateTime!! }
        assertEquals(19, cal.get(Calendar.HOUR_OF_DAY))
        assertTrue(!result.title.contains("7"))
        assertTrue(result.title.lowercase().contains("meeting"))
    }

    @Test
    fun `tomorrow morning combines relative day and day part`() {
        val result = QuickAddParser.parse("Buy groceries tomorrow morning", now = fixedNow)
        assertNotNull(result.dateTime)
        val cal = Calendar.getInstance().apply { timeInMillis = result.dateTime!! }
        assertEquals(8, cal.get(Calendar.HOUR_OF_DAY))
    }

    @Test
    fun `next week advances by seven days`() {
        val result = QuickAddParser.parse("Team sync next week", now = fixedNow)
        assertNotNull(result.dateTime)
        val cal = Calendar.getInstance().apply { timeInMillis = result.dateTime!! }
        val expected = Calendar.getInstance().apply { timeInMillis = fixedNow; add(Calendar.WEEK_OF_YEAR, 1) }
        assertEquals(expected.get(Calendar.DAY_OF_YEAR), cal.get(Calendar.DAY_OF_YEAR))
    }

    @Test
    fun `blank input returns blank title and no schedule`() {
        val result = QuickAddParser.parse("   ", now = fixedNow)
        assertNull(result.dateTime)
    }
}
