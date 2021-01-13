package com.example.service.impl;


import com.example.dao.OriginDao;
import com.example.entiy.Origin;
import com.example.service.OriginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OriginServiceImpl implements OriginService {
    @Autowired(required = false)
    private OriginDao originDao;


    public int save(Origin origin) {

        return originDao.save(origin);
    }

    @Override
    public int update(Origin origin) {
        return originDao.update(origin);
    }

    @Override
    public int delete(Integer mid) {
        return originDao.delete(mid);
    }

    @Override
    public int deleteOrder(String json) {
        return originDao.deleteOrder(json);
    }

    @Override
    public int updateTime(String json) {
        return originDao.updateTime(json);
    }

    @Override
    public  List<Map<String,Object>> allmechanism() {
        return originDao.allmechanism();
    }
}