package com.fazziclay.fclayspyandroid;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public long formatVersion = 1;
    public String accessToken = "";
    public String baseUrl = "";
    public Map<String, String> allowedPrograms = new HashMap<>();
    public String blacklist = "";
    public boolean enablePostCurrentSong = true;

    public transient String[] blackListCachedLines = new String[0];
    public String notesToken = "";

    private void recacheBlacklist() {
        blackListCachedLines = blacklist.trim().split("\n");
    }

    public void recacheAll() {
        recacheBlacklist();
    }
}
