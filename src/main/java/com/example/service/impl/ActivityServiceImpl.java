package com.example.service.impl;

import com.example.dao.ActivityDao;
import com.example.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired(required = false)
    private ActivityDao activityDao;
    @Override
    public Map<String, Object> getAllReservation(String json) {

        return activityDao.getAllReservation(json);
    }
    @Override
    public Map<String, Object> getAllHref(String json) {
        return activityDao.getAllHref(json);
    }

    @Override
    public int addHref(String json) {
        return activityDao.addHref(json);
    }
}
