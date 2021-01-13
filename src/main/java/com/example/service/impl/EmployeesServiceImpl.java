package com.example.service.impl;

import com.example.dao.EmployeesDao;
import com.example.entiy.User;
import com.example.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class EmployeesServiceImpl implements EmployeesService {
    @Autowired(required = false)
    private EmployeesDao employeesDao;


    @Override
    public Map<String, Object> getAllEmployees(String json) {
        return employeesDao.getAllEmployees(json);
    }

    @Override
    public Integer addEmployees(User user) {

        return employeesDao.addEmployees(user);
    }

    @Override
    public Integer updateEmployees(User user)
    {
        return employeesDao.updateEmployees(user);
    }

    @Override
    public User findUserOne(String  json)
    {
        return employeesDao.findUserOne(json);
    }

    @Override
    public int deleteEmployees(String  json) {
        return employeesDao.deleteEmployees(json);
    }

    @Override
    public Map<String, Object> getEmployeesList(String json) {
        return employeesDao.getEmployeesList(json);
    }

    @Override
    public Map<String,Object> employeesCondition() {
        return employeesDao.employeesCondition();
    }

    @Override
    public int  addOrUpdateUser(User user) {
        return employeesDao.addOrUpdateUser(user);
    }

    @Override
    public List<Map<String, Object>> findOneUser(String json) {
        return employeesDao.findOneUser(json);
    }
}
