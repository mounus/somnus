package com.example.dao.impl;

import com.example.dao.TimeDao;
import groovy.util.IFileNameFinder;
import lombok.SneakyThrows;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class TimeDaoImpl implements TimeDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public void matronShelfTime() {
        SimpleDateFormat timeS = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        // 一天的毫秒数
        // long daySpan = 24 * 60 * 60 * 1000;

        //1天
        long daySpan = 1 * 24 * 60 * 60 * 1000;
        // 规定的每天时间02:00:00运行
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 0:30:00");
        // 首次运行时间
        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
            // 如果今天的已经过了 首次运行时间就改为明天
            if (System.currentTimeMillis() > startTime.getTime()) {
                startTime = new Date(startTime.getTime() + daySpan);
            }
            Timer t = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("定时器执行");
                    String nowTime = timeS.format(new Date());

                    //修改月嫂上下架状态
                    String sql_update = "update yx_bussiness set shelf=? where mid=?";

                    StringBuffer sb = new StringBuffer();
                    sb.append("select m.id,p.period,b.isorder from yx_matorn m left join yx_bussiness b on (m.id=b.mid)");
                    sb.append(" left join yx_period p on (m.id=p.mid) where m.idtype=1 ");

                    List list = null;
                    list = jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                        @SneakyThrows
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                            Map<String, Object> mp = new HashedMap();
                            Integer mid = rs.getInt("id");

                            String month = rs.getString("month");

                            if (rs.getInt("isorder") == 1) {
                                //月嫂服务中不做处理
                            } else {

                                //判断月嫂是否上架
                                Integer shelf = 0;
                                if (shelf == 0) {
                                    int states = jdbcTemplate.update(sql_update, 0, mid);
                                }
                                if (shelf == 1) {
                                    int states = jdbcTemplate.update(sql_update, 1, mid);
                                }

                            }
                            return mp;
                        }
                    });

                }
            };
            // 以每1天执行一次
            t.schedule(task, startTime, daySpan);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void oldCreatMatronTime() {

    }

    @Override
    public void newCreatMatronTime() {

    }

    @Override
    public void serviceMatronTime() {
        SimpleDateFormat timeS = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        // 一天的毫秒数
        // long daySpan = 24 * 60 * 60 * 1000;

        //1天
        long daySpan = 1 * 24 * 60 * 60 * 1000;
        // 规定的每天时间02:00:00运行
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 0:30:00");
        // 首次运行时间
        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
            // 如果今天的已经过了 首次运行时间就改为明天
            if (System.currentTimeMillis() > startTime.getTime()) {
                startTime = new Date(startTime.getTime() + daySpan);
            }
            Timer t = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("定时器执行");
                    String nowTime = timeS.format(new Date());

                    //修改经理人
                    String sql_updateAgent="update yx_matorn set uid=? where id=?";


                    String sql_id="select id from yx_user where id ";


                    StringBuffer sb = new StringBuffer();
                    sb.append("select od.mid,od.service_type from yx_custom c left join yx_order_demand od on (c.id=od.cid) ");
                    sb.append(" left join yx_order o on (c.id=o.cid) ");
                    sb.append(" where o.order_states=2 ");
                    List list = null;
                    list = jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                        @SneakyThrows
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                            Map<String, Object> mp = new HashedMap();
                            Integer mid = rs.getInt("id");


                            Integer power=0;
                            if (power==0){
                                 //月嫂所属的经理人已经是  服务中所要分配的经理人

                            }else {
                                //第一次分配对应的经理人
                                if (rs.getString("service_type").equals("月子会所服务")) {

                                    Integer uid=0;
                                    int states_updateAgent=jdbcTemplate.update(sql_updateAgent,uid,mid);
                                }
                                if (rs.getString("service_type").equals("居家服务")) {

                                }
                            }




                            return mp;
                        }
                    });

                }
            };
            // 以每1天执行一次
            t.schedule(task, startTime, daySpan);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
