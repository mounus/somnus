package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entiy.Period;
import com.example.service.PeriodService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping(value="/wx/period",produces="application/json;charset=utf-8")
@Api(tags="月嫂服务状态管理")
public class PeriodController {

@Autowired(required = false)
private PeriodService periodService;


    /**
     * @save: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("修改月嫂档期")
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public int  update(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String month=jsonObject.getString("month");
       // System.out.println("----------"+month);
        int states = this.periodService.update(json);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("条件查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【" +
                    "all-全部，" +
                    "shelf-上架," +
                    "black-黑名单" +
                    "】", required = true,  paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "用户uid", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/listBy", method = RequestMethod.GET)
    public List<Map<String,Object>> listBy(String somnus,Integer uid,Integer start) {

      return   periodService.listBy(somnus,uid,start);

    }
    /**
     * @getById: 通过name查询。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("查询name")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户uid", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "name", value = "月嫂姓名", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/getByName", method = RequestMethod.POST)
    public List<Map<String, Object>> getByName(@RequestBody String json) {

        return    periodService.getByName(json);

    }


//    /**
//     * @save: 获取表中所有信息。
//     * @return: java.util.List<com.example.entity.Period>
//     */
//    @ApiOperation("添加修改")
//    @ResponseBody
//    @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
//    public int  saveOrUpdate(@RequestBody String json) {
//        JSONObject jsonObject = JSON.parseObject(json);//转换类型
//        int states = this.periodService.saveOrUpdate(json);
//        if (states > 0) {
//            return 1;
//        } else {
//            return 0;
//        }
//
//    }

    /**
     * @getById: 通过id查询。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("查询id")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂mid", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public List<Map<String, Object>>  getById(Integer mid) {

                  return    periodService.getById(mid);

    }

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台查询")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【" +
                    "all-全部，" +
                    "black-黑名单" +
                    "shelf-上架" +
                    "】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/getAdmin", method = RequestMethod.POST)
    public Map<String,Object> getAdmin(@RequestBody String json) {
       JSONObject jsonObject=JSON.parseObject(json);

        return   periodService.getAdmin(json);
    }
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台通过id/姓名/工号查询")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【" +
                    "all-全部，" +
                    "black-黑名单" +
                    "】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "number", value = "姓名/编号/id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/getByNumber", method = RequestMethod.POST
    )
    public Map<String,Object> getByNumber(@RequestBody String json) {

        return   periodService.getByNumber(json);
    }


    /**
     * updateGrade_agent:(). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("添加经纪人修改grade")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grade", value = "等级", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "id", value = "月嫂id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "aid", value = "后台登陆人aid", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "agent", value = "要修改的经理人", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "uid", value = "录入人uid", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "key", value = "", required = false,  paramType = "query"),
    })
    @RequestMapping(value ="/updateGrade_agent", method = RequestMethod.POST)
    public int  updateGrade_agent(@RequestBody String json){
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        return periodService.updateGrade_agent(json);

    }

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("后台通过时间查询")
    @ResponseBody
    @RequestMapping(value = "/getByTime", method = RequestMethod.POST)
    public List<Map<String,Object>> getByTime(@RequestBody String json) {

        return   periodService.getByTime(json);
    }


    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("查询月嫂动态")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户uid", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "页数", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/dynamic", method = RequestMethod.POST)
    public List<Map<String,Object>> dynamic(@RequestBody String json) {

        return   periodService.dynamic(json);
    }
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("通过月嫂等级/经纪人查询")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【" +
                    "all-全部，" +
                    "byGrade-待定级," +
                    "ding-已定级," +
                    "allGrade-待晋级," +
                    "quit-离职," +
                    "rest-休息," +
                    "black-黑名单" +
                    "】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "agent", value = "经理人", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "sort", value = "排序方式【desc-倒序,asc-正序】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "grade", value = "级别", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "institution_name", value = "机构名称", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/getByGrade", method = RequestMethod.POST)
    public Map<String,Object> getByGrade(@RequestBody String json) {


        return   periodService.getByGrade(json);
    }
    /**
     * updateCollect:(). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation(" 修改月嫂是否收藏")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "iscollect", value = "是否收藏【0-未收藏,1-已收藏】", required = false,  paramType = "query"),
    })
    @RequestMapping(value ="/updateCollect", method = RequestMethod.POST)
    public int  updateCollect(@RequestBody String json){
        return periodService.updateCollect(json);

    }

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("查询我的收藏")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/myCollect", method = RequestMethod.POST)
    public List<Map<String,Object>> myCollect(@RequestBody String json) {

        return   periodService.myCollect(json);
    }
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("通过月嫂姓名查询我的收藏")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "name", value = "月嫂姓名", required = false,  paramType = "query"),
    })
    @RequestMapping(value = "/myCollectByName", method = RequestMethod.POST)
    public List<Map<String,Object>> myCollectByName(@RequestBody String json) {

        return   periodService.myCollectByName(json);
    }
}
