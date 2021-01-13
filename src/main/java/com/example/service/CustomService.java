package com.example.service;

import com.example.entiy.Custom;

import java.util.List;
import java.util.Map;

public interface CustomService {
    /**
     * @description: 向表中插入一条数据
     * @param: custom
     * @return:
     */
    int save (Custom custom);
    /**
     * @description: 修改一条数据
     * @param: custom
     * @return:
     */
    int update(Custom custom);

    /**
     * @description: 查询渠道。
     * @return:
     */
    Map<String,Object> channel();



    /**
     * @description: 选定月嫂。
     * @return:
     */
    int  chooseMatorn(String json);

    /**
     * @description: 查询月嫂。
     * @return:
     */
    List<Map<String, Object>> getOneMatorn(String json);
    /**
     * @description: 修改状态。
     * @return:
     */
    int updateStates(String json);

    /**
     * @description: 我的统计。
     * @return:
     */
    Map<String, Object> getStatistics(String json);
    /**
     * @description: 查询所有派岗需求。
     * @return:
     */
    List<Map<String, Object>> postList(String json);
    /**
     * @description: 推荐月嫂。
     * @return:
     */
   Map<String,Object> recommendMatorn(String json);

    /**
     * @description: 选择推荐月嫂。
     * @return:
     */
    int choiceMatorn (String json);

    /**
     * @description: 确认派岗。
     * @return:
     */
    int  confirmPost(String json);
    /**
     * @description: 派岗小程序查询姓名
     * @param: order_demand
     *
     */
    List<Map<String ,Object>> PostByName(String json);
    /**
     * @description: 订单推荐月嫂清空
     * @param: order_demand
     *
     */
    int getMatornNull (String json);
    /**
     * @description: 换人之后选择推荐月嫂。
     * @return:
     */
    int newMatorn (String json);

    /**
     * @description: 后台查询客户
     * @param: order_demand
     *
     */
 Map<String ,Object> getAllCustom(String json);
    /**
     * @description: 派岗/订单小程序月嫂派岗动态
     * @param: order_demand
     *
     */
    List<Map<String ,Object>> orderDynamic(String json);

    /**
     * @description: 订单小程序 意向类型客户
     * @param:
     *
     */
    List<Map<String ,Object>> intentionList(String json);
    /**
     * @description:修改订单动态信息
     * @param:
     *
     */
    int updateMessage(String json);

}
