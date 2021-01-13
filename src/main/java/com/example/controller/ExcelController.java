package com.example.controller;

import com.example.entiy.Reservation;
import com.example.entiy.excel.*;
import com.example.service.ExcelServie;
import com.example.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.ELState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.OssUtil.getOssUrl;

@RestController
@RequestMapping(value = "/wx/excel", produces = "application/json;charset=utf-8")
@Api(tags = "导出表格管理")
public class ExcelController {

    @Autowired(required = false)
    private ExcelServie excelServie;

    @ApiOperation("导出黑名单，上架月嫂情况情况一览表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "条件【black-黑名单，shelf-上架,byGrade-待定级，ding-已定级，待晋级-allGrade】", required = false, dataType = "String", paramType = "query"),
    })
    @GetMapping(value = "/out_matorn")
    public String out_matorn(HttpServletResponse response, String somnus, String agent) throws IOException, IllegalAccessException {
        //文件名
        String fileName = null;
        //sheet名
        String sheetName = "月嫂情况sheet";
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());

        if (agent == null || agent == "" || agent.isEmpty()) {

        } else {
            fileName = agent;
        }
        if (somnus.equals("all")) {
            fileName = fileName + "全部月嫂情况一览表" + nowtime;
        }
        if (somnus.equals("byGrade")) {
            fileName = fileName + "待定级月嫂情况一览表" + nowtime;
        }
        if (somnus.equals("allGrade")) {
            fileName = fileName + "待晋级月嫂情况一览表" + nowtime;
        }
        if (somnus.equals("black")) {
            fileName = fileName + "黑名单月嫂情况一览表" + nowtime;
        }
        if (somnus.equals("shelf")) {
            fileName = fileName + "上架月嫂情况一览表" + nowtime;
        }
        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();
        titleList.add("工号");
        titleList.add("姓名");
        titleList.add("手机号");
        titleList.add("经理人");
        titleList.add("等级");
        titleList.add("身份证");
        titleList.add("银行卡卡号");
        titleList.add("银行卡开户行");
        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到

        List<MatornExcel> matornExcelList = new ArrayList<MatornExcel>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        list = excelServie.out_matorn(somnus, agent);
        for (int i = 0; i < list.size(); i++) {
            MatornExcel matornExcel = new MatornExcel();
            matornExcel.setNumber(String.valueOf(list.get(i).get("number")));
            matornExcel.setName(String.valueOf(list.get(i).get("name")));
            matornExcel.setPhone(String.valueOf(list.get(i).get("phone")));
            matornExcel.setAgent(String.valueOf(list.get(i).get("agent")));
            matornExcel.setGrade(String.valueOf(list.get(i).get("grade")));
            matornExcel.setIdcard(String.valueOf(list.get(i).get("idcard")));
            matornExcel.setBank_card(String.valueOf(list.get(i).get("bank_card")));
            matornExcel.setBank_name(String.valueOf(list.get(i).get("bank_name")));

            matornExcelList.add(matornExcel);

        }

        //调取封装的方法，传入相应的参数
        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, matornExcelList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;
    }


    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("导出所有预约客户信息")
    @GetMapping(value = "/outExcelReservation")
    public String outExcelReservation(HttpServletResponse response, Integer uid) throws IOException, IllegalAccessException {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        //文件名
        String fileName = "渠道客户预约信息表" + nowtime;
        //sheet名
        String sheetName = "预约情况sheet";

        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();

        titleList.add("序号");
        titleList.add("客户名");
        titleList.add("手机号");
        titleList.add("生产情况");
        titleList.add("生产日期");
        titleList.add("渠道来源");
        titleList.add("医院来源");
        titleList.add("预约时间");

        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Reservation> list = new ArrayList<Reservation>();
        list = excelServie.outExcelReservation(uid);

        List<ExcelReservation> excelJSONList = new ArrayList<ExcelReservation>();

        for (int i = 0; i < list.size(); i++) {
            ExcelReservation excelJSON = new ExcelReservation();
            excelJSON.setId(String.valueOf(list.get(i).getId()));
            excelJSON.setName(String.valueOf(list.get(i).getName()));
            excelJSON.setPhone(String.valueOf(list.get(i).getPhone()));

            if (list.get(i).getProduction_situation() == null) {
                excelJSON.setProduction_situation("未知");
            } else {
                if (list.get(i).getProduction_situation() == 0) {
                    excelJSON.setProduction_situation("未生产");
                } else {
                    excelJSON.setProduction_situation("生产");
                }
            }
            excelJSON.setProduction_date(String.valueOf(list.get(i).getProduction_date()));
            excelJSON.setOrigin(String.valueOf(list.get(i).getOrigin()));
            excelJSON.setHospital(String.valueOf(list.get(i).getHospital()));
            excelJSON.setTime(String.valueOf(list.get(i).getTime()));
            excelJSONList.add(excelJSON);
        }
        //调取封装的方法，传入相应的参数

        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;

    }

    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("选择条件/字段导出客户数据")
    @GetMapping(value = "/outExcelCustom")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "登录id", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "origin", value = "客户来源", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "origin_channel", value = "客户渠道", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "intention", value = "意向类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "sname", value = "咨询顾问", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bname", value = "母婴顾问", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "orderSpeed", value = "订单进度", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "service_type", value = "服务类型1-【月子会所服务，居家服务】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "timetype", value = "服务类型2- 月子会所服务【月子会所服务，全程，医院服务】 居家服务【U1,U2,U3,U4,9800】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "level", value = "服务类型3-【U1,U2,U3,U4,9800】", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "returnType", value = "回岗类型", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "number", value = "客户手机号，姓名，编号", required = false, dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "powerFiled", value = "权限字段", required = false, dataType = "String", paramType = "query"),
    })
    public String outExcelCustom(HttpServletResponse response, Integer uid, String origin, String origin_channel, String intention, Integer sname, Integer bname, String orderSpeed,
                                 String service_type, String timetype, String level, String returnType, String number, String powerFiled, String start_time, String end_time, String first_time, String last_time) throws IOException, IllegalAccessException, ClassNotFoundException {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());

        List<Map<String, Object>> powerFiledList = new ArrayList<>();

        powerFiled = powerFiled.replace("[", "{");
        powerFiled = powerFiled.replace("]", "}");
        JSONArray jsonArray = JSONArray.fromObject("[" + powerFiled + "]");
        List<Map<String, Object>> powerFiledList1 = new ArrayList<>();
        powerFiledList1 = (List) jsonArray;
        Map<String, Object> powerFiledMap = new LinkedHashMap();//保证put值有序
        for (int i = 0; i < powerFiledList1.size(); i++) {
            for (String key : powerFiledList1.get(i).keySet()) {
                Integer value = Integer.valueOf(powerFiledList1.get(i).get(key).toString());
                powerFiledMap.put(key, value);
            }
        }
        powerFiledList.add(powerFiledMap);
        System.out.println("powerFiledMap = " + powerFiledMap);

        //文件名
        String fileName = "客户搜索条件/字段信息表" + nowtime;
        //sheet名
        String sheetName = "情况sheet";

        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();

                titleList.add("客户id");
                titleList.add("客户姓名");
                titleList.add("客户生产时间");
                titleList.add("客户手机");
                titleList.add("微信号");

                titleList.add("来源");
                titleList.add("居住区域");
                titleList.add("创建时间");
                titleList.add("意向类型");
                titleList.add("跟进次数");

                titleList.add("咨询顾问");
                titleList.add("母婴顾问");
                titleList.add("订单进度");
                titleList.add("订单类型");
                titleList.add("订单天数");

                titleList.add("服务人次");
                titleList.add("回岗类型");
                titleList.add("满意度");
                titleList.add("订单金额");
                titleList.add("服务天数");

                titleList.add("实收金额");


        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Map<String, Object>> list = new ArrayList<>();

        list = excelServie.outFiledCustom(uid, origin, origin_channel, intention, sname, bname, orderSpeed, service_type, timetype, level, returnType, number, powerFiled, start_time, end_time);
        List<ExcelCustom> excelJSONList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            ExcelCustom excelCustom = new ExcelCustom();
            if (powerFiledMap.get("cid") != null) {
                if (powerFiledMap.get("cid").toString().equals("1")) {
                    excelCustom.setCid(list.get(i).get("cid").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("c_name") != null) {
                if (powerFiledMap.get("c_name").toString().equals("1")) {
                    if (list.get(i).get("c_name")!=null){
                        excelCustom.setC_name(list.get(i).get("c_name").toString());
                    }else {

                    }

                } else {

                }
            } else {

            }
            if (powerFiledMap.get("start") != null) {
                if (powerFiledMap.get("start").toString().equals("1")) {
                    if (list.get(i).get("start")!=null){
                        excelCustom.setStart(list.get(i).get("start").toString());
                    }else {

                    }

                } else {

                }
            } else {

            }
            if (powerFiledMap.get("c_phone") != null) {
                if (powerFiledMap.get("c_phone").toString().equals("1")) {
                    excelCustom.setC_phone(list.get(i).get("c_phone").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("w_name") != null) {
                if (powerFiledMap.get("w_name").toString().equals("1")) {
                    excelCustom.setW_name(list.get(i).get("w_name").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("origin") != null) {
                if (powerFiledMap.get("origin").toString().equals("1")) {
                    excelCustom.setOrigin(list.get(i).get("origin").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("address") != null) {
                if (powerFiledMap.get("address").toString().equals("1")) {
                    excelCustom.setAddress(list.get(i).get("address").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("creat_time") != null) {
                if (powerFiledMap.get("creat_time").toString().equals("1")) {
                    excelCustom.setCreat_time(list.get(i).get("creat_time").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("intention") != null) {
                if (powerFiledMap.get("intention").toString().equals("1")) {
                    excelCustom.setIntention(list.get(i).get("intention").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("followCount") != null) {
                if (powerFiledMap.get("followCount").toString().equals("1")) {
                    excelCustom.setFollowCount(list.get(i).get("followCount").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("sname") != null) {
                if (powerFiledMap.get("sname").toString().equals("1")) {
                    excelCustom.setSname(list.get(i).get("sname").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("bname") != null) {
                if (powerFiledMap.get("bname").toString().equals("1")) {
                    excelCustom.setBname(list.get(i).get("bname").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("orderSpeed") != null) {
                if (powerFiledMap.get("orderSpeed").toString().equals("1")) {
                    excelCustom.setOrderSpeed(list.get(i).get("orderSpeed").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("orderType") != null) {
                if (powerFiledMap.get("orderType").toString().equals("1")) {
                    excelCustom.setOrderType(list.get(i).get("orderType").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("service_day") != null) {
                if (powerFiledMap.get("service_day").toString().equals("1")) {
                    excelCustom.setService_day(list.get(i).get("service_day").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("service_count") != null) {
                if (powerFiledMap.get("service_count").toString().equals("1")) {
                    excelCustom.setService_count(list.get(i).get("service_count").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("returnType") != null) {
                if (powerFiledMap.get("returnType").toString().equals("1")) {
                    excelCustom.setReturnType(list.get(i).get("returnType").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("satisfied") != null) {
                if (powerFiledMap.get("satisfied").toString().equals("1")) {
                    excelCustom.setSatisfied(list.get(i).get("satisfied").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("price") != null) {
                if (powerFiledMap.get("price").toString().equals("1")) {
                    excelCustom.setPrice(list.get(i).get("price").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("orderDay") != null) {
                if (powerFiledMap.get("orderDay").toString().equals("1")) {
                    excelCustom.setOrderDay(list.get(i).get("orderDay").toString());
                } else {

                }
            } else {

            }
            if (powerFiledMap.get("truePrice") != null) {
                if (powerFiledMap.get("truePrice").toString().equals("1")) {
                    excelCustom.setTruePrice(list.get(i).get("truePrice").toString());
                } else {

                }
            } else {

            }

//            if (powerFiledMap.get("cid") != null) {
//                if (powerFiledMap.get("cid").toString()) ==1){
//                    excelCustom.setCid(list.get(i).get("cid").toString());
//                } else{
//                    excelCustom.setCid("未知");
//                }
//            } else {
//
//            }
//
//            if (Integer.valueOf(powerFiledMap.get("c_name").toString()) == 1) {
//                excelCustom.setC_name(list.get(i).get("c_name").toString());
//            } else {
//                excelCustom.setC_name("未知");
//            }
//            if (Integer.valueOf(powerFiledMap.get("start").toString()) == 1) {
//                if (list.get(i).get("start") != null) {
//                    excelCustom.setStart(list.get(i).get("start").toString());
//                } else {
//                    excelCustom.setStart("未知");
//                }
//
//            } else {
//                excelCustom.setStart("未知");
//            }
//            if (Integer.valueOf(powerFiledMap.get("c_phone").toString()) == 1) {
//                excelCustom.setC_phone(list.get(i).get("c_phone").toString());
//            } else {
//                excelCustom.setC_phone("未知");
//            }
//
//            if (Integer.valueOf(powerFiledMap.get("w_name").toString()) == 1) {
//                excelCustom.setW_name(list.get(i).get("w_name").toString());
//            } else {
//                excelCustom.setW_name("未知");
//            }
//            if (Integer.valueOf(powerFiledMap.get("origin").toString()) == 1) {
//                excelCustom.setOrigin(list.get(i).get("origin").toString());
//            } else {
//                excelCustom.setOrigin("未知");
//            }
//            if (Integer.valueOf(powerFiledMap.get("address").toString()) == 1) {
//                excelCustom.setAddress(list.get(i).get("address").toString());
//            } else {
//                excelCustom.setAddress("未知");
//            }
//
//            if (Integer.valueOf(powerFiledMap.get("creat_time").toString()) == 1) {
//                excelCustom.setCreat_time(list.get(i).get("creat_time").toString());
//            } else {
//                excelCustom.setCreat_time("未知");
//            }
//            if (Integer.valueOf(powerFiledMap.get("intention").toString()) == 1) {
//                excelCustom.setIntention(list.get(i).get("intention").toString());
//            } else {
//                excelCustom.setIntention("未知");
//            }
//            if (Integer.valueOf(powerFiledMap.get("followCount").toString()) == 1) {
//                excelCustom.setFollowCount(list.get(i).get("followCount").toString());
//            } else {
//                excelCustom.setFollowCount("未知");
//            }
//            if (powerFiledMap.get("sname") != null) {
//                if (Integer.valueOf(powerFiledMap.get("sname").toString()) == 1) {
//                    excelCustom.setSname(list.get(i).get("sname").toString());
//                } else {
//                    excelCustom.setSname("未知");
//                }
//            } else {
//                excelCustom.setSname("未知");
//            }
//            if (powerFiledMap.get("bname") != null) {
//                if (Integer.valueOf(powerFiledMap.get("bname").toString()) == 1) {
//                    excelCustom.setBname(list.get(i).get("bname").toString());
//                } else {
//                    excelCustom.setBname("未知");
//                }
//            } else {
//                excelCustom.setBname("未知");
//            }
//            if (Integer.valueOf(powerFiledMap.get("orderSpeed").toString()) == 1) {
//                excelCustom.setOrderSpeed(list.get(i).get("orderSpeed").toString());
//            } else {
//                excelCustom.setOrderSpeed("未知");
//            }
//            if (Integer.valueOf(powerFiledMap.get("orderType").toString()) == 1) {
//                excelCustom.setOrderType(list.get(i).get("orderType").toString());
//            } else {
//                excelCustom.setOrderType("未知");
//            }
//            if (powerFiledMap.get("service_day") != null) {
//                if (Integer.valueOf(powerFiledMap.get("service_day").toString()) == 1) {
//                    excelCustom.setService_day(list.get(i).get("service_day").toString());
//                } else {
//                    excelCustom.setService_day("未知");
//                }
//            } else {
//                excelCustom.setService_day("未知");
//            }
//            if (powerFiledMap.get("orderDay") != null) {
//                if (Integer.valueOf(powerFiledMap.get("orderDay").toString()) == 1) {
//                    excelCustom.setOrderDay(list.get(i).get("orderDay").toString());
//                } else {
//                    excelCustom.setOrderDay("未知");
//                }
//            } else {
//                excelCustom.setOrderDay("未知");
//            }
//
//            if (powerFiledMap.get("service_count") != null) {
//                if (Integer.valueOf(powerFiledMap.get("service_count").toString()) == 1) {
//                    excelCustom.setService_count(list.get(i).get("service_count").toString());
//                } else {
//                    excelCustom.setService_count("未知");
//                }
//            } else {
//                excelCustom.setService_count("未知");
//            }
//            if (powerFiledMap.get("returnType") != null) {
//                if (Integer.valueOf(powerFiledMap.get("returnType").toString()) == 1) {
//                    excelCustom.setReturnType(list.get(i).get("returnType").toString());
//                } else {
//                    excelCustom.setReturnType("未知");
//                }
//            } else {
//                excelCustom.setReturnType("未知");
//            }
//            if (powerFiledMap.get("satisfied") != null) {
//                if (Integer.valueOf(powerFiledMap.get("satisfied").toString()) == 1) {
//                    excelCustom.setSatisfied(list.get(i).get("satisfied").toString());
//                } else {
//                    excelCustom.setReturnType("未知");
//                }
//            } else {
//                excelCustom.setReturnType("未知");
//            }
//            if (powerFiledMap.get("price") != null) {
//                if (Integer.valueOf(powerFiledMap.get("price").toString()) == 1) {
//                    excelCustom.setPrice(list.get(i).get("price").toString());
//                } else {
//                    excelCustom.setPrice("未知");
//                }
//            } else {
//                excelCustom.setPrice("未知");
//            }
//            if (powerFiledMap.get("truePrice") != null) {
//                if (Integer.valueOf(powerFiledMap.get("truePrice").toString()) == 1) {
//                    excelCustom.setTruePrice(list.get(i).get("truePrice").toString());
//                } else {
//                    excelCustom.setTruePrice("未知");
//                }
//            } else {
//                excelCustom.setTruePrice("未知");
//            }


            excelJSONList.add(excelCustom);
        }


        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;

    }

    @ResponseBody
    @ApiOperation("上传pdf报告）")
    @RequestMapping(value = "/pdf", method = RequestMethod.POST)
    public int pdf() {
        String path = "C:\\Users\\MLOONG\\Desktop\\体检报告数据"; // 路径
        File f = new File(path);//获取路径  F:\测试目录
        if (!f.exists()) {
            System.out.println(path + " not exists");//不存在就输出
            return 0;
        }

        File fa[] = f.listFiles();//用数组接收  F:\笔记总结\C#, F:\笔记总结\if语句.txt
        for (int i = 0; i < fa.length; i++) {//循环遍历
            File fs = fa[i];//获取数组中的第i个
            if (fs.isDirectory()) {
                System.out.println(fs.getName() + " [目录]");//如果是目录就输出
            } else {
                System.out.println("_________" + fs.getName());//否则直接输出

                Integer mid = Integer.valueOf(fs.getName().substring(0, fs.getName().indexOf(".")));
                String filePath = path + "\\" + fs.getName();
                String url = getOssUrl(filePath, fs.getName());
                System.out.println("url = " + url);
                System.out.println("mid = " + mid);
                int states = excelServie.addPDf(mid, url);
                System.out.println("states = " + states);

            }
        }

        return 0;
    }

    @ResponseBody
    @ApiOperation("导入问题")
    @RequestMapping(value = "/addQustion", method = RequestMethod.POST)
    public int addQustion() {

        String excelPath = "C:\\Users\\MLOONG\\Desktop\\7月7日定级_1.xls";
        List<String> list = null;
        try {
            //String encoding = "GBK";
            File excel = new File(excelPath);
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在

                String[] split = excel.getName().split("\\.");  //.是特殊字符，需要转义！！！！！
                Workbook wb = null;
                //根据文件后缀（xls/xlsx）进行判断
                if ("xls".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);   //文件流对象
                    wb = new HSSFWorkbook(fis);
                } else if ("xlsx".equals(split[1])) {
                    FileInputStream fis = new FileInputStream(excel);
                    wb = new XSSFWorkbook(fis);
                } else {
                    System.out.println("文件类型错误!");
                    return 0;
                }

                //开始解析
                Sheet sheet = wb.getSheetAt(0);     //读取sheet 0

                int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                int lastRowIndex = sheet.getLastRowNum() + 1;

                List<String> nameList = new ArrayList<String>();
                List<String> agentList = new ArrayList<String>();
                List<String> aaList = new ArrayList<String>();
                int i = 0;
                for (int rIndex = firstRowIndex; rIndex < lastRowIndex; rIndex++) {   //遍历行
                    // System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();

                        String questionType = null;
                        String number = null;
                        String dayString = null;

                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null && !"".equals(cell.toString().trim())) {
                                switch (cIndex) {


                                    case 0:
                                        number = cell.toString();
                                        System.out.println("id:" + number);

                                    case 1:
                                        questionType = cell.toString();
                                        break;
                                    case 2:
                                        dayString = cell.toString();
                                        System.out.println("dayString:" + dayString);
                                        break;


                                    default:
                                        break;
                                }

                            }

                        }
                        Integer day = Double.valueOf(dayString).intValue();

                    }
                }

            } else {
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("导出客户服务订单统计表")
    @GetMapping(value = "/outCustomService")
    public String outCustomService(HttpServletResponse response) throws IOException, IllegalAccessException {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        //文件名
        String fileName = "客户服务订单统计表" + nowtime;
        //sheet名
        String sheetName = "订单统计sheet";

        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();

        titleList.add("客户编号");
        titleList.add("客户来源");
        titleList.add("客户姓名");
        titleList.add("客户电话");

        titleList.add("订单编号");
        titleList.add("订单类型");
        titleList.add("订单天数");
        titleList.add("订单状态");
        titleList.add("订单金额");
        titleList.add("订单单价");

        titleList.add("母婴顾问");
        titleList.add("月嫂编号");
        titleList.add("月嫂姓名");
        titleList.add("月嫂来源");
        titleList.add("月嫂经理人");
        titleList.add("服务单价");
        titleList.add("上岗日期");
        titleList.add("回岗日期");
        titleList.add("当月服务天数");
        titleList.add("次月服务天数");
        titleList.add("当月回款");
        titleList.add("次月回款");
        titleList.add("当月工资");
        titleList.add("次月工资");
        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String json = null;
        list = excelServie.outCustomService(json);

        List<ExcelCustomService> excelJSONList = new ArrayList<ExcelCustomService>();

        for (int i = 0; i < list.size(); i++) {
            ExcelCustomService excelJSON = new ExcelCustomService();

            excelJSON.setCid(String.valueOf(list.get(i).get("cid")));
            excelJSON.setC_origin(String.valueOf(list.get(i).get("c_origin")));
            excelJSON.setC_name(String.valueOf(list.get(i).get("c_name")));
            excelJSON.setC_phone(String.valueOf(list.get(i).get("c_phone")));
            excelJSON.setO_number(String.valueOf(list.get(i).get("o_number")));
            excelJSON.setOrder_type(String.valueOf(list.get(i).get("order_type")));
            excelJSON.setService_day(String.valueOf(list.get(i).get("service_day")));
            excelJSON.setOrder_states(String.valueOf(list.get(i).get("order_states")));
            excelJSON.setPrice(String.valueOf(list.get(i).get("price")));
            excelJSON.setOnePrice(String.valueOf(list.get(i).get("onePrice")));
            excelJSON.setBname(String.valueOf(list.get(i).get("bname")));
            excelJSON.setMid(String.valueOf(list.get(i).get("mid")));
            excelJSON.setM_name(String.valueOf(list.get(i).get("m_name")));
            excelJSON.setSource(String.valueOf(list.get(i).get("source")));
            excelJSON.setM_agent(String.valueOf(list.get(i).get("m_agent")));
            excelJSON.setUnitPrice(String.valueOf(list.get(i).get("unitPrice")));
            excelJSON.setArrival_time(String.valueOf(list.get(i).get("arrival_time")));
            excelJSON.setConfirm_time(String.valueOf(list.get(i).get("confirm_time")));
            excelJSON.setNowMonth_day(String.valueOf(list.get(i).get("nowMonth_day")));
            excelJSON.setNextMonth_day(String.valueOf(list.get(i).get("nextMonth_day")));
            excelJSON.setNowMonth_price(String.valueOf(list.get(i).get("nowMonth_price")));
            excelJSON.setNextMonth_price(String.valueOf(list.get(i).get("nextMonth_price")));
            excelJSON.setNowMonth_wages(String.valueOf(list.get(i).get("nowMonth_wages")));
            excelJSON.setNextMonth_wages(String.valueOf(list.get(i).get("nextMonth_wages")));


            excelJSONList.add(excelJSON);
        }
        //调取封装的方法，传入相应的参数

        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;

    }

    /**
     * @return
     * @author Somnus
     */
    @ApiOperation("")
    @ResponseBody
    @RequestMapping(value = "/outService", method = RequestMethod.POST)
    public List<Map<String, Object>> outCustomService(@RequestBody String json) {

        return this.excelServie.outCustomService(json);
    }

    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("导出客户居家服务订单统计表")
    @GetMapping(value = "/outCustomHome")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "【月子会所服务，居家服务】", required = false, paramType = "query"),
            @ApiImplicitParam(name = "monthFirst", value = "搜索开始时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "monthLast", value = "搜索结束时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "bid", value = "母婴顾问 id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "service_type", value = "服务类型1", required = false, paramType = "query"),
            @ApiImplicitParam(name = "timetype", value = "服务类型2", required = false, paramType = "query"),
            @ApiImplicitParam(name = "origin", value = "来源1", required = false, paramType = "query"),
            @ApiImplicitParam(name = "channel", value = "来源2", required = false, paramType = "query"),
            @ApiImplicitParam(name = "work_states", value = "订单进度", required = false, paramType = "query"),
            @ApiImplicitParam(name = "number", value = "姓名、手机号、id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, paramType = "query"),

    })
    public String outCustomHome(HttpServletResponse response, String somnus, String monthFirst, String monthLast, Integer bid, String service_type, String timetype, String origin, String channel, Integer work_states,
                                String number, Integer start) throws IOException, IllegalAccessException {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        //文件名
        String fileName = "客户居家服务订单统计表" + nowtime;
        //sheet名
        String sheetName = "订单统计sheet";

        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();

        titleList.add("订单编号");
        titleList.add("客户姓名");
        titleList.add("母婴顾问");
        titleList.add("客户来源");
        titleList.add("订单类型");

        titleList.add("订单进度");
        titleList.add("开始时间");
        titleList.add("订单天数");
        titleList.add("结束时间");
        titleList.add("订单金额");

        titleList.add("服务人次");
        titleList.add("服务天数");
        titleList.add("实收金额");
        titleList.add("订单创建时间");

        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        list = excelServie.outCustomHome(somnus, monthFirst, monthLast, bid, service_type, timetype, origin, channel, work_states, number, start);

        List<ExcelCustomHome> excelJSONList = new ArrayList<ExcelCustomHome>();

        for (int i = 0; i < list.size(); i++) {
            ExcelCustomHome excelJSON = new ExcelCustomHome();

            excelJSON.setCid(String.valueOf(list.get(i).get("did")));
            excelJSON.setC_name(String.valueOf(list.get(i).get("c_name")));
            excelJSON.setBname(String.valueOf(list.get(i).get("bname")));
            excelJSON.setC_origin(String.valueOf(list.get(i).get("c_origin")));
            excelJSON.setOrder_type(String.valueOf(list.get(i).get("order_type")));

            excelJSON.setOrder_speed(String.valueOf(list.get(i).get("order_speed")));
            excelJSON.setStart_time(String.valueOf(list.get(i).get("start_time")));
            excelJSON.setService_day(String.valueOf(list.get(i).get("service_day")));
            excelJSON.setEnd_time(String.valueOf(list.get(i).get("end_time")));
            excelJSON.setPrice(String.valueOf(list.get(i).get("price")));

            excelJSON.setService_count(String.valueOf(list.get(i).get("service_count")));
            excelJSON.setOrderDay(String.valueOf(list.get(i).get("orderDay")));
            excelJSON.setTruePrice(String.valueOf(list.get(i).get("truePrice")));
            excelJSON.setCreat_time(String.valueOf(list.get(i).get("creat_time")));


            excelJSONList.add(excelJSON);
        }
        //调取封装的方法，传入相应的参数

        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;

    }

    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("导出客户月子会所服务订单统计表")
    @GetMapping(value = "/outCustomClub")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "somnus", value = "【月子会所服务，居家服务】", required = false, paramType = "query"),
            @ApiImplicitParam(name = "monthFirst", value = "搜索开始时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "monthLast", value = "搜索结束时间", required = false, paramType = "query"),
            @ApiImplicitParam(name = "bid", value = "母婴顾问 id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "service_type", value = "服务类型1", required = false, paramType = "query"),
            @ApiImplicitParam(name = "timetype", value = "服务类型2", required = false, paramType = "query"),
            @ApiImplicitParam(name = "origin", value = "来源1", required = false, paramType = "query"),
            @ApiImplicitParam(name = "channel", value = "来源2", required = false, paramType = "query"),
            @ApiImplicitParam(name = "work_states", value = "订单进度", required = false, paramType = "query"),
            @ApiImplicitParam(name = "number", value = "姓名、手机号、id", required = false, paramType = "query"),
            @ApiImplicitParam(name = "start", value = "当前页数", required = false, paramType = "query"),

    })
    public String outCustomClub(HttpServletResponse response, String somnus, String monthFirst, String monthLast, Integer bid, String service_type, String timetype, String origin, String channel, Integer work_states,
                                String number, Integer start) throws IOException, IllegalAccessException {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        //文件名
        String fileName = "客户月子会所服务订单统计表" + nowtime;
        //sheet名
        String sheetName = "订单统计sheet";

        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();

        titleList.add("订单编号");
        titleList.add("客户姓名");
        titleList.add("母婴顾问");
        titleList.add("客户来源");
        titleList.add("订单类型");

        titleList.add("订单进度");
        titleList.add("开始时间");
        titleList.add("订单天数");
        titleList.add("结束时间");
        titleList.add("订单金额");

        titleList.add("服务人次");
        titleList.add("医院天数");
        titleList.add("会所天数");
        titleList.add("医院实收金额");
        titleList.add("会所实收金额");

        titleList.add("订单创建时间");

        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        list = excelServie.outCustomClub(somnus, monthFirst, monthLast, bid, service_type, timetype, origin, channel, work_states, number, start);

        List<ExcelCustomClub> excelJSONList = new ArrayList<ExcelCustomClub>();

        for (int i = 0; i < list.size(); i++) {
            ExcelCustomClub excelJSON = new ExcelCustomClub();

            excelJSON.setCid(String.valueOf(list.get(i).get("did")));
            excelJSON.setC_name(String.valueOf(list.get(i).get("c_name")));
            excelJSON.setBname(String.valueOf(list.get(i).get("bname")));
            excelJSON.setC_origin(String.valueOf(list.get(i).get("c_origin")));
            excelJSON.setOrder_type(String.valueOf(list.get(i).get("order_type")));

            excelJSON.setOrder_speed(String.valueOf(list.get(i).get("order_speed")));
            excelJSON.setStart_time(String.valueOf(list.get(i).get("start_time")));
            excelJSON.setService_day(String.valueOf(list.get(i).get("service_day")));
            excelJSON.setEnd_time(String.valueOf(list.get(i).get("end_time")));
            excelJSON.setPrice(String.valueOf(list.get(i).get("price")));

            excelJSON.setService_count(String.valueOf(list.get(i).get("service_count")));
            excelJSON.setHospitalDay(String.valueOf(list.get(i).get("hospitalDay")));
            excelJSON.setClubDay(String.valueOf(list.get(i).get("clubDay")));
            excelJSON.setTrueHospitalPrice(String.valueOf(list.get(i).get("trueHospitalPrice")));
            excelJSON.setTrueClubPrice(String.valueOf(list.get(i).get("trueClubPrice")));

            excelJSON.setCreat_time(String.valueOf(list.get(i).get("creat_time")));

            excelJSONList.add(excelJSON);
        }
        //调取封装的方法，传入相应的参数

        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;

    }

    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("导出客户服务订单统计表(admin)")
    @GetMapping(value = "/outCustomInfo")
    public String outCustomInfo(HttpServletResponse response) throws IOException, IllegalAccessException {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        //文件名
        String fileName = "客户信息计表" + nowtime;
        //sheet名
        String sheetName = "订单统计sheet";

        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();

        titleList.add("客户编号");
        titleList.add("客户姓名");
        titleList.add("生产情况");
        titleList.add("生产日期");
        titleList.add("居住区域");

        titleList.add("手机号码");
        titleList.add("微信号码");
        titleList.add("客户来源");
        titleList.add("咨询顾问");
        titleList.add("客户意向");

        titleList.add("跟进次数");
        titleList.add("当前进度");
        titleList.add("母婴顾问");
        titleList.add("订单创建时间");

        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        String json = null;
        list = excelServie.outCustomInfo(json);

        List<ExcelCustomInfo> excelJSONList = new ArrayList<ExcelCustomInfo>();

        for (int i = 0; i < list.size(); i++) {
            ExcelCustomInfo excelJSON = new ExcelCustomInfo();
            excelJSON.setCid(String.valueOf(list.get(i).get("cid")));
            excelJSON.setC_name(String.valueOf(list.get(i).get("c_name")));
            excelJSON.setProduction_situation(String.valueOf(list.get(i).get("production_situation")));
            excelJSON.setProduction_time(String.valueOf(list.get(i).get("production_time")));
            excelJSON.setAddress(String.valueOf(list.get(i).get("address")));

            excelJSON.setC_phone(String.valueOf(list.get(i).get("c_phone")));
            excelJSON.setWeChat(String.valueOf(list.get(i).get("WeChat")));
            excelJSON.setC_origin(String.valueOf(list.get(i).get("c_origin")));
            excelJSON.setSname(String.valueOf(list.get(i).get("sname")));
            excelJSON.setIntention(String.valueOf(list.get(i).get("intention")));

            excelJSON.setFollowCount(String.valueOf(list.get(i).get("followCount")));
            excelJSON.setOrder_speed(String.valueOf(list.get(i).get("order_speed")));
            excelJSON.setBname(String.valueOf(list.get(i).get("bname")));
            excelJSON.setCreat_time(String.valueOf(list.get(i).get("creat_time")));


            excelJSONList.add(excelJSON);
        }
        //调取封装的方法，传入相应的参数

        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;

    }

    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("导出客户字段模板")
    @GetMapping(value = "/outCustomTemplate")
    public String outCustomTemplate(HttpServletResponse response) throws IOException, IllegalAccessException {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd HH:mm:ss");//设置日期格式
        String nowtime = time.format(new Date());
        //文件名
        String fileName = "客户字段模板表" + nowtime;
        //sheet名
        String sheetName = "订单统计sheet";

        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();

        //  titleList.add("客户编号");
        titleList.add("客户姓名");
        titleList.add("客户来源");
        titleList.add("客户渠道");
        titleList.add("生产日期");

        titleList.add("居住区域");
        titleList.add("手机号码");
        titleList.add("微信号码");
        titleList.add("咨询顾问");
        titleList.add("母婴顾问");


        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        List<ExcelCustomTemplate> excelJSONList = new ArrayList<ExcelCustomTemplate>();
        //调取封装的方法，传入相应的参数
        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;

    }

    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("月嫂管理列表导出")
    @GetMapping(value = "/outNewMatorn")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "登录人id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "zodiac", value = "月嫂属相", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "grade", value = "月嫂等级", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "shelf", value = "月嫂状态【0-为空，1-黑名单，2-未上架，3-已上架，4-服务中】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "bid", value = "月嫂经理人id 【0-没有，经理人id】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "source", value = "月嫂来源1", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "origin", value = "月嫂来源2", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "isPrice", value = "押金质保金状态【0-为空，1-待激活，2-已交押金，3-已退押金，4-已交质保金，5-已退质保金】", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "powerFiled", value = "权限字段", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "number", value = "月嫂姓名工号id", required = false,  paramType = "query"),
    })
    public String outNewMatorn(HttpServletResponse response,Integer uid,String zodiac,String grade,Integer shelf,Integer bid,String source,String origin,
                              Integer isPrice,String powerFiled,String number) throws IOException, IllegalAccessException {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        //文件名
        String fileName = "月嫂统计表" + nowtime;
        //sheet名
        String sheetName = "订单统计sheet";




        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();

        titleList.add("工装照");
        titleList.add("工号");
        titleList.add("姓名");
        titleList.add("身份证");
        titleList.add("年龄");

        titleList.add("属相");
        titleList.add("户籍");
        titleList.add("手机");
        titleList.add("银行卡号");
        titleList.add("开户行");

        titleList.add("等级");
        titleList.add("服务天数");
        titleList.add("服务单数");
        titleList.add("服务状态");
        titleList.add("经理人");

        titleList.add("来源");
        titleList.add("押金质保金状态");
        titleList.add("登记人");

        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list=excelServie.outNewMatorn(uid, zodiac, grade, shelf, bid, source, origin, isPrice, powerFiled, number);
        System.out.println("list = " + list);
        List<ExcelNewMatorn> excelJSONList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ExcelNewMatorn excelNewMatorn = new ExcelNewMatorn();

            excelNewMatorn.setPhoto(String.valueOf(list.get(i).get("photo")));
            excelNewMatorn.setNumber(String.valueOf(list.get(i).get("number")));
            excelNewMatorn.setName(String.valueOf(list.get(i).get("name")));
            excelNewMatorn.setIdcard(String.valueOf(list.get(i).get("idcard")));
            excelNewMatorn.setAge(String.valueOf(list.get(i).get("age")));

            excelNewMatorn.setZodiac(String.valueOf(list.get(i).get("zodiac")));
            excelNewMatorn.setHousehold(String.valueOf(list.get(i).get("household")));
            excelNewMatorn.setPhone(String.valueOf(list.get(i).get("phone")));
            excelNewMatorn.setBank_card(String.valueOf(list.get(i).get("bank_card")));
            excelNewMatorn.setBank_name(String.valueOf(list.get(i).get("bank_name")));

            excelNewMatorn.setGrade(String.valueOf(list.get(i).get("grade")));
            excelNewMatorn.setTrueday(String.valueOf(list.get(i).get("trueday")));
            excelNewMatorn.setService_count(String.valueOf(list.get(i).get("service_count")));
            String shelf1=null;
            if (String.valueOf(list.get(i).get("shelf")).equals("1")){
                shelf1="黑名单";
            }
            if (String.valueOf(list.get(i).get("shelf")).equals("2")){
                shelf1="未上架";
            }
            if (String.valueOf(list.get(i).get("shelf")).equals("3")){
                shelf1="已上架";
            }
            if (String.valueOf(list.get(i).get("shelf")).equals("4")){
                shelf1="服务中";
            }
            excelNewMatorn.setShelf(shelf1);
            excelNewMatorn.setAgent(String.valueOf(list.get(i).get("agent")));

            excelNewMatorn.setOrigin(String.valueOf(list.get(i).get("origin")));
            String isPrice1=null;
            if (String.valueOf(list.get(i).get("isPrice")).equals("1")){
                isPrice1="待激活";
            }
            if (String.valueOf(list.get(i).get("isPrice")).equals("2")){
                isPrice1="已交押金";
            }
            if (String.valueOf(list.get(i).get("isPrice")).equals("3")){
                isPrice1="已退押金";
            }
            if (String.valueOf(list.get(i).get("isPrice")).equals("4")){
                isPrice1="已交质保金";
            }
            if (String.valueOf(list.get(i).get("isPrice")).equals("5")){
                isPrice1="已退质保金";
            }
            excelNewMatorn.setIsPrice(isPrice1);
            excelNewMatorn.setL_name(String.valueOf(list.get(i).get("l_name")));

            excelJSONList.add(excelNewMatorn);
        }
        //调取封装的方法，传入相应的参数

        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;

    }

    /**
     * :(导出数据). <br/>
     * * @author Somnus
     *
     * @return
     */
    @ApiOperation("员工账号导出")
    @GetMapping(value = "/outUser")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cid", value = "单位id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "did", value = "部门id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "pid", value = "岗位id", required = false,  paramType = "query"),
            @ApiImplicitParam(name = "number", value = "姓名工号手机号", required = false,  paramType = "query"),

    })
    public String outUser(HttpServletResponse response,Integer cid,Integer did,Integer pid,String number) throws IOException, IllegalAccessException {
        SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
        String nowtime = time.format(new Date());
        //文件名
        String fileName = "员工统计表" + nowtime;
        //sheet名
        String sheetName = "员工统计sheet";


        //表头集合，作为表头参数
        List<String> titleList = new ArrayList<>();

        titleList.add("工装照");
        titleList.add("工号");
        titleList.add("姓名");
        titleList.add("性别");
        titleList.add("单位");

        titleList.add("部门");
        titleList.add("岗位");
        titleList.add("账号");
        titleList.add("密码");
        titleList.add("责任描述");

        titleList.add("创建时间");

        //数据对象，这里模拟手动添加，真实的环境往往是从数据库中得到
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list=excelServie.outUser(cid,did,pid,number);

        List<ExcelUser> excelJSONList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ExcelUser excelUser = new ExcelUser();

            excelUser.setPhoto(String.valueOf(list.get(i).get("photo")));
            excelUser.setUid(String.valueOf(list.get(i).get("uid")));
            excelUser.setName(String.valueOf(list.get(i).get("name")));
            excelUser.setSex(String.valueOf(list.get(i).get("sex")));
            excelUser.setCompany(String.valueOf(list.get(i).get("company")));

            excelUser.setDepartment(String.valueOf(list.get(i).get("department")));
            excelUser.setPost(String.valueOf(list.get(i).get("post")));
            excelUser.setPhone(String.valueOf(list.get(i).get("phone")));
            excelUser.setPassword(String.valueOf(list.get(i).get("password")));
            excelUser.setDescription(String.valueOf(list.get(i).get("description")));

            excelUser.setCreat_time(String.valueOf(list.get(i).get("creat_time")));



            excelJSONList.add(excelUser);
        }
        //调取封装的方法，传入相应的参数

        HSSFWorkbook workbook = ExcelUtil.createExcel(sheetName, titleList, excelJSONList);

        //输出Excel文件
        OutputStream output = response.getOutputStream();
        response.reset();
        //中文名称要进行编码处理
        response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("GB2312"), "ISO8859-1") + ".xls");
        response.setContentType("application/msexcel");
        workbook.write(output);
        output.close();
        return null;

    }
}