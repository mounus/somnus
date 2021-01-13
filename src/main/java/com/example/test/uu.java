package com.example.test;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.File;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getDays;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.daysBetween;
import static com.example.util.MonthUtil.timeSize;
import static com.example.util.PriceUtil.getDayList;

public class uu {

    public static void main(String[] args) throws ParseException, ClientException {

        String monthFirst="2021-01-04";
        String monthLast="2021-01-04";
        String start_time="2020-12-31";
        String end_time="2021-01-03";
        String a="";
        String b="";
        Integer workStates=1;
        String nowTime="2021-01-09";

        List<String> mList=getDays(monthFirst, monthLast);
        System.out.println("mList = " + mList);
        List<String> sList=new ArrayList<>();
        if (workStates==1){
            sList=getDays(getNewEndtime(start_time,-1),nowTime);
        }
        if (workStates==2){
            sList=getDays(getNewEndtime(start_time,-1), end_time);
        }
        mList.retainAll(sList);
        System.out.println("mList = " + mList);





    }
}
