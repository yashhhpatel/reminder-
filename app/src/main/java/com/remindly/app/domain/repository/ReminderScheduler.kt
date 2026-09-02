package com.remindly.app.domain.repository

import com.remindly.app.domain.model.Reminder

/**
 * Port for turning a persisted [Reminder] into an actual scheduled OS alarm (or geofence).
 * Implemented in the notification/alarm layer using AlarmManager so the domain/data layers
 * stay free of Android framework alarm APIs.
 */
interface ReminderScheduler {
    fun schedule(reminder: Reminder)
    fun cancel(reminderId: Long)
    fun rescheduleAll(reminders: List<Reminder>)
}
