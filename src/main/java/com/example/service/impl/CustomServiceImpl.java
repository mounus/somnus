package com.example.service.impl;

import com.example.dao.CustomDao;
import com.example.entiy.Custom;
import com.example.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CustomServiceImpl implements CustomService {

    @Autowired(required = false)
    private CustomDao customDao;

    @Override
    public int save(Custom custom) {

        return customDao.save(custom);
    }

    @Override
    public Map<String, Object> channel() {
        return customDao.channel();
    }

    @Override
    public int chooseMatorn(String json) {
        return customDao.chooseMatorn(json);
    }


    @Override
    public List<Map<String, Object>> getOneMatorn(String json) {
        return customDao.getOneMatorn(json);
    }

    @Override
    public int update(Custom custom) {
        return customDao.update(custom);
    }
    @Override
    public int updateStates(String json) {
        return customDao.updateStates(json);
    }

    @Override
    public Map<String, Object> getStatistics(String json) {
        return customDao.getStatistics(json);
    }

    @Override
    public List<Map<String, Object>> postList(String json) {
        return customDao.postList(json);
    }

    @Override
    public Map<String,Object> recommendMatorn(String json) {
        return customDao.recommendMatorn(json);
    }

    @Override
    public int choiceMatorn(String json) { return customDao.choiceMatorn(json); }

    @Override
    public int confirmPost(String json) {
        return customDao.confirmPost(json);
    }

    @Override
    public List<Map<String, Object>> PostByName(String json) {
        return customDao.PostByName(json);
    }

    @Override
    public int getMatornNull(String json) {
        return customDao.getMatornNull(json);
    }

    @Override
    public int newMatorn(String json) {
        return customDao.newMatorn(json);
    }

    @Override
    public Map<String, Object> getAllCustom(String json) {
        return customDao.getAllCustom(json);
    }

    @Override
    public List<Map<String, Object>> orderDynamic(String json) {
        return customDao.orderDynamic(json);
    }

    @Override
    public List<Map<String, Object>> intentionList(String json) {
        return customDao.intentionList(json);
    }

    @Override
    public int updateMessage(String  json) {
        return customDao.updateMessage(json);
    }

}
