package com.example.dao;

import sun.applet.resources.MsgAppletViewer;

import java.util.List;
import java.util.Map;

/**
 * @description: 表格导出管理接口
 * @param:
 * @return:
 */
public interface ImportExcelDao {
    /**
     * @description: 导入月嫂模板字段资料。
     * @return:
     */
    Map<String,Object> importExcelCustomTemplate(String json);

}
