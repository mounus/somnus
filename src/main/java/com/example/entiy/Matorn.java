package com.example.entiy;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
//月嫂表
@Table(name="yx_matorn")
@Data
public class Matorn implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue
    private int id;
    private int uid;//经理人id
    private int lid;//添加月嫂人id
    private String name;//姓名
    private int sex;//性别
    private String born;//出生年月
    private String nation;//民族
    private String idcard;//身份证号码
    private String idcard_type;//证件类型
    private String household;//户籍
    private String marriage;//婚姻
    private String educational;//学历
    private String weight;//身高
    private String height;//体重
    private String address;//现居地址
    private String zodiac;//生肖
    private String constellation;//星座
    private int idtype;//身份证状态
    private String aa;//分类


}
