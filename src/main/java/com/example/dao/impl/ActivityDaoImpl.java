package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.ActivityDao;
import com.example.entiy.Reservation;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.net.URLDecoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.example.util.BASE64.decryptBASE64;
import static com.example.util.BASE64.encryptBASE64;
import static com.example.util.PageUtil.getAllPage;

@Repository
public class ActivityDaoImpl implements ActivityDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @SneakyThrows
    @Override
    public Map<String, Object> getAllReservation(String json) {

        JSONObject jsonObject = JSON.parseObject(json);
        Integer start = jsonObject.getInteger("start");
        Integer uid = jsonObject.getInteger("uid");

        String sql = "select power,mechanism_name from yx_user where id=?";

        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        list1 = this.jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("power", rs.getInt("power"));
                mp.put("mechanism_name", rs.getString("mechanism_name"));
                return mp;
            }
        }, uid);
        String mechanism_name = list1.get(0).get("mechanism_name").toString();
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from yx_reservation where 1=1");

        String sql_count = null;
        int count = 0;
        List<Reservation> list =null;
        if (list1.get(0).get("power").toString().equals("100")) {
            sb.append(" order by id desc");
            sql_count = "select count(*) from yx_reservation";
            count = this.jdbcTemplate.queryForObject(sql_count, Integer.class);
            list = jdbcTemplate.query(sb.toString(), new Object[]{}, new BeanPropertyRowMapper(Reservation.class));
        } else {
            sb.append("  and origin=?");
            sb.append(" order by id desc");
            sql_count = "select count(*) from yx_reservation where origin=?";
            count = this.jdbcTemplate.queryForObject(sql_count, Integer.class, mechanism_name);
           list = jdbcTemplate.query(sb.toString(), new Object[]{mechanism_name}, new BeanPropertyRowMapper(Reservation.class));
        }

        map.put("count", count);
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        map.put("num", page.get("num"));
        String sql_page = page.get("sql_page").toString();
        map.put("page", page.get("page"));
        sb.append(sql_page);
        map.put("list", list);

        return map;
    }

    @SneakyThrows
    @Override
    public Map<String, Object> getAllHref(String json) {

        JSONObject jsonObject = JSON.parseObject(json);
        Integer start = jsonObject.getInteger("start");
        Map<String, Object> map = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        sb.append("select * from yx_origin_base order by id desc");

        String sql_count = "select count(*) from yx_origin_base";
        //获取总条数
        int count = this.jdbcTemplate.queryForObject(sql_count, Integer.class);
        map.put("count", count);
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        map.put("num", page.get("num"));
        String sql_page = page.get("sql_page").toString();
        map.put("page", page.get("page"));
        sb.append(sql_page);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("id", rs.getInt("id"));
                mp.put("origin_name", rs.getString("origin_name"));
                mp.put("base", rs.getString("base"));
                mp.put("base_href", rs.getString("base_href"));
                return mp;
            }
        });
        map.put("list", list);
        return map;
    }

    @SneakyThrows
    @Override
    public int addHref(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String origin_name = jsonObject.getString("origin_name");
        String base = Base64.getEncoder().encodeToString(origin_name.getBytes("utf-8"));

        String newBase = base.replace("+", "-");
        String base_href = "https://www.m.haoyuexiang.com/#/index/bianjieyuyue?base=" + newBase;
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_origin_base(origin_name,base,base_href) ");
        sb.append("value(?,?,?)");
        int states = this.jdbcTemplate.update(sb.toString(),origin_name , newBase, base_href.toString());

        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
