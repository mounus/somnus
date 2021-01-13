package com.example.controller;

import com.example.entiy.Custom;
import com.example.entiy.Order_demand;
import com.example.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/wx/order", produces = "application/json;charset=utf-8")
@Api(tags = "订单管理")
public class OrderControlleer {
    @Autowired(required = false)
    private OrderService orderService;

    /**
     * demand:(客户订单需求新增). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("添加客户订单需求")
    @ResponseBody
    @RequestMapping(value = "/saveDemand", method = RequestMethod.POST)
    public int saveDemand(@RequestBody Order_demand order_demand) {
        return this.orderService.saveDemand(order_demand);

    }

    /**
     * demand:(客户订单需求新增). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("修改客户订单需求")
    @ResponseBody
    @RequestMapping(value = "/updateDemand", method = RequestMethod.POST)
    public int updateDemand(@RequestBody Order_demand order_demand) {
        return this.orderService.updateDemand(order_demand);

    }



    /**
     * managerOrder:(). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("经理人小程序查询订单")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户uid", required = false, paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件【all-全部,before-待上岗,work-上岗中/待回岗,after-待评价,】"
                    , required = false, paramType = "query"),
            @ApiImplicitParam(name = "other", value = "work【on-待上岗,stay-待回岗】", required = false, paramType = "query"),

    })
    @RequestMapping(value = "/managerOrder", method = RequestMethod.POST)
    public List<Map<String, Object>> managerOrder(@RequestBody String json) {

        return orderService.managerOrder(json);
    }

    /**
     * managerOrder:(). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("后台订单列表")
    @ResponseBody
    @RequestMapping(value = "/allOrder", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【居家服务，月子会所服务】", required = false, paramType = "query"),
            @ApiImplicitParam(name = "monthFirst", value = "搜索开始时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "monthLast", value = "搜索结束时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "bid", value = "母婴顾问 id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "service_type", value = "服务类型1", required = false, paramType = "query"),
            @ApiImplicitParam(name = "timetype", value = "服务类型2", required = false, paramType = "query"),
            @ApiImplicitParam(name = "origin", value = "来源1", required = false, paramType = "query"),
            @ApiImplicitParam(name = "channel", value = "来源2", required = false, paramType = "query"),
            @ApiImplicitParam(name = "work_states", value = "订单进度", required = false, paramType = "query"),
            @ApiImplicitParam(name = "number", value = "姓名、手机号、id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, paramType = "query"),

    })
    public Map<String, Object> allOrder(@RequestBody String json) {
        return orderService.allOrder(json);
    }
    /**
     * managerOrder:(). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("后台订单筛选条件")
    @ResponseBody
    @RequestMapping(value = "/orderCondition", method = RequestMethod.POST)
    public Map<String, Object> orderCondition() {
        return orderService.orderCondition();
    }
    /**
     * managerOrder:(). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("订单小程序居家，会所，本月昨天天数单数金额")
    @ResponseBody
    @RequestMapping(value = "/homeAndClub", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bid", value = "bid", required = false, paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "【home-居家，club-会所】", required = false, paramType = "query"),

    })
    public Map<String, Object> homeAndClub(@RequestBody String json) {

        return orderService.homeAndClub(json);
    }


}
