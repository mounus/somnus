package com.example.dao;

import com.example.entiy.User;

import java.util.List;
import java.util.Map;
/**
 * @description: 后台员工管理接口
 * @param:
 * @return:
 */
public interface EmployeesDao {

    /**
     * @description: 后台员工查询所有
     * @return:
     */
    Map<String,Object> getAllEmployees(String json);


    /**
     * @description: 后台员工/机构添加
     * @return:
     */
    Integer addEmployees(User user);

    /**
     * @description: 后台员工/机构修改
     * @return:
     */
    Integer updateEmployees(User user);

    /**
     * @description: 后台员工/机构查询
     * @return:
     */
    User findUserOne(String  json);

    /**
     * @description: 后台员工/机构删除
     * @return:
     */
    int deleteEmployees(String  json);

    //________________________
    /**
     * @description: 后台员工账号管理
     * @return:
     */
    Map<String,Object> getEmployeesList(String json);
    /**
     * @description: 后台员工查询条件
     * @return:
     */
    Map<String,Object> employeesCondition();

    /**
     * @description: 后台添加/修改员工
     * @return:
     */
    int  addOrUpdateUser(User user);


    /**
     * @description: 后台员工查询
     * @return:
     */
    List<Map<String,Object>>  findOneUser(String  json);


}
