package com.meteor.meteorenderbox.lib.message;

import org.bukkit.configuration.file.*;

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
    }
}