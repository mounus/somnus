package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.HttpMethod;
import com.example.util.WXAuthUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Map;
import java.util.UUID;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.alibaba.fastjson.JSONObject;
import org.springframework.web.client.RestTemplate;

import static com.example.util.BASE64.decryptBASE64;


/**
 *  
 *
 * @author  lbh 
 * @version 1.0 
 * @date 创建时间：2018年1月18日 下午12:35:11 
 * @parameter  
 * @return  
 * @since  
 */
@RestController
@RequestMapping(value = "/wx/pay", produces = "application/json;charset=utf-8")
public class PayController {


    private static final Logger logger = Logger.getLogger(PayController.class);

    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("")
    @ResponseBody

    @RequestMapping(value = "/getDetailByUrl", method = RequestMethod.POST)
    public Map<String,Object> getDetailByUrl() {

            String access_token_url = "https://api.weixin.qq.com/cgi-bin/token?" +
                    "grant_type=client_credential" +
                    "&appid="+
                    "&secret=";
        JSONObject map =null;
            try {
                HttpClient client = HttpClientBuilder.create().build();//构建一个Client
                HttpGet get = new HttpGet(access_token_url.toString());    //构建一个GET请求
                HttpResponse response = client.execute(get);//提交GET请求
                HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
                String content = EntityUtils.toString(result);
                System.out.println("_________"+content);

                map =JSON.parseObject(content);

                System.out.println("_________"+map);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("获取信息失败");
            }

        return map;


        }


    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Period>
     */
    @ApiOperation("通过code")
    @ResponseBody

    @RequestMapping(value = "/getCode", method = RequestMethod.POST)
    public Map<String,Object> getCode( @RequestBody String json) {
      JSONObject jsonObject=JSON.parseObject(json);
      String code=jsonObject.getString("code");
        System.out.println("code_______"+code);



         String access_token_url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
               "appid="+
                "&secret="+
                 "&code="+code+
                 "&grant_type=authorization_code";

        JSONObject map =null;
        try {
            HttpClient client = HttpClientBuilder.create().build();//构建一个Client
            HttpGet get = new HttpGet(access_token_url.toString());    //构建一个GET请求
            HttpResponse response = client.execute(get);//提交GET请求
            HttpEntity result = response.getEntity();//拿到返回的HttpResponse的"实体"
            String content = EntityUtils.toString(result);
            System.out.println("_________"+content);
            map =JSON.parseObject(content);

            System.out.println("_________"+map);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取信息失败");
        }

        return map;


    }




}
