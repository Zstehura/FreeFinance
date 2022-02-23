package com.example.financefree.sampleData;

import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SampleDataHandler {
    public SampleDataHandler(){}

    private static final String ABS_PATH = "/Users/zstehura/StudioProjects/FreeFinance/app/src/main/java/com/example/financefree/sampleData/";
    public static final String BANK_ACC = "bankAccount.json";
    public static final String DATA_MGR = "dataManager.json";
    public static final String RECUR_PAY = "recurringPayment.json";
    public static final String SINGLE_PAY = "singlePayment.json";
    public static final String TAX_BRACK = "taxBrackets.json";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static JSONObject get(String fileName) throws IOException, JSONException {
        File f = new File(ABS_PATH + fileName);
        String str = "";
        for(String s: Files.readAllLines(f.toPath())){
            str += s;
        }
        return new JSONObject(str);
    }

}
