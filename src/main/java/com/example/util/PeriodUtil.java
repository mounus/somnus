package com.example.util;

import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getDays;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;
import static com.example.util.TimeUtil.getYearMonth;
import static com.example.util.TimeUtil.parseDate;

public class PeriodUtil {

    public static Map<String, String> getAll(String month) throws ParseException {

        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        NumberFormat ns = NumberFormat.getNumberInstance();
        ns.setMaximumFractionDigits(0);

        JSONArray jsonArray = JSONArray.fromObject(month);
        List<Map<String, String>> list = (List) jsonArray;
        Map<String, String> map = new HashMap<String, String>();


        Calendar cale = null;
        cale = Calendar.getInstance();
        int m = cale.get(Calendar.MONTH) + 1;
        Map<String, String> mon = new HashMap<String, String>();

        Map<String, Object> newmap = new HashMap<String, Object>();

        for (String key : list.get(0).keySet()) {
            map.put(key, list.get(0).get(key));
        }

        Integer a2 = m + 5;
        String a3 = String.valueOf(a2);

        for (int i = 0; i < 6; i++) {
            Integer a = m + i;

            String a1 = String.valueOf(a);
            if (a > a2) {
                newmap.put(a3, 0);
            } else {
                newmap.put(a1, map.get(a1));

            }

        }


        for (int j = 0; j < 6; j++) {
            int a = m + j;
            String a1 = String.valueOf(a);

            Double ss = Double.parseDouble(newmap.get(a1).toString());

            if (ss > 1) {
                ss = 1.0;
            } else {
                ss = Double.parseDouble(newmap.get(a1).toString());

            }

            BigDecimal b = new BigDecimal(ss);

            b = b.setScale(2, BigDecimal.ROUND_DOWN); //小数位 直接舍去

            Double result = b.doubleValue() * 360;

            mon.put(a1, String.valueOf(ns.format(result)));

        }


        return mon;
    }


    public static String getNewMap(String month) {

        JSONArray jsonArray = JSONArray.fromObject(month);
        List<Map<String, String>> list = (List) jsonArray;
        Map<String, String> map = new HashMap<String, String>();

        Calendar cale = null;
        cale = Calendar.getInstance();
        int m = cale.get(Calendar.MONTH) + 1;

        Map<String, Object> newmap = new HashMap<String, Object>();

        for (String key : list.get(0).keySet()) {
            map.put(key, list.get(0).get(key));
        }


        Integer a2 = m + 5;
        String a3 = String.valueOf(a2);
        for (int i = 0; i < 6; i++) {
            Integer a = m + i;
            String a1 = String.valueOf(a);
            if (a > a2) {
                newmap.put(a3, 0);

            } else {
                if (String.valueOf(map.get(a1)).equals("null")) {
                    newmap.put(a1, 0);
                } else {
                    newmap.put(a1, map.get(a1));
                }

            }


        }
        JSONObject obj = JSONObject.fromObject(newmap);
        return obj.toString();

    }

    public static String getNewPeriod(String period) throws ParseException {
        com.alibaba.fastjson.JSONObject parseObject = com.alibaba.fastjson.JSONArray.parseObject(period);
        if (parseObject != null) {
            List<String> perlist = new ArrayList<String>();
            for (String key : parseObject.keySet()) {
                perlist.add(key);
            }
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String nowtime = time.format(new Date());
            String firstday = getFirstDay(nowtime);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date first = sdf.parse(firstday);
            Map<String, String> newmap = new HashMap<String, String>();
            for (int i = 0; i < perlist.size(); i++) {
                Date et = sdf.parse(perlist.get(i));
                if (et.before(first)) {

                } else {
                    newmap.put(perlist.get(i), "1");
                }

            }
            net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(newmap);

            if (obj.toString().equals("{}")) {
                return null;
            } else {
                return obj.toString();
            }


        } else {
            return null;
        }

    }

    /**
     * 获取数据库的period
     * 订单时间处理后返回的档期
     *
     * @return
     * @author Somnus
     */

    public static String getSqlPeriod(String starttime, String endtime, String period) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String start = getNewEndtime(starttime, 1);
        Date dBegin = null;
        Date dEnd = null;
        try {
            dBegin = sdf.parse(start);
            dEnd = sdf.parse(endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<String> lDate = new ArrayList<String>();
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }

        String p = period.replace("{", "[");
        String p1 = p.replace("}", "]");
        String p2 = p1.replace(":\"1\"", "");

        JSONArray jsonArray = JSONArray.fromObject(p2);
        List<String> list = (List) jsonArray;

        List<String> date = new ArrayList<String>();

        for (int i = 0; i < lDate.size(); i++) {
            StringBuffer sb = new StringBuffer();
            if (lDate.get(i).substring(5, 6).equals("0")) {
                sb.append(lDate.get(i).substring(0, 5));
                sb.append(lDate.get(i).substring(6, 8));
                if (lDate.get(i).substring(8, 9).equals("0")) {
                    sb.append(lDate.get(i).substring(9, 10));
                    date.add(sb.toString());
                } else {
                    sb.append(lDate.get(i).substring(8, 10));
                    date.add(sb.toString());
                }
            } else {
                sb.append(lDate.get(i).substring(0, 8));
                if (lDate.get(i).substring(8, 9).equals("0")) {
                    sb.append(lDate.get(i).substring(9, 10));
                    date.add(sb.toString());
                } else {
                    sb.append(lDate.get(i).substring(8, 10));
                    date.add(sb.toString());
                }
            }

        }
        List<String> ss = ListUtils.subtract(list, date);
        List<Map<String, Object>> newlist = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        for (int i = 0; i < ss.size(); i++) {
            Object o = "1";
            map.put("\"" + ss.get(i) + "\"", "\"" + 1 + "\"");
            newlist.add(map);
        }

        String newperiod = newlist.get(0).toString().replace("=", ":");
        return newperiod;
    }

    /**
     * 2020-01-01转换为数据库period里一样的格式
     * 2020-01-01转换2020-1-1
     *
     * @return 2020-1-1
     * @author Somnus
     */
    public static String getNewStart(String starttime) {

        StringBuffer sb = new StringBuffer();
        String date = "";
        if (starttime.substring(5, 6).equals("0")) {
            sb.append(starttime.substring(0, 5));
            sb.append(starttime.substring(6, 8));
            if (starttime.substring(8, 9).equals("0")) {
                sb.append(starttime.substring(9, 10));
                date = sb.toString();
            } else {
                sb.append(starttime.substring(8, 10));
                date = sb.toString();
            }
        } else {
            sb.append(starttime.substring(0, 8));
            if (starttime.substring(8, 9).equals("0")) {
                sb.append(starttime.substring(9, 10));
                date = sb.toString();
            } else {
                sb.append(starttime.substring(8, 10));
                date = sb.toString();
            }
        }
        return date;
    }


    /**
     * 当前时间后六个月的最后一天
     *
     * @return
     * @author Somnus
     */
    public static String getSixDate() {

        Calendar cal = Calendar.getInstance();//获取一个Calendar对象
        cal.setTime(new Date());


        cal.add(Calendar.MONTH, 5);//获取当前时间的六个月
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));//获取下一个月的最后一天
        Date preMonth = cal.getTime();//得到下六个月的最后一天  Wed Sep 30 09:18:34 CST 2020


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String timeFormat = sdf.format(preMonth);//2020-09-30
        return timeFormat;

    }

    /**
     * 新添加月嫂六个月档期period填充
     *
     * @return
     * @author Somnus
     */
    public static String getSixPeriod() {

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String creat_time = time.format(new Date());
        //{"2020-5-22":"1","2020-8-15":,"2020-10-41","2020}
        String sixDate = getSixDate();
        List<String> list = new ArrayList<>();
        list = getDays(creat_time, sixDate);

        List<String> list1 = new ArrayList<String>();

        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put(getNewStart(list.get(i)), "1");
            JSONObject jsonObject = JSONObject.fromObject(map);
            list1.add(jsonObject.toString());
        }
        String string = StringUtils.join(list1, ",");
        String s = StringUtils.remove(string, "{");
        String a = StringUtils.remove(s, "}");
        String period = "{" + a + "}";


        return period;
    }

    public static Map<String, String> gNull() {

        Map<String, String> month = new HashMap<String, String>();
        Calendar cale = null;
        cale = Calendar.getInstance();
        int m = cale.get(Calendar.MONTH) + 1;
        for (int j = 0; j < 6; j++) {
            int a = m + j;
            String a1 = String.valueOf(a);
            month.put(a1, "0");

        }
        return month;

    }

    /**
     * 新添加月嫂六个月月份占比month填充
     *
     * @return
     * @author Somnus
     */
    public static String getSixMonth() {

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String creat_time = time.format(new Date());

        //{"2020-5-22":"1","2020-8-15":,"2020-10-41","2020}
        String sixDate = getSixDate();
        List<String> list = new ArrayList<>();
        list = getDays(creat_time, sixDate);

        Calendar cale = null;
        cale = Calendar.getInstance();
        int m = cale.get(Calendar.MONTH) + 1;
        int c = 0;
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list2 = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (Integer.valueOf(list.get(i).substring(5, 7)) == m) {
                c++;//当前月份剩余天数
            }
        }
        Integer year = Integer.valueOf(creat_time.substring(0, 4));
        Integer month = Integer.valueOf(creat_time.substring(5, 7));
        Integer count = dayByMonth(year, month);
        double a = new BigDecimal((float) c / count).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

        map.put(String.valueOf(m), a);//当前月份占比
        for (int j = 1; j < 6; j++) {
            if (m + j <= 12) {
                map.put(String.valueOf(m + j), 1);
            }
            if (m + j > 12) {
                map.put(String.valueOf(m + j - 12), 1);
            }
        }
        JSONObject jsonObject = JSONObject.fromObject(map);
        String newMonth = "[" + jsonObject + "]";
        return newMonth;
    }

    /**
     * 档期period去除订单里预计回岗时间return_time
     * 之间的时间节点
     *
     * @return
     * @author Somnus
     */
    public static String getReturnPeriod(String period, String return_time) throws ParseException {
        String p = period.replace("{", "[");
        String p1 = p.replace("}", "]");
        String p2 = p1.replace(":\"1\"", "");

        JSONArray jsonArray = JSONArray.fromObject(p2);
        List<String> list = (List) jsonArray;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date first = sdf.parse(return_time);
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < list.size(); i++) {
            Date et = sdf.parse(list.get(i));
            if (et.before(first)) {
            } else {
                map.put(list.get(i), "1");
            }
        }
        net.sf.json.JSONObject obj = net.sf.json.JSONObject.fromObject(map);
        String newmap = "";
        if (obj.toString().equals("{}")) {
            newmap = "{}";
        } else {
            newmap = obj.toString();

        }

        return newmap;
    }

    //之前档期处理的方法
    //____________________________________________________________________


    //现在的档期处理方法
    //2021年1月26日09:06:54

    /**
     * 更新月嫂档期占比
     *
     * @param month
     * @return
     */
    public static Map<String, Object> getMonthMap(String month) {
        List<Map<String, Object>> monthList = JSONArray.fromObject(month);
        Calendar cale = null;
        cale = Calendar.getInstance();
        //当前月份
        int m = cale.get(Calendar.MONTH) + 1;
        Map<String, Object> newMap = new LinkedHashMap<>();

        Integer monthLast = m + 5;

        String a3 = String.valueOf(monthLast);
        int i = 0;
        for (Map<String, Object> map : monthList) {
            for (String k : map.keySet()) {
                Integer a = m + i;
                String a1 = String.valueOf(a);
                if (a > monthLast) {
                    newMap.put(a3, 0);
                } else {
                    if (String.valueOf(map.get(a1)).equals("null")) {
                        newMap.put(a1, 0);
                    } else {
                        newMap.put(a1, map.get(a1));
                    }

                }
                i = i + 1;

            }
        }
        return newMap;

    }


    /**
     * 获取月份占比度数
     *
     * @param map
     * @return
     */
    public static Map<String, Object> monthDegrees(Map<String, Object> map) {

        NumberFormat ns = NumberFormat.getNumberInstance();
        ns.setMaximumFractionDigits(0);
        Map<String, Object> newMap = new LinkedHashMap<>();
        for (String k : map.keySet()) {
            Double result = Double.parseDouble(map.get(k).toString()) * 360;
            newMap.put(k, ns.format(result));
        }
        return newMap;
    }

    /**
     * 月嫂没有档期 档期占比空白填充
     *
     * @return
     */
    public static Map<String, Object> getNull() {
        Map<String, Object> month = new HashMap<String, Object>();
        Calendar cale = null;
        cale = Calendar.getInstance();
        int m = cale.get(Calendar.MONTH) + 1;
        for (int j = 0; j < 6; j++) {
            Integer a = m + j;
            if (a > 12) {
                String a1 = String.valueOf(a - 12);
                month.put(a1, "0");
            } else {
                month.put(a.toString(), "0");
            }

        }
        return month;
    }

    /**
     * 新的月嫂档期清理
     * 清理上一月之前的档期
     *
     * @param period
     * @return
     */
    @SneakyThrows
    public static List<Map<String, Object>> clearPeriod(String period) {
        List<Map<String, Object>> periodList = JSONArray.fromObject(period);

        Calendar cale = null;
        cale = Calendar.getInstance();
        Integer month = cale.get(Calendar.MONTH) + 1;
        Integer year = cale.get(Calendar.YEAR);

        String time = getYearMonth(year, month).get(0);


        List<Map<String, Object>> list = new ArrayList<>();
        for (Map<String, Object> m : periodList) {
            for (String k : m.keySet()) {
                if (timeSize(k, time) == 1) {
                    Map<String, Object> kMap = new HashedMap();
                    String kTime = parseDate(k);
                    kMap.put(kTime, m.get(k));
                    list.add(kMap);
                } else {

                }
            }
        }

        return list;
    }

}
