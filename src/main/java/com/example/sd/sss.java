package com.example.sd;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;

import java.io.*;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import static com.example.util.BASE64.decryptBASE64;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;
import static com.example.util.TimeUtil.*;
import static com.example.util.TimeUtil.twoList;

public class sss {

    public static List<Map<String, Object>> dd(List<Map<String, Object>> fList, List<Map<String, Object>> sList) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> strList = new ArrayList<>();
        if (sList.size() > 0) {
            for (int i = 0; i < fList.size(); i++) {
                for (int j = 0; j < sList.size(); j++) {
                    if (fList.get(i).keySet().equals(sList.get(j).keySet())) {
                        list.remove(fList.get(i));
                        if (strList.contains(fList.get(i).keySet())) {
                            list.add(sList.get(j));
                        } else {
                            if (list.contains(fList.get(i))) {

                            } else {

                                list.add(sList.get(j));
                                strList.add(sList.get(j).keySet().toString());
                            }
                        }

                    } else {

                        if (list.contains(fList.get(i))) {

                        } else {

                            if (strList.contains(fList.get(i).keySet().toString())) {

                            } else {

                                list.add(fList.get(i));
                                strList.add(fList.get(i).keySet().toString());
                            }

                        }

                    }

                }
            }
        } else {
            list.addAll(fList);
        }

        return list;
    }
    @SneakyThrows
    public static void main(String[] args) {
        String first="[{2021-01-01:0},{2021-01-02:0},{2021-01-03:0},{2021-01-04:0}]";
        List<Map<String,Object>> firstList=JSONArray.fromObject(first);
        System.out.println("firstList = " + firstList);


        



    }


}