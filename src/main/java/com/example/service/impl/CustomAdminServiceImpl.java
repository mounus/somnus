package com.example.service.impl;

import com.example.dao.CustomAdminDao;
import com.example.dao.CustomAppletDao;
import com.example.entiy.ConsultingOrder;
import com.example.entiy.Custom;
import com.example.entiy.Order_demand;
import com.example.entiy.SaleOrder;
import com.example.service.CustomAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

@Service
public class CustomAdminServiceImpl implements CustomAdminService {
    @Autowired(required = false)
    private CustomAdminDao customAdminDao ;

    @Override
    public Map<String, Object> getAllCustom(String json) {
        return customAdminDao.getAllCustom(json);
    }

    @Override
    public int setCustomCount(String json) {
        return customAdminDao.setCustomCount(json);
    }

    @Override
    public  Map<String,Object> saveOrUpdateCustom(Custom custom) {
        return customAdminDao.saveOrUpdateCustom(custom);
    }

    @Override
    public Map<String, Object> getAllCondition() {
        return customAdminDao.getAllCondition();
    }


    @Override
    public  Map<String,Object> saveOrUpdateOrder(String json) {
        return customAdminDao.saveOrUpdateOrder(json);
    }

    @Override
    public Map<String, Object> getOneCustom(String json) {
        return customAdminDao.getOneCustom(json);
    }

    @Override
    public int addConsultingOrder(ConsultingOrder consultingOrder) {
        return customAdminDao.addConsultingOrder(consultingOrder);
    }

    @Override
    public int addSaleOrder(SaleOrder saleOrder) {
        return customAdminDao.addSaleOrder(saleOrder);
    }

    @Override
    public int deleteCustom(String json) {
        return customAdminDao.deleteCustom(json);
    }

    @Override
    public Map<String, Object> orderList(String json)  {
        return customAdminDao.orderList(json);
    }

    @Override
    public int chooseMatorn(String json) {
        return customAdminDao.chooseMatorn(json);
    }

    @Override
    public int updateAddress(String json) {
        return customAdminDao.updateAddress(json);
    }

    @Override
    public int submitOrder(String json) {
        return customAdminDao.submitOrder(json);
    }

    @Override
    public int getArrival(String json) {
        return customAdminDao.getArrival(json);
    }

    @Override
    public int substitution(String json) {
        return customAdminDao.substitution(json);
    }

    @Override
    public int getConfirm(String json) {
        return customAdminDao.getConfirm(json);
    }

    @Override
    public int getMatornNull(String json) {
        return customAdminDao.getMatornNull(json);
    }

    @Override
    public int runService(String json) {
        return customAdminDao.runService(json);
    }

    @Override
    public int stopService(String json) {
        return customAdminDao.stopService(json);
    }

    @Override
    public Map<String,Object> updateBussiness(String json) {
        return customAdminDao.updateBussiness(json);
    }

    @Override
    public int continueOrder(String json) {
        return customAdminDao.continueOrder(json);
    }
}
