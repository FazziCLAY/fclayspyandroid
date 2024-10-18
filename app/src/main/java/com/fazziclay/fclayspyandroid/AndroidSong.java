package com.fazziclay.fclayspyandroid;


public class AndroidSong {
    private final String title;
    private final String artist;
    private final String album;
    private final long position;
    private final long duration;
    private final float volume;
    private final String playerPackage;

    public AndroidSong(String title, String artist, String album, long position, long duration, float volume, String playerPackage) {
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.position = position;
        this.duration = duration;
        this.volume = volume;
        this.playerPackage = playerPackage;
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
                '}';
    }
}
