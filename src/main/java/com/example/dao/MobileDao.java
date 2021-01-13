package com.example.dao;

import com.example.entiy.Reservation;

import java.util.List;
import java.util.Map;
/**
 * @description: 手机官网管理接口
 * @param:
 * @return:
 */
public interface MobileDao {
    /**
     * @description: 添加预约
     * @param: Reservation
     * @return:
     */
    int save(String  json);
    /**
     * @description: 获取预约所有信息。
     * @return: java.util.List<com.example.reservation>
     */
    public List<Reservation> reservationList();


    /**
     * @description: 查询渠道。
     * @return:
     */
    Map<String, Object> channel();

}
