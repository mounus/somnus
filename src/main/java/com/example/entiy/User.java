package com.example.entiy;



import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.ResultSet;
import java.sql.SQLException;
@Data
public class User {
    @Id
    private  Integer id;
    @ApiModelProperty(value = "手机号、登录账号")
    private  String phone;
    @ApiModelProperty(value = "验证码")
    private  String code;
    @ApiModelProperty(value = "账号密码")
    private  String password;
    @ApiModelProperty(value = "姓名")
    private  String name;
    @ApiModelProperty(value = "密码base")
    private  String base;
    @ApiModelProperty(value = "岗位")
    private  String post;
    @ApiModelProperty(value = "权限")
    private  Integer power;
    @ApiModelProperty(value = "上次登录时间")
    private  String logintime;
    @ApiModelProperty(value = "上次退出时间")
    private  String exittime;
    @ApiModelProperty(value = "图片")
    private  String photo;
    @ApiModelProperty(value = "机构名称")
    private  String mechanism_name;
    @ApiModelProperty(value = "创建时间")
    private  String creat_time;
    @ApiModelProperty(value = "责任范围")
    private String description;
    @ApiModelProperty(value = "单位")
    private Integer company;
    @ApiModelProperty(value = "部门")
    private Integer  department;
    @ApiModelProperty(value = "性别")
    private String sex;
    @ApiModelProperty(value = "门店")
    private String store;
}
