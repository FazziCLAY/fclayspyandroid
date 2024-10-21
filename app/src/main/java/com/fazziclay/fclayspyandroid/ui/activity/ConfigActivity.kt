package com.fazziclay.fclayspyandroid.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.waterfallPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.fazziclay.fclayspyandroid.App
import com.fazziclay.fclayspyandroid.Config
import com.fazziclay.fclayspyandroid.ui.theme.FClaySpyThemeTheme
import com.google.gson.GsonBuilder

class ConfigActivity : ComponentActivity() {
    private lateinit var app: App
    private lateinit var cfg: Config
    private val gson = GsonBuilder().setPrettyPrinting().create()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = App.getApp(this@ConfigActivity)
        cfg = app.getCfg()

        actionBar?.hide()
        enableEdgeToEdge()
        setContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        FClaySpyThemeTheme {
            val context = LocalContext.current
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0), true)
                .safeContentPadding()
                .imePadding()
                .waterfallPadding()
                .padding(5.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        "Posting",
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                    )
                    var enableCheck by remember { mutableStateOf(cfg.enablePostCurrentSong) }
                    Checkbox(checked = enableCheck, onCheckedChange = {
                        enableCheck = it
                        cfg.enablePostCurrentSong = it
                        app.saveCfg()
                        app.delete()
                    })
                }

                var accessTokenNotes by remember { mutableStateOf(cfg.notesToken) }
                OutlinedTextField(
                    value = accessTokenNotes,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(ScrollState(0), true),
                    onValueChange = {
                        accessTokenNotes = it
                        cfg.notesToken = it
                        app.saveCfg()
                    },
                    label = { Text("accessToken for notes") }
                )

                var baseUrlText by remember { mutableStateOf(cfg.baseUrl) }
                OutlinedTextField(
                    value = baseUrlText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(ScrollState(0), true),
                    onValueChange = {
                        baseUrlText = it
                        cfg.baseUrl = it
                        app.saveCfg()
                    },
                    label = { Text("baseUrl") }
                )

                var accessToken by remember { mutableStateOf(cfg.accessToken) }
                OutlinedTextField(
                    value = accessToken,
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(ScrollState(0), true),
                    onValueChange = {
                        accessToken = it
                        cfg.accessToken = it
                        app.saveCfg()
                    },
                    label = { Text("accessToken") }
                )

                var allowedPrograms by remember { mutableStateOf(gson.toJson(cfg.allowedPrograms)) }
                var error by remember { mutableStateOf(false) }
                OutlinedTextField(
                    value = allowedPrograms,
                    isError = error,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChange = {
                        allowedPrograms = it
                        try {
                            cfg.allowedPrograms = gson.fromJson(it, Map::class.java) as MutableMap<String, String>?
                            app.saveCfg()
                            error = false
                            allowedPrograms = gson.toJson(cfg.allowedPrograms)

                        } catch (e: Exception) {
                            error = true
                        }
                    },
                    label = { Text("allowedPrograms") }
                )

                var blackList by remember { mutableStateOf(cfg.blacklist) }
                OutlinedTextField(
                    value = blackList,
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChange = {
                        blackList = it
                        cfg.blacklist = it
                        app.saveCfg()
                    },
                    label = { Text("blackList") }
                )




            }
        }
    }

}