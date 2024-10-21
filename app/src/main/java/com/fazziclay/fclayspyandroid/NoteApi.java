package com.fazziclay.fclayspyandroid;


import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

public interface NoteApi {
    @GET("notes")
    Call<String> getNotes(@Header("Authorization") @NotNull String auth);

    @PATCH("notes")
    Call<String> setNotes(@Header("Authorization") @NotNull String auth, @Body @NotNull String noteText);
}
