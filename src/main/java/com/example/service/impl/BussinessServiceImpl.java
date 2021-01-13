package com.example.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.dao.BussinessDao;
import com.example.entiy.Bussiness;
import com.example.service.BussinessService;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
