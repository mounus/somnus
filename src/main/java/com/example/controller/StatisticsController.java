package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(value="/web/statistics",produces="application/json;charset=utf-8")
@Api(tags="统计管理")
public class StatisticsController {
    @Autowired(required = false)
    private StatisticsService statisticsService;


    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("统计经理人下月嫂字段的完整度")
    @ResponseBody

    @RequestMapping(value = "/getComplete", method = RequestMethod.POST)
    public List<Map<String, Object>> getComplete() {

        return   statisticsService.getComplete();
    }

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("数据中心")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "year", value = "默认当前年份", required = false, paramType = "query"),
            @ApiImplicitParam(name = "month", value = "month为空-13", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/allStatistics", method = RequestMethod.POST)
    public Map<String, Object> allStatistics(@RequestBody String json ) {

        return   statisticsService.allStatistics(json);
    }


    /**
     * @description: 获取表中所有信息。
     *
     */
    @ApiOperation("数据中心定时器")
    @ResponseBody
    @RequestMapping(value = "/statisticsTiming", method = RequestMethod.POST)
    public  void statisticsTiming( ) {

        statisticsService.statisticsTiming();
    }


}
