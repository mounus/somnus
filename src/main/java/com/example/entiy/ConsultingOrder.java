package com.example.entiy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yx_consultingOrder")//咨询工单
@Data
public class ConsultingOrder {
    @Id
    @GeneratedValue
    private  Integer id;
    @ApiModelProperty(value = "客户cid")
    private  Integer cid;
    @ApiModelProperty(value = "创建人id")
    private  Integer sid;
    @ApiModelProperty(value = "咨询工单类型-【0-咨询，1-售前，2-售后】")
    private  Integer  workType;
    @ApiModelProperty(value = "意向类型-【A，B，C,D】")
    private  String  intention;
    @ApiModelProperty(value = "更进纪要")
    private  String  points;
    @ApiModelProperty(value = "母婴顾问")
    private  Integer  bid;
    @ApiModelProperty(value = "创建时间")
    private  String  creat_time;
    @ApiModelProperty(value = "是否已读")
    private  Integer  isRead;

}
