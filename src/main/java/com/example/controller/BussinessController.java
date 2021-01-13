package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entiy.Bussiness;
import com.example.entiy.Contact;
import com.example.service.BussinessService;
import com.example.service.ContactServie;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

@RestController
@RequestMapping(value="/wx/bussiness",produces="application/json;charset=utf-8")
@Api(tags="月嫂业务管理")
public class BussinessController {

    @Autowired(required = false)
    private BussinessService bussinessService;

    /**
     * save:(月嫂业务信息新增). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("添加月嫂业务")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "订单oid", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "photo", value = "工装照", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "character", value = "性格", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "strength", value = "特长", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "num", value = "服务客户数量", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "works", value = "工作经历", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "trains", value = "培训经历", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "qualification", value = "资格证", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "identity", value = "身份证", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "heathly", value = "健康证", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public int save(@RequestBody String json) {

        int states=this.bussinessService.save(json);

        if(states>0){
            return 1;
        }else{
            return  0;
        }

    }

    /**
     * uodate:(修改月嫂业务表). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("修改月嫂业务表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "订单oid", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "photo", value = "工装照", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "character", value = "性格", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "strength", value = "特长", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "num", value = "服务客户数量", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "works", value = "工作经历", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "trains", value = "培训经历", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "qualification", value = "资格证", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "identity", value = "身份证", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "heathly", value = "健康证", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value ="/update", method = RequestMethod.PUT)
    public int update(@RequestBody String json){

        int states = this.bussinessService.update(json);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * updateAssess:(修改月嫂assess). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("申请")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "月嫂id", required = false, dataType = "String", paramType = "query"),

    })
    @RequestMapping(value ="/updateAssess", method = RequestMethod.PUT)
    public int updateAssess(Integer id){
        int states = this.bussinessService.updateAssess(id);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * updateShelf:(修改月嫂shelf). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("上架下架")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "月嫂id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "shelf", value = "状态【0-下架,1-上架】", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value ="/updateShelf", method = RequestMethod.PUT)
    public int updateShelf(Integer id,Integer shelf){
        int states = this.bussinessService.updateShelf(id,shelf);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }
    /**
     * updateIsblack:(修改月嫂isblack). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("拉黑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "月嫂id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "isblack", value = "状态【0-不拉黑，1-拉黑】", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value ="/updateIsblack", method = RequestMethod.PUT)
    public int updateIsblack(Integer id,Integer isblack){
        int states = this.bussinessService.updateIsblack(id,isblack);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }


}
