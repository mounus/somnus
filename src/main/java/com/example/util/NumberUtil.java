package com.example.util;

import org.apache.commons.collections.map.HashedMap;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.util.MonthUtil.daysBetween;

public class NumberUtil {


    /**
     * 生成月嫂工号
     */
    public static String getNumber(int mid) {


        String a = "HYX";
        String b = "";
        String c = "";
        if (mid == 0) {
            c = "00000";
        }
        if (mid > 0 && mid < 10) {
            b = "0000";
            c = a + b + mid;
        }
        if (mid > 10 && mid < 100) {
            b = "000";
            c = a + b + mid;
        }
        if (mid >= 100 && mid < 1000) {
            b = "00";
            c = a + b + mid;
        }
        if (mid >= 1000 && mid < 10000) {
            b = "0";
            c = a + b + mid;
        }
        if (mid >= 10000 && mid < 100000) {
            b = "";
            c = a + b + mid;
        }
        return c;
    }

    /**
     * 判断非p0晋级天数
     */
    public static int getDay(int day, String grade) {
        int p0 = 0;
        int p1 = 5;
        int p2 = 35;
        int p3 = 63;
        int p4 = 123;
        int p5 = 183;
        int p6 = 273;
        int p7 = 363;
        int p8 = 453;
        int a = 0;
        if (grade.equals("P0")) {
            if (day > p1) {
                a = day - p0;
            } else {
                a = 0;
            }

        }
        if (grade.equals("P1")) {
            if (day > p1) {
                a = day - p1;
            } else {
                a = 0;
            }

        }
        if (grade.equals("P2")) {
            if (day > p2) {
                a = day - p2;
            } else {
                a = 0;
            }
        }
        if (grade.equals("P3")) {
            if (day > p3) {
                a = day - p3;
            } else {
                a = 0;
            }
        }
        if (grade.equals("P4")) {
            if (day > p4) {
                a = day - p4;
            } else {
                a = 0;
            }
        }
        if (grade.equals("P5")) {
            if (day > p5) {
                a = day - p5;
            } else {
                a = 0;
            }
        }
        if (grade.equals("P6")) {
            if (day > p6) {
                a = day - p6;
            } else {
                a = 0;
            }
        }
        if (grade.equals("P7")) {
            if (day > p7) {
                a = day - p7;
            } else {
                a = 0;
            }
        }
        if (grade.equals("P8")) {
            if (day > p8) {
                a = day - p8;
            } else {
                a = 0;
            }
        }
        return a;
    }

    /**
     * 判断p0
     */
    public static int getNull(String creat_time) throws ParseException {

        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = date.format(new Date());//获取当前时间
        int day = daysBetween(creat_time, time);
        return day;
    }


    /**
     * 判断p0
     */
    public static int getGradeday(String grade) throws ParseException {
        Integer day = 0;

        if (grade.equals("P0")) {
            day = 0;
        }
        if (grade.equals("P1")) {
            day = 5;
        }
        if (grade.equals("P2")) {
            day = 35;
        }
        if (grade.equals("P3")) {
            day = 63;
        }
        if (grade.equals("P4")) {
            day = 123;
        }
        if (grade.equals("P5")) {
            day = 183;
        }
        if (grade.equals("P6")) {
            day = 273;
        }
        if (grade.equals("P7")) {
            day = 363;
        }
        if (grade.equals("P8")) {
            day = 453;
        }
        return day;
    }


    /**
     * 生成订单号
     */
    public static String getOrderNumber(String servie_type, String timetype, Integer count) {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        String number = "";
        number = "CU" + nowtime;

        if (servie_type.equals("月子会所服务")) {
            number = number + "C";
            if (timetype.equals("月子会所服务")) {
                number = number + "CC";
            }
            if (timetype.equals("医院服务")) {
                number = number + "CP";
            }
            if (timetype.equals("全程")) {
                number = number + "AA";
            }

        }
        if (servie_type.equals("居家服务")) {
            number = number + "HM";

            if (timetype.equals("长期")) {
                number = number + "L";
            }

            if (timetype.equals("短期居家")) {
                number = number + "M";
            }
            if (timetype.equals("短期医院")) {
                number = number + "P";
            }
        }

        count = count + 1;
        if (count == 0) {
            number = number + "0001";
        }
        if (count > 0 && count < 10) {
            number = number + "000" + count;
        }
        if (count >= 10 && count < 100) {
            number = number + "00" + count;
        }
        if (count >= 100 && count < 1000) {
            number = number + "0" + count;
        }

        return number;
    }

    /**
     * 转岗生成编号
     */
    public static String getNewOrder(Integer count) {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        String number = "";
        number = "CU" + nowtime;

        number = number + "CAB";//CAB转岗 医院到月子会所
        count = count + 1;
        if (count == 0) {
            number = number + "0001";
        }
        if (count > 0 && count < 10) {
            number = number + "000" + count;
        }
        if (count >= 10 && count < 100) {
            number = number + "00" + count;
        }
        if (count >= 100 && count < 1000) {
            number = number + "0" + count;
        }

        return number;
    }

    /**
     * 普通换人 生成编号
     */
    public static String newNumber(Integer count, String number) {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        String new_number = "";
        new_number = "CU" + nowtime;

        new_number = new_number + number;//number之前订单
        count = count + 1;
        if (count == 0) {
            new_number = new_number + "0001";
        }
        if (count > 0 && count < 10) {
            new_number = new_number + "000" + count;
        }
        if (count >= 10 && count < 100) {
            new_number = new_number + "00" + count;
        }
        if (count >= 100 && count < 1000) {
            new_number = new_number + "0" + count;
        }

        return new_number;
    }

    /**
     * 时间字符串
     * 返回 几月几日
     */
    public static String getMonth(String time) throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(time);

        String month = "";
        // 获取String字符串中的年
        SimpleDateFormat y = new SimpleDateFormat("yyyy");

        // 获取String字符串中的月
        SimpleDateFormat m = new SimpleDateFormat("MM");
        month = m.format(date) + "月";
        // 获取String字符串中的日
        SimpleDateFormat d = new SimpleDateFormat("dd");

        month = month + d.format(date) + "日";


        return month;
    }


    /**
     * 生成客户id
     */

    public static String getCustomNumber(Integer id) {
        String number = "KH";
        if (id > 0 && id < 10) {
            number = number + "0000" + id;
        }
        if (id > 10 && id < 100) {
            number = number + "000" + id;
        }
        if (id > 100 && id < 1000) {
            number = number + "00" + id;
        }
        if (id > 1000 && id < 10000) {
            number = number + "0" + id;
        }
        if (id > 10000 && id < 100000) {
            number = number + id;
        }
        return number.toString();

    }

    /**
     * 时间字符串
     * 返回 几月几日
     */
    public static String getYearMonth(String time) throws ParseException {


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(time);

        String month = "";
        // 获取String字符串中的年
        SimpleDateFormat y = new SimpleDateFormat("yyyy");
        month = y.format(date) + "年";
        // 获取String字符串中的月
        SimpleDateFormat m = new SimpleDateFormat("MM");
        month = month + m.format(date) + "月";
        // 获取String字符串中的日
        SimpleDateFormat d = new SimpleDateFormat("dd");

        month = month + d.format(date) + "日";


        return month;
    }


    /**
     * 时间字符串
     * 返回星期几
     */
    public static String getWeek(String time) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(time));
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }


    public static Map<String, Object> getGradeStates(String grade, Integer day) {

        int p0 = 0;
        int p1 = 5;
        int p2 = 35;
        int p3 = 63;
        int p4 = 123;
        int p5 = 183;
        int p6 = 273;
        int p7 = 363;
        int p8 = 453;


        Map<String, Object> map = new HashMap<String, Object>();
        if (grade.equals("P0")) {

            if (day >= p0 && day < p1) {
                map.put("grade", "P0");
                map.put("assess", 2);
            }
            if (day >= p1 && day < p2) {
                map.put("grade", "P1");
                map.put("assess", 2);
            }
            if (day >= p2) {
                map.put("grade", "P2");
                map.put("assess", 3);
            }

        }
        if (grade.equals("P1")) {
            if (day >= p1) {
                map.put("grade", "P2");
                map.put("assess", 2);
            }
            if (day < p1) {
                map.put("grade", "P1");
                map.put("assess", 2);
            }
            if (day >= p2) {
                map.put("grade", "P2");
                map.put("assess", 3);
            }
            if (day < p2 && day > p1) {
                map.put("grade", "P1");
                map.put("assess", 2);
            }

        }
        if (grade.equals("P2")) {
            if (day >= p2) {
                map.put("grade", "P2");
                map.put("assess", 3);
            } else {
                map.put("grade", "P2");
                map.put("assess", 2);
            }
        }
        if (grade.equals("P3")) {
            if (day >= p3) {
                map.put("grade", "P3");
                map.put("assess", 3);
            } else {
                map.put("grade", "P3");
                map.put("assess", 2);
            }
        }
        if (grade.equals("P4")) {
            if (day >= p4) {
                map.put("grade", "P4");
                map.put("assess", 3);
            } else {
                map.put("grade", "P4");
                map.put("assess", 2);
            }
        }
        if (grade.equals("P5")) {
            if (day >= p5) {
                map.put("grade", "P5");
                map.put("assess", 3);
            } else {
                map.put("grade", "P5");
                map.put("assess", 2);
            }
        }
        if (grade.equals("P6")) {
            if (day >= p6) {
                map.put("grade", "P6");
                map.put("assess", 3);
            } else {
                map.put("grade", "P6");
                map.put("assess", 2);
            }
        }
        if (grade.equals("P7")) {
            if (day >= p7) {
                map.put("grade", "P7");
                map.put("assess", 3);
            } else {
                map.put("grade", "P7");
                map.put("assess", 2);
            }
        }
        if (grade.equals("P8")) {
            if (day >= p8) {
                map.put("grade", "P8");
                map.put("assess", 3);
            } else {
                map.put("grade", "P8");
                map.put("assess", 2);
            }
        }
        return map;
    }


    /**
     * 月嫂新等级天数计算
     * date:2020年12月8日10:27:12
     */
    public static String getNewGrade(String grade, Integer day) {

        String new_grade = null;
        if (grade.equals("P")) {
            //等级是P不变
            new_grade = grade;
        } else {
            if (day == 0) {
                new_grade = "N";
            }
            if (day >= 1 && day <= 5) {
                new_grade = "H";
            }
            if (day >= 6 && day <= 35) {
                new_grade = "Y1";
            }
            if (day >= 36 && day <= 65) {
                new_grade = "Y2";
            }
            if (day >= 66 && day <= 95) {
                new_grade = "Y3";
            }
            if (day >= 96) {
                new_grade = "X";
            }

        }
        return new_grade;

    }

    /**
     * @param number      编号
     * @param service_day 订单天数
     * @param price       价格
     * @return 单价
     * 2020年11月6日11:41:59
     */
    public static Integer getNumberOnePrice(String number, Integer service_day, Integer price) {
        Integer onePrice = 0;

        if (number.equals("CCC") || number.equals("CAB")) {
            //月子会所价格
            onePrice = 200;
        }
        if (number.equals("CCP") || number.equals("CAA")) {
            //医院
            onePrice = 220;
        }
        if (number.equals("HML")) {
            onePrice = Integer.valueOf(price / service_day);
        }
        if (number.equals("HMS")) {
            onePrice = 100;
        }

        return onePrice;
    }
}
