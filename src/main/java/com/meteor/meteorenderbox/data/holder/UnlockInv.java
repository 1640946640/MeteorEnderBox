package com.meteor.meteorenderbox.data.holder;

<<<<<<< HEAD
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
    
=======
import org.bukkit.inventory.*;

/**
 * 解锁末影箱库存持有者类
 * 用于标识解锁末影箱的库存
 */
public class UnlockInv implements InventoryHolder
{
    /** 末影箱编号 */
    private int number;
    /** 支付类型 */
    private String payType;
    /** 价格 */
    private int price;
    
    /**
     * 构造函数
     * @param number 末影箱编号
     * @param payType 支付类型
     * @param price 价格
     */
    public UnlockInv(final int number, final String payType, final int price) {
        this.number = number;
        this.payType = payType;
        this.price = price;
    }
    
    /**
     * 获取末影箱编号
     * @return 末影箱编号
     */
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    public int getNumber() {
        return this.number;
    }
    
<<<<<<< HEAD
=======
    /**
     * 设置末影箱编号
     * @param number 末影箱编号
     */
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    public void setNumber(final int number) {
        this.number = number;
    }
    
<<<<<<< HEAD
    public PayType getPayType() {
        return this.payType;
    }
    
    public void setPayType(final PayType payType) {
        this.payType = payType;
    }
}
=======
    /**
     * 获取支付类型
     * @return 支付类型
     */
    public String getPayType() {
        return this.payType;
    }
    
    /**
     * 设置支付类型
     * @param payType 支付类型
     */
    public void setPayType(final String payType) {
        this.payType = payType;
    }
    
    /**
     * 获取价格
     * @return 价格
     */
    public int getPrice() {
        return this.price;
    }
    
    /**
     * 设置价格
     * @param price 价格
     */
    public void setPrice(final int price) {
        this.price = price;
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
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
