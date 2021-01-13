package com.example.service.impl;

import com.example.dao.UsersEntityDao;
import com.example.entiy.UsersEntity;
import com.example.service.UsersEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class UsersEntityServiceImpl implements UsersEntityService {

    @Autowired(required = false)
    private UsersEntityDao usersEntityDao;

    @Override
    public List<UsersEntity> usersList() {
        return usersEntityDao.usersList();
    }

    @Override
    public UsersEntity findUserOne(String name) {
        return usersEntityDao.findUserOne(name);
    }

    @Override
    public void saveUser(UsersEntity usersEntity) {
        usersEntityDao.saveUser(usersEntity);
    }

    @Override
    public void updateUser(UsersEntity usersEntity) {
        usersEntityDao.updateUser(usersEntity);
    }

    @Override
    public void removeUser(String name) {
        usersEntityDao.removeUser(name);
    }

    @Override
    public int test() {
        return usersEntityDao.test();
    }
}
