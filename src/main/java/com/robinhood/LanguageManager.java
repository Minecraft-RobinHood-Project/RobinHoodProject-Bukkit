package com.robinhood;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LanguageManager {
    private Map<String, String> messages = new HashMap<>();
    private FileConfiguration config;

    public LanguageManager(FileConfiguration config) {
        this.config = config;
        loadLanguage();
    }

    public void loadLanguage() {
        // 有效的语言列表
        Set<String> validLanguages = new HashSet<>(Arrays.asList("zh_hans", "zh_hant", "en_us", "ja_jp"));

        // 读取语言配置
        String language = config.getString("language", "zh_hans");

        // 判断合法性，不合法则取默认值
        if (!validLanguages.contains(language.toLowerCase())) {
            language = "zh_hans";
        }

        // 清除列表，准备添加语言内容
        messages.clear();

        if ("zh_hans".equalsIgnoreCase(language)) {
            // 简体中文消息
            messages.put("TranslationContributors", "当前语言: 简体中文 (贡献者: Zhang1233)");
            messages.put("StartUP", "RobinHoodProject 已加载!");
            messages.put("ShutDown", "RobinHoodProject 已卸载!");
            // 黑名单部分
            messages.put("loadBlackList_LocalCache", "联网获取黑名单列表失败, 正在使用本地缓存");
            messages.put("loadBlackList_Failed", "获取黑名单列表失败: %s");
            messages.put("loadBlackList_FailedManual", "无法联网获取黑名单数据, 请查看帮助文档!");
            messages.put("loadBlackList_ParseError", "黑名单文件格式解析失败, 原始内容: %s");
            messages.put("loadBlackList_Success", "黑名单联网更新成功");
            // 检查更新部分
            messages.put("CheckUpdate_CurrentUsingVersion", "当前使用版本: %s");
            messages.put("CheckUpdate_Checking", "正在检查更新...");
            messages.put("CheckUpdate_Failed", "检查更新失败，请检查你的网络状况!");
            messages.put("CheckUpdate_NewVersionAvailable", "发现新版本: %s");
            messages.put("CheckUpdate_NewVersionDownloadURL", "新版本下载地址: %s");
            messages.put("CheckUpdate_AdviseUpdate", "建议尽快更新至最新版本, 避免出现问题!");
            messages.put("CheckUpdate_NowUsingLatestVersion", "您正在使用最新版本!");
        
            messages.put("BlackList_BannedTitle", "\n\n&6&l================================================\n\n&6&l你已被 RobinHood Project 联合黑名单封禁\n\n&c封禁原因: %s\n\n&c封禁类型: %s\n\n&c封禁时间: %s\n\n&6请前往 RobinHood Project Github 仓库页面申诉\n\n&6&l================================================");
            messages.put("autoUpdateBlackList_Updating", "正在尝试自动更新黑名单列表");
            messages.put("autoUpdateBlackList_Failed", "黑名单自动更新失败: %s");
        } else if ("zh_hant".equalsIgnoreCase(language)) {
            // 繁體中文消息
            messages.put("TranslationContributors", "當前語言: 繁體中文 (貢獻者: Zhang1233)");
            messages.put("StartUP", "RobinHoodProject 已加載!");
            messages.put("ShutDown", "RobinHoodProject 已卸載!");

            messages.put("loadBlackList_LocalCache", "無法聯網獲取黑名單列表，正在使用本地快取");
            messages.put("loadBlackList_Failed", "獲取黑名單列表失敗: %s");
            messages.put("loadBlackList_FailedManual", "無法聯網獲取黑名單數據，請查看幫助文檔!");
            messages.put("loadBlackList_ParseError", "黑名單文件格式解析失敗，原始內容: %s");
            messages.put("loadBlackList_Success", "黑名單聯網更新成功");

            messages.put("CheckUpdate_CurrentUsingVersion", "當前使用版本: %s");
            messages.put("CheckUpdate_Checking", "正在檢查更新...");
            messages.put("CheckUpdate_Failed", "檢查更新失敗，請檢查你的網路狀況!");
            messages.put("CheckUpdate_NewVersionAvailable", "發現新版本: %s");
            messages.put("CheckUpdate_NewVersionDownloadURL", "新版本下載地址: %s");
            messages.put("CheckUpdate_AdviseUpdate", "建議盡快更新至最新版本，避免出現問題!");
            messages.put("CheckUpdate_NowUsingLatestVersion", "您正在使用最新版本!");

            messages.put("BlackList_BannedTitle", "\n\n&6&l================================================\n\n&6&l你已被 RobinHood Project 聯合黑名單封禁\n\n&c封禁原因: %s\n\n&c封禁類型: %s\n\n&c封禁時間: %s\n\n&6請前往 RobinHood Project Github 倉庫頁面申訴\n\n&6&l================================================");
            messages.put("autoUpdateBlackList_Updating", "正在嘗試自動更新黑名單列表");
            messages.put("autoUpdateBlackList_Failed", "黑名單自動更新失敗: %s");
        } else if ("en_us".equalsIgnoreCase(language)) {
            // English messages
            messages.put("TranslationContributors", "Current language: English (Contributor: Zhang1233)");
            messages.put("StartUP", "RobinHoodProject loaded!");
            messages.put("ShutDown", "RobinHoodProject unloaded!");

            messages.put("loadBlackList_LocalCache", "Failed to fetch blacklist online, using local cache.");
            messages.put("loadBlackList_Failed", "Failed to fetch blacklist: %s");
            messages.put("loadBlackList_FailedManual", "Unable to fetch blacklist online, please check the documentation!");
            messages.put("loadBlackList_ParseError", "Blacklist file parse error, raw content: %s");
            messages.put("loadBlackList_Success", "Blacklist updated from the internet successfully.");

            messages.put("CheckUpdate_CurrentUsingVersion", "Current version: %s");
            messages.put("CheckUpdate_Checking", "Checking for updates...");
            messages.put("CheckUpdate_Failed", "Update check failed, please check your network!");
            messages.put("CheckUpdate_NewVersionAvailable", "New version found: %s");
            messages.put("CheckUpdate_NewVersionDownloadURL", "Download URL: %s");
            messages.put("CheckUpdate_AdviseUpdate", "It is recommended to update to the latest version as soon as possible to avoid problems!");
            messages.put("CheckUpdate_NowUsingLatestVersion", "You are using the latest version!");

            messages.put("BlackList_BannedTitle", "\n\n&6&l================================================\n\n&6&lYou are banned by RobinHood Project Global Blacklist\n\n&cReason: %s\n\n&cType: %s\n\n&cTime: %s\n\n&6Please appeal on the RobinHood Project Github page\n\n&6&l================================================");
            messages.put("autoUpdateBlackList_Updating", "Trying to auto-update blacklist...");
            messages.put("autoUpdateBlackList_Failed", "Auto-update blacklist failed: %s");
        } else if ("ja_jp".equalsIgnoreCase(language)) {
            // Japanese messages
            messages.put("TranslationContributors", "現在の言語: 日本語 (貢献者: Zhang1233)");
            messages.put("StartUP", "RobinHoodProjectがロードされました!");
            messages.put("ShutDown", "RobinHoodProjectがアンロードされました!");

            messages.put("loadBlackList_LocalCache", "ブラックリストのオンライン取得に失敗、ローカルキャッシュを使用します。");
            messages.put("loadBlackList_Failed", "ブラックリストの取得に失敗しました: %s");
            messages.put("loadBlackList_FailedManual", "ブラックリストをオンラインで取得できません。ドキュメントを確認してください!");
            messages.put("loadBlackList_ParseError", "ブラックリストファイルの解析エラー、生データ: %s");
            messages.put("loadBlackList_Success", "ブラックリストのオンライン更新に成功しました。");

            messages.put("CheckUpdate_CurrentUsingVersion", "現在のバージョン: %s");
            messages.put("CheckUpdate_Checking", "アップデートを確認中...");
            messages.put("CheckUpdate_Failed", "アップデートの確認に失敗しました。ネットワークを確認してください!");
            messages.put("CheckUpdate_NewVersionAvailable", "新しいバージョンが見つかりました: %s");
            messages.put("CheckUpdate_NewVersionDownloadURL", "ダウンロードURL: %s");
            messages.put("CheckUpdate_AdviseUpdate", "できるだけ早く最新バージョンにアップデートすることをお勧めします!");
            messages.put("CheckUpdate_NowUsingLatestVersion", "あなたは最新バージョンを使用しています!");
            
            messages.put("BlackList_BannedTitle", "\n\n&6&l================================================\n\n&6&lあなたはRobinHood ProjectのグローバルブラックリストによりBANされました\n\n&c理由: %s\n\n&cタイプ: %s\n\n&c時間: %s\n\n&6異議がある場合はRobinHood ProjectのGithubページで申立てしてください\n\n&6&l================================================");
            messages.put("autoUpdateBlackList_Updating", "ブラックリストの自動更新を試みています...");
            messages.put("autoUpdateBlackList_Failed", "ブラックリストの自動更新に失敗しました: %s");
        }
    }

    /**
     * 获取语言消息
     * @param key 消息键名
     * @return 对应的语言消息，如果不存在则返回键名
     */
    public String getMessage(String key) {
        return messages.getOrDefault(key, key);
    }
}
