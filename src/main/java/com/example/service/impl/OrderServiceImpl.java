package com.example.service.impl;

import com.example.dao.OrderDao;
import com.example.entiy.Order_demand;
import com.example.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl  implements OrderService {
    @Autowired(required = false)
    private OrderDao orderDao;

    @Override
    public int getForId() { return orderDao.getForId(); }


    @Override
    public int saveDemand(Order_demand order_demand) {
        return orderDao.saveDemand(order_demand);
    }

    @Override
    public int updateDemand(Order_demand order_demand) {
        return orderDao.updateDemand(order_demand);
    }


    @Override
    public List<Map<String, Object>> managerOrder(String json) {
        return orderDao.managerOrder(json);
    }

    @Override
    public Map<String, Object> allOrder(String json) {
        return orderDao.allOrder(json);
    }

    @Override
    public Map<String, Object> orderCondition() {
        return orderDao.orderCondition();
    }

    @Override
    public Map<String, Object> homeAndClub(String json) {
        return orderDao.homeAndClub(json);
    }
}

