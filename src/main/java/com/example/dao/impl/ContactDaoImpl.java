package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.ContactDao;
import com.example.entiy.Contact;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ContactDaoImpl implements ContactDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Transactional
    @Override
    public int save(Contact contact) {
        String sql = "insert into yx_contact(mid,phone,wechat,bank_card,bank_name,emergency_person,emergency_phone) " +
                "values(?,?,?,?,?,?,?)";
        return jdbcTemplate.update(sql, contact.getMid(), contact.getPhone(), contact.getWechat(), contact.getBank_card(), contact.getBank_name(), contact.getEmergency_person(), contact.getEmergency_phone());
    }

    @Override
    public int update(Contact contact) {
        String sql = "update  yx_contact set phone =?,wechat=?,bank_card=?,bank_name=?,emergency_person=?,emergency_phone=? where mid=?";
        return jdbcTemplate.update(sql, contact.getPhone(), contact.getWechat(), contact.getBank_card(), contact.getBank_name(), contact.getEmergency_person(), contact.getEmergency_phone(), contact.getMid());

    }

    @Override
    public List<Integer> outExcel(String somnus, String agent, String grade) {
       // JSONObject jsonObject = JSON.parseObject(json);
//        String somnus=jsonObject.getString("somnus");
//        String grade=jsonObject.getString("grade");
//        String agent=jsonObject.getString("agent");


        List<Object> queryList = new ArrayList<Object>();



        StringBuffer sb = new StringBuffer();
        sb.append("select yx_matorn.id from yx_matorn LEFT JOIN yx_contact  on(yx_matorn.id=yx_contact.mid) LEFT JOIN yx_bussiness  on(yx_matorn.id=yx_bussiness.mid) ");
        sb.append("LEFT JOIN yx_period  on (yx_matorn.id=yx_period.mid) where yx_bussiness.isorder =0 ");

        if (grade != null && grade != "" && !grade.trim().equals("")) {
            sb.append(" and yx_bussiness.grade=?");
            queryList.add(grade);
        } else {

        }

        if (agent != null && agent != "" && !agent.trim().equals("")) {
            String sql_uid = "select id from yx_user where name=?";
            Integer uid = this.jdbcTemplate.queryForObject(sql_uid, Integer.class, agent);
            if(uid!=22){
                sb.append(" and yx_matorn.uid=?   ");
                queryList.add(uid);
            }else {
                sb.append("  and (yx_matorn.uid=0 or yx_matorn.uid=22) ");
            }

        } else {

        }

        if (somnus.equals("all")) {
            sb.append("  and yx_bussiness.isblack =0 order by yx_bussiness.creat_time desc ");
        }
        if (somnus.equals("byGrade")) {
            sb.append("and yx_bussiness.isblack =0  and yx_bussiness.isquit=0 and yx_bussiness.assess=1  and yx_bussiness.grade='待定级' order by yx_bussiness.update_time desc");
        }
        if (somnus.equals(("ding"))) {
            sb.append(" and yx_bussiness.isblack =0  and yx_bussiness.isquit=0  and yx_bussiness.assess=2  order by yx_bussiness.update_time desc");
        }
        if (somnus.equals(("allGrade"))) {
            sb.append(" and yx_bussiness.isblack =0  and yx_bussiness.isquit=0  and yx_bussiness.assess=1 and yx_bussiness.grade<>'待定级' order by yx_bussiness.update_time desc");
        }

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = (List) this.jdbcTemplate.queryForList(sb.toString(), queryList.toArray());
        List<Integer> listmid = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            listmid.add(Integer.valueOf(list.get(i).get("id").toString()));
        }

        return listmid;
    }


    @Override
    public List<Map<String, Object>> outExcelById(Integer id) {

        StringBuffer sb = new StringBuffer();
        sb.append("select  yx_matorn.uid,yx_matorn.lid,yx_matorn.name,yx_contact.phone,yx_bussiness.grade,yx_bussiness.number,yx_bussiness.trueday,yx_bussiness.creat_time ");
        sb.append(" from yx_matorn LEFT JOIN yx_contact  on(yx_matorn.id=yx_contact.mid)LEFT JOIN yx_bussiness  on(yx_matorn.id=yx_bussiness.mid)");
        sb.append(" LEFT JOIN yx_period  on (yx_matorn.id=yx_period.mid) where yx_matorn.id=? ");
        List list = null;
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("name", rs.getString("name"));
                mp.put("number", rs.getString("number"));
                mp.put("phone", rs.getString("phone"));
                mp.put("grade", rs.getString("grade"));
                String day = null;
                if (rs.getString("trueday") != null) {
                    day = rs.getString("trueday");
                } else {
                    day = "0";
                }
                mp.put("trueday", day);
                Integer uid = rs.getInt("uid");
                String sql_agent = "select name from yx_user where id=?";
                String agent = jdbcTemplate.queryForObject(sql_agent, String.class, uid);
                mp.put("agent", agent);
                mp.put("creattime", rs.getString("creat_time"));
                Integer lid = rs.getInt("lid");
                if (lid!=0){
                    String sql_lid = "select name from yx_user where id=?";
                    String login = jdbcTemplate.queryForObject(sql_lid, String.class, lid);
                    mp.put("login", login);
                }else {
                    mp.put("login", "自助填写");
                }

                return mp;
            }
        }, id);
        return list;
    }
}
