package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.EvaluateDao;

import com.example.entiy.Score;
import com.example.entiy.UsersEntity;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

import static com.example.util.Year.getCustomerName;

@Repository
public class EvaluateDaoImpl implements EvaluateDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;


    @Override
    public int save(String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        String customer_name = getCustomerName();
        String customer_photo = jsonObject.getString("customer_photo");
        String address = jsonObject.getString("address");
        Double praise = jsonObject.getDouble("praise");
        String label = jsonObject.getString("label");
        String evaluate_word = jsonObject.getString("evaluate_word");
        String evaluate_photo = jsonObject.getString("evaluate_photo");


        String sql = "insert into yx_evaluate(mid,customer_name,customer_photo,address,praise,label,evaluate_word,evaluate_photo)" +
                "values(?,?,?,?,?,?,?,?)";
        return this.jdbcTemplate.update(sql, mid, customer_name, customer_photo, address, praise, label, evaluate_word, evaluate_photo);
    }


    @Override
    public Map<String, Object> getById(Integer mid) {
        List<Map<String, Object>> list = null;
        NumberFormat ns = NumberFormat.getNumberInstance();
        ns.setMaximumFractionDigits(1);
        Map<String, Object> map = new HashMap<>();
        String sql = "select * from yx_evaluate where mid =? order by id desc";
        String sql_count = "select count(*) from yx_evaluate where mid= ?";
        String sql_mid = "select mid from yx_evaluate where mid =?";
        try {
            List<Integer> listmid = this.jdbcTemplate.queryForList(sql_mid.toString(), Integer.class, mid);

            if (mid.equals(listmid.get(0))) {
                Integer count = this.jdbcTemplate.queryForObject(sql_count.toString(), Integer.class, mid);
                map.put("count", count);
                String sql_sum = "select sum(praise) from yx_evaluate where mid=?";
                Double sum = this.jdbcTemplate.queryForObject(sql_sum.toString(), Double.class, mid);
                Double aa = sum / count;

                Double average = Double.parseDouble(ns.format(aa));

                map.put("average", average);
                list = this.jdbcTemplate.query(sql.toString(), new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        mp.put("id", rs.getInt("id"));
                        mp.put("mid", rs.getInt("mid"));
                        mp.put("customer_name", rs.getString("customer_name"));
                        mp.put("address", rs.getString("address"));
                        mp.put("praise", rs.getString("praise"));
                        String label = rs.getString("label");

                        mp.put("evaluate_word", rs.getString("evaluate_word"));
                        String evaluate_photo = rs.getString("evaluate_photo");
                        if (label != null && label != "") {
                            mp.put("label", JSONArray.fromObject(label));
                        } else {
                            mp.put("label", null);
                        }
                        if (evaluate_photo != null && evaluate_photo != "") {
                            mp.put("evaluate_photo", JSONArray.fromObject(evaluate_photo));
                        } else {
                            mp.put("evaluate_photo", null);
                        }
                        return mp;
                    }
                }, mid);
                map.put("list", list);
                return map;

            } else {
                return map;
            }
        } catch (Exception e) {
            return map;
        }

    }

    @Override
    public int deleteEvaluate(String json) {
        JSONObject jsonObject=JSON.parseObject(json);
        Integer id=jsonObject.getInteger("id");
        String sql="delete from yx_evaluate where id=? ";
        int states=this.jdbcTemplate.update(sql,id);
        if (states>0){
            return 1;
        }else {
            return 0;
        }

    }

    @Override
    public List<String> getComment() {

        String sql = "select comment from yx_template";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<String> newList = new ArrayList<String>();
        list = this.jdbcTemplate.queryForList(sql);

        for (int i = 0; i < list.size(); i++) {
            newList.add(list.get(i).get("comment").toString());
        }

        return newList;

    }

    @Override
    public int saveScore(Score score) {

        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_score (mid,background,appearance,communicate,nurse,breast_fed,confinement,postpartum,intelligence,psychology,know,total,comment)");
        sb.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?)");

        int sates = this.jdbcTemplate.update(sb.toString(), score.getMid(), score.getBackground(), score.getAppearance(), score.getCommunicate(), score.getNurse(), score.getBreast_fed(), score.getConfinement(),
                score.getPostpartum(), score.getIntelligence(), score.getPsychology(), score.getKnow(), score.getTotal(), score.getComment());
        if (sates > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public Score getScoreById(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        Score score = new Score();
        String sql_mid = "select mid from yx_score where mid =?";
        String sql_a = "select *  from yx_score where mid=? ";
        try {
            List<Integer> newmid = this.jdbcTemplate.queryForList(sql_mid, Integer.class, mid);

            if (mid.equals(newmid.get(0))) {

                List<Score> list = jdbcTemplate.query(sql_a, new Object[]{mid}, new BeanPropertyRowMapper(Score.class));
                score = list.get(0);

            } else {

            }

        } catch (Exception e) {
            return score;
        }

        return score;
    }

    @Override
    public int update(Score score) {

         String sql="update yx_score set background=?,appearance=?,communicate=?,nurse=?,breast_fed=?,confinement=?,postpartum=?,intelligence=?,psychology=?,know=?,total=?,comment=? where mid=?";
         int state=this.jdbcTemplate.update(sql,score.getBackground(),score.getAppearance(),score.getCommunicate(),score.getNurse(),score.getBreast_fed(),
                 score.getConfinement(),score.getPostpartum(),score.getIntelligence(),score.getPostpartum(),score.getKnow(),score.getTotal(),score.getComment(),score.getMid());
            if (state>0){
                return 1;
            }else {
                return 0;
            }


    }
}