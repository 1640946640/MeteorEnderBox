package com.meteor.meteorenderbox;

import org.bukkit.plugin.java.*;
import org.black_ixx.playerpoints.*;
import com.meteor.meteorenderbox.util.*;
import net.milkbowl.vault.economy.*;
import com.meteor.meteorenderbox.cmd.*;
import org.bukkit.command.*;
import com.meteor.meteorenderbox.events.*;
import org.bukkit.event.*;
import org.bukkit.plugin.*;
import java.io.*;
import com.meteor.meteorenderbox.data.*;
import org.bukkit.Bukkit;

/**
 * 末影箱插件主类
 * 负责插件的初始化、依赖检查、命令注册等功能
 */
public final class MeteorEnderBox extends JavaPlugin
{
    /** 插件实例，用于全局访问 */
    public static MeteorEnderBox Instance;
    /** 末影箱数据管理类实例 */
    EnderData enderData;
    
    /**
     * 构造函数，初始化插件实例
     */
    public MeteorEnderBox() {
        MeteorEnderBox.Instance = this;
    }
    
    /**
     * 插件启用时执行的方法
     * 检查依赖插件、初始化数据、注册命令和事件
     */
    public void onEnable() {
        // 检查依赖插件是否齐全
        if (!this.getServer().getPluginManager().isPluginEnabled("Vault") || 
            !this.getServer().getPluginManager().isPluginEnabled("PlayerPoints")) {
            this.getLogger().info("\u524d\u7f6e\u672a\u9f50\u5168,\u63d2\u4ef6\u90e8\u5206\u529f\u80fd\u53ef\u80fd\u5f02\u5e38,\u8bf7\u68c0\u67e5:");
            this.getLogger().info(" Vault:" + this.getServer().getPluginManager().isPluginEnabled("Vault"));
            this.getLogger().info(" PlayerPoints:" + this.getServer().getPluginManager().isPluginEnabled("PlayerPoints"));
        }
        else {
            // 连接PlayerPoints
            final PlayerPoints playerPoints = (PlayerPoints)PlayerPoints.getPlugin((Class)PlayerPoints.class);
            ApiManager.pointsAPI = playerPoints.getAPI();
            this.getLogger().info("\u5df2\u5b8c\u6210\u5173\u8054PlayerPoints");
            // 连接Vault经济系统
            final RegisteredServiceProvider<Economy> ecoapi = (RegisteredServiceProvider<Economy>)this.getServer().getServicesManager().getRegistration((Class)Economy.class);
            final Economy economy = ApiManager.econmy = (Economy)ecoapi.getProvider();
            this.getLogger().info("\u5df2\u5b8c\u6210\u5173\u8054Vault");
        }
        
        // 保存默认配置文件
        this.saveDefaultConfig();
        // 初始化末影箱数据管理
        this.enderData = new EnderData(this);
        // 注册命令和事件
        this.registerCmdEvents();
        
        // 启动定时保存任务，每5分钟保存一次所有玩家数据
        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> {
            if (!this.enderData.getPlayerDataMap().isEmpty()) {
                this.enderData.getPlayerDataMap().values().forEach(playerData -> {
                    try {
                        this.enderData.saveData(playerData, true);
                    } catch (Exception e) {
                        this.getLogger().warning("保存玩家 " + playerData.getPlayer() + " 的数据时出错: " + e.getMessage());
                    }
                });
                this.getLogger().info("已自动保存所有玩家末影箱数据");
            }
        }, 20 * 60 * 5, 20 * 60 * 5); // 5分钟（20ticks/秒 * 60秒 * 5分钟）
        
        this.getLogger().info("\u672b\u5f71\u7bb1\u63d2\u4ef6\u5df2\u8f7d\u5165...");
    }
    
    /**
     * 插件禁用时执行的方法
     * 保存玩家数据并关闭数据库连接
     */
    public void onDisable() {
        // 如果有玩家数据，保存并关闭连接
        if (!this.getEnderData().getPlayerDataMap().isEmpty()) {
            this.getEnderData().getPlayerDataMap().values().forEach(a -> this.getEnderData().saveData(a, true));
            this.getEnderData().close();
            this.getLogger().info("\u4ed3\u5e93\u4fe1\u606f\u5df2\u4fdd\u5b58\u5b8c\u6bd5,\u65ad\u5f00\u6570\u636e\u5e93\u8fde\u63a5");
        }
    }
    
    /**
     * 获取末影箱数据管理实例
     * @return EnderData实例
     */
    public EnderData getEnderData() {
        return this.enderData;
    }
    
    /**
     * 注册命令和事件监听器
     */
    private void registerCmdEvents() {
        // 注册enderbox命令执行器
        this.getServer().getPluginCommand("enderbox").setExecutor((CommandExecutor)new EnderBoxCmds(this));
        // 注册事件监听器
        this.getServer().getPluginManager().registerEvents((Listener)new EnderBoxEvents(this), (Plugin)this);
    }
    
    /**
     * 保存默认配置文件
     * 检查config.yml和message.yml是否存在，不存在则自动创建
     */
    public void saveDefaultConfig() {
        // 保存默认配置文件
        final String[] array;
        final String[] strings = array = new String[] { "config.yml" };
        for (final String yml : array) {
            final File file = new File(this.getDataFolder() + "/" + yml);
            if (!file.exists()) {
                this.saveResource(yml, false);
                this.getLogger().info("\u7f3a\u5931\u914d\u7f6e\u6587\u4ef6" + yml + ",\u5df2\u81ea\u52a8\u8865\u5168...");
            }
        }
        
        // 根据服务器版本保存对应的消息配置文件
        String version = Bukkit.getVersion();
        String versionPattern = "";
        if (version.contains("1.16.5")) {
            versionPattern = "1.16.5";
        } else if (version.contains("1.17")) {
            versionPattern = "1.17";
        } else if (version.contains("1.18")) {
            versionPattern = "1.18";
        } else if (version.contains("1.19")) {
            versionPattern = "1.19";
        } else if (version.contains("1.20")) {
            versionPattern = "1.20";
        }
        
        // 确定要使用的消息配置文件
        String messageFile = "message.yml";
        if (!versionPattern.isEmpty()) {
            String versionedMessageFile = "message_V" + versionPattern + ".yml";
            // 检查版本特定的配置文件是否存在
            if (this.getResource(versionedMessageFile) != null) {
                messageFile = versionedMessageFile;
            }
        }
        
        // 保存消息配置文件为 message.yml
        File messageYmlFile = new File(this.getDataFolder() + "/message.yml");
        if (!messageYmlFile.exists()) {
            this.saveResource(messageFile, false);
            this.getLogger().info("\u7f3a\u5931\u914d\u7f6e\u6587\u4ef6message.yml,\u5df2\u81ea\u52a8\u8865\u5168...");
        }
    }
}