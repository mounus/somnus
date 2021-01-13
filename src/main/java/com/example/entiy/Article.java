package com.example.entiy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yx_article")
@Data
public class Article {

    @Id
    @GeneratedValue
    private  Integer id;
    @ApiModelProperty(value = "标题")
    private  String title;
    @ApiModelProperty(value = "页面 【1-活动，2知识】")
    private  String page;
    @ApiModelProperty(value = "type【1-孕期知识，2-分娩知识，3-产后知识，4-育儿知识，5-社群活动，6-专家讲座】")
    private  Integer type;
    @ApiModelProperty(value = "标签1-【1-1-8周，2-8-16周，3-16-24周，4-24-32周，5-32-42周】2-【1-1-2个月，2-2-4个月，3-4-6个月】，3-【1-准备期间，2-准备期间，3-准备期间】")
    private  Integer  label;
    @ApiModelProperty(value = "封面")
    private  String  cover;
    @ApiModelProperty(value = "链接")
    private  String  url;
    @ApiModelProperty(value = "上传时间")
    private  String  upload_time;
}
