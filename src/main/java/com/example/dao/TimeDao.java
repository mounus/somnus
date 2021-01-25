package com.example.dao;


/**
 * @description: 定时器接口
 * @param:
 * @return:
 */
public interface TimeDao {
    /**
     * @description: 月嫂上下架定时器
     * @param:
     *
     */
    void matronShelfTime( );

    /**
     * @description: 已创建月嫂定时器
     * @param:
     *
     */
    void oldCreatMatronTime( );

    /**
     * @description: 新创建月嫂定时器
     * @param:
     *
     */
    void newCreatMatronTime( );
    /**
     * @description: 服务中月嫂定时器
     * @param:
     *
     */
    void serviceMatronTime( );


}
