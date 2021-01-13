package com.example.entiy;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


//业务表
@Data
@Table(name="yx_bussiness")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Bussiness {
    @Id
    @GeneratedValue
    private  int id;
    private  int mid;
    private String photo;//工装照
    private String character;//性格
    private String strength;//特长
    private String work_age;//从业年限
    private int num;//服务客户数量
    private String works;//从业经历
    private String trains;//培训经历
    private String qualification;//从业资格证
    private String grade;//月嫂级别
    private String creat_time;//创建时间
    private String number;//工号
    private int day;//晋级总天数
    private int trueday;//
    private int  isorder;//是否创建订单
    private int  isquit;//是否离职
    private int  assess;//是否申请考核
    private int  isblack;//是否拉黑
    private int  shelf;//是否上架
    private String identity;//身份证
    private String heathly;//健康证
    private String video;//视频
    private  int isscore;//评分
    private  int isevaluate;//评分
    private  int  iscollect;//收藏







}
