package com.meteor.meteorenderbox.data.holder;

<<<<<<< HEAD
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
    
=======
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    public int getPage() {
        return this.page;
    }
    
<<<<<<< HEAD
=======
    /**
     * 设置页码
     * @param page 页码
     */
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    public void setPage(final int page) {
        this.page = page;
    }
    
<<<<<<< HEAD
    public void setBoxDataMap(final Map<Integer, BoxData> boxDataMap) {
        this.boxDataMap = boxDataMap;
    }
}
=======
    /**
     * 获取库存
     * @return 库存
     */
    @Override
    public Inventory getInventory() {
        return null;
    }
}
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
