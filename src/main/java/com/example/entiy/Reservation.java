package com.example.entiy;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Data
@ApiModel(description= "预约信息数据")
public class Reservation {
    @Id
    @GeneratedValue
    @ApiModelProperty(value = "预约客户ID")
    private int id;
    @ApiModelProperty(value = "预约客户姓名")
    private String name;
    @ApiModelProperty(value = "预约客户电话")
    private String phone;
    @ApiModelProperty(value = "预约客户生产情况:未生产/生产")
    private  Integer production_situation;
    @ApiModelProperty(value = "预约客户生产日期")
    private  String  production_date;
    @ApiModelProperty(value = "填写时间")
    private  String time;
    @ApiModelProperty(value = "来源")
    private  String origin;
    @ApiModelProperty(value = "医院")
    private  String hospital;


}
