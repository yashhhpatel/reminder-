package com.remindly.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.remindly.app.RemindlyApp
import com.remindly.app.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class NotificationActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val reminderId = intent.getLongExtra(NotificationHelper.EXTRA_REMINDER_ID, -1L)
        if (reminderId <= 0) return

        val pendingResult = goAsync()
        val app = context.applicationContext as RemindlyApp
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = app.container.reminderRepository
                when (intent.action) {
                    NotificationHelper.ACTION_DONE -> {
                        repository.setCompleted(reminderId, true)
                        NotificationHelper.dismiss(context, reminderId)
                    }
                    NotificationHelper.ACTION_SNOOZE_MENU -> {
                        val reminder = repository.getById(reminderId)
                        NotificationHelper.showSnoozeOptions(context, reminderId, reminder?.title.orEmpty())
                    }
                    NotificationHelper.ACTION_SNOOZE_10 -> snooze(repository, reminderId, 10, context)
                    NotificationHelper.ACTION_SNOOZE_30 -> snooze(repository, reminderId, 30, context)
                    NotificationHelper.ACTION_SNOOZE_60 -> snooze(repository, reminderId, 60, context)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }

    private suspend fun snooze(
        repository: com.remindly.app.domain.repository.ReminderRepository,
        reminderId: Long,
        minutes: Long,
        context: Context,
    ) {
        val until = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(minutes)
        repository.snooze(reminderId, until)
        NotificationHelper.dismiss(context, reminderId)
    }
}
