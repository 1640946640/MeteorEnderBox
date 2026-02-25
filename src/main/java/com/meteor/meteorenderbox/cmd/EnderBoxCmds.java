package com.meteor.meteorenderbox.cmd;

import com.meteor.meteorenderbox.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.meteor.meteorenderbox.util.*;
import org.bukkit.*;

/**
 * 末影箱命令执行器
 * 处理enderbox命令的各种子命令
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
     * 处理命令执行
     * @param sender 命令发送者
     * @param command 命令对象
     * @param label 命令标签
     * @param args 命令参数
     * @return 是否成功执行
     */
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        // 处理help命令
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            this.plugin.getEnderData().getMessageManager().getStringList("mes.help").forEach(a -> sender.sendMessage(a));
            // 如果是管理员，显示管理员帮助
            if (sender.isOp()) {
                this.plugin.getEnderData().getMessageManager().getStringList("mes.admin-help").forEach(b -> sender.sendMessage(b));
            }
            return true;
        }
        
        // 处理open命令，打开末影箱
        if (args[0].equalsIgnoreCase("open")) {
            // 管理员打开其他玩家的末影箱
            if (args.length == 2 && sender.isOp()) {
                final String targetPlayerName = args[1];
                final Player senderPlayer = (sender instanceof Player) ? (Player)sender : null;
                
                // 加载目标玩家的数据
                if (!this.plugin.getEnderData().getPlayerDataMap().containsKey(targetPlayerName)) {
                    this.plugin.getEnderData().loadPlayerData(targetPlayerName);
                }
                
                // 打开目标玩家的末影箱界面
                if (senderPlayer != null) {
                    InventoryUtil.openInv(senderPlayer, targetPlayerName, 1);
                    sender.sendMessage("已打开玩家 " + targetPlayerName + " 的末影箱");
                }
                return true;
            }
            // 普通玩家打开自己的末影箱
            else if (args.length == 1 && sender instanceof Player) {
                final Player player = (Player)sender;
                final String playerName = player.getName();
                // 如果玩家数据不存在，加载数据
                if (!this.plugin.getEnderData().getPlayerDataMap().containsKey(playerName)) {
                    this.plugin.getEnderData().loadPlayerData(playerName);
                }
                else {
                    // 打开末影箱界面
                    InventoryUtil.openInv(player, 1);
                }
                return true;
            }
        }
        
        // 处理reload命令，重新加载配置
        if (args[0].equalsIgnoreCase("reload") && args.length == 1 && sender.isOp()) {
            // 关闭所有在线玩家的 inventory
            Bukkit.getOnlinePlayers().forEach(a -> Bukkit.getOnlinePlayers().forEach(player -> player.closeInventory()));
            // 重新加载配置
            this.plugin.getEnderData().reloadConfig();
            sender.sendMessage(this.plugin.getEnderData().getMessageManager().getString("mes.reload"));
            this.plugin.reloadConfig();
        }
        
        return false;
    }
}
