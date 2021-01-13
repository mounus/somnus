package com.example.entiy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name="yx_custom")//客户表
@Data
public class Custom {
    @Id
    @GeneratedValue
    private  Integer id;
    @ApiModelProperty(value = "业务员id")
    private  Integer bid;//
    @ApiModelProperty(value = "客服id")
    private  Integer sid;//
    @ApiModelProperty(value = "来源")
    private  String  origin;//来源
    @ApiModelProperty(value = "渠道")
    private  String  channel;//渠道
    @ApiModelProperty(value = "客户姓名")
    private  String  c_name;//客户姓名
    @ApiModelProperty(value = "客户微信姓名号码")
    private  String  w_name;//客户姓名
    @ApiModelProperty(value = "电话")
    private  String  c_phone;//电话
    @ApiModelProperty(value = "生产情况:0未生产/1生产")
    private  Integer production_situation;//生产情况:未生产/生产
    @ApiModelProperty(value = "预产日期")
    private  String  nodate;//预产日期
    @ApiModelProperty(value = "生产日期")
    private  String  production_date;//生产日期
    @ApiModelProperty(value = "生产方式")
    private  String  production_mode;//生产方式
    @ApiModelProperty(value = "宝宝性别")
    private  String  baby_sex;//宝宝性别
    @ApiModelProperty(value = "创建时间")
    private  String  creat_time;//
    @ApiModelProperty(value = "客户出生年月")
    private  String  born;//客户出生年月
    @ApiModelProperty(value = "星座")
    private  String  constellation;//星座
    @ApiModelProperty(value = "预约月嫂")
    private  String  appointment;//预约月嫂
    @ApiModelProperty(value = "客户头像微信图片")
    private  String  c_photo;//客户头像微信图片
    @ApiModelProperty(value = "修改时间")
    private  String  update_time;//修改时间
    @ApiModelProperty(value = "积分")
    private  String  integral;//积分
    @ApiModelProperty(value = "是否是合伙人")
    private  String  isPartner;//是否是合伙人
    @ApiModelProperty(value = "地址")
    private  String  address;//地址
    @ApiModelProperty(value = "followCount")
    private  String  followCount;//更进次数
    @ApiModelProperty(value = "isIntention【0-不是，1-是】")
    private  Integer  isIntention;//是否意向



}
