package com.meteor.meteorenderbox.cmd;

import com.meteor.meteorenderbox.*;
import com.meteor.meteorenderbox.data.*;
import com.meteor.meteorenderbox.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

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
            player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.help"));
            player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.help-1"));
            if (player.hasPermission("meteor.enderbox.admin")) {
                player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.help-2"));
            }
            return true;
        }
        
        // 处理 open 命令
        if (args[0].equalsIgnoreCase("open")) {
            // 检查是否有管理员权限打开其他玩家的末影箱
            if (args.length >= 2 && player.hasPermission("meteor.enderbox.admin")) {
                final String targetPlayerName = args[1];
                final Player targetPlayer = Bukkit.getPlayerExact(targetPlayerName);
                
                if (targetPlayer == null) {
                    player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.no-player"));
                    return true;
                }
                
                // 加载目标玩家的末影箱数据并打开
                this.plugin.getEnderData().loadPlayerData(targetPlayerName);
                player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + "正在打开玩家 " + targetPlayerName + " 的末影箱...");
                return true;
            }
            
            // 普通玩家打开自己的末影箱
            this.plugin.getEnderData().loadPlayerData(player.getName());
            return true;
        }
        
        // 处理 reload 命令
        if (args[0].equalsIgnoreCase("reload")) {
            if (!player.hasPermission("meteor.enderbox.admin")) {
                player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.no-permission"));
                return true;
            }
            
            this.plugin.getEnderData().reloadConfig();
            player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.reload"));
            return true;
        }
        
        // 处理 help 命令
        if (args[0].equalsIgnoreCase("help")) {
            player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.help"));
            player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.help-1"));
            if (player.hasPermission("meteor.enderbox.admin")) {
                player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.help-2"));
            }
            return true;
        }
        
        // 未知命令，显示帮助信息
        player.sendMessage(this.plugin.getEnderData().getMessageManager().getString("prefix") + " " + this.plugin.getEnderData().getMessageManager().getString("mes.help"));
        return true;
    }
}