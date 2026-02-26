package com.meteor.meteorenderbox.data.holder;

import com.meteor.meteorenderbox.data.*;
import java.util.*;
import org.bukkit.inventory.*;

/**
 * 末影箱列表持有者类
 * 用于标识末影箱列表库存的持有者
 */
public class EnderBoxListHolder implements InventoryHolder
{
    /** 页码 */
    private int page;
    /** 目标玩家名称 */
    private String targetPlayerName;
    /** 末影箱数据映射 */
    private Map<Integer, BoxData> boxDataMap;
    
    /**
     * 构造函数
     * @param page 页码
     */
    public EnderBoxListHolder(final int page) {
        this(page, null);
    }
    
    /**
     * 构造函数
     * @param page 页码
     * @param targetPlayerName 目标玩家名称
     */
    public EnderBoxListHolder(final int page, final String targetPlayerName) {
        this.page = page;
        this.targetPlayerName = targetPlayerName;
        this.boxDataMap = new HashMap<Integer, BoxData>();
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
     * 获取目标玩家名称
     * @return 目标玩家名称
     */
    public String getTargetPlayerName() {
        return this.targetPlayerName;
    }
    
    /**
     * 设置目标玩家名称
     * @param targetPlayerName 目标玩家名称
     */
    public void setTargetPlayerName(final String targetPlayerName) {
        this.targetPlayerName = targetPlayerName;
    }
    
    /**
     * 获取末影箱数据映射
     * @return 末影箱数据映射
     */
    public Map<Integer, BoxData> getBoxDataMap() {
        return this.boxDataMap;
    }
    
    /**
     * 设置末影箱数据映射
     * @param boxDataMap 末影箱数据映射
     */
    public void setBoxDataMap(final Map<Integer, BoxData> boxDataMap) {
        this.boxDataMap = boxDataMap;
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
