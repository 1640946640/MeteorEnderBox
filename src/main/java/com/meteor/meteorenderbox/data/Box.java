package com.meteor.meteorenderbox.data;

import org.bukkit.inventory.*;
import java.util.*;

public class Box
{
    int number;
    int slot;
    Map<Integer, ItemStack> map;
    
    public Box() {
        this.map = new HashMap<Integer, ItemStack>();
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public void setNumber(final int number) {
        this.number = number;
    }
    
    public int getSlot() {
        return this.slot;
    }
    
    public void setSlot(final int slot) {
        this.slot = slot;
    }
    
    public Map<Integer, ItemStack> getMap() {
        return this.map;
    }
    
    public void setMap(final Map<Integer, ItemStack> map) {
        this.map = map;
    }
}
