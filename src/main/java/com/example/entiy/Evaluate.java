package com.example.entiy;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
@Data
public class Evaluate {
    @Id
    @GeneratedValue
    private Integer id;
    private Integer mid;
    private String  customer_name;//客户姓名
    private String  customer_photo;//客户图片
    private String  address;//客户地址
    private String  praise;//好评指数
    private String  label;//标签
    private String  evaluate_word;//好评文字
    private String  evaluate_photo;//好评图片




}
