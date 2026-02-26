package com.meteor.meteorenderbox.cmd;

import com.meteor.meteorenderbox.*;
import com.meteor.meteorenderbox.data.*;
import com.meteor.meteorenderbox.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;
import java.util.List;

/**
 * 末影箱命令执行类
 * 处理玩家和管理员的命令请求
 */
public class EnderBoxCmds implements CommandExecutor
{
    /** 插件实例 */
    MeteorEnderBox plugin;
    
    /**
     * 构造函数
     * @param plugin 插件实例
     */
    public EnderBoxCmds(final MeteorEnderBox plugin) {
        this.plugin = plugin;
    }
    
    /**
     * 执行命令
     * @param sender 命令发送者
     * @param command 命令
     * @param label 命令标签
     * @param args 命令参数
     * @return 是否执行成功
     */
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        // 检查命令发送者是否为玩家
        if (!(sender instanceof Player)) {
            sender.sendMessage("只能由玩家执行此命令");
            return true;
        }
        
        final Player player = (Player)sender;
        
        // 处理不同的命令参数
        if (args.length == 0) {
            // 没有参数，显示帮助信息
            this.showHelp(player);
            return true;
        }
        
        // 处理 open 命令
        if (args[0].equalsIgnoreCase("open")) {
            // 检查是否有管理员权限打开其他玩家的末影箱
            if (args.length >= 2 && player.hasPermission("meteor.enderbox.admin")) {
                final String targetPlayerName = args[1];
                
                // 直接加载目标玩家的末影箱数据（无论在线与否）
                this.plugin.getEnderData().loadPlayerData(targetPlayerName);
                // 延迟一下确保数据加载完成，然后打开目标玩家的末影箱列表界面给管理员
                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                    InventoryUtil.openInv(player, targetPlayerName, 1);
                }, 20L); // 1 second delay
                player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " 正在打开玩家 " + targetPlayerName + " 的末影箱...");
                return true;
            }
            
            // 普通玩家打开自己的末影箱
            this.plugin.getEnderData().loadPlayerData(player.getName());
            return true;
        }
        
        // 处理 reload 命令
        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("meteor.enderbox.admin")) {
                player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("mes.no-permission"));
                return true;
            }
            
            this.plugin.getEnderData().reloadConfig();
            player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("mes.reload"));
            return true;
        }
        
        // 处理 help 命令
        if (args[0].equalsIgnoreCase("help")) {
            this.showHelp(player);
            return true;
        }
        
        // 未知命令，显示帮助信息
        this.showHelp(player);
        return true;
    }
    
    /**
     * 显示帮助信息
     * @param player 玩家
     */
    private void showHelp(final Player player) {
        // 直接硬编码帮助信息，确保显示完整的命令列表
        player.sendMessage("&a&lMeteorEnderBox | &f随身末影箱");
        player.sendMessage("&c /enderbox open &7打开随身末影箱");
        player.sendMessage("&c /enderbox reload &7重载配置文件");
        
        // 如果玩家有管理员权限，显示管理员帮助信息
        if (player.hasPermission("meteor.enderbox.admin")) {
            player.sendMessage("&c /enderbox open <玩家名> &7打开指定玩家的末影箱");
        }
    }
}
