package com.example.util;

import groovy.grape.GrapeIvy;
import lombok.SneakyThrows;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.dayByMonth;
import static com.example.util.MonthUtil.timeSize;


public class TimeUtil {
    private static String[] parsePatterns = {
            "yyyy年MM月dd日", "yyyy年MM月d日", "yyyy年M月dd日", "yyyy年M月d日",
            "yyyy-MM-dd", "yyyy-MM-d", "yyyy-M-dd", "yyyy-M-d",
            "yyyy/MM/dd", "yyyy/MM/d", "yyyy/M/dd", "yyyy/M/d",
            "yyyyMMdd"};

    /**
     * 判断是否是时间类型
     * 并转换为 yyyy-MM-dd 格式
     *
     * @param string
     * @return
     */
    public static String parseDate(String string) {
        if (string == null) {
            return null;
        }
        try {
            Date date1 = DateUtils.parseDate(string, parsePatterns);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = sdf.format(date1);
            return dateString;
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取本年的第一天
     *
     * @return String
     **/
    public static String getYearStart(String year) {

        String time = year + "-01-01";
        return time;
    }

    /**
     * 获取本年的最后一天
     *
     * @return String
     **/
    public static String getYearEnd(String year) {
        String time = year + "-12-31 23:59:59";
        return time;
    }

    public static Date getFirstDay(int year, int month) {
        // 获取Calendar类的实例
        Calendar c = Calendar.getInstance();
        // 设置年份
        c.set(Calendar.YEAR, year);
        // 设置月份，因为月份从0开始，所以用month - 1
        c.set(Calendar.MONTH, month - 1);
        // 设置日期
        c.set(Calendar.DAY_OF_MONTH, 1);

        return c.getTime();
    }


    /**
     * 2020-01-01 到当前时间所有月份开头结尾
     *
     * @return String
     **/
    public static List<List> getAllMonth() {
        Integer year = 2020;
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowTime = time.format(new Date());


        //获取昨天时间
        nowTime = getNewEndtime(nowTime, 1);

        System.out.println("nowTime = " + nowTime);
        Integer year1 = Integer.valueOf(nowTime.substring(0, 4).toString());
        System.out.println("year1 = " + year1);
        Integer month = Integer.valueOf(nowTime.substring(5, 7).toString());


        Integer size = year1 - year;
        List<List> monthList = new ArrayList<>();

        for (int i = year; i < year + size + 1; i++) {
            if (i == year1) {
                //当前年份
                for (int j = 1; j < month + 1; j++) {
                    List<String> iMonth = new ArrayList<>();
                    String getFirstDay = time.format(getFirstDay(i, j));
                    Integer day = dayByMonth(i, j);
                    String monthString = "";
                    if (j < 10) {
                        monthString = "0" + String.valueOf(j);
                    } else {
                        monthString = String.valueOf(j);
                    }
                    String dayString = "";
                    if (j == month) {
                        dayString = nowTime.substring(8);
                    } else {
                        if (day < 10) {
                            dayString = "0" + day.toString();
                        } else {
                            dayString = day.toString();
                        }
                    }

                    String getLastDay = String.valueOf(i) + "-" + monthString + "-" + dayString.toString();
                    iMonth.add(getFirstDay);
                    iMonth.add(getLastDay);
                    monthList.add(iMonth);

                }

            } else {
                //不是当前年份
                for (int j = 1; j <= 12; j++) {
                    List<String> iMonth = new ArrayList<>();
                    String getFirstDay = time.format(getFirstDay(i, j));
                    Integer day = dayByMonth(i, j);
                    String monthString = "";
                    if (j < 10) {
                        monthString = "0" + String.valueOf(j);
                    } else {
                        monthString = String.valueOf(j);
                    }
                    String dayString = "";
                    if (day < 10) {
                        dayString = "0" + day.toString();
                    } else {
                        dayString = day.toString();
                    }
                    String getLastDay = String.valueOf(i) + "-" + monthString + "-" + dayString.toString();
                    iMonth.add(getFirstDay);
                    iMonth.add(getLastDay);
                    monthList.add(iMonth);
                }

            }


        }
        return monthList;
    }

    /**
     * 获取某一年所有月份开头语结尾
     *
     * @return String
     **/
    public static List<List> getOneAllMonth() {
        Integer year=2020;
        SimpleDateFormat timeS = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowTime= timeS.format(new Date());
        Integer year1=Integer.valueOf(nowTime.substring(0, 4).toString());
        Integer month = Integer.valueOf(nowTime.substring(5, 7).toString());
        List<List> list=new ArrayList<>();
        for (int i = year; i <=year1 ; i++) {

            if (i==year1){
                for (int j = 1; j <=month ; j++) {
                    List<Integer> monthList=new ArrayList<>();
                    monthList.add(i);
                    monthList.add(j);
                    list.add(monthList);
                }
            }else {

                for (int j = 1; j < 13; j++) {
                    List<Integer> monthList=new ArrayList<>();
                    monthList.add(i);
                    monthList.add(j);
                    list.add(monthList);
                }

            }

        }
           return list;
    }

    public static String getNowNext() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) +1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = format.format(date);
        return accDate;
    }

    public static List<String> getYearMonth(Integer year,Integer month) {
        List<String> list=new ArrayList<>();
        String monthString = "";
        String dayString = "";
        if (month>10){
            monthString=String.valueOf(month);
        }else{
            monthString="0"+String.valueOf(month);
        }
        Integer day = dayByMonth(year, month);
        dayString=String.valueOf(day);

        String getFirstDay =String.valueOf(year)+"-"+monthString+"-"+"01";
        String getLastDay =String.valueOf(year)+"-"+monthString+"-"+dayString;
        list.add(getFirstDay);
        list.add(getLastDay);

        return list;
    }


}
