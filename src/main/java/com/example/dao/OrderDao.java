package com.example.dao;

import com.example.entiy.Matorn;
import com.example.entiy.Order_demand;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
/**
 * @description: 订单管理接口
 * @param:
 * @return:
 */
public interface OrderDao {


    /**
     * @description:
     * @param: order_demand
     */
    int getForId();


    /**
     * @description: 添加订单需求
     * @param: order_demand
     */
    int saveDemand(Order_demand order_demand);
    /**
     * @description: 修改订单需求
     * @param: order_demand
     */
    int updateDemand(Order_demand order_demand);



    /**
     * @description: 经理人小程序查询所有月嫂订单
     * @param: order_demand
     *
     */
    List<Map<String ,Object>>  managerOrder(String json) ;

    /**
     * @description: 后台订单列表
     * @param:
     *
     */
     Map<String ,Object> allOrder(String json) ;

    /**
     * @description: 后台订单筛选条件
     * @param:
     *
     */
    Map<String ,Object> orderCondition() ;

    /**
     * @description: 后台订单居家会所成单数，天数
     * @param:
     *
     */
    Map<String ,Object> homeAndClub(String json) ;


}
