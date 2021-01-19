package com.example.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * ClassName: MatornDto <br/>
 * Function: 封装月嫂信息数据传输对象. <br/>
 * date: 2019年1月15日 下午5:31:16 <br/>
 *
 */
@Data
@ApiModel(description= "月嫂信息数据")
public class MatornDto {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "月嫂id")
    private  Integer mid;
    @ApiModelProperty(value = "来源1")
    private  String source;
    @ApiModelProperty(value = "1-机构名")
    private  String institution_name;
    @ApiModelProperty(value = "1-证明人")
    private  String witness;
    @ApiModelProperty(value = "1-证明人电话")
    private  String witness_phone;
    @ApiModelProperty(value = "1-其他")
    private  String other;
    @ApiModelProperty(value = "1-介绍人")
    private  String introducer;
    @ApiModelProperty(value = "1-介绍人电话")
    private  String introducer_phone;

    @ApiModelProperty(value = "身份信息2 姓名")
    private String name;
    @ApiModelProperty(value = "经理人id")
    private Integer uid;
    @ApiModelProperty(value = "2身份证号码")
    private String idcard;
    @ApiModelProperty(value = "2身份证证件类型")
    private String idcard_type;
    @ApiModelProperty(value = "2出生年月")
    private String born;
    @ApiModelProperty(value = "2民族")
    private String nation;
    @ApiModelProperty(value = "2户籍地址")
    private String household;
    @ApiModelProperty(value = "2学历")
    private String educational;
    @ApiModelProperty(value = "2婚姻")
    private String marriage;
    @ApiModelProperty(value = "2身高体重")
    private String weightHeight;
    @ApiModelProperty(value = "2现居地址")
    private String address;

    @ApiModelProperty(value = "联系方式3 电话")
    private String phone;
    @ApiModelProperty(value = "3紧急联系人")
    private String emergency_person;
    @ApiModelProperty(value = "3紧急联系人电话")
    private String emergency_phone;
    @ApiModelProperty(value = "3银行卡号")
    private String bank_card;
    @ApiModelProperty(value = "3银行卡开户行")
    private String bank_name;

    @ApiModelProperty(value = "业务信息4 工装照")
    private String photo;
    @ApiModelProperty(value = "4性格")
    private String character;
    @ApiModelProperty(value = "4从业年限")
    private String work_age;
    @ApiModelProperty(value = "4工作经历")
    private String works;
    @ApiModelProperty(value = "4培训经历")
    private String trains;

    @ApiModelProperty(value = "证件信息5 身份证")
    private String identity;
    @ApiModelProperty(value = "证件信息5 护理师证")
    private String qualification;
    @ApiModelProperty(value = "证件信息5 健康证")
    private String heathly;



}
