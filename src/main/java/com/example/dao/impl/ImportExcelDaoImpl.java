package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.ImportExcelDao;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import sun.applet.resources.MsgAppletViewer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.util.MonthUtil.timeSize;
import static com.example.util.TimeUtil.parseDate;

@Repository
public class ImportExcelDaoImpl implements ImportExcelDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;


    @SneakyThrows
    @Override
    public Map<String, Object> importExcelCustomTemplate(String json) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_custom(bid,sid,origin,channel,c_name,c_phone,production_situation,nodate,production_date,production_mode,baby_sex,creat_time,update_time,integral,isPartner,isIntention)");
        sb.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        JSONObject jsonObject = JSON.parseObject(json);
        String stringList = jsonObject.getString("list");
        System.out.println("list = " + stringList);
        Map<String, Object> map = new HashedMap();
        List<Map<String, Object>> list = new ArrayList<>();

        JSONArray jsonArray = JSONArray.fromObject(stringList);
        list = (List) jsonArray;
        System.out.println("list.size() = " + list.size());
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time1.format(new Date());
        System.out.println("list = " + list);

        String sql_bid = "select id from yx_user where name=? and power=4";
        String sql_sid = "select id from yx_user where name=? and power=7";
        Integer success = 0;//成功条数
        Integer fail = 0;//失败条数
        Integer isAdd=0;//是否能添加

        List<Integer> failList=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {

            Integer production_situation = 0;
            String nodate = null;
            String production_date = null;
            if (list.get(i).get("time") == null || list.get(i).get("time") == ""||list.get(i).get("time").toString().isEmpty()) {

                isAdd=1;
            } else {
                System.out.println("list.get(i).get(\"time\") = " + list.get(i).get("time"));
                System.out.println("parseDate(list.get(i).get(\"time\").toString()) = " + parseDate(list.get(i).get("time").toString()));
                if (parseDate(list.get(i).get("time").toString())!=null){
                    //是时间格式
                    isAdd=1;
                    String dateString=parseDate(list.get(i).get("time").toString());
                    if (timeSize(nowTime, dateString) == 1) {
                        production_situation = 0;
                        nodate = dateString;
                    } else {
                        production_situation = 1;
                        production_date = dateString;
                    }
                }else {
                    //不是时间格式
                    isAdd=0;
                }


            }
            String origin = null;
            if (list.get(i).get("origin") == ""||list.get(i).get("origin").toString().isEmpty()) {

            } else {
                origin = list.get(i).get("origin").toString();
            }

            String channel = null;
            if (list.get(i).get("channel") == ""||list.get(i).get("channel").toString().isEmpty()) {

            } else {
                channel = list.get(i).get("channel").toString();
            }

            String c_name = null;
            if (list.get(i).get("c_name") == ""||list.get(i).get("c_name").toString().isEmpty()) {

            } else {
                c_name = list.get(i).get("c_name").toString();
            }
            String c_phone = null;
            if (list.get(i).get("c_phone") == ""||list.get(i).get("c_phone").toString().isEmpty()) {

            } else {
                c_phone = list.get(i).get("c_phone").toString();
            }



            Integer bid = 0;
            Integer sid = 0;
            if (list.get(i).get("bname") == null || list.get(i).get("bname") == ""|| list.get(i).get("bname").toString().isEmpty()) {
                isAdd=1;
            } else {
                try {
                    bid = jdbcTemplate.queryForObject(sql_bid, Integer.class, list.get(i).get("bname"));
                    isAdd=1;
                } catch (Exception e) {
                    isAdd=0;
                }
            }
            if (list.get(i).get("sname") == null || list.get(i).get("sname") == ""|| list.get(i).get("sname").toString().isEmpty()) {
                isAdd=1;

            } else {
                try {

                    sid = jdbcTemplate.queryForObject(sql_sid, Integer.class, list.get(i).get("sname"));
                    isAdd=1;
                } catch (Exception e) {
                    isAdd=0;
                }
            }


            if (isAdd == 0) {
                    fail=fail+1;
                   // failList.add(i+2);

            } else {
                //没问题
            int   states=  this.jdbcTemplate.update(sb.toString(), bid, sid, origin, channel, c_name, c_phone, production_situation, nodate, production_date, null, null, nowTime, nowTime, 0, 0, 1);
              success = success + states;
            }

        }

        if (success == list.size()) {
            // 全部添加成功
            map.put("code", 1);
            map.put("success", success);
            map.put("fail", fail);
        } else {
            //添加失败
            map.put("code", 0);
            map.put("success", success);
            map.put("fail", fail);
           // map.put("failList", failList);
        }

        return map;

    }


}
