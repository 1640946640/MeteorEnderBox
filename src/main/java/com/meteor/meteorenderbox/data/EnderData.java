package com.meteor.meteorenderbox.data;

import com.meteor.meteorenderbox.*;
import com.meteor.meteorenderbox.lib.mysql.*;
import com.meteor.meteorenderbox.lib.message.*;

import java.util.*;
import org.bukkit.plugin.java.*;
import org.bukkit.configuration.file.*;
import com.meteor.meteorenderbox.lib.mysql.column.*;
import java.nio.charset.*;
import java.io.*;
import org.bukkit.inventory.*;
import org.bukkit.configuration.*;
import java.sql.*;
import org.bukkit.*;
import com.meteor.meteorenderbox.util.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import com.meteor.meteorenderbox.lib.mysql.data.*;

/**
 * 末影箱数据管理类
 * 负责玩家数据的加载、保存和管理
 */
public class EnderData
{
    /** 插件实例 */
    MeteorEnderBox plugin;
    /** 末影箱数据列表 */
    List<BoxData> boxDataList;
    /** 玩家数据映射，键为玩家名称 */
    Map<String, PlayerData> playerDataMap;
    /** MySQL存储实例 */
    FastMySQLStorage fastMySQLStorage;
    /** 消息管理器 */
    MessageManager messageManager;
    
    /**
     * 获取末影箱数据列表
     * @return 末影箱数据列表
     */
    public List<BoxData> getBoxDataList() {
        return this.boxDataList;
    }
    
    /**
     * 获取玩家数据映射
     * @return 玩家数据映射
     */
    public Map<String, PlayerData> getPlayerDataMap() {
        return this.playerDataMap;
    }
    
    /**
     * 获取消息管理器
     * @return 消息管理器
     */
    public MessageManager getMessageManager() {
        return this.messageManager;
    }
    
    /**
     * 设置消息管理器
     * @param messageManager 消息管理器
     */
    public void setMessageManager(final MessageManager messageManager) {
        this.messageManager = messageManager;
    }
    
    /**
     * 构造函数
     * @param plugin 插件实例
     */
    public EnderData(final MeteorEnderBox plugin) {
        this.boxDataList = new ArrayList<BoxData>();
        this.playerDataMap = new HashMap<String, PlayerData>();
        this.plugin = plugin;
        
        // 初始化MySQL存储
        (this.fastMySQLStorage = new FastMySQLStorage((JavaPlugin)this.plugin, this.plugin.getConfig().getConfigurationSection("mysql-info"))).enable();
        
        // 初始化消息管理器，根据服务器版本选择配置文件
        this.messageManager = new MessageManager(YamlConfiguration.loadConfiguration(this.getVersionedMessageFile()), true);
        
        // 创建数据库表
        final Column player = Column.of("player", Column.Char.CHAR, true, new int[] { 25 });
        final Column data = Column.of("data", Column.Bob.LONGBLOB);
        final Column number = Column.of("number", Column.Integer.INT, new int[] { 100 });
        final Column lock = Column.of("knit", Column.Integer.INT, new int[] { 2 });
        final Column[] columns = { player, data, number, lock };
        this.fastMySQLStorage.createTable("enderbox", columns);
        
        // 加载末影箱数据
        this.loadBoxData();
    }
    
    /**
     * 重新加载配置
     */
    public void reloadConfig() {
        this.plugin.saveDefaultConfig();
        this.plugin.reloadConfig();
        final MessageManager messageManager = new MessageManager(YamlConfiguration.loadConfiguration(this.getVersionedMessageFile()), true);
        this.setMessageManager(messageManager);
        this.loadBoxData();
    }
    
    /**
     * 加载末影箱数据
     */
    private void loadBoxData() {
        if (this.boxDataList != null || !this.boxDataList.isEmpty()) {
            this.boxDataList.clear();
        }
        final int max = this.plugin.getConfig().getInt("setting.max-amount");
        final int points = this.plugin.getConfig().getInt("setting.points-number");
        for (int i = 0; i < max; ++i) {
            final PayType payType = (i > points) ? PayType.POINTS : PayType.MONEY;
            final BoxData boxData = new BoxData(i + 1, payType);
            this.boxDataList.add(boxData);
        }
    }
    
    /**
     * 从数据库获取玩家数据的二进制流
     * @param player 玩家名称
     * @return 二进制输入流
     */
    private ByteArrayInputStream getByte(final String player) {
        try {
            final PreparedStatement ps = this.fastMySQLStorage.getConnection().prepareStatement("select data from enderbox where player = ?");
            ps.setString(1, player);
            final ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return (ByteArrayInputStream)resultSet.getBinaryStream("data");
            }
        }
        catch (Throwable var4) {
            var4.printStackTrace();
        }
        return null;
    }
    
    /**
     * 从数据库获取玩家数据并转换为YamlConfiguration
     * @param player 玩家名称
     * @return YamlConfiguration对象
     */
    private YamlConfiguration getData(final String player) {
        final YamlConfiguration yamlConfiguration = new YamlConfiguration();
        final ByteArrayInputStream inputStream = this.getByte(player);
        if (inputStream != null) {
            final byte[] bytes = new byte[inputStream.available()];
            try {
                inputStream.read(bytes);
                yamlConfiguration.load((Reader)new StringReader(new String(bytes, StandardCharsets.UTF_8)));
                inputStream.close();
            }
            catch (Exception var6) {
                var6.printStackTrace();
            }
        }
        return yamlConfiguration;
    }
    
    /**
     * 从YamlConfiguration获取末影箱映射
     * @param yml YamlConfiguration对象
     * @return 末影箱映射
     */
    private Map<Integer, Box> getBoxMap(final YamlConfiguration yml) {
        final Map<Integer, Box> boxMap = new HashMap<Integer, Box>();
        yml.getKeys(false).forEach(a -> {
            final Box box = new Box();
            box.setNumber(Integer.valueOf(a));
            final Map<Integer, ItemStack> itemStackMap = new HashMap<Integer, ItemStack>();
            if (yml.getConfigurationSection(a + ".items") != null) {
                final ConfigurationSection items = yml.getConfigurationSection(a + ".items");
                items.getKeys(false).forEach(item -> itemStackMap.put(Integer.valueOf(item), items.getItemStack(item)));
            }
            box.setMap(itemStackMap);
            box.setSlot(yml.getInt(a + ".rows") * 9);
            boxMap.put(Integer.valueOf(a), box);
        });
        return boxMap;
    }
    
    /**
     * 锁定玩家数据
     * @param player 玩家名称
     */
    private void lockData(final String player) {
        PreparedStatement ps = null;
        try {
            ps = this.fastMySQLStorage.getConnection().prepareStatement("update enderbox set knit = 1 where player = ?");
            ps.setString(1, player);
            ps.execute();
        }
        catch (Throwable var12) {
            var12.printStackTrace();
        }
        finally {
            // 确保资源关闭
            if (ps != null) {
                try {
                    ps.close();
                }
                catch (SQLException var14) {
                    var14.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 获取玩家数据
     * @param player 玩家名称
     * @return 玩家数据
     */
    private PlayerData getPlayerData(final String player) {
        final PlayerData playerData = new PlayerData();
        playerData.setPlayer(player);
        playerData.setNumber(0);
        playerData.setBoxMap(new HashMap<Integer, Box>());
        
        if (this.fastMySQLStorage.isExists("enderbox", "player", (Object)player)) {
            // 减少数据库查询次数，只查询一次数据
            final YamlConfiguration data = this.getData(player);
            final Map<Integer, Box> boxMap = this.getBoxMap(data);
            playerData.setBoxMap(boxMap);
            playerData.setNumber((int)this.fastMySQLStorage.get("enderbox", "player", (Object)player, "number"));
            this.lockData(player);
        }
        return playerData;
    }
    
    /**
     * 加载玩家数据
     * @param player 玩家名称
     */
    public void loadPlayerData(final String player) {
        final Player p = Bukkit.getPlayerExact(player);
        // 统一使用异步操作，避免阻塞主线程
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, () -> {
            // 无论玩家数据是否存在，都异步加载
            this.getPlayerDataMap().put(player, this.getPlayerData(player));
            final Player player2 = Bukkit.getPlayerExact(player);
            if (player2 != null) {
                // 回到主线程执行UI操作
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                    if (this.fastMySQLStorage.isExists("enderbox", "player", (Object)player)) {
                        player2.sendMessage(this.messageManager.getString("mes.load-data"));
                    }
                    player2.sendMessage(this.messageManager.getString("mes.sur-load"));
                    InventoryUtil.openInv(player2, 1);
                });
            }
        });
    }
    
    /**
     * 解锁末影箱
     * @param playerData 玩家数据
     * @param number 末影箱编号
     * @param row 行数
     */
    public void unLockBox(final PlayerData playerData, final int number, final int row) {
        final Box box = new Box();
        box.setNumber(number);
        box.setSlot(row * 9);
        playerData.setNumber(number);
        playerData.unLockBox(number, box);
        InventoryUtil.openInv(Bukkit.getPlayer(playerData.getPlayer()), 1);
    }
    
    /**
     * 将玩家数据转换为二进制流
     * @param playerData 玩家数据
     * @return 二进制输入流
     */
    public ByteArrayInputStream getByte(final PlayerData playerData) {
        final YamlConfiguration yamlConfiguration = new YamlConfiguration();
        playerData.getBoxMap().values().forEach(a -> {
            yamlConfiguration.set(a.getNumber() + ".rows", (Object)(a.getSlot() / 9));
            a.getMap().keySet().forEach(i -> yamlConfiguration.set(a.getNumber() + ".items." + i, (Object)a.getMap().get(i)));
        });
        return new ByteArrayInputStream(yamlConfiguration.saveToString().getBytes(StandardCharsets.UTF_8));
    }
    
    /**
     * 保存玩家数据到数据库
     * @param playerData 玩家数据
     */
    private void save(final PlayerData playerData) {
        try {
            final KeyValue player = new KeyValue("player", (Object)playerData.getPlayer());
            final ByteArrayInputStream byteArrayInputStream = this.getByte(playerData);
            final KeyValue data = new KeyValue("data", (Object)byteArrayInputStream);
            final KeyValue number = new KeyValue("number", (Object)playerData.getNumber());
            final KeyValue lock = new KeyValue("knit", (Object)0);
            final KeyValue[] keyValues = { player, data, number, lock };
            this.fastMySQLStorage.put("enderbox", keyValues);
            byteArrayInputStream.close();
        }
        catch (Exception var8) {
            this.plugin.getLogger().info(playerData.getPlayer() + "\u4ed3\u5e93\u6570\u636e\u4fdd\u5b58\u5931\u8d25.");
        }
    }
    
    /**
     * 保存玩家数据
     * @param playerData 玩家数据
     * @param stopsave 是否立即保存
     */
    public void saveData(final PlayerData playerData, final boolean stopsave) {
        // 统一使用异步操作，避免阻塞主线程
        Bukkit.getScheduler().runTaskAsynchronously((Plugin)this.plugin, () -> {
            this.save(playerData);
            if (!stopsave) {
                this.plugin.getLogger().info("\u5df2\u4fdd\u5b58" + playerData.getPlayer() + "\u4ed3\u5e93\u4fe1\u606f.\u5e76\u89e3\u9501\u6570\u636e");
                this.getPlayerDataMap().remove(playerData.getPlayer());
            }
        });
    }
    
    /**
     * 获取消息配置文件
     * @return 消息配置文件
     */
    private File getVersionedMessageFile() {
        // 直接返回 message.yml 文件，因为我们已经在 saveDefaultConfig 中根据版本选择了正确的配置文件
        File messageFile = new File(plugin.getDataFolder() + "/message.yml");
        return messageFile;
    }
    
    /**
     * 关闭数据库连接
     */
    public void close() {
        this.fastMySQLStorage.disable();
    }
}