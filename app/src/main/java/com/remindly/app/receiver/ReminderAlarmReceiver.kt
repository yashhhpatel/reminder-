package com.remindly.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.remindly.app.RemindlyApp
import com.remindly.app.domain.usecase.RepeatCalculator
import com.remindly.app.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReminderAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra(NotificationHelper.EXTRA_REMINDER_ID, -1L)
        if (reminderId <= 0) return

        val pendingResult = goAsync()
        val app = context.applicationContext as RemindlyApp
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = app.container.reminderRepository
                val reminder = repository.getById(reminderId) ?: return@launch
                if (reminder.isCompleted || reminder.isArchived) return@launch

                NotificationHelper.showReminderNotification(context, reminder)

                val nextOccurrence = reminder.dateTime?.let {
                    RepeatCalculator.nextOccurrence(it, reminder.repeatType, reminder.repeatDays)
                }
                if (nextOccurrence != null) {
                    repository.save(reminder.copy(dateTime = nextOccurrence, snoozeUntil = null))
                } else if (reminder.snoozeUntil != null) {
                    repository.save(reminder.copy(snoozeUntil = null))
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
