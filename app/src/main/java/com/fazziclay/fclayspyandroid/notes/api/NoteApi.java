package com.fazziclay.fclayspyandroid.notes.api;


import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

public interface NoteApi {
    @GET("notes")
    Call<NoteDto> getNotes(@Header("Authorization") @NotNull String auth);

    @PATCH("notes")
    Call<NoteDto> setNotes(@Header("Authorization") @NotNull String auth, @Body @NotNull NoteDto noteText);
}
