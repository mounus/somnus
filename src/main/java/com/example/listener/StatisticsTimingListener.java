package com.example.listener;

import com.example.controller.StatisticsController;
import com.example.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 项目启动监听数据中心定时器启动
 */
@WebListener
public class StatisticsTimingListener implements ServletContextListener {
    @Autowired(required = false)
    private StatisticsService statisticsService;


    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("数据中心定时器初始化加载！");
        statisticsService.statisticsTiming();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
