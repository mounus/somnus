package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.BussinessDao;
import com.example.dto.MatornDto;
import com.example.entiy.Bussiness;
import com.example.entiy.Score;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import static com.example.util.MonthUtil.daysBetween;
import static com.example.util.NumberUtil.getNumber;
import static com.example.util.PageUtil.getPage;
import static com.example.util.PeriodUtil.*;
import static com.example.util.PriceUtil.getDayList;
import static com.example.util.PriceUtil.getOneMatornDay;
import static com.example.util.TimeUtil.getOneAllMonth;
import static com.example.util.Year.getConstellation;
import static com.example.util.Year.getYear;

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
        String period = jsonObject.getString("period");
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

    @SneakyThrows
    @Override
    public List<Map<String, Object>> allMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer start = jsonObject.getInteger("start");
        String number = jsonObject.getString("number");
        String grade = jsonObject.getString("grade");
        Integer uid = jsonObject.getInteger("uid");
        Integer agent = jsonObject.getInteger("agent");
        Integer isService = jsonObject.getInteger("isService");

        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        sb.append("select m.id,m.name,YEAR(from_days(datediff(now(), m.born))) as  age ,m.zodiac,m.household,b.grade,");
        sb.append("p.period,m.uid,m.lid,b.photo,m.constellation,p.month,b.isorder");
        sb.append(" from yx_matorn m  left join yx_bussiness b on (m.id=b.mid) left join yx_period p on (m.id=p.mid) ");
        sb.append(" where  m.idtype=1 ");

        List<String> queryList = new ArrayList<>();

        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, uid);
        if (power == 100 || power == 10) {
            //主管全部能看
        }
        if (power == 1) {
            //经理人
            sb.append("  and m.uid= ? ");
            queryList.add(uid.toString());
        }
        if (power == 3 || power == 30) {
            //登记人 或者机构添加
            sb.append("  and m.lid= ? ");
            queryList.add(uid.toString());
        }


        if (agent != 0) {
            sb.append(" and m.uid=? ");
            queryList.add(agent.toString());
        } else {

        }


        if (number.equals("null") || number.isEmpty() || number == "" || number == null) {

        } else {
            if (number.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
                if (number.length() > 8) {
                    //电话
                    sb.append(" and c.phone = ? ");
                    queryList.add(number);
                } else {
                    //id
                    sb.append(" and m.id = ? ");
                    queryList.add(number);
                }
            }
            //汉字
            if (number.matches("^[\\u4e00-\\u9fa5]+$")) {
                String sql2 = "select name from yx_matorn where idtype=1 and name =? ";
                try {
                    List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{number}, String.class);
                    if (number.equals(list1.get(0))) {
                        sb.append("  and m.name=?  ");
                        queryList.add(list1.get(0));
                    } else {

                    }
                } catch (Exception e) {
                    if (number.length() == 1) {
                        String name = number.substring(0, 1);
                        sb.append(" and m.name like ?  ");
                        queryList.add(name + "%");
                    } else {
                        return list;
                    }

                }
            }
            //字母与数字
            if (number.matches(".*\\p{Alpha}.*")) {
                sb.append(" and b.number=? ");
                queryList.add(number);
            }
        }

        if (grade == null || grade == "" || grade.isEmpty()) {

        } else {
            sb.append(" and b.grade=?");
            queryList.add(grade);

        }

        if (isService == 0) {

        }
        if (isService == 1) {
            sb.append(" and p.month is null ");
        }
        if (isService == 2) {
            sb.append(" and p.month is not null ");
        }
        if (isService == 3) {
            sb.append(" and b.isorder=1 ");
        }

        sb.append(" order by m.id desc");
        Map<String, Object> page = new HashMap<>();
        page = getPage(start);
        String sql_page = page.get("sql_page").toString();
        sb.append(sql_page);
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashedMap();

                mp.put("mid", rs.getString("id"));
                mp.put("name", rs.getString("name"));
                mp.put("age", rs.getInt("age"));
                mp.put("photo", rs.getString("photo"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("constellation", rs.getString("constellation"));
                mp.put("grade", rs.getString("grade"));


                String month = rs.getString("month");
                Map<String, String> periodMap = new HashMap<String, String>();
                String newmap = "";
                if (month != null && month != "") {
                    if (rs.getInt("isorder") == 0) {
                        mp.put("isService", 2);//可服务
                    } else {
                        mp.put("isService", 3);//服务中
                    }

                    try {
                        newmap = "[" + getNewMap(month) + "]";
                        String sql_newmap = "update  yx_period set month=? where mid=?";
                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));
                        periodMap = getAll(newmap);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    periodMap = gNull();
                    mp.put("isService", 1);//无档期
                }
                mp.put("period", periodMap);

                String h = rs.getString("household");
                String household = "";
                if (h.length() > 3) {
                    String h1 = h.substring(0, 2);
                    String h2 = h.substring(3, 5);
                    household = h1 + h2;
                } else {
                    household = h.substring(0, 2);

                }
                mp.put("household", household);

                if (rs.getInt("uid") == 0) {
                    mp.put("agent", "待分配");
                } else {
                    String sql_old_agent = "select name from yx_user where id=?";
                    String agent = jdbcTemplate.queryForObject(sql_old_agent, String.class, rs.getInt("uid"));
                    mp.put("agent", agent);
                }

                return mp;


            }
        }, queryList.toArray());

        return list;
    }

    @Override
    public Map<String, Object> bussinessCondition() {
        Map<String, Object> map = new HashedMap();

        //服务档期
        List<Map<String, Object>> periodList = new ArrayList<>();
        Map<String, Object> periodMap = new HashedMap();
        Map<String, Object> periodMap1 = new HashedMap();
        Map<String, Object> periodMap2 = new HashedMap();
        periodMap.put("1", "无档期");
        periodMap1.put("2", "可服务");
        periodMap2.put("3", "服务中");
        periodList.add(periodMap);
        periodList.add(periodMap1);
        periodList.add(periodMap2);
        map.put("periodList", periodList);

        //等级
        String[] gradeString = {"N", "H", "Y1", "Y2", "Y3", "X", "P"};
        List<String> gradeList = Arrays.asList(gradeString);
        map.put("gradeList", gradeList);

        String sql_agent = "select id,name from yx_user where power=10";
        List<Map<String, Object>> agentList = jdbcTemplate.queryForList(sql_agent);
        map.put("agentList", agentList);

        return map;
    }

    @Override
    public Map<String, Object> oneMatornDetail(String json) {
        Map<String, Object> map = new HashedMap();
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");


        //详情资料
        StringBuffer sb_matorn = new StringBuffer();
        sb_matorn.append("select m.id,m.name,YEAR(from_days(datediff(now(), m.born))) as  age ,m.zodiac,m.household,b.grade,");
        sb_matorn.append("p.period,m.uid,m.lid,b.photo,m.constellation,p.month,b.isorder,b.trueday");
        sb_matorn.append(" from yx_matorn m  left join yx_bussiness b on (m.id=b.mid) left join yx_period p on (m.id=p.mid) ");
        sb_matorn.append(" where  m.idtype=1 and m.id=? ");
        List<Map<String, Object>> matornlist = this.jdbcTemplate.query(sb_matorn.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashedMap();

                mp.put("mid", rs.getString("id"));
                mp.put("name", rs.getString("name"));
                mp.put("age", rs.getInt("age"));
                mp.put("photo", rs.getString("photo"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("constellation", rs.getString("constellation"));
                mp.put("grade", rs.getString("grade"));
                mp.put("trueday", rs.getInt("trueday"));


                String h = rs.getString("household");
                String household = "";
                if (h.length() > 3) {
                    String h1 = h.substring(0, 2);
                    String h2 = h.substring(3, 5);
                    household = h1 + h2;
                } else {
                    household = h.substring(0, 2);

                }
                mp.put("household", household);

                if (rs.getInt("uid") == 0) {
                    mp.put("agent", "待分配");
                } else {
                    String sql_old_agent = "select name from yx_user where id=?";
                    String agent = jdbcTemplate.queryForObject(sql_old_agent, String.class, rs.getInt("uid"));
                    mp.put("agent", agent);
                }

                String month = rs.getString("month");
                if (month != null && month != "") {
                    if (rs.getInt("isorder") == 0) {
                        mp.put("isService", 2);//可服务
                    } else {
                        mp.put("isService", 3);//服务中
                    }
                } else {
                    mp.put("isService", 1);
                }

                return mp;
            }
        }, mid);
        map.put("matornlist", matornlist);

        //考核评估
        List<Map<String, Object>> scoreList = new ArrayList<>();
        String sql_score_count = "select count(*) from yx_score where mid=?";
        int score_count = jdbcTemplate.queryForObject(sql_score_count, Integer.class, mid);
        if (score_count > 0) {
            String sql_score = "select background,appearance,communicate,nurse,breast_fed,confinement,postpartum,intelligence,psychology,know,total,comment  from  yx_score where mid=?";
            Map<String, Object> scoreMap = jdbcTemplate.queryForMap(sql_score, mid);
            scoreList.add(scoreMap);
            map.put("scoreList", scoreList);
        } else {
            map.put("scoreList", scoreList);
        }

        //服务档期
        List<Map<String, Object>> periodList = new ArrayList<>();
        map.put("periodList", periodList);


        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowTime = time.format(new Date());
        Integer year = Integer.valueOf(nowTime.substring(0, 4));

        //服务记录
        List<Map<String, Object>> serviceList = new ArrayList<>();
        String sql_service_count = "select count(*) from yx_order where order_states>=2 and mid=?";
        int service_count = jdbcTemplate.queryForObject(sql_service_count, Integer.class, mid);
        if (service_count > 0) {
            StringBuffer sb_order = new StringBuffer();
            sb_order.append("select c.id,c.bid,o.o_number,o.arrival_time,o.confirm_time,o.order_day,");
            sb_order.append("o.wages_remarks,o.order_states ");
            sb_order.append(" from yx_custom c left join yx_order o on (c.id=o.cid) ");
            sb_order.append(" where  o.id=(select max(id) from yx_order where order_states>=2 and  mid=?)");

            Map<String, Object> serviceMap = new HashedMap();
            List<Map<String, Object>> orderList = this.jdbcTemplate.query(sb_order.toString(), new RowMapper<Map<String, Object>>() {
                @SneakyThrows
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashedMap();
                    mp.put("o_number", rs.getString("o_number"));
                    mp.put("cid", rs.getInt("id"));
                    String sql_agent = "select name from yx_user where id=?";
                    String agent = jdbcTemplate.queryForObject(sql_agent, String.class, rs.getInt("bid"));
                    mp.put("agent", agent);
                    mp.put("arrival_time", rs.getString("arrival_time"));
                    mp.put("wages_remarks", rs.getString("wages_remarks"));
                    if (rs.getInt("order_states") == 2) {
                        mp.put("order_states", rs.getInt("order_states"));
                        mp.put("day", daysBetween(rs.getString("arrival_time"), nowTime));
                    }
                    if (rs.getInt("order_states") == 3) {
                        mp.put("order_states", rs.getInt("order_states"));
                        mp.put("confirm_time", rs.getString("confirm_time"));
                        mp.put("order_day", rs.getInt("order_day"));
                        mp.put("returnType", "正常回岗");
                        mp.put("day", daysBetween(rs.getString("confirm_time"), nowTime));
                    }


                    return mp;
                }
            }, mid);
            serviceMap.put("orderList", orderList);

            List<List> dayList = getOneAllMonth(year);
            StringBuffer sb_month = new StringBuffer();
            List<Map<String, Object>> monthList = new ArrayList<>();
            Integer allCount = 0;
            sb_month.append("select arrival_time,confirm_time,order_states from yx_order where mid=? ");
            sb_month.append("  and order_states>=2   and  (   (arrival_time>=?  and arrival_time<=?) or (confirm_time>=? and confirm_time<=?) or (?>=arrival_time and confirm_time>=?) )  ");
            System.out.println("sb_month = " + sb_month);
            for (int i = 0; i < 12; i++) {
                List<String> queryList = new ArrayList<>();
                queryList.add(mid.toString());
                String monthFirst = dayList.get(i).get(0).toString();
                String monthLast = dayList.get(i).get(1).toString();
                queryList.add(monthFirst);
                queryList.add(monthLast);
                queryList.add(monthFirst);
                queryList.add(monthLast);
                queryList.add(monthFirst);
                queryList.add(monthLast);

                List<Map<String, Object>> sql_monthList = jdbcTemplate.query(sb_month.toString(), new RowMapper<Map<String, Object>>() {
                    @SneakyThrows
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        String arrival_time = rs.getString("arrival_time");
                        String confirm_time = rs.getString("confirm_time");
                        Integer order_states = rs.getInt("order_states");
                        List<String> dayList = getOneMatornDay(order_states, monthFirst, monthLast, arrival_time, confirm_time, nowTime);
                        Integer orderDay = dayList.size();
                        mp.put("orderDay", orderDay);
                        return mp;
                    }
                }, queryList.toArray());

                Map<String, Object> monthMap = new HashedMap();
                Integer sumDay = 0;
                for (int j = 0; j < sql_monthList.size(); j++) {
                    Integer order_day = Integer.valueOf(sql_monthList.get(j).get("orderDay").toString());
                    sumDay = order_day + sumDay;
                }
                allCount = sumDay + allCount;
                monthMap.put("month", i + 1);
                monthMap.put("count", sumDay);
                monthList.add(monthMap);
            }
            serviceMap.put("allCount", allCount);
            serviceMap.put("monthList", monthList);
            serviceList.add(serviceMap);

            map.put("serviceList", serviceList);

        } else {
            map.put("serviceList", serviceList);
        }


        //服务照片
        List<String> evaluateList = new ArrayList<>();
        String sql_evaluate_count = "select count(*) from yx_evaluate where mid=?";
        int evaluate_count = jdbcTemplate.queryForObject(sql_evaluate_count, Integer.class, mid);
        if (evaluate_count > 0) {
            String sql_score = "select evaluate_photo from  yx_evaluate where mid=? order by id desc ";
            List<Map<String, Object>> evaluate_photoList = jdbcTemplate.queryForList(sql_score, mid);

            for (int i = 0; i < evaluate_photoList.size(); i++) {
                JSONArray jsonArray = JSONArray.fromObject(evaluate_photoList.get(i).get("evaluate_photo"));
                List<String> photoList = (List) jsonArray;
                for (int j = 0; j < photoList.size(); j++) {
                    evaluateList.add(photoList.get(j).toString());
                }

            }
            map.put("evaluateList", evaluateList);
        } else {
            map.put("evaluateList", evaluateList);
        }

        return map;
    }

    @SneakyThrows
    @Override
    public List<Map<String, Object>> matornAllOrder(String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        Integer start = jsonObject.getInteger("start");
        StringBuffer sb_order = new StringBuffer();
        sb_order.append("select c.id,c.bid,o.o_number,o.arrival_time,o.confirm_time,o.order_day,");
        sb_order.append("o.wages_remarks,o.order_states ");
        sb_order.append(" from yx_custom c left join yx_order o on (c.id=o.cid) ");
        sb_order.append(" where  o.mid=? order by o.creattime desc ");


        Map<String, Object> page = new HashMap<>();
        page = getPage(start);
        String sql_page = page.get("sql_page").toString();
        sb_order.append(sql_page);
        System.out.println("sb_order = " + sb_order);
        List<Map<String, Object>> list = this.jdbcTemplate.query(sb_order.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashedMap();
                mp.put("o_number", rs.getString("o_number"));
                mp.put("cid", rs.getInt("id"));
                String sql_agent = "select name from yx_user where id=?";
                String agent = jdbcTemplate.queryForObject(sql_agent, String.class, rs.getInt("bid"));
                mp.put("agent", agent);
                mp.put("arrival_time", rs.getString("arrival_time"));
                mp.put("wages_remarks", rs.getString("wages_remarks"));
                if (rs.getInt("order_states") == 2) {
                    mp.put("order_states", rs.getInt("order_states"));
                }
                if (rs.getInt("order_states") == 3) {
                    mp.put("order_states", rs.getInt("order_states"));
                    mp.put("confirm_time", rs.getString("confirm_time"));
                    mp.put("order_day", rs.getInt("order_day"));
                    mp.put("returnType", "正常回岗");
                }

                return mp;
            }
        }, mid);

        return list;
    }

    @Override
    public int saveScore(Score score) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_score (mid,background,appearance,communicate,nurse,breast_fed,confinement,postpartum,intelligence,psychology,know,total,comment)");
        sb.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?)");

        int states = this.jdbcTemplate.update(sb.toString(), score.getMid(), score.getBackground(), score.getAppearance(), score.getCommunicate(), score.getNurse(), score.getBreast_fed(), score.getConfinement(),
                score.getPostpartum(), score.getIntelligence(), score.getPsychology(), score.getKnow(), score.getTotal(), score.getComment());
        if (states > 0) {
            return 1;
        } else {
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
    public List<Map<String, Object>> getById(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");

        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        sb.append("select m.id,m.name,m.idcard,m.idcard_type,m.household,nation,m.born,m.educational,m.marriage,");
        sb.append("m.weight,m.height,m.address,c.phone,c.bank_card,c.bank_name,c.emergency_person,c.emergency_phone,");
        sb.append("ori.source,ori.institution_name,ori.witness,ori.witness_phone,ori.other,ori.introducer,ori.introducer_phone,");
        sb.append("b.identity,b.qualification,b.heathly,b.photo,b.charact,b.work_age,b.works,b.trains");
        sb.append(" from yx_matorn m ");
        sb.append(" left JOIN yx_contact c on (m.id=c.mid) left JOIN yx_origin ori ON (m.id=ori.mid) left JOIN yx_bussiness b on (m.id=b.mid) where  b.isquit=0  and m.id = ?");
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {

                Map<String, Object> mp = new HashMap<String, Object>();

                mp.put("source", rs.getString("source"));
                mp.put("institution_name", rs.getString("institution_name"));
                mp.put("witness", rs.getString("witness"));
                mp.put("witness_phone", rs.getString("witness_phone"));
                mp.put("other", rs.getString("other"));
                mp.put("introducer", rs.getString("introducer"));
                mp.put("introducer_phone", rs.getString("introducer_phone"));

                mp.put("name", rs.getString("name").toString());
                mp.put("idcard", rs.getString("idcard"));
                mp.put("idcard_type", rs.getString("idcard_type"));
                mp.put("born", rs.getString("born"));
                mp.put("nation", rs.getString("nation"));
                mp.put("household", rs.getString("household"));
                mp.put("educational", rs.getString("educational"));
                mp.put("marriage", rs.getString("marriage"));
                mp.put("weight", rs.getString("weight"));
                mp.put("height", rs.getString("height"));
                mp.put("address", rs.getString("address"));

                mp.put("source", rs.getString("source"));
                mp.put("institution_name", rs.getString("institution_name"));
                mp.put("witness", rs.getString("witness"));
                mp.put("witness_phone", rs.getString("witness_phone"));
                mp.put("other", rs.getString("other"));
                mp.put("introducer", rs.getString("introducer"));
                mp.put("introducer_phone", rs.getString("introducer_phone"));

                mp.put("photo", rs.getString("photo"));
                mp.put("character", rs.getString("charact"));
                mp.put("work_age", rs.getString("work_age"));


                if (rs.getString("works") != "" && rs.getString("works") != null) {
                    mp.put("works", JSONArray.fromObject(rs.getString("works")));
                } else {
                    mp.put("works", null);
                }

                if (rs.getString("trains") != "" && rs.getString("trains") != null) {
                    mp.put("trains", JSONArray.fromObject(rs.getString("trains")));
                } else {
                    mp.put("trains", null);
                }
                if (rs.getString("qualification") != "" && rs.getString("qualification") != null) {
                    mp.put("qualification", JSONArray.fromObject(rs.getString("qualification")));
                } else {
                    mp.put("qualification", null);
                }
                if (rs.getString("identity") != "" && rs.getString("identity") != null) {
                    mp.put("identity", JSONArray.fromObject(rs.getString("identity")));
                } else {
                    mp.put("identity", null);
                }
                if (rs.getString("heathly") != "" && rs.getString("heathly") != null) {
                    mp.put("heathly", JSONArray.fromObject(rs.getString("heathly")));
                } else {
                    mp.put("heathly", null);
                }


                return mp;
            }
        }, mid);

        return list;
    }

    @Override
    public int updateMatornDto(MatornDto matornDto) {
        String date = matornDto.getBorn();
        String str = date.substring(0, 4);
        String str1 = date.substring(5, 7);
        String str2 = date.substring(8, 10);
        Integer year = Integer.valueOf(str);
        Integer month = Integer.valueOf(str1);
        Integer day = Integer.valueOf(str2);

        String zodiac = getYear(year);
        String constellation = getConstellation(month, day);
        String height = null;
        String weight = null;
        if (matornDto.getWeightHeight() == null || matornDto.getWeightHeight().isEmpty() || matornDto.getWeightHeight() == null) {

        } else {
            height = matornDto.getWeightHeight().substring(0, matornDto.getWeightHeight().indexOf("/"));
            weight = matornDto.getWeightHeight().substring(matornDto.getWeightHeight().indexOf("/") + 1);
        }
        //修改
        String sql_m = "update yx_matorn set name=?,born=?,nation=?,idcard=?,idcard_type=?,household=?,marriage=?,educational=?,weight=?,height=?,address=?,zodiac=?,constellation=? where id=?";
        int states_m = jdbcTemplate.update(sql_m, matornDto.getName(), matornDto.getBorn(), matornDto.getNation(), matornDto.getIdcard(),
                matornDto.getIdcard_type(), matornDto.getHousehold(), matornDto.getMarriage(), matornDto.getEducational(), weight, height, matornDto.getAddress(), zodiac, constellation, matornDto.getMid());

        String sql_c = "update  yx_contact set phone=?,bank_card=?,bank_name=?,emergency_person=?,emergency_phone=? where mid=?";
        int states_c = jdbcTemplate.update(sql_c, matornDto.getPhone(), matornDto.getBank_card(), matornDto.getBank_name(),
                matornDto.getEmergency_person(), matornDto.getEmergency_phone(), matornDto.getMid());

        String sql_o= "update  yx_origin set source=?,institution_name=?,witness=?,witness_phone=?,other=?,introducer=?,introducer_phone=? where mid=?";
        int states_o=jdbcTemplate.update(sql_o,matornDto.getSource(),matornDto.getInstitution_name(),matornDto.getWitness(),matornDto.getWitness_phone(),
                matornDto.getOther(),matornDto.getIntroducer(),matornDto.getIntroducer_phone(),matornDto.getMid());

        String sql_b= "update  yx_bussiness set photo=?,charact=?,work_age=?,works=?,trains=?,qualification=?,identity=?,heathly=? where mid=?";
       int states_b=jdbcTemplate.update(sql_b,matornDto.getPhoto(),matornDto.getCharacter(),matornDto.getWork_age(),matornDto.getWorks(),
               matornDto.getTrains(),matornDto.getQualification(),matornDto.getIdentity(),matornDto.getHeathly(),matornDto.getMid());

       if (states_m>0&&states_c>0&&states_o>0&&states_b>0){
           return 1;
       }else {
           return 0;
       }
    }

    @Override
    public int updateScore(Score score) {

        String sql="update yx_score set background=?,appearance=?,communicate=?,nurse=?,breast_fed=?,confinement=?,postpartum=?,intelligence=?,psychology=?,know=?,total=?,comment=? where mid=?";
        int states=this.jdbcTemplate.update(sql,score.getBackground(),score.getAppearance(),score.getCommunicate(),score.getNurse(),score.getBreast_fed(),
                score.getConfinement(),score.getPostpartum(),score.getIntelligence(),score.getPostpartum(),score.getKnow(),score.getTotal(),score.getComment(),score.getMid());
        if (states>0){
            return 1;
        }else {
            return 0;
        }
    }
}