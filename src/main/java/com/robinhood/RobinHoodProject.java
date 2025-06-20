package com.robinhood;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import net.md_5.bungee.api.ChatColor;

public class RobinHoodProject extends JavaPlugin implements Listener {

    private LanguageManager languageManager;
    private ReasonLanguageManager reasonLanguageManager;
    private BukkitTask autoUpdateTask;

    @Override
    public void onEnable() {

        // 保存默认配置文件
        this.saveDefaultConfig();

        // bStats
        boolean enablebStats = getConfig().getBoolean("bStats", true);
        int pluginId = 26224;
        if (enablebStats) {
            new Metrics(this, pluginId);
        }

        // 初始化 LanguageManager
        languageManager = new LanguageManager(getConfig());

        // 初始化 ReasonLanguageManager
        reasonLanguageManager = new ReasonLanguageManager(getConfig());

        // 注册事件监听器
        Bukkit.getPluginManager().registerEvents(this, this);

        // 加载黑名单
        boolean blackListLoaded = false;
        try {
            BlackListManager.loadBlackList(getLogger(), languageManager, getConfig(), this.getDataFolder());
            blackListLoaded = true;
        } catch (Exception e) {
            getLogger().severe(String.format(languageManager.getMessage("loadBlackList_Failed"), e.getMessage()));
            throw new RuntimeException(e);
        }

        // 自动定时更新黑名单
        if (blackListLoaded && getConfig().getBoolean("auto-update-blacklist.enable", false)) {
            int interval = getConfig().getInt("auto-update-blacklist.interval-minutes", 60);
            autoUpdateTask = new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        getLogger().info(languageManager.getMessage("autoUpdateBlackList_Updating"));
                        BlackListManager.loadBlackList(getLogger(), languageManager, getConfig(), RobinHoodProject.this.getDataFolder());
                    } catch (Exception e) {
                        getLogger().warning(String.format(languageManager.getMessage("autoUpdateBlackList_Failed"), e.getMessage()));
                    }
                }
            }.runTaskTimerAsynchronously(this, interval * 60L * 20L, interval * 60L * 20L);
        }

        // 翻译者
        getLogger().info(languageManager.getMessage("TranslationContributors"));

        // 启动消息
        getLogger().info(languageManager.getMessage("StartUP"));

        // 创建 CheckUpdate 实例
        CheckUpdate updateChecker = new CheckUpdate(
            getLogger(), // log记录器
            languageManager, // 语言管理器
            getDescription() // 插件版本信息
        );

        // 异步检查更新
        new BukkitRunnable() {
            @Override
            public void run() {
                updateChecker.checkUpdate();
            }
        }.runTaskAsynchronously(this);
    }

    @Override
    public void onDisable() {
        if (autoUpdateTask != null) {
            autoUpdateTask.cancel();
        }
        getLogger().info(languageManager.getMessage("ShutDown"));
    }

    // 玩家预登录事件
    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String uuid = event.getUniqueId().toString();
        if (BlackListManager.isBlacklisted(uuid)) {
            String reasonId = BlackListManager.getReasonId(uuid);
            String reasonOriginal = BlackListManager.getReasonOriginal(uuid);
            String reasonMain = reasonLanguageManager.getReason(reasonId);
            String banTimestamp = BlackListManager.getBanTimestamp(uuid);
            String banTimeStr = formatTimestamp(banTimestamp) + " (UTC+8)";
            String msg = String.format(
                languageManager.getMessage("BlackList_BannedTitle"),
                reasonOriginal,
                reasonMain,
                banTimeStr
            );
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, ChatColor.translateAlternateColorCodes('&', msg));
            return;
        }
    }

    // 时间戳格式化方法
    private String formatTimestamp(String timestampStr) {
        try {
            long ts = Long.parseLong(timestampStr) * 1000L;
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(java.util.TimeZone.getTimeZone("Asia/Shanghai"));
            return sdf.format(new java.util.Date(ts));
        } catch (Exception e) {
            return "unknown";
        }
    }
}
