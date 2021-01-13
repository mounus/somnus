package com.example.entiy;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name="yx_order")//
public class Order {
    @Id
    @GeneratedValue
    private  Integer  id;
    private  Integer  did;//客户需求表id
    private  Integer  mid;//月嫂id
    private  Integer  cid;//客户id
    private  String   o_name;//订单联系人
    private  String   o_phone;//订单手机
    private  String   region;//所在地区
    private  String   o_address;//详细地址
    private  Integer  type;//订单状态
    private  Integer  states;//月嫂状态
    private  String   o_number;//订单编号
    private  String   creattime;//订单创建时间
    private  String   arrival_time;//到岗时间
    private  String   return_time;//回岗时间
    private  String   confirm_time;//确认回岗时间
    private  String   service_states;//服务状态：暂停/开始
    private  String   stop_time;//服务暂停时间
    private  String   run_time;//服务开始时间
    private  Integer  order_day;//订单天数
    private  Integer  order_states;//单个订单状态


}
