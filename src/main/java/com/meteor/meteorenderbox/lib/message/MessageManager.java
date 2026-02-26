package com.meteor.meteorenderbox.lib.message;

import org.bukkit.configuration.file.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;

/**
 * 消息管理器
 * 用于管理消息配置和发送消息
 */
public class MessageManager
{
    /** 配置文件 */
    private YamlConfiguration yml;
    /** 是否启用前缀 */
    private boolean prefix;
    
    /**
     * 构造函数
     * @param yml 配置文件
     * @param prefix 是否启用前缀
     */
    public MessageManager(final YamlConfiguration yml, final boolean prefix) {
        this.yml = yml;
        this.prefix = prefix;
    }
    
    /**
     * 获取消息
     * @param key 消息键
     * @return 消息内容
     */
    public String getString(final String key) {
        if (this.yml.contains(key)) {
            String message = this.yml.getString(key).replace('&', '§');
            if (this.yml.contains("prefix")) {
                String prefix = this.yml.getString("prefix").replace('&', '§');
                message = message.replace("@prefix@", prefix);
            }
            return message;
        }
        return "消息不存在: " + key;
    }
    
    /**
     * 获取消息字符串列表
     * @param path 消息路径
     * @return 消息字符串列表
     */
    public List<String> getStringList(final String path) {
        if (!this.yml.contains(path)) {
            return Arrays.asList("消息不存在: " + path);
        }
        final List<String> mes = new ArrayList<String>();
        this.yml.getStringList(path).forEach(a -> {
            a = a.replace('&', '§');
            if (this.yml.contains("prefix")) {
                String prefix = this.yml.getString("prefix").replace('&', '§');
                a = a.replace("@prefix@", prefix);
            }
            mes.add(a);
        });
        return mes;
    }
    
    /**
     * 发送消息给玩家
     * @param player 玩家
     * @param path 消息路径
     */
    public void sendMessage(final Player player, final String path) {
        player.sendMessage(this.getString(path));
    }
    
    /**
     * 发送消息列表给玩家
     * @param player 玩家
     * @param path 消息路径
     */
    public void sendMessageList(final Player player, final String path) {
        this.getStringList(path).forEach(a -> player.sendMessage(a));
    }
    
    /**
     * 发送消息给所有在线玩家
     * @param path 消息路径
     */
    public void allMessage(final String path) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (this.yml.contains(path) && this.yml.isList(path)) {
                this.sendMessageList(player, path);
            }
            else {
                this.sendMessage(player, path);
            }
        });
    }
    
    /**
     * 设置配置文件
     * @param yml 配置文件
     */
    public void setYml(final YamlConfiguration yml) {
        this.yml = yml;
    }
    
    /**
     * 获取配置文件
     * @return 配置文件
     */
    public YamlConfiguration getYml() {
        return this.yml;
    }
}