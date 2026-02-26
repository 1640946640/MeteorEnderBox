package com.meteor.meteorenderbox.events;

import com.meteor.meteorenderbox.*;
import com.meteor.meteorenderbox.data.*;
import com.meteor.meteorenderbox.data.holder.*;
import com.meteor.meteorenderbox.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import java.util.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;

/**
 * Ender Box event listener
 * Handles player open, close ender box events
 */
public class EnderBoxEvents implements Listener
{
    /** Plugin instance */
    MeteorEnderBox plugin;
    
    /**
     * Constructor
     * @param plugin Plugin instance
     */
    public EnderBoxEvents(final MeteorEnderBox plugin) {
        this.plugin = plugin;
    }
    
    /**
     * Listen for player close inventory event
     * @param event Close inventory event
     */
    @EventHandler
    public void closeInvSaveData(final InventoryCloseEvent event) {
        // Check if it's ender box inventory
        if (event.getInventory().getHolder() instanceof EnderBoxHolder) {
            final EnderBoxHolder holder = (EnderBoxHolder)event.getInventory().getHolder();
            final Player player = (Player)event.getPlayer();
            
            // Get target player name from holder
            String targetPlayerName = holder.getTargetPlayerName();
            if (targetPlayerName == null) {
                targetPlayerName = player.getName();
            }
            
            // Check if player data exists
            if (this.plugin.getEnderData().getPlayerDataMap().containsKey(targetPlayerName)) {
                final PlayerData playerData = this.plugin.getEnderData().getPlayerDataMap().get(targetPlayerName);
                final Box box = playerData.getBoxMap().get(holder.getNumber());
                
                // Save items to ender box
                final ItemStack[] contents = event.getInventory().getContents();
                for (int i = 0; i < contents.length; ++i) {
                    box.getMap().put(i, contents[i]);
                }
                
                // Save data
                this.plugin.getEnderData().saveData(playerData, false);
                player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("mes.save-data"));
            }
        }
    }
    
    /**
     * Listen for player quit game event
     * @param event Player quit event
     */
    @EventHandler
    public void quitGame(final PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        // Check if player data exists
        if (this.plugin.getEnderData().getPlayerDataMap().containsKey(player.getName())) {
            final PlayerData playerData = this.plugin.getEnderData().getPlayerDataMap().get(player.getName());
            // Save data
            this.plugin.getEnderData().saveData(playerData, true);
        }
    }
    
    /**
     * Listen for player join game event
     * @param event Player join event
     */
    @EventHandler
    public void joinGame(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        // Reset player's lock status to ensure player can open ender box normally
        // Additional initialization logic can be added here
    }
    
    /**
     * Listen for player click inventory event
     * @param event Inventory click event
     */
    @EventHandler
    public void click(final InventoryClickEvent event) {
        // Check if it's ender box inventory, using holder type check instead of getTitle() method
        if (event.getInventory().getHolder() instanceof EnderBoxHolder) {
            final EnderBoxHolder holder = (EnderBoxHolder)event.getInventory().getHolder();
            final Player player = (Player)event.getWhoClicked();
            final InventoryAction action = event.getAction();
            final ClickType clickType = event.getClick();
            final int slot = event.getSlot();
            
            // Log click event for debugging
            plugin.getLogger().info("Player " + player.getName() + " clicked on ender box " + holder.getNumber() + " at slot " + slot);
            plugin.getLogger().info("Action: " + action + ", ClickType: " + clickType);
            
            // Check if player has permission to interact with this ender box
            if (!player.getName().equals(holder.getTargetPlayerName()) && !player.hasPermission("enderbox.admin")) {
                player.sendMessage(plugin.getEnderData().getMessageManager().getString("mes.no-permission"));
                event.setCancelled(true);
                return;
            }
            
            // Allow all normal inventory operations
            // The event is not cancelled, so players can add, remove, and rearrange items
            plugin.getLogger().info("Inventory interaction allowed for player " + player.getName());
        }
        // Also handle ender box list inventory clicks
        else if (event.getInventory().getHolder() instanceof EnderBoxListHolder) {
            final EnderBoxListHolder holder = (EnderBoxListHolder)event.getInventory().getHolder();
            final Player player = (Player)event.getWhoClicked();
            final int slot = event.getSlot();
            
            // Log click event for debugging
            plugin.getLogger().info("Player " + player.getName() + " clicked on ender box list page " + holder.getPage() + " at slot " + slot);
            
            // Calculate box number from slot
            int boxNumber = (holder.getPage() - 1) * 45 + slot + 1;
            
            // Check if box number is valid
            if (boxNumber <= plugin.getEnderData().getBoxDataList().size()) {
                plugin.getLogger().info("Player " + player.getName() + " attempting to open ender box " + boxNumber);
                
                // Get target player name from holder
                final String targetPlayerName = holder.getTargetPlayerName() != null ? holder.getTargetPlayerName() : player.getName();
                
                // Get player data
                PlayerData playerData = plugin.getEnderData().getPlayerDataMap().get(targetPlayerName);
                if (playerData != null) {
                    // Check if box is unlocked
                    if (playerData.getBoxMap().containsKey(boxNumber)) {
                        plugin.getLogger().info("Ender box " + boxNumber + " is unlocked for player " + targetPlayerName);
                        
                        // Check if it's a right-click (for admin to lock)
                        if (event.getClick() == ClickType.RIGHT && player.hasPermission("enderbox.admin")) {
                            // Allow locking for admins regardless of how the ender box was opened
                            if (player.hasPermission("enderbox.admin")) {
                                // Check if the box is empty
                                Box box = playerData.getBoxMap().get(boxNumber);
                                boolean isEmpty = true;
                                for (ItemStack item : box.getMap().values()) {
                                    if (item != null) {
                                        isEmpty = false;
                                        break;
                                    }
                                }
                                
                                if (isEmpty) {
                                    // Show lock confirmation GUI
                                    showLockConfirmationGUI(player, playerData, boxNumber, targetPlayerName);
                                } else {
                                    player.sendMessage(plugin.getEnderData().getMessageManager().getString("prefix") + " §7无法封锁末影箱 " + boxNumber + "，因为里面还有物品");
                                }
                            } else {
                                // Not opened with /enderbox open <player> command, open the box instead
                                InventoryUtil.openBox(player, playerData, boxNumber, targetPlayerName);
                            }
                            event.setCancelled(true);
                        } else {
                            // Left-click to open the box
                            InventoryUtil.openBox(player, playerData, boxNumber, targetPlayerName);
                            event.setCancelled(true);
                        }
                    } else {
                        plugin.getLogger().info("Ender box " + boxNumber + " is locked for player " + targetPlayerName);
                        // Try to unlock the box
                        this.tryUnlockBox(player, playerData, boxNumber);
                        event.setCancelled(true);
                    }
                } else {
                    plugin.getLogger().warning("Player data not found for " + targetPlayerName + ", reloading...");
                    // Reload player data
                    plugin.getEnderData().loadPlayerData(targetPlayerName);
                    // Wait a bit for data to load
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        InventoryUtil.openInv(player, targetPlayerName, 1);
                    }, 20L);
                    event.setCancelled(true);
                }
            } else if (slot == 45) {
                // Previous page button
                int prevPage = holder.getPage() - 1;
                if (prevPage > 0) {
                    plugin.getLogger().info("Player " + player.getName() + " navigating to previous page " + prevPage);
                    InventoryUtil.openInv(player, prevPage);
                    event.setCancelled(true);
                }
            } else if (slot == 53) {
                // Next page button
                int nextPage = holder.getPage() + 1;
                int maxPage = (plugin.getEnderData().getBoxDataList().size() + 44) / 45;
                if (nextPage <= maxPage) {
                    plugin.getLogger().info("Player " + player.getName() + " navigating to next page " + nextPage);
                    InventoryUtil.openInv(player, nextPage);
                    event.setCancelled(true);
                }
            }
        }
        // Handle payment method selection GUI
        else if (event.getView().getTitle().equals("选择支付方式")) {
            final Player player = (Player)event.getWhoClicked();
            final int slot = event.getSlot();
            
            // Get payment context
            PaymentContext context = paymentSelectionContext.get(player.getUniqueId());
            if (context == null) {
                player.sendMessage(plugin.getEnderData().getMessageManager().getString("prefix") + " §7支付上下文丢失，请重新尝试");
                event.setCancelled(true);
                return;
            }
            
            // Handle payment selection
            if (slot == 11) {
                // Money payment
                handlePayment(player, context, PayType.MONEY);
            } else if (slot == 15) {
                // Points payment
                handlePayment(player, context, PayType.POINTS);
            }
            
            event.setCancelled(true);
        }
        // Handle lock confirmation GUI
        else if (event.getView().getTitle().equals("确认封锁末影箱")) {
            final Player player = (Player)event.getWhoClicked();
            final int slot = event.getSlot();
            
            // Handle lock confirmation
            handleLockConfirmation(player, slot);
            
            event.setCancelled(true);
        }
    }
    
    /**
     * Handle payment processing
     * @param player Player (the admin who is performing the unlock)
     * @param context Payment context
     * @param payType Payment type
     */
    private void handlePayment(final Player player, final PaymentContext context, final PayType payType) {
        // Use the admin's name for payment (the one who clicked to unlock)
        String payerName = player.getName();
        boolean success = false;
        int cost = 0;
        
        // Process payment based on selected type
        if (payType == PayType.MONEY) {
            cost = context.moneyCost;
            success = ApiManager.takeMoney(payerName, cost);
        } else {
            cost = context.pointsCost;
            success = ApiManager.takePoints(payerName, cost);
        }
        
        // If payment successful, unlock the box
        if (success) {
            // Unlock the box for the target player
            plugin.getEnderData().unLockBox(player, context.playerData, context.boxNumber, context.rows);
            // Save the data immediately
            plugin.getEnderData().saveData(context.playerData, false);
            // Send success message to the admin
            player.sendMessage(plugin.getEnderData().getMessageManager().getString("mes.unlock-box").replace("@number@", String.valueOf(context.boxNumber)));
        } else {
            // Send failure message to the admin
            if (payType == PayType.MONEY) {
                player.sendMessage(plugin.getEnderData().getMessageManager().getString("mes.no-money"));
            } else {
                player.sendMessage(plugin.getEnderData().getMessageManager().getString("mes.no-points"));
            }
        }
        
        // Remove context
        paymentSelectionContext.remove(player.getUniqueId());
    }
    
    /**
     * Try to unlock an ender box
     * @param player Player
     * @param playerData Player data
     * @param boxNumber Box number
     */
    private void tryUnlockBox(final Player player, final PlayerData playerData, final int boxNumber) {
        // Get box data
        BoxData boxData = null;
        for (BoxData data : plugin.getEnderData().getBoxDataList()) {
            if (data.getNumber() == boxNumber) {
                boxData = data;
                break;
            }
        }
        
        if (boxData == null) {
            player.sendMessage(plugin.getEnderData().getMessageManager().getString("prefix") + " §7末影箱不存在");
            return;
        }
        
        // Determine rows based on box number
        int rows = 2; // Default rows
        if (boxNumber <= 10) {
            rows = 2;
        } else if (boxNumber <= 15) {
            rows = 4;
        } else {
            rows = 6;
        }
        
        // Show payment method selection GUI
        showPaymentSelectionGUI(player, playerData, boxNumber, rows);
    }
    
    /**
     * Show payment method selection GUI
     * @param player Player
     * @param playerData Player data
     * @param boxNumber Box number
     * @param rows Rows of the ender box
     */
    private void showPaymentSelectionGUI(final Player player, final PlayerData playerData, final int boxNumber, final int rows) {
        // Create inventory for payment selection
        Inventory inv = Bukkit.createInventory(null, 27, "选择支付方式");
        
        // Get costs for both payment types
        int moneyCost = plugin.getConfig().getInt("setting.take-money." + rows, 1000);
        int pointsCost = plugin.getConfig().getInt("setting.take-points." + rows, 100);
        
        // Create money payment item
        ItemStack moneyItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta moneyMeta = moneyItem.getItemMeta();
        moneyMeta.setDisplayName("§e金币支付");
        List<String> moneyLore = new ArrayList<>();
        moneyLore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        moneyLore.add("§6末影箱编号: §e" + boxNumber);
        moneyLore.add("§6容量: §e" + rows + " 行");
        moneyLore.add("§6价格: §e" + moneyCost + " 金币");
        moneyLore.add("§6支付方式: §e金币");
        moneyLore.add("");
        moneyLore.add("§6点击支付: §7立即解锁末影箱");
        moneyLore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        moneyMeta.setLore(moneyLore);
        moneyItem.setItemMeta(moneyMeta);
        inv.setItem(11, moneyItem);
        
        // Create points payment item
        ItemStack pointsItem = new ItemStack(Material.EMERALD);
        ItemMeta pointsMeta = pointsItem.getItemMeta();
        pointsMeta.setDisplayName("§c点券支付");
        List<String> pointsLore = new ArrayList<>();
        pointsLore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        pointsLore.add("§6末影箱编号: §e" + boxNumber);
        pointsLore.add("§6容量: §e" + rows + " 行");
        pointsLore.add("§6价格: §c" + pointsCost + " 点券");
        pointsLore.add("§6支付方式: §c点券");
        pointsLore.add("");
        pointsLore.add("§6点击支付: §7立即解锁末影箱");
        pointsLore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        pointsMeta.setLore(pointsLore);
        pointsItem.setItemMeta(pointsMeta);
        inv.setItem(15, pointsItem);
        
        // Open the inventory
        player.openInventory(inv);
        
        // Store the context for later use
        paymentSelectionContext.put(player.getUniqueId(), new PaymentContext(playerData, boxNumber, rows, moneyCost, pointsCost));
    }
    
    // Context class to store payment selection data
    private static class PaymentContext {
        PlayerData playerData;
        int boxNumber;
        int rows;
        int moneyCost;
        int pointsCost;
        
        public PaymentContext(PlayerData playerData, int boxNumber, int rows, int moneyCost, int pointsCost) {
            this.playerData = playerData;
            this.boxNumber = boxNumber;
            this.rows = rows;
            this.moneyCost = moneyCost;
            this.pointsCost = pointsCost;
        }
    }
    
    // Map to store payment selection context
    private Map<UUID, PaymentContext> paymentSelectionContext = new HashMap<>();
    
    // Map to store lock confirmation context
    private Map<UUID, LockConfirmationContext> lockConfirmationContext = new HashMap<>();
    
    /**
     * Show lock confirmation GUI
     * @param player Player
     * @param playerData Player data
     * @param boxNumber Box number
     * @param targetPlayerName Target player name
     */
    private void showLockConfirmationGUI(final Player player, final PlayerData playerData, final int boxNumber, final String targetPlayerName) {
        // Create inventory for lock confirmation
        Inventory inv = Bukkit.createInventory(null, 27, "确认封锁末影箱");
        
        // Create confirm item
        ItemStack confirmItem = new ItemStack(Material.EMERALD_BLOCK);
        ItemMeta confirmMeta = confirmItem.getItemMeta();
        confirmMeta.setDisplayName("§a确认封锁");
        List<String> confirmLore = new ArrayList<>();
        confirmLore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        confirmLore.add("§6末影箱编号: §e" + boxNumber);
        confirmLore.add("§6目标玩家: §e" + targetPlayerName);
        confirmLore.add("");
        confirmLore.add("§6点击确认: §c封锁此末影箱");
        confirmLore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        confirmMeta.setLore(confirmLore);
        confirmItem.setItemMeta(confirmMeta);
        inv.setItem(11, confirmItem);
        
        // Create cancel item
        ItemStack cancelItem = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta cancelMeta = cancelItem.getItemMeta();
        cancelMeta.setDisplayName("§c取消操作");
        List<String> cancelLore = new ArrayList<>();
        cancelLore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        cancelLore.add("§6末影箱编号: §e" + boxNumber);
        cancelLore.add("§6目标玩家: §e" + targetPlayerName);
        cancelLore.add("");
        cancelLore.add("§6点击取消: §a取消封锁操作");
        cancelLore.add("§7━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        cancelMeta.setLore(cancelLore);
        cancelItem.setItemMeta(cancelMeta);
        inv.setItem(15, cancelItem);
        
        // Open the inventory
        player.openInventory(inv);
        
        // Store the context for later use
        lockConfirmationContext.put(player.getUniqueId(), new LockConfirmationContext(playerData, boxNumber, targetPlayerName));
    }
    
    /**
     * Handle lock confirmation GUI click
     * @param player Player
     * @param slot Slot clicked
     */
    private void handleLockConfirmation(final Player player, final int slot) {
        // Get lock context
        LockConfirmationContext context = lockConfirmationContext.get(player.getUniqueId());
        if (context == null) {
            player.sendMessage(plugin.getEnderData().getMessageManager().getString("prefix") + " §7操作上下文丢失，请重新尝试");
            return;
        }
        
        // Handle confirmation
        if (slot == 11) {
            // Confirm lock
            Box box = context.playerData.getBoxMap().get(context.boxNumber);
            boolean isEmpty = true;
            for (ItemStack item : box.getMap().values()) {
                if (item != null) {
                    isEmpty = false;
                    break;
                }
            }
            
            if (isEmpty) {
                // Lock the box
                context.playerData.getBoxMap().remove(context.boxNumber);
                // Save the data
                plugin.getEnderData().saveData(context.playerData, false);
                // Refresh the inventory
                InventoryUtil.openInv(player, context.targetPlayerName, 1);
                player.sendMessage(plugin.getEnderData().getMessageManager().getString("prefix") + " §7已封锁玩家 §e" + context.targetPlayerName + " §7的末影箱 " + context.boxNumber);
            } else {
                player.sendMessage(plugin.getEnderData().getMessageManager().getString("prefix") + " §7无法封锁末影箱 " + context.boxNumber + "，因为里面还有物品");
            }
        } else if (slot == 15) {
            // Cancel lock
            player.sendMessage(plugin.getEnderData().getMessageManager().getString("prefix") + " §7已取消封锁操作");
            // Return to ender box list
            InventoryUtil.openInv(player, context.targetPlayerName, 1);
        }
        
        // Remove context
        lockConfirmationContext.remove(player.getUniqueId());
    }
    
    // Context class to store lock confirmation data
    private static class LockConfirmationContext {
        PlayerData playerData;
        int boxNumber;
        String targetPlayerName;
        
        public LockConfirmationContext(PlayerData playerData, int boxNumber, String targetPlayerName) {
            this.playerData = playerData;
            this.boxNumber = boxNumber;
            this.targetPlayerName = targetPlayerName;
        }
    }
}