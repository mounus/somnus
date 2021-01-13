package com.example.sd;

import net.sf.json.JSONArray;
import org.w3c.dom.css.ElementCSSInlineStyle;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;

public class oo {
    public static Integer getStopDay(List<Map<String, Object>> timeList, String monthFirst, String firstDay, String lastDay) throws ParseException {
        Integer sum_stopDay = 0;//总暂停天数
        Integer stopDay = 0;//单个订单暂停天数
        Integer oneStopDay = 0;//单次暂停天数
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        //当前时间
        String nowTime = time.format(new Date());

        System.out.println(" _____________________ ");
        System.out.println("timeList.size() = " + timeList.size());
        for (int i = 0; i < timeList.size(); i++) {
            Object stop_time = timeList.get(i).get("stop_time");
            Object run_time = timeList.get(i).get("run_time");
            if (stop_time != null && run_time != null) {
                //有暂停，开启时间集合
                String stop = stop_time.toString();
                String run = run_time.toString();

                List<String> stoplist = new ArrayList<>();
                List<String> runlist = new ArrayList<>();
                stoplist = JSONArray.fromObject(stop);
                runlist = JSONArray.fromObject(run);
                System.out.println("stoplist = " + stoplist);
                System.out.println("runlist = " + runlist);
                for (int j = 0; j < stoplist.size(); j++) {

                    if (j==runlist.size()){
                        //开启时间不暂停时间少一个
                        System.out.println(" 1-1111111111111111111" );
                        //算月底要加一，不然不包括最后一个一天
                        //不是月底，不包括
                        if (timeSize(lastDay,nowTime)==1){
                            oneStopDay = daysBetween(stoplist.get(j), nowTime);
                        }else {
                            oneStopDay = daysBetween(stoplist.get(j), lastDay)+1;
                        }
                        System.out.println("oneStopDay = " + oneStopDay);
                    }else {
                        //暂停时间与开启时间一样多
                        //暂停，开启时间与搜索时间同月
                        if (isMonth(stoplist.get(j), monthFirst) == 1 && isMonth(runlist.get(j), monthFirst) == 1) {
                            oneStopDay = daysBetween(stoplist.get(j), runlist.get(j));
                        }
                        //暂停与搜索时间同月,开启时间与搜索时间不同月
                        if (isMonth(stoplist.get(j), monthFirst) == 1 && isMonth(runlist.get(j), monthFirst) == 0) {
                            oneStopDay = daysBetween(stoplist.get(j), lastDay);
                        }
                        //暂停与搜索时间不同月,开启时间与搜索时间同月
                        if (isMonth(stoplist.get(j), monthFirst) == 0 && isMonth(runlist.get(j), monthFirst) == 1) {
                            //搜索时间上个最后一天
                            String last=getNewEndtime(firstDay,1);
                            System.out.println("last = " + last);
                               if(stoplist.get(j).equals(last)){

                               }else {

                               }
                            oneStopDay = daysBetween(firstDay, runlist.get(j)) + 1;
                        }
                        //暂停与搜索时间不同月,开启时间与搜索时间同月
                        if (isMonth(stoplist.get(j), monthFirst) == 0 && isMonth(runlist.get(j), monthFirst) == 0) {
                            if (isMonth(stoplist.get(j), runlist.get(j)) == 1) {
                                oneStopDay = 0;
                            } else {
                                oneStopDay = daysBetween(firstDay, lastDay) + 1;
                            }

                        }
                        System.out.println(" 1---222222222222222 " );
                        System.out.println("oneStopDay = " + oneStopDay);
                    }

                    stopDay = stopDay + oneStopDay;

                }

                sum_stopDay = sum_stopDay + stopDay;

            }


//                for (int j = 0; j < stoplist.size(); j++) {
//                    if (stoplist.size() == runlist.size()) {
//                        //暂停时间与开启时间一一对应，暂停时间有几个开启时间就有几个
//                        //暂停，开启时间与搜索时间同月
//                        if (isMonth(stoplist.get(j), monthFirst) == 1 && isMonth(runlist.get(j), monthFirst) == 1) {
//                            oneStopDay = daysBetween(stoplist.get(j), runlist.get(j));
//                        }
//                        //暂停与搜索时间同月,开启时间与搜索时间不同月
//                        if (isMonth(stoplist.get(j), monthFirst) == 1 && isMonth(runlist.get(j), monthFirst) == 0) {
//                            oneStopDay = daysBetween(stoplist.get(j), lastDay);
//                        }
//                        //暂停与搜索时间不同月,开启时间与搜索时间同月
//                        if (isMonth(stoplist.get(j), monthFirst) == 0 && isMonth(runlist.get(j), monthFirst) == 1) {
//                            System.out.println(" =ddddddddddd " );
//                            oneStopDay = daysBetween(firstDay, runlist.get(j)) + 1;
//                        }
//                        //暂停与搜索时间不同月,开启时间与搜索时间同月
//                        if (isMonth(stoplist.get(j), monthFirst) == 0 && isMonth(runlist.get(j), monthFirst) == 0) {
//                            if (isMonth(stoplist.get(j), runlist.get(j)) == 1) {
//                                oneStopDay = 0;
//                            } else {
//                                oneStopDay = daysBetween(firstDay, lastDay) + 1;
//                            }
//
//                        }
//                    }
//                   if (runlist.size()<j+1){
//                       System.out.println("adasdsds = " );
//                       oneStopDay=daysBetween(stoplist.get(j), nowTime);
//                   }
//                    stopDay = stopDay + oneStopDay;
//                }
//                sum_stopDay = sum_stopDay + stopDay;
//
//            }


            if (stop_time != null && run_time == null) {
                System.out.println(" 2222222222222222222 " );
                //暂停服务未开启服务
                String stop = stop_time.toString();
                List<String> stoplist = new ArrayList<>();
                stoplist = JSONArray.fromObject(stop);


                if(stoplist.get(0).equals(lastDay)){
                    //月底最后一天不算工作天数
                    //月底与暂停时间相同
                    oneStopDay=0;
                }else {
                    //有暂停时间，没有开启时间，
                    //搜索时间月底时间与当前时间比大小
                    if (timeSize(lastDay,nowTime)==1){
                        //不是月底，不包括
                        oneStopDay = daysBetween(stoplist.get(0), nowTime);
                    }else {
                        //算月底要加一，不然不包括最后一个一天
                        oneStopDay = daysBetween(stoplist.get(0), lastDay)+1;
                    }
                }

                sum_stopDay = sum_stopDay + oneStopDay;
            }

        }
        return sum_stopDay;

    }
}
