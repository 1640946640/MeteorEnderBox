package com.meteor.meteorenderbox.util;

import org.bukkit.entity.*;
import org.bukkit.permissions.*;
import com.meteor.meteorenderbox.*;
import java.util.*;

/**
 * 工具类
 * 提供一些通用的工具方法
 */
public class Util
{
    /**
     * 获取玩家最大可解锁末影箱数量
     * @param player 玩家
     * @return 最大可解锁末影箱数量
     */
    public static int getMaxLockBox(final Player player) {
        for (final PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
            if (perm.getPermission().startsWith("meteor.ender.")) {
                return Integer.valueOf(perm.getPermission().substring(13));
            }
        }
        return MeteorEnderBox.Instance.getConfig().getInt("setting.default-maxlock");
    }
    
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