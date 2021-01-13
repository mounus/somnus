package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.JobDao;
import com.example.entiy.Job_evaluate;
import com.example.entiy.Score;
import groovy.util.IFileNameFinder;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.ELState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getDays;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.timeSize;
import static com.example.util.PriceUtil.*;


@Repository
public class JobDaoImpl implements JobDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public  Map<String, Object> getById(Integer mid) {


        String sql = "select yx_matorn.id,yx_matorn.name,yx_matorn.born,yx_matorn.idcard,yx_bussiness.number,yx_bussiness.photo from yx_matorn LEFT JOIN yx_bussiness on (yx_matorn.id=yx_bussiness.mid) where yx_matorn.id=?";

        List<Map<String, Object>> list = null;

        list = this.jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("mid", rs.getInt("id"));
                mp.put("name", rs.getString("name"));
                mp.put("born", rs.getString("born"));
                mp.put("idcard", rs.getString("idcard"));
                mp.put("photo", rs.getString("photo"));
                String number=rs.getString("number");
                SimpleDateFormat time = new SimpleDateFormat("yyyyMMdd");//设置日期格式
                String creat_time = time.format(new Date());

                String new_number=number.substring(0,3)+creat_time+number.substring(3,8);

                mp.put("number",new_number);
                return mp;
            }
        }, mid);

        return list.get(0);
    }


    @Override
    public int save(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        String name = jsonObject.getString("name");
        String born = jsonObject.getString("born");
        String idcard = jsonObject.getString("idcard");
        String photo = jsonObject.getString("photo");
        String number = jsonObject.getString("number");
        String alllist = jsonObject.getString("alllist");

        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_job_evaluate(mid,sex,name,born,idcard,photo,number,alllist)");
        sb.append("values(?,?,?,?,?,?,?,?)");
        int states = this.jdbcTemplate.update(sb.toString(), mid, 0, name, born, idcard, photo, number, alllist);
        String sql = "update yx_bussiness set isevaluate=? where mid=?";
        int states_b = this.jdbcTemplate.update(sql, 1, mid);
        if (states > 0 && states_b > 0) {
            return 1;
        } else {
            return 0;
        }
    }




    @Override
    public  Map<String, Object> findOne(Integer mid) {


        StringBuffer sb=new StringBuffer();
        sb.append(" select m.id,m.name,m.born,m.idcard,b.photo,b.number,j.alllist from yx_matorn m ");
        sb.append(" left join yx_bussiness b on (m.id=b.id) left join yx_job_evaluate j on (m.id=j.mid)");
        sb.append(" where m.id=?");
        List<Map<String, Object>> list = null;
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("mid", rs.getInt("id"));
                mp.put("name", rs.getString("name"));
                mp.put("born", rs.getString("born"));
                mp.put("idcard", rs.getString("idcard"));
                mp.put("photo", rs.getString("photo"));
                mp.put("number", rs.getString("number"));
                mp.put("alllist",JSONArray.fromObject(rs.getString("alllist")));
                return mp;
            }
        }, mid);

      Map<String, Object> map = null;
        if (list.size()>0){
            map=list.get(0);
        }else {
            map=null;
        }

         return map;

    }

    @Override
    public int update(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        String alllist = jsonObject.getString("alllist");
        Integer mid = jsonObject.getInteger("mid");

        StringBuffer sb = new StringBuffer();
        sb.append("update yx_job_evaluate set alllist=? where mid=?");
        int states = this.jdbcTemplate.update(sb.toString(), alllist, mid);

        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }


    public Map<String, Object> test(String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String monthFirst=jsonObject.getString("monthFirst");
        String monthLast=jsonObject.getString("monthLast");
        Integer did=jsonObject.getInteger("did");
        StringBuffer sb=new StringBuffer();
        sb.append("select c.id,od.id as did,c.c_name,c.bid,c.origin,c.channel,od.service_type,od.timetype,od.level,od.work_states, ");
        sb.append("od.price,c.creat_time,od.service_day,od.starttime,od.endtime,od.onePrice");
        sb.append(" from yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");


        StringBuffer sb_price = new StringBuffer();
        sb_price.append(" select c.id,od.work_states,od.starttime,od.endtime,od.onePrice,od.timetype,od.price,od.service_day");
        sb_price.append("  from  yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");

        StringBuffer sb_public=new StringBuffer();
        sb_public.append("  and od.service_type='居家服务'  ");
        sb_public.append(" and od.work_states>=0  and od.service_day<>0 ");

        sb.append(sb_public);
        sb_price.append(sb_public);

        //时间段搜索
        String sql_days=" and  (   (od.starttime>=?  and od.starttime<=?) or (od.endtime>=? and od.endtime<=?) or (od.starttime<=? and od.endtime>=?) and od.id=?  ) ";
        sb.append(sql_days);
        sb_price.append(sql_days);
      

        sb.append("   order by c.creat_time desc ");
           //2991  2914
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowTime = time.format(new Date());

        List<String> dayList = getDays(monthFirst, monthLast);
        System.out.println("dayList = " + dayList);

        System.out.println("sb_price = " + sb_price);
       

        List<String>  queryList=new ArrayList<>();
        queryList.add(monthFirst);
        queryList.add(monthLast);
        queryList.add(monthFirst);
        queryList.add(monthLast);
        queryList.add(monthFirst);
        queryList.add(monthLast);
       queryList.add(did.toString());

//        List<Map<String, Object>> sql_homeList = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
//            @SneakyThrows
//            @Override
//            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                Map<String, Object> mp = new HashMap<String, Object>();
//                String start_time = rs.getString("starttime");
//                String end_time = rs.getString("endtime");
//
//                Integer onePrice = 0;//单价
//                System.out.println("rs.getInt(\"onePrice\") = " + rs.getInt("onePrice"));
//                if (rs.getInt("onePrice") != 0) {
//                    onePrice = rs.getInt("onePrice");
//                } else {
//                    onePrice = rs.getInt("price") / rs.getInt("service_day");
//                }
//                Integer workStates = rs.getInt("work_states");
//
//                System.out.println("onePrice =_____________ " + onePrice);
//                List<String> dayList = getDayList(workStates,monthFirst, monthLast,start_time,end_time,nowTime);
//                Integer orderDay=dayList.size();
//                Integer truePrice =orderDay*onePrice;
//                mp.put("orderDay", orderDay);
//                mp.put("truePrice", truePrice);
//
//                return mp;
//            }
//
//        }, queryList.toArray());
//        System.out.println("sql_homeList = " + sql_homeList);
//
//        Integer sumTruePrice = 0;//一个时间段金额
//        Integer sumOrderDay = 0;//一个时间段天数
//        for (int j = 0; j <sql_homeList.size() ; j++) {
//            //单个实收金额
//            Integer truePrice = Integer.valueOf(sql_homeList.get(j).get("truePrice").toString());//单个实收金额
//            //单个天数
//            Integer orderDay = Integer.valueOf(sql_homeList.get(j).get("orderDay").toString());
//            sumTruePrice = truePrice + sumTruePrice;
//            sumOrderDay = orderDay + sumOrderDay;
//        }
//        System.out.println("sumTruePrice = " + sumTruePrice);
//        System.out.println("sumOrderDay = " + sumOrderDay);


        System.out.println("sb = " + sb);



        List<Map<String, Object>> list1 = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("did", rs.getString("did"));
                mp.put("cid", rs.getString("id"));



                String start_time = rs.getString("starttime");
                String end_time = rs.getString("endtime");

                //全程里 医院部分订单
                String sql_hospital = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAA%'";
                List<Map<String, Object>> hospitalList = jdbcTemplate.queryForList(sql_hospital, rs.getInt("id"));
                //全程里 会所部分订单
                String sql_club = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAB%'";
                List<Map<String, Object>> clubList = jdbcTemplate.queryForList(sql_club, rs.getInt("id"));


                Map<String, Object> clubMap = new HashMap<>();
                Integer workStates = rs.getInt("work_states");
                String timeType = rs.getString("timetype");
                List<String> dayList = getDayList(workStates, monthFirst, monthLast, start_time, end_time, nowTime);
                Map<String, Object> priceMap = getClubPrice(timeType, workStates, hospitalList, clubList, dayList);

                mp.put("hospitalDay", priceMap.get("hospitalDay"));
                mp.put("clubDay", priceMap.get("clubDay"));
                mp.put("trueHospitalPrice", priceMap.get("trueHospitalPrice"));
                mp.put("trueClubPrice", priceMap.get("trueClubPrice"));

                return mp;
            }
        }, queryList.toArray());
        Integer clubPrice = 0;
        for (int j = 0; j < list1.size(); j++) {
            Integer trueHospitalPrice = Integer.valueOf(list1.get(j).get("trueHospitalPrice").toString());//单个医院实收金额
            Integer trueClubPrice = Integer.valueOf(list1.get(j).get("trueClubPrice").toString());//单个会所实收金额
            clubPrice = trueHospitalPrice + trueClubPrice + clubPrice;
        }

        System.out.println("clubPrice = " + clubPrice);
        return null;
    }
}
