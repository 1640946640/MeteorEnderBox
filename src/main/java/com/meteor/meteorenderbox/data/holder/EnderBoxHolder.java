package com.meteor.meteorenderbox.data.holder;

import org.bukkit.inventory.*;

/**
 * 末影箱持有者类
 * 用于标识末影箱库存的持有者
 */
public class EnderBoxHolder implements InventoryHolder
{
    /** 末影箱编号 */
    private int number;
    
    /**
     * 构造函数
     * @param number 末影箱编号
     */
    public EnderBoxHolder(final int number) {
        this.number = number;
    }
    
    /**
     * 获取末影箱编号
     * @return 末影箱编号
     */
    public int getNumber() {
        return this.number;
    }
    
    /**
     * 设置末影箱编号
     * @param number 末影箱编号
     */
    public void setNumber(final int number) {
        this.number = number;
    }
    
    /**
     * 获取库存
     * @return 库存
     */
    @Override
    public Inventory getInventory() {
        return null;
    }
}