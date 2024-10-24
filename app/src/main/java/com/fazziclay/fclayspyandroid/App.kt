package com.fazziclay.fclayspyandroid

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.fazziclay.fclaypersonstatusclient.FClayClient
import com.fazziclay.fclayspyandroid.notes.api.NoteApi
import com.fazziclay.fclaysystem.personstatus.api.dto.PlaybackDto
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.nio.file.Files

class App : Application() {
    public var netClient: FClayClient? = null
    public var noteApi: NoteApi? = null
    public lateinit var config: Config
    private var currentSong: AndroidSong? = null

    override fun onCreate() {
        loadConfig()
        super.onCreate()
    }

    fun delete() {
        if (config.accessToken.isEmpty()) return
        netClient?.delete()
    }

    fun setCurrentSong(song: AndroidSong?) {
        this.currentSong = song
        postSong(toDto(song))
    }

    /**
     * Check isBlocked and Post song
     */
    private fun postSong(song: PlaybackDto?) {
        if (!config.enablePostCurrentSong || isBlocked(song)) {
            delete()
            return
        }

        if (config.accessToken.isEmpty()) return
        netClient?.postSong(song)
    }

    fun getCfg() = config

    private fun loadConfig() {
        val dir = getExternalFilesDir("")
        val file = File(dir, "config.json")
        val gson = Gson()
        if (!file.exists()) {
            Files.write(file.toPath(), gson.toJson(Config()).trimMargin().lines())
        }
        config = gson.fromJson(FileReader(file), Config::class.java)
        config.recacheAll()
        try {
            netClient = FClayClient(config.baseUrl, config.personName, config.accessToken)
            noteApi = netClient!!.retrofit.create(NoteApi::class.java)

        } catch (e: Exception) {
            Toast.makeText(this, "Err: $e", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveCfg() {
        val dir = getExternalFilesDir("")
        val file = File(dir, "config.json")
        val gson = GsonBuilder().setPrettyPrinting().create()
        config.recacheAll()

        Files.write(file.toPath(), gson.toJson(config).trimMargin().lines())
    }

    private fun isBlocked(song: PlaybackDto?): Boolean {
        if (song == null) return false;

        val st = "${song.artist} ${song.album} ${song.title}"

        for (stopWord in config.blackListCachedLines) {
            if (stopWord.trim().isEmpty()) continue

            if (st.contains(stopWord, ignoreCase = true)) {
                Log.d("BlockList", "Blocked by $stopWord word: $song")
                return true
            }
        }

        return song.player == null
    }

    // null for isBlocked => true
    private fun packageNameToPlayer(x: String): String? {
        return config.allowedPrograms[x]
    }

    private fun toDto(song: AndroidSong?): PlaybackDto? {
        if (song == null) return null
        return PlaybackDto(song.title, song.artist, song.album, packageNameToPlayer(song.playerPackage), song.artUrl, song.position, song.duration, song.volume)
    }
    
    companion object {
        val DEBUG: Boolean = false

        fun getApp(context: Context): App = context.applicationContext as App
    }
}
