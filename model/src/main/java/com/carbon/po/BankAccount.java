package com.carbon.po;

public class BankAccount {
    private String id;
    //资金余额
    private Double capital;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getCapital() {
        return capital;
    }

    public void setCapital(Double capital) {
        this.capital = capital;
    }
}
