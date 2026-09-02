package com.remindly.app.location

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.remindly.app.domain.model.Reminder
import com.remindly.app.receiver.GeofenceBroadcastReceiver

/**
 * Isolated wrapper around the Play Services Geofencing API for "Add place" reminders.
 * Kept deliberately separate from [com.remindly.app.notification.AlarmScheduler] so
 * time-based and place-based triggers can evolve independently. Callers MUST check
 * ACCESS_FINE_LOCATION (and ACCESS_BACKGROUND_LOCATION for triggers while the app is closed)
 * before calling [addGeofence] — this class does not request permissions itself and never
 * throws when permission is missing, it just fails to register the geofence.
 */
class GeofenceManager(private val context: Context) {

    private val geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)

    @SuppressLint("MissingPermission")
    fun addGeofence(reminder: Reminder) {
        val lat = reminder.latitude ?: return
        val lng = reminder.longitude ?: return

        val geofence = Geofence.Builder()
            .setRequestId(reminder.id.toString())
            .setCircularRegion(lat, lng, reminder.placeRadiusMeters)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build()

        val request = GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()

        runCatching {
            geofencingClient.addGeofences(request, geofencePendingIntent())
        }
    }

    fun removeGeofence(reminderId: Long) {
        runCatching {
            geofencingClient.removeGeofences(listOf(reminderId.toString()))
        }
    }

    private fun geofencePendingIntent(): PendingIntent {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
        )
    }
}
