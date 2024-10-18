package com.fazziclay.fclayspyandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fazziclay.fclayspyandroid.ui.theme.FClaySpyThemeTheme

class MainActivity : ComponentActivity() {
    private lateinit var app: App

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = App.getApp(this@MainActivity)

        enableEdgeToEdge()
        setContent {
            Content()
        }
        actionBar?.hide()
    }

    @Preview
    @Composable
    fun Content() {
        FClaySpyThemeTheme {
            val context = LocalContext.current
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)) {

                Button(onClick = {
                    startActivity(Intent(this@MainActivity, ConfigActivity::class.java))
                }, ) {
                    Text("Config")
                }

                var remaining by remember { mutableStateOf("uwu") }

                val handler = android.os.Handler()
                var rr: Runnable? = null
                val r = Runnable {
                    remaining = "${app.getCurrentSong()}"
                    handler.postDelayed(rr!!, 500)
                }
                rr = r
                handler.post(rr)

                Text(text = remaining)
            }
        }
    }

}

