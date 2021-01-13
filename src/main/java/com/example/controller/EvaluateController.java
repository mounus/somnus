package com.example.controller;

import com.example.entiy.Score;
import com.example.service.EvaluateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/wx/evaluate",produces="application/json;charset=utf-8")
@Api(tags="月嫂评价/")
public class EvaluateController {
    @Autowired(required = false)
    private EvaluateService evaluateService;


    /**
     * save:(月嫂评价信息新增). <br/>
     *
     * @return
     */
    @ApiOperation("添加月嫂评价")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂mid", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "customer_photo", value = "客户照片", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "address", value = "地址", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "praise", value = "评分", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "label", value = "标签", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "evaluate_word", value = "好评文字", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "evaluate_photo", value = "好评照片", required = false, dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public int save(@RequestBody String json) {
        int states=this.evaluateService.save(json);
        if(states>0){
            return 1;
        }else{
            return  0;
        }
    }




    /**
     * save:(月嫂评价信息新增). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("添加月嫂十项全能")
    @ResponseBody
    @RequestMapping(value = "/saveScore", method = RequestMethod.POST)
    public int saveScore(@RequestBody Score score) {
         return  this.evaluateService.saveScore(score);

    }

    /**
     * save:(删除月嫂评价). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("删除月嫂评价")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "月嫂mid", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/deleteEvaluate", method = RequestMethod.POST)
    public int deleteEvaluate(@RequestBody String json) {
        return  this.evaluateService.deleteEvaluate(json);

    }
    /**
     * @getById: 通过id查询。
     * @return: java.util.List<com.example.entity.Evaluate>
     */
    @ApiOperation("查询id")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂mid", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public Map<String, Object> getById(Integer mid) {

        return   evaluateService.getById(mid);

    }

    /**
     * @getById: 通过id查询。
     * @return: java.util.List<com.example.entity.Evaluate>
     */
    @ApiOperation("查询评语模板")
    @ResponseBody
    @RequestMapping(value = "/getComment", method = RequestMethod.GET)
    public List<String> getComment() {
        return   evaluateService.getComment();

    }
    /**
     * @getById: 通过id查询。
     * @return: java.util.List<com.example.entity.score>
     */
    @ApiOperation("查询月嫂十项全能")
    @ResponseBody
    @RequestMapping(value = "/getScoreById", method = RequestMethod.POST)
    public Score getScoreById(@RequestBody String json) {
        return   evaluateService.getScoreById(json);

    }
    /**
     * @getById: 通过id查询。
     * @return: java.util.List<com.example.entity.score>
     */
    @ApiOperation("修改月嫂十项全能")
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public int update(@RequestBody Score score) {
        return   evaluateService.update(score);

    }
}
