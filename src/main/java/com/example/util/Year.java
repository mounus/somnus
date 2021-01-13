package com.example.util;

import org.thymeleaf.util.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Year {

    private final static int[] dayArr = new int[] { 20, 19, 21, 20, 21, 22, 23,
            23, 23, 24, 23, 22 };
    private final static String[] constellationArr = new String[] { "摩羯座",
            "水瓶座", "双鱼座", "白羊座", "金牛座", "双子座", "巨蟹座", "狮子座", "处女座", "天秤座",
            "天蝎座", "射手座", "摩羯座" };


/**
 * 根据出生日期计算属相和星座
 *
 * @param args
 */

    /**
     * Java通过生日计算星座
     *
     * @param month
     * @param day
     * @return
     */
    public static String getConstellation(int month, int day) {
        return day < dayArr[month - 1] ? constellationArr[month - 1]
                : constellationArr[month];
    }

    /**
     * 通过生日计算属相
     *
     * @param year
     * @return
     */
    public static String getYear(int year) {
        if (year < 1900) {
            return "未知";
        }
        int start = 1900;
        String[] years = new String[]{"子鼠", "丑牛", "寅虎", "卯兔", "辰龙", "巳蛇", "午马", "未羊",
                "申猴", "酉鸡", "戌狗", "亥猪"};
        return years[(year - start) % years.length];
    }

    /**
     * 通过生日计算年龄
     *
     * @param birthTime
     * @return
     */

    public static long getAge(String birthTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowtime=df.format(new Date());
        long age=0;
        try {
            age =((df.parse(nowtime).getTime()-df.parse(birthTime).getTime())/86400000)/365;//当前时间减去测试时间 这个的除以1000得到秒，相应的60000得到分，3600000得到小时

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  age;
    }

    /**
     * 随机生产客户姓氏
     *
     * @param
     * @return
     */
    public static String  getCustomerName(){

        String[] name=new String[]{"李女士","王女士","张女士","刘女士","陈女士","杨女士","赵女士","黄女士","周女士","吴女士","徐女士","孙女士","胡女士","朱女士","高女士","林女士","何女士","郭女士","马女士","罗女士","宋女士"};
        int a = (int)(Math.random()*21);
        String custom_name=name[a];

              return custom_name;
    }

    /**
     * 星座匹配三个合适的星座
     *
     * @param
     * @return
     */
      public  static  List<String> getConstellation(String constellation){
          List<String> constellationList=new ArrayList<String>();
          if ( constellation.equals("白羊座")) {
              constellationList.add("狮子座");
              constellationList.add("射手座");
              constellationList.add("双子座");
          }
          if ( constellation.equals("金牛座")) {
              constellationList.add("摩羯座");
              constellationList.add("处女座");
              constellationList.add("巨蟹座");
          }
          if ( constellation.equals("双子座")){
              constellationList.add("天秤座");
              constellationList.add("水瓶座");
              constellationList.add("狮子座");
          }
          if ( constellation.equals("巨蟹座")){
              constellationList.add("天蝎座");
              constellationList.add("双鱼座");
              constellationList.add("巨蟹座");
          }
          if ( constellation.equals("狮子座")){
              constellationList.add("白羊座");
              constellationList.add("双子座");
              constellationList.add("射手座");
          }
          if ( constellation.equals("处女座")){
              constellationList.add("金牛座");
              constellationList.add("摩羯座");
              constellationList.add("天蝎座");
          }
          if ( constellation.equals("天秤座")){
              constellationList.add("处女座");
              constellationList.add("射手座");
              constellationList.add("水瓶座");
          }
          if ( constellation.equals("天蝎座")){
              constellationList.add("巨蟹座");
              constellationList.add("双鱼座");
              constellationList.add("摩羯座");
          }
          if ( constellation.equals("射手座")){
              constellationList.add("白羊座");
              constellationList.add("狮子座");
              constellationList.add("天秤座");
          }
          if ( constellation.equals("摩羯座")){
              constellationList.add("金牛座");
              constellationList.add("处女座");
              constellationList.add("双鱼座");
          }
          if ( constellation.equals("水瓶座")){
              constellationList.add("双子座");
              constellationList.add("天秤座");
              constellationList.add("白羊座");
          }
          if ( constellation.equals("双鱼座")){
              constellationList.add("巨蟹座");
              constellationList.add("天蝎座");
              constellationList.add("摩羯座");
          }

          return constellationList;
      }



}