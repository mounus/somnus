package com.example.service;

import com.example.entiy.Period;
import springfox.documentation.spring.web.json.Json;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface PeriodService {



    /**
     * @description: 小程序通过条件查询。
     * @return:
     */
    List<Map<String,Object>> listBy(String somnus,Integer uid,Integer start);
    /**
     * @description: 后台月嫂列表查询。
     * @return:
     */
   Map<String,Object> getAdmin(String json);


    /**
     * @description: 后台通过工号/姓名/id查询。
     * @return:
     */
    Map<String,Object> getByNumber(String json);
    /**
     * @description: 后台通过时间。
     * @return:
     */
    List<Map<String,Object>> getByTime(String json);

    /**
     * @description: 小程序通过name查询。
     * @return:
     */
    List<Map<String,Object>> getByName(String json);
    /**
     * @description: 向表中插入一条数据
     * @param: period
     * @return:
     */
    int save(String json);

    /**
     * @description: 通过id来查询信息
     * @param: id
     * @return: com.example.entity.Period
     */
   List<Map<String, Object>>getById(Integer mid) ;

    /**
     * @description: 通过states来查询信息
     * @param: states
     * @return: com.example.entity.Period
     */
    Period getByStates(int states) ;

    /**
     * @description: 更新表中单条数据
     * @param: Period
     * @return:
     */
    int update(String  json);

    /**
     * @description: 修改grade/agent
     * @param: bussiness
     * @return:
     */
    int updateGrade_agent(String json) ;
    /**
     * @description: 通过uid查询月嫂动态
     * @param: uid
     * @return: com.example.entity.Period
     */
    List<Map<String,Object>> dynamic(String  json) ;
    /**
     * @description: 后台通过grade/agent查询。
     * @return:
     */
   Map<String,Object> getByGrade(String json);


    /**
     * @description:   月嫂是否收藏
     * @param:
     * @return:
     */
    int updateCollect(String json) ;
    /**
     * @description: 查询我的收藏
     * @param: uid
     * @return: com.example.entity.Period
     */
    List<Map<String,Object>> myCollect(String json) ;
    /**
     * @description: 通过月嫂姓名查询我的收藏
     * @param: uid
     * @return: com.example.entity.Period
     */
    List<Map<String,Object>> myCollectByName(String json) ;

}
