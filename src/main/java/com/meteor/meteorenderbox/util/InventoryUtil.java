package com.meteor.meteorenderbox.util;

import com.meteor.meteorenderbox.*;
import com.meteor.meteorenderbox.data.*;
import com.meteor.meteorenderbox.data.holder.*;
import com.meteor.meteorenderbox.lib.message.MessageManager;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import java.util.*;
import org.bukkit.inventory.meta.*;

/**
 * 库存工具类
 * 负责打开末影箱和处理库存相关操作
 */
public class InventoryUtil
{
    /** 消息管理器 */
    private static MessageManager messageManager;
    
    /**
     * 打开末影箱列表界面
     * @param player 玩家
     * @param page 页码
     */
    public static void openInv(final Player player, final int page) {
        openInv(player, player.getName(), page);
    }
    
    /**
     * 打开指定玩家的末影箱列表界面（管理员用）
     * @param player 执行命令的玩家
     * @param targetPlayerName 目标玩家名称
     * @param page 页码
     */
    public static void openInv(final Player player, final String targetPlayerName, final int page) {
        final PlayerData playerData = MeteorEnderBox.Instance.getEnderData().getPlayerDataMap().get(targetPlayerName);
        if (playerData == null) {
            player.sendMessage(MeteorEnderBox.Instance.getEnderData().getMessageManager().getString("prefix") + " §7无法找到玩家 " + targetPlayerName + " 的数据");
            return;
        }
        
        // 创建末影箱列表界面
        final EnderBoxListHolder holder = new EnderBoxListHolder(page, targetPlayerName);
        final Inventory inv = Bukkit.createInventory((InventoryHolder)holder, 54, "末影箱列表 (第" + page + "页)");
        
        // 填充末影箱列表
        int index = 0;
        for (final BoxData boxData : MeteorEnderBox.Instance.getEnderData().getBoxDataList()) {
            if (index >= (page - 1) * 45 && index < page * 45) {
                // 检查末影箱是否已解锁
                if (playerData.getBoxMap().containsKey(boxData.getNumber())) {
                    // 已解锁，显示末影箱图标
                    final ItemStack item = new ItemStack(Material.ENDER_CHEST);
                    final ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("§a# 末影箱 " + boxData.getNumber() + " §7[已解锁]");
                    
                    // 添加 LORE
                    List<String> lore = new ArrayList<>();
                    lore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    lore.add("§6状态: §a已解锁");
                    
                    // 计算末影箱大小
                    int rows = 2;
                    if (boxData.getNumber() <= 10) {
                        rows = 2;
                    } else if (boxData.getNumber() <= 15) {
                        rows = 4;
                    } else {
                        rows = 6;
                    }
                    
                    lore.add("§6容量: §e" + rows + " 行");
                    lore.add("§6点击打开: §7查看存储物品");
                    
                    // 如果是管理员查看其他玩家的末影箱，添加右键封锁提示
                    if (!player.getName().equals(targetPlayerName) && player.hasPermission("enderbox.admin")) {
                        lore.add("§6右键点击: §c封锁末影箱（需为空）");
                    }
                    
                    lore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    meta.setLore(lore);
                    
                    item.setItemMeta(meta);
                    inv.setItem(index % 45, item);
                } else {
                    // 未解锁，显示锁定图标
                    final ItemStack item = new ItemStack(Material.BARRIER);
                    final ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("§c# 末影箱 " + boxData.getNumber() + " §7[未解锁]");
                    
                    // 添加 LORE
                    List<String> lore = new ArrayList<>();
                    lore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    lore.add("§6状态: §c未解锁");
                    
                    // 计算末影箱大小和价格
                    int rows = 2;
                    int moneyCost = 0;
                    int pointsCost = 0;
                    
                    if (boxData.getNumber() <= 10) {
                        rows = 2;
                    } else if (boxData.getNumber() <= 15) {
                        rows = 4;
                    } else {
                        rows = 6;
                    }
                    
                    moneyCost = MeteorEnderBox.Instance.getConfig().getInt("setting.take-money." + rows, 1000);
                    pointsCost = MeteorEnderBox.Instance.getConfig().getInt("setting.take-points." + rows, 100);
                    
                    lore.add("§6容量: §e" + rows + " 行");
                    lore.add("§6金币价格: §e" + moneyCost + " 金币");
                    lore.add("§6点券价格: §c" + pointsCost + " 点券");
                    lore.add("§6点击解锁: §7选择支付方式");
                    lore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    meta.setLore(lore);
                    
                    item.setItemMeta(meta);
                    inv.setItem(index % 45, item);
                }
            }
            index++;
        }
        
        // 添加上一页和下一页按钮
        if (page > 1) {
            final ItemStack prevPage = new ItemStack(Material.ARROW);
            final ItemMeta meta = prevPage.getItemMeta();
            meta.setDisplayName("上一页");
            prevPage.setItemMeta(meta);
            inv.setItem(45, prevPage);
        }
        
        if (page < (MeteorEnderBox.Instance.getEnderData().getBoxDataList().size() + 44) / 45) {
            final ItemStack nextPage = new ItemStack(Material.ARROW);
            final ItemMeta meta = nextPage.getItemMeta();
            meta.setDisplayName("下一页");
            nextPage.setItemMeta(meta);
            inv.setItem(53, nextPage);
        }
        
        // 打开库存
        player.openInventory(inv);
    }
    
    /**
     * 打开末影箱
     * @param player 玩家
     * @param playerData 玩家数据
     * @param number 末影箱编号
     */
    public static void openBox(final Player player, final PlayerData playerData, final int number) {
        openBox(player, playerData, number, player.getName());
    }
    
    /**
     * 打开末影箱（带目标玩家名称）
     * @param player 玩家
     * @param playerData 玩家数据
     * @param number 末影箱编号
     * @param targetPlayerName 目标玩家名称
     */
    public static void openBox(final Player player, final PlayerData playerData, final int number, final String targetPlayerName) {
        synchronized (playerData) {
            if (playerData.isLock()) {
                player.sendMessage(InventoryUtil.messageManager.getString("mes.lock-data"));
                return;
            }
            
            final EnderBoxHolder enderBoxHolder = new EnderBoxHolder(number);
            enderBoxHolder.setTargetPlayerName(targetPlayerName);
            final Box box = playerData.getBoxMap().get(number);
            final Inventory inventory = Bukkit.createInventory((InventoryHolder)enderBoxHolder, box.getSlot(), "§e# 末影箱 " + number);
            
            // 标记为锁定，防止重复打开
            playerData.setLock(true);
            
            // 异步加载物品，避免阻塞主线程
            Bukkit.getScheduler().runTaskAsynchronously((Plugin)MeteorEnderBox.Instance, () -> {
                // 加载所有物品
                box.getMap().keySet().forEach(a -> {
                    final int slot = (int)a;
                    final ItemStack item = (ItemStack)box.getMap().get(a);
                    // 回到主线程设置物品
                    Bukkit.getScheduler().runTask((Plugin)MeteorEnderBox.Instance, () -> inventory.setItem(slot, item));
                });
            });
            
            // 打开背包
            player.openInventory(inventory);
        }
    }
    
    /**
     * 静态初始化块
     * 初始化消息管理器
     */
    static {
        InventoryUtil.messageManager = MeteorEnderBox.Instance.getEnderData().getMessageManager();
    }
}