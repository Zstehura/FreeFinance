package com.example.financefree.preferenceManagement;

import com.example.financefree.structures.CustomDate;

import java.util.Map;

public class BankAccount {
    public String bank_id;
    public String note;
    public Map<CustomDate,Double> statements;
}
