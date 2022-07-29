package com.example.financefree;

import android.content.Context;
import android.content.res.Resources;

public class MyResources {
    private static Resources mRes;

    private MyResources() {}

    public static void setContext(Context context) { mRes = context.getResources();}
    public static Resources getRes(){return mRes;}
}
