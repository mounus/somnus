package com.example.entiy.excel;

import lombok.Data;

@Data
public class ExcelReservation {
    private String id;
    private String name;//姓名
    private String phone;//电话
    private  String production_situation;//生产情况:未生产/生产
    private  String  production_date;//生产日期
    private  String origin;//来源
    private  String hospital;//来源
    private  String time;//时间
}
