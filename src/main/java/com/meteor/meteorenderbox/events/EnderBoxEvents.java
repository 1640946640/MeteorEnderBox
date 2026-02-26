package com.meteor.meteorenderbox.events;

import com.meteor.meteorenderbox.*;
<<<<<<< HEAD
import com.meteor.meteorenderbox.lib.message.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.event.inventory.*;
import com.meteor.meteorenderbox.data.holder.*;
import com.meteor.meteorenderbox.data.*;
import com.meteor.meteorenderbox.util.*;

public class EnderBoxEvents implements Listener
{
    MeteorEnderBox plugin;
    MessageManager mes;
    
    public EnderBoxEvents(final MeteorEnderBox plugin) {
        this.plugin = plugin;
        this.mes = plugin.getEnderData().getMessageManager();
    }
    
    private boolean isUnlock(final int number, final int playernumber) {
        return number <= playernumber;
    }
    
    private int takePoint(final PayType payType, final int row) {
        final String path = (payType == PayType.MONEY) ? "setting.take-money." : "setting.take-points.";
        return this.plugin.getConfig().getInt(path + row);
    }
    
    @EventHandler
    void quitGame(final PlayerQuitEvent quitEvent) {
        final String pn = quitEvent.getPlayer().getName();
        if (this.plugin.getEnderData().getPlayerDataMap().containsKey(pn)) {
            final PlayerData playerData = this.plugin.getEnderData().getPlayerDataMap().get(pn);
            // 重置锁定状态
            playerData.setLock(false);
            // 保存玩家数据
            this.plugin.getEnderData().saveData(playerData, false);
        }
    }
    
    @EventHandler
    void joinGame(final PlayerJoinEvent joinEvent) {
        final String pn = joinEvent.getPlayer().getName();
        // 确保玩家数据加载时锁定状态为false
        if (this.plugin.getEnderData().getPlayerDataMap().containsKey(pn)) {
            final PlayerData playerData = this.plugin.getEnderData().getPlayerDataMap().get(pn);
            playerData.setLock(false);
        }
    }
    
    @EventHandler
    void closeInvSaveData(final InventoryCloseEvent closeEvent) {
        if (closeEvent.getInventory().getHolder() != null) {
            try {
                final Player player = (Player)closeEvent.getPlayer();
                if (closeEvent.getInventory().getHolder() instanceof EnderBoxHolder) {
                    final EnderBoxHolder enderBoxHolder = (EnderBoxHolder)closeEvent.getInventory().getHolder();
                    // 使用 EnderBoxHolder 中的 targetPlayerName 获取正确的 PlayerData
                    final String targetPlayerName = enderBoxHolder.getTargetPlayerName();
                    final PlayerData playerData = this.plugin.getEnderData().getPlayerDataMap().get(targetPlayerName);
                    
                    if (playerData == null) {
                        player.sendMessage(this.mes.getString("mes.error-data"));
                        return;
                    }
                    
                    synchronized (playerData) {
                        final ItemStack[] itemStacks = closeEvent.getInventory().getContents();
                        final Box box = playerData.getBoxMap().get(enderBoxHolder.getNumber());
                        
                        if (box == null) {
                            player.sendMessage(this.mes.getString("mes.error-box"));
                            return;
                        }
                        
                        // 清空箱子并重新填充物品
                        box.getMap().clear();
                        for (int i = 0; i < itemStacks.length; ++i) {
                            if (itemStacks[i] != null && itemStacks[i].getType() != Material.AIR) {
                                box.getMap().put(i, itemStacks[i]);
                            }
                        }
                        
                        // 重置锁定状态
                        playerData.setLock(false);
                        
                        // 立即保存数据
                        this.plugin.getEnderData().saveData(playerData, true);
                    }
                    
                    player.sendMessage(this.mes.getString("mes.save-data"));
                }
            } catch (Exception e) {
                final Player player = (Player)closeEvent.getPlayer();
                player.sendMessage(this.mes.getString("mes.error-save"));
                this.plugin.getLogger().warning("保存末影箱数据时出错: " + e.getMessage());
                e.printStackTrace();
=======
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
            }
        }
    }
    
<<<<<<< HEAD
    @EventHandler
    void clickUnlock(final InventoryClickEvent clickEvent) {
        if (clickEvent.getInventory().getHolder() == null || !(clickEvent.getInventory().getHolder() instanceof UnlockInv)) {
            return;
        }
        final UnlockInv unlockInv = (UnlockInv)clickEvent.getInventory().getHolder();
        final PayType payType = unlockInv.getPayType();
        final Player player = (Player)clickEvent.getWhoClicked();
        final PlayerData playerData = this.plugin.getEnderData().getPlayerDataMap().get(player.getName());
        clickEvent.setCancelled(true);
        switch (clickEvent.getRawSlot()) {
            case 3: {
                if ((payType == PayType.MONEY && ApiManager.takeMoney(player.getName(), this.takePoint(payType, 2))) || (payType == PayType.POINTS && ApiManager.takePoints(player.getName(), this.takePoint(payType, 2)))) {
                    this.plugin.getEnderData().unLockBox(playerData, unlockInv.getNumber(), 2);
                    player.sendMessage(this.mes.getString("mes.unlock-box").replace("@number@", unlockInv.getNumber() + ""));
                }
            }
            case 4: {
                if ((payType == PayType.MONEY && ApiManager.takeMoney(player.getName(), this.takePoint(payType, 4))) || (payType == PayType.POINTS && ApiManager.takePoints(player.getName(), this.takePoint(payType, 4)))) {
                    this.plugin.getEnderData().unLockBox(playerData, unlockInv.getNumber(), 4);
                    player.sendMessage(this.mes.getString("mes.unlock-box").replace("@number@", unlockInv.getNumber() + ""));
                }
            }
            case 5: {
                if ((payType == PayType.MONEY && ApiManager.takeMoney(player.getName(), this.takePoint(payType, 6))) || (payType == PayType.POINTS && ApiManager.takePoints(player.getName(), this.takePoint(payType, 6)))) {
                    this.plugin.getEnderData().unLockBox(playerData, unlockInv.getNumber(), 6);
                    player.sendMessage(this.mes.getString("mes.unlock-box").replace("@number@", unlockInv.getNumber() + ""));
                }
            }
            default: {}
        }
    }
    
    @EventHandler
    void click(final InventoryClickEvent clickEvent) {
        if (clickEvent.getInventory().getHolder() == null || !(clickEvent.getInventory().getHolder() instanceof EnderBoxListHolder) || clickEvent.getCurrentItem() == null || clickEvent.getCurrentItem().getType() == Material.AIR) {
            return;
        }
        clickEvent.setCancelled(true);
        final EnderBoxListHolder enderBoxListHolder = (EnderBoxListHolder)clickEvent.getInventory().getHolder();
        final int page = enderBoxListHolder.getPage();
        final int rawslot = clickEvent.getRawSlot();
        final Player player = (Player)clickEvent.getWhoClicked();
        // 从库存标题中提取目标玩家名称
        String targetPlayerName = player.getName();
        final String inventoryTitle = clickEvent.getInventory().getTitle();
        if (inventoryTitle.contains(" - ")) {
            targetPlayerName = inventoryTitle.substring(inventoryTitle.lastIndexOf(" - ") + 3);
        }
        final PlayerData playerData = this.plugin.getEnderData().getPlayerDataMap().get(targetPlayerName);
        final int maxpage = (int)Math.ceil(this.plugin.getEnderData().getBoxDataList().size() / 46.0);
        switch (rawslot) {
            case 48: {
                if (page == 1) {
                    this.mes.sendMessage(player, "mes.start-page");
                    return;
                }
                player.closeInventory();
                InventoryUtil.openInv(player, targetPlayerName, page - 1);
            }
            case 50: {
                if (page == maxpage) {
                    this.mes.sendMessage(player, "mes.end-page");
                    return;
                }
                player.closeInventory();
                InventoryUtil.openInv(player, targetPlayerName, page + 1);
            }
            default: {
                if (!enderBoxListHolder.getBoxDataMap().containsKey(rawslot)) {
                    return;
                }
                final int number = enderBoxListHolder.getBoxDataMap().get(rawslot).getNumber();
                if (number > Util.getMaxLockBox(player)) {
                    player.sendMessage(this.mes.getString("mes.max-lock"));
                    return;
                }
                if (this.isUnlock(number, playerData.getNumber())) {
                    InventoryUtil.openBox(player, playerData, number, targetPlayerName);
                    return;
                }
                if (number != 1 && number - playerData.getNumber() != 1) {
                    player.sendMessage(this.mes.getString("mes.pre-number").replace("@number@", "" + (number - 1)));
                    return;
                }
                player.closeInventory();
                InventoryUtil.openUnlockGui(player, number);
            }
        }
    }
}
=======
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
