package com.example.service.impl;

import com.example.dao.ContactDao;
import com.example.entiy.Contact;

import com.example.service.ContactServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ContactServiceImpl implements ContactServie {
    @Autowired(required = false)
    private ContactDao contactDao;

    @Override
    public int update(Contact contact) {
        return contactDao.update(contact);
    }

    public int save(Contact contact) {

        return  contactDao.save(contact);
    }

    @Override
    public List<Integer> outExcel(String somnus,String agent,String grade) {
        return contactDao.outExcel(somnus, agent, grade);
    }

    @Override
    public List<Map<String, Object>> outExcelById(Integer id) {
        return contactDao.outExcelById(id);
    }
}
