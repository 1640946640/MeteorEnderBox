package com.meteor.meteorenderbox.lib.mysql.data;

/**
 * 键值对类
 * 用于存储数据库操作的键值对
 */
public class KeyValue
{
    /** 键 */
    private String key;
    /** 值 */
    private Object value;
    
    /**
     * 构造函数
     * @param key 键
     * @param value 值
     */
    public KeyValue(final String key, final Object value) {
        this.key = key;
        this.value = value;
    }
    
    /**
     * 获取键
     * @return 键
     */
    public String getKey() {
        return this.key;
    }
    
    /**
     * 设置键
     * @param key 键
     */
    public void setKey(final String key) {
        this.key = key;
    }
    
    /**
     * 获取值
     * @return 值
     */
    public Object getValue() {
        return this.value;
    }
    
    /**
     * 设置值
     * @param value 值
     */
    public void setValue(final Object value) {
        this.value = value;
    }
}