package com.remindly.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.remindly.app.RemindlyApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Restores every scheduled alarm after a reboot, an app update, or a system time/timezone
 * change — AlarmManager alarms do not survive any of these, and Room is the source of truth.
 */
class BootRestoreReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult = goAsync()
        val app = context.applicationContext as RemindlyApp
        CoroutineScope(Dispatchers.IO).launch {
            try {
                app.container.reminderRepository.restoreAllScheduledAlarms()
            } finally {
                pendingResult.finish()
            }
        }
    }
}
