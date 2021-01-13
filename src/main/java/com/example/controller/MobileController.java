package com.example.controller;


import com.example.entiy.excel.ExcelReservation;
import com.example.entiy.Reservation;
import com.example.service.MobileServie;
import com.example.util.ExcelUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/web/mobile",produces="application/json;charset=utf-8")
@Api(tags="手机网页管理")
public class MobileController {

    @Autowired(required = false)
    private MobileServie mobileServie;
    /**
     * save:(新添加预约). <br/>
     * @author Somnus
     * @return
     */
    @ApiOperation("添加预约")
    @ResponseBody
    @RequestMapping(value="/save",method = RequestMethod.POST)
    public int save(@RequestBody  String json) {
     return    mobileServie.save(json);
    }



    /**
     * save:(查询所有预约). <br/>
     * @author Somnus
     * @return
     */
    @ApiOperation("查询所有预约")
    @ResponseBody
    @RequestMapping(value="/reservationList",method = RequestMethod.POST)
    public List<Reservation> reservationList() {
        return mobileServie.reservationList();
    }



    /**
     * channel:(客户渠道查询). <br/>
     *
     * @return
     * @author Somnus
     */
    @ApiOperation("客户渠道查询")
    @ResponseBody
    @RequestMapping(value = "/channel", method = RequestMethod.POST)
    public Map<String, Object> channel() {
        return  this.mobileServie.channel();

    }



}
