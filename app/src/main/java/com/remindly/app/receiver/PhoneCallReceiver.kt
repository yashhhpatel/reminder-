package com.remindly.app.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.remindly.app.MainActivity
import com.remindly.app.R
import com.remindly.app.data.datastore.SettingsDataStore
import com.remindly.app.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * After-call reminder architecture. Uses only the call *state* broadcast (READ_PHONE_STATE) —
 * it never touches call audio, never records anything, and never reads call content. It just
 * detects "a call that was ringing/off-hook just went idle" (i.e. a call ended) and offers the
 * user a one-tap shortcut into New Reminder. This is the closest legal, system-compliant
 * approximation of "reminder after a call" on modern Android: there is no public API to detect
 * *which* call ended or its number without additional runtime permissions the user may deny,
 * so this stays a lightweight, permission-light nudge rather than an intrusive call-log reader.
 */
class PhoneCallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != TelephonyManager.ACTION_PHONE_STATE_CHANGED) return
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE) ?: return

        when (state) {
            TelephonyManager.EXTRA_STATE_RINGING, TelephonyManager.EXTRA_STATE_OFFHOOK -> {
                wasInCall = true
            }
            TelephonyManager.EXTRA_STATE_IDLE -> {
                if (wasInCall) {
                    wasInCall = false
                    val pendingResult = goAsync()
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val settings = SettingsDataStore(context.applicationContext)
                            if (settings.presetAfterCall.first()) {
                                showAfterCallPrompt(context)
                            }
                        } finally {
                            pendingResult.finish()
                        }
                    }
                }
            }
        }
    }

    private fun showAfterCallPrompt(context: Context) {
        NotificationHelper.ensureChannel(context)
        val intent = Intent(context, MainActivity::class.java).apply {
            action = ACTION_AFTER_CALL
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(
            context, AFTER_CALL_NOTIFICATION_ID, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        val notification = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID_REMINDERS)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(context.getString(R.string.settings_after_call))
            .setContentText(context.getString(R.string.settings_after_call_desc))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        runCatching {
            NotificationManagerCompat.from(context).notify(AFTER_CALL_NOTIFICATION_ID, notification)
        }
    }

    companion object {
        const val ACTION_AFTER_CALL = "com.remindly.app.action.AFTER_CALL"
        private const val AFTER_CALL_NOTIFICATION_ID = 90001

        @Volatile
        private var wasInCall = false
    }
}
