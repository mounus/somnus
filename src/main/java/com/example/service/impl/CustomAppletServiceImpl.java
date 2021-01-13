package com.example.service.impl;

import com.example.dao.CustomAppletDao;
import com.example.entiy.Custom;
import com.example.service.CustomAppletService;
import com.example.service.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class CustomAppletServiceImpl implements CustomAppletService {
    @Autowired(required = false)
    private CustomAppletDao customAppletDao;


    @Override
    public Map<String, Object>getCustomAppletLogin(String json) {
        return customAppletDao.getCustomAppletLogin(json);
    }

    @Override
    public int addCustomPhoto(String json) {
        return customAppletDao.addCustomPhoto(json);
    }

    @Override
    public  List<Map<String,Object>> recommendMatorn(String json) {
        return customAppletDao.recommendMatorn(json);
    }

    @Override
    public Map<String, Object> getCustomConstellation(String json) {
        return customAppletDao.getCustomConstellation(json);
    }
    @Override
    public int addCollect(String json) {
        return customAppletDao.addCollect(json);
    }

    @Override
    public Map<String, Object> myCollectBrowse(String json) {
        return customAppletDao.myCollectBrowse(json);
    }

    @Override
    public List<Map<String, Object>> getOneMatorn(String json) {
        return customAppletDao.getOneMatorn(json);
    }

    @Override
    public Map<String, Object> appointmentBusiness(String json) {
        return customAppletDao.appointmentBusiness(json);
    }
    @Override
    public int appointment(String json) {
        return customAppletDao.appointment(json);
    }

    @Override
    public Map<String,Object> addInformation(String json) {
        return customAppletDao.addInformation(json);
    }

    @Override
    public int deleteBrowse(String json) {
        return customAppletDao.deleteBrowse(json);
    }
    @Override
    public List< Map<String, Object>>  myOrder(String json) {
        return customAppletDao.myOrder(json);
    }

    @Override
    public int newChoiceMatorn(String json) {
        return customAppletDao.newChoiceMatorn(json);
    }

    @Override
    public int addAddress(String json) {
        return customAppletDao.addAddress(json);
    }

    @Override
    public int newRecommend(String json) {
        return customAppletDao.newRecommend(json);
    }

    @Override
    public List<Map<String, Object>> myRecommend(String json) {
        return customAppletDao.myRecommend(json);
    }

    @Override
    public Map<String, Object> myRank(String json) {
        return customAppletDao.myRank(json);
    }
}
