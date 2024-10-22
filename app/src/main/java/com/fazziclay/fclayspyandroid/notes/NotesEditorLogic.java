package com.fazziclay.fclayspyandroid.notes;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.fazziclay.fclayspyandroid.App;
import com.fazziclay.fclayspyandroid.notes.api.NoteDto;

import java.util.Objects;
import java.util.function.Consumer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesEditorLogic {
    private final String TAG = "NotesEditorLogic" + "@" + Integer.toHexString(hashCode());
    private final App app;


    private String textBoxText;
    private long noteTypingLatestKeyup;

    private String textFromServer;
    private long syncFromServerAt;

    private Runnable runnable;
    private Handler handler;
    private final Consumer<String> consumer;
    private final Runnable tickRunnable;
    private boolean destroyed;

    public NotesEditorLogic(App app, Consumer<String> consumer, Runnable tickRunnable) {
        this.app = app;
        this.consumer = consumer;
        this.tickRunnable = tickRunnable;
        this.runnable = () -> {
            interval();
            if (!destroyed) {
                handler.postDelayed(runnable, 500);
            }
        };

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, 500);
    }

    public void destroy() {
        destroyed = true;
        handler.removeCallbacks(runnable);
    }

    private void overwriteNoteArea(NoteDto note) {
        String text = note.getText();
        Log.d(TAG, "overwriteNoteArea text="+text);
        if (text == null) {
            Log.w(TAG, "TEXT IS NULL");
            return;
        }
        if (noteTypingLatestKeyup <= syncFromServerAt && !Objects.equals(textFromServer, text)) {
            textBoxText = text;
            consumer.accept(text);
        }
        textFromServer = text;
        syncFromServerAt = System.currentTimeMillis();
    }

    public boolean serverEqual() {
        return Objects.equals(textFromServer, textBoxText);
    }

    private void interval() {
        if (System.currentTimeMillis() - noteTypingLatestKeyup > 1000 && syncFromServerAt <= noteTypingLatestKeyup) {
            Log.d(TAG, "typing > sync && elapsed > 1000");
            sendNoteAreaToServer();
        }

        if (noteTypingLatestKeyup <= syncFromServerAt) {
            Log.d(TAG, "typing =< sync");
            setNoteFromServer();
        }

        if (tickRunnable != null) {
            tickRunnable.run();
        }
    }

    private void setNoteFromServer() {
        if (app.getNoteApi() == null) return;
        app.getNoteApi().getNotes(app.config.notesToken).enqueue(new Callback<NoteDto>() {
            @Override
            public void onResponse(@NonNull Call<NoteDto> call, @NonNull Response<NoteDto> response) {
                NoteDto body = response.body();
                if (body != null && response.code() == 200) {
                    Log.d(TAG, "setNoteFromServer :: onResponse :: " + body);
                    overwriteNoteArea(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<NoteDto> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void sendNoteAreaToServer() {
        Log.d(TAG, "sendNoteAreaToServer");
        if (textBoxText == null || textBoxText.length() < 4) {
            Log.d(TAG, "sendNoteAreaToServer TEXT == NULL OR LEN < 4!!!!!!!!!! RETURN");
            return;
        }

        if (app.getNoteApi() == null) return;
        app.getNoteApi().setNotes(app.config.notesToken, new NoteDto(textBoxText, null))
                .enqueue(new Callback<NoteDto>() {
                    @Override
                    public void onResponse(Call<NoteDto> call, Response<NoteDto> response) {
                        NoteDto note = response.body();
                        if (note != null && response.code() == 200) {
                            Log.d(TAG, "sendNoteAreaToServer :: onResponse ::" + note);
                            overwriteNoteArea(note);
                        }
                    }

                    @Override
                    public void onFailure(Call<NoteDto> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    public void textBoxChanged(String s) {
        Log.d(TAG, "note onkeyup");
        if (s == null) {
            Log.w(TAG, "note onkeyup: NULL; return");
            return;
        }
        this.textBoxText = s;
        noteTypingLatestKeyup = System.currentTimeMillis();
    }
}
