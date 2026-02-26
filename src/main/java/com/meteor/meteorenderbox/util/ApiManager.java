package com.meteor.meteorenderbox.util;

import org.black_ixx.playerpoints.*;
import net.milkbowl.vault.economy.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import com.meteor.meteorenderbox.*;
import java.util.UUID;

/**
 * API管理类
 * 负责处理经济系统和点券系统的操作
 */
public class ApiManager
{
    /** PlayerPoints API实例 */
    public static org.black_ixx.playerpoints.PlayerPointsAPI pointsAPI;
    /** Vault经济API实例 */
    public static Economy econmy;
    
    /**
     * 扣除玩家金钱
     * @param player 玩家名称
     * @param money 扣除金额
     * @return 是否成功扣除
     */
    public static boolean takeMoney(final String player, final int money) {
        // 检查玩家是否在线
        Player onlinePlayer = Bukkit.getPlayer(player);
        if (onlinePlayer != null) {
            onlinePlayer.closeInventory();
        }
        
        // 检查余额
        if (ApiManager.econmy.getBalance(player) < money) {
            return false;
        }
        
        // 扣除金钱
        ApiManager.econmy.withdrawPlayer(player, (double)money);
        return true;
    }
    
    /**
     * 扣除玩家点券
     * @param player 玩家名称
     * @param points 扣除点券数量
     * @return 是否成功扣除
     */
    public static boolean takePoints(final String player, final int points) {
        // 检查玩家是否在线
        Player onlinePlayer = Bukkit.getPlayer(player);
        if (onlinePlayer != null) {
            onlinePlayer.closeInventory();
        }
        
        // 检查余额
        if (onlinePlayer != null) {
            if (ApiManager.pointsAPI.look(onlinePlayer.getUniqueId()) < points) {
                return false;
            }
            ApiManager.pointsAPI.take(onlinePlayer.getUniqueId(), points);
        } else {
            // 离线玩家处理 - 使用 UUID 转换
            try {
                UUID playerUUID = Bukkit.getOfflinePlayer(player).getUniqueId();
                if (ApiManager.pointsAPI.look(playerUUID) < points) {
                    return false;
                }
                ApiManager.pointsAPI.take(playerUUID, points);
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 检查经济系统是否可用
     * @return 是否可用
     */
    public static boolean isEconmy() {
        return ApiManager.econmy != null;
    }
    
    /**
     * 检查点券系统是否可用
     * @return 是否可用
     */
    public static boolean isPoints() {
        return ApiManager.pointsAPI != null;
    }
    
    /**
     * 静态初始化块
     * 初始化API实例为null
     */
    static {
        ApiManager.pointsAPI = null;
        ApiManager.econmy = null;
    }
}
