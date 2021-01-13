package com.example.sd;

import lombok.Data;
import lombok.val;
import net.sf.json.JSONArray;

import java.awt.image.ImageProducer;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static com.example.util.ExcelUtil.downloadPicture;
import static com.example.util.HolidyUtil.getCount;
import static javafx.scene.input.KeyCode.K;
import static javafx.scene.input.KeyCode.V;

@Data
public class rr {
    public static void main(String[] args) {

   String ss="ddd";


        //features.forEach(System.out::println);

        List<Map<String,Object>> list=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        Map<String,Object> map1=new HashMap<>();
        Map<String,Object> map2=new HashMap<>();
        Map<String,Object> map3=new HashMap<>();
        Map<String,Object> map4=new HashMap<>();
        Map<String,Object> map5=new HashMap<>();
        map.put("name","sa");
        map.put("age",1);
        map5.put("name","sa");
        map5.put("age",55);
        map1.put("name","ta");
        map1.put("age",2);
        map2.put("name","sf");
        map2.put("age",3);
        map3.put("name","sg");
        map3.put("age",4);
        map4.put("name","tk");
        map4.put("age",5);


        list.add(map);
        list.add(map1);
        list.add(map2);
        list.add(map3);
        list.add(map4);
        list.forEach(stringObjectMap -> {if (stringObjectMap.get("name").equals("sa")){
            System.out.println(stringObjectMap);
        }else {
            System.out.println("asddasd");
        }
        });


        //list.forEach(i -> System.out.println(i));


         map2.forEach((K,V)->{
              System.out.println("Key : " + K);

              System.out.println("Value : " + V);

         });




    }



}
