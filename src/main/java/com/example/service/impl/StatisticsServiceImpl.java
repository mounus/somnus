package com.example.service.impl;

import com.example.dao.StatisticsDao;
import com.example.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StatisticsServiceImpl  implements StatisticsService {
    @Autowired(required = false)
    private StatisticsDao statisticsDao;

    @Override
    public List<Map<String, Object>> getComplete() {
        return statisticsDao.getComplete();
    }

    @Override
    public Map<String, Object> allStatistics(String json) {
        return statisticsDao.allStatistics(json);
    }

    @Override
    public Map<String, Object> statisticsCondition() {
        return statisticsDao.statisticsCondition();
    }

    @Override
    public void statisticsTiming() {
        statisticsDao.statisticsTiming();
    }
}
