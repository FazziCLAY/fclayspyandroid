package com.fazziclay.fclayspyandroid

import android.app.Application
import android.content.Context
import android.util.Log
import com.fazziclay.fclaypersonstatusclient.FClayClient
import com.fazziclay.fclaypersonstatusclient.SongDto
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.nio.file.Files

class App : Application() {
    var ssss: String = "owo"

    private lateinit var netClient: FClayClient
    private lateinit var config: Config
    private var currentSong: AndroidSong? = null

    override fun onCreate() {
        loadConfig()
        super.onCreate()
    }

    /**
     * Check isBlocked and Post song
     */
    fun postSong(song: SongDto?) {
        if (isBlocked(song)) {
            return
        }
        netClient.postSong(song)
    }

    fun getCfg() = config

    fun loadConfig() {
        val dir = getExternalFilesDir("")
        val file = File(dir, "config.json")
        val gson = Gson()
        if (!file.exists()) {
            Files.write(file.toPath(), gson.toJson(Config()).trimMargin().lines())
        }
        config = gson.fromJson(FileReader(file), Config::class.java)
        config.recacheAll()
        netClient = FClayClient(config.baseUrl, config.accessToken)
    }

    fun saveCfg() {
        val dir = getExternalFilesDir("")
        val file = File(dir, "config.json")
        val gson = GsonBuilder().setPrettyPrinting().create()
        config.recacheAll()

        Files.write(file.toPath(), gson.toJson(config).trimMargin().lines())
    }

    fun isBlocked(song: SongDto?): Boolean {
        if (song == null) return false;

        val st = "${song.artist} ${song.album} ${song.title}"

        for (stopWord in config.blackListCachedLines) {
            if (stopWord.trim().isEmpty()) continue

            if (st.contains(stopWord)) {
                Log.d("BlockList", "Blocked by $stopWord word: $song")
                return true
            }
        }

        return song.player == null
    }

    // null for isBlocked => true
    fun packageNameToPlayer(x: String): String? {
        return config.allowedPrograms[x]

//
//        return if (x == "com.aimp.player") {
//            "aimp"
//        } else if (x == "org.mozilla.firefox") {
//            "firefox"
//        } else if (x == "com.spotify.music") {
//            "spotify"
//        } else {
//            if (config.allowedPrograms.contains(x)) {
//                return x
//            } else {
//                null
//            }
//        }
    }

    fun toDto(song: AndroidSong?): SongDto? {
        if (song == null) return null
        currentSong = song
        return SongDto(song.title, song.artist, song.album, packageNameToPlayer(song.playerPackage))
    }

    fun getCurrentSong() = currentSong?.toString()

    companion object {
        fun getApp(context: Context): App = context.applicationContext as App
    }
}
