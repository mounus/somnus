package com.example.service;

import com.example.dao.JobDao;
import com.example.entiy.Job_evaluate;
import com.example.entiy.Matorn;
import com.example.entiy.Score;

import java.util.List;
import java.util.Map;

public interface JobService {

    /**
     * @description: 通过name来查询信息
     * @param: mid
     * @return: com.study.spring.entity.matorn
     */
    Map<String, Object> getById(Integer mid);

    /**
     * @description: 向表中插入一条数据
     * @param: Job_evaluate
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
    Map<String, Object> findOne( Integer mid);

    /**
     * @description: 测试
     * @param: uid
     * @return: com.example.entity.Period
     */
    Map<String,Object> test(String json);

}
