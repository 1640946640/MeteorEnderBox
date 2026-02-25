package com.meteor.meteorenderbox.data;

import java.util.*;

/**
 * 玩家数据模型类
 * 存储玩家的末影箱数据和属性
 */
public class PlayerData
{
    /** 玩家名称 */
    private String player;
    /** 已解锁末影箱数量 */
    private int number;
    /** 末影箱映射 */
    private Map<Integer, Box> boxMap;
    
    /**
     * 获取玩家名称
     * @return 玩家名称
     */
    public String getPlayer() {
        return this.player;
    }
    
    /**
     * 设置玩家名称
     * @param player 玩家名称
     */
    public void setPlayer(final String player) {
        this.player = player;
    }
    
    /**
     * 获取已解锁末影箱数量
     * @return 末影箱数量
     */
    public int getNumber() {
        return this.number;
    }
    
    /**
     * 设置已解锁末影箱数量
     * @param number 末影箱数量
     */
    public void setNumber(final int number) {
        this.number = number;
    }
    
    /**
     * 获取末影箱映射
     * @return 末影箱映射
     */
    public Map<Integer, Box> getBoxMap() {
        return this.boxMap;
    }
    
    /**
     * 设置末影箱映射
     * @param boxMap 末影箱映射
     */
    public void setBoxMap(final Map<Integer, Box> boxMap) {
        this.boxMap = boxMap;
    }
    
    /**
     * 解锁末影箱
     * @param number 末影箱编号
     * @param box 末影箱
     */
    public void unLockBox(final int number, final Box box) {
        this.boxMap.put(number, box);
    }
}