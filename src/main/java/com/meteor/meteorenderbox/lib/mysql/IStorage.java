package com.meteor.meteorenderbox.lib.mysql;

/**
 * 存储接口
 * 定义存储系统的基本方法
 */
public interface IStorage
{
    /**
     * 启用存储系统
     */
    void enable();
    
    /**
     * 禁用存储系统
     */
    void disable();
}