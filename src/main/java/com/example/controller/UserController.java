package com.example.controller;


import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;


import com.example.message.KXTSmsSDK;
import com.example.service.UserService;
import io.swagger.annotations.Api;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value="/wx/user",produces="application/json;charset=utf-8")
@Api(tags="用户管理")
public class UserController {

    @Autowired(required = false)
    private UserService userService;



    @ResponseBody
    @ApiOperation("经理人小程序登录验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "query"),

    })
    @RequestMapping(value = "/getLogin", method = RequestMethod.POST)
    public Map<String,Object> getLogin(@RequestBody String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String  phone=jsonObject.getString("phone");
        String  password=jsonObject.getString("password");

        Map<String,Object>  map=userService.getLogin(phone,password);
        return map;

    }
    @ResponseBody
    @ApiOperation("后台登录验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getAdmin", method = RequestMethod.POST)
    public Map<String,Object> getAdmin(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String  phone=jsonObject.getString("phone");
        String  password=jsonObject.getString("password");
        Map<String,Object>  map=userService.getAdmin(phone,password);
        return map;

    }


    @ResponseBody
    @ApiOperation("获取验证码")
    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    public int getCode( @RequestBody String json){
        JSONObject jsonObject = JSON.parseObject(json);
        String phone=jsonObject.getString("phone").toString();
        String code=jsonObject.getString("code");
        short responseType=jsonObject.getShort("responseType");
        String extno=jsonObject.getString("extno");
        String body="【好悦享月嫂】您的验证码为"+code+"，如非本人操作，请忽略。";
        // String body="【好悦享月嫂】您的验证码为@，如非本人操作，请忽略。";
        KXTSmsSDK kxtsms = new KXTSmsSDK();
        String getResponse =kxtsms.send(phone,body,responseType,extno);
        int a=userService.saveOrUpdate(phone,code);

        if (getResponse!=""){
            return 1;
        } else {
            return 0;
        }
    }
    @ResponseBody
    @ApiOperation("经理人小程序状态验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "base", value = "状态码", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getStates", method = RequestMethod.POST)
    public Map<String,Object> getStates(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String  phone=jsonObject.getString("phone");
        String  base=jsonObject.getString("base");

        Map<String,Object>  map=userService.getStates(phone,base);

        return map;

    }
    @ResponseBody
    @ApiOperation("后台状态验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "base", value = "状态码", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getAdminStates", method = RequestMethod.POST)
    public Map<String,Object> getAdminStates(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String  phone=jsonObject.getString("phone");
        String  base=jsonObject.getString("base");

        Map<String,Object>  map=userService.getAdminStates(phone,base);

        return map;

    }
    @ResponseBody
    @ApiOperation("获取退出时间")
    @RequestMapping(value = "/getExitTime", method = RequestMethod.POST)
    public int getExitTime(@RequestBody String phone) {

        int states=userService.getExitTime(phone);
        if(states>0){
            return 1;
        }else {
            return 0;
        }

    }

    @ResponseBody
    @ApiOperation("获取新消息")
    @RequestMapping(value = "/getMessage", method = RequestMethod.POST)
    public Map<String,Object> getMessage(@RequestBody String uid) {


        return userService.getMessage(uid);
    }


    @ResponseBody
    @ApiOperation("业务员小程序登录验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getBussiness", method = RequestMethod.POST)
    public Map<String,Object> getBussiness(@RequestBody String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String  phone=jsonObject.getString("phone");
        String  password=jsonObject.getString("password");

        Map<String,Object>  map=userService.getBussiness(phone,password);
        return map;

    }
    @ResponseBody
    @ApiOperation("业务员小程序登录状态验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "base", value = "状态码", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getBussinessStates", method = RequestMethod.POST)
    public Map<String,Object> getBussinessStates(@RequestBody String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String  phone=jsonObject.getString("phone");
        String  base=jsonObject.getString("base");

        Map<String,Object>  map=userService.getBussinessStates(phone,base);
        return map;

    }

    @ResponseBody
    @ApiOperation("派岗小程序登录验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "password", value = "密码", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getPost", method = RequestMethod.POST)
    public Map<String,Object> getPost(@RequestBody String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String  phone=jsonObject.getString("phone");
        String  password=jsonObject.getString("password");

        Map<String,Object>  map=userService.getPost(phone,password);
        return map;

    }
    @ResponseBody
    @ApiOperation("派岗小程序登录状态验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "电话", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "base", value = "状态码", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getPostStates", method = RequestMethod.POST)
    public Map<String,Object> getPostStates(@RequestBody String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String  phone=jsonObject.getString("phone");
        String  base=jsonObject.getString("base");
        Map<String,Object>  map=userService.getPostStates(phone,base);
        return map;

    }
}
