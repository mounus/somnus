package com.example.controller;

import com.example.entiy.ConsultingOrder;
import com.example.entiy.Custom;
import com.example.entiy.Order_demand;
import com.example.entiy.SaleOrder;
import com.example.service.CustomAdminService;
import com.example.service.CustomAppletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/wx/customAdmin",produces="application/json;charset=utf-8")
@Api(tags="后台客户管理")
public class CustomAdminController {
    @Autowired(required = false)
    private CustomAdminService customAdminService;


    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("查询后台所有客户")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "origin", value = "客户来源", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "origin_channel", value = "客户渠道", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "intention", value = "意向类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sname", value = "咨询顾问", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bname", value = "母婴顾问", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderSpeed", value = "订单进度", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "service_type", value = "服务类型1-【月子会所服务，居家服务】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "timetype", value = "服务类型2- 月子会所服务【月子会所服务，全程，医院服务】 居家服务【U1,U2,U3,U4,9800】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "level", value = "服务类型3-【U1,U2,U3,U4,9800】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "returnType", value = "回岗类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "number", value = "客户手机号，姓名，编号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "powerFiled", value = "权限字段", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start_time", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "end_time", value = "结束时间", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getAllCustom", method = RequestMethod.POST)
    public Map<String,Object> getAllCustom(@RequestBody String json) {
        return  this.customAdminService.getAllCustom(json);
    }
    /**
     * getAllCondition:(后台查询条件集合). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("设置后台统计客户数量")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型【1-年客户数，2-今日新增，3-本周新增，4-本月新增】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "count", value = "设置数量", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/setCustomCount", method = RequestMethod.POST)
    public int setCustomCount(@RequestBody String json) {
        return this.customAdminService.setCustomCount(json);

    }

    /**
     * getAllCondition:(后台查询条件集合). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("后台查询条件集合")
    @ResponseBody
    @RequestMapping(value = "/getAllCondition", method = RequestMethod.POST)
    public Map<String, Object> getAllCondition() {
        return this.customAdminService.getAllCondition();

    }


    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("后台添加修改客户")
    @ResponseBody
    @RequestMapping(value = "/saveOrUpdateCustom", method = RequestMethod.POST)
    public Map<String, Object> saveOrUpdateCustom(@RequestBody Custom custom) {
        return this.customAdminService.saveOrUpdateCustom(custom);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("后台添加修改客户订单需求")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "did", value = "客户订单需求id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "service_type", value = "服务类型1", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "service_day", value = "服务天数", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "starttime", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "endtime", value = "结束时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "timetype", value = "服务类型2- 月子会所服务【月子会所服务，全程，医院服务】 居家服务【U1,U2,U3,U4,9800】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "level", value = "级别服务类型3-【U1,U2,U3,U4,9800】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "price", value = "价格", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "activityType", value = "活动类型", required = false, dataType = "String", paramType = "query"),

    })
    @RequestMapping(value = "/saveOrUpdateOrder", method = RequestMethod.POST)
    public Map<String, Object> saveOrUpdateOrder(@RequestBody String json) {
        return this.customAdminService.saveOrUpdateOrder(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("后台查询客户详情")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件【order-订单调用，admin-后台调用】", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getOneCustom", method = RequestMethod.POST)
    public Map<String, Object> getOneCustom(@RequestBody String json) {
        return this.customAdminService.getOneCustom(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("后台客户添加质询工单")
    @ResponseBody
    @RequestMapping(value = "/addConsultingOrder", method = RequestMethod.POST)
    public int addConsultingOrder(@RequestBody ConsultingOrder consultingOrder) {
        return this.customAdminService.addConsultingOrder(consultingOrder);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("后台删除客户")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/deleteCustom", method = RequestMethod.POST)
    public int deleteCustom(@RequestBody String json ) {
        return this.customAdminService.deleteCustom(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("后台客户添加销售工单")
    @ResponseBody
    @RequestMapping(value = "/addSaleOrder", method = RequestMethod.POST)
    public int addSaleOrder(@RequestBody SaleOrder saleOrder) {
        return this.customAdminService.addSaleOrder(saleOrder);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("订单小程序订单列表")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bid", value = "用户bid", required = false, paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件【all-全部，post-待派岗,before-待服务,on-服务中,after-服务完成】", required = false, paramType = "query"),
            @ApiImplicitParam(name = "start", value = "开始页数", required = false, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "开始页数", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/orderList", method = RequestMethod.POST)
    public Map<String,Object> orderList(@RequestBody String json){
        return this.customAdminService.orderList(json);
    }


    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("选择月嫂/二次确认选择")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "用户bid", required = false, paramType = "query"),
            @ApiImplicitParam(name = "oid", value = "用户bid", required = false, paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件-【one-确认选择，two-二次确认选择.three-到岗之前换人】", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/chooseMatorn", method = RequestMethod.POST)
    public int chooseMatorn(@RequestBody String json){

        return this.customAdminService.chooseMatorn(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("修改订单地址")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "o_name", value = "订单上客户姓名", required = false, paramType = "query"),
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "o_address", value = "详细地址", required = false, paramType = "query"),
            @ApiImplicitParam(name = "region", value = "所在区域", required = false, paramType = "query"),
            @ApiImplicitParam(name = "o_phone", value = "客户电话", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/updateAddress", method = RequestMethod.POST)
    public int updateAddress(@RequestBody String json){

        return this.customAdminService.updateAddress(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("提交派岗")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/submitOrder", method = RequestMethod.POST)
    public int submitOrder(@RequestBody String json){

        return this.customAdminService.submitOrder(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("确认月嫂到岗")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "did", value = "订单需求id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "service_day", value = "服务天数", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/getArrival", method = RequestMethod.POST)
    public int getArrival(@RequestBody String json){

        return this.customAdminService.getArrival(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("确认月嫂回岗/转岗")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "did", value = "订单需求id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件【confirm-正常回岗，post-转岗，continue-续单】", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/getConfirm", method = RequestMethod.POST)
    public int getConfirm(@RequestBody String json){
        return this.customAdminService.getConfirm(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("到岗之前重新推荐月嫂")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "did", value = "订单需求id", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/getMatornNull", method = RequestMethod.POST)
    public int getMatornNull(@RequestBody String json){
        return this.customAdminService.getMatornNull(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("月嫂换人/转岗换人")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "did", value = "订单需求id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件【substitution-普通换人，post-转岗换人】", required = false, paramType = "query"),
            @ApiImplicitParam(name = "label", value = "标签", required = false, paramType = "query"),
            @ApiImplicitParam(name = "age", value = "年龄要求", required = false, paramType = "query"),
            @ApiImplicitParam(name = "zodiac", value = "属相要求", required = false, paramType = "query"),
            @ApiImplicitParam(name = "educational", value = "学历要求", required = false, paramType = "query"),
            @ApiImplicitParam(name = "household", value = "户籍要求", required = false, paramType = "query"),
            @ApiImplicitParam(name = "remarks", value = "换人备注", required = false, paramType = "query"),


    })
    @RequestMapping(value = "/substitution", method = RequestMethod.POST)
    public int substitution(@RequestBody String json){

        return this.customAdminService.substitution(json);
    }

    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("订单续岗")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "did", value = "订单需求id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "price", value = "续岗总金额", required = false, paramType = "query"),
            @ApiImplicitParam(name = "continue_onePrice", value = "续岗单价金额", required = false, paramType = "query"),
            @ApiImplicitParam(name = "continue_day", value = "续岗天数", required = false, paramType = "query"),
            @ApiImplicitParam(name = "service_type", value = "服务类型1", required = false, paramType = "query"),
            @ApiImplicitParam(name = "timetype", value = "服务类型2", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/continueOrder", method = RequestMethod.POST)
    public int continueOrder(@RequestBody String json){

        return this.customAdminService.continueOrder(json);
    }

    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("暂停服务")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "did", value = "订单需求id", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/stopService", method = RequestMethod.POST)
    public int stopService(@RequestBody String json){

        return this.customAdminService.stopService(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("开启服务")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "did", value = "订单需求id", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/runService", method = RequestMethod.POST)
    public int runService(@RequestBody String json){

        return this.customAdminService.runService(json);
    }
    /**
     * save:(客户信息新增). <br/>
     * @return
     * @author Somnus
     */
    @ApiOperation("修改业务员图片/手机号/姓名")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "name", value = "姓名", required = false, paramType = "query"),
            @ApiImplicitParam(name = "photo", value = "图片", required = false, paramType = "query"),
            @ApiImplicitParam(name = "bid", value = "业务员id", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/updateBussiness", method = RequestMethod.POST)
    public Map<String,Object> updateBussiness(@RequestBody String json){
        return this.customAdminService.updateBussiness(json);
    }
}
