package com.example.entiy;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="yx_period")
@Data
public class Period {

    @Id
    @GeneratedValue
    private  int id;
    private int mid;//月嫂id
    private  int states;//月嫂服务状态
    private String period;//月嫂档期

}
