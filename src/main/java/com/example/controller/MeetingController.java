package com.example.controller;

import com.example.entiy.Article;
import com.example.entiy.Picture;
import com.example.service.MeetingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/wx/meeting", produces = "application/json;charset=UTF-8")
@Api(tags = "悦月会")
public class MeetingController {
    @Autowired(required = false)
    private MeetingService meetingService;


    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("后台添加头部图片")
    @ResponseBody
    @RequestMapping(value = "/savePicture", method = RequestMethod.POST)
    public int savePicture(@RequestBody Picture picture) {
        return  this.meetingService.savePicture(picture);
    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("后台添加文章")
    @ResponseBody
    @RequestMapping(value = "/saveOrUpdateArticle", method = RequestMethod.POST)
    public int saveOrUpdateArticle(@RequestBody Article article) {
        return  this.meetingService.saveOrUpdateArticle(article);
    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("后台查询单个文章内容")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/getArticle", method = RequestMethod.POST)
    public  Article getOneArticle(@RequestBody String json) {
        return  this.meetingService.getOneArticle(json);
    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("后台查询全部文章内容")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件【article-文章,picture-图片】", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/getAllArticleOrPicture", method = RequestMethod.POST)
    public Map<String, Object> getAllArticleOrPicture(@RequestBody String json) {
        return  this.meetingService.getAllArticleOrPicture(json);
    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("文章的分类及其标签")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【article-后台文章添加/修改,list-网页】", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/articleType", method = RequestMethod.POST)
    public List<Map<String, Object>> articleType(@RequestBody String json) {

        return  this.meetingService.articleType(json);
    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("删除文章/图片")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id号", required = false, paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件【article-文章,picture-图片】", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/deleteArticleOrPicture", method = RequestMethod.POST)
    public int deleteArticleOrPicture(@RequestBody String json) {

        return  this.meetingService.deleteArticleOrPicture(json);
    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("文章列表")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "条件【1-知识12活动】", required = false, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "条件【0-今日知识,1-孕期知识，2-分娩知识，3-产后知识，4育儿知识】", required = false, paramType = "query"),
            @ApiImplicitParam(name = "label", value = "标签 没有为0-【1-1-8周，2-8-16周，3-16-24周，4-24-32周，5-32-42周】2-【1-1-2个月，2-2-4个月，3-4-6个月】，3-【1-准备期间，2-准备期间，3-准备期间】", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/articleList", method = RequestMethod.POST)
    public List<Map<String, Object>> articleList(@RequestBody String json) {

        return  this.meetingService.articleList(json);
    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("轮播图")
    @ResponseBody
    @RequestMapping(value = "/pictureList", method = RequestMethod.POST)
    public List<Map<String, Object>> pictureList(@RequestBody String json ) {
        return  this.meetingService.pictureList(json);
    }
}
