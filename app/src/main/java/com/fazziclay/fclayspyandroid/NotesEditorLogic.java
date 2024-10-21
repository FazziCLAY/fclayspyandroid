package com.fazziclay.fclayspyandroid;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

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
    private boolean destroyed;

    public NotesEditorLogic(App app, Consumer<String> consumer) {
        this.app = app;
        this.consumer = consumer;
        this.runnable = () -> {
            interval();
            if (!destroyed) {
                handler.postDelayed(runnable, 1000);
            }
        };

        handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(runnable, 1000);
    }

    public void destroy() {
        destroyed = true;
        handler.removeCallbacks(runnable);
    }

    private void overwriteNoteArea(String text) {
        Log.d(TAG, "overwriteNoteArea text="+text);
        syncFromServerAt = System.currentTimeMillis();
        textFromServer = text;
        consumer.accept(text);
        textBoxText = text;
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
    }

    private void setNoteFromServer() {
        app.noteApi.getNotes(app.config.notesToken).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                String body = response.body();
                if (body != null) {
                    Log.d(TAG, "setNoteFromServer :: onResponse :: " + body);
                    overwriteNoteArea(body);
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    private void sendNoteAreaToServer() {
        Log.d(TAG, "sendNoteAreaToServer");
        app.noteApi.setNotes(app.config.notesToken, textBoxText)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        String text = response.body();
                        if (text != null) {
                            Log.d(TAG, "sendNoteAreaToServer :: onResponse ::" + text);
                            overwriteNoteArea(text);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

    }

    public void textBoxChanged(String s) {
        this.textBoxText = s;
        Log.d(TAG, "note onkeyup");
        noteTypingLatestKeyup = System.currentTimeMillis();
    }
}
