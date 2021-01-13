package com.example.service;

import com.example.entiy.Origin;

import java.util.List;
import java.util.Map;

public interface OriginService {
    /**
     * @description: 向表中插入一条数据
     * @param: contact
     * @return:
     */
    int save(Origin origin);

    /**
     * @description: 修改一条数据
     * @param: Origin
     * @return:
     */
    int update(Origin origin);

    /**
     * @description: 删除一条数据
     * @param: Origin
     * @return:
     */
    int delete(Integer mid);

    /**
     * @description: 删除一条数据
     * @param: Origin
     * @return:
     */
    int deleteOrder(String json);
    /**
     * @description: 修改时间
     * @param: Origin
     * @return:
     */
    int updateTime(String json );
    /**
     * @description: 查询所有机构
     * @param: Origin
     * @return:
     */
    List<Map<String,Object>> allmechanism();
}
