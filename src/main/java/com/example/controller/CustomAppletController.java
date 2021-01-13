package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entiy.Custom;
import com.example.service.CustomAppletService;
import com.example.service.CustomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.example.util.BASE64.decryptBASE64;

@RestController
@RequestMapping(value="/wx/customApplet",produces="application/json;charset=utf-8")
@Api(tags="客户会员小程序管理")
public class CustomAppletController {
    @Autowired(required = false)
    private CustomAppletService customAppletService;

    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("登录授权，获取客户信息")
    @ResponseBody
    @ApiImplicitParams({

            @ApiImplicitParam(name = "encryptedData", value = "被加密的数据", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "iv", value = "偏移量", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sessionKey", value = "加密秘钥", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cname", value = "客户昵称", required = false, dataType = "String", paramType = "query"),

    })
    @RequestMapping(value = "/getCustomAppletLogin", method = RequestMethod.POST)
    public Map<String,Object> getCustomAppletLogin(@RequestBody String json) {
        return  this.customAppletService.getCustomAppletLogin(json);

    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("添加客户图片/昵称")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "c_photo", value = "客户图片", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "c_name", value = "客户昵称", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/addCustomPhoto", method = RequestMethod.POST)
    public int  addCustomPhoto(@RequestBody String json) {
        return  this.customAppletService.addCustomPhoto(json);

    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("推荐月嫂")
    @ResponseBody
    @ApiImplicitParams({

            @ApiImplicitParam(name = "start", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "constellation", value = "客户星座", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "born", value = "客户出生年月", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/recommendMatorn", method = RequestMethod.POST)
    public List<Map<String,Object>> recommendMatorn(@RequestBody String json) {
        return  this.customAppletService.recommendMatorn(json);

    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("通过出生年月获取客户星座/姓名，出生年月")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getCustomConstellation", method = RequestMethod.POST)
    public Map<String,Object> getCustomConstellation(@RequestBody String json) {
        return  this.customAppletService.getCustomConstellation(json);

    }

    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("客户添加收藏")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "isCustomCollect", value = "是否收藏【0-不收藏，1-收藏】", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/addCollect", method = RequestMethod.POST)
    public int addCollect(@RequestBody String json) {
        return  this.customAppletService.addCollect(json);

    }

    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("查询月嫂详情及添加浏览记录")
    @ResponseBody
    @ApiImplicitParams({

            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/getOneMatorn", method = RequestMethod.POST)
    public List<Map<String,Object>> getOneMatorn(@RequestBody String json) {
        return  this.customAppletService.getOneMatorn(json);

    }


    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("客户的所有收藏/浏览记录")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "somnus", value = "条件【collect-收藏,browse-浏览记录】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/myCollectBrowse", method = RequestMethod.POST)
    public Map<String,Object> myCollectBrowse(@RequestBody String json) {
        return  this.customAppletService.myCollectBrowse(json);

    }


  /**
     * @return
     * @author Somnus
     */
    @ApiOperation("预约面试推荐业务员")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/appointmentBusiness", method = RequestMethod.POST)
    public Map<String,Object> appointmentBusiness(@RequestBody String json) {
        return  this.customAppletService.appointmentBusiness(json);

    }
    /**
     * @return
     * @author Somnus
     */
    @SneakyThrows
    @ApiOperation("通过code获取sessionKey")
    @ResponseBody
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/getSessionKey", method = RequestMethod.POST)
    public Map<String,Object> getSessionKey(@RequestBody String json) {

        JSONObject jsonObject= JSON.parseObject(json);
        String code=jsonObject.getString("code");

        //加密appid
        String appidByte="d3gzMDNjZTNhODUzMmZkNmRk";
        byte[] appidArrayKey = decryptBASE64(appidByte);
        String appid =new String(appidArrayKey);
        //加密的accessKeySecret
        String byteKeySecret="ZmMxYTEwZjI0NmVjZDQ1YzcyY2FhNWMxZTAyZmVhODk=";
        byte[] byteArray = decryptBASE64(byteKeySecret);
        String secret = new String(byteArray);

        StringBuffer sb=new StringBuffer();
        sb.append("https://api.weixin.qq.com/sns/jscode2session?");
        sb.append("appid="+appid);
        sb.append("&secret="+secret);
        sb.append("&js_code="+code);
        sb.append("&grant_type=authorization_code");
        JSONObject map =null;
        try {
            HttpClient client = HttpClientBuilder.create().build();//构建一个Client
            HttpGet get = new HttpGet(sb.toString());    //构建一个GET请求
            HttpResponse response = client.execute(get);//提交GET请求
            HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
            String content = EntityUtils.toString(result);
            map =JSON.parseObject(content);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取信息失败");
        }
        return map;
    }

    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("预约面试业务员/月嫂")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bid", value = "业务员id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public int appointment(@RequestBody String json) {
        return  this.customAppletService.appointment(json);

    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("客户填写推荐信息")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "c_name", value = "客户姓名", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "production_situation", value = "生产情况【0-未生产，1-生产】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "nodate", value = "未生产日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "production_date", value = "已生产日期", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "born", value = "客户出生年月", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/addInformation", method = RequestMethod.POST)
    public Map<String, Object> addInformation(@RequestBody String json) {

        return  this.customAppletService.addInformation(json);

    }

    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("删除客户记录")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/deleteBrowse", method = RequestMethod.POST)
    public int deleteBrowse(@RequestBody String json) {
        return  this.customAppletService.deleteBrowse(json);

    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("客户我的订单")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/myOrder", method = RequestMethod.POST)
    public  List< Map<String, Object>>  myOrder(@RequestBody String json) {
        return  this.customAppletService.myOrder(json);

    }

    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("客户重新选择月嫂")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/newChoiceMatorn", method = RequestMethod.POST)
    public  int  newChoiceMatorn(@RequestBody String json) {
        return  this.customAppletService.newChoiceMatorn(json);

    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("客户添加派岗地址")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "oid", value = "订单id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "did", value = "需求订单id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "starttime", value = "开始时间", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "region", value = "大地址", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "o_address", value = "详细地址", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "service_day", value = "服务时长", required = false, dataType = "String", paramType = "query"),

    })
    @RequestMapping(value = "/addAddress", method = RequestMethod.POST)
    public  int  addAddress(@RequestBody String json) {
        return  this.customAppletService.addAddress(json);

    }
    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("客户重新推荐月嫂")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/newRecommend", method = RequestMethod.POST)
    public  int  newRecommend(@RequestBody String json) {
        return  this.customAppletService.newRecommend(json);

    }

    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("客户我的推荐")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/myRecommend", method = RequestMethod.POST)
    public  List< Map<String, Object>>  myRecommend(@RequestBody String json) {
        return  this.customAppletService.myRecommend(json);

    }

    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("客户的排名")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "客户id", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value = "/myRank", method = RequestMethod.POST)
    public   Map<String, Object>  myRank(@RequestBody String json) {
        return  this.customAppletService.myRank(json);

    }
}
