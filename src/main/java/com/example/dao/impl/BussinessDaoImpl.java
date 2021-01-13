package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.BussinessDao;
import com.example.entiy.Bussiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;


import static com.example.util.NumberUtil.getNumber;
import static com.example.util.PeriodUtil.getSixMonth;
import static com.example.util.PeriodUtil.getSixPeriod;

@Repository
public class BussinessDaoImpl implements BussinessDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public int update(String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        Integer mid = jsonObject.getInteger("mid");
        String photo = jsonObject.getString("photo");
        String character = jsonObject.getString("character");
        String strength = jsonObject.getString("strength");
        String work_age = jsonObject.getString("work_age");
        Integer num = jsonObject.getInteger("num");
        String works = jsonObject.getString("works");
        String trains = jsonObject.getString("trains");
        String qualification = jsonObject.getString("qualification");

        String identity = jsonObject.getString("identity");
        String heathly = jsonObject.getString("heathly");
        String sql = "update  yx_bussiness set photo =?,charact=?,strength=?,work_age=?,num=?,works=?,trains=?,qualification=?,identity=?,heathly=? where mid=?";

        return this.jdbcTemplate.update(sql, photo, character, strength, work_age, num, works, trains, qualification, identity, heathly, mid);
    }

    @Transactional
    public int save(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creat_time = time.format(new Date());

        Integer mid = jsonObject.getInteger("mid");
        String photo = jsonObject.getString("photo");
        String character = jsonObject.getString("character");
        String strength = jsonObject.getString("strength");
        String work_age = jsonObject.getString("work_age");
        Integer num = jsonObject.getInteger("num");
        String works = jsonObject.getString("works");
        String trains = jsonObject.getString("trains");
        String qualification = jsonObject.getString("qualification");
        String identity = jsonObject.getString("identity");
        String heathly = jsonObject.getString("heathly");

         String grade = "N";//默认级别
        String number = getNumber(mid);
        String sql = "insert into yx_bussiness(mid,photo,charact,strength,work_age,num,works,trains,qualification,grade,creat_time,number,day,isorder,isquit,assess,isblack,shelf,identity,heathly) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        int states = this.jdbcTemplate.update(sql, mid, photo, character, strength, work_age, num, works, trains, qualification, grade, creat_time, number, 0, 0, 0, 1, 0, 0, identity, heathly);
        String sql1 = "insert into yx_period(mid,period,month,rest) values(?,?,?,?)";
        String period=jsonObject.getString("period");
        String month = jsonObject.getString("month");
//        String period = getSixPeriod();
//        String month = getSixMonth();

        String rest = "[]";
        int states1 = this.jdbcTemplate.update(sql1, mid, period, month, rest);
        String sql_idtype = "update yx_matorn set idtype= 1 where id=? ";
        int states2 = this.jdbcTemplate.update(sql_idtype, mid);

        if (states > 0 && states1 > 0 && states2 > 0) {
            return 1;

        } else {
            return 0;
        }


    }

    @Override
    public int updateAssess(Integer id) {

        String sql = "update  yx_bussiness set assess=1 ,update_time= ?  where mid=?";
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String update_time = time.format(new Date());

        return this.jdbcTemplate.update(sql, update_time, id);
    }

    @Override
    public int updateShelf(Integer id, Integer shelf) {
        String sql = "update  yx_bussiness set shelf=? where mid=?";

        return this.jdbcTemplate.update(sql, shelf, id);
    }

    @Override
    public int updateIsblack(Integer id, Integer isblack) {
        String sql = "update  yx_bussiness set isblack=? where mid=?";

        return this.jdbcTemplate.update(sql, isblack, id);

    }
}