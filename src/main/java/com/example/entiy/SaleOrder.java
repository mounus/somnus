package com.example.entiy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yx_saleOrder")//销售工单
@Data
public class SaleOrder {
    @Id
    @GeneratedValue
    private  Integer id;
    @ApiModelProperty(value = "客户id")
    private  Integer cid;
    @ApiModelProperty(value = "业务员id")
    private  Integer bid;
    @ApiModelProperty(value = "销售工单类型【0-咨询,1-售前，2-售后】")
    private  Integer saleType;
    @ApiModelProperty(value = "确认意向【A,B,C,D】")
    private  String confirmIntention;
    @ApiModelProperty(value = "信息备注")
    private  String information ;
    @ApiModelProperty(value = "是否推荐【0-否，1-是】")
    private  Integer isRecommend ;
    @ApiModelProperty(value = "推荐要求，年龄")
    private  String age ;
    @ApiModelProperty(value = "推荐要求，生肖")
    private  String zodiac ;
    @ApiModelProperty(value = "推荐要求，学历")
    private  String educational ;
    @ApiModelProperty(value = "推荐要求，籍贯")
    private  String household ;
    @ApiModelProperty(value = "推荐备注")
    private  String recommendNotes ;
    @ApiModelProperty(value = "是否面试【0-否，1-是】")
    private  Integer isInterview ;
    @ApiModelProperty(value = "面试方式【0-视频面试，】")
    private  Integer interviewType ;
    @ApiModelProperty(value = "面试时间")
    private  String interviewTime ;
    @ApiModelProperty(value = "创建时间")
    private  String creat_time ;
        }
