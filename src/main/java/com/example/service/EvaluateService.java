package com.example.service;

import com.example.entiy.Score;

import java.util.List;
import java.util.Map;

public interface EvaluateService {
    /**
     * @description: 向表中插入一条数据
     * @param: evaluate
     * @return:
     */
    int save(String  json);

    /**
     * @description: 通过mid来查询信息
     * @param: mid
     * @return: com.example.entity.evaluate
     */
    Map<String, Object> getById(Integer mid);

    /**
     * @description: 通过mid来删除评价
     * @param: mid
     * @return: com.example.entity.evaluate
     */
    int deleteEvaluate(String json);

    /**
     * @description: 查询模板评语
     * @param:
     * @return:
     */
    List<String> getComment( );
    /**
     * @description: 添加十项全能
     * @param:
     * @return: void
     */
    int saveScore(Score score);
    /**
     * @description: 通过mid来查询月嫂十项全能
     * @param: mid
     * @return: com.example.entity.score
     */
    Score getScoreById(String  json);
    /**
     * @description: 通过mid修改月嫂十项全能
     * @param: mid
     * @return:
     */
    int update(Score score);




}
