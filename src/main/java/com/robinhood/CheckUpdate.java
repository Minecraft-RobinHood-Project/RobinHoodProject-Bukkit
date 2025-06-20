package com.robinhood;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import java.util.logging.Logger;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * 更新检查类，用于检查 GitHub 上的最新版本并提示用户更新
 */
public class CheckUpdate {
    private final Logger logger;
    private final LanguageManager languageManager;
    private final PluginDescriptionFile description;
    private static final String LATEST_VERSION_URL = "https://api.github.com/repos/Minecraft-RobinHood-Project/RobinHoodProject-Bukkit/releases/latest";
    private static final String DOWNLOAD_URL = "https://github.com/Minecraft-RobinHood-Project/RobinHoodProject-Bukkit/releases/latest";

    /**
     * 构造函数，初始化日志、语言管理和插件描述
     * @param logger log记录器
     * @param languageManager 语言管理器
     * @param description 读版本号
     */
    public CheckUpdate(Logger logger, LanguageManager languageManager, PluginDescriptionFile description) {
        this.logger = logger;
        this.languageManager = languageManager;
        this.description = description;
    }

    /**
     * 检查更新
     */
    public void checkUpdate() {
        // 获取当前插件版本号
        String currentVersion = description.getVersion();

        // 提示-当前版本号
        logger.info(String.format(languageManager.getMessage("CheckUpdate_CurrentUsingVersion"), currentVersion));

        // 提示-检查更新中
        logger.info(languageManager.getMessage("CheckUpdate_Checking"));

        // 使用 GitHub API 获取最新版本信息
        try {
            // 返回体
            String response = fetchLatestVersion();
            // 解析 JSON
            JSONObject json = new JSONObject(response);
            // 拆分版本号
            String latestVersion = json.getString("tag_name");

            // 比较版本号
            if (isVersionGreater(latestVersion, currentVersion)) {
                // 提示检查到的新版本
                logger.warning(String.format(languageManager.getMessage("CheckUpdate_NewVersionAvailable"), latestVersion));
                logger.warning(String.format(languageManager.getMessage("CheckUpdate_NewVersionDownloadURL"), DOWNLOAD_URL));
                logger.warning(languageManager.getMessage("CheckUpdate_AdviseUpdate"));
            } else {
                // 已为最新版本
                logger.info(languageManager.getMessage("CheckUpdate_NowUsingLatestVersion"));
            }
        } catch (Exception e) {
            // 爆了，提示检查失败
            logger.warning(languageManager.getMessage("CheckUpdate_Failed"));
        }
    }

    /**
     * 获取最新版本信息
     * @return GitHub API 的 JSON 响应
     * @throws Exception 如果网络请求失败
     */
    private String fetchLatestVersion() throws Exception {
        // 构建URL
        URL url = new URI(LATEST_VERSION_URL).toURL();
        // 请求连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        // 判断返回码
        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            throw new Exception("HTTP Response " + responseCode);
        }

        // 解析返回内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        // 读取
        while ((line = reader.readLine()) != null) {
            // 追加每行内容
            response.append(line);
        }
        // 关闭流和连接
        reader.close();
        connection.disconnect();

        // 返回内容
        return response.toString();
    }

    /**
     * 比较版本号（支持 1.1.1 格式）
     * @param latestVersion 最新版本号
     * @param currentVersion 当前版本号
     * @return 如果 latestVersion > currentVersion，返回 true
     */
    private boolean isVersionGreater(String latestVersion, String currentVersion) {
        // 移除可能的 "v" 前缀
        latestVersion = latestVersion.replaceFirst("^v", "");
        currentVersion = currentVersion.replaceFirst("^v", "");

        String[] v1Parts = latestVersion.split("\\.");
        String[] v2Parts = currentVersion.split("\\.");

        for (int i = 0; i < Math.max(v1Parts.length, v2Parts.length); i++) {
            int v1Part = i < v1Parts.length ? Integer.parseInt(v1Parts[i]) : 0;
            int v2Part = i < v2Parts.length ? Integer.parseInt(v2Parts[i]) : 0;
            if (v1Part > v2Part) return true;
            if (v1Part < v2Part) return false;
        }
        return false;
    }
}