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

    @ResponseBody
    @ApiOperation("导入问题")
    @RequestMapping(value = "/importDeposit", method = RequestMethod.POST)
    public int addQustion() {

        String excelPath = "C:\\Users\\MLOONG\\Desktop\\质保金.xlsx";
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

                int i = 0;
                for (int rIndex = firstRowIndex; rIndex < lastRowIndex; rIndex++) {   //遍历行
                    // System.out.println("rIndex: " + rIndex);
                    Row row = sheet.getRow(rIndex);
                    if (row != null) {
                        int firstCellIndex = row.getFirstCellNum();
                        int lastCellIndex = row.getLastCellNum();


                        String number = null;
                        String name = null;
                        for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                            Cell cell = row.getCell(cIndex);
                            if (cell != null && !"".equals(cell.toString().trim())) {
                                switch (cIndex) {

                                    case 0:
                                        number = cell.toString();
                                        System.out.println("number:" + number);
                                        break;
                                    case 1:
                                        name = cell.toString();
                                        System.out.println("name:" + name);
                                        break;

                                    default:
                                        break;
                                }

                            }

                        }
                        testService.importDeposit(number);

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
}






