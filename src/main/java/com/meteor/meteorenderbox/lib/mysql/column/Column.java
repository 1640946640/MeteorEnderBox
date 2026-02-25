package com.meteor.meteorenderbox.lib.mysql.column;

/**
 * 数据库列定义类
 * 用于创建数据库表结构
 */
public class Column
{
    /** 列名 */
    private String name;
    /** 列类型 */
    private Type type;
    /** 是否为主键 */
    private boolean primary;
    /** 列大小 */
    private int[] size;
    
    /**
     * 构造函数
     * @param name 列名
     * @param type 列类型
     * @param primary 是否为主键
     * @param size 列大小
     */
    private Column(final String name, final Type type, final boolean primary, final int[] size) {
        this.name = name;
        this.type = type;
        this.primary = primary;
        this.size = size;
    }
    
    /**
     * 创建列实例
     * @param name 列名
     * @param type 列类型
     * @param primary 是否为主键
     * @param size 列大小
     * @return 列实例
     */
    public static Column of(final String name, final Type type, final boolean primary, final int[] size) {
        return new Column(name, type, primary, size);
    }
    
    /**
     * 创建列实例
     * @param name 列名
     * @param type 列类型
     * @param size 列大小
     * @return 列实例
     */
    public static Column of(final String name, final Type type, final int[] size) {
        return new Column(name, type, false, size);
    }
    
    /**
     * 创建列实例
     * @param name 列名
     * @param type 列类型
     * @param primary 是否为主键
     * @return 列实例
     */
    public static Column of(final String name, final Type type, final boolean primary) {
        return new Column(name, type, primary, null);
    }
    
    /**
     * 创建列实例
     * @param name 列名
     * @param type 列类型
     * @return 列实例
     */
    public static Column of(final String name, final Type type) {
        return new Column(name, type, false, null);
    }
    
    /**
     * 获取列名
     * @return 列名
     */
    public String getName() {
        return this.name;
    }
    
    /**
     * 获取列类型
     * @return 列类型
     */
    public Type getType() {
        return this.type;
    }
    
    /**
     * 是否为主键
     * @return 是否为主键
     */
    public boolean isPrimary() {
        return this.primary;
    }
    
    /**
     * 获取列大小
     * @return 列大小
     */
    public int[] getSize() {
        return this.size;
    }
    
    /**
     * 类型枚举
     */
    public static class Type
    {
        /** 类型名称 */
        private String name;
        
        /**
         * 构造函数
         * @param name 类型名称
         */
        public Type(final String name) {
            this.name = name;
        }
        
        /**
         * 获取类型名称
         * @return 类型名称
         */
        public String getName() {
            return this.name;
        }
    }
    
    /**
     * 整数类型
     */
    public static class Integer
    {
        public static final Type INT = new Type("INT");
        public static final Type TINYINT = new Type("TINYINT");
        public static final Type SMALLINT = new Type("SMALLINT");
        public static final Type MEDIUMINT = new Type("MEDIUMINT");
        public static final Type BIGINT = new Type("BIGINT");
    }
    
    /**
     * 浮点数类型
     */
    public static class Float
    {
        public static final Type FLOAT = new Type("FLOAT");
        public static final Type DOUBLE = new Type("DOUBLE");
        public static final Type DECIMAL = new Type("DECIMAL");
    }
    
    /**
     * 字符类型
     */
    public static class Char
    {
        public static final Type CHAR = new Type("CHAR");
        public static final Type VARCHAR = new Type("VARCHAR");
        public static final Type TEXT = new Type("TEXT");
        public static final Type LONGTEXT = new Type("LONGTEXT");
    }
    
    /**
     * 二进制类型
     */
    public static class Bob
    {
        public static final Type TINYBLOB = new Type("TINYBLOB");
        public static final Type BLOB = new Type("BLOB");
        public static final Type MEDIUMBLOB = new Type("MEDIUMBLOB");
        public static final Type LONGBLOB = new Type("LONGBLOB");
    }
    
    /**
     * 日期类型
     */
    public static class Date
    {
        public static final Type DATE = new Type("DATE");
        public static final Type TIME = new Type("TIME");
        public static final Type DATETIME = new Type("DATETIME");
        public static final Type TIMESTAMP = new Type("TIMESTAMP");
    }
}