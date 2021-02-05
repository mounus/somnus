package com.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.dao.BussinessDao;
import com.example.dto.MatornDto;
import com.example.entiy.Bussiness;
import com.example.entiy.Score;
import com.example.service.BussinessService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BussinessServiceImpl implements BussinessService {
    @Autowired(required = false)
    private BussinessDao bussinessDao;

    @Override
    public int update(String json) {
        return bussinessDao.update(json);
    }

    @Override
    public int save(String json) {
       return bussinessDao.save(json);

    }

    @Override
    public int updateAssess(Integer id) {
        return bussinessDao.updateAssess(id);
    }

    @Override
    public int updateShelf(Integer id,Integer shelf) {
        return bussinessDao.updateShelf(id,shelf);
    }

    @Override
    public int updateIsblack(Integer id, Integer isblack) {
        return bussinessDao.updateIsblack(id,isblack);
    }

    @Override
    public List<Map<String, Object>> allMatorn(String json) {
        return bussinessDao.allMatorn(json);
    }

    @Override
    public Map<String, Object> bussinessCondition() {
        return bussinessDao.bussinessCondition();
    }

    @Override
    public Map<String, Object> oneMatornDetail(String json) {
        return bussinessDao.oneMatornDetail(json);
    }

    @Override
    public List<Map<String, Object>> matornAllOrder(String json) {
        return bussinessDao.matornAllOrder(json);
    }

    @Override
    public int saveScore(Score score) {
        return bussinessDao.saveScore(score);
    }

    @Override
    public List<String> getComment() {
        return bussinessDao.getComment();
    }

    @Override
    public List<Map<String, Object>> getById(String json) {
        return bussinessDao.getById(json);
    }

    @Override
    public int updateMatornDto(MatornDto matornDto) {
        return bussinessDao.updateMatornDto(matornDto);
    }

    @Override
    public int updateScore(Score score) {
        return bussinessDao.updateScore(score);
    }

    @Override
    public int updatePeriod(String json) {
        return bussinessDao.updatePeriod(json);
    }

    @Override
    public int addPhoto(String json) {
        return bussinessDao.addPhoto(json);
    }

    @Override
    public int deletePhoto(String json) {
        return bussinessDao.deletePhoto(json);
    }
}
