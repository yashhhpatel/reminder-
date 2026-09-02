package com.remindly.app.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.remindly.app.MainActivity
import com.remindly.app.R
import com.remindly.app.domain.model.Reminder
import com.remindly.app.domain.model.SoundMode
import com.remindly.app.receiver.NotificationActionReceiver

object NotificationHelper {
    const val CHANNEL_ID_REMINDERS = "reminder_alerts"
    const val EXTRA_REMINDER_ID = "extra_reminder_id"
    const val ACTION_DONE = "com.remindly.app.action.DONE"
    const val ACTION_SNOOZE_MENU = "com.remindly.app.action.SNOOZE_MENU"
    const val ACTION_SNOOZE_10 = "com.remindly.app.action.SNOOZE_10"
    const val ACTION_SNOOZE_30 = "com.remindly.app.action.SNOOZE_30"
    const val ACTION_SNOOZE_60 = "com.remindly.app.action.SNOOZE_60"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        if (manager.getNotificationChannel(CHANNEL_ID_REMINDERS) != null) return
        val channel = NotificationChannel(
            CHANNEL_ID_REMINDERS,
            context.getString(R.string.notification_channel_reminders_name),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = context.getString(R.string.notification_channel_reminders_desc)
            enableVibration(true)
        }
        manager.createNotificationChannel(channel)
    }

    fun showReminderNotification(context: Context, reminder: Reminder) {
        ensureChannel(context)

        val contentIntent = Intent(context, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            putExtra(EXTRA_REMINDER_ID, reminder.id)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val contentPendingIntent = PendingIntent.getActivity(
            context, reminder.id.toInt(), contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val donePendingIntent = actionPendingIntent(context, reminder.id, ACTION_DONE, 1)
        val snoozePendingIntent = actionPendingIntent(context, reminder.id, ACTION_SNOOZE_MENU, 2)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(reminder.title)
            .setContentText(reminder.description ?: context.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .addAction(0, context.getString(R.string.notification_action_done), donePendingIntent)
            .addAction(0, context.getString(R.string.notification_action_snooze), snoozePendingIntent)

        if (reminder.soundMode == SoundMode.SILENT) {
            builder.setSilent(true)
        }

        runCatching {
            NotificationManagerCompat.from(context).notify(reminder.id.toInt(), builder.build())
        }
    }

    /** Replaces the firing notification with one offering the three snooze durations. */
    fun showSnoozeOptions(context: Context, reminderId: Long, title: String) {
        ensureChannel(context)
        val snooze10 = actionPendingIntent(context, reminderId, ACTION_SNOOZE_10, 3)
        val snooze30 = actionPendingIntent(context, reminderId, ACTION_SNOOZE_30, 4)
        val snooze60 = actionPendingIntent(context, reminderId, ACTION_SNOOZE_60, 5)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(context.getString(R.string.notification_action_snooze))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .addAction(0, context.getString(R.string.snooze_10_min), snooze10)
            .addAction(0, context.getString(R.string.snooze_30_min), snooze30)
            .addAction(0, context.getString(R.string.snooze_1_hour), snooze60)

        runCatching {
            NotificationManagerCompat.from(context).notify(reminderId.toInt(), builder.build())
        }
    }

    fun dismiss(context: Context, reminderId: Long) {
        runCatching { NotificationManagerCompat.from(context).cancel(reminderId.toInt()) }
    }

    private fun actionPendingIntent(context: Context, reminderId: Long, action: String, requestCodeSalt: Int): PendingIntent {
        val intent = Intent(context, NotificationActionReceiver::class.java).apply {
            this.action = action
            putExtra(EXTRA_REMINDER_ID, reminderId)
        }
        return PendingIntent.getBroadcast(
            context,
            (reminderId.toInt() * 10) + requestCodeSalt,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
