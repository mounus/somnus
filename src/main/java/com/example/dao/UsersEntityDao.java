package com.example.dao;

import com.example.entiy.UsersEntity;

import java.util.List;

public interface UsersEntityDao {
    /**
     * @description: 获取表中所有信息。
     * @return: java.util.List<com.study.spring.entity.UsersEntity>
     */
    List<UsersEntity> usersList();

    /**
     * @description: 通过name来查询信息
     * @param: name
     * @return: com.study.spring.entity.UsersEntity
     */
    UsersEntity findUserOne(String name);

    /**
     * @description: 向表中插入一条数据
     * @param: usersEntity
     * @return: void
     */
    void saveUser(UsersEntity usersEntity);

    /**
     * @description: 更新表中单条数据
     * @param: usersEntity
     * @return: void
     */
    void updateUser(UsersEntity usersEntity);

    /**
     * @description: 删除表中单条数据
     * @param: name
     * @return: void
     */
    void removeUser(String name);
    /**
     * @description:
     * @param: name
     * @return: void
     */
    int  test();
}
