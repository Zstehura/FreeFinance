package com.example.financefree.datahandlers;

import org.json.JSONObject;

import java.util.List;
import java.util.Vector;

public class MonthFile {



    private final List<SinglePayment> payments = new Vector<>();

    public MonthFile() {}

    public void readJSON(JSONObject jsonObject){

    }
    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();

        return jsonObject;
    }


}
