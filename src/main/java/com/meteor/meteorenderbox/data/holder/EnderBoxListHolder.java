package com.meteor.meteorenderbox.data.holder;

import org.bukkit.inventory.*;

/**
 * 末影箱列表持有者类
 * 用于标识末影箱列表库存的持有者
 */
public class EnderBoxListHolder implements InventoryHolder
{
    /** 页码 */
    private int page;
    
    /**
     * 构造函数
     * @param page 页码
     */
    public EnderBoxListHolder(final int page) {
        this.page = page;
    }
    
    /**
     * 获取页码
     * @return 页码
     */
    public int getPage() {
        return this.page;
    }
    
    /**
     * 设置页码
     * @param page 页码
     */
    public void setPage(final int page) {
        this.page = page;
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