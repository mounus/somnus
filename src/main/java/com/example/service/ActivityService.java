package com.example.service;

import java.util.Map;

public interface ActivityService {

    /**
     * @description: 获取所有预约客户。
     * @return:
     */
    Map<String,Object> getAllReservation(String json);
    /**
     * @description: 获取所有来源链接。
     * @return:
     */
    Map<String,Object> getAllHref(String json);
    /**
     * @description: 添加一个活动来源链接
     * @param:
     *
     */
    int addHref(String json);
}
