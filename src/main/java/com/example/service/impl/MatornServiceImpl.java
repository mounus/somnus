package com.example.service.impl;

import com.example.dao.MatornDao;
import com.example.dto.MatornDto;
import com.example.entiy.Matorn;
import com.example.service.MatornService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class MatornServiceImpl implements MatornService {

    @Autowired(required = false)
    private MatornDao matornDao;

    @Override
    public List<Matorn> matornsList() {
        return matornDao.matonsList();
    }

    @Override
    public Matorn findOne(String name) {

        return matornDao.findOne(name);
    }

    @Override
    public List<Map<String, Object>> getById(Integer id) {
        return matornDao.getById(id);
    }

    @Override
    public int save(Matorn matorn) {
     return    matornDao.save(matorn);
    }

    @Override
    public int update(String json) {
        return  matornDao.update(json);
    }

    @Override
    public int getForId() {
       return  matornDao.getForId();
    }

    @Override
    public void del(String name) {
        matornDao.del(name);

    }


    @Override
    public int updateMatorn(Matorn matorn) {
        return matornDao.updateMatorn(matorn);
    }

    @Override
    public int saveOrUpdate(Matorn matorn) {
        return matornDao.saveOrUpdate(matorn);
    }

    @Override
    public String getIdentity(String identity) {
        return matornDao.getIdentity(identity);
    }

    @Override
    public Map<String, Object> serviceRecord(String json) {
        return matornDao.serviceRecord(json);
    }

    @Override
    public int addHeathly(String json) {
        return matornDao.addHeathly(json);
    }

    @Override
    public Map<String, Object> getAllmatorn(String json) {
        return matornDao.getAllmatorn(json);
    }

    @Override
    public Map<String, Object> matornCondition() {
        return matornDao.matornCondition();
    }

    @Override
    public int editMatorn(String json) {
        return matornDao.editMatorn(json);
    }

    @Override
    public int setMatornCount(String json) {
        return matornDao.setMatornCount(json);
    }

    @Override
    public int addMatornDto(MatornDto matornDto) {
        return matornDao.addMatornDto(matornDto);
    }

    @Override
    public List<Map<String,Object>> workAge() {
        return matornDao.workAge();
    }
}
