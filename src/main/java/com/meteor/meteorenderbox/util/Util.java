package com.meteor.meteorenderbox.util;

import org.bukkit.entity.*;

/**
 * 工具类
 * 提供一些通用的工具方法
 */
public class Util
{
    /**
     * 发送消息给玩家
     * @param player 玩家
     * @param message 消息
     */
    public static void sendMessage(final Player player, final String message) {
        player.sendMessage(message);
    }
    
    /**
     * 检查玩家是否在线
     * @param player 玩家
     * @return 是否在线
     */
    public static boolean isOnline(final Player player) {
        return player != null && player.isOnline();
    }
}