package com.example.service.impl;

import com.example.dao.UserDao;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl  implements UserService {
    @Autowired(required = false)
    private UserDao userDao;

    @Override
    public int getForId(String phone) {
        return userDao.getForId(phone);
    }

    @Override
    public Map<String, Object> getLogin(String phone, String password) {
        return userDao.getLogin(phone, password);
    }

    @Override
    public int saveOrUpdate(String phone, String code) {
        return userDao.saveOrUpdate(phone,code);
    }


    @Override
    public int getTrue(String phone, String code) {

        return userDao.getTrue(phone,code);
    }

    @Override
    public Map<String, Object> getStates(String phone, String base) {
        return userDao.getStates(phone, base);
    }

    @Override
    public Map<String, Object> getAdmin(String phone, String password) {
        return userDao.getAdmin(phone, password);
    }

    @Override
    public Map<String, Object> getAdminStates(String phone, String base) {
        return userDao.getAdminStates(phone, base);
    }

    @Override
    public int getExitTime(String phone) {
        return userDao.getExitTime(phone);
    }

    @Override
    public Map<String, Object> getMessage(String uid) {
        return userDao.getMessage(uid);
    }

    @Override
    public Map<String, Object> getBussiness(String phone, String password) {
        return userDao.getBussiness(phone, password);
    }

    @Override
    public Map<String, Object> getBussinessStates(String phone, String base) {
        return userDao.getBussinessStates(phone, base);
    }

    @Override
    public Map<String, Object> getPost(String phone, String password) {
        return userDao.getPost(phone, password);
    }

    @Override
    public Map<String, Object> getPostStates(String phone, String base) {
        return userDao.getPostStates(phone, base);
    }

    @Override
    public int  addUser(String json) {
        return userDao.addUser(json);
    }


}
