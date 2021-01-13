package com.example.entiy;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
@Data
@Entity
@Table(name="yx_job_evaluate")
//职业评测
public class Job_evaluate {
    @Id
    @GeneratedValue
    private  int id;
    private  int mid;
    private  int sex;
    private  String name;//姓名
    private  String born;//出身年月
    private  String idcard;//身份证
    private  String photo;//照片
    private  String number;//证件号
    private  String alllist;//评审记录


}
