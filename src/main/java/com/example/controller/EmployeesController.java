package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entiy.User;
import com.example.service.EmployeesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/wx/employees",produces="application/json;charset=utf-8")
@Api(tags="员工管理")
public class EmployeesController {
    @Autowired(required = false)
    private EmployeesService employeesService;

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台查询所有员工/机构")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【" +
                    "all-全部，" +
                    "agent-经理人," +
                    "login-登记员," +
                    "quality-品控," +
                    "bussiness-业务员," +
                    "post-派岗," +
                    "service-客服," +
                    "mechanism-机构," +
                    "channel-渠道," +
                    "】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/getAllEmployees", method = RequestMethod.POST)
    public Map<String,Object> getAllEmployees(@RequestBody String json) {

        return   employeesService.getAllEmployees(json);
    }
    /**
     * @description: 获取表中所有信息。
     * @return:
     */
    @ApiOperation("后台员工/机构添加")
    @ResponseBody
    @RequestMapping(value = "/addEmployees", method = RequestMethod.POST)
    public int addEmployees(@RequestBody User user) {

        return   employeesService.addEmployees(user);
    }
    /**
     * @description: 获取表中所有信息。
     * @return:
     */
    @ApiOperation("后台员工/机构修改")
    @ResponseBody
    @RequestMapping(value = "/updateEmployees", method = RequestMethod.POST)
    public int updateEmployees(@RequestBody User user) {

        return   employeesService.updateEmployees(user);
    }
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台员工/机构单一查询")
    @ResponseBody
    @RequestMapping(value = "/findUserOne", method = RequestMethod.POST)
    public User findUserOne(@RequestBody  String  json)
    {
        return   employeesService.findUserOne(json);
    }
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台员工/机构删除")
    @ResponseBody
    @RequestMapping(value = "/deleteEmployees", method = RequestMethod.POST)
    public int deleteEmployees(@RequestBody  String  json)
    {
        return   employeesService.deleteEmployees(json);
    }


    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台员工账号管理列表")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "单位id【没有为0】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "did", value = "部门id【没有为0】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "pid", value = "权限id【没有为0】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "number", value = "姓名手机工号id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),


    })
    @RequestMapping(value = "/getEmployeesList", method = RequestMethod.POST)
    public Map<String,Object> getEmployeesList(@RequestBody  String  json) {

        return   employeesService.getEmployeesList(json);
    }
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("员工账号条件")
    @ResponseBody
    @RequestMapping(value = "/employeesCondition", method = RequestMethod.POST)
    public Map<String,Object> employeesCondition() {

        return   employeesService.employeesCondition();
    }
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台员工添加")
    @ResponseBody
    @RequestMapping(value = "/addOrUpdateUser", method = RequestMethod.POST)
    public int addOrUpdateUser(@RequestBody User user) {

        return   employeesService.addOrUpdateUser(user);
    }
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台员工查询")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "员工id", required = false,  paramType = "query"),


    })
    @RequestMapping(value = "/findOneUser", method = RequestMethod.POST)
    public List<Map<String,Object>>  findOne(@RequestBody String json) {

        return   employeesService.findOneUser(json);
    }
}
