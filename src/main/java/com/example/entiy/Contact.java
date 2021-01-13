package com.example.entiy;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name="yx_contact")
public class Contact {

    @Id
    @GeneratedValue
    private int id;
    private int mid;//月嫂id
    private String phone;//电话
    private String wechat;//微信
    private String bank_card;//银行卡号
    private String bank_name;//银行卡开户行
    private String emergency_person;//紧急联系人
    private String emergency_phone;//紧急联系人电话





}
