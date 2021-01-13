package com.example.dao;

import com.example.entiy.Contact;

import java.util.List;
import java.util.Map;
/**
 * @description: 月嫂联系接口
 * @param:
 * @return:
 */
public interface ContactDao {
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
     * @description: 导出全部数据
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
