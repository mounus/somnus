package com.example.service.impl;

import com.example.dao.PeriodDao;
import com.example.entiy.Period;
import com.example.service.PeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;
@Service
public class PeriodServiceImpl  implements PeriodService {

    @Autowired(required = false)
    private PeriodDao periodDao;



    @Override
    public List<Map<String, Object>> listBy(String somnus, Integer uid,Integer start) {

        return periodDao.listBy(somnus,uid,start);
    }

    @Override
    public Map<String, Object> getAdmin(String json) {
        return periodDao.getAdmin(json);
    }

    @Override
    public int save(String json) {
        return periodDao.save(json);
    }

    @Override
    public List<Map<String, Object>> getById(Integer mid) {

        return periodDao.getById(mid);
    }

    @Override
    public Period getByStates(int states) {
        return periodDao.getByStates(states);
    }

    @Override
    public int update(String  json) {
        return periodDao.update(json);
    }


    @Override
    public Map<String, Object> getByNumber(String json) {
        return periodDao.getByNumber(json);
    }

    @Override
    public int updateGrade_agent(String json) {
        return periodDao.updateGrade_agent(json);
    }

    @Override
    public List<Map<String, Object>> getByTime(String json) {
        return periodDao.getByTime(json);
    }

    @Override
    public List<Map<String, Object>> dynamic(String json) {
        return periodDao.dynamic(json);
    }

    @Override
    public Map<String, Object> getByGrade(String json) {
        return periodDao.getByGrade(json);
    }

    @Override
    public List<Map<String, Object>> getByName(String json) {
        return periodDao.getByName( json);
    }

    @Override
    public int updateCollect(String json) {
        return periodDao.updateCollect(json);
    }

    @Override
    public List<Map<String, Object>> myCollect(String json) {
        return periodDao.myCollect(json);
    }

    @Override
    public List<Map<String, Object>> myCollectByName(String json) {
        return periodDao.myCollectByName( json);
    }
}
