package com.example.financefree.structures;

public class payment {
    public double amount;
    public CustomDate date;
    public String name;
    public long bankId;

    public payment(double amount, CustomDate date,String name, long bankId){
        this.amount = amount;
        this.date = new CustomDate(date);
        this.name = name;
        this.bankId = bankId;
    }
    public payment() throws CustomDate.DateErrorException {
        this(0, new CustomDate(1,1,2000), "new payment", 0);
    }
}
