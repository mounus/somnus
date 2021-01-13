package com.example.message;

import java.net.URLEncoder;
import java.util.HashMap;

public class SMSSendDemo {

    public static void main(String[] args) {
        String address = "http://apis.xntdx.com";//远程地址
        //String address = "http://apis.xntdx.com/SMS/Send";

        int port = 80;//远程端口
        String account = "32833806136845E598E473EE105B3069";//账户
        String token = "12c2909e1caa43cc8489cde8293c4a84";//token
        String mobile = "15972065654";//发送手机号
        String body = "【好悦享月嫂】您的验证码为：@，如非本人操作，请忽略。";//短信内容
        short responseType = 0;//响应类型  0 json类型，1 xml类型
        String extno = "";//扩展号 有就填写，没有就不填写
        KXTSmsSDK kxtsms = new KXTSmsSDK();
        kxtsms.init(address, port, account, token);
        try
        {
            body = URLEncoder.encode(body,"UTF-8");//URL编码 UTF-8方式
        }
        catch (Exception e) {

        }
        String result = kxtsms.send(mobile,body,responseType,extno);
        HashMap<String, Object> hashMap = null;
        if(responseType == 0)
        {
            //json
            hashMap = CommonUtils.jsonToMap(result);
        }
        if(responseType == 1)
        {
            //xml
            hashMap = CommonUtils.xmlToMap(result);
        }
        if(hashMap != null)
        {
            //写自己的业务逻辑代码
            //hashMap.get("Code");
        }
    }

}