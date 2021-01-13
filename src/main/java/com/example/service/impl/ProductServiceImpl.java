package com.example.service.impl;

import com.example.dao.ProductDao;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired(required = false)
    private ProductDao productDao;


    @Override
    public Map<String, Object> getAdmin(String json) {
        return productDao.getAdmin(json);
    }

    @Override
    public Map<String, Object> getByNumber(String json) {
        return productDao.getByNumber(json);
    }

    @Override
    public Map<String, Object> getByGrade(String json) {
        return productDao.getByGrade(json);
    }
}
