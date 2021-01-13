package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.entiy.Bussiness;
import com.example.entiy.Contact;
import com.google.gson.JsonObject;

public interface BussinessService {
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
     * @description: 上架下架
     * @param: bussiness
     * @return:
     */
    int updateShelf(Integer id,Integer shelf);


    /**
     * @description: 拉黑
     * @param: bussiness
     * @return:
     */
    int updateIsblack(Integer id,Integer isblack);
}
