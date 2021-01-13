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
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
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
    public String getBank(@RequestBody String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String image="data:image/png;base64,"+jsonObject.getString("image");
        String host = "https://bankocr.market.alicloudapi.com";
        String path = "/cardbank";
        String method = "POST";
        String appcode = "f4880d954dc54a679b8e70532c1f0bcc";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();

        bodys.put("img",image);

        String bank=null;
        try{
            HttpUtils httpUtils=new HttpUtils();
            HttpResponse response = httpUtils.doPost(host, path, method, headers, querys, bodys);
            bank= EntityUtils.toString(response.getEntity());
            System.out.println(bank);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return  bank;

    }


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
    public String outExcel(HttpServletResponse response, String somnus,String agent,String grade) throws IOException, IllegalAccessException {
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