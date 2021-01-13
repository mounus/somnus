package com.example.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entiy.Contact;
import com.example.entiy.Custom;
import com.example.service.ContactServie;
import com.example.service.CustomService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/wx/custom",produces="application/json;charset=utf-8")
@Api(tags="订单小程序客户管理")
public class CustomController {
    @Autowired(required = false)
    private CustomService customService;


    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("添加客户")
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public int save(@RequestBody Custom custom) {
       return this.customService.save(custom);

    }
    /**
     * save:(客户信息修改). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("修改客户")
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public int update(@RequestBody Custom custom) {
        return this.customService.update(custom);

    }




    /**
     * channel:(客户渠道查询). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("客户渠道查询")
    @ResponseBody
    @RequestMapping(value = "/channel", method = RequestMethod.POST)
    public Map<String, Object> channel() {
      return  this.customService.channel();

    }



    /**
     * chooseMatorn:("选择月嫂"). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("推荐中三个月嫂选择一个月嫂")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂mid", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "oid", value = "订单oid", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/chooseMatorn", method = RequestMethod.POST)
    public int chooseMatorn(@RequestBody String json) {
        return  this.customService.chooseMatorn(json);

    }
    /**
     * chooseMatorn:("修改月嫂状态"). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("修改月嫂状态")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂mid", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "oid", value = "订单oid", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/updateStates", method = RequestMethod.POST)
    public int updateStates(@RequestBody String json) {
        return  this.customService.updateStates(json);

    }

    /**
     * getOneMatorn:("查看月嫂详情"). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("查看派岗月嫂详情")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂mid", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getOneMatorn", method = RequestMethod.POST)
    public List<Map<String,Object>> getOneMatorn(@RequestBody String json) {
        return  this.customService.getOneMatorn(json);

    }

    /**
     * getOneMatorn:("我的统计"). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("我的统计")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bid", value = "业务员bid", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getStatistics", method = RequestMethod.POST)
    public Map<String,Object> getStatistics(@RequestBody String json) {
        return  this.customService.getStatistics(json);

    }
    /**
     * postList:(查询所有派岗需). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("查询所有派岗需求")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类别【0-待推荐，1-待派岗，2-待更换】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "开始页数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cname", value = "客户姓名【为空-全部客户，有值-指定客户】", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/postList", method = RequestMethod.POST)
    public List<Map<String, Object>> postList(@RequestBody String json) {
        return  this.customService.postList(json);

    }


    /**
     * recommendMatorn:(推荐月嫂). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("派岗小程序推荐月嫂")
    @ResponseBody
    @ApiImplicitParams({

            @ApiImplicitParam(name = "start", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "service_day", value = "订单天数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "价格", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "pageStart", value = "开始页数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "月嫂姓名【为空-全部月嫂，有值-指定月嫂】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "did", value = "订单需求id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/recommendMatorn", method = RequestMethod.POST)
    public Map<String,Object> recommendMatorn(@RequestBody String json) {
        return  this.customService.recommendMatorn(json);

    }

       /**
         * choiceMatorn:(推荐月嫂). <br/>
         * @return
         * @author Somnus
         */
        @ApiOperation("选择推荐月嫂")
        @ResponseBody
        @ApiImplicitParams({

                @ApiImplicitParam(name = "oid", value = "订单oid", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "did", value = "订单需求did", required = false, dataType = "String", paramType = "query"),
                @ApiImplicitParam(name = "threematorn", value = "推荐月嫂", required = false, dataType = "String", paramType = "query"),
        })
        @RequestMapping(value = "/choiceMatorn", method = RequestMethod.POST)
        public int choiceMatorn(@RequestBody String json) {
            return  this.customService.choiceMatorn(json);

    }
    /**
     * choiceMatorn:(推荐月嫂). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("月嫂确认派岗")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oid", value = "订单oid", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "wages_remarks", value = "月嫂工资备注", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/confirmPost", method = RequestMethod.POST)
    public int confirmPost(@RequestBody String json) {
        return  this.customService.confirmPost(json);

    }
    /**
     * PostByName:(派岗小程序搜索月嫂). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("派岗小程序搜索月嫂")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "月嫂姓名", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/PostByName", method = RequestMethod.POST)
    public List<Map<String ,Object>> PostByName(@RequestBody String json) {
        return  this.customService.PostByName(json);

    }

    /**
     * getMatornNull:(重新推荐月嫂). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("重新推荐月嫂")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "did", value = "订单需求id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "oid", value = "订单oid", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getMatornNull", method = RequestMethod.POST)
    public int getMatornNull(@RequestBody String json) {
        return  this.customService.getMatornNull(json);

    }

    /**
     * choiceMatorn:(推荐月嫂). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("换人之后选择月嫂上岗")
    @ResponseBody
    @ApiImplicitParams({

            @ApiImplicitParam(name = "oid", value = "订单oid", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "post_states", value = "派岗状态", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "wages_remarks", value = "月嫂工资备注", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/newMatorn", method = RequestMethod.POST)
    public int newMatorn(@RequestBody String json) {

        return  this.customService.newMatorn(json);

    }

    /**
     * customList:(查询所有客户). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("后台查询所有客户")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【all-全部，production-已生产，unproduction-未生产，unknown-未知】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "页数", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getAllCustom", method = RequestMethod.POST)
    public Map<String, Object> getAllCustom(@RequestBody String json) {
        return  this.customService.getAllCustom(json);

    }
    /**
     * PostByName:(派岗小程序搜索月嫂). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("派岗/订单小程序月嫂派岗动态")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【order-订单，post-派岗】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bid", value = "母婴顾问id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/orderDynamic", method = RequestMethod.POST)
    public List<Map<String ,Object>> orderDynamic(@RequestBody String json) {
        return  this.customService.orderDynamic(json);

    }

    /**
     * PostByName:(派岗小程序搜索月嫂). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("订单小程序意向客户")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bid", value = "母婴顾问id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件【read-未读，全部已读/未读-all】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/intentionList", method = RequestMethod.POST)
    public List<Map<String ,Object>> intentionList(@RequestBody String json) {
        return  this.customService.intentionList(json);

    }


    /**
     * choiceMatorn:(推荐月嫂). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("修改订单动态是否已读信息")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bid", value = "母婴顾问id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/updateMessage", method = RequestMethod.POST)
    public int updateMessage(@RequestBody String json) {
        return  this.customService.updateMessage(json);
    }

}
