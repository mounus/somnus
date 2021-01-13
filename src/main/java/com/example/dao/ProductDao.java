package com.example.dao;

import java.util.Map;

public interface ProductDao {
    /**
     * @description: 后台产品列表查询。
     * @return:
     */
    Map<String,Object> getAdmin(String json);

    /**
     * @description: 后台通过工号/姓名/id查询。
     * @return:
     */
    Map<String,Object> getByNumber(String json);


    /**
     * @description: 后台通过grade/agent查询。
     * @return:
     */
    Map<String,Object> getByGrade(String json);
}
