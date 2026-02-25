package com.meteor.meteorenderbox.data;

import java.util.*;

public class PlayerData
{
    String player;
    int number;
    Map<Integer, Box> boxMap;
    boolean lock;
    
    public PlayerData() {
        this.boxMap = new HashMap<Integer, Box>();
        this.lock = false;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public void setNumber(final int number) {
        this.number = number;
    }
    
    public Map<Integer, Box> getBoxMap() {
        return this.boxMap;
    }
    
    public void setBoxMap(final Map<Integer, Box> boxMap) {
        this.boxMap = boxMap;
    }
    
    public boolean isLock() {
        return this.lock;
    }
    
    public void setLock(final boolean lock) {
        this.lock = lock;
    }
    
    public void unLockBox(final int i, final Box box) {
        this.boxMap.put(i, box);
    }
    
    public String getPlayer() {
        return this.player;
    }
    
    public void setPlayer(final String player) {
        this.player = player;
    }
}
