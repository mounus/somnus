package com.example.dao;

import com.example.entiy.Origin;

import java.util.List;
import java.util.Map;
/**
 * @description: 月嫂管理接口
 * @param:
 * @return:
 */
public interface OriginDao {
    /**
     * @description: 向表中插入一条数据
     * @param: Origin
     * @return:
     */
    int save(Origin origin);//del

    /**
     * @description: 修改一条数据
     * @param: Origin
     * @return:
     */
    int update(Origin origin);//del

    /**
     * @description: 删除一条数据
     * @param: Origin
     * @return:
     */
    int delete(Integer mid);//del

    /**
     * @description: 删除一条数据
     * @param: Origin
     * @return:
     */
    int deleteOrder(String json );

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
