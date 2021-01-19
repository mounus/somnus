package com.example.util;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.apache.commons.collections.map.HashedMap;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.ELState;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.util.HolidyUtil.getDays;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;


/**
 * 计算客户居家，月子会所服务天数价格统计服务
 * 2020年11月19日16:07:20
 */
public class PriceUtil {


    /**
     * 计算两个搜索时间之间有多少服务天数
     *
     * @param work_states
     * @param monthFirst
     * @param monthLast
     * @param start_time
     * @param end_time
     * @param nowTime
     * @return
     */
    public static List<String> getDayList(Integer work_states, String monthFirst, String monthLast, String start_time, String end_time, String nowTime) {

        List<String> mList = getDays(monthFirst, monthLast);
        List<String> sList = new ArrayList<>();
        if (work_states == 0) {
            return sList;
        }
        if (work_states == 1) {
            sList = getDays(getNewEndtime(start_time, -1), nowTime);
        }
        if (work_states == 2) {
            sList = getDays(getNewEndtime(start_time, -1), end_time);
        }

        mList.retainAll(sList);

        return mList;
    }

    public static Map<String, Object> getClubPrice(String timeType, Integer workStates, List<Map<String, Object>> hospitalList, List<Map<String, Object>> clubList, List<String> dayList) {
        Map<String, Object> priceMap = new HashedMap();
        Integer hospitalDay = 0;
        Integer clubDay = 0;
        Integer trueHospitalPrice = 0;
        Integer trueClubPrice = 0;

        Integer hospital_onePrice = 220;
        Integer club_onePrice = 200;

        if (timeType.equals("全程")) {
            if (workStates == 0) {

            }
            if (workStates == 1) {
                if (hospitalList.get(hospitalList.size() - 1).get("confirm_time") != null) {
                    //有医院回岗时间
                    String hospital_confirm = hospitalList.get(hospitalList.size() - 1).get("confirm_time").toString();
                    for (int i = 0; i < dayList.size(); i++) {
                        if (dayList.get(i).equals(hospital_confirm)) {
                            hospitalDay = i + 1;
                        }
                    }
                    clubDay = dayList.size() - hospitalDay;
                } else {
                    //没有医院回岗时间
                    hospitalDay = dayList.size();
                }

            }
            if (workStates == 2) {
                //医院回岗时间
                String hospital_confirm = hospitalList.get(hospitalList.size() - 1).get("confirm_time").toString();

                for (int i = 0; i < dayList.size(); i++) {
                    if (dayList.get(i).equals(hospital_confirm)) {
                        hospitalDay = i + 1;
                    }
                }
                clubDay = dayList.size() - hospitalDay;

            }

        }

        if (timeType.equals("月子会所服务")) {
            clubDay = dayList.size();
        }
        if (timeType.equals("医院服务")) {
            hospitalDay = dayList.size();
        }

        trueHospitalPrice = hospitalDay * hospital_onePrice;
        trueClubPrice = clubDay * club_onePrice;
        priceMap.put("hospitalDay", hospitalDay);
        priceMap.put("clubDay", clubDay);
        priceMap.put("trueHospitalPrice", trueHospitalPrice);
        priceMap.put("trueClubPrice", trueClubPrice);

        return priceMap;
    }

    public static List<String> getOneMatornDay(Integer order_states, String monthFirst, String monthLast, String arrival_time, String confirm_time, String nowTime) {

        List<String> mList = getDays(monthFirst, monthLast);
        List<String> sList = new ArrayList<>();

        if (order_states == 2) {
            sList = getDays(getNewEndtime(arrival_time, -1), nowTime);
        }
        if (order_states == 3) {
            sList = getDays(getNewEndtime(arrival_time, -1), confirm_time);
        }

        mList.retainAll(sList);

        return mList;
    }
}
