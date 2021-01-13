package com.example.service;

import com.example.entiy.Question;
import com.example.entiy.Reservation;

import java.util.List;
import java.util.Map;

public interface ExcelServie {

    /**
     * @description: 导出月嫂姓名，工号，手机号，经理人，级别
     * @param: evaluate
     * @return:
     */
    List<Map<String,Object>> out_matorn(String somnus,String agent) ;

    /**
     * @description: 导出所有预约客户信息。
     * @return: java.util.List<com.example.reservation>
     */
     List<Reservation> outExcelReservation(Integer uid);

    /**
     * @description: 选择字段导出月嫂数据。
     * @return:
     */
    List<Map<String,Object>> outFiledMatorn(String json);

    /**
     * @description: 选择条件/字段导出客户数据。
     * @return:
     */
    List<Map<String,Object>> outFiledCustom(Integer uid,String origin,String origin_channel,String intention,Integer sname,Integer bname,String orderSpeed, String service_type,String timetype,String level,String returnType, String number,String powerFiled ,String start_time,String end_time);
    /**
     * @description: 添加月嫂体检报告pdf路径。
     * @return:
     */
    int addPDf(Integer mid,String url);
    /**
     * @description: 添加问题。
     * @return:
     */
    int addQuestion(Question question);
    /**
     * @description: 客户服务订单统计表。
     * @return:
     */
    List<Map<String,Object>> outCustomService (String json);

    /**
     * @description: 客户居家服务订单统计表。
     * @return:
     */
    List<Map<String,Object>> outCustomHome (String somnus,String monthFirst,String monthLast,Integer bid,String service_type,String timetype,String origin,String channel,Integer work_states, String number,Integer start);
    /**
     * @description: 客户会所服务订单统计表。
     * @return:
     */
    List<Map<String,Object>> outCustomClub (String somnus,String monthFirst,String monthLast,Integer bid,String service_type,String timetype,String origin,String channel,Integer work_states, String number,Integer start);
    /**
     * @description: 客户信息列表。
     * @return:
     */
    List<Map<String,Object>> outCustomInfo (String json);

    /**
     * @description: 月嫂管理列表导出。
     * @return:
     */
    List<Map<String,Object>> outNewMatorn (Integer uid,String zodiac,String grade,Integer shelf,Integer bid,String source,String origin, Integer isPrice,String powerFiled,String number);

    /**
     * @description: 导出员工列表。
     * @return:
     */
    List<Map<String,Object>> outUser (Integer cid,Integer did,Integer pid,String number);

}
