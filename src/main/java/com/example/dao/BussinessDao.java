package com.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.example.dto.MatornDto;
import com.example.entiy.Bussiness;
import com.example.entiy.Contact;
import com.example.entiy.Score;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

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
    int save(String json);//del

    /**
     * @description: 修改一条数据
     * @param: bussiness
     * @return:
     */
    int update(String json);//del

    /**
     * @description: 修改assess状态
     * @param: bussiness
     * @return:
     */
    int updateAssess(Integer id);//del

    /**
     * @description: 修改shelf状态
     * @param: bussiness
     * @return:
     */
    int updateShelf(Integer id,Integer shelf);//del
    /**
     * @description: 修改isblack状态
     * @param: bussiness
     * @return:
     */
    int updateIsblack(Integer id,Integer isblack);//del
//________________________________________
    /**
     * @description: 全部月嫂列表
     * @param: bussiness
     * @return:
     */
    List<Map<String, Object>> allMatorn(String json);

    /**
     * @description: 月嫂小程序搜索筛选条件
     * @param:
     *
     */
    Map<String ,Object> bussinessCondition() ;

    /**
     * @description: 一个月嫂的详情
     * @param:
     *
     */
    Map<String ,Object> oneMatornDetail(String json ) ;


    /**
     * @description: 一个月嫂的所有订单
     * @param:
     *
     */
    List<Map<String,Object>> matornAllOrder(String json);
    /**
     * @description: 添加考核评估
     * @param:
     * @return: void
     */
    int saveScore(Score score);

    /**
     * @description: 查询模板评语
     * @param: mid
     * @return:
     */
    List<String> getComment( );
    /**
     * @description:月嫂全部信息
     * @param: id
     * @return: com.example.entity.Matorn/Origin/Contact/Bussiness
     */
    List<Map<String,Object>> getById(String json);

    /**
     * @description: 修改月嫂所有信息
     * @param:
     * @return:
     */
    int updateMatornDto(MatornDto matornDto );
    /**
     * @description: 添加考核评估
     * @param:
     * @return:
     */
    int updateScore(Score score );
    /**
     * @description: 修改档期
     * @param:
     * @return:
     */
    int updatePeriod(String json );
    /**
     * @description: 添加服务照片
     * @param:
     * @return:
     */
    int addPhoto(String json );

    /**
     * @description: 删除服务照片
     * @param:
     * @return:
     */
    int deletePhoto(String json );


}
