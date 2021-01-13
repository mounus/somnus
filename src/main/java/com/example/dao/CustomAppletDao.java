package com.example.dao;

import com.example.entiy.Custom;

import java.util.List;
import java.util.Map;
/**
 * @description: 会员小程序接口
 * @param:
 * @return:
 */
public interface CustomAppletDao {
    /**
     * @description: 登录授权，获取客户信息。
     * @return:
     */
    Map<String, Object> getCustomAppletLogin(String json);
    /**
     * @description: 添加客户图片，昵称。
     * @return:
     */
    int addCustomPhoto(String json);


    /**
     * @description: 推荐月嫂。
     * @return:
     */
    List<Map<String, Object>> recommendMatorn(String json);

    /**
     * @description: 通过出生年月获取客户星座/姓名，出生年月。
     * @return:
     */
    Map<String, Object> getCustomConstellation(String json);

    /**
     * @description: 客户添加收藏。
     * @return:
     */
    int addCollect(String json);

    /**
     * @description: 客户的所有收藏/浏览记录。
     * @return:
     */
   Map<String, Object> myCollectBrowse(String json);

    /**
     * @description: 查询月嫂详情及添加浏览记录。
     * @return:
     */
    List<Map<String, Object>> getOneMatorn(String json);

    /**
     * @description:预约面试推荐业务员。
     * @return:
     */
    Map<String, Object> appointmentBusiness(String json);

    /**
     * @description:预约面试业务员。
     * @return:
     */
    int appointment(String json);

    /**
     * @description:客户填写推荐信息。
     * @return:
     */
    Map<String,Object> addInformation(String json);

    /**
     * @description:删除浏览记录。
     * @return:
     */
    int deleteBrowse(String json);

    /**
     * @description:客户我的订单。
     * @return:
     */
   List< Map<String, Object>> myOrder(String json);
    /**
     * @description:客户重新选择月嫂。
     * @return:
     */
    int newChoiceMatorn(String json);
    /**
     * @description:客户添加派岗地址。
     * @return:
     */
    int addAddress(String json);
    /**
     * @description:客户重新推荐月嫂。
     * @return:
     */
    int newRecommend(String json);
    /**
     * @description:客户我的推荐(详细)。
     * @return:
     */
    List< Map<String, Object>> myRecommend(String json);

    /**
     * @description:客户我的排名(详细)。
     * @return:
     */
     Map<String, Object> myRank(String json);



}
