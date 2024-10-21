package com.fazziclay.fclayspyandroid


import android.content.ComponentName
import android.media.AudioAttributes.USAGE_MEDIA
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.content.getSystemService

class NotiService : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        val app = App.getApp(this)

        val m = getSystemService<MediaSessionManager>()!!
        val component = ComponentName(this, NotiService::class.java)
        val sessions = m.getActiveSessions(component)
        val song = getAndroidSong(sessions)

        app.setCurrentSong(song)
    }

    private fun getAndroidSong(s: List<MediaController?>): AndroidSong? {
        val sb = (if (App.DEBUG) StringBuilder() else null)

        for (c: MediaController? in s) {
            if (c == null || c.metadata == null || c.playbackState == null) continue

            val metadata = c.metadata!!
            val playerPackage = c.packageName!!
            val playbackState = c.playbackState!!
            val playbackInfo = c.playbackInfo!!
            val title = metadata.getString(MediaMetadata.METADATA_KEY_TITLE)
            val artist = metadata.getString(MediaMetadata.METADATA_KEY_ARTIST)
            val album = metadata.getString(MediaMetadata.METADATA_KEY_ALBUM)
            val position = playbackState.position
            val duration = metadata.getLong(MediaMetadata.METADATA_KEY_DURATION)
            val artHttpUrl = if (metadata.containsKey(KEY_SPOTIFY_ART_HTTP_URL)) metadata.getString(KEY_SPOTIFY_ART_HTTP_URL) else null
            val volume: Float = playbackInfo.currentVolume.toFloat() / playbackInfo.maxVolume.toFloat()

            // if playing and if media
            if (playbackState.state == PlaybackState.STATE_PLAYING && playbackInfo.audioAttributes.usage == USAGE_MEDIA) {
                return AndroidSong(title, artist, album, position, duration, volume, playerPackage, artHttpUrl)
            }

            if (App.DEBUG && sb != null) {
                sb.append("playbackInfo=").append(playbackInfo).append("\n")
                sb.append("playbackState=").append(playbackState).append("\n")
                sb.append("packageName=").append(playerPackage).append("\n")

                for (s1 in c.metadata!!.keySet()) {
                    try {
                        var sggg =
                            "[$s1] - ${c.metadata!!.getString(s1)} - ${c!!.metadata!!.getLong(s1)}"
                        sb.append(sggg).append("\n")
                    } catch (e: Exception) {
                    }
                }
                sb.append("=======")
            }
        }

        if (App.DEBUG) {
            Log.d("MediaSessions", sb.toString())
        }
        return null
    }

    companion object {
        const val KEY_SPOTIFY_ART_HTTP_URL = "com.spotify.music.extra.ART_HTTPS_URI"
    }
}