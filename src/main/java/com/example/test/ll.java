package com.example.test;

import java.text.SimpleDateFormat;
import java.util.*;

public class ll  {
//    public void timerRun() {
//
//        SimpleDateFormat timeS = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
//
//        // 一天的毫秒数
//        //long daySpan = 24 * 60 * 60 * 1000;
//        long daySpan =  10000;
//        // 规定的每天时间15:33:30运行
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 10:14:00");
//        // 首次运行时间
//        try {
//            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
//            // 如果今天的已经过了 首次运行时间就改为明天
//            if (System.currentTimeMillis() > startTime.getTime()){
//                startTime = new Date(startTime.getTime() + daySpan);
//            }
//            Timer t = new Timer();
//            TimerTask task = new TimerTask() {
//                @Override
//                public void run() {
//                    System.out.print("定时器执行________________");
//                    String time = timeS.format(new Date());
//                    System.out.println("time = " + time);
//                }
//            };
//            // 以每24小时执行一次
//            t.schedule(task, startTime, daySpan);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public static void main(String[] args) {

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

        System.out.println("list = " + list);



    }

   
}
