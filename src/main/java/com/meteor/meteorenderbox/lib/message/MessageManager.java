package com.meteor.meteorenderbox.lib.message;

import org.bukkit.configuration.file.*;
<<<<<<< HEAD
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.*;

/**
 * 消息管理器
 * 用于管理消息配置和发送消息
 */
public class MessageManager
{
    /** 消息配置 */
    YamlConfiguration yml;
    /** 消息前缀 */
    String prefix;
    /** 未找到消息 */
    String nofind;
    
    /**
     * 构造函数
     * @param yml 消息配置
     * @param prefix 是否使用前缀
     */
    public MessageManager(final YamlConfiguration yml, final boolean prefix) {
        this.prefix = "";
        this.nofind = "未找到消息文件,请检查配置";
        this.yml = yml;
        this.prefix = (prefix ? yml.getString("prefix") : this.prefix);
    }
    
    /**
     * 获取消息字符串
     * @param path 消息路径
     * @return 消息字符串
     */
    public String getString(final String path) {
        return (this.yml.getString(path) == null) ? this.nofind : this.yml.getString(path).replace("@prefix@", this.prefix).replace("&", "§");
    }
    
    /**
     * 获取消息字符串列表
     * @param path 消息路径
     * @return 消息字符串列表
     */
    public List<String> getStringList(final String path) {
        if (this.yml.getStringList(path) == null) {
            return Arrays.asList(this.nofind);
        }
        final List<String> mes = new ArrayList<String>();
        this.yml.getStringList(path).forEach(a -> {
            a = a.replace("@prefix@", this.prefix).replace("&", "§");
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
            if (!this.getStringList(path).equals(this.nofind)) {
                this.sendMessageList(player, path);
            }
            else {
                this.sendMessage(player, path);
            }
        });
=======

/**
 * 消息管理器
 * 负责管理插件的消息配置和获取
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
            if (this.prefix && this.yml.contains("prefix")) {
                message = this.yml.getString("prefix").replace('&', '§') + " " + message;
            }
            return message;
        }
        return "消息不存在: " + key;
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    }
}