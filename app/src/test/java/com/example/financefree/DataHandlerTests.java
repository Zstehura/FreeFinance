package com.example.financefree;

import com.example.financefree.datahandlers.BankAccount;
import com.example.financefree.datahandlers.SinglePayment;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.GregorianCalendar;

public class DataHandlerTests {
    @Test
    public void singlePaymentTest() throws IOException {
        SinglePayment sp = new SinglePayment();
        //BufferedReader br = new BufferedReader(new FileReader("singlePayment.json"));
        File f = new File(  "/Users/zstehura/StudioProjects/FreeFinance/app/src/test/java/com/example/financefree/sampleData/singlePayment.json");
        String str = "";
        for(String s:Files.readAllLines(f.toPath())){
            str += s;
        }
        sp.setBankId("Bank of Someplace");
        sp.setName("New Payday!");
        sp.setDate(new GregorianCalendar(2022,2,24));
        sp.setAmount(231.51);
        sp.setNotes("I had a great day!");
        JSONObject jActual = new JSONObject(), jTest = new JSONObject();
        try {
            jActual = new JSONObject(str);
            jTest = sp.toJSON();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        assertEquals(jActual.toString(), jTest.toString());
    }

}
