package com.meteor.meteorenderbox.data.holder;

import com.meteor.meteorenderbox.data.*;
import org.bukkit.inventory.*;

public class UnlockInv implements InventoryHolder
{
    int number;
    PayType payType;
    
    public UnlockInv(final int number) {
        this.number = number;
    }
    
    public Inventory getInventory() {
        return null;
    }
    
    public int getNumber() {
        return this.number;
    }
    
    public void setNumber(final int number) {
        this.number = number;
    }
    
    public PayType getPayType() {
        return this.payType;
    }
    
    public void setPayType(final PayType payType) {
        this.payType = payType;
    }
}
