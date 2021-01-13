package com.example.entiy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="yx_score")
@Data
public class Score {
    @Id
    @GeneratedValue
    private  int id;
    private  int mid;//月嫂id
    @ApiModelProperty(value = "背景资历")
    private  int background;//背景资历
    @ApiModelProperty(value = "仪容仪表")
    private  int appearance;//仪容仪表
    @ApiModelProperty(value = "沟通能力")
    private  int communicate;//沟通能力
    @ApiModelProperty(value = "护理技能")
    private  int nurse;//护理技能
    @ApiModelProperty(value = "母乳喂养")
    private  int breast_fed;//母乳喂养
    @ApiModelProperty(value = "月子营养")
    private  int confinement;//月子营养
    @ApiModelProperty(value = "产后恢复")
    private  int postpartum;//产后恢复
    @ApiModelProperty(value = "早期启智")
    private  int intelligence;//早期启智
    @ApiModelProperty(value = "心理素质")
    private  int psychology;//心理素质
    @ApiModelProperty(value = "知识传授")
    private  int know;//知识传授
    @ApiModelProperty(value = "总分")
    private  int total;//总分
    @ApiModelProperty(value = "评语")
    private  String comment;//评语
}
