package com.example.util;

import java.util.HashMap;
import java.util.Map;

public class ArticleUtill {
    /**
     * 文章标题类型，标签类型判断
     */
    public static Map<String, Object> articleTypeLabel(Integer page,Integer type, Integer label) {
        Map<String, Object> map = new HashMap<>();

        if (page==1){
            map.put("page","知识");
        }
        if(page==2){
            map.put("page","活动");
        }

        if (type == 1) {
            map.put("type", "孕期知识");
            if (label == 1) {
                map.put("label", "1-8周");
            }
            if (label == 2) {
                map.put("label", "8-16周");
            }
            if (label == 3) {
                map.put("label", "16-24周");
            }
            if (label == 4) {
                map.put("label", "24-32周");
            }
            if (label == 5) {
                map.put("label", "32-42周");
            }

        }
        if (type == 2) {
            map.put("type", "分娩知识");

            if (label == 1) {
                map.put("label", "准备期间");
            }
            if (label == 2) {
                map.put("label", "准备期间");
            }
            if (label == 3) {
                map.put("label", "准备期间");
            }
        }
        if (type == 3) {
            map.put("type", "产后知识");
            if (label == 1) {
                map.put("label", "1-2个月");
            }
            if (label == 2) {
                map.put("label", "2-4个月");
            }
            if (label == 3) {
                map.put("label", "4-6个月");
            }
        }
        if (type == 4) {
            map.put("type", "育儿知识");
            if (label == 1) {
                map.put("label", "1-2个月");
            }
            if (label == 2) {
                map.put("label", "2-4个月");
            }
            if (label == 3) {
                map.put("label", "4-6个月");
            }
        }
        if (type == 5) {
            map.put("type", "社群活动");
            map.put("label","");
        }
        if (type == 6) {
            map.put("type", "专家讲座");
            map.put("label","");
        }


        return map;
    }
}
