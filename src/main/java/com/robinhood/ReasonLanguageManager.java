package com.robinhood;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ReasonLanguageManager {
    private final Map<String, String> reasonMap = new HashMap<>();
    private final String language;

    public ReasonLanguageManager(FileConfiguration config) {
        // 有效的语言列表
        Set<String> validLanguages = new HashSet<>(Arrays.asList("zh_hans", "zh_hant", "en_us", "ja_jp"));

        // 读取语言配置
        String lang = config.getString("language", "zh_hans");

        // 判断合法性，不合法则取默认值
        if (!validLanguages.contains(lang.toLowerCase())) {
            this.language = "zh_hans";
        } else {
            this.language = lang;
        }

        loadReasons();
    }

    private void loadReasons() {
        reasonMap.clear();
        if ("zh_hans".equalsIgnoreCase(language)) {
            reasonMap.put("other", "其他");
            reasonMap.put("game_cheat_client", "使用作弊客户端");
            reasonMap.put("game_pvp_tk", "PVP恶意伤害队友");
            reasonMap.put("chat_spam", "滥用聊天");
        } else if ("zh_hant".equalsIgnoreCase(language)) {
            reasonMap.put("other", "其他");
            reasonMap.put("game_cheat_client", "使用作弊客戶端");
            reasonMap.put("game_pvp_tk", "PVP惡意傷害隊友");
            reasonMap.put("chat_spam", "濫用聊天");
        } else if ("en_us".equalsIgnoreCase(language)) {
            reasonMap.put("other", "Other");
            reasonMap.put("game_cheat_client", "Using cheat client");
            reasonMap.put("game_pvp_tk", "Malicious PVP teamkilling");
            reasonMap.put("chat_spam", "Chat spam/abuse");
        } else if ("ja_jp".equalsIgnoreCase(language)) {
            reasonMap.put("other", "その他");
            reasonMap.put("game_cheat_client", "チートクライアントの使用");
            reasonMap.put("game_pvp_tk", "PVPでの悪意ある味方攻撃");
            reasonMap.put("chat_spam", "チャットスパム/乱用");
        }
    }

    public String getReason(String reasonId) {
        return reasonMap.getOrDefault(reasonId, reasonId);
    }
} 