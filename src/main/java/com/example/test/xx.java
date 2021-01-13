package com.example.test;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.collections.MapUtils;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import static com.example.util.HolidyUtil.getDays;
import static com.example.util.MonthUtil.dayByMonth;
import static com.example.util.TimeUtil.*;

public class xx {



    public static void main(String[] args) {
       List<List> getAllMonth=getAllMonth();
        System.out.println("getAllMonth = " + getAllMonth);

//        String s_time="2020-02-28";
//               Integer year=2020;
//          List<List> ss= getOneAllMonth(year);
//        System.out.println("ss = " + ss);

        String ss=getNowNext();
        System.out.println("ss = " + ss);

        Integer nomTimeNext_year = Integer.valueOf(ss.substring(0, 4).toString());
        Integer nomTimeNext_month = Integer.valueOf(ss.substring(5, 7).toString());
        System.out.println("nomTimeNext_year = " + nomTimeNext_year);
        System.out.println("nomTimeNext_month = " + nomTimeNext_month);
        
        Integer year=2020;

     String zz="2020-10-02";

        System.out.println("zz.substring(8) = " + zz.substring(8));

       
       

    
        

        
        
        
        
    }
    


}
