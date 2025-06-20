package com.robinhood;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class BlackListManager {
    private static final String BLACKLIST_URL = "https://raw.githubusercontent.com/Minecraft-RobinHood-Project/RobinHoodProject-Blacklist/refs/heads/main/blacklist.json";
    private static final Set<String> blackList = new HashSet<>();
    private static final java.util.Map<String, String> reasonIdMap = new java.util.HashMap<>();
    private static final java.util.Map<String, String> reasonOriginalMap = new java.util.HashMap<>();
    private static final java.util.Map<String, String> banTimestampMap = new java.util.HashMap<>();
    private static File dataFolder = null;

    public static void loadBlackList(Logger logger, LanguageManager languageManager, org.bukkit.configuration.file.FileConfiguration config, java.io.File pluginDataFolder) throws Exception {
        dataFolder = pluginDataFolder;
        blackList.clear();
        reasonIdMap.clear();
        reasonOriginalMap.clear();
        banTimestampMap.clear();
        boolean loaded = false;
        String remoteUrl = BLACKLIST_URL;
        // 处理加速链接
        if (config.getBoolean("github-acceleration-link.enable", false)) {
            String prefix = config.getString("github-acceleration-link.prefix_address", "");
            if (prefix != null && !prefix.isEmpty()) {
                remoteUrl = prefix + BLACKLIST_URL;
            }
        }
        try {
            String jsonStr = fetchRemoteBlackList(remoteUrl);
            parseBlackListJson(jsonStr, languageManager);
            // 只有解析成功才保存本地缓存
            saveBlackListToFile(jsonStr);
            loaded = true;
            logger.info(languageManager.getMessage("loadBlackList_Success"));
        } catch (Exception e) {
            // 下载失败，尝试本地
            try {
                String jsonStr = loadBlackListFromFile(languageManager);
                parseBlackListJson(jsonStr, languageManager);
                loaded = true;
                logger.warning(languageManager.getMessage("loadBlackList_LocalCache"));
            } catch (Exception ex) {
                // 本地也失败
            }
        }
        if (!loaded) throw new Exception(languageManager.getMessage("loadBlackList_FailedManual"));
    }

    private static String fetchRemoteBlackList(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("HTTP Response " + responseCode);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();
        return response.toString();
    }

    private static void saveBlackListToFile(String jsonStr) {
        if (dataFolder == null) dataFolder = new File(".");
        File file = new File(dataFolder, "blacklist_cache.json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(jsonStr);
        } catch (IOException e) {
            // 忽略写入失败
        }
    }

    private static String loadBlackListFromFile(LanguageManager languageManager) throws Exception {
        if (dataFolder == null) dataFolder = new File(".");
        File file = new File(dataFolder, "blacklist_cache.json");
        if (!file.exists()) throw new Exception(languageManager.getMessage("loadBlackList_FailedManual"));
        StringBuilder sb = new StringBuilder();
        try (FileReader fr = new FileReader(file); BufferedReader br = new BufferedReader(fr)) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private static void parseBlackListJson(String jsonStr, LanguageManager languageManager) throws Exception {
        try {
            JSONObject json = new JSONObject(jsonStr);
            JSONArray blacklistArray = json.getJSONArray("blacklist");
            for (int i = 0; i < blacklistArray.length(); i++) {
                JSONObject entry = blacklistArray.getJSONObject(i);
                String uuid = entry.getString("uuid");
                String reasonId = entry.getString("reason_id");
                String reasonOriginal = entry.getString("reason_original");
                String banTimestamp = entry.get("ban_timestamp").toString();
                blackList.add(uuid);
                reasonIdMap.put(uuid, reasonId);
                reasonOriginalMap.put(uuid, reasonOriginal);
                banTimestampMap.put(uuid, banTimestamp);
            }
        } catch (Exception e) {
            throw new Exception(String.format(languageManager.getMessage("loadBlackList_ParseError"), jsonStr));
        }
    }

    public static boolean isBlacklisted(String uuid) {
        return blackList.contains(uuid);
    }

    public static String getReasonId(String uuid) {
        return reasonIdMap.getOrDefault(uuid, "unknown");
    }

    public static String getReasonOriginal(String uuid) {
        return reasonOriginalMap.getOrDefault(uuid, "unknown");
    }

    public static String getBanTimestamp(String uuid) {
        return banTimestampMap.getOrDefault(uuid, "unknown");
    }
} 