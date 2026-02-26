package com.meteor.meteorenderbox.data;

import org.bukkit.inventory.*;
import java.util.*;

<<<<<<< HEAD
public class Box
{
    int number;
    int slot;
    Map<Integer, ItemStack> map;
    
    public Box() {
        this.map = new HashMap<Integer, ItemStack>();
    }
    
=======
/**
 * 末影箱模型类
 * 存储末影箱的物品和属性
 */
public class Box
{
    /** 末影箱编号 */
    private int number;
    /** 末影箱物品映射 */
    private Map<Integer, ItemStack> map;
    /** 末影箱槽位数 */
    private int slot;
    
    /**
     * 获取末影箱编号
     * @return 末影箱编号
     */
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    public int getNumber() {
        return this.number;
    }
    
<<<<<<< HEAD
=======
    /**
     * 设置末影箱编号
     * @param number 末影箱编号
     */
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    public void setNumber(final int number) {
        this.number = number;
    }
    
<<<<<<< HEAD
    public int getSlot() {
        return this.slot;
    }
    
    public void setSlot(final int slot) {
        this.slot = slot;
    }
    
=======
    /**
     * 获取末影箱物品映射
     * @return 物品映射
     */
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    public Map<Integer, ItemStack> getMap() {
        return this.map;
    }
    
<<<<<<< HEAD
    public void setMap(final Map<Integer, ItemStack> map) {
        this.map = map;
    }
}
=======
    /**
     * 设置末影箱物品映射
     * @param map 物品映射
     */
    public void setMap(final Map<Integer, ItemStack> map) {
        this.map = map;
    }
    
    /**
     * 获取末影箱槽位数
     * @return 槽位数
     */
    public int getSlot() {
        return this.slot;
    }
    
    /**
     * 设置末影箱槽位数
     * @param slot 槽位数
     */
    public void setSlot(final int slot) {
        this.slot = slot;
    }
}
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
