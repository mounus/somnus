package com.example.sd;

import com.alibaba.fastjson.JSONObject;
import com.example.util.BASE64;

import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




import java.util.*;

import static com.example.util.BASE64.*;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.daysBetween;
import static com.example.util.MonthUtil.getMonthSize;
import static com.example.util.NumberUtil.getOrderNumber;
import static com.example.util.TimeUtil.parseDate;

import java.util.Base64;
import java.util.Base64.Decoder;
public class yy {


    @SneakyThrows
    public static void main(String[] args) {
             String aa="2020-11-27";
       String  end_time=getNewEndtime(aa,-28);
        System.out.println("end_time = " + end_time);
//
//      String bb="2020-11-30";
//        System.out.println("daysBetween(aa,bb) = " + daysBetween(aa, bb));



    }





}

