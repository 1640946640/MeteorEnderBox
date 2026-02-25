package com.meteor.meteorenderbox.lib.mysql;

import com.meteor.meteorenderbox.lib.mysql.column.*;
import com.meteor.meteorenderbox.lib.mysql.data.*;
import org.bukkit.configuration.*;
import org.bukkit.plugin.java.*;

import java.sql.*;
import java.util.*;

/**
 * MySQL存储类
 * 负责数据库连接和操作
 */
public class FastMySQLStorage implements IStorage
{
    /** 插件实例 */
    private JavaPlugin plugin;
    /** 数据库连接 */
    private Connection connection;
    /** 数据库配置 */
    private ConfigurationSection config;
    
    /**
     * 构造函数
     * @param plugin 插件实例
     * @param config 数据库配置
     */
    public FastMySQLStorage(final JavaPlugin plugin, final ConfigurationSection config) {
        this.plugin = plugin;
        this.config = config;
    }
    
    /**
     * 启用存储
     */
    public void enable() {
        this.connect();
    }
    
    /**
     * 禁用存储
     */
    public void disable() {
        this.disconnect();
    }
    
    /**
     * 连接数据库
     */
    private void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            final String ip = this.config.getString("ip");
            final String port = this.config.getString("port");
            final String parm = this.config.getString("parm");
            final String user = this.config.getString("user");
            final String password = this.config.getString("password");
            final String database = this.config.getString("database");
            this.connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + database + "?" + parm, user, password);
            this.plugin.getLogger().info("MySQL连接成功");
        }
        catch (Exception e) {
            this.plugin.getLogger().info("MySQL连接失败: " + e.getMessage());
        }
    }
    
    /**
     * 断开数据库连接
     */
    private void disconnect() {
        try {
            if (this.connection != null && !this.connection.isClosed()) {
                this.connection.close();
                this.plugin.getLogger().info("MySQL连接已关闭");
            }
        }
        catch (Exception e) {
            this.plugin.getLogger().info("MySQL断开连接失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取数据库连接
     * @return 数据库连接
     */
    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return this.connection;
    }
    
    /**
     * 创建表
     * @param table 表名
     * @param columns 列定义
     */
    public void createTable(final String table, final Column[] columns) {
        try {
            final StringBuilder sql = new StringBuilder();
            sql.append("CREATE TABLE IF NOT EXISTS `").append(table).append("` (");
            for (int i = 0; i < columns.length; ++i) {
                final Column column = columns[i];
                sql.append("`").append(column.getName()).append("` ");
                sql.append(column.getType().getName());
                if (column.getSize() != null && column.getSize().length > 0) {
                    sql.append("(");
                    for (int j = 0; j < column.getSize().length; ++j) {
                        sql.append(column.getSize()[j]);
                        if (j < column.getSize().length - 1) {
                            sql.append(",");
                        }
                    }
                    sql.append(")");
                }
                if (column.isPrimary()) {
                    sql.append(" PRIMARY KEY");
                }
                if (i < columns.length - 1) {
                    sql.append(",");
                }
            }
            sql.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
            final PreparedStatement ps = this.getConnection().prepareStatement(sql.toString());
            ps.execute();
            ps.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 插入或更新数据
     * @param table 表名
     * @param keyValues 键值对
     */
    public void put(final String table, final KeyValue[] keyValues) {
        try {
            // 检查数据是否存在
            if (this.isExists(table, keyValues[0].getKey(), keyValues[0].getValue())) {
                // 更新数据
                final StringBuilder sql = new StringBuilder();
                sql.append("UPDATE `").append(table).append("` SET ");
                for (int i = 1; i < keyValues.length; ++i) {
                    sql.append("`").append(keyValues[i].getKey()).append("` = ?");
                    if (i < keyValues.length - 1) {
                        sql.append(",");
                    }
                }
                sql.append(" WHERE `").append(keyValues[0].getKey()).append("` = ?");
                final PreparedStatement ps = this.getConnection().prepareStatement(sql.toString());
                for (int i = 1; i < keyValues.length; ++i) {
                    ps.setObject(i, keyValues[i].getValue());
                }
                ps.setObject(keyValues.length, keyValues[0].getValue());
                ps.execute();
                ps.close();
            } else {
                // 插入数据
                final StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO `").append(table).append("` (");
                final StringBuilder values = new StringBuilder();
                for (int i = 0; i < keyValues.length; ++i) {
                    sql.append("`").append(keyValues[i].getKey()).append("`");
                    values.append("?");
                    if (i < keyValues.length - 1) {
                        sql.append(",");
                        values.append(",");
                    }
                }
                sql.append(") VALUES (").append(values).append(")");
                final PreparedStatement ps = this.getConnection().prepareStatement(sql.toString());
                for (int i = 0; i < keyValues.length; ++i) {
                    ps.setObject(i + 1, keyValues[i].getValue());
                }
                ps.execute();
                ps.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 检查数据是否存在
     * @param table 表名
     * @param key 键
     * @param value 值
     * @return 是否存在
     */
    public boolean isExists(final String table, final String key, final Object value) {
        try {
            final PreparedStatement ps = this.getConnection().prepareStatement("SELECT * FROM `" + table + "` WHERE `" + key + "` = ?");
            ps.setObject(1, value);
            final ResultSet resultSet = ps.executeQuery();
            final boolean exists = resultSet.next();
            resultSet.close();
            ps.close();
            return exists;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * 获取数据
     * @param table 表名
     * @param key 键
     * @param value 值
     * @param target 目标字段
     * @return 数据
     */
    public Object get(final String table, final String key, final Object value, final String target) {
        try {
            final PreparedStatement ps = this.getConnection().prepareStatement("SELECT `" + target + "` FROM `" + table + "` WHERE `" + key + "` = ?");
            ps.setObject(1, value);
            final ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                final Object object = resultSet.getObject(target);
                resultSet.close();
                ps.close();
                return object;
            }
            resultSet.close();
            ps.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 删除数据
     * @param table 表名
     * @param key 键
     * @param value 值
     */
    public void delete(final String table, final String key, final Object value) {
        try {
            final PreparedStatement ps = this.getConnection().prepareStatement("DELETE FROM `" + table + "` WHERE `" + key + "` = ?");
            ps.setObject(1, value);
            ps.execute();
            ps.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}