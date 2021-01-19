package com.example.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.entiy.Contact;
import com.example.entiy.excel.ExcelJSON;
import com.example.service.ContactServie;
import com.example.util.ExcelUtil;
import com.example.util.HttpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/wx/contact",produces="application/json;charset=utf-8")
@Api(tags="月嫂联系管理")
public class ContactController {

    @Autowired(required = false)
    private ContactServie contactServie;

    /**
     * save:(月嫂联系信息新增). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("添加月嫂联系")
    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public int save(@RequestBody Contact contact) {
        int states = this.contactServie.save(contact);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * uodate:(修改月嫂联系表). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("查询id")
    @RequestMapping(value ="/update", method = RequestMethod.PUT)
    public int update(@RequestBody  Contact contact){

        int states = this.contactServie.update(contact);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    /**
     * :(获取银行卡号信息). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("获取银行卡信息")
    @RequestMapping(value ="/getBank", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "image", value = "base64", required = false, dataType = "String", paramType = "query"),
    })
    public String getBank(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String host = "https://bankocr.market.alicloudapi.com";// 【1】请求地址 支持http 和 https 及 WEBSOCKET
        String path = "/cardbank"; // 【2】后缀
        String appcode = "c5af306689d544969d8e217da82d7f34"; // 【3】开通服务后 买家中心-查看AppCode

     String body="img="+jsonObject.getString("image");
        // String body = "img=http://img3.fegine.com/image/bank.jpg"; // 【4】请求参数，详见文档描述
        // 或者base64
        // String body = "img=data:image/jpeg;base64,/9j/4A......";
        String urlSend = host + path; // 【5】拼接请求链接
        String bank=null;
        System.out.println("body = " + body);
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
                bank  = read(httpURLCon.getInputStream());
                System.out.println("正常请求计费(其他均不计费)");
                System.out.println("获取返回的bank:");
                System.out.print(bank);
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
        System.out.println("bank = " + bank);
        return bank;

    }
    /*
     * 读取返回结果
     */
    private static String read(InputStream is) throws IOException {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        while ((line = br.readLine()) != null) {
            line = new String(line.getBytes(), "utf-8");
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

//    /**
//     * :(获取银行卡号信息). <br/>
//     * * @author Somnus
//     *     @return
//     */
//    @ApiOperation("获取银行卡信息")
//    @RequestMapping(value ="/getBank", method = RequestMethod.POST)
//    public String getBank(@RequestBody String json) {
//        JSONObject jsonObject = JSON.parseObject(json);//转换类型
//        String image=jsonObject.getString("image");
//     //   String image="data:image/png;base64,"+jsonObject.getString("image");
//        String host = "https://bankocr.market.alicloudapi.com";
//        String path = "/cardbank";
//        String method = "POST";
//        String appcode = "f4880d954dc54a679b8e70532c1f0bcc";
//        Map<String, String> headers = new HashMap<String, String>();
//        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
//        headers.put("Authorization", "APPCODE " + appcode);
//        //根据API的要求，定义相对应的Content-Type
//        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        Map<String, String> querys = new HashMap<String, String>();
//        Map<String, String> bodys = new HashMap<String, String>();
//
//        bodys.put("img",image);
//
//        String bank=null;
//        try{
//            HttpUtils httpUtils=new HttpUtils();
//            HttpResponse response = httpUtils.doPost(host, path, method, headers, querys, bodys);
//            bank= EntityUtils.toString(response.getEntity());
//            System.out.println(bank);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("bank = " + bank);
//        return  bank;
//
//    }


    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *     @return
     */
    @ApiOperation("导出数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【" +
                    "all-全部，" +
                    "byGrade-待定级," +
                    "ding-已定级," +
                    "allGrade-待晋级," +
                    "】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "agent", value = "经理人", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "grade", value = "等级", required = false,  paramType = "query"),
    })
    @GetMapping(value = "/outExcel")
    public String outExcel(HttpServletResponse response, String somnus, String agent, String grade) throws IOException, IllegalAccessException {
        //文件名
        String fileName = "月嫂情况一览表";
        //sheet名
        String sheetName = "月嫂情况sheet";
        //JSONObject jsonObject = JSON.parseObject(somnus,agent,grade);

        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();
        titleList.add("姓名");
        titleList.add("工号");
        titleList.add("电话");
        titleList.add("等级");
        titleList.add("服务天数");
        titleList.add("经理人");
        titleList.add("创建时间");
        titleList.add("登记人");

        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Integer> listmid = new ArrayList<Integer>();
               listmid=contactServie.outExcel(somnus,agent,grade);

        List<ExcelJSON> excelJSONList = new ArrayList<>();


        for (int i = 0; i < listmid.size(); i++) {
            ExcelJSON excelJSON = new ExcelJSON();
            List<Map<String, Object>> mp = contactServie.outExcelById(listmid.get(i));

            excelJSON.setName(String.valueOf(mp.get(0).get("name")));
            excelJSON.setNumber(String.valueOf(mp.get(0).get("number")));
            excelJSON.setPhone(String.valueOf(mp.get(0).get("phone")));
            excelJSON.setGrade(String.valueOf(mp.get(0).get("grade")));
            excelJSON.setTrueday(String.valueOf(mp.get(0).get("trueday")));
            excelJSON.setAgent(String.valueOf(mp.get(0).get("agent")));
            excelJSON.setCreattime(String.valueOf(mp.get(0).get("creattime")));
            excelJSON.setLogin(String.valueOf(mp.get(0).get("login")));
            excelJSONList.add(excelJSON);
        }

        //调取封装的方法，传入相应的参数
        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response
                .setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;




    }
}