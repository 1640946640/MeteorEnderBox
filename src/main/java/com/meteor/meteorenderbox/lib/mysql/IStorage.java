package com.meteor.meteorenderbox.lib.mysql;

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
}