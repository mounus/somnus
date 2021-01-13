package com.example.service.impl;

import com.example.dao.MobileDao;
import com.example.entiy.Reservation;
import com.example.service.MobileServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MobileServieImpl implements MobileServie {
    @Autowired(required = false)
    private MobileDao mobileDao;

    @Override
    public int save(String json) {
        return mobileDao.save(json);
    }

    @Override
    public List<Reservation> reservationList() {
        return mobileDao.reservationList();
    }

    @Override
    public Map<String, Object> channel() {
        return mobileDao.channel();
    }
}
