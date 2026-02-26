package com.meteor.meteorenderbox.lib.mysql.column;

/**
 * 数据库列定义类
<<<<<<< HEAD
 * 用于创建数据库表时定义列的类型、名称和属性
 */
public class Column
{
    /** 是否为主键 */
    private boolean primary;
    /** 列名 */
    private String name;
    /** 列类型 */
    private ColumnType type;
    /** 列参数 */
    private int[] m;
    
    /**
     * 将参数数组转换为字符串
     * @return 参数字符串
     */
    public String toStringM() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.m.length; ++i) {
            if (i == this.m.length - 1) {
                sb.append(this.m[i]);
            }
            else {
                sb.append(this.m[i]).append(", ");
            }
        }
        return sb.toString();
    }
    
    /**
     * 获取列类型
     * @return 列类型
     */
    public ColumnType getType() {
        return this.type;
=======
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    }
    
    /**
     * 获取列名
     * @return 列名
     */
    public String getName() {
        return this.name;
    }
    
    /**
<<<<<<< HEAD
     * 判断是否为主键
=======
     * 获取列类型
     * @return 列类型
     */
    public Type getType() {
        return this.type;
    }
    
    /**
     * 是否为主键
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
     * @return 是否为主键
     */
    public boolean isPrimary() {
        return this.primary;
    }
    
    /**
<<<<<<< HEAD
     * 创建列实例
     * @param name 列名
     * @param type 列类型
     * @return 列实例
     */
    public static Column of(final String name, final ColumnType type) {
        final Column column = new Column();
        column.name = name;
        column.primary = false;
        column.type = type;
        return column;
    }
    
    /**
     * 创建列实例
     * @param name 列名
     * @param type 列类型
     * @param primary 是否为主键
     * @return 列实例
     */
    public static Column of(final String name, final ColumnType type, final boolean primary) {
        final Column column = new Column();
        column.name = name;
        column.primary = primary;
        column.type = type;
        return column;
    }
    
    /**
     * 创建列实例
     * @param name 列名
     * @param type 列类型
     * @param m 参数数组
     * @return 列实例
     */
    public static Column of(final String name, final ColumnType type, final int... m) {
        final Column column = new Column();
        column.name = name;
        column.primary = false;
        column.type = type;
        column.m = m;
        return column;
    }
    
    /**
     * 创建列实例
     * @param name 列名
     * @param type 列类型
     * @param primary 是否为主键
     * @param m 参数数组
     * @return 列实例
     */
    public static Column of(final String name, final ColumnType type, final boolean primary, final int... m) {
        final Column column = new Column();
        column.name = name;
        column.primary = primary;
        column.type = type;
        column.m = m;
        return column;
    }
    
    /**
     * 判断列类型是否需要括号
     * @param column 列类型
     * @return 是否需要括号
     */
    public static boolean hasBracket(final ColumnType column) {
        return getColumnType(column).hasBracket();
    }
    
    /**
     * 判断列类型是否需要参数
     * @param column 列类型
     * @return 是否需要参数
     */
    public static boolean hasM(final ColumnType column) {
        return getColumnType(column).hasM();
    }
    
    /**
     * 获取列类型的类型
     * @param column 列类型
     * @return 类型
     */
    public static Type getColumnType(final ColumnType column) {
        if (column instanceof Integer) {
            return Type.INTEGER;
        }
        if (column instanceof Text) {
            return Type.TEXT;
        }
        if (column instanceof Char) {
            return Type.CHAR;
        }
        if (column instanceof Float) {
            return Type.FLOAT;
        }
        return Type.BOB;
    }
    
    /**
     * 整数类型枚举
     */
    public enum Integer implements ColumnType
    {
        TINYINT, 
        SMALLINT, 
        MEDIUMINT, 
        INT, 
        BIGINT;
    }
    
    /**
     * 字符类型枚举
     */
    public enum Char implements ColumnType
    {
        CHAR, 
        VARCHAR;
    }
    
    /**
     * 文本类型枚举
     */
    public enum Text implements ColumnType
    {
        TINYTEXT, 
        TEXT, 
        MEDIUMTEXT, 
        LONGTEXT;
    }
    
    /**
     * 浮点数类型枚举
     */
    public enum Float implements ColumnType
    {
        FLOAT, 
        DOUBLE;
    }
    
    /**
     * 二进制类型枚举
     */
    public enum Bob implements ColumnType
    {
        TINYBLOB, 
        BLOB, 
        MEDIUMBLOB, 
        LONGBLOB;
=======
     * 获取列大小
     * @return 列大小
     */
    public int[] getSize() {
        return this.size;
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    }
    
    /**
     * 类型枚举
     */
<<<<<<< HEAD
    public enum Type
    {
        INTEGER(true, true), 
        CHAR(true, true), 
        TEXT, 
        FLOAT(true, true), 
        BOB;
        
        /** 是否需要括号 */
        private boolean bracket;
        /** 是否需要参数 */
        private boolean m;
        
        /**
         * 构造函数
         * @param bracket 是否需要括号
         * @param m 是否需要参数
         */
        private Type(final boolean bracket, final boolean m) {
            this.bracket = bracket;
            this.m = m;
        }
        
        /**
         * 构造函数
         */
        private Type() {
            this.bracket = false;
            this.m = false;
        }
        
        /**
         * 判断是否需要括号
         * @return 是否需要括号
         */
        public boolean hasBracket() {
            return this.bracket;
        }
        
        /**
         * 判断是否需要参数
         * @return 是否需要参数
         */
        public boolean hasM() {
            return this.m;
=======
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
        }
    }
    
    /**
<<<<<<< HEAD
     * 列类型接口
     */
    interface ColumnType
    {
=======
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    }
}