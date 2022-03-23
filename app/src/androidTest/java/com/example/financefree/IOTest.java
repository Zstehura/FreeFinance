package com.example.financefree;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

@RunWith(AndroidJUnit4.class)
public class IOTest {
    private static final String FILE = "test.file";


    @Test
    public void testOpenFile() throws IOException {
        File f = InstrumentationRegistry.getInstrumentation().getTargetContext().getDir(FILE, Context.MODE_PRIVATE);
        //if(!f.exists()) f.mkdirs();
        assert f.exists();
        assert f.canWrite();
        assert f.canRead();

    }



}
