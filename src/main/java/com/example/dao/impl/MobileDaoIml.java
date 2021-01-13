package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.MobileDao;
import com.example.entiy.Matorn;
import com.example.entiy.Reservation;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.BASE64.decodeURIComponent;
import static com.example.util.BASE64.decryptBASE64;
import static com.example.util.PeriodUtil.getNewStart;

@Repository
public class MobileDaoIml implements MobileDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @SneakyThrows
    @Override
    public int save(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String name = jsonObject.getString("name");
        String phone = jsonObject.getString("phone");
        Integer production_situation = jsonObject.getInteger("production_situation");
        String production_date = jsonObject.getString("production_date");
        String base = jsonObject.getString("base");
        String hospital = jsonObject.getString("hospital");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = sdf.format(new Date());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        Date now = sdf1.parse(time);
        Date start_ = sdf1.parse(production_date);
        if (now.before(start_)) {
            //表示now小于start_
            production_situation = 0;
        } else {
            production_situation = 1;
        }

        StringBuffer sb = new StringBuffer();
        //解密base
        String newBase = decodeURIComponent(base).replace("-", "+");

        byte[] byteArray = decryptBASE64(newBase);
        String origin = new String(byteArray, "utf-8");


        sb.append("insert into yx_reservation (name,phone,production_situation,production_date,time,origin,hospital)");
        sb.append("value(?,?,?,?,?,?,?)");

        String sql_phone = "select count(*) from yx_reservation where phone=? ";

        Integer count = this.jdbcTemplate.queryForObject(sql_phone, Integer.class, phone);

        if (count > 0) {
            return -1;
        } else {
            int states = this.jdbcTemplate.update(sb.toString(), name, phone, production_situation, production_date, time, origin, hospital);
            if (states > 0) {
                return 1;
            } else {
                return 0;
            }
        }

    }

    @Override
    public List<Reservation> reservationList() {
        List<Reservation> list = jdbcTemplate.query("select * from yx_reservation GROUP BY time desc ", new Object[]{}, new BeanPropertyRowMapper(Reservation.class));
        return list;
    }


    @Override
    public Map<String, Object> channel() {
        Map<String, Object> map = new HashMap<String, Object>();

        String sql1 = "select channel from yx_channel where type=2";
        List<Map<String, Object>> channel = new ArrayList<Map<String, Object>>();
        channel = (List) this.jdbcTemplate.queryForList(sql1.toString());


        List<Integer> namelist = new ArrayList<Integer>();
        for (int i = 1; i < channel.size(); i++) {
            namelist.add(Integer.valueOf(channel.get(i).get("channel").toString()));
        }

        String sql_channel = "select  name,fullwrite from yx_channel where channel=?";
        List<Map<String, Object>> channellist = new ArrayList<Map<String, Object>>();
        List<List> UnderlineList = new ArrayList<List>();

        for (int i = 0; i < namelist.size(); i++) {
            channellist = (List) this.jdbcTemplate.queryForList(sql_channel, namelist.get(i));
            UnderlineList.add(channellist);
        }
        map.put("UnderlineList", UnderlineList);
        return map;
    }
}
