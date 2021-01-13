package com.example.service;

import java.util.List;
import java.util.Map;

public interface StatisticsService {
    /**
     * @description: 查询月嫂完整度
     * @param: uid
     * @return: com.example.entity.Period
     */
    List<Map<String,Object>> getComplete() ;

    /**
     * @description: 数据中心
     * @param: uid
     * @return: com.example.entity.Period
     */
    Map<String,Object> allStatistics(String json);
    /**
     * @description: 数据中心条件集合
     * @param: uid
     * @return: com.example.entity.Period
     */
    Map<String,Object> statisticsCondition( );

    /**
     * @description: 数据中心定时器
     * @param:
     *
     */
    void statisticsTiming( );
}
