package com.example.dao.impl;

import com.example.dao.UsersEntityDao;
import com.example.entiy.UsersEntity;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.sd.oo.getStopDay;
import static com.example.util.MonthUtil.*;


@Repository
public class UsersEntityDaoIml implements UsersEntityDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<UsersEntity> usersList() {
        List<UsersEntity> list = jdbcTemplate.query("select * from user_manage", new Object[]{}, new BeanPropertyRowMapper(UsersEntity.class));
        return list;
    }

    @Override
    public UsersEntity findUserOne(String name) {
        List<UsersEntity> list = jdbcTemplate.query("select * from user_manage where user_name = ?", new Object[]{name}, new BeanPropertyRowMapper(UsersEntity.class));
        if (list != null && list.size() > 0) {
            UsersEntity usersEntity = list.get(0);
            return usersEntity;
        } else {
            return null;
        }
    }

    @Override
    public void saveUser(UsersEntity usersEntity) {
        jdbcTemplate.update("insert into user_manage(user_name, password, telPhone) values(?, ?, ?)",
                usersEntity.getUserName(), usersEntity.getPassword(), usersEntity.getTelPhone());
    }

    @Override
    public void updateUser(UsersEntity usersEntity) {
        jdbcTemplate.update("UPDATE user_manage SET password=?, telPhone=? WHERE user_name=?",
                usersEntity.getPassword(), usersEntity.getTelPhone(), usersEntity.getUserName());
    }

    @Override
    public void removeUser(String name) {
        jdbcTemplate.update("DELETE FROM user_manage WHERE user_name = ?", name);
    }

    @Override
    public int test() {

//        System.out.println("ssssssssssssssss = ");
//        //其他订单类型计算语句
//        StringBuffer sb_price = new StringBuffer();
//        sb_price.append(" select c.id,od.work_states,od.starttime,od.endtime,od.onePrice,od.timetype,od.price,od.service_day");
//        sb_price.append("  from  yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");
//
//
//        sb_price.append(" and c.id=2432 ");
//        sb_price.append("  and od.service_type='居家服务' and od.work_states>=0  and od.service_day<>0 ");
//
//
//        List<String> queryList = new ArrayList<>();
//
//        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//        //当前时间
//        String nowTime = time.format(new Date());
//        String monthFirst = "2020-12-01";
//        String monthLast = "2020-12-10";
////        String monthFirst = "2020-11-30";
////        String monthLast = "2020-11-30";
//        Integer type = 1;
//        String firstDay = getFirstDay(monthFirst);//搜索时间这个月第一天
//        String lastDay = getLastDay(monthFirst);//搜索时间这个月最后一天
//        if (type == 0) {
//            type=0;
//            sb_price.append(" and (  ( od.starttime=? ) or ( od.endtime=? ) or ( od.starttime<? and od.endtime>?)   )");
//            queryList.add(monthFirst);
//            queryList.add(monthFirst);
//            queryList.add(monthFirst);
//            queryList.add(monthFirst);
//        }
//        if (type == 1) {
//            type=1;
//            sb_price.append(" and (  (od.starttime>=? and od.starttime<=?) or (od.endtime>=? and od.endtime<=?)  or (?>od.starttime and od.endtime>?)  ) ");
//            queryList.add(monthFirst);
//            queryList.add(monthLast);
//            queryList.add(monthFirst);
//            queryList.add(monthLast);
//            queryList.add(firstDay);
//            queryList.add(lastDay);
//        }
//
//
//        sb_price.append(" order by c.id desc limit 0,70");
//        System.out.println("sb_price = " + sb_price);
//
//        Integer finalType = type;
//        List<Map<String, Object>> sql_homeList = this.jdbcTemplate.query(sb_price.toString(), new RowMapper<Map<String, Object>>() {
//            @SneakyThrows
//            @Override
//            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                Map<String, Object> mp = new HashMap<String, Object>();
//                String start_time = rs.getString("starttime");
//                String end_time = rs.getString("endtime");
//
//                Integer onePrice = 0;//单价
//                if (rs.getInt("onePrice") != 0) {
//                    onePrice = rs.getInt("onePrice");
//                } else {
//                    onePrice = rs.getInt("price") / rs.getInt("service_day");
//                }
//                Integer workStates = rs.getInt("work_states");
//                Map<String, Object> priceMap = new HashMap<>();
//
//
//                //暂停时间
//                String sql_time = "select stop_time,run_time from yx_order where cid=? and stop_time is not null ";
//                List<Map<String, Object>> timeList = jdbcTemplate.queryForList(sql_time, rs.getInt("id"));
//
//                System.out.println("rs.getInt(\"id\") = " + rs.getInt("id"));
//                //时间段搜索，中间包含暂停天数
//
//                for (int i = 0; i < timeList.size(); i++) {
//                    Object stop_time = timeList.get(i).get("stop_time");
//                    Object run_time = timeList.get(i).get("run_time");
//                    if (stop_time != null && run_time != null) {
//
//                    }
//                    if (stop_time != null && run_time == null){
//
//                    }
//                    if (stop_time == null && run_time == null){
//
//                    }
//                }
//
//
//                Integer sum_stopDay=0;
//                if (finalType == 1) {
//                    //     时间段搜索
//                    priceMap = homePrice(workStates, onePrice, start_time,  end_time, monthFirst, monthLast, nowTime);
//                    sum_stopDay=getStopDay(timeList,monthFirst,firstDay,lastDay);
//                    System.out.println("sum_stopDay = " + sum_stopDay);
//                    System.out.println("priceMap = " + priceMap);
//                }
//                if (finalType == 0) {
//                    //是某一天（默认）
//
//                    priceMap = homePriceOneDay(workStates, onePrice, start_time, monthFirst);
//                }
//                System.out.println("priceMap.get(\"orderDay\") = " + priceMap.get("orderDay"));
//                Integer orderDay = Integer.valueOf(priceMap.get("orderDay").toString()) - sum_stopDay;
//                System.out.println("orderDay = " + orderDay);
//                mp.put("orderDay", priceMap.get("orderDay"));
//                mp.put("truePrice", priceMap.get("truePrice"));
//
//                return mp;
//            }
//
//        }, queryList.toArray());


        return 0;
    }
}
