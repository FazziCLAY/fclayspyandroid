package com.fazziclay.fclayspyandroid;


import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

public class AndroidSong {
    private final String title;
    private final String artist;
    private final String album;
    private final long position;
    private final long duration;
    private final float volume; // [0.0; 1.0]
    private final String playerPackage;
    private final String artUrl;

    public AndroidSong(String title, String artist, String album, long position, long duration, float volume, String playerPackage, @Nullable String artUrl) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.position = position;
        this.duration = duration;
        this.volume = volume;
        this.playerPackage = playerPackage;
        this.artUrl = artUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public long getPosition() {
        return position;
    }

    public long getDuration() {
        return duration;
    }

    public float getVolume() {
        return volume;
    }

    public String getPlayerPackage() {
        return playerPackage;
    }

    public String getArtUrl() {
        return artUrl;
    }

    @NonNull
    @Override
    public String toString() {
        return "AndroidSong{" +
                "title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", position=" + position +
                ", duration=" + duration +
                ", volume=" + volume +
                ", playerPackage='" + playerPackage + '\'' +
                ", artUrl='" + artUrl + '\'' +
                '}';
    }
}
