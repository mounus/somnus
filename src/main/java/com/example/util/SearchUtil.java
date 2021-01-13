package com.example.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

import static com.example.util.PeriodUtil.getNewStart;

public class SearchUtil {
    /**
     * (推荐月嫂). <br/>
     * 符合档期的月嫂集合
     *
     * @return
     * @author Somnus
     */
    public static List<Integer> searchPeriod(List<Map<String, Object>> list, String start) {

        String starttime = getNewStart(start);

        List<Map<String, Object>> midlist = new ArrayList<Map<String, Object>>();

        for (int a = 0; a < list.size(); a++) {
            String aa = list.get(a).get("period").toString().replace(":\"1\"", "");
            Map<String, Object> li = new HashMap<String, Object>();
            li.put(aa, list.get(a).get("mid"));
            midlist.add(li);
        }

        List<Integer> mid = new ArrayList<Integer>();
        for (int i = 0; i < midlist.size(); i++) {
            for (String key : midlist.get(i).keySet()) {
                String a2 = key.toString().replace("{", "");
                String a3 = a2.toString().replace("}", "");
                List<String> keylist = new ArrayList<String>();
            keylist = Arrays.asList(a3.split(","));
            for (int a = 0; a < keylist.size(); a++) {
                if (keylist.get(a).equals("\"" + starttime + "\"")) {
                    mid.add(Integer.valueOf((Integer) midlist.get(i).get(key)));//Object转Integer
                    break;
                } else {
                }
            }
            }
        }
        return mid;
    }

    /**
     * (推荐月嫂). <br/>
     * 符合价格的月嫂集合
     *
     * @return
     * @author Somnus
     */
    public static List<Map<String,Object>> searchPrice(List<Map<String, Object>> list, Integer service_day, Integer price) {
        List<Map<String, Object>> midlist = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> li = new HashMap<String, Object>();
            li.put(list.get(i).get("grade").toString(), list.get(i).get("mid"));
            midlist.add(li);
        }

        List<Map<String,Object>> maxmid = new ArrayList<Map<String,Object>>();
        List<Map<String,Object>> minmid = new ArrayList<Map<String,Object>>();
        List<List> mid = new ArrayList<List>();//第0个 小于0.7 第二个大于0.7

        Map<String, Object> max = new HashMap<>();
        for (int i = 0; i < midlist.size(); i++) {

            for (String key : midlist.get(i).keySet()) {
            double oneprice = getOnePrice(key, service_day, price);
                Map<String, Object> min = new HashMap<>();
                if (oneprice <= 0.7) {
                    min.put("mid",midlist.get(i).get(key));
                    min.put("min","0");
                    minmid.add(min);
                } else {
                    min.put("mid",midlist.get(i).get(key));
                    min.put("min","1");
                    minmid.add(min);

                }
            }
        }
        return minmid;
    }


    /**
     * (推荐月嫂). <br/>
     * 符合价格的月嫂集合
     *
     * @return
     * @author Somnus
     */
    public static double getOnePrice(String grade, Integer service_day, Integer price) {
        double oneprice = 0;
        DecimalFormat df = new DecimalFormat("0.00");
        if (grade.equals("P0")) {
            oneprice = Double.parseDouble(df.format((float) 100 * (service_day-5) / price));
        }
        if (grade.equals("P1")) {
            oneprice = Double.parseDouble(df.format((float) 100 * service_day / price));
        }
        if (grade.equals("P2")) {
            oneprice = Double.parseDouble(df.format((float) 120 * service_day / price));
        }
        if (grade.equals("P3")) {
            oneprice = Double.parseDouble(df.format((float) 150 * service_day / price));
        }
        if (grade.equals("P4")) {
            oneprice = Double.parseDouble(df.format((float) 200 * service_day / price));
        }
        if (grade.equals("P5")) {
            oneprice = Double.parseDouble(df.format((float) 240 * service_day / price));
        }
        if (grade.equals("P6")) {
            oneprice = Double.parseDouble(df.format((float) 280 * service_day / price));
        }
        if (grade.equals("P7")) {
            oneprice = Double.parseDouble(df.format((float) 320 * service_day / price));
        }
        if (grade.equals("P8")) {
            oneprice = 0.7;
        }
        return oneprice;
    }


}
