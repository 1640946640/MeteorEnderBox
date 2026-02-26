package com.meteor.meteorenderbox.lib.mysql.data;

/**
 * 键值对类
<<<<<<< HEAD
 * 用于存储键值对数据，在数据库操作中使用
 * @param <V> 值的类型
 */
public class KeyValue<V>
=======
 * 用于存储数据库操作的键值对
 */
public class KeyValue
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
{
    /** 键 */
    private String key;
    /** 值 */
<<<<<<< HEAD
    private V value;
=======
    private Object value;
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    
    /**
     * 构造函数
     * @param key 键
     * @param value 值
     */
<<<<<<< HEAD
    public KeyValue(final String key, final V value) {
=======
    public KeyValue(final String key, final Object value) {
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
        this.key = key;
        this.value = value;
    }
    
    /**
<<<<<<< HEAD
     * 获取值
     * @return 值
     */
    public V getValue() {
        return this.value;
    }
    
    /**
=======
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
     * 获取键
     * @return 键
     */
    public String getKey() {
        return this.key;
    }
<<<<<<< HEAD
=======
    
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
}