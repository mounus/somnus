package com.example.service;

import com.example.dto.MatornDto;
import com.example.entiy.Matorn;

import java.util.List;
import java.util.Map;

public interface MatornService {
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.example.entity.Matorn>
     */
    List<Matorn> matornsList();

    /**
     * @description: 通过name来查询信息
     * @param: name
     * @return: com.example.entity.UsersEntity
     */
    Matorn findOne(String name);

    /**
     * @description: 通过id/uid来查询信息
     * @param: id
     * @return: com.example.entity.Matorn/Origin/Contact/Bussiness
     */
  //String getById(Integer id);
    List<Map<String,Object>>getById(Integer id);


    /**
     * @description: 向表中插入一条数据
     * @param: matorn
     * @return: void
     */
    int save(Matorn matorn);

    /**
     * @description: 更新一个表中数据
     * @param: matorn
     * @return: void
     */
    int updateMatorn(Matorn matorn);


    /**
     * @description: 更新四个表中数据
     * @param: matorn
     * @return: void
     */
    int update(String json);

    /**
     * @description: 删除表中单条数据
     * @param: name
     * @return: void
     */
    void del(String name);

    /**
     * @description:返回一个月嫂id
     * @param: name
     * @return: void
     */
    int getForId();

    /**
     * @description: 添加修改
     * @param: matorn
     *
     */
    int saveOrUpdate(Matorn matorn);


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
}
