package com.example.dao;

import com.example.entiy.ConsultingOrder;
import com.example.entiy.Custom;
import com.example.entiy.Order_demand;
import com.example.entiy.SaleOrder;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
/**
 * @description: 后台客户管理接口
 * @param:
 * @return:
 */
public interface CustomAdminDao {


    /**
     * @description: 后台查询客户
     * @param:
     *
     */
    Map<String ,Object> getAllCustom(String json);

    /**
     * @description: 设置后台统计客户数量
     * @param:
     *
     */
   int setCustomCount(String json);
    /**
     * @description: 后台客户添加/修改/
     * @param:
     *
     */
    Map<String,Object> saveOrUpdateCustom(Custom custom);
    /**
     * @description: 后台查询条件集合
     * @param:
     *
     */
    Map<String ,Object> getAllCondition();

    /**
     * @description: 后台订单需求添加/修改/
     * @param:
     *
     */
    Map<String,Object> saveOrUpdateOrder(String json);
    /**
     * @description: 后台客户详情
     * @param:
     *
     */
    Map<String,Object> getOneCustom(String json);

    /**
     * @description: 后台添加客户咨询工单
     * @param:
     *
     */
    int addConsultingOrder(ConsultingOrder consultingOrder);
    /**
     * @description: 后台添加客户销售工单
     * @param:
     *
     */
    int addSaleOrder(SaleOrder saleOrder);

    /**
     * @description: 后台删除客户所有相关信息
     * @param:
     *
     */
    int deleteCustom(String json);
    /**
     * @description: 订单小程序订单列表。
     * @return:
     */
   Map<String, Object> orderList(String json) ;

    /**
     * @description:选择月嫂/二次确认选择
     * @param:
     *
     */
    int chooseMatorn(String json);
    /**
     * @description:修改订单地址
     * @param:
     *
     */
    int updateAddress(String json);

    /**
     * @description:提交派岗
     * @param:
     *
     */
    int submitOrder(String json);

    /**
     * @description:月嫂到岗
     * @param:
     *
     */
    int getArrival(String json);

    /**
     * @description:到岗之前月嫂换人
     * @param:
     *
     */
    int substitution(String json);
    /**
     * @description:月嫂到岗
     * @param:
     *
     */
    int getConfirm(String json);
    /**
     * @description:到岗之前重新推荐月嫂
     * @param:
     *
     */
    int getMatornNull(String json);
    /**
     * @description:开启服务
     * @param:
     *
     */
    int runService(String json);
    /**
     * @description:暂停服务
     * @param:
     *
     */
    int stopService(String json);
    /**
     * @description:修改业务员图片/电话
     * @param:
     *
     */
    Map<String,Object> updateBussiness(String json);
    /**
     * @description:订单续岗
     * @param:
     *
     */
    int continueOrder(String json);
}
