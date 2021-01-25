package com.example.util;

import groovy.grape.GrapeIvy;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.util.HolidyUtil.getDays;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;


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
    public static List<List> getOneAllMonth(Integer year) {

        List<List> list = new ArrayList<>();

        for (int i = 1; i < 13; i++) {
            String monthString = "";
            String dayString = "";
            List<String> monthList = new ArrayList<>();
            if (i > 10) {
                monthString = String.valueOf(i);
            } else {
                monthString = "0" + String.valueOf(i);
            }
            Integer day = dayByMonth(year, i);
            dayString = String.valueOf(day);

            String getFirstDay = String.valueOf(year) + "-" + monthString + "-" + "01";
            String getLastDay = String.valueOf(year) + "-" + monthString + "-" + dayString;
            monthList.add(getFirstDay);
            monthList.add(getLastDay);
            list.add(monthList);

        }

        return list;
    }

    public static String getNowNext() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date); // 设置为当前时间
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1); // 设置为上一个月
        date = calendar.getTime();
        String accDate = format.format(date);
        return accDate;
    }

    /**
     * 根据年份，月份获取这个月的月头月尾
     *
     * @param year
     * @param month
     * @return
     */
    public static List<String> getYearMonth(Integer year, Integer month) {
        List<String> list = new ArrayList<>();
        String monthString = "";
        String dayString = "";
        if (month > 10) {
            monthString = String.valueOf(month);
        } else {
            monthString = "0" + String.valueOf(month);
        }
        Integer day = dayByMonth(year, month);
        dayString = String.valueOf(day);

        String getFirstDay = String.valueOf(year) + "-" + monthString + "-" + "01";
        String getLastDay = String.valueOf(year) + "-" + monthString + "-" + dayString;
        list.add(getFirstDay);
        list.add(getLastDay);
        return list;
    }

    /**
     * 获取星期几
     *
     * @param
     * @return
     */
    @SneakyThrows
    public static Integer getWeek(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekday = calendar.get(Calendar.DAY_OF_WEEK);

        Integer week = 0;
        if (weekday == 1) {
            week = 7;
        } else if (weekday == 2) {
            week = 1;
        } else if (weekday == 3) {
            week = 2;
        } else if (weekday == 4) {
            week = 3;
        } else if (weekday == 5) {
            week = 4;
        } else if (weekday == 6) {
            week = 5;
        } else if (weekday == 7) {
            week = 6;
        }
        //不减1美式日历
        // week = week - 1;//中式日历

        return week;
    }

    /**
     * 11
     * 获取某月日历上，上一个月的时间,默认值给0
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

    /**
     * 11111
     * 搜索月份空白档期
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> onMonth(List<String> list, Integer monthDay) {
        List<Map<String, Object>> onList = new ArrayList<>();
        for (int i = 0; i < monthDay; i++) {
            Map<String, Object> timeMap = new HashedMap();
            timeMap.put(getNewEndtime(list.get(0), -i), 0);
            onList.add(timeMap);
        }
        return onList;
    }

    /**
     * 没有档期的月嫂
     * 获取这月下一月日历上的时间
     *
     * @param list
     * @return
     */
    public static List<Map<String, Object>> nextMonth(List<String> list, Integer monthDay) {
        List<Map<String, Object>> nextList = new ArrayList<>();
        monthDay = 42 - monthDay;
        for (int i = 1; i <= monthDay; i++) {
            Map<String, Object> timeMap = new HashedMap();
            timeMap.put(getNewEndtime(list.get(1), -i), 0);
            nextList.add(timeMap);
        }
        return nextList;
    }

    /**
     * 获取当前时间月
     *
     * @param date
     * @return
     */
    public static Integer getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        return month;
    }

    /**
     * 获取当前时间年
     *
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Integer year = calendar.get(Calendar.YEAR);
        return year;
    }



    /**
     * 搜索月份的档期第一天不是这个月的第一天
     * <p>
     * 档期的第一天
     *
     * @param time
     * @param day
     * @return
     */
    public static List<Map<String, Object>> yeMonth(String time, Integer day) {
        List<Map<String, Object>> yesterdayMonth = new ArrayList<>();
        for (int i = 1; i <= day; i++) {
            Map<String, Object> timeMap = new HashedMap();
            timeMap.put(getNewEndtime(time, -i), 0);
            yesterdayMonth.add(timeMap);
        }
        return yesterdayMonth;
    }

    /**
     * 获取月嫂搜索月份档期 ，搜索月份下一个月档期，搜索月份 当天到月底的档期
     *
     * @param periodList
     * @param month
     * @param yesterday
     * @return
     */
    @SneakyThrows
    public static Map<String, Object> lastNextPeriod(List<Map<String, Object>> periodList, Integer month, String yesterday, Integer year) {
        Map<String, Object> map = new HashedMap();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //搜索月份上一个月的档期
        List<Map<String, Object>> lastPeriodList = new ArrayList<>();
        //搜索月份的档期
        List<Map<String, Object>> nowPeriodList = new ArrayList<>();
        //搜索月份下一个月的档期
        List<Map<String, Object>> nextPeriodList = new ArrayList<>();


        for (Map<String, Object> m : periodList) {
            for (String k : m.keySet()) {
                Date kTime = simpleDateFormat.parse(k);
                Integer kMonth = getMonth(kTime);
                Integer kYear = getYear(kTime);
                Map<String, Integer> lastMap = lastMonth(year, month, 1);
                Integer lastMonth = Integer.valueOf(lastMap.get("lastMonth").toString());
                Integer lastYear = Integer.valueOf(lastMap.get("lastYear").toString());
                //搜索月份上一个月的档期
                if (kMonth == lastMonth&&kYear==lastYear) {
                    Map<String, Object> kMap = new HashedMap();
                    if (timeSize(k, yesterday) == 1 && !k.equals(yesterday)) {
                        kMap.put(k, m.get(k));
                    } else {
                        kMap.put(k, 0);
                    }
                    lastPeriodList.add(kMap);
                } else {
                }

                //搜索月份的档期
                if (kMonth == month&&kYear == year) {
                    Map<String, Object> kMap = new HashedMap();
                    if (timeSize(k, yesterday) == 1 && !k.equals(yesterday)) {
                        kMap.put(k, m.get(k));
                    } else {
                        kMap.put(k, 0);
                    }
                    nowPeriodList.add(kMap);
                } else {
                }

                Map<String, Integer> nextMap = lastMonth(year, month, -1);
                Integer nextMonth = Integer.valueOf(nextMap.get("lastMonth").toString());
                Integer nextYear= Integer.valueOf(nextMap.get("lastYear").toString());
                //搜索月份下一个的档期
                if (kMonth == nextMonth&&kYear==nextYear) {
                    Map<String, Object> kMap = new HashedMap();
                    if (timeSize(k, yesterday) == 1 && !k.equals(yesterday)) {
                        kMap.put(k, m.get(k));
                    } else {
                        kMap.put(k, 0);
                    }
                    nextPeriodList.add(kMap);
                } else {

                }
            }

        }
        map.put("lastPeriodList", lastPeriodList);
        map.put("nowPeriodList", nowPeriodList);
        map.put("nextPeriodList", nextPeriodList);
        System.out.println("lastPeriodList = " + lastPeriodList);

        System.out.println("nowPeriodList = " + nowPeriodList);
        System.out.println("nextPeriodList = " + nextPeriodList);
        
        return map;

    }


    /**
     * 获取搜索月份档期
     *
     * @param year
     * @param month
     * @param period
     * @param list
     * @param monthDay
     * @return
     */
    @SneakyThrows
    public static List<Map<String, Object>> newList(Integer year, Integer month, String period, List<String> list, Integer monthDay) {
        List<Map<String, Object>> allList = new ArrayList<>();

        List<Map<String, Object>> periodList = JSONArray.fromObject(period);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = simpleDateFormat.format(new Date());
        String yesterday = getNewEndtime(nowTime, 1);

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
//        nullLastMonth = lastMonth(list);
//        lastMonth = twoList(nullLastMonth, lastPeriodList);
//        allList.addAll(lastMonth);

        nullLastMonth = lastMonth(list);
        allList.addAll(lastMonth);

        if (nowPeriodList.size() > 0) {
            //搜索月份有档期
            nowMonth = twoList(nullNowMonth, nowPeriodList);
            allList.addAll(nowMonth);
        } else {
            //搜索月份没有档期
            allList.addAll(nullNowMonth);
        }

        //搜索月份下一个档期
//
//        nullNextMonth = nextMonth(list, allList.size());
//        nextMonth = twoList(nullNextMonth, nextPeriodList);
//        allList.addAll(nextMonth);

        nullNextMonth = nextMonth(list, allList.size());
        allList.addAll(nullNextMonth);

        return allList;

    }


    /**
     * 查询搜索月份有多少可服务天数
     *
     * @param period
     * @param month
     * @return
     */
    @SneakyThrows
    public static Integer dayCount(String period, Integer month,Integer year) {
        List<Map<String, Object>> periodList = JSONArray.fromObject(period);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = simpleDateFormat.format(new Date());
        String yesterday = getNewEndtime(nowTime, 1);
        List<Map<String, Object>> countList = new ArrayList<>();
        for (Map<String, Object> m : periodList) {
            for (String k : m.keySet()) {
                Date kTime = simpleDateFormat.parse(k);
                Integer kMonth = getMonth(kTime);
                Integer kYear = getYear(kTime);
                //搜索月份的档期
                Map<String, Object> kMap = new HashedMap();
                if (kMonth == month&& kYear==year) {
                    if (timeSize(k, nowTime) == 1 && !k.equals(nowTime)) {
                        kMap.put(k, m.get(k));
                        countList.add(kMap);
                    } else {
                    }
                } else {

                }

            }

        }

        return countList.size();
    }

    /**
     * 111
     * 获取一个月份的上一个月 ，下一个月月份
     *
     * @param year
     * @param month
     * @param count
     * @return
     */
    @SneakyThrows
    public static Map<String, Integer> lastMonth(Integer year, Integer month, Integer count) {

        Map<String, Integer> map = new HashedMap();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String str = null;
        if (month > 10) {
            str = year.toString() + month.toString();
        } else {
            str = year.toString() + "0" + month.toString();
        }

        Date date = format.parse(str);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        // -1 设置为上一个月
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - count);
        date = calendar.getTime();
        String accDate = format.format(date);
        Integer lastYear = Integer.valueOf(accDate.substring(0, 4).toString());
        Integer lastMonth = Integer.valueOf(accDate.substring(4).toString());
        map.put("lastYear", lastYear);
        map.put("lastMonth", lastMonth);
        return map;
    }

    /**
     * 111
     *
     * @param fList 搜索月份上一月，或者下一月的空白档期
     * @param sList 月嫂档期
     * @return
     */
    public static List<Map<String, Object>> twoList(List<Map<String, Object>> fList, List<Map<String, Object>> sList) {
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

    /**
     * 已服务天数 赋值
     *
     * @param served
     * @return
     */
    public static List<Map<String, Object>> servedList11(String served) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> servedList = JSONArray.fromObject(served);
        List<String> lastServed = new ArrayList<>();
        for (int i = 0; i < servedList.size(); i++) {
            List<String> ss = JSONArray.fromObject(servedList.get(i).get("dayList").toString());
            lastServed.addAll(ss);
        }
        for (int i = 0; i < lastServed.size(); i++) {
            Map<String, Object> map = new HashedMap();
            map.put(lastServed.get(i), 2);
            list.add(map);
        }

        return list;

    }

    /**
     * 已服务天数 赋值
     *
     * @param servedList
     * @return
     */
    public static List<Map<String, Object>> servedList(List<Map<String, Object>> servedList) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> lastServed = new ArrayList<>();
        for (int i = 0; i < servedList.size(); i++) {
            List<String> ss = JSONArray.fromObject(servedList.get(i).get("dayList").toString());
            lastServed.addAll(ss);
        }
        for (int i = 0; i < lastServed.size(); i++) {
            Map<String, Object> map = new HashedMap();
            map.put(lastServed.get(i), 2);
            list.add(map);
        }

        return list;

    }

    public static List<Map<String, Object>> newServedList(List<Map<String, Object>> nowList, List<Map<String, Object>> lastList, List<Map<String, Object>> nextList, String period, Integer month, Integer year) {
        List<Map<String, Object>> allList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowTime = simpleDateFormat.format(new Date());
        String yesterday = getNewEndtime(nowTime, 1);

        List<String> list = getYearMonth(year, month);

        Integer monthDay = dayByMonth(year, month);

        //搜索月份上一月的已服务天数
        List<Map<String, Object>> lastServedList = servedList(lastList);
        //搜索月份已服务天数
        List<Map<String, Object>> nowServedList = servedList(nowList);
        //搜索月份下一月已服务天数
        List<Map<String, Object>> nextServedList = servedList(nextList);

        List<Map<String, Object>> periodList = JSONArray.fromObject(period);

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
//        nullLastMonth = lastMonth(list);
//        List<Map<String, Object>> lastPeriodServed = twoList(nullLastMonth, lastPeriodList);
//        lastMonth = twoList(lastPeriodServed, lastServedList);
//        allList.addAll(lastMonth);

        nullLastMonth = lastMonth(list);
        allList.addAll(nullLastMonth);



        //搜索月份
        System.out.println("nowPeriodList = " + nowPeriodList);
        List<Map<String, Object>> nowPeriodServed = twoList(nullNowMonth, nowPeriodList);
        System.out.println("nowPeriodServed = " + nowPeriodServed);
        nowMonth = twoList(nowPeriodServed, nowServedList);
        System.out.println("nowPeriodServed = " + nowPeriodServed);
        allList.addAll(nowMonth);


//        nullNextMonth = nextMonth(list, allList.size());
//        List<Map<String, Object>> nextPeriodServed = twoList(nullNextMonth, nextPeriodList);
//        nextMonth = twoList(nextPeriodServed, nextServedList);
//        allList.addAll(nextMonth);

        nullNextMonth = nextMonth(list, allList.size());
        allList.addAll(nullNextMonth);
        return allList;

    }




}