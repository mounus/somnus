package com.example.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;


import com.example.entiy.excel.AgentExcel;
import com.example.entiy.excel.SignOutExcelJSON;
import com.example.service.MatornService;
import com.example.service.OriginService;
import com.example.service.StatisticsService;
import com.example.service.TestService;

import com.example.util.ExcelUtil;
import com.example.util.HttpUtils;
import com.sun.org.apache.xpath.internal.objects.XNumber;
import io.swagger.annotations.*;

import net.sf.json.JSONArray;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping(value = "/wx/test", produces = "application/json;charset=utf-8")
@Api(tags = "test管理")
public class TestController {

    @Autowired(required = false)
    private TestService testService;
    @Autowired(required = false)
    private OriginService originService;

    @Autowired(required = false)
    private MatornService matornService;
    @Autowired(required = false)
    private StatisticsService statisticsService;

}






