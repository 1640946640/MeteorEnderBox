package com.meteor.meteorenderbox.util;

<<<<<<< HEAD
import com.meteor.meteorenderbox.lib.message.*;
import com.meteor.meteorenderbox.lib.util.*;
import org.bukkit.entity.*;
import com.meteor.meteorenderbox.*;
import org.bukkit.plugin.*;
import org.bukkit.inventory.*;
import org.bukkit.*;
import java.util.*;
import com.meteor.meteorenderbox.data.*;
import org.bukkit.inventory.meta.*;
import com.meteor.meteorenderbox.data.holder.*;

/**
 *  inventory工具类
 * 负责处理末影箱的GUI操作
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
            player.sendMessage("无法找到玩家 " + targetPlayerName + " 的数据");
            return;
        }
        
        final List<BoxData> boxDataList = (List<BoxData>)PageUtil.getPageList((List)MeteorEnderBox.Instance.getEnderData().getBoxDataList(), page, 45);
        final Map<Integer, BoxData> boxDataMap = new HashMap<Integer, BoxData>();
        int slot = 0;
        for (final BoxData boxData : boxDataList) {
            boxDataMap.put(slot, boxData);
            ++slot;
        }
        final EnderBoxListHolder enderBoxListHolder = new EnderBoxListHolder();
        enderBoxListHolder.setBoxDataMap(boxDataMap);
        enderBoxListHolder.setPage(page);
        // 修改标题，显示目标玩家名称
        String title = InventoryUtil.messageManager.getString("title.main");
        if (!player.getName().equals(targetPlayerName)) {
            title = title + " - " + targetPlayerName;
        }
        final Inventory inventory = Bukkit.createInventory((InventoryHolder)enderBoxListHolder, 54, title);
        inventory.setItem(48, getFlagItem("Item.pre"));
        inventory.setItem(50, getFlagItem("Item.next"));
        Arrays.asList(45, 46, 47, 51, 52, 53).forEach(a -> inventory.setItem((int)a, getFlagItem("Item.flag")));
        inventory.setItem(49, getInfo("Item.info", playerData.getNumber(), Util.getMaxLockBox(player)));
        player.openInventory(inventory);
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)MeteorEnderBox.Instance, () -> boxDataList.forEach(a -> {
            final PlayerData playerData2 = MeteorEnderBox.Instance.getEnderData().getPlayerDataMap().get(targetPlayerName);
            final boolean bool = (playerData2.getNumber() >= a.getNumber());
            inventory.addItem(new ItemStack[] { getItemStack(a.getNumber(), bool, a.getPayType(), playerData2) });
        }));
    }
    
    /**
     * 获取末影箱物品栈
     * @param number 末影箱编号
     * @param unlock 是否已解锁
     * @param payTyp 支付类型
     * @param playerData 玩家数据
     * @return 物品栈
     */
    private static ItemStack getItemStack(final int number, final boolean unlock, final PayType payTyp, final PlayerData playerData) {
        final String string = unlock ? "Item.unlock" : "Item.lock";
        final ItemStack itemStack = new ItemStack(Material.valueOf(InventoryUtil.messageManager.getString(string + ".id")), 1, (short)Short.valueOf(InventoryUtil.messageManager.getString(string + ".data")));
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(InventoryUtil.messageManager.getString(string + ".name").replace("@number@", "" + number));
        final List<String> lores = new ArrayList<String>();
        InventoryUtil.messageManager.getStringList(string + ".lore").forEach(a -> {
            final String paytype = ((payTyp == PayType.MONEY) ? InventoryUtil.messageManager.getString("economy-info.vault") : InventoryUtil.messageManager.getString("economy-info.points"));
            String modified = a.replace("@paytype@", paytype).replace("@number@", "" + number);
            if (unlock) {
                modified = modified.replace("@rows@", playerData.getBoxMap().get(number).getSlot() / 9 + "").replace("@item@", playerData.getBoxMap().get(number).getMap().size() + "").replace("@max@", playerData.getBoxMap().get(number).getSlot() + "");
            }
            lores.add(modified);
        });
        itemMeta.setLore((List)lores);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    /**
     * 获取标志物品
     * @param path 配置路径
     * @return 物品栈
     */
    private static ItemStack getFlagItem(final String path) {
        final ItemStack itemStack = new ItemStack(Material.valueOf(InventoryUtil.messageManager.getString(path + ".id")), 1, (short)Short.valueOf(InventoryUtil.messageManager.getString(path + ".data")));
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(InventoryUtil.messageManager.getString(path + ".name"));
        itemMeta.setLore(InventoryUtil.messageManager.getStringList(path + ".lore"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    /**
     * 获取信息物品
     * @param path 配置路径
     * @param number 当前解锁数量
     * @param max 最大解锁数量
     * @return 物品栈
     */
    private static ItemStack getInfo(final String path, final int number, final int max) {
        final ItemStack itemStack = new ItemStack(Material.valueOf(InventoryUtil.messageManager.getString(path + ".id")), 1, (short)Short.valueOf(InventoryUtil.messageManager.getString(path + ".data")));
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(InventoryUtil.messageManager.getString(path + ".name"));
        final List<String> lore = new ArrayList<String>();
        InventoryUtil.messageManager.getStringList(path + ".lore").forEach(a -> {
            a = a.replace("@number@", number + "").replace("@max@", max + "");
            lore.add(a);
        });
        itemMeta.setLore((List)lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    /**
     * 获取解锁物品
     * @param number 末影箱编号
     * @param rows 行数
     * @param payType 支付类型
     * @return 物品栈
     */
    private static ItemStack getUnlockItem(final int number, final int rows, final PayType payType) {
        final ItemStack itemStack = new ItemStack(Material.valueOf(InventoryUtil.messageManager.getString("Item.unlock-row.id." + rows)), 1, (short)Short.valueOf(InventoryUtil.messageManager.getString("Item.unlock-row.data." + rows)));
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(InventoryUtil.messageManager.getString("Item.unlock-row.name").replace("@number@", number + ""));
        final List<String> lore = new ArrayList<String>();
        final int points = (payType == PayType.MONEY) ? MeteorEnderBox.Instance.getConfig().getInt("setting.take-money." + rows) : MeteorEnderBox.Instance.getConfig().getInt("setting.take-points." + rows);
        InventoryUtil.messageManager.getStringList("Item.unlock-row.lore").forEach(a -> {
            final String paytype = ((payType == PayType.MONEY) ? InventoryUtil.messageManager.getString("economy-info.vault") : InventoryUtil.messageManager.getString("economy-info.points"));
            a = a.replace("@paytype@", paytype).replace("@rows@", rows + "").replace("@points@", points + "");
            lore.add(a);
        });
        itemMeta.setLore((List)lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    
    /**
     * 打开解锁界面
     * @param player 玩家
     * @param number 末影箱编号
     */
    public static void openUnlockGui(final Player player, final int number) {
        final UnlockInv unlockInv = new UnlockInv(number);
        final PayType payType = (number > MeteorEnderBox.Instance.getConfig().getInt("setting.points-number")) ? PayType.POINTS : PayType.MONEY;
        unlockInv.setPayType(payType);
        final Inventory inventory = Bukkit.createInventory((InventoryHolder)unlockInv, 9, InventoryUtil.messageManager.getString("title.unlock").replace("@number@", "" + number));
        player.openInventory(inventory);
        inventory.setItem(3, getUnlockItem(number, 2, payType));
        inventory.setItem(4, getUnlockItem(number, 4, payType));
        inventory.setItem(5, getUnlockItem(number, 6, payType));
        Arrays.asList(0, 1, 2, 6, 7, 8).forEach(slot -> inventory.setItem((int)slot, getFlagItem("Item.flag")));
=======
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    }
    
    /**
     * 打开末影箱
     * @param player 玩家
<<<<<<< HEAD
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
            
            final EnderBoxHolder enderBoxHolder = new EnderBoxHolder();
            enderBoxHolder.setNumber(number);
            enderBoxHolder.setTargetPlayerName(targetPlayerName);
            final Box box = playerData.getBoxMap().get(number);
            final Inventory inventory = Bukkit.createInventory((InventoryHolder)enderBoxHolder, box.getSlot(), InventoryUtil.messageManager.getString("title.box").replace("@number@", "" + number));
            
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
=======
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
