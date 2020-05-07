package com.assigned.printart.Model;

public class Test {
    public Test() {

    }

    private String Credit, KeyValue, DeliveryDate, Original, orderstatus;

    public Test(String credit, String keyValue, String deliveryDate, String original, String orderstatus) {
        Credit = credit;
        KeyValue = keyValue;
        DeliveryDate = deliveryDate;
        Original = original;
        this.orderstatus = orderstatus;
    }

    public String getCredit() {
        return Credit;
    }

    public void setCredit(String credit) {
        Credit = credit;
    }

    public String getKeyValue() {
        return KeyValue;
    }

    public void setKeyValue(String keyValue) {
        KeyValue = keyValue;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getOriginal() {
        return Original;
    }

    public void setOriginal(String original) {
        Original = original;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }
}
