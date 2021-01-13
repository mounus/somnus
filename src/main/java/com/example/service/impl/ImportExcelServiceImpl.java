package com.example.service.impl;

import com.example.dao.ImportExcelDao;
import com.example.service.ImportExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ImportExcelServiceImpl implements ImportExcelService {
    @Autowired(required = false)
    private ImportExcelDao importExcelDao;

    @Override
    public   Map<String,Object> importExcelCustomTemplate(String json ) {
        return importExcelDao.importExcelCustomTemplate(json);
    }
}
