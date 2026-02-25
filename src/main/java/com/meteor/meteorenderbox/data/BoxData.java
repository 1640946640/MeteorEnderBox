package com.meteor.meteorenderbox.data;

public class BoxData
{
    int number;
    PayType payType;
    
    public BoxData(final int number, final PayType payType) {
        this.number = number;
        this.payType = payType;
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
