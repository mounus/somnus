package com.example.entiy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yx_questiom")//问题表
@Data
public class Question {
    @Id
    @GeneratedValue
    private int id;

    @ApiModelProperty(value = "题型【1-产妇护理,2-宝宝护理，3-生活护理，4-月子餐，5-心理指导】")
    private  int questionType  ;
    @ApiModelProperty(value = "类型【1-单选题，2-多选题，3-判断题】")
    private  int type ;
    @ApiModelProperty(value = "题目")
    private  String  question ;
    @ApiModelProperty(value = "选项A")
    private  String A ;
    @ApiModelProperty(value = "选项B")
    private  String B;
    @ApiModelProperty(value = "选项C")
    private  String C;
    @ApiModelProperty(value = "选项D")
    private  String D;
    @ApiModelProperty(value = "答案")
    private  String answer ;


}
