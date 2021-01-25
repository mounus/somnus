package com.example.service.impl;

import com.example.dao.TimeDao;
import com.example.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TimeServiceImpl implements TimeService {
    @Autowired(required = false)
    private TimeDao timeDao;


    @Override
    public void matronShelfTime() {
        timeDao.matronShelfTime();
    }

    @Override
    public void oldCreatMatronTime() {
        timeDao.oldCreatMatronTime();
    }

    @Override
    public void newCreatMatronTime() {
        timeDao.newCreatMatronTime();
    }

    @Override
    public void serviceMatronTime() {
        timeDao.serviceMatronTime();
    }
}
