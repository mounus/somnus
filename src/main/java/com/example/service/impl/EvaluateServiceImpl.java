package com.example.service.impl;

import com.example.dao.EvaluateDao;
import com.example.entiy.Score;
import com.example.service.EvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.service.EvaluateService;

import java.util.List;
import java.util.Map;

@Service
public class EvaluateServiceImpl  implements EvaluateService {
    @Autowired(required = false)
    private EvaluateDao evaluateDao;


    @Override
    public int save(String json) {
        return evaluateDao.save(json);
    }

    @Override
    public Map<String, Object> getById(Integer mid) {
        return evaluateDao.getById(mid);
    }

    @Override
    public int deleteEvaluate(String json) {
        return evaluateDao.deleteEvaluate(json);
    }

    @Override
    public List<String> getComment() {
        return evaluateDao.getComment();
    }
    @Override
    public int saveScore(Score score) {
        return evaluateDao.saveScore(score);
    }

    @Override
    public Score getScoreById(String  json) {
        return evaluateDao.getScoreById(json);
    }

    @Override
    public int update(Score score) {
        return evaluateDao.update(score);
    }
}
