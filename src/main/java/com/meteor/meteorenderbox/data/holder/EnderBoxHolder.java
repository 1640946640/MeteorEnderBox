package com.meteor.meteorenderbox.data.holder;

import org.bukkit.inventory.*;

public class EnderBoxHolder implements InventoryHolder
{
    int number;
    String targetPlayerName;
    
    public Inventory getInventory() {
        return null;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public void setNumber(final int number) {
        this.number = number;
    }
    
    public String getTargetPlayerName() {
        return this.targetPlayerName;
    }
    
    public void setTargetPlayerName(final String targetPlayerName) {
        this.targetPlayerName = targetPlayerName;
    }
}
