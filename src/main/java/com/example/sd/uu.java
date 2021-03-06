package com.example.sd;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.dayByMonth;
import static com.example.util.MonthUtil.timeSize;
import static com.example.util.TimeUtil.*;

public class uu {
    @SneakyThrows
    public static void main(String[] args) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = simpleDateFormat.format(new Date());
        String yesterday = getNewEndtime(nowTime, 1);

        Integer year = 2021;
        Integer month = 2;

        List<String> list = getYearMonth(year, month);
        List<Map<String, Object>> allList = new ArrayList<>();
        Integer monthDay = dayByMonth(year, month);
        String period = "[{\"2021-01-01\":3},{\"2021-01-02\":3},{\"2021-01-03\":3},{\"2021-01-04\":3},{\"2021-01-05\":3},{\"2021-01-06\":3},{\"2021-01-07\":3},{\"2021-01-08\":3},{\"2021-01-09\":3},{\"2021-01-10\":3},{\"2021-01-11\":3},{\"2021-01-12\":3},{\"2021-01-13\":3},{\"2021-01-14\":3},{\"2021-01-15\":3},{\"2021-01-16\":3},{\"2021-01-17\":3},{\"2021-01-18\":3},{\"2021-01-19\":3},{\"2021-01-20\":3},{\"2021-01-21\":3},{\"2021-01-22\":3},{\"2021-01-23\":3},{\"2021-01-24\":3},{\"2021-01-25\":3},{\"2021-01-26\":3},{\"2021-01-27\":3},{\"2021-01-28\":3},{\"2021-01-29\":3},{\"2021-01-30\":3},{\"2021-01-31\":3},{\"2021-02-01\":3},{\"2021-02-02\":3},{\"2021-02-03\":3},{\"2021-02-04\":3},{\"2021-02-05\":3},{\"2021-02-06\":3},{\"2021-02-07\":3},{\"2021-02-08\":3},{\"2021-02-09\":3},{\"2021-02-10\":3}]";
        List<Map<String, Object>> periodList = JSONArray.fromObject(period);

        String last = "[{day=11, dayList=[2021-01-01, 2021-01-02, 2021-01-03, 2021-01-04, 2021-01-05, 2021-01-06, 2021-01-07, 2021-01-08, 2021-01-09, 2021-01-10, 2021-01-11]}, {day=1, dayList=[2021-01-12]}, {day=8, dayList=[2021-01-16, 2021-01-17, 2021-01-18, 2021-01-19, 2021-01-20, 2021-01-21, 2021-01-22, 2021-01-23]}]";
        Integer serviceType = 0;
        List<Map<String, Object>> lastServedList = servedList11(last);
        System.out.println("lastList = " + lastServedList);

        String now = "[]";
        List<Map<String, Object>> nowServedList = servedList11(now);

        System.out.println("nowServedList = " + nowServedList);

        String next = "[]";
        List<Map<String, Object>> nextServedList = servedList11(next);


        Map<String, Object> lastNextPeriodMap = lastNextPeriod(periodList, month, yesterday, year);

        //搜索月份上一个月的档期
        List<Map<String, Object>> lastPeriodList = (List<Map<String, Object>>) lastNextPeriodMap.get("lastPeriodList");
        //搜索月份的档期
        List<Map<String, Object>> nowPeriodList = (List<Map<String, Object>>) lastNextPeriodMap.get("nowPeriodList");
        //下一个月的档期
        List<Map<String, Object>> nextPeriodList = (List<Map<String, Object>>) lastNextPeriodMap.get("nextPeriodList");


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
        System.out.println("nullLastMonth = " + nullLastMonth);
        List<Map<String, Object>> lastPeriodServed = twoList(nullLastMonth, lastPeriodList);
        System.out.println("lastPeriodServed = " + lastPeriodServed);
        lastMonth = twoList(lastPeriodServed, lastServedList);
        System.out.println("lastMonth =_______________________ " + lastMonth);
        allList.addAll(lastMonth);


        System.out.println("nullNowMonth = " + nullNowMonth);
        List<Map<String, Object>> nowPeriodServed = twoList(nullNowMonth, nowPeriodList);
        System.out.println("nowPeriodServed = " + nowPeriodServed);
        nowMonth = twoList(nowPeriodServed, nowServedList);
        System.out.println("nowMonth_____ = " + nowMonth);
        allList.addAll(nowMonth);


        nullNextMonth = nextMonth(list, allList.size());
        System.out.println("nullNextMonth =___________________ " + nullNextMonth);
        List<Map<String, Object>> nextPeriodServed = twoList(nullNextMonth, nextPeriodList);
        System.out.println("nextPeriodServed = " + nextPeriodServed);
        nextMonth=twoList(nextPeriodServed, nextServedList);
        System.out.println("nextMonth = " + nextMonth);
        allList.addAll(nextMonth);

        System.out.println("allList = " + allList);


    }

}
