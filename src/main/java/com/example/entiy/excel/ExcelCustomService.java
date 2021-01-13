package com.example.entiy.excel;


import lombok.Data;

/**
 * 客户服务订单统计表（财务）
 */
@Data
public class ExcelCustomService {

    private String cid;//客户编号
    private String c_origin;//客户来源
    private String c_name;//客户姓名
    private String c_phone;//客户电话
    private String o_number;//订单编号
    private String order_type;//订单类型
    private String service_day;//订单天数
    private String order_states;//订单状态
    private String price;//订单金额
    private String onePrice;//订单单价
    private String bname;//母婴顾问
    private String mid;//月嫂编号
    private String m_name;//月嫂姓名
    private String source;//月嫂来源
    private String m_agent;//月嫂经理人
    private String unitPrice;//月嫂服务单价
    private String arrival_time;//上岗日期
    private String confirm_time;//回岗日期
    private String nowMonth_day;//当月服务天数
    private String nextMonth_day;//次月服务天数
    private String nowMonth_price;//当月回款
    private String nextMonth_price;//次月回款
    private String nowMonth_wages;//当月工资
    private String nextMonth_wages;//次月工资

}
