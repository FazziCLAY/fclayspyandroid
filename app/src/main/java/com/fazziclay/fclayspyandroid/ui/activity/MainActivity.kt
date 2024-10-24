package com.fazziclay.fclayspyandroid.ui.activity

import android.content.Intent
import android.graphics.Color.parseColor
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fazziclay.fclayspyandroid.App
import com.fazziclay.fclayspyandroid.notes.NotesEditorLogic
import com.fazziclay.fclayspyandroid.ui.theme.FClaySpyThemeTheme

class MainActivity : ComponentActivity() {
    companion object {
        private val COLOR_WARNING = Color(parseColor("#f59703"))
    }
    private lateinit var app: App
    private var notesEditorLogic: NotesEditorLogic? = null
    private var textMutableStatus = mutableStateOf("?")
    private var textMutableStatusColor = mutableStateOf(Color.Red)

    override fun onCreate(savedInstanceState: Bundle?) {
        app = App.getApp(this@MainActivity)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Box(modifier = Modifier
                .imePadding()
                .safeContentPadding()) {
                Content()
                NotesStateIndicator(modifier = Modifier
                    .safeContentPadding()
                    .padding(end = 15.dp)
                    .align(alignment = Alignment.TopEnd))
            }
        }
        actionBar?.hide()
    }

    override fun onDestroy() {
        super.onDestroy()
        notesEditorLogic?.destroy()
    }

    @Composable
    fun NotesStateIndicator(modifier: Modifier) {
        val textStatus by remember {
            textMutableStatus
        }

        val textStatusColor by remember {
            textMutableStatusColor
        }

        Text(text = textStatus, style = TextStyle.Default.copy(color = textStatusColor), modifier = modifier)
    }

    private fun updateStatus() {
        if (notesEditorLogic?.serverEqual() == true) {
            textMutableStatus.value = "A"
            textMutableStatusColor.value = Color.Green
        } else {
            textMutableStatus.value = "W"
            textMutableStatusColor.value = COLOR_WARNING
        }
    }

    @Composable
    fun NotesTextArea() {
        val context = LocalContext.current
        var text by remember { mutableStateOf("") }
        notesEditorLogic = remember {
            NotesEditorLogic(app, {
                text = it!!
            }, {
                updateStatus()
            });
        }

        TextField(
            value = text,
            textStyle = TextStyle.Default.copy(fontSize = 14.sp),
            onValueChange = {
                text = it
                notesEditorLogic!!.textBoxChanged(it)
                updateStatus()

            }, modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 400.dp)
                .padding(1.dp)
                .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(8.dp))
        )
    }

    @Preview
    @Composable
    fun Content() {
        FClaySpyThemeTheme {
            val context = LocalContext.current
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(ScrollState(0), true)
                .padding(5.dp)) {

                NotesTextArea()

                Button(onClick = {
                    startActivity(Intent(this@MainActivity, ConfigActivity::class.java))
                }) {
                    Text("Config")
                }
            }
        }
    }

}

