package com.meteor.meteorenderbox.lib.mysql.data;

/**
 * 键值对类
 * 用于存储键值对数据，在数据库操作中使用
 * @param <V> 值的类型
 */
public class KeyValue<V>
{
    /** 键 */
    private String key;
    /** 值 */
    private V value;
    
    /**
     * 构造函数
     * @param key 键
     * @param value 值
     */
    public KeyValue(final String key, final V value) {
        this.key = key;
        this.value = value;
    }
    
    /**
     * 获取值
     * @return 值
     */
    public V getValue() {
        return this.value;
    }
    
    /**
     * 获取键
     * @return 键
     */
    public String getKey() {
        return this.key;
    }
}