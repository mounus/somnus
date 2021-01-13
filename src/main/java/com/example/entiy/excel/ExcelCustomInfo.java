package com.example.entiy.excel;

import lombok.Data;

@Data
public class ExcelCustomInfo {
    private String cid;//客户编号
    private String c_name;//客户姓名
    private String production_situation;//生产情况
    private String production_time;//生产时间
    private String address;

    private String c_phone;//客户电话
    private String WeChat;//微信
    private String c_origin;//客户来源
    private String sname;//咨询顾问
    private String intention;//客户意向

    private String followCount;//跟进次数
    private String order_speed;//当前进度
    private String bname;//母婴顾问
    private String creat_time;//创建时间
}
