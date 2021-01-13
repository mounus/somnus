package com.example.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entiy.Matorn;
import com.example.entiy.Origin;

import com.example.service.OriginService;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/wx/origin",produces="application/json;charset=utf-8")
@Api(tags="月嫂来源管理")
public class OriginController {

    @Autowired(required = false)
    private OriginService originService;

    /**
     * addGoodsbaseInfo:(月嫂来源信息新增). <br/>
     * @author Somnus
     * @return
     */
    @ApiOperation("添加月嫂来源")
    @ResponseBody
    @RequestMapping(value="/save",method = RequestMethod.POST)


    public int save(@RequestBody Origin origin) {
        int states = this.originService.save(origin);
        if (states > 0) {
            return origin.getMid();
        } else {
            return 0;
        }
    }

    /**
     * uodate:(修改月嫂表). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("修改")
    @RequestMapping(value ="/update", method = RequestMethod.PUT)
    public int updateMatorn(@RequestBody  Origin origin){
        int states = this.originService.update(origin);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }
    /**
     * uodate:(修改月嫂表). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("修改")
    @RequestMapping(value ="/delete", method = RequestMethod.POST)
    public int delete(@RequestBody   String json){
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        int states = this.originService.delete(jsonObject.getInteger("mid"));
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * uodate:(删除客户及其需求表). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("删除客户及其需求表")
    @RequestMapping(value ="/deleteOrder", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户cid", required = false, paramType = "query"),
    })
    public int deleteOrder(@RequestBody   String json){
      return   originService.deleteOrder(json);

    }

    /**
     * uodate:(删除客户及其需求表). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("修改时间")
    @RequestMapping(value ="/updateTime", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户cid", required = false, paramType = "query"),
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "starttime", value = "开始时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "endtime", value = "结束时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "service_day", value = "订单天数", required = false, paramType = "query"),
    })
    public int updateTime(@RequestBody   String json){
        return   originService.updateTime(json);

    }
    /**
     * uodate:(查询所有机构名称). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("查询所有机构名称")
    @RequestMapping(value ="/allmechanism", method = RequestMethod.POST)

    public  List<Map<String,Object>> allmechanism(){
        return   originService.allmechanism();

    }


}