package com.example.financefree.structures;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public final class IdMgr {
    private static final Random rnd = new Random(new Date().getTime());
    private static final List<Long> usedIds = new LinkedList<>();

    private IdMgr(){}

    public static void registerId(long id) {
        usedIds.add(id);
    }

    public static void releaseId(long id){
        usedIds.remove(id);
    }

    public static long genNewId() {
        boolean unique = false;
        long newId = rnd.nextLong();
        while(!unique) {
            if(usedIds.contains(newId) && newId != 0){
                newId = rnd.nextLong();
            }
            else unique = true;
        }
        usedIds.add(newId);
        return newId;
    }
}
