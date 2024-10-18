package com.fazziclay.fclayspyandroid;

import java.util.HashMap;
import java.util.Map;

public class Config {
    public String accessToken = "";
    public String baseUrl = "";
    public Map<String, String> allowedPrograms = new HashMap<>();
    public String blacklist = "";

    public transient String[] blackListCachedLines = new String[0];

    private void recacheBlacklist() {
        blackListCachedLines = blacklist.trim().split("\n");
    }

    public void recacheAll() {
        recacheBlacklist();
    }
}
