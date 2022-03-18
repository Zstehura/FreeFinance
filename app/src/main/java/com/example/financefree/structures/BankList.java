package com.example.financefree.structures;

import java.util.LinkedList;
import java.util.List;

public class BankList {
    public final List<String> names;
  //  public final List<Long> ids;
    public BankList(){
       // ids = DatabaseAccessor.getBankIds();
        names = new LinkedList<>();
      //  for(long n: ids){
        //    names.add(DatabaseAccessor.getBankName(n));
       // }
    }
}
