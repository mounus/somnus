package com.example.util;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import groovy.util.IFileNameFinder;
import net.sf.json.JSONArray;

import javax.persistence.Convert;
import java.sql.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.getMonthSize;

public class MonthUtil {


    /**
     * 字符串的日期格式的计算
     */
    public static int daysBetween(String smdate, String bdate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.setTime(sdf.parse(smdate));
        long time1 = cal.getTimeInMillis();
        cal.setTime(sdf.parse(bdate));
        long time2 = cal.getTimeInMillis();
        long between_days = (time2 - time1) / (1000 * 3600 * 24);

        return Integer.parseInt(String.valueOf(between_days));


    }

    /**
     * 判断当前月有多少天
     */

    public static int dayByMonth(int year, int month) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        int year = cal.get(Calendar.YEAR);
//        int month = cal.get(Calendar.MONTH)+1;

        //  String str1=str.substring(0,4);
        //  String str2=str.substring(5,7);
        // Integer year = Integer.valueOf(str1);
        // Integer month = Integer.valueOf(str2);

        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            //对于2月份需要判断是否为闰年
            case 2:
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                    return 29;
                } else {
                    return 28;
                }

            default:
                return 0;
        }


    }

    /**
     * 根据给的时间获取此月的第一天
     * 传入参数为String，可传入：1、"201807"；2、"2018-07-01"
     *
     * @param str 账期
     * @return String
     * 当月的第一天
     */
    public static String getFirstDayByMonth(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cale = Calendar.getInstance();
        // 获取当月第一天和最后一天
        String firstday, lastday;
        cale.setTime(date);

        // 获取前月的第一天
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime()) + " 00:00:00";

        return firstday;
    }

    public static String getFirstDay(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cale = Calendar.getInstance();
        // 获取当月第一天和最后一天
        String firstday, lastday;
        cale.setTime(date);

        // 获取前月的第一天
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());

        return firstday;
    }

    /**
     * 根据给的时间获取此月的最后一天
     * 传入参数为String，可传入：1、"201807"；2、"2018-07-01"
     *
     * @param str 账期
     * @return String  yyyy-MM-dd  HH:MM:SS
     * 当月的最后一天
     */
    public static String getLastDay(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(str);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cale = Calendar.getInstance();
        // 获取当月第一天和最后一天
        String firstday, lastday;
        cale.setTime(date);

        // 获取前月的第一天
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());

        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime()) ;

        return lastday;
    }
    /**
     * 根据给的时间获取此月的最后一天
     * 传入参数为String，可传入：1、"201807"；2、"2018-07-01"
     *
     * @param str 账期
     * @return String
     * 当月的最后一天
     */
    public static String getLastDayByMonth(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(str);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cale = Calendar.getInstance();
        // 获取当月第一天和最后一天
        String firstday, lastday;
        cale.setTime(date);

        // 获取前月的第一天
        cale.add(Calendar.MONTH, 0);
        cale.set(Calendar.DAY_OF_MONTH, 1);
        firstday = format.format(cale.getTime());

        cale.add(Calendar.MONTH, 1);
        cale.set(Calendar.DAY_OF_MONTH, 0);
        lastday = format.format(cale.getTime()) + " 23:59:59";

        return lastday;
    }

    /**
     * 获取每个档期的map
     *
     * @return
     */
    public static Map<Integer, Object> getFalse(String start, String end) throws ParseException {

        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);

        Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(1, 0);
        map.put(2, 0);
        map.put(3, 0);
        map.put(4, 0);
        map.put(5, 0);
        map.put(6, 0);
        map.put(7, 0);
        map.put(8, 0);
        map.put(9, 0);
        map.put(10, 0);
        map.put(11, 0);
        map.put(12, 0);

        String aa = start.substring(0, 4);
        Integer year = Integer.valueOf(aa);
        String str1 = start.substring(5, 7);
        Integer month = Integer.valueOf(str1);

        String str2 = end.substring(5, 7);
        Integer month1 = Integer.valueOf(str2);

        int num = 0;

        if (month1 - month >= 0) {

            num = month1 - month;
        } else {

            num = month1 + 12 - month;
        }

        if (num == 0) {
            int day = daysBetween(start, end);

            int mouth_num = dayByMonth(year, month);//当前月的天数

            String b = numberFormat.format((float) day / (float) mouth_num * 100);
            int result = Integer.valueOf(b);

            map.put(month, result);

        }
        if (num == 1) {

            if (month1 > month) {
                String start1 = getLastDayByMonth(start);
                String end1 = getFirstDayByMonth(end);
                int day = daysBetween(start, start1);
                int mouth_num = dayByMonth(year, month);

                String n = numberFormat.format((float) day / (float) mouth_num * 100);
                int result = Integer.valueOf(n);
                int day1 = daysBetween(end1, end);
                int mouth_num1 = dayByMonth(year, month1);
                String n1 = numberFormat.format((float) day1 / (float) mouth_num1 * 100);
                int result1 = Integer.valueOf(n1);

                for (int i = 0; i < num; i++) {
                    if (month + i <= 12) {
                        map.put(month, result);

                        map.put(month1, result1);
                    }
                    if (month + i >= 12 && month - i >= 0) {
                        map.put(month1, result);

                        map.put(month, result1);

                    }
                }
            }
            if (month > month1) {
                String start1 = getLastDayByMonth(start);
                String end1 = getFirstDayByMonth(end);
                int day = daysBetween(start, start1);
                int mouth_num = dayByMonth(year, month);

                int day1 = daysBetween(end1, end);
                String n = numberFormat.format((float) day1 / (float) mouth_num * 100);
                int result = Integer.valueOf(n);
                int mouth_num1 = dayByMonth(year, month1);
                String n1 = numberFormat.format((float) day / (float) mouth_num1 * 100);
                int result1 = Integer.valueOf(n1);
                for (int i = 0; i < num; i++) {
                    if (month + i <= 12) {
                        map.put(month, result);
                        map.put(month + i, 100);
                        map.put(month1, result1);
                    }
                    if (month + i >= 12 && month - i >= 0) {
                        map.put(month1, result);
                        map.put(month1 - i, 100);
                        map.put(month, result1);

                    }
                }


            }

        }

        if (num > 1) {
            if (month1 > month) {
                String start1 = getLastDayByMonth(start);
                String end1 = getFirstDayByMonth(end);
                int day = daysBetween(start, start1);
                int mouth_num = dayByMonth(year, month);

                String n = numberFormat.format((float) day / (float) mouth_num * 100);
                int result = Integer.valueOf(n);
                int day1 = daysBetween(end1, end);
                int mouth_num1 = dayByMonth(year, month1);
                String n1 = numberFormat.format((float) day1 / (float) mouth_num1 * 100);
                int result1 = Integer.valueOf(n1);

                for (int i = 0; i < num; i++) {
                    if (month + i <= 12) {
                        map.put(month, result);
                        map.put(month + i, 100);
                        map.put(month1, result1);
                    }
                    if (month + i >= 12 && month - i >= 0) {
                        map.put(month1, result);
                        map.put(month1 - i, 100);

                        map.put(month, result1);

                    }
                }
            }
            if (month > month1) {
                String start1 = getLastDayByMonth(start);
                String end1 = getFirstDayByMonth(end);
                int day = daysBetween(start, start1);
                int mouth_num = dayByMonth(year, month);

                int day1 = daysBetween(end1, end);
                String n = numberFormat.format((float) day1 / (float) mouth_num * 100);
                int result = Integer.valueOf(n);
                int mouth_num1 = dayByMonth(year, month1);
                String n1 = numberFormat.format((float) day / (float) mouth_num1 * 100);
                int result1 = Integer.valueOf(n1);
                for (int i = 0; i < num; i++) {
                    if (month + i <= 12) {
                        map.put(month, result);
                        map.put(month + i, 100);
                        map.put(month1, result1);
                    }
                    if (month + i >= 12 && month - i >= 0) {
                        map.put(month1, result);
                        map.put(month1 - i, 100);
                        map.put(month, result1);

                    }
                }


            }


        }

        return map;

    }


    /**
     * 综合多个档期生成的map集合
     *
     * @return
     */
    public static Map<Integer, Object> getMonthMap(List<Map<Integer, Object>> list1) {

        int m1 = 0;
        int m2 = 0;
        int m3 = 0;
        int m4 = 0;
        int m5 = 0;
        int m6 = 0;
        int m7 = 0;
        int m8 = 0;
        int m9 = 0;
        int m10 = 0;
        int m11 = 0;
        int m12 = 0;
        for (int i = 0; i < list1.size(); i++) {

            int l1 = Integer.parseInt(list1.get(i).get(1).toString());
            m1 = l1 + m1;
            int l2 = Integer.parseInt(list1.get(i).get(2).toString());
            m2 = l2 + m2;
            int l3 = Integer.parseInt(list1.get(i).get(3).toString());
            m3 = l3 + m3;
            int l4 = Integer.parseInt(list1.get(i).get(4).toString());
            m4 = l4 + m4;
            int l5 = Integer.parseInt(list1.get(i).get(5).toString());
            m5 = l5 + m5;
            int l6 = Integer.parseInt(list1.get(i).get(6).toString());
            m6 = l6 + m6;
            int l7 = Integer.parseInt(list1.get(i).get(7).toString());
            m7 = l7 + m7;
            int l8 = Integer.parseInt(list1.get(i).get(8).toString());
            m8 = l8 + m8;
            int l9 = Integer.parseInt(list1.get(i).get(9).toString());
            m9 = l9 + m9;
            int l10 = Integer.parseInt(list1.get(i).get(10).toString());
            m10 = l10 + m10;
            int l11 = Integer.parseInt(list1.get(i).get(11).toString());
            m11 = l11 + m11;
            int l12 = Integer.parseInt(list1.get(i).get(12).toString());
            m12 = l12 + m12;

        }
        Map<Integer, Object> mon = new HashMap<Integer, Object>();
        mon.put(1, m1 * 3.6);
        mon.put(2, m2 * 3.6);
        mon.put(3, m3 * 3.6);
        mon.put(4, m4 * 3.6);
        mon.put(5, m5 * 3.6);
        mon.put(6, m6 * 3.6);
        mon.put(7, m7 * 3.6);
        mon.put(8, m8 * 3.6);
        mon.put(9, m9 * 3.6);
        mon.put(10, m10 * 3.6);
        mon.put(11, m11 * 3.6);
        mon.put(12, m12 * 3.6);
        Map<Integer, Object> month = new HashMap<Integer, Object>();
        Calendar cale = null;
        cale = Calendar.getInstance();
        int m = cale.get(Calendar.MONTH) + 1;
        for (int j = 0; j < 6; j++) {
            if (m + j <= 12) {
                month.put(m + j, mon.get(m + j));
            }
            if (m + j > 12) {
                month.put(m + j, mon.get(m + j - 12));
            }
        }


        return month;

    }

    /**
     * 字符串档期
     *
     * @return
     */
    public static List<Map<Integer, Object>> getPeriod(String period) throws ParseException {

        JSONArray jsonArray = JSONArray.fromObject(period);
        List<Map<String, Object>> list = (List) jsonArray;
        List<Map<Integer, Object>> list1 = new ArrayList<Map<Integer, Object>>();
        Map<Integer, Object> map = new HashMap<Integer, Object>();
        for (int i = 0; i < list.size(); i++) {
            map = getFalse(list.get(i).get("start").toString(), list.get(i).get("end").toString());
            list1.add(map);
        }

        return list1;

    }

    /**
     * 档期为空时
     *
     * @return
     */

    public static Map<Integer, Object> monthNull() {

        Map<Integer, Object> mon = new HashMap<Integer, Object>();
        mon.put(1, 0);
        mon.put(2, 0);
        mon.put(3, 0);
        mon.put(4, 0);
        mon.put(5, 0);
        mon.put(6, 0);
        mon.put(7, 0);
        mon.put(8, 0);
        mon.put(9, 0);
        mon.put(10, 0);
        mon.put(11, 0);
        mon.put(12, 0);

        Map<Integer, Object> month = new HashMap<Integer, Object>();
        Calendar cale = null;
        cale = Calendar.getInstance();
        int m = cale.get(Calendar.MONTH) + 1;
        for (int j = 0; j < 6; j++) {
            if (m + j <= 12) {
                month.put(m + j, mon.get(m + j));
            }
            if (m + j > 12) {
                month.put(m + j, mon.get(m + j - 12));
            }
        }

        return month;
    }

    /**
     * 两个时间段
     * 各自每个月所占的天数
     *
     * @return
     */
    public static Map<Integer, Object> getMonthDay(String start, String end) throws ParseException {


        Map<Integer, Object> map = new HashMap<Integer, Object>();
        map.put(1, 0);
        map.put(2, 0);
        map.put(3, 0);
        map.put(4, 0);
        map.put(5, 0);
        map.put(6, 0);
        map.put(7, 0);
        map.put(8, 0);
        map.put(9, 0);
        map.put(10, 0);
        map.put(11, 0);
        map.put(12, 0);

        String aa = start.substring(0, 4);
        Integer year = Integer.valueOf(aa);
        String str1 = start.substring(5, 7);
        Integer month = Integer.valueOf(str1);

        String str2 = end.substring(5, 7);
        Integer month1 = Integer.valueOf(str2);

        int num = 0;

        if (month1 - month >= 0) {

            num = month1 - month;
        } else {

            num = month1 + 12 - month;
        }

        if (num == 0) {
            //同一个月

            int day = daysBetween(start, end);
            map.put(month, day);

        }
        if (num == 1) {
            //间隔一个月

            if (month1 > month) {
                String start1 = getLastDayByMonth(start);
                String end1 = getFirstDayByMonth(end);
                int day = daysBetween(start, start1);
                int mouth_num = dayByMonth(year, month);

                int day1 = daysBetween(end1, end);
                int mouth_num1 = dayByMonth(year, month1);
                for (int i = 0; i < num; i++) {
                    if (month + i <= 12) {
                        map.put(month, day);
                        map.put(month1, day1);
                    }
                    if (month + i >= 12 && month - i >= 0) {
                        map.put(month1, day);
                        map.put(month, day1);

                    }
                }
            }
            if (month > month1) {
                String start1 = getLastDayByMonth(start);
                String end1 = getFirstDayByMonth(end);
                int day = daysBetween(start, start1);
                int mouth_num = dayByMonth(year, month);
                int day1 = daysBetween(end1, end);
                int mouth_num1 = dayByMonth(year, month1);
                for (int i = 0; i < num; i++) {
                    if (month + i <= 12) {
                        map.put(month, day);
                        map.put(month + i, 100);
                        map.put(month1, day1);
                    }
                    if (month + i >= 12 && month - i >= 0) {
                        map.put(month1, day);
                        map.put(month1 - i, 100);
                        map.put(month, day1);

                    }
                }


            }

        }

        if (num > 1) {
            //间隔两个月
            System.out.println("_____DD____");
            if (month1 > month) {
                System.out.println("dd");
                String start1 = getLastDayByMonth(start);
                String end1 = getFirstDayByMonth(end);
                int day = daysBetween(start, start1);
                int mouth_num = dayByMonth(year, month);
                int day1 = daysBetween(end1, end);
                int mouth_num1 = dayByMonth(year, month1);
                for (int i = 0; i < num; i++) {
                    if (month + i <= 12) {
                        map.put(month, day);
                        map.put(month + i, dayByMonth(year, month + i));
                        map.put(month1, day1);
                    }
                    if (month + i >= 12 && month - i >= 0) {
                        map.put(month1, day);
                        map.put(month1 - i, dayByMonth(year, month - i));

                        map.put(month, day1);

                    }
                }
            }
            if (month > month1) {
                String start1 = getLastDayByMonth(start);
                String end1 = getFirstDayByMonth(end);
                int day = daysBetween(start, start1);
                int mouth_num = dayByMonth(year, month);
                int day1 = daysBetween(end1, end);
                int mouth_num1 = dayByMonth(year, month1);

                for (int i = 0; i < num; i++) {
                    if (month + i <= 12) {
                        map.put(month, day);
                        map.put(month + i, dayByMonth(year, month + i));
                        map.put(month1, day1);
                    }
                    if (month + i >= 12 && month - i >= 0) {
                        map.put(month1, day);
                        map.put(month1 - i, dayByMonth(year, month - i));
                        map.put(month, day1);

                    }
                }


            }


        }

        return map;

    }

    /**
     * 综合多个map中每个月天数的综合
     *
     * @return
     */
    public static Map<Integer, Object> getAllMonthDay(List<Map<Integer, Object>> list1) {

        int m1 = 0;
        int m2 = 0;
        int m3 = 0;
        int m4 = 0;
        int m5 = 0;
        int m6 = 0;
        int m7 = 0;
        int m8 = 0;
        int m9 = 0;
        int m10 = 0;
        int m11 = 0;
        int m12 = 0;
        for (int i = 0; i < list1.size(); i++) {

            int l1 = Integer.parseInt(list1.get(i).get(1).toString());
            m1 = l1 + m1;
            int l2 = Integer.parseInt(list1.get(i).get(2).toString());
            m2 = l2 + m2;
            int l3 = Integer.parseInt(list1.get(i).get(3).toString());
            m3 = l3 + m3;
            int l4 = Integer.parseInt(list1.get(i).get(4).toString());
            m4 = l4 + m4;
            int l5 = Integer.parseInt(list1.get(i).get(5).toString());
            m5 = l5 + m5;
            int l6 = Integer.parseInt(list1.get(i).get(6).toString());
            m6 = l6 + m6;
            int l7 = Integer.parseInt(list1.get(i).get(7).toString());
            m7 = l7 + m7;
            int l8 = Integer.parseInt(list1.get(i).get(8).toString());
            m8 = l8 + m8;
            int l9 = Integer.parseInt(list1.get(i).get(9).toString());
            m9 = l9 + m9;
            int l10 = Integer.parseInt(list1.get(i).get(10).toString());
            m10 = l10 + m10;
            int l11 = Integer.parseInt(list1.get(i).get(11).toString());
            m11 = l11 + m11;
            int l12 = Integer.parseInt(list1.get(i).get(12).toString());
            m12 = l12 + m12;

        }
        Map<Integer, Object> mon = new HashMap<Integer, Object>();
        mon.put(1, m1);
        mon.put(2, m2);
        mon.put(3, m3);
        mon.put(4, m4);
        mon.put(5, m5);
        mon.put(6, m6);
        mon.put(7, m7);
        mon.put(8, m8);
        mon.put(9, m9);
        mon.put(10, m10);
        mon.put(11, m11);
        mon.put(12, m12);


        return mon;

    }

    /**
     * 根据月份返回月份第一天和最后一天
     *
     * @return
     */
    public static List<String> getFistMonth(String month) {

        SimpleDateFormat time = new SimpleDateFormat("yyyy");//设置日期格式
        String year = time.format(new Date());

        List<String> monthlist = new ArrayList<>();
        String firstday = "";
        String lastday = "";
        if (month.equals("一月")) {
            firstday = year + "-01-01 00:00:00";
            lastday = year + "-01-31 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("二月")) {
            int day = dayByMonth(Integer.valueOf(year), 2);
            firstday = year + "-02-01 00:00:00";
            lastday = year + "-02-" + day + " 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("三月")) {
            firstday = year + "-03-01 00:00:00";
            lastday = year + "-03-30 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("四月")) {
            firstday = year + "-04-01 00:00:00";
            lastday = year + "-04-31 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("五月")) {
            firstday = year + "-05-01 00:00:00";
            lastday = year + "-05-31 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("六月")) {
            firstday = year + "-06-01 00:00:00";
            lastday = year + "-06-30 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("七月")) {
            firstday = year + "-07-01 00:00:00";
            lastday = year + "-07-31 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("八月")) {
            firstday = year + "-08-01 00:00:00";
            lastday = year + "-08-31 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("九月")) {
            firstday = year + "-09-01 00:00:00";
            lastday = year + "-09-30 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("十月")) {
            firstday = year + "-10-01 00:00:00";
            lastday = year + "-10-31 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("十一月")) {
            firstday = year + "-11-01 00:00:00";
            lastday = year + "-11-30 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }
        if (month.equals("十二月")) {
            firstday = year + "-12-01 00:00:00";
            lastday = year + "-12-31 23:59:59";
            monthlist.add(firstday);
            monthlist.add(lastday);
        }

        return monthlist;
    }

    /**
     * 判断两个时间是否同月
     *
     * @param time1
     * @param time2
     * @return 0不是 1是
     * @throws ParseException
     */
    public static Integer isMonth(String time1, String time2) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        Date dTime1 = simpleDateFormat.parse(time1);
        Date dTime2 = simpleDateFormat.parse(time2);
        String str1 = simpleDateFormat.format(dTime1);
        String str2 = simpleDateFormat.format(dTime2);
        if (str1.equals(str2)) {
            return 1;
        } else {
            return 0;
        }
    }


    /**
     * 两个日期相差了几个月
     *
     * @param time1
     * @param time2
     * @return 0不是 1是
     * @throws ParseException
     */
    public static Integer getMonthSize(String time1, String time2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date dTime1 = sdf.parse(time1);
        Date dTime2 = sdf.parse(time2);
        String str1 = sdf.format(dTime1);
        String str2 = sdf.format(dTime2);
        Calendar bef = Calendar.getInstance();
        Calendar aft = Calendar.getInstance();
        bef.setTime(sdf.parse(str1));
        aft.setTime(sdf.parse(str2));
        int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
        int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
        return result + month;
    }


    /**
     * 查询当月服务多少天
     *
     * @param arrival_time
     * @return
     * @throws ParseException
     */
    public static Integer nowMonthDay(String arrival_time, String confirm_time) throws ParseException {

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowTime = time.format(new Date());
        Integer day = 0;
        if (confirm_time != null) {
            //回岗
            Integer states = isMonth(arrival_time, confirm_time);
            if (states == 0) {
                //不是同月
                //到岗时间至月底的天数为当月天数
                day = daysBetween(arrival_time, getLastDayByMonth(arrival_time));
            } else {
                //是同月
                day = daysBetween(arrival_time, confirm_time);
            }

        } else {
            //没有回岗
            Integer monthSize = getMonthSize(arrival_time, nowTime);
            if (monthSize == 0) {
                //到岗时间与当前时间是同月
                day = daysBetween(arrival_time, nowTime);
            }
            if (monthSize >= 1) {
                //到岗时间与当前时间相邻一个月
                //到岗时间与当前时间相隔两个月及两个以上
                //这两个的到岗时间至月底时间相同
                day = daysBetween(arrival_time, getLastDayByMonth(arrival_time));
            }


        }


        return day;
    }


    /**
     * 获取下一个月时间
     *
     * @param arrival_time
     * @return
     * @throws ParseException
     */
    public static String nextMonth(String arrival_time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date arrival = sdf.parse(arrival_time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(arrival);
        calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
        calendar.getTime();//获取一年前的时间，或者一个月前的时间

        String time = sdf.format(calendar.getTime());
        return time;

    }

    /**
     * 比较两个时间大小 0小，1大
     *
     * @param time1
     * @param time2
     * @return
     * @throws ParseException
     */
    public static Integer timeSize(String time1, String time2) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date time1_ = sdf.parse(time1);
        Date time2_ = sdf.parse(time2);
        if (time1_.before(time2_)) {
            //表示time1_小于time2_
            return 0;
        } else {
            return 1;
        }

    }


    /**
     * 获取次月天数
     *
     * @param arrival_time 到岗时间
     * @param confirm_time 回岗时间
     * @return
     * @throws ParseException
     */
    public static Integer nextMonthday(String arrival_time, String confirm_time) throws ParseException {

        Integer nextMonth_day = 0;//次月天数
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        //当前时间
        String nowTime = time.format(new Date());
        if (confirm_time != null) {
            //有回岗时间

            Integer monthSize = getMonthSize(arrival_time, confirm_time);
            if (monthSize == 0) {
                //到岗时间与回岗时间同月，次月时间为0

            }
            if (monthSize == 1) {
                //到岗时间与回岗时间相邻一个月

                //回岗时间的这个月的第一天，至回岗时间的天数为次月天数
                nextMonth_day = daysBetween(getFirstDayByMonth(confirm_time), confirm_time);
            }
            if (monthSize >= 2) {
                //到岗时间与回岗时间相隔两个个月及两个以上

                //获取到岗时间下一个月的月份
                String after_month = nextMonth(arrival_time);

                Integer year = Integer.valueOf(after_month.substring(0, 4).toString());
                Integer month = Integer.valueOf(after_month.substring(5, 7).toString());
                //下一个月有多少天
                Integer month_day = dayByMonth(year, month);
                nextMonth_day = month_day;

            }

        } else {
            //没有回岗时间

            Integer monthSize = getMonthSize(arrival_time, nowTime);
            if (monthSize == 0) {
                //到岗时间与当前时间同月，次月时间为0
            }
            if (monthSize == 1) {
                //到岗时间与回岗时间相邻一个月

                //当前时间的这个月的第一天，至当前时间的天数为次月天数
                nextMonth_day = daysBetween(getFirstDayByMonth(nowTime), nowTime);
            }
            if (monthSize >= 2) {
                //到岗时间与当前时间相隔两个个月及两个以上

                //获取到岗时间下一个月的月份
                String after_month = nextMonth(arrival_time);

                Integer year = Integer.valueOf(after_month.substring(0, 4).toString());
                Integer month = Integer.valueOf(after_month.substring(5, 7).toString());
                //下一个月有多少天
                Integer month_day = dayByMonth(year, month);
                nextMonth_day = month_day;
            }


        }


        return nextMonth_day;
    }


    /**
     * 判断两个时间是否相同
     * 在计算两个时间天数
     * 1.相同为0
     */
    public static int daysBetweenEquals(String smdate, String bdate) throws ParseException {

        Integer day = 0;
        if (smdate.equals(bdate)) {
            return day;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar cal = Calendar.getInstance();
            cal.setTime(sdf.parse(smdate));
            long time1 = cal.getTimeInMillis();
            cal.setTime(sdf.parse(bdate));
            long time2 = cal.getTimeInMillis();
            long between_days = (time2 - time1) / (1000 * 3600 * 24);
            day = Integer.parseInt(String.valueOf(between_days))+1;
        }
        return day;

    }


//    /**
//     * 查询当月服务多少天
//     * @param arrival_time
//     * @param confirm_time
//     * @return
//     * @throws ParseException
//     */
//    public static Integer nowMonthDay(String arrival_time,String confirm_time) throws ParseException {
//
//        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//        String nowTime = time.format(new Date());
//        Integer day=0;
//        if (confirm_time!=null){
//            //回岗
//            Integer states=isMonth(arrival_time,confirm_time);
//            if (states==0){
//                //不是同月
//                day=daysBetween(getFirstDay(confirm_time),confirm_time);
//            }else {
//                //是同月
//                day=daysBetween(arrival_time,confirm_time);
//            }
//
//        }else {
//            //没有回岗
//            Integer states=isMonth(arrival_time,nowTime);
//            if (states==0){
//                //不是同月
//                day=daysBetween(getFirstDay(nowTime),nowTime);
//            }else {
//                //是同月
//                day=daysBetween(arrival_time,nowTime);
//            }
//
//        }
//
//     return day ;
//    }
//
//
//    /**
//     * 获取下一个月时间
//     * @param arrival_time
//
//     * @return
//     * @throws ParseException
//     */
//    public static String nextMonth(String arrival_time) throws ParseException {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date arrival = sdf.parse(arrival_time);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(arrival);
//        calendar.add(Calendar.MONTH, 1);//当前时间前去一个月，即一个月前的时间
//        calendar.getTime();//获取一年前的时间，或者一个月前的时间
//
//        String time=sdf.format(calendar.getTime());
//        return time;
//
//    }
//
//    public static Integer nextMonth_day(Integer isServiceDay,String arrival_time) throws ParseException {
//        String return_time = getNewEndtime(arrival_time, -(isServiceDay));
//
//        Integer states = isMonth(arrival_time, return_time);
//        Integer after_day = 0;//到岗时间到这个月底的天数
//        Integer nextMonth_day = 0;
//        if (states == 0) {
//            //不是同月
//            //到岗时间这个月最后一天
//            String lastDay = getLastDayByMonth(arrival_time);
//
//            after_day = daysBetween(arrival_time, lastDay);
//            //获取下一个月的月份
//            String nextMonth = nextMonth(arrival_time);
//
//            Integer year = Integer.valueOf(nextMonth.substring(0, 4).toString());
//            Integer month = Integer.valueOf(nextMonth.substring(5, 7).toString());
//            //下一个月有多少天
//            Integer month_day = dayByMonth(year, month);
//            //下个月需要完成的天数
//            nextMonth_day = isServiceDay - after_day;
//            if (nextMonth_day > month_day) {
//                nextMonth_day = month_day;
//            } else {
//            }
//        } else {
//            //同月,下个月需要完成天数为0
//        }
//             return nextMonth_day;
//    }

}