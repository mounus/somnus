package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dto.MatornDto;
import com.example.entiy.Matorn;

import com.example.service.MatornService;

import com.example.util.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping(value="/wx/matorn",produces="application/json;charset=utf-8")
@Api(tags="月嫂管理")
public class MatornController {

    @Autowired(required = false)
    private MatornService matornService;

    /**
     * save:(月嫂基础信息新增). <br/>
     * @author Somnus
     * @return
     */
    @ApiOperation("添加月嫂")
    @ResponseBody
    @RequestMapping(value="/save",method = RequestMethod.POST)
    public int save(@RequestBody Matorn matorn){
        int states=this.matornService.save(matorn);

        if(states>0){
            int id =this.matornService.getForId();
            return  id;
        }if (states==0){
            return 0;
        }else {
            return -1;
        }


    }

    /**
     * getById:(通过id查询月嫂相关信息). <br/>
     * @author Somnus
     * @return
     */
    @ApiOperation("查询id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂mid", required = false, dataType = "String", paramType = "query"),

    })
    @RequestMapping(value="/getById",method = RequestMethod.GET)
    @ResponseBody
    public  List<Map<String,Object>> getById(Integer id) {

                return matornService.getById(id);

    }
    /**
     * uodate:(修改月嫂四表相关信息). <br/>
     * @author Somnus
     * @return
     */

    @ApiOperation("修改四个表")
    @RequestMapping(value ="/update", method = RequestMethod.PUT)
    public int update(@RequestBody String json) {

        int states = this.matornService.update(json);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }
   /**
    * uodate:(修改月嫂表). <br/>
    * * @author Somnus
    *     @return
    */
        @ApiOperation("修改单表")
        @RequestMapping(value ="/updateMatorn", method = RequestMethod.PUT)
        public int updateMatorn(@RequestBody Matorn matorn){
            int states = this.matornService.updateMatorn(matorn);
            if (states > 0) {
                return 1;
            }
            if (states ==-2) {
                return -2;
            }

            else {
                return 0;
            }

        }

    /**
     * uodate:(修信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("添加修改")
    @RequestMapping(value ="/saveOrUpdate", method = RequestMethod.POST)
    public int saveOrUpdate(@RequestBody Matorn matorn){
        System.out.println(matorn.getId());
        int states = this.matornService.saveOrUpdate(matorn);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

//    /**
//     * :(获取身份证信息). <br/>
//     * * @author Somnus
//     *     @return
//     */
//    @ApiOperation("获取身份证信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "identity", value = "身份证信息", required = false, dataType = "String", paramType = "query"),
//    })
//    @RequestMapping(value ="/getIdentity", method = RequestMethod.POST)
//    public String getIdentity(@RequestBody String json){
//        JSONObject jsonObject = JSON.parseObject(json);//转换类型
//        String image="data:image/png;base64,"+jsonObject.getString("image");
//
//         String host = "https://orcidcard.market.alicloudapi.com";
//         String path = "/idCardAuto";
//         String method = "POST";
//         String appcode = "5ccef636c46840138f6cc9eb52040790";
//
//       // String host = "https://ocridcard.market.alicloudapi.com";
//       // String path = "/idimages";
//       // String method = "POST";
//       // String appcode = "f4880d954dc54a679b8e70532c1f0bcc";
//        Map<String, String> headers = new HashMap<String, String>();
//        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//        headers.put("Authorization", "APPCODE " + appcode);
//        //根据API的要求，定义相对应的Content-Type
//        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        Map<String, String> querys = new HashMap<String, String>();
//        Map<String, String> bodys = new HashMap<String, String>();
//
//        bodys.put("image",image);
//        bodys.put("idCardSide", "front");//默认正面，背面请传back
//        String identity=null;
//        try{
//            HttpUtils httpUtils=new HttpUtils();
//            HttpResponse  response = httpUtils.doPost(host, path, method, headers, querys, bodys);
//            identity=EntityUtils.toString(response.getEntity());
//           // System.out.println(identity);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("identity = " + identity);
//        System.out.println(" matornService.getIdentity(identity) = " +  matornService.getIdentity(identity));
//       return   matornService.getIdentity(identity);
//
//    }

    /**
     * :(获取身份证信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("获取身份证信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "identity", value = "身份证信息", required = false, dataType = "String", paramType = "query"),
    })
    @RequestMapping(value ="/getIdentity", method = RequestMethod.POST,produces = "application/x-www-form-urlencoded;charset=utf-8")
    public String getIdentity(@RequestBody String json){
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String host = "https://orcidcard.market.alicloudapi.com";// 【1】请求地址 支持http 和 https 及 WEBSOCKET
        String path = "/idCardAuto"; // 【2】后缀
        String appcode = "5ccef636c46840138f6cc9eb52040790"; // 【3】开通服务后 买家中心-查看AppCode
        String body="image=data:image/png;base64,"+jsonObject.getString("image");   // 【4】请求参数，详见文档描述

        // 或者base64
        // String body = "image=data:image/jpeg;base64,/9j/4A......";
        String urlSend = host + path; // 【5】拼接请求链接
        String identity=null;
        try {
            URL url = new URL(urlSend);
            HttpURLConnection httpURLCon = (HttpURLConnection) url.openConnection();
            httpURLCon.setRequestMethod("POST");
            httpURLCon.setRequestProperty("Authorization", "APPCODE " + appcode);// 格式Authorization:APPCODE
            // (中间是英文空格)
            StringBuilder postData = new StringBuilder(body);

            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            httpURLCon.setDoOutput(true);
            OutputStream out = httpURLCon.getOutputStream();
            out.write(postDataBytes);
            out.close();
            int httpCode = httpURLCon.getResponseCode();

            if (httpCode == 200) {
                System.out.println("httpURLCon.getInputStream() = " + httpURLCon.getInputStream());
                String json1 = read(httpURLCon.getInputStream());
                System.out.println("正常请求计费(其他均不计费)");
                System.out.println("获取返回的json:");
                System.out.println("json1 = " + json1);
                identity= matornService.getIdentity(json1);
                System.out.println("identity = " + identity);

            } else {
                Map<String, List<String>> map = httpURLCon.getHeaderFields();
                String error = map.get("X-Ca-Error-Message").get(0);
                if (httpCode == 400 && error.equals("Invalid AppCode")) {
                    System.out.println("AppCode错误 ");
                } else if (httpCode == 400 && error.equals("Invalid Url")) {
                    System.out.println("请求的 Method、Path 或者环境错误");
                } else if (httpCode == 400 && error.equals("Invalid Param Location")) {
                    System.out.println("参数错误");
                } else if (httpCode == 403 && error.equals("Unauthorized")) {
                    System.out.println("服务未被授权（或URL和Path不正确）");
                } else if (httpCode == 403 && error.equals("Quota Exhausted")) {
                    System.out.println("套餐包次数用完 ");
                } else {
                    System.out.println(httpCode);
                    System.out.println("参数名错误 或 其他错误");
                    System.out.println(error);
                }
            }

        } catch (MalformedURLException e) {
            System.out.println("URL格式错误");
        } catch (UnknownHostException e) {
            System.out.println("URL地址错误");
        } catch (Exception e) {
            // 打开注释查看详细报错异常信息
            // e.printStackTrace();
        }
       return identity;
    }
    /*
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "UTF-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }


    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("月嫂服务记录")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "month", value = "月份", required = false,  paramType = "query"),

    })
    @RequestMapping(value = "/serviceRecord", method = RequestMethod.POST)
    public Map<String, Object> serviceRecord(@RequestBody String json) {

        return   matornService.serviceRecord(json);
    }
    /**
     * uodate:(修信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("添加月嫂健康证pdf")
    @RequestMapping(value ="/addHeathly", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mid", value = "月嫂id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "base64sString", value = "pdf转base64", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "fileName", value = "文件名称", required = false,  paramType = "query"),
    })
    public int addHeathly(@RequestBody String json){
        return matornService.addHeathly(json);

    }
    /**
     * uodate:(修信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("月嫂管理列表")
    @RequestMapping(value ="/getAllmatorn", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "登录人id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "zodiac", value = "月嫂属相", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "grade", value = "月嫂等级", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "shelf", value = "月嫂状态【0-为空，1-黑名单，2-未上架，3-已上架，4-服务中，5-取消黑名单】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "bid", value = "月嫂经理人id 【0-没有，经理人id】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "source", value = "月嫂来源1", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "origin", value = "月嫂来源2", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "isPrice", value = "押金质保金状态【0-为空，1-待激活，2-已交押金，3-已退押金，4-已交质保金，5-已退质保金】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "powerFiled", value = "权限字段", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "number", value = "月嫂姓名工号id", required = false,  paramType = "query"),
    })
    public Map<String, Object> getAllmatorn(@RequestBody String json){

        return matornService.getAllmatorn(json);
    }
    /**
     * uodate:(修信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("月嫂条件集合")
    @RequestMapping(value ="/matornCondition", method = RequestMethod.POST)
    public Map<String, Object> matornCondition(){
        return matornService.matornCondition();
    }

    /**
     * uodate:(修信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("编辑月嫂状态")
    @RequestMapping(value ="/editMatorn", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grade", value = "月嫂等级", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "shelf", value = "月嫂状态【0-为空，1-黑名单，2-未上架，取消拉黑，3-已上架，4-服务中】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "bid", value = "月嫂经理人id 【0-没有，经理人id】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "mid", value = "mid", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "isPrice", value = "押金质保金状态【0-为空，1-待激活，2-已交押金，3-已退押金，4-已交质保金，5-已退质保金】", required = false,  paramType = "query"),

    })
    public int editMatorn(@RequestBody  String json){

        return matornService.editMatorn(json);
    }
    /**
     * uodate:(修信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("编辑月嫂数量")
    @RequestMapping(value ="/setMatornCount", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "类型【1-年客户数，2-今日新增，3-本周新增，4-本月新增】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "count", value = "设置数量", required = false, dataType = "String", paramType = "query"),
    })
    public int setMatornCount(@RequestBody  String json){

        return matornService.setMatornCount(json);
    }
    /**
     * uodate:(修信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("新的添加月嫂")
    @RequestMapping(value ="/addMatornDto", method = RequestMethod.POST)
    public int addMatornDto(@RequestBody MatornDto matornDto){

        return matornService.addMatornDto(matornDto);
    }

    /**
     * uodate:(修信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("从业年限集合")
    @RequestMapping(value ="/workAge", method = RequestMethod.POST)
    public List<Map<String,Object>> workAge(){

        return matornService.workAge();
    }
}






