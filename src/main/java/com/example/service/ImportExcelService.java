package com.example.service;

import java.util.List;
import java.util.Map;

public interface ImportExcelService {
    /**
     * @description: 导入月嫂模板字段资料。
     * @return:
     */
    Map<String,Object> importExcelCustomTemplate(String json);

}
