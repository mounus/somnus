package com.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.entiy.Bussiness;
import com.example.entiy.Contact;
import com.google.gson.JsonObject;
/**
 * @description: 月嫂业务表接口
 * @param: bussiness
 * @return:
 */
public interface BussinessDao {
    /**
     * @description: 向表中插入一条数据
     * @param: bussiness
     * @return:
     */
    int save(String json);

    /**
     * @description: 修改一条数据
     * @param: bussiness
     * @return:
     */
    int update(String json);

    /**
     * @description: 修改assess状态
     * @param: bussiness
     * @return:
     */
    int updateAssess(Integer id);

    /**
     * @description: 修改shelf状态
     * @param: bussiness
     * @return:
     */
    int updateShelf(Integer id,Integer shelf);
    /**
     * @description: 修改isblack状态
     * @param: bussiness
     * @return:
     */
    int updateIsblack(Integer id,Integer isblack);


}
