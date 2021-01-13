package com.example.service.impl;

import com.example.dao.ExcelDao;
import com.example.entiy.Question;
import com.example.entiy.Reservation;
import com.example.service.ExcelServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelServie {

    @Autowired(required = false)
    private ExcelDao excelDao;

    @Override
    public List<Map<String, Object>> out_matorn(String somnus,String agent) {
        return excelDao.out_matorn(somnus,agent);
    }

    @Override
    public List<Reservation> outExcelReservation(Integer uid) {
        return excelDao.outExcelReservation(uid);
    }

    @Override
    public List<Map<String, Object>> outFiledMatorn(String json) {
        return excelDao.outFiledMatorn(json);
    }

    @Override
    public List<Map<String, Object>> outFiledCustom(Integer uid,String origin,String origin_channel,String intention,Integer sname,Integer bname,String orderSpeed, String service_type,String timetype,String level,String returnType, String number,String powerFiled,String start_time,String end_time ) {
        return excelDao.outFiledCustom(uid, origin, origin_channel, intention, sname, bname, orderSpeed, service_type, timetype, level, returnType, number, powerFiled, start_time, end_time);
    }

    @Override
    public int addPDf(Integer mid, String url) {
        return excelDao.addPDf(mid,url);
    }

    @Override
    public int addQuestion(Question question) {
        return excelDao.addQuestion(question);
    }

    @Override
    public List<Map<String, Object>> outCustomService(String json) {
        return excelDao.outCustomService(json);
    }

    @Override
    public List<Map<String, Object>> outCustomHome(String somnus,String monthFirst,String monthLast,Integer bid,String service_type,String timetype,String origin,String channel,Integer work_states, String number,Integer start) {
        return excelDao.outCustomHome(somnus,monthFirst, monthLast, bid, service_type, timetype, origin, channel, work_states, number, start);
    }

    @Override
    public List<Map<String, Object>> outCustomInfo(String json) {
        return excelDao.outCustomInfo(json);
    }

    @Override
    public List<Map<String, Object>> outCustomClub(String somnus,String monthFirst,String monthLast,Integer bid,String service_type,String timetype,String origin,String channel,Integer work_states, String number,Integer start) {

        return excelDao.outCustomClub(somnus,monthFirst, monthLast, bid, service_type, timetype, origin, channel, work_states, number, start);
    }

    @Override
    public List<Map<String, Object>> outNewMatorn(Integer uid,String zodiac,String grade,Integer shelf,Integer bid,String source,String origin, Integer isPrice,String powerFiled,String number) {
        return excelDao.outNewMatorn(uid, zodiac, grade, shelf, bid, source, origin, isPrice, powerFiled, number);
    }

    @Override
    public List<Map<String, Object>> outUser(Integer cid, Integer did, Integer pid, String number) {
        return excelDao.outUser(cid, did, pid, number);
    }
}
