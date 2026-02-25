package com.meteor.meteorenderbox.data.holder;

import com.meteor.meteorenderbox.data.*;
import java.util.*;
import org.bukkit.inventory.*;

public class EnderBoxListHolder implements InventoryHolder
{
    int page;
    Map<Integer, BoxData> boxDataMap;
    
    public EnderBoxListHolder() {
        this.boxDataMap = new HashMap<Integer, BoxData>();
    }
    
    public Inventory getInventory() {
        return null;
    }
    
    public Map<Integer, BoxData> getBoxDataMap() {
        return this.boxDataMap;
    }
    
    public int getPage() {
        return this.page;
    }
    
    public void setPage(final int page) {
        this.page = page;
    }
    
    public void setBoxDataMap(final Map<Integer, BoxData> boxDataMap) {
        this.boxDataMap = boxDataMap;
    }
}
