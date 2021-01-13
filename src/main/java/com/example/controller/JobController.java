package com.example.controller;

import com.example.entiy.Job_evaluate;
import com.example.entiy.Matorn;
import com.example.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/wx/job",produces="application/json;charset=utf-8")
@Api(tags="月嫂职业评测")
public class JobController {

    @Autowired(required = false)
    private JobService jobService;

    /**
     * @getById: 通过id查询。
     * @return: java.util.List<com.example.entity.Job_evaluate>
     */
    @ApiOperation("通过mid查询月嫂基础信息")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂mid", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public Map<String, Object> getById(Integer mid) {
        return   jobService.getById(mid);

    }

    /**
     * @description: 添加一条月嫂职业评测
     * @param: usersEntity
     * @return: java.util.Map<java.lang.String,java.lang.Boolean>
     */
    @ApiOperation("添加一条月嫂职业评测")
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public int  save(@RequestBody String  json) {

        return jobService.save(json);
    }
    /**
     * @description: 查询月嫂职业评测
     * @param: usersEntity
     * @return: java.util.Map<java.lang.String,java.lang.Boolean>
     */
    @ApiOperation("查询月嫂职业评测")
    @ResponseBody
    @RequestMapping(value = "/findOne", method = RequestMethod.POST)
    public  Map<String, Object>  findOne( Integer mid) {

        return jobService.findOne(mid);
    }

    /**
     * @description: 修改月嫂职业评测
     * @param: usersEntity
     * @return: java.util.Map<java.lang.String,java.lang.Boolean>
     */
    @ApiOperation("修改月嫂职业评测")
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public int update(@RequestBody  String json) {

        return jobService.update(json);
    }

    /**
     * @description: 修改月嫂职业评测
     * @param: usersEntity
     * @return: java.util.Map<java.lang.String,java.lang.Boolean>
     */
    @ApiOperation("test")
    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public Map<String,Object> test(@RequestBody  String json) {

        return jobService.test(json);
    }




}
