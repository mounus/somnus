package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dto.MatornDto;
import com.example.entiy.Bussiness;
import com.example.entiy.Contact;
import com.example.entiy.Score;
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

import java.util.List;
import java.util.Map;

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
    /**
     * allMatorn:(小程序查询所有月嫂列表). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("小程序查询所有月嫂列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "登录人id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "number", value = "id，姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "grade", value = "等级【N,H,Y1,Y2,Y3,X,P】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "agent", value = "护理师zhugid", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "isService", value = "服务档期【0-为空，1-无档期，2-可服务，3-服务中】", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value ="/allMatorn", method = RequestMethod.POST)
    public List<Map<String,Object>> allMatorn(@RequestBody String json){
     return   bussinessService.allMatorn(json);
    }

    /**
     * allMatorn:(小程序查询所有月嫂列表). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("月嫂小程序搜索筛选条件")
    @RequestMapping(value ="/bussinessCondition", method = RequestMethod.POST)
    public Map<String,Object> bussinessCondition(){
        return  bussinessService.bussinessCondition();
    }
    /**
     * allMatorn:(小程序查询所有月嫂列表). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("一个月嫂的详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false, dataType = "Integer", paramType = "query"),
    })
    @RequestMapping(value ="/oneMatornDetail", method = RequestMethod.POST)
    public Map<String,Object> oneMatornDetail(@RequestBody String json){
        return  bussinessService.oneMatornDetail(json);
    }
    /**
     * allMatorn:(一个月嫂的所有订单). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("一个月嫂的所有订单")
    @RequestMapping(value ="/matornAllOrder", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false, dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, dataType = "Integer", paramType = "query"),
    })
    public List<Map<String,Object>> matornAllOrder(@RequestBody String json){
        return  bussinessService.matornAllOrder(json);
    }

    /**
     * * @author Somnus
     *     @return
     */
    @ApiOperation("添加考核评估")
    @RequestMapping(value ="/saveScore", method = RequestMethod.POST)
    public int  saveScore(@RequestBody Score score){
        return  bussinessService.saveScore(score);
    }
    /**
     * * @author Somnus
     *     @return
     */
    @ApiOperation("查询模板评语")
    @RequestMapping(value ="/getComment", method = RequestMethod.POST)
    public List<String>  getComment(){
        return  bussinessService.getComment();
    }
    /**
     * * @author Somnus
     *     @return
     */
    @ApiOperation("月嫂全部信息")
    @RequestMapping(value ="/getById", method = RequestMethod.POST)
    public List<Map<String,Object>>  getById(@RequestBody String json){
        return  bussinessService.getById(json);
    }
    /**
     * * @author Somnus
     *     @return
     */
    @ApiOperation("修改月嫂全部信息")
    @RequestMapping(value ="/updateMatornDto", method = RequestMethod.POST)
    public int  updateMatornDto(@RequestBody MatornDto matornDto){
        return  bussinessService.updateMatornDto(matornDto);
    }
    /**
     * * @author Somnus
     *     @return
     */
    @ApiOperation("修改考核评估")
    @RequestMapping(value ="/updateScore", method = RequestMethod.POST)
    public int  updateScore(@RequestBody Score score){
        return  bussinessService.updateScore(score);
    }
}
