package com.example.entiy;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yx_origin")
@Data
public class Origin {
    @Id
    @GeneratedValue
    private int id;
    private  int mid;//月嫂id
    private  String source;//来源
    private  String institution_name;//机构名
    private  String witness;//证明人
    private  String witness_phone;//证明人电话
    private  String other;//其他
    private  String introducer;//介绍人
    private  String introducer_phone;//介绍人电话


}
