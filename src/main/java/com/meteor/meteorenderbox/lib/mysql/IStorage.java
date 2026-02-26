package com.meteor.meteorenderbox.lib.mysql;

import java.sql.*;

/**
 * 存储接口
 * 定义存储的基本操作
 */
public interface IStorage
{
    /**
     * 启用存储
     */
    void enable();
    
    /**
     * 禁用存储
     */
    void disable();
    
    /**
     * 获取数据库连接
     * @return 数据库连接
     * @throws Throwable 异常
     */
    Connection getConnection() throws Throwable;
}