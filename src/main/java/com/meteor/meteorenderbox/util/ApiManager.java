package com.meteor.meteorenderbox.util;

import org.black_ixx.playerpoints.*;
import net.milkbowl.vault.economy.*;

import org.bukkit.*;
import com.meteor.meteorenderbox.*;

/**
 * API管理类
 * 负责处理经济系统和点券系统的操作
 */
public class ApiManager
{
    /** PlayerPoints API实例 */
    public static PlayerPointsAPI pointsAPI;
    /** Vault经济API实例 */
    public static Economy econmy;
    
    /**
     * 扣除玩家金钱
     * @param player 玩家名称
     * @param money 扣除金额
     * @return 是否成功扣除
     */
    public static boolean takeMoney(final String player, final int money) {
        Bukkit.getPlayer(player).closeInventory();
        if (ApiManager.econmy.getBalance(player) < money) {
            Bukkit.getPlayer(player).sendMessage(MeteorEnderBox.Instance.getEnderData().getMessageManager().getString("mes.no-money"));
            return false;
        }
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
        Bukkit.getPlayer(player).closeInventory();
        if (ApiManager.pointsAPI.look(Bukkit.getPlayer(player).getUniqueId()) < points) {
            Bukkit.getPlayer(player).sendMessage(MeteorEnderBox.Instance.getEnderData().getMessageManager().getString("mes.no-points"));
            return false;
        }
        ApiManager.pointsAPI.take(Bukkit.getPlayer(player).getUniqueId(), points);
        return true;
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
