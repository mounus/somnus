package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.OriginDao;
import com.example.entiy.Origin;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.daysBetween;
import static com.example.util.NumberUtil.getGradeStates;

@Repository
public class OriginDaoImpl implements OriginDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public int save(Origin origin) {
        String sql = "insert into yx_origin(mid,source,institution_name,witness,witness_phone,other,introducer,introducer_phone) " +
                "values(?,?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, origin.getMid(), origin.getSource(), origin.getInstitution_name(), origin.getWitness(), origin.getWitness_phone(), origin.getOther(), origin.getIntroducer(), origin.getIntroducer_phone());
    }

    @Override
    public int update(Origin origin) {
        String sql = "update  yx_origin set source=?,institution_name=?,witness=?,witness_phone=?,other=?,introducer=?,introducer_phone=? where mid=?";

        return this.jdbcTemplate.update(sql, origin.getSource(), origin.getInstitution_name(), origin.getWitness(), origin.getWitness_phone(), origin.getOther(), origin.getIntroducer(), origin.getIntroducer_phone(), origin.getMid());
    }


    @Override
    public int delete(Integer mid) {
        String sql_m = "delete from yx_matorn where id=?";
        int m = this.jdbcTemplate.update(sql_m, mid);

        String sql_o = "delete from yx_origin where mid=?";
        int o = this.jdbcTemplate.update(sql_o, mid);

        String sql_c = "delete from yx_contact where mid=?";
        int c = this.jdbcTemplate.update(sql_c, mid);

        String sql_b = "delete from yx_bussiness where mid=?";
        int b = this.jdbcTemplate.update(sql_b, mid);

        String sql_p = "delete from yx_period where mid=?";
        int p = this.jdbcTemplate.update(sql_p, mid);

        return 1;
    }

    @Override
    public int deleteOrder(String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer cid = jsonObject.getInteger("cid");


        String sql_od = "delete from yx_order_demand where cid=?";
        int od = this.jdbcTemplate.update(sql_od, cid);

        String sql_o = "delete from yx_order where cid=?";
        int o = this.jdbcTemplate.update(sql_o, cid);

        if (od > 0 && o > 0) {
            return 1;
        } else {
            return 0;
        }
    }

@Transactional
    @SneakyThrows
    @Override
    public int updateTime(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String starttime = "2020-" + jsonObject.getString("start");
        Integer service_day = jsonObject.getInteger("day");
        Integer oid = jsonObject.getInteger("oid");
        Integer did = jsonObject.getInteger("did");

        String newEndtimendtime = getNewEndtime(starttime.toString(), -service_day);
        System.out.println();

        String sql = "update yx_order_demand set service_day=?,starttime=?,endtime=? where id=?";
        int state = this.jdbcTemplate.update(sql, service_day, starttime, newEndtimendtime, did);

        String sql_s = "select id,mid from yx_order where did=?";
        List<Map<String, Object>> idList = (List) this.jdbcTemplate.queryForList(sql_s, did);
        int state_o = 0;
        if (idList.size() > 1) {
            String sql_time = "select arrival_time,confirm_time  from yx_order where id=?";
            List<Map<String, Object>> timeList = this.jdbcTemplate.queryForList(sql_time, idList.get(0).get("id"));

            Integer day1 = daysBetween(timeList.get(0).get("arrival_time").toString(), timeList.get(0).get("confirm_time").toString());
            //第一个月嫂修改时间后，完成的天数
            System.out.println("day1 = " + day1);
            String sql_o = "update yx_order set arrival_time=?,order_day=? where id=?";
            state_o = this.jdbcTemplate.update(sql_o, starttime, day1, idList.get(0).get("id"));



            Integer day2 = daysBetween(starttime, timeList.get(0).get("confirm_time").toString());
              //原来的第一个月嫂完成的天数
            Integer addDay=day1-day2;
            //修改时间后，第一个月嫂与原来天数的比较
            System.out.println("addDay__________"+addDay);

            String sql_deamnd_oneorder="select oneorder from yx_order_demand where id=?";
            Integer oneorder=this.jdbcTemplate.queryForObject(sql_deamnd_oneorder,Integer.class,did);
            oneorder=oneorder-addDay;

          String sql_update_demand="update yx_order_demand set oneorder=? where id=?";
          int states_update_demand=this.jdbcTemplate.update(sql_update_demand,oneorder,did);

            String sql_b="select grade,day,trueday from yx_bussiness where mid=?";
            List<Map<String, Object>> dayList=this.jdbcTemplate.queryForList(sql_b,idList.get(0).get("mid"));

            Integer day=Integer.valueOf(dayList.get(0).get("day").toString())-addDay;
            Integer trueday=Integer.valueOf(dayList.get(0).get("trueday").toString())-addDay;

            Map<String, Object> map = new HashMap<String, Object>();
            map = getGradeStates(dayList.get(0).get("grade").toString(), day);
            String newgrade = map.get("grade").toString();
            Integer assess = Integer.valueOf(map.get("assess").toString());

            //月嫂的等级，申请状态
            String sql_bb = "update yx_bussiness set grade=?,day=?,trueday=?,assess=? where mid=?";
            int states_bb = this.jdbcTemplate.update(sql_bb, newgrade, day, trueday, assess, idList.get(0).get("mid"));


            //回岗时间的修改

            String sql_o1 = "update yx_order set return_time=?,order_day=? where id=?";
            int state_o1 = this.jdbcTemplate.update(sql_o1, newEndtimendtime,null, idList.get(idList.size() - 1).get("id"));

        } else {

            String sql_o = "update yx_order set arrival_time=?,return_time=? where id=?";
            state_o = this.jdbcTemplate.update(sql_o, starttime, newEndtimendtime, idList.get(0).get("id"));

        }


        if (state > 0 && state_o > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public List<Map<String, Object>> allmechanism() {
        String sql = "select mechanism_name as name from yx_user where power=30";

        List<Map<String, Object>> list = (List) this.jdbcTemplate.queryForList(sql);

        return list;
    }
}
