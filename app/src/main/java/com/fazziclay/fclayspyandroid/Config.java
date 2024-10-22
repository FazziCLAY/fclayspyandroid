package com.fazziclay.fclayspyandroid;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public long formatVersion = 1;

    public String baseUrl = "";

    // Post dong
    public boolean enablePostCurrentSong = true;
    public String accessToken = "";
    public String personName = "fazziclay";
    public Map<String, String> allowedPrograms = new HashMap<>();
    public String blacklist = "";
    public transient String[] blackListCachedLines = new String[0];

    // notes
    public String notesToken = "";

    private void recacheBlacklist() {
        blackListCachedLines = blacklist.trim().split("\n");
    }

    public void recacheAll() {
        recacheBlacklist();
    }
}
