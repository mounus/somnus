package com.example.controller;

import com.example.service.ActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/wx/acivity", produces = "application/json;charset=UTF-8")
@Api(tags = "活动管理")
public class ActivityController {

    @Autowired(required = false)
    private ActivityService activityService;


    /**
     * getAllReservation:(查询所有预约客户). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("查询所有预约客户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getAllReservation", method = RequestMethod.POST)
    public Map<String, Object> getAllReservation(@RequestBody String json) {
        return activityService.getAllReservation(json);
    }
    /**
     * getAllHref:(查询所有活动链接). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("查询所有活动链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getAllHref", method = RequestMethod.POST)
    public Map<String, Object> getAllHref(@RequestBody String json) {
        return activityService.getAllHref(json);
    }
    /**
     * getAllHref:(查询所有活动链接). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("添加一个活动来源链接")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "origin_name", value = "来源名称", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/addHref", method = RequestMethod.POST,produces = "application/json;charset=UTF-8")
    public int addHref(@RequestBody String json) {
        return activityService.addHref(json);
    }

}

