package com.example.entiy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yx_top_picture")
@Data
public class Picture {

    @Id
    @GeneratedValue
    private  Integer id;
    @ApiModelProperty(value = "名称")
    private  String name;
    @ApiModelProperty(value = "页面 【1-活动，2知识】")
    private  String page;
    @ApiModelProperty(value = "上传图片链接")
    private  String picture_url;
    @ApiModelProperty(value = "文章链接")
    private  String  url;
    @ApiModelProperty(value = "上传时间")
    private  String  upload_time;

}
