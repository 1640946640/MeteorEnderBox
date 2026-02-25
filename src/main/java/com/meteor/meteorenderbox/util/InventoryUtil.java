package com.meteor.meteorenderbox.util;

import com.meteor.meteorenderbox.*;
import com.meteor.meteorenderbox.data.*;
import com.meteor.meteorenderbox.data.holder.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.*;

/**
 * 库存工具类
 * 负责打开末影箱和处理库存相关操作
 */
public class InventoryUtil
{
    /**
     * 打开末影箱列表
     * @param player 玩家
     */
    public static void openInv(final Player player, final int page) {
        // 获取玩家数据
        final PlayerData playerData = MeteorEnderBox.Instance.getEnderData().getPlayerDataMap().get(player.getName());
        if (playerData == null) {
            return;
        }
        
        // 创建末影箱列表界面
        final EnderBoxListHolder holder = new EnderBoxListHolder(page);
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
                    meta.setDisplayName("末影箱 " + boxData.getNumber());
                    item.setItemMeta(meta);
                    inv.setItem(index % 45, item);
                } else {
                    // 未解锁，显示锁定图标
                    final ItemStack item = new ItemStack(Material.IRON_BARS);
                    final ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("末影箱 " + boxData.getNumber() + " (未解锁)");
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
     * @param number 末影箱编号
     */
    public static void openBox(final Player player, final int number) {
        // 获取玩家数据
        final PlayerData playerData = MeteorEnderBox.Instance.getEnderData().getPlayerDataMap().get(player.getName());
        if (playerData == null) {
            return;
        }
        
        // 检查末影箱是否已解锁
        if (!playerData.getBoxMap().containsKey(number)) {
            player.sendMessage(MeteorEnderBox.Instance.getEnderData().getMessageManager().getString("prefix") + " " + MeteorEnderBox.Instance.getEnderData().getMessageManager().getString("mes.no-box"));
            return;
        }
        
        // 获取末影箱数据
        final Box box = playerData.getBoxMap().get(number);
        
        // 创建末影箱界面
        final EnderBoxHolder holder = new EnderBoxHolder(number);
        final Inventory inv = Bukkit.createInventory((InventoryHolder)holder, box.getSlot(), "末影箱 " + number);
        
        // 填充物品
        for (final int slot : box.getMap().keySet()) {
            inv.setItem(slot, box.getMap().get(slot));
        }
        
        // 打开库存
        player.openInventory(inv);
    }
}