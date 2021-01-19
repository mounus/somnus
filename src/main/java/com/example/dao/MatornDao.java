package com.example.dao;

import com.example.dto.MatornDto;
import com.example.entiy.Matorn;


import java.util.List;
import java.util.Map;
/**
 * @description: 月嫂小程序月嫂信息管理接口
 * @param:
 * @return:
 */
public interface MatornDao {


    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Matorn>
     */
    List<Matorn> matonsList();//del

    /**
     * @description: 通过id来查询月嫂所有相关的表
     * @param: id
     * @return: com.example.entity.Matorn/Origin/Contact/Bussiness
     */
    List<Map<String,Object>> getById(Integer id);//del

    /**
     * @description: 通过name来查询信息
     * @param: name
     * @return: com.example.entity.Matorn
     */
    Matorn findOne(String name);//del

    /**
     * @description: 向表中插入一条数据
     * @param: matorn
     *
     */
    int save(Matorn matorn);//del
    /**
     * @description: 向表中插入一条数据
     * @param: matorn
     *
     */
    int updateMatorn(Matorn matorn);//del
    /**
     * @description: 更中数据
     * @param: matorn
     * @return:
     */
    int update(String json);//del

    /**
     * @description: 删除表中单条数据
     * @param: name
     * @return: void
     */
    void del(String name);//del

    /**
     * @description: 返回月嫂id
     * @param:
     * @return: id
     */
    int getForId();//del

    /**
     * @description: 添加修改
     * @param: matorn
     *
     */
    int saveOrUpdate(Matorn matorn);//del
    /**
     * @description: 添加修改
     * @param: matorn
     *
     */
    String getIdentity(String identity);

    /**
     * @description: 月嫂的服务记录
     * @param: uid
     * @return: com.example.entity.Period
     */
    Map<String,Object> serviceRecord(String json) ;


    /**
     * @description: 月嫂上传健康证
     * @param:
     *
     */
    int addHeathly(String json);

    /**
     * @description: 后台月嫂管理列表
     * @param:
     *
     */
    Map<String ,Object> getAllmatorn(String json);
    /**
     * @description: 月嫂管理筛选条件
     * @param:
     *
     */
    Map<String ,Object> matornCondition() ;

    /**
     * @description: 修改月嫂信息
     * @param:
     *
     */
    int editMatorn(String json) ;
    /**
     * @description: 设置后台统计月嫂数量
     * @param:
     *
     */
    int setMatornCount(String json);

    /**
     * @description: 新的添加月嫂
     * @param:
     *
     */
    int addMatornDto(MatornDto matornDto);

    /**
     * @description: 从业年限条件
     * @param:
     *
     */
    List<Map<String, Object>>  workAge();

    /**
     * @description: 判断是否添加身份证
     * @param:
     *
     */
    int isAdd (String json);
}
