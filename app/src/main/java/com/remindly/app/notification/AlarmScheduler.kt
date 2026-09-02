package com.remindly.app.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.remindly.app.domain.model.Reminder
import com.remindly.app.domain.repository.ReminderScheduler
import com.remindly.app.receiver.ReminderAlarmReceiver

/**
 * AlarmManager-backed [ReminderScheduler]. Uses exact alarms when the OS grants them
 * (API < 31 always does; API 31+ requires SCHEDULE_EXACT_ALARM / user permission) and falls
 * back to an inexact-but-still-timely alarm otherwise so a denied permission never crashes
 * or silently drops the reminder.
 */
class AlarmScheduler(private val context: Context) : ReminderScheduler {

    private val alarmManager: AlarmManager
        get() = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun schedule(reminder: Reminder) {
        val triggerAt = reminder.snoozeUntil ?: reminder.dateTime ?: return
        if (triggerAt <= 0) return

        val pendingIntent = pendingIntentFor(reminder.id)
        val canScheduleExact = Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            alarmManager.canScheduleExactAlarms()

        runCatching {
            if (canScheduleExact) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            } else {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt, pendingIntent)
            }
        }
    }

    override fun cancel(reminderId: Long) {
        runCatching { alarmManager.cancel(pendingIntentFor(reminderId)) }
    }

    override fun rescheduleAll(reminders: List<Reminder>) {
        reminders.forEach { schedule(it) }
    }

    private fun pendingIntentFor(reminderId: Long): PendingIntent {
        val intent = Intent(context, ReminderAlarmReceiver::class.java).apply {
            putExtra(NotificationHelper.EXTRA_REMINDER_ID, reminderId)
        }
        return PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
