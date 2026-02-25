package com.meteor.meteorenderbox.events;

import com.meteor.meteorenderbox.*;
import com.meteor.meteorenderbox.data.*;
import com.meteor.meteorenderbox.data.holder.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;

/**
 * 末影箱事件监听器
 * 处理玩家打开、关闭末影箱等事件
 */
public class EnderBoxEvents implements Listener
{
    /** 插件实例 */
    MeteorEnderBox plugin;
    
    /**
     * 构造函数
     * @param plugin 插件实例
     */
    public EnderBoxEvents(final MeteorEnderBox plugin) {
        this.plugin = plugin;
    }
    
    /**
     * 监听玩家关闭末影箱事件
     * @param event 关闭库存事件
     */
    @EventHandler
    public void closeInvSaveData(final InventoryCloseEvent event) {
        // 检查是否为末影箱库存
        if (event.getInventory().getHolder() instanceof EnderBoxHolder) {
            final EnderBoxHolder holder = (EnderBoxHolder)event.getInventory().getHolder();
            final Player player = (Player)event.getPlayer();
            
            // 检查玩家数据是否存在
            if (this.plugin.getEnderData().getPlayerDataMap().containsKey(player.getName())) {
                final PlayerData playerData = this.plugin.getEnderData().getPlayerDataMap().get(player.getName());
                final Box box = playerData.getBoxMap().get(holder.getNumber());
                
                // 保存物品到末影箱
                final ItemStack[] contents = event.getInventory().getContents();
                for (int i = 0; i < contents.length; ++i) {
                    box.getMap().put(i, contents[i]);
                }
                
                // 保存数据
                this.plugin.getEnderData().saveData(playerData, false);
                player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.save"));
            }
        }
    }
    
    /**
     * 监听玩家退出游戏事件
     * @param event 玩家退出事件
     */
    @EventHandler
    public void quitGame(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        // 检查玩家数据是否存在
        if (this.plugin.getEnderData().getPlayerDataMap().containsKey(player.getName())) {
            final PlayerData playerData = this.plugin.getEnderData().getPlayerDataMap().get(player.getName());
            // 保存数据
            this.plugin.getEnderData().saveData(playerData, true);
        }
    }
    
    /**
     * 监听玩家加入游戏事件
     * @param event 玩家加入事件
     */
    @EventHandler
    public void joinGame(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        // 重置玩家的锁定状态，确保玩家可以正常打开末影箱
        // 这里可以添加额外的初始化逻辑
    }
}