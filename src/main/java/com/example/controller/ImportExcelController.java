package com.example.controller;

import com.example.service.ImportExcelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/wx/importExcel", produces = "application/json;charset=utf-8")
@Api(tags = "导入表格管理/")
public class ImportExcelController {
    @Autowired(required = false)
    private ImportExcelService importExcelService;

    @ResponseBody
    @ApiOperation("导入客户字段模板")
    @RequestMapping(value = "/importExcelCustomTemplate", method = RequestMethod.POST)
    public   Map<String,Object> changeGrade(@RequestBody String json) {

      return   importExcelService.importExcelCustomTemplate(json);

    }

}
