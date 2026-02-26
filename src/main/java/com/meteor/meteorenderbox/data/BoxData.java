package com.meteor.meteorenderbox.data;

<<<<<<< HEAD
public class BoxData
{
    int number;
    PayType payType;
    
=======
/**
 * 末影箱数据模型类
 * 存储末影箱的基本信息和解锁方式
 */
public class BoxData
{
    /** 末影箱编号 */
    private int number;
    /** 支付类型 */
    private PayType payType;
    
    /**
     * 构造函数
     * @param number 末影箱编号
     * @param payType 支付类型
     */
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    public BoxData(final int number, final PayType payType) {
        this.number = number;
        this.payType = payType;
    }
    
<<<<<<< HEAD
=======
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
=======
    /**
     * 获取支付类型
     * @return 支付类型
     */
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
    public PayType getPayType() {
        return this.payType;
    }
    
<<<<<<< HEAD
    public void setPayType(final PayType payType) {
        this.payType = payType;
    }
}
=======
    /**
     * 设置支付类型
     * @param payType 支付类型
     */
    public void setPayType(final PayType payType) {
        this.payType = payType;
    }
}
>>>>>>> d199dc23307236853a9b444e91a7b223fe082c7d
