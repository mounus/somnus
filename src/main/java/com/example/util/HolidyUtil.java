package com.example.util;



import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HolidyUtil {




    /**
     *
     * 方法描述：方法描述：获取节假日 访问接口，根据返回值判断当前日期是否为工作日，
     * 返回结果：检查具体日期是否为节假日，工作日对应结果为 {"20200101":"0"}, 休息日对应结果为 {"20200101":"1"}, 节假日对应的结果为 {"20200101":"2"}；
     * 2018年4月3日上午11:26:40
     */
    public static Map<String,Object> getHoliday(String  httpUrl) {
        BufferedReader reader = null;
        Map<String,Object> result=null;
        StringBuffer sbf = new StringBuffer();
        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
            }
            reader.close();
           // result = (Map) JSON.parse(String.valueOf(sbf));
            result = (Map) JSON.parse(String.valueOf(sbf));
        } catch (Exception e) {

        }

        return result;
    }



    public static Integer getCount(String starttime,String endtime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = null;
        Date dEnd = null;
        try {
            dBegin = sdf.parse(starttime);
            dEnd = sdf.parse(endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> lDate = new ArrayList<String>();

       // String dc = "http://www.easybots.cn/api/holiday.php?d=";
       String dc = " https://tool.bitefu.net/jiari/?d=";

        List<String> count = new ArrayList<String>();

        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        lDate.add(sd.format(dBegin));
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

        Map<String, Object> dateFlag = null;
              String datelist= String.join(",",lDate);
                   dateFlag=getHoliday(dc+datelist);
//            System.out.println("_____"+dateFlag);
//            for (Object map : dateFlag.entrySet()) {        //http://www.easybots.cn/api/holiday.php?d
//
//                if (((Map.Entry) map).getValue().toString().equals("2")) {
//                    count.add((String) ((Map.Entry) map).getValue());
//                } else {
//                }
//            }

        Integer num=0;//dateFlag l两个时间相同返回null，

        if (dateFlag!=null) {
            for (Object map : dateFlag.entrySet()) {        //https://tool.bitefu.net/jiari/?d=

                if (Integer.valueOf(((Map.Entry) map).getValue().toString()).equals(2) ) {
                    count.add(((Map.Entry) map).getValue().toString());
                } else {
                }
            }
          num = count.size();
        }else {
            num=0;
        }

        return num;

    }
    /**
     * 获取过去天数的时间字符串
     *
     * @return
     * @author Somnus
     */

    public static  String getNewEndtime(String endtime,Integer count){

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date date=null;
        try {
            date = format.parse(endtime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //过去七天
        c.setTime(date);
        c.add(Calendar.DATE,-count);//过去天数
        Date d = c.getTime();
       return format.format(d);

    }

    /**
     * 获取两个日期之间的所有日期
     *
     * @param startTime
     *            开始日期
     * @param endTime
     *            结束日期
     * @return
     */
    public static List<String> getDays(String startTime, String endTime) {

        // 返回的日期集合
        List<String> days = new ArrayList<String>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(dateFormat.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return days;
    }



}
