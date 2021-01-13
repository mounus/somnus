package com.example.test;


import lombok.SneakyThrows;
import net.sf.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.BASE64.decryptBASE64;

public class tt {
    @SneakyThrows
    public static void main(String[] args) throws ParseException {
             int[] aa={1,9,6,45,12,26,8,29};

        for (int i = 0; i <aa.length-1 ; i++) {
            System.out.println("aa[i] = " + aa[i]);
            for (int j = 0; j <aa.length-i-1 ; j++) {
                        if(aa[j]>aa[j+1]){
                            int tmp = aa[j] ;
                            aa[j] = aa[j+1] ;
                            aa[j+1] = tmp;

                        }
            }
        }
        for (int i = 0; i <aa.length ; i++) {
            System.out.print( aa[i]);
        }
    }
}
