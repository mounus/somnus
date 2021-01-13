package com.example.entiy.excel;

import lombok.Data;

/**
 *后台月嫂管理列表
 */
@Data
public class ExcelNewMatorn {
    private String photo;//月嫂图片
    private String number;//月嫂工号
    private String name;//姓名
    private String idcard;//身份证
    private String age;//年龄

    private String zodiac;//属相
    private String household;//户籍
    private String phone;//手机
    private String bank_card;//银行卡号
    private String bank_name;//开户行

    private String grade;//等级
    private String trueday;//服务天数
    private String service_count;//服务单数
    private String shelf;//服务状态
    private String agent;//经理人

    private String origin;//来源
    private String isPrice;//押金质保金状态
    private String l_name;//登记人
}
