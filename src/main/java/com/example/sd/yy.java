package com.example.sd;

import com.alibaba.fastjson.JSONObject;
import com.example.util.BASE64;

import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateUtils;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.jws.Oneway;
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
import static com.example.util.HolidyUtil.getDays;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;
import static com.example.util.NumberUtil.getOrderNumber;
import static com.example.util.TimeUtil.*;
import static com.example.util.TimeUtil.lastMonth;

import java.util.Base64;
import java.util.Base64.Decoder;

public class yy {

    /**
     * 获取当前时间月日 7.13
     *
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        //month从0开始
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取某月日历上  ，上一个月的时间
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> lastMonth(List<String> list) {
        int lastCount = getWeek(list.get(0));
        List<Map<String, Object>> lastMonth = new ArrayList<>();
        for (int i = lastCount; i > 0; i--) {
            Map<String, Object> timeMap = new HashedMap();
            timeMap.put(getNewEndtime(list.get(0), i), 0);
            lastMonth.add(timeMap);
        }
        return lastMonth;

    }

    public static List<Map<String, Object>> yesterdayMonth(List<String> list, Integer day) {
        List<Map<String, Object>> yesterdayMonth = new ArrayList<>();
        for (int i = 0; i <= day; i++) {
            Map<String, Object> timeMap = new HashedMap();
            timeMap.put(getNewEndtime(list.get(0), -i), 0);
            yesterdayMonth.add(timeMap);
        }
        return yesterdayMonth;
    }


    @SneakyThrows
    public static void main(String[] args) {

        String period = "[{\"2020-12-30\":3},{\"2021-01-02\":3},{\"2021-01-23\":3},{\"2021-01-25\":3},{\"2021-01-31\":3},{\"2021-02-02\":3},{\"2021-03-01\":3},{\"2021-03-30\":3},{\"2021-03-31\":3}]";
        List<Map<String, Object>> peList = JSONArray.fromObject(period);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = simpleDateFormat.format(new Date());
        String yesterday = getNewEndtime(nowTime, 1);

        //Integer nowMonth = Integer.valueOf(nowTime.substring(5, 7).toString());
        Integer year = 2021;
        Integer month = 4;
        List<String> list = getYearMonth(year, month);
        Integer monthDay = dayByMonth(year, month);


        Map<String, Object> lastNextPeriodMap = lastNextPeriod(peList, month, yesterday, year);

        //搜索月份上一个月的档期
        List<Map<String, Object>> lastPeriodList = (List<Map<String, Object>>) lastNextPeriodMap.get("lastPeriodList");
        //搜索月份的档期
        List<Map<String, Object>> nowPeriodList = (List<Map<String, Object>>) lastNextPeriodMap.get("nowPeriodList");
        //下一个月的档期
        List<Map<String, Object>> nextPeriodList = (List<Map<String, Object>>) lastNextPeriodMap.get("nextPeriodList");


        List<Map<String, Object>> allList = new ArrayList<>();

        //搜索月份上一个月的档期
        List<Map<String, Object>> lastMonth = new ArrayList<>();
        //搜索月份上一个月的档期
        List<Map<String, Object>> nowMonth = new ArrayList<>();
        //搜索月份下一个月的档期
        List<Map<String, Object>> nextMonth = new ArrayList<>();


        //搜索月份上一个空白档期
        List<Map<String, Object>> nullLastMonth = lastMonth(list);
        //搜索月份空白档期
        List<Map<String, Object>> nullNowMonth = onMonth(list, monthDay);
        //搜索月份下一个空白档期
        List<Map<String, Object>> nullNextMonth = new ArrayList<>();

        //上一个月档期
        nullLastMonth = lastMonth(list);
        //  System.out.println("nullLastMonth = " + nullLastMonth);
        lastMonth = twoList(nullLastMonth, lastPeriodList);
        System.out.println("lastMonth = " + lastMonth);
        allList.addAll(lastMonth);


        if (nowPeriodList.size() > 0) {
            //搜索月份有档期
            nowMonth = twoList(nullNowMonth, nowPeriodList);
            allList.addAll(nowMonth);
        } else {
            //搜索月份没有档期
            allList.addAll(nullNowMonth);
        }
        nullNextMonth = nextMonth(list, allList.size());
        nextMonth = twoList(nullNextMonth, nextPeriodList);
        allList.addAll(nextMonth);

    }


}