package com.example.dao;

import com.example.entiy.Job_evaluate;
import com.example.entiy.Matorn;
import com.example.entiy.Score;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


public interface JobDao {

    /**
     * @description: 通过id来查询月嫂基础信息
     * @param: mid
     * @return: com.study.spring.entity.matorn
     */
  Map<String, Object> getById(Integer mid);


    /**
     * @description: 向表中插入一条数据
     * @param:
     * @return: void
     */
    int save(String  json);
    /**
     * @description: 修改
     * @param:
     * @return: void
     */
    int update(String  json);


    /**
     * @description: 通过id查询月嫂评测记录
     * @param: Job_evaluate
     * @return: void
     */
    Map<String, Object> findOne(Integer mid);


  /**
   * @description: 测试
   * @param: uid
   * @return: com.example.entity.Period
   */
  Map<String,Object> test(String json);
}
