package com.remindly.app.domain.usecase

import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

data class QuickAddResult(
    val title: String,
    val dateTime: Long?,
)

/**
 * Best-effort natural-language parsing for the quick-add bar. Recognizes a small, reliable
 * set of date/time phrases (today/tomorrow/next week + explicit or day-part times) and strips
 * them from the title. Anything it can't confidently parse is left as plain title text with no
 * schedule — a safe, graceful fallback rather than a wrong guess (the user can add a time later
 * from the edit screen).
 */
object QuickAddParser {

    private val timePattern: Pattern =
        Pattern.compile("\\b(\\d{1,2})(?::(\\d{2}))?\\s*(am|pm|AM|PM)\\b")

    private val dayPartTimes = mapOf(
        "morning" to (8 to 0),
        "afternoon" to (14 to 0),
        "evening" to (19 to 0),
        "tonight" to (21 to 0),
        "night" to (21 to 0),
        "noon" to (12 to 0),
        "midnight" to (0 to 0),
    )

    fun parse(rawInput: String, now: Long = System.currentTimeMillis()): QuickAddResult {
        var text = rawInput.trim()
        if (text.isEmpty()) return QuickAddResult(title = text, dateTime = null)

        val calendar = Calendar.getInstance().apply { timeInMillis = now }
        var dateSet = false
        var timeSet = false

        // Relative date phrases (order matters: check longer phrases first).
        val lower = text.lowercase(Locale.getDefault())
        when {
            lower.contains("next week") -> {
                calendar.add(Calendar.WEEK_OF_YEAR, 1)
                text = text.replace(Regex("(?i)next week"), "").trim()
                dateSet = true
            }
            lower.contains("tomorrow") -> {
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                text = text.replace(Regex("(?i)tomorrow"), "").trim()
                dateSet = true
            }
            lower.contains("today") -> {
                text = text.replace(Regex("(?i)today"), "").trim()
                dateSet = true
            }
        }

        // Explicit "H:MM AM/PM" or "H AM/PM" time.
        val matcher = timePattern.matcher(text)
        if (matcher.find()) {
            var hour = matcher.group(1)?.toIntOrNull() ?: 0
            val minute = matcher.group(2)?.toIntOrNull() ?: 0
            val meridiem = matcher.group(3)?.lowercase(Locale.getDefault())
            if (meridiem == "pm" && hour != 12) hour += 12
            if (meridiem == "am" && hour == 12) hour = 0
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            text = text.substring(0, matcher.start()) + text.substring(matcher.end())
            text = text.replace(Regex("(?i)\\bat\\b"), "").trim()
            dateSet = true
            timeSet = true
        } else {
            for ((phrase, hm) in dayPartTimes) {
                if (lower.contains(phrase)) {
                    calendar.set(Calendar.HOUR_OF_DAY, hm.first)
                    calendar.set(Calendar.MINUTE, hm.second)
                    calendar.set(Calendar.SECOND, 0)
                    calendar.set(Calendar.MILLISECOND, 0)
                    text = text.replace(Regex("(?i)$phrase"), "").trim()
                    dateSet = true
                    timeSet = true
                    break
                }
            }
        }

        if (dateSet && !timeSet) {
            // Date-only phrase (e.g. "tomorrow"): default to 9:00 AM.
            calendar.set(Calendar.HOUR_OF_DAY, 9)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }

        val cleanedTitle = text.replace(Regex("\\s{2,}"), " ").trim().ifEmpty { rawInput.trim() }
        return QuickAddResult(
            title = cleanedTitle,
            dateTime = if (dateSet) calendar.timeInMillis else null,
        )
    }
}
