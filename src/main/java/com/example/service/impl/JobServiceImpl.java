package com.example.service.impl;

import com.example.dao.EvaluateDao;
import com.example.dao.JobDao;
import com.example.entiy.Job_evaluate;
import com.example.entiy.Matorn;
import com.example.entiy.Score;
import com.example.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class JobServiceImpl implements JobService {
    @Autowired(required = false)
    private JobDao jobDao;


    @Override
    public Map<String, Object> getById(Integer mid) {
     return   jobDao.getById(mid);
    }


    @Override
    public int save(String  json) {
        return jobDao.save(json);
    }

    @Override
    public  Map<String, Object> findOne(Integer mid) {
        return jobDao.findOne(mid);
    }

    @Override
    public int update(String json) {
        return jobDao.update(json);
    }


    @Override
    public Map<String, Object> test(String json) {
        return jobDao.test(json);
    }
}
