package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value="/wx/product",produces="application/json;charset=utf-8")
@Api(tags="产品管理")
public class ProductController {

    @Autowired(required = false)
    private ProductService productService;

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台产品查询")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/getAdmin", method = RequestMethod.POST)
    public Map<String,Object> getAdmin(@RequestBody String json) {

        return   productService.getAdmin(json);
    }

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台产品通过id/姓名/工号查询")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "number", value = "姓名/编号/id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/getByNumber", method = RequestMethod.POST
    )
    public Map<String,Object> getByNumber(@RequestBody String json) {

        return   productService.getByNumber(json);
    }

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("通过月嫂等级/经纪人查询")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "agent", value = "经理人", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序方式【desc-倒序,asc-正序】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "grade", value = "级别", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "institution_name", value = "机构名称", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/getByGrade", method = RequestMethod.POST)
    public Map<String,Object> getByGrade(@RequestBody String json) {

        return   productService.getByGrade(json);
    }


}
