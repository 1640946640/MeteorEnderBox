package com.meteor.meteorenderbox.lib.mysql;

import org.bukkit.plugin.java.*;
import org.bukkit.configuration.*;
import org.bukkit.scheduler.*;
import org.bukkit.plugin.*;
import com.meteor.meteorenderbox.lib.mysql.column.*;
import java.util.*;
import com.meteor.meteorenderbox.lib.mysql.data.*;
import java.sql.*;

/**
 * MySQL存储实现类
 * 用于处理数据库连接和操作
 */
public class FastMySQLStorage implements IStorage
{
    /** 插件实例 */
    private JavaPlugin plugin;
    /** 数据库IP */
    private String ip;
    /** 数据库端口 */
    private int port;
    /** 数据库参数 */
    private String param;
    /** 数据库用户名 */
    private String user;
    /** 数据库密码 */
    private String password;
    /** 数据库名称 */
    private String database;
    /** 数据库连接 */
    private Connection connection;
    /** 配置部分 */
    private ConfigurationSection section;
    /** 连接检查任务 */
    private BukkitRunnable check;
    
    /**
     * 构造函数
     * @param plugin 插件实例
     * @param section 配置部分
     */
    public FastMySQLStorage(final JavaPlugin plugin, final ConfigurationSection section) {
        this(plugin, section.getString("ip"), section.getInt("port", 3306), section.getString("param", ""), section.getString("user", "root"), section.getString("password", "root"), section.getString("database"), section);
    }
    
    /**
     * 构造函数
     * @param plugin 插件实例
     * @param ip 数据库IP
     * @param port 数据库端口
     * @param param 数据库参数
     * @param user 数据库用户名
     * @param password 数据库密码
     * @param database 数据库名称
     * @param section 配置部分
     */
    private FastMySQLStorage(final JavaPlugin plugin, final String ip, final int port, final String param, final String user, final String password, final String database, final ConfigurationSection section) {
        this.plugin = plugin;
        this.ip = ip;
        this.port = port;
        this.param = param;
        this.user = user;
        this.password = password;
        this.database = database;
        this.section = section;
    }
    
    @Override
    public void enable() {
        try {
            this.connect();
        } catch (Throwable throwable) {
            this.plugin.getLogger().info("数据库初始连接失败,请检查配置文件");
            this.plugin.getServer().getPluginManager().disablePlugin((Plugin)this.plugin);
            return;
        }
        (this.check = new BukkitRunnable() {
            public void run() {
                try {
                    FastMySQLStorage.this.execute(FastMySQLStorage.this.section.getString("validSql"));
                } catch (Throwable throwable) {
                    try {
                        FastMySQLStorage.this.connect();
                    } catch (Throwable e) {
                        this.cancel();
                        FastMySQLStorage.this.plugin.getLogger().info("数据库断开连接无法重连！");
                    }
                }
            }
        }).runTaskTimerAsynchronously((Plugin)this.plugin, 0L, this.section.getLong("checkTime", 600L) * 20L);
    }
    
    /**
     * 执行SQL语句
     * @param sql SQL语句
     */
    private void execute(final String sql) {
        try {
            final PreparedStatement pst = this.getConnection().prepareStatement(sql);
            pst.execute();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public void disable() {
        try {
            this.close();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.check.cancel();
        }
    }
    
    /**
     * 关闭数据库连接
     * @throws Throwable 异常
     */
    private void close() throws Throwable {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new Throwable("数据库关闭失败", e);
        }
    }
    
    /**
     * 创建数据库表
     * @param name 表名
     * @param columns 列定义
     */
    public void createTable(final String name, final Column... columns) {
        PreparedStatement pst = null;
        try {
            final StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS `" + name + "` (");
            for (int i = 0; i < columns.length; ++i) {
                final Column column = columns[i];
                sb.append(column.getName()).append(" " + column.getType());
                if (Column.hasBracket(column.getType())) {
                    sb.append("(").append(column.toStringM()).append(")");
                }
                if (column.isPrimary()) {
                    sb.append(" PRIMARY KEY");
                }
                if (i == columns.length - 1) {
                    sb.append(")");
                } else {
                    sb.append(", ");
                }
            }
            this.plugin.getLogger().info(sb.toString());
            pst = this.getConnection().prepareStatement(sb.toString());
            pst.executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(pst);
        }
    }
    
    /**
     * 检查记录是否存在
     * @param table 表名
     * @param keyColumn 键列
     * @param searchValue 搜索值
     * @param <K> 搜索值类型
     * @return 是否存在
     */
    public <K> boolean isExists(final String table, final String keyColumn, final K searchValue) {
        final StringBuilder sb = new StringBuilder("SELECT `" + keyColumn + "` FROM `" + table + "` WHERE " + keyColumn + "=?");
        PreparedStatement pst = null;
        ResultSet set = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            pst.setObject(1, searchValue);
            set = pst.executeQuery();
            if (set.next()) {
                return true;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(set, pst);
        }
        return false;
    }
    
    /**
     * 删除记录
     * @param table 表名
     * @param keyColumn 键列
     * @param key 键值
     * @param <K> 键值类型
     */
    public <K> void delete(final String table, final String keyColumn, final K key) {
        final StringBuilder sb = new StringBuilder("DELETE FROM `" + table + "` WHERE " + keyColumn + "=?");
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            pst.setObject(1, key);
            pst.executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(pst);
        }
    }
    
    /**
     * 获取单个值
     * @param table 表名
     * @param keyColumn 键列
     * @param searchValue 搜索值
     * @param column 列名
     * @param <K> 搜索值类型
     * @param <V> 返回值类型
     * @return 值
     */
    public <K, V> V get(final String table, final String keyColumn, final K searchValue, final String column) {
        V value = null;
        final StringBuilder sb = new StringBuilder("SELECT `" + column + "` FROM `" + table + "` WHERE " + keyColumn + "=?");
        ResultSet set = null;
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            pst.setObject(1, searchValue);
            set = pst.executeQuery();
            if (set.next()) {
                value = (V)set.getObject(column);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(set, pst);
        }
        return value;
    }
    
    /**
     * 获取单个值，如果不存在则返回默认值
     * @param table 表名
     * @param keyColumn 键列
     * @param searchValue 搜索值
     * @param column 列名
     * @param defaultValue 默认值
     * @param <K> 搜索值类型
     * @param <V> 返回值类型
     * @return 值
     */
    public <K, V> V getOrDefault(final String table, final String keyColumn, final K searchValue, final String column, final V defaultValue) {
        V value = null;
        final StringBuilder sb = new StringBuilder("SELECT `" + column + "` FROM `" + table + "` WHERE " + keyColumn + "=?");
        ResultSet set = null;
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            pst.setObject(1, searchValue);
            set = pst.executeQuery();
            if (set.next()) {
                value = (V)set.getObject(column);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(set, pst);
        }
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }
    
    /**
     * 获取多个值
     * @param table 表名
     * @param keyColumn 键列
     * @param searchValue 搜索值
     * @param valuesColumn 值列
     * @param <K> 搜索值类型
     * @return 值映射
     */
    public <K> Map<String, Object> get(final String table, final String keyColumn, final K searchValue, final String... valuesColumn) {
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        final StringBuilder sb = new StringBuilder("SELECT " + this.getArrayString(valuesColumn) + " FROM `" + table + "` WHERE " + keyColumn + "=?");
        ResultSet set = null;
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            pst.setObject(1, searchValue);
            set = pst.executeQuery();
            if (set.next()) {
                for (final String s : valuesColumn) {
                    map.put(s, set.getObject(s));
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(set, pst);
        }
        return map;
    }
    
    /**
     * 获取值列表
     * @param table 表名
     * @param keyColumn 键列
     * @param searchValue 搜索值
     * @param valueColumn 值列
     * @param <K> 搜索值类型
     * @param <V> 值类型
     * @return 值列表
     */
    public <K, V> List<V> getList(final String table, final String keyColumn, final K searchValue, final String valueColumn) {
        final List<V> list = new ArrayList<V>();
        final StringBuilder sb = new StringBuilder("SELECT " + valueColumn + " FROM `" + table + "` WHERE " + keyColumn + "=?");
        ResultSet set = null;
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            pst.setObject(1, searchValue);
            set = pst.executeQuery();
            if (set.next()) {
                list.add((V)set.getObject(valueColumn));
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(set, pst);
        }
        return list;
    }
    
    /**
     * 获取值列表，如果不存在则返回默认值
     * @param table 表名
     * @param keyColumn 键列
     * @param searchValue 搜索值
     * @param valueColumn 值列
     * @param defaultValue 默认值
     * @param <K> 搜索值类型
     * @param <V> 值类型
     * @return 值列表
     */
    public <K, V> List<V> getListOrDefault(final String table, final String keyColumn, final K searchValue, final String valueColumn, final V defaultValue) {
        final List<V> list = new ArrayList<V>();
        final StringBuilder sb = new StringBuilder("SELECT " + valueColumn + " FROM `" + table + "` WHERE " + keyColumn + "=?");
        ResultSet set = null;
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            pst.setObject(1, searchValue);
            set = pst.executeQuery();
            if (set.next()) {
                V object = (V)set.getObject(valueColumn);
                if (object == null) {
                    object = defaultValue;
                }
                list.add(object);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(set, pst);
        }
        return list;
    }
    
    /**
     * 获取值映射列表
     * @param table 表名
     * @param keyColumn 键列
     * @param searchValue 搜索值
     * @param valuesColumn 值列
     * @param <K> 搜索值类型
     * @return 值映射列表
     */
    public <K> List<Map<String, Object>> getList(final String table, final String keyColumn, final K searchValue, final String... valuesColumn) {
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        final StringBuilder sb = new StringBuilder("SELECT " + this.getArrayString(valuesColumn) + " FROM `" + table + "` WHERE " + keyColumn + "=?");
        ResultSet set = null;
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            pst.setObject(1, searchValue);
            set = pst.executeQuery();
            if (set.next()) {
                final Map<String, Object> map = new HashMap<String, Object>();
                for (final String s : valuesColumn) {
                    map.put(s, set.getObject(s));
                }
                list.add(map);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(set, pst);
        }
        return list;
    }
    
    /**
     * 获取排序值映射列表
     * @param table 表名
     * @param sortColumn 排序列
     * @param size 大小
     * @param desc 是否降序
     * @param valuesColumn 值列
     * @return 值映射列表
     */
    public List<Map<String, Object>> getSortList(final String table, final String sortColumn, final int size, final boolean desc, final String... valuesColumn) {
        final List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        final StringBuilder sb = new StringBuilder("SELECT " + this.getArrayString(valuesColumn) + " FROM `" + table + "` ORDER BY " + sortColumn).append(desc ? " DESC" : "").append((size < 0) ? "" : (" LIMIT " + size));
        ResultSet set = null;
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            set = pst.executeQuery();
            while (set.next()) {
                final Map<String, Object> map = new LinkedHashMap<String, Object>();
                for (final String s : valuesColumn) {
                    map.put(s, set.getObject(s));
                }
                list.add(map);
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(set, pst);
        }
        return list;
    }
    
    /**
     * 插入或更新记录
     * @param table 表名
     * @param kv 键值对
     */
    public void put(final String table, final KeyValue... kv) {
        final StringBuilder sb = new StringBuilder("INSERT INTO `" + table + "` (" + this.getArrayString(kv) + ") VALUES(" + this.getArrayString(kv.length) + ") ON DUPLICATE KEY UPDATE " + this.getArrayStringValues(kv));
        PreparedStatement pst = null;
        try {
            pst = this.getConnection().prepareStatement(sb.toString());
            for (int i = 0; i < kv.length; ++i) {
                pst.setObject(i + 1, kv[i].getValue());
            }
            pst.executeUpdate();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            this.free(pst);
        }
    }
    
    /**
     * 将字符串数组转换为逗号分隔的字符串
     * @param array 字符串数组
     * @return 逗号分隔的字符串
     */
    private String getArrayString(final String... array) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            if (i == array.length - 1) {
                sb.append(array[i]);
            } else {
                sb.append(array[i]).append(", ");
            }
        }
        return sb.toString();
    }
    
    /**
     * 生成问号占位符字符串
     * @param length 长度
     * @return 问号占位符字符串
     */
    private String getArrayString(final int length) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            if (i == length - 1) {
                sb.append("?");
            } else {
                sb.append("?").append(", ");
            }
        }
        return sb.toString();
    }
    
    /**
     * 将键值对数组转换为逗号分隔的键字符串
     * @param kv 键值对数组
     * @return 逗号分隔的键字符串
     */
    private String getArrayString(final KeyValue... kv) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kv.length; ++i) {
            final KeyValue keyValue = kv[i];
            if (i == kv.length - 1) {
                sb.append(keyValue.getKey());
            } else {
                sb.append(keyValue.getKey()).append(", ");
            }
        }
        return sb.toString();
    }
    
    /**
     * 生成ON DUPLICATE KEY UPDATE子句
     * @param kv 键值对数组
     * @return ON DUPLICATE KEY UPDATE子句
     */
    private String getArrayStringValues(final KeyValue... kv) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < kv.length; ++i) {
            final KeyValue keyValue = kv[i];
            if (i == kv.length - 1) {
                sb.append(keyValue.getKey()).append("=VALUES(").append(keyValue.getKey()).append(")");
            } else {
                sb.append(keyValue.getKey()).append("=VALUES(").append(keyValue.getKey()).append(")").append(", ");
            }
        }
        return sb.toString();
    }
    
    /**
     * 释放PreparedStatement资源
     * @param statement PreparedStatement
     */
    public void free(final PreparedStatement statement) {
        try {
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 释放ResultSet和PreparedStatement资源
     * @param set ResultSet
     * @param statement PreparedStatement
     */
    public void free(final ResultSet set, final PreparedStatement statement) {
        try {
            if (set != null && !set.isClosed()) {
                set.close();
            }
            if (statement != null && !statement.isClosed()) {
                statement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取数据库连接
     * @return 数据库连接
     * @throws Throwable 异常
     */
    public Connection getConnection() throws Throwable {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connect();
            }
            return this.connection;
        } catch (Throwable e) {
            throw new Throwable("Connection is null or close", e);
        }
    }
    
    /**
     * 连接数据库
     * @throws Throwable 异常
     */
    private void connect() throws Throwable {
        final String url = "jdbc:mysql://" + this.ip + ":" + this.port + "/" + this.database;
        try {
            this.connection = DriverManager.getConnection(this.param.isEmpty() ? url : (url + "?" + this.param), this.user, this.password);
        } catch (SQLException e) {
            this.plugin.getLogger().info("数据库连接失败");
            throw new Throwable("数据库连接失败", e);
        }
    }
}