package com.example.entiy;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "yx_order_demand")//
//订单需求表
@Data
public class Order_demand {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer cid;//客户id
    private Integer order_type;//订单类型 0预定 1即时
    private String service_type;//服务类型
    private Integer service_day;//服务天数
    private String starttime;//开始时间
    private String endtime;//结束时间
    private String timetype;//时间类型 短期 长期
    private String level;//级别
    private Integer twins; //双胞胎 0没有1有
    private Integer preemie;//早产儿 0没有1有
    private String remarks;//备注
    private Integer price;//价格
    private Integer work_day;//工作天数
    private String threematorn;//推荐给客户的月嫂
    private Integer work_states;//订单整体工作状态 /可以有多个订单
    private Integer threestates;//订单是否有  0正常1暂停2更换月嫂 操作
    private Integer oneorder;//多个订单天数之和
    private Integer post_states;
    private String creat_time;
    private Integer activityType;//活动类型
    private Integer onePrice;//订单单价  只有服务类型为 居家 ，时间类型为短期才有值
}
