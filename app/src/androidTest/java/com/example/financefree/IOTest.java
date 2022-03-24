package com.example.financefree;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

@RunWith(AndroidJUnit4.class)
public class IOTest {
    private static final String DIR = "test";
    private static final String FILE = "test.file";


    @Test
    public void testOpenFile() throws IOException {
        File f = InstrumentationRegistry.getInstrumentation().getTargetContext()
                .getDir(DIR, Context.MODE_PRIVATE);
        //if(!f.exists()) f.mkdirs();
        assert f.exists();
        assert f.canWrite();
        assert f.canRead();
    }

    @Test
    public void testReadWrite() throws IOException {
        String testStr = "Hello world!";
        FileOutputStream fOut = InstrumentationRegistry.getInstrumentation().getTargetContext()
                .openFileOutput(FILE, Context.MODE_PRIVATE);

        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fOut));
        bufferedWriter.flush();
        bufferedWriter.write(testStr);
        bufferedWriter.close();
        fOut.close();

        FileInputStream fIn = InstrumentationRegistry.getInstrumentation().getTargetContext()
                .openFileInput(FILE);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fIn));
        String s1 = bufferedReader.readLine();
        String s2 = bufferedReader.readLine();
        bufferedReader.close();
        fIn.close();

        assert s1.equals(testStr);
        assert s2 == null;
    }



}
