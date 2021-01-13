package com.example.entiy.excel;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *后台客户管理客户居家订单列表
 */
@Data
public class ExcelCustomHome {

    private String cid;//订单编号
    private String c_name;//客户姓名
    private String bname;//母婴顾问
    private String c_origin;//客户来源
    private String order_type;//订单类型
    private String order_speed;//订单进度
    private String start_time;//开始时间
    private String service_day;//订单天数
    private String end_time;//结束时间
    private String price;//订单金额
    private String service_count;//服务人次
    private String orderDay;//服务天数
    private String truePrice;//实收金额
    private String creat_time;//订单创建时间


}
