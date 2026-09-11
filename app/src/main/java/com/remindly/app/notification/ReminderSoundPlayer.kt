package com.remindly.app.notification

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.util.Log

/**
 * Plays the looping reminder sound for SoundMode.REPEAT_UNTIL_DISMISSED. A single MediaPlayer
 * is reused app-wide since only one reminder can be actively ringing at a time (posting a new
 * notification always stops whatever was ringing before it).
 */
object ReminderSoundPlayer {
    private const val TAG = "ReminderSoundPlayer"
    private var mediaPlayer: MediaPlayer? = null
    private var activeReminderId: Long = -1L

    fun startLooping(context: Context, reminderId: Long) {
        stop()
        val uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
            ?: RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_NOTIFICATION)
            ?: RingtoneManager.getValidRingtoneUri(context)
        if (uri == null) {
            Log.w(TAG, "No ringtone URI available on this device; reminder $reminderId will ring silently")
            return
        }
        runCatching {
            mediaPlayer = MediaPlayer().apply {
                // Alarm stream: audible through silent mode / alarms-only Do Not Disturb, same
                // reasoning as NotificationHelper.ensureChannel's channel sound.
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build(),
                )
                setDataSource(context, uri)
                isLooping = true
                prepare()
                start()
            }
            activeReminderId = reminderId
            Log.d(TAG, "Started looping reminder sound for $reminderId using $uri")
        }.onFailure { error ->
            Log.e(TAG, "Failed to start looping reminder sound for $reminderId using $uri", error)
            mediaPlayer?.release()
            mediaPlayer = null
            activeReminderId = -1L
        }
    }

    /** Pass a reminderId to only stop if it's the one currently ringing; omit to force-stop. */
    fun stop(reminderId: Long? = null) {
        if (reminderId != null && reminderId != activeReminderId) return
        runCatching { mediaPlayer?.stop() }
        runCatching { mediaPlayer?.release() }
        mediaPlayer = null
        activeReminderId = -1L
    }
}
