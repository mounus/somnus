package com.example.service;

import java.util.Map;

public interface UserService {
    /**
     * @description: 小程序登录验证。
     * @return:
     */
    Map<String,Object> getLogin(String phone, String password);
    /**
     * @description: 后台登录验证。
     * @return:
     */
    Map<String,Object> getAdmin(String phone, String password);

    /**
     * @description: 业务员登录验证。
     * @return:
     */
    Map<String,Object> getBussiness(String phone, String password);


    /**
     * @description: 小程序登录状态验证。
     * @return:
     */
    Map<String,Object> getStates(String phone, String base);
    /**
     * @description: 小程序登录状态验证。
     * @return:
     */
    Map<String,Object> getAdminStates(String phone, String base);
    /**
     * @description: 验证码登录验证。
     * @return:
     */

    int getTrue (String phone,String code);
    /**
     * @description: 验证添加/修改。
     * @return:
     */
    int saveOrUpdate(String phone,String code);//添加

    /**
     * @description: 验证码登录验证。
     * @return:
     */
    Map<String,Object> getBussinessStates(String phone, String base);
    /**
     * @description: 获得最新添加的id
     * @return:
     */
    int getForId(String phone);
    /**
     * @description: 获取退出时间
     * @return:
     */
    int getExitTime(String phone);

    /**
     * @description: 查看退出后的新消息
     * @return:
     */
    Map<String ,Object> getMessage(String uid);
    /**
     * @description: 派岗小程序登录验证。
     * @return:
     */
    Map<String,Object> getPost(String phone, String password);

    /**
     * @description: 派岗小程序登录状态验证。
     * @return:
     */
    Map<String,Object> getPostStates(String phone, String base);
    /**
     * @description: 添加用户。
     * @return:
     */
    int  addUser(String json);


}
