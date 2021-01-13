package com.example.dao;

import com.example.entiy.Score;

import java.util.List;
import java.util.Map;
/**
 * @description: 月嫂评价管理接口
 * @param:
 * @return:
 */
public interface EvaluateDao {
    /**
     * @description: 向表中插入一条数据评价
     * @param: evaluate
     * @return:
     */
    int save(String  json);

    /**
     * @description: 通过mid来查询月嫂所有评价
     * @param: mid
     * @return: com.example.entity.evaluate
     */
    Map<String,Object> getById(Integer mid);

    /**
     * @description: 通过mid来删除评价
     * @param: mid
     * @return: com.example.entity.evaluate
     */
    int deleteEvaluate(String json
    );

    /**
     * @description: 查询模板评语
     * @param: mid
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
     * @return:
     */
   Score getScoreById(String  json);

    /**
     * @description: 通过mid修改月嫂十项全能
     * @param: mid
     * @return:
     */
    int update(Score score);
}
