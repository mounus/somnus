package com.example.service;

import com.example.entiy.Contact;
import com.example.entiy.Matorn;

import java.util.List;
import java.util.Map;

public interface ContactServie {
    /**
     * @description: 向表中插入一条数据
     * @param: contact
     * @return:
     */
    int save(Contact contact);



    /**
     * @description: 向表中插入一条数据
     * @param: contact
     * @return:
     */
    int update(Contact contact);

    /**
     *
     * @description: 导出数据
     * @param: contact
     * @return:
     */
    List<Integer> outExcel(String somnus,String agent,String grade);
    /**
     *
     * @description: 查询单个导出数据
     * @param: contact
     * @return:
     */
    List<Map<String,Object>> outExcelById(Integer id);
}
