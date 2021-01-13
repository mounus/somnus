package com.example.util;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PageUtil {

    /**
     * @description: 分页参数
     * @param: start 当前页数
     * @param: count 总条数数
     * @return:总页数/分页语句
     */
    public static Map<String, Object> getAllPage(Integer count, Integer start) throws ParseException {
        Integer num = 10;//每页条数；
        Map<String, Object> map=new HashMap<>();
        map.put("num",num);
        String sql_page = "";
        if (start == 1) {
            Integer startnum = 0;
            sql_page = "  limit  " + startnum + "," + num;
            map.put("sql_page",sql_page);
        } else {
            sql_page = "  limit  " + (start - 1) * num + "," + num;
            map.put("sql_page",sql_page);
        }
        int page = 0;
        if (count % num == 0) {
            page = count / num;
            map.put("page",page);
        } else {
            page = count / num + 1;
            map.put("page",page);
        }
       return map;


    }
    /**
     * @description: 分页参数
     * @param:
     * @param:
     * @return:分页语句
     */
    public static Map<String, Object> getPage( Integer start) throws ParseException {
        Integer num = 10;//每页条数；
        Map<String, Object> map=new HashMap<>();
        map.put("num",num);
        String sql_page = "";
        if (start == 1) {
            Integer startnum = 0;
            sql_page = "  limit  " + startnum + "," + num;
            map.put("sql_page",sql_page);
        } else {
            sql_page = "  limit  " + (start - 1) * num + "," + num;
            map.put("sql_page",sql_page);
        }
        return map;


    }

    /**
     * @description: 分页参数
     * @param:
     * @param:
     * @return:分页语句
     */
    public static Map<String, Object> getHalfPage( Integer start) throws ParseException {
        Integer num = 5;//每页条数；
        Map<String, Object> map=new HashMap<>();
        map.put("num",num);
        String sql_page = "";
        if (start == 1) {
            Integer startnum = 0;
            sql_page = "  limit  " + startnum + "," + num;
            map.put("sql_page",sql_page);
        } else {
            sql_page = "  limit  " + (start - 1) * num + "," + num;
            map.put("sql_page",sql_page);
        }

        return map;


    }

}
