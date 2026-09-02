package com.remindly.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.remindly.app.RemindlyApp
import com.remindly.app.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val event = GeofencingEvent.fromIntent(intent) ?: return
        if (event.hasError()) {
            return
        }
        if (event.geofenceTransition != Geofence.GEOFENCE_TRANSITION_ENTER) return

        val reminderIds = event.triggeringGeofences?.mapNotNull { it.requestId.toLongOrNull() } ?: return
        if (reminderIds.isEmpty()) return

        val pendingResult = goAsync()
        val app = context.applicationContext as RemindlyApp
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val repository = app.container.reminderRepository
                reminderIds.forEach { id ->
                    val reminder = repository.getById(id) ?: return@forEach
                    if (!reminder.isCompleted) {
                        NotificationHelper.showReminderNotification(context, reminder)
                    }
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
