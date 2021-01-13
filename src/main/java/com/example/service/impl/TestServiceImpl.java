package com.example.service.impl;

import com.example.dao.TestDao;
import com.example.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {
    @Autowired(required = false)
    private TestDao testDao;

}