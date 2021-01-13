package com.example.dao.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.PeriodDao;
import com.example.entiy.Period;

import com.example.entiy.UsersEntity;
import com.tencentcloudapi.ds.v20180523.models.DeleteSealRequest;
import groovy.util.IFileNameFinder;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.jasper.compiler.JspUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;
import static com.example.util.NumberUtil.*;

import static com.example.util.PageUtil.getAllPage;
import static com.example.util.PageUtil.getPage;
import static com.example.util.PeriodUtil.*;
import static com.example.util.Year.getAge;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.STREAM_XML_OUTPUT;
import static com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type.Int;

@Repository
public class PeriodDaoImpl implements PeriodDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @SneakyThrows
    @Override
    public List<Map<String, Object>> listBy(String somnus, Integer uid, Integer start) {

        List list = null;
        StringBuffer sb = new StringBuffer();

        sb.append(" select m.id,m.name,m.household,m.born,m.zodiac,m.constellation,b.photo,b.grade,b.day,b.creat_time,b.assess,c.phone,");
        sb.append("p.states,b.iscollect,p.month from yx_matorn m left JOIN yx_contact c on (m.id=c.mid)");
        sb.append(" left JOIN yx_bussiness b on (m.id=b.mid) left JOIN yx_period p on (m.id=p.mid)  where 1=1 ");

        List<Object> queryList = new ArrayList<Object>();
        Map<String, Object> page = new HashMap<>();
        page = getPage(start);
        String sql_page = page.get("sql_page").toString();

        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, uid);
        if (power == 100 || power == 10) {
            //主管全部能看
        }
        if (power==1){
            //经理人
            sb.append("  and m.uid= ? ");
            queryList.add(uid);
        }
        if (power==3||power==30){
            //登记人 或者机构添加
            sb.append("  and m.lid= ? ");
            queryList.add(uid);
        }


        if (somnus.equals("all")) {
            sb.append("  and b.isblack =0   ");
        }
        if (somnus.equals("black")) {
            sb.append(" and b.isblack = 1   ");
        }
        if (somnus.equals("shelf")) {
            sb.append(" and b.shelf = 1   ");
        }
        sb.append(" order by b.creat_time desc ");
        sb.append(sql_page);

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                String birthTime = rs.getString("born");
                Long age = getAge(birthTime);
                mp.put("age", age);

                String month = rs.getString("month");
                Map<String, String> map = new HashMap<String, String>();
                String newmap = "";
                if (month != null && month != "") {
                    try {
                        newmap = "[" + getNewMap(month) + "]";
                        String sql_newmap = "update  yx_period set month=? where mid=?";
                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));
                        map = getAll(newmap);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    map = gNull();
                }

                mp.put("grade", rs.getString("grade"));
                mp.put("rise", 2);//不提醒

//                String creat_time = rs.getString("creat_time");
//                int day = rs.getInt("day");
//                String grade = rs.getString("grade");
//                int a = 0;
//                if (grade.equals("待定级") || grade.equals("P0") || grade.equals("P1")) {
//                    try {
//                        a = getNull(creat_time);
//                        //mp.put("rise", 0);//等待考核状态
//                        mp.put("rise", 2);//等待考核状态
//                        mp.put("day", a);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    a = getDay(day, grade);
//                    if (a != 0) {
//                        mp.put("rise", 1);//等待晋级状态
//                        mp.put("day", a);
//                    } else {
//                        mp.put("rise", 2);//不提醒
//                    }
//                }
                String household = rs.getString("household");
                String city = "";
                if (household.length() > 3) {
                    String h1 = household.substring(0, 2);
                    String h2 = household.substring(3, 5);
                    city = h1 + h2;
                } else {

                    city = household.substring(0, 2);

                }

                mp.put("id", rs.getInt("id"));
                mp.put("name", rs.getString("name"));
                mp.put("city", city);
                mp.put("month", map);
                mp.put("phone", rs.getString("phone"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("constellation", rs.getString("constellation"));
                mp.put("states", rs.getString("states"));
                mp.put("photo", rs.getString("photo"));
                mp.put("assess", rs.getInt("assess"));
                mp.put("iscollect", rs.getInt("iscollect"));
                return mp;
            }
        }, queryList.toArray());

        return list;
    }

    @SneakyThrows
    @Override
    public List<Map<String, Object>> getByName(String json) {

        JSONObject jsonObject = JSON.parseObject(json);
        Integer uid = jsonObject.getInteger("uid");
        String name = jsonObject.getString("name");
        Integer start = jsonObject.getInteger("start");
        String somnus = jsonObject.getString("somnus");
        List list = null;

        StringBuffer sb = new StringBuffer();
        List<Object> queryList = new ArrayList<Object>();

        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, uid);
        sb.append("select m.id,m.name,m.household,m.born,m.zodiac,m.constellation,b.photo,b.grade,b.day,b.creat_time,b.assess,c.phone,");
        sb.append(" p.states,b.iscollect,p.month from yx_matorn m left JOIN yx_contact c on (m.id=c.mid)");
        sb.append("  left JOIN yx_bussiness b on (m.id=b.mid) left JOIN yx_period p  on (m.id=p.mid) where  1=1    ");


        if (power == 100 || power == 10) {
        } else {
            sb.append(" and m.uid= ?  ");
            queryList.add(uid);
        }

        Map<String, Object> page = new HashMap<>();
        page = getPage(start);
        String sql_page = page.get("sql_page").toString();

        if (somnus.equals("all")) {
            sb.append("  and b.isblack =0    ");
        }
        if (somnus.equals("black")) {
            sb.append(" and b.isblack = 1  ");
        }
        if (somnus.equals("shelf")) {
            sb.append(" and b.shelf = 1   ");
        }


            if (name.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
                if (name.length() > 8) {
                    //电话
                    sb.append(" and c.phone = ? ");
                    queryList.add(name);
                } else {
                    //id
                    sb.append(" and m.id = ? ");
                    queryList.add(name);
                }
            }
            //汉字
            if (name.matches("^[\\u4e00-\\u9fa5]+$")) {
                String sql2 = "select name from yx_matorn where name =? and idtype=1 ";
                try {
                    List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{name}, String.class);
                    if (name.equals(list1.get(0))) {
                        sb.append("  and m.name=?  ");
                        queryList.add(list1.get(0));
                    } else {

                    }
                } catch (Exception e) {
                    if (name.length() == 1) {
                        String sex = name.substring(0, 1);
                        sb.append(" and m.name like ?  ");
                        queryList.add(sex + "%");
                    } else {
                        return list;
                    }

                }
            }
            //字母与数字
            if (name.matches(".*\\p{Alpha}.*")) {
                sb.append(" and b.number=? ");
                queryList.add(name);
            }



//        String sql2 = "select name from yx_matorn where name = ? ";
//        try {
//            List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{name}, String.class);
//            if (name.equals(list1.get(0))) {
//                sb.append("  and m.name= ? ");
//                queryList.add(list1.get(0));
//            } else {
//
//            }
//        } catch (Exception e) {
//
//            String name1 = name.substring(0, 1);
//            sb.append("  and m.name like ?  ");
//            queryList.add(name1 + "%");
//
//        }
        sb.append(" order by b.creat_time desc");
        sb.append(sql_page);

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                String birthTime = rs.getString("born");
                Long age = getAge(birthTime);
                mp.put("age", age);
                String month = rs.getString("month");
                Map<String, String> map = new HashMap<String, String>();
                String newmap = "";
                if (month != null && month != "") {

                    try {
                        newmap = "[" + getNewMap(month) + "]";
                        String sql_newmap = "update  yx_period set month=? where mid=?";
                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));
                        map = getAll(newmap);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    map = gNull();
                }


                mp.put("grade", rs.getString("grade"));
                mp.put("rise", 2);
//                String creat_time = rs.getString("creat_time");
//                int day = rs.getInt("day");
//                String grade = rs.getString("grade");
//                int a = 0;
//                if (grade.equals("待定级") || grade.equals("P0") || grade.equals("P1")) {
//                    try {
//                        a = getNull(creat_time);
//                        mp.put("rise", 2);//等待考核状态0
//                        mp.put("day", a);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    a = getDay(day, grade);
//                    if (a != 0) {
//                        mp.put("rise", 1);//等待晋级状态
//                        mp.put("day", a);
//                    } else {
//                        mp.put("rise", 2);//不提醒
//                    }
//                }
                String household = rs.getString("household");
                String city = "";
                if (household.length() > 3) {
                    String h1 = household.substring(0, 2);
                    String h2 = household.substring(3, 5);
                    city = h1 + h2;
                } else {
                    city = household.substring(0, 2);

                }
                mp.put("id", rs.getInt("id"));
                mp.put("name", rs.getString("name"));
                mp.put("city", city);
                mp.put("month", map);
                mp.put("phone", rs.getString("phone"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("constellation", rs.getString("constellation"));
                mp.put("states", rs.getString("states"));
                mp.put("photo", rs.getString("photo"));
                //mp.put("qualification", rs.getString("qualification"));
                mp.put("assess", rs.getInt("assess"));
                mp.put("iscollect", rs.getInt("iscollect"));
                return mp;
            }
        }, queryList.toArray());

        return list;


    }

    @Override
    public int save(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        Integer states = jsonObject.getInteger("states");
        String period = jsonObject.getString("period");
        String month = jsonObject.getString("month");
        String rest = jsonObject.getString("rest");
        String sql = "insert into yx_period (mid,period,month,rest) values(?,?,?,?) ";


        return this.jdbcTemplate.update(sql, mid, period, month, rest);

    }

    @Override
    public List<Map<String, Object>> getById(Integer mid) throws EmptyResultDataAccessException {

        List list = null;
        String sql = "select mid from yx_period where mid =?";
        Integer mid1;
        try {
            mid1 = this.jdbcTemplate.queryForObject(sql, Integer.class, mid);//获取单个值
            String sql1 = "select * from yx_period where mid =?";

            if (mid.equals(mid1)) {
                list = this.jdbcTemplate.query(sql1.toString(), new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        mp.put("mid", rs.getInt("mid"));
                        mp.put("states", rs.getString("states"));

                        String period = null;
                        try {
                            period = getNewPeriod(rs.getString("period"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String sql_newperiod = "update  yx_period set period=? where mid=?";
                        jdbcTemplate.update(sql_newperiod, period, rs.getInt("mid"));//清除前几个月的period

                        mp.put("period", period);
                        mp.put("month", rs.getString("month"));
                        mp.put("rest", rs.getString("rest"));
                        return mp;
                    }
                }, mid);
                return list;
            } else {
                return list;
            }

        } catch (EmptyResultDataAccessException e) {

            return list;
        }


    }

    @Override
    public Period getByStates(int states) {
        return null;
    }

    @Override
    public int update(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer states = jsonObject.getInteger("states");
        String period = jsonObject.getString("period");
        String month = jsonObject.getString("month");
        String rest = jsonObject.getString("rest");
        Integer mid = jsonObject.getInteger("mid");

        String sql = "update yx_period set states=?,period= ? ,month=?,rest=? where mid=? ";
        return this.jdbcTemplate.update(sql, states, period, month, rest, mid);


    }


    @SneakyThrows
    public Map<String, Object> getAdmin(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String somnus = jsonObject.getString("somnus");
        Integer start = jsonObject.getInteger("start");
        List<Map<String, Object>> list = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select m.id,m.uid,m.lid,m.name,c.phone,b.photo,b.grade,b.number,b.day,p.month,u.name as l_name,");
        sb.append("b.creat_time,b.reportPdf,o.institution_name,b.assess,b.trueday,b.isblack,b.isevaluate,b.shelf  from yx_matorn m left JOIN yx_contact c  on(m.id=c.mid)left JOIN yx_bussiness b  on(m.id=b.mid)  ");
        sb.append(" left JOIN yx_origin o on (m.id=o.mid) left join  yx_user u on (m.lid=u.id)");
        sb.append(" left JOIN yx_period p  on (m.id=p.mid) where  1=1    ");

        StringBuffer sb_count = new StringBuffer();
        sb_count.append("select count(*) from yx_matorn m left JOIN yx_contact c  on (m.id=c.mid) left JOIN yx_bussiness b  on (m.id=b.mid)");
        sb_count.append(" left JOIN yx_period p on (m.id=p.mid) where 1=1  ");

        Map<String, Object> map = new HashMap<String, Object>();

        //公共条件
        StringBuffer sb_query = new StringBuffer();
        if (somnus.equals("all")) {
            sb_query.append("  and b.isblack =0  ");
        }
        if (somnus.equals("black")) {
            sb_query.append(" and b.isblack = 1  ");
        }
        if (somnus.equals("shelf")) {
            sb_query.append(" and b.shelf = 1   ");
        }
        sb_query.append(" order by b.creat_time  desc ");

        sb_count.append(sb_query);
        Integer count = this.jdbcTemplate.queryForObject(sb_count.toString(), Integer.class);
        map.put("count", count);
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        map.put("num", page.get("num"));
        String sql_page = page.get("sql_page").toString();
        map.put("page", page.get("page"));

        sb.append(sb_query);
        sb.append(sql_page);

        String sql_user = "select name from yx_user where power = 1 ";
        List<Object> listagent = (List) this.jdbcTemplate.queryForList(sql_user.toString());
        map.put("listagent", listagent);
        String sql_institution = "select mechanism_name as name from yx_user where power=30";
        List<Object> institution_nameList = (List) this.jdbcTemplate.queryForList(sql_institution.toString());
        map.put("institution_nameList", institution_nameList);

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();

                String month = rs.getString("month");

                Map<String, String> map = new HashMap<String, String>();
                String newmap = "";
                Integer isPeriod = 0;
                if (month != null && month != "") {

                    try {
                        newmap = "[" + getNewMap(month) + "]";
                        isPeriod = 1;
                        String sql_newmap = "update  yx_period set month=? where mid=?";
                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));//清除前几个月的month
                        map = getAll(newmap);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    map = gNull();
                    isPeriod = 0;
                }

                mp.put("grade", rs.getString("grade"));
                mp.put("assess", rs.getString("assess"));
                mp.put("id", rs.getInt("id"));
                mp.put("isblack", rs.getInt("isblack"));

                Integer uid = rs.getInt("uid");
                mp.put("l_name", rs.getString("l_name"));
                mp.put("uid", rs.getInt("lid"));
                System.out.println("uid = " + uid);
                if (uid == 0 || uid == 22) {
                    mp.put("agent", "");
                } else {
                    String sql_agent = "select name from yx_user where id = ?  ";
                    String agent = jdbcTemplate.queryForObject(sql_agent, String.class, uid);
                    mp.put("agent",agent);
                }

                mp.put("name", rs.getString("name"));
                mp.put("month", map);
                mp.put("isPeriod", isPeriod);
                mp.put("number", rs.getString("number"));
                mp.put("photo", rs.getString("photo"));
                mp.put("creat_time", rs.getString("creat_time"));

                mp.put("trueday", rs.getInt("trueday"));
                mp.put("day", rs.getInt("day"));
                mp.put("phone", rs.getString("phone"));
                mp.put("isevaluate", rs.getInt("isevaluate"));
                mp.put("shelf", rs.getInt("shelf"));
                mp.put("institution_name", rs.getString("institution_name"));
                mp.put("reportPdf", rs.getString("reportPdf"));
                return mp;
            }
        });
        map.put("list", list);

        return map;
    }


    @SneakyThrows
    @Override
    public Map<String, Object> getByNumber(String json) {
        List<Map<String, Object>> list = null;
        JSONObject jsonObject = JSON.parseObject(json);
        String number = jsonObject.getString("number");
        String somnus = jsonObject.getString("somnus");
        Integer start = jsonObject.getInteger("start");

        //查询语句
        StringBuffer sb = new StringBuffer();
        sb.append("select m.id,m.id,m.uid,m.lid,m.name,c.phone,b.photo,b.grade,b.number,b.day,p.month,u.name as l_name,");
        sb.append("b.creat_time,o.institution_name,b.reportPdf,b.assess,b.trueday,b.isblack,b.isevaluate,b.shelf from yx_matorn m left JOIN yx_contact c on(m.id=c.mid)left JOIN yx_bussiness b on(m.id=b.mid)  ");
        sb.append(" left JOIN yx_origin o on (m.id=o.mid) left join  yx_user u on (m.lid=u.id)");
        sb.append(" left JOIN yx_period p on (m.id=p.mid) where 1=1 ");

        //查询条数语句
        StringBuffer sb_count = new StringBuffer();
        sb_count.append("select count(*) from yx_matorn m left JOIN yx_contact c on (m.id=c.mid) left JOIN yx_bussiness b  on (m.id=b.mid)");
        sb_count.append(" left JOIN yx_period p on (m.id=p.mid) where  1=1 ");

        List<Object> queryList = new ArrayList<Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        //公共条件
        StringBuffer sb_query = new StringBuffer();
        if (somnus.equals("all")) {
            sb_query.append(" and b.isblack =0  ");
        }
        if (somnus.equals("black")) {
            sb_query.append(" and b.isblack = 1  ");
        }
        if (somnus.equals("shelf")) {
            sb_query.append(" and b.shelf = 1   ");
        }

        number = number.replaceAll(" ", "");
        //数字id
        if (number.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
            if (number.length() > 8) {
                //电话
                sb_query.append(" and c.phone = ? ");
                queryList.add(number);
            } else {
                //id
                sb_query.append(" and m.id = ? ");
                queryList.add(number);
            }
        }
        //汉字
        if (number.matches("^[\\u4e00-\\u9fa5]+$")) {

            String sql2 = "select name from yx_matorn where name = ? ";
            try {
                List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{number}, String.class);
                if (number.equals(list1.get(0))) {
                    sb_query.append("  and m.name= ?  ");
                    queryList.add(list1.get(0));
                } else {

                }
            } catch (Exception e) {
                if (number.length() == 1) {
                    String name = number.substring(0, 1);
                    sb_query.append("  and m.name like ?   ");
                    queryList.add(name + "%");
                } else {
                    return map;
                }

            }
        }
        //字母与数字
        if (number.matches(".*\\p{Alpha}.*")) {
            sb_query.append(" and b.number=? ");
            queryList.add(number);
        }
        sb_query.append("  order by b.creat_time desc ");

        sb_count.append(sb_query);
        int count = this.jdbcTemplate.queryForObject(sb_count.toString(), Integer.class, queryList.toArray());
        map.put("count", count);
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        map.put("num", page.get("num"));
        String sql_page = page.get("sql_page").toString();
        map.put("page", page.get("page"));

        sb.append(sb_query);
        sb.append(sql_page);

        String sql_user = "select name from yx_user where power = 1 ";
        List<Object> listagent = (List) this.jdbcTemplate.queryForList(sql_user.toString());
        map.put("listagent", listagent);
        String sql_institution = "select mechanism_name as name from yx_user where power=30";
        List<Object> institution_nameList = (List) this.jdbcTemplate.queryForList(sql_institution.toString());
        map.put("institution_nameList", institution_nameList);

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                String month = rs.getString("month");

                Map<String, String> map = new HashMap<String, String>();
                String newmap = "";
                Integer isPeriod = 0;
                if (month != null && month != "") {

                    try {
                        newmap = "[" + getNewMap(month) + "]";
                        String sql_newmap = "update  yx_period set month=? where mid=?";
                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));//清除前几个月的month
                        isPeriod = 1;
                        map = getAll(newmap);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    isPeriod = 0;
                    map = gNull();
                }

                mp.put("grade", rs.getString("grade"));
                mp.put("assess", rs.getString("assess"));
                mp.put("id", rs.getInt("id"));
                Integer uid = rs.getInt("uid");
                mp.put("l_name", rs.getString("l_name"));
                mp.put("uid", rs.getInt("lid"));
                if (uid == 0 || uid == 22) {
                    mp.put("agent", "");
                } else {
                    String sql_agent = "select name from yx_user where id = ?  ";
                    String agent = jdbcTemplate.queryForObject(sql_agent, String.class, uid);
                    mp.put("agent",agent);
                }

                mp.put("name", rs.getString("name"));
                mp.put("month", map);
                mp.put("isPeriod", isPeriod);
                mp.put("number", rs.getString("number"));
                mp.put("photo", rs.getString("photo"));
                mp.put("creat_time", rs.getString("creat_time"));
                mp.put("day", rs.getInt("day"));
                mp.put("trueday", rs.getInt("trueday"));

                mp.put("phone", rs.getString("phone"));
                mp.put("isevaluate", rs.getInt("isevaluate"));
                mp.put("shelf", rs.getInt("shelf"));
                mp.put("isblack", rs.getInt("isblack"));
                mp.put("reportPdf", rs.getString("reportPdf"));
                mp.put("institution_name", rs.getString("institution_name"));

                return mp;
            }
        }, queryList.toArray());
        map.put("list", list);
        return map;
    }

    @Override
    public int updateGrade_agent(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String grade = jsonObject.getString("grade");
        Integer id = jsonObject.getInteger("id");
        Integer aid = jsonObject.getInteger("aid");
        String agent = jsonObject.getString("agent");//要修改的经理人
        Integer uid = jsonObject.getInteger("uid");//原来录资料的人或者没有分配在她头上经理人id
        Integer count = jsonObject.getInteger("count");//定级加的天数

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String update_time = time.format(new Date());
        String sql_new_uid = "select id from yx_user where name=?  ";
        //修改之后经理人id
        Integer new_uid = this.jdbcTemplate.queryForObject(sql_new_uid, Integer.class, agent);

        //修改之前的经理人
        String sql_old_uid = "select uid from yx_matorn where id=?";
        Integer old_uid = this.jdbcTemplate.queryForObject(sql_old_uid, Integer.class, id);

        String old_agent = null;
        if (old_uid == 0 || old_uid == 22) {
            //没有经理人
            if(uid==0){
                old_agent = "待分配";
            }
            if (uid==22){
                String sql_old_agent = "select name from yx_user where  and id=?";
                old_agent = jdbcTemplate.queryForObject(sql_old_agent, String.class, 22);
            }

        } else {
            //有经理人
            String sql_old_agent = "select name from yx_user where  and id=?";
            old_agent = jdbcTemplate.queryForObject(sql_old_agent, String.class, old_uid);
        }
        String sql_agent = "update  yx_matorn set uid=? where id=?";
        int states_agent = this.jdbcTemplate.update(sql_agent, new_uid, id);
        //查询之前的等级
        String sql_old_grade = "select grade from yx_bussiness where mid=?";
        String new_grage = null;
        String old_grade = jdbcTemplate.queryForObject(sql_old_grade, String.class, id);
        if (old_grade.equals("N") && grade.equals("P")) {
            new_grage = grade;
        } else {
            new_grage = old_grade;
        }
        String sql_update_grade = "update  yx_bussiness set grade=? where mid=?";
        int states_update_grade = jdbcTemplate.update(sql_update_grade, new_grage, id);

        String sql_grade = "insert into yx_update_grade(uid,aid,mid,new_uid,update_time,update_txt,type) values(?,?,?,?,?,?,?)";
        String update_txt = old_agent + "," + agent;
        //type=0 更换经理人
        int states = this.jdbcTemplate.update(sql_grade, uid, aid, id, new_uid, update_time, update_txt, 0);
        if (states > 0 && states_agent > 0 && states_update_grade > 0) {
            return 1;
        } else {
            return 0;
        }

    }


    @Override
    public List<Map<String, Object>> getByTime(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String starttime = jsonObject.getString("starttime");
        String endtime = jsonObject.getString("endtime");
        List list = null;

        StringBuffer sb = new StringBuffer();
        sb.append("select yx_matorn.id,yx_matorn.uid,yx_matorn.name,yx_contact.phone,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.number,yx_bussiness.day,");
        sb.append("yx_period.month,yx_bussiness.creat_time,yx_bussiness.trueday,yx_bussiness.isblack,yx_bussiness.shelf from yx_matorn left JOIN yx_contact  on(yx_matorn.id=yx_contact.mid)left JOIN yx_bussiness ");
        sb.append(" on(yx_matorn.id=yx_bussiness.mid) left JOIN yx_period  on (yx_matorn.id=yx_period.mid) where  yx_bussiness.isblack = 0  and  yx_bussiness.assess= 0");
        sb.append(" and yx_bussiness.creat_time  between ? and ?");

        String sql_user = "select name from yx_user where power = 1";
        List<Object> listagent = (List) this.jdbcTemplate.queryForList(sql_user.toString());
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();

                String month = rs.getString("month");
                Map<String, String> map = new HashMap<String, String>();
                if (month != null && month != "") {

                    try {
                        map = getAll(month);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    map = gNull();
                }


                Integer uid = rs.getInt("uid");
                String sql_a = "select name from yx_user where id = ? ";
                String agent = jdbcTemplate.queryForObject(sql_a, String.class, uid);

                Integer id = rs.getInt("id");
                String sql_lid = "select lid from yx_matorn where id = ? ";
                try {
                    Integer lid = jdbcTemplate.queryForObject(sql_lid, Integer.class, id);
                    if (lid == null || lid == 0 || lid.equals("")) {
                        mp.put("l_name", agent);
                        mp.put("uid", rs.getInt("uid"));
                        mp.put("agent", "");
                    } else {
                        String sql_lname = "select name from yx_user where id = ? ";
                        String l_name = jdbcTemplate.queryForObject(sql_lname, String.class, lid);
                        mp.put("uid", lid);
                        mp.put("l_name", l_name);
                        mp.put("agent", agent);
                    }

                } catch (Exception e) {

                }

                mp.put("grade", rs.getString("grade"));
                mp.put("id", rs.getInt("id"));
                mp.put("uid", rs.getInt("uid"));
                mp.put("name", rs.getString("name"));
                mp.put("month", map);
                mp.put("number", rs.getString("number"));
                mp.put("photo", rs.getString("photo"));
                mp.put("creat_time", rs.getString("creat_time"));

                mp.put("listagent", listagent);
                mp.put("trueday", rs.getInt("trueday"));
                mp.put("phone", rs.getString("phone"));
                mp.put("isevaluate", rs.getInt("isevaluate"));
                mp.put("shelf", rs.getInt("shelf"));
                return mp;
            }
        }, starttime, endtime);

        return list;


    }

    @Override
    public List<Map<String, Object>> dynamic(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        List list = null;
        Integer uid = jsonObject.getInteger("uid");
        Integer start = jsonObject.getInteger("start");
        StringBuffer sb = new StringBuffer();
        List<Object> queryList = new ArrayList<Object>();

        sb.append("select yx_matorn.id,yx_matorn.name,yx_update_grade.update_time,yx_update_grade.type,yx_update_grade.update_txt from yx_matorn  left JOIN yx_update_grade on(yx_matorn.id=yx_update_grade.mid)");

        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, uid);

        if (power == 100 || power == 10) {
            sb.append("where  yx_update_grade.type in(1,3) ");
        } else {
            sb.append("where yx_update_grade.uid=? and yx_update_grade.type in(1,3) ");
            queryList.add(uid);
        }
        sb.append(" and yx_update_grade.update_time>?  and yx_update_grade.update_time<?");
        sb.append(" order by yx_update_grade.update_time desc");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowtime = time.format(new Date());
        String threetime = "";
        String sixetime = "";
        if (start == 1) {
            threetime = getNewEndtime(nowtime, start * 3);
            queryList.add(threetime);
            queryList.add(nowtime);
            System.out.println("threetime = " + threetime);
            System.out.println("nowtime = " + nowtime);
        }
        if (start == 2) {
            threetime = getNewEndtime(nowtime, (start - 1) * 3);
            sixetime = getNewEndtime(nowtime, start * 3);
            queryList.add(sixetime);
            queryList.add(threetime);

        }

        System.out.println("sb = " + sb);
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("name", rs.getString("name"));
                mp.put("id", rs.getString("id"));
                mp.put("time", rs.getString("update_time"));
                mp.put("type", rs.getInt("type"));
                mp.put("update_txt", rs.getString("update_txt"));
                return mp;
            }

        }, queryList.toArray());

        return list;
    }

    @SneakyThrows
    @Override
    public Map<String, Object> getByGrade(String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String grade = jsonObject.getString("grade");
        String agent = jsonObject.getString("agent");
        String sort = jsonObject.getString("sort");
        String somnus = jsonObject.getString("somnus");
        Integer start = jsonObject.getInteger("start");
        String institution_name = jsonObject.getString("institution_name");

        List<Map<String, Object>> list = null;
        StringBuffer sb = new StringBuffer();
        StringBuffer sb_count = new StringBuffer();
        //统计总数 页数
        sb_count.append("select count(*) from yx_matorn left JOIN yx_contact  on (yx_matorn.id=yx_contact.mid) left JOIN yx_bussiness  on (yx_matorn.id=yx_bussiness.mid)");
        sb_count.append(" left JOIN yx_origin on (yx_matorn.id=yx_origin.mid)");
        sb_count.append(" left JOIN yx_period on (yx_matorn.id=yx_period.mid) where 1=1 ");

        //搜索语句
        sb.append("select yx_matorn.id,yx_matorn.uid,yx_matorn.lid,yx_matorn.name,yx_contact.phone,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.number,yx_bussiness.day,yx_bussiness.trueday,yx_period.month,yx_bussiness.reportPdf,yx_origin.institution_name,");
        sb.append("yx_bussiness.creat_time,yx_bussiness.assess,yx_bussiness.isblack,yx_bussiness.isevaluate,yx_user.name as l_name,yx_bussiness.shelf from yx_matorn left JOIN yx_contact  on(yx_matorn.id=yx_contact.mid) left JOIN yx_bussiness  on(yx_matorn.id=yx_bussiness.mid)  ");
        sb.append(" left JOIN yx_origin on (yx_matorn.id=yx_origin.mid) left join yx_user on (yx_matorn.lid=yx_user.id)");
        sb.append(" left JOIN yx_period  on (yx_matorn.id=yx_period.mid) where 1=1 ");

        List<Object> queryList = new ArrayList<Object>();

        StringBuffer sb_query = new StringBuffer();
        Integer id = 0;
        if (agent != "" && agent != null && !agent.isEmpty() && agent != "经理人") {
            String sql_u = "select id from yx_user where name=? ";
            id = this.jdbcTemplate.queryForObject(sql_u, Integer.class, agent);
            if (id != 22) {
                sb_query.append(" and yx_matorn.uid =? ");
                queryList.add(id.toString());
            } else {
                sb_query.append(" and (yx_matorn.uid=0 or yx_matorn.uid=22 ) ");
            }

        } else {

        }

        if (grade != "" && grade != null && !grade.isEmpty() && grade != "业务等级") {
            sb_query.append(" and yx_bussiness.grade=?");
            queryList.add(grade);
        } else {

        }

        if (institution_name == null || institution_name == "" || institution_name.isEmpty()) {

        } else {
            sb_query.append(" and yx_origin.institution_name= ? ");
            queryList.add(institution_name);
        }
        if (somnus.equals("all")) {
            sb_query.append(" and yx_bussiness.isblack =0");
        }
        if (somnus.equals("black")) {
            sb_query.append(" and yx_bussiness.isblack=1 ");
        }
        if (somnus.equals("shelf")) {
            sb_query.append(" and yx_bussiness.shelf=1");
        }


        Map<String, Object> map = new HashMap<String, Object>();
        sb_count.append(sb_query);

        int count = this.jdbcTemplate.queryForObject(sb_count.toString(), Integer.class, queryList.toArray());
        map.put("count", count);
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        String sql_page = page.get("sql_page").toString();
        map.put("page", page.get("page"));
        map.put("num", page.get("num"));

        sb_query.append("   order by yx_bussiness.trueday ");
        if (sort.equals("desc")) {
            sb_query.append(" desc ");
        } else {
            sb_query.append(" asc ");
        }
        sb_query.append(sql_page);
        sb.append(sb_query);

        String sql_user = "select name from yx_user where power = 1";
        List<Object> listagent = (List) this.jdbcTemplate.queryForList(sql_user.toString());
        map.put("listagent", listagent);
        String sql_institution = "select mechanism_name as name from yx_user where power=30";
        List<Object> institution_nameList = (List) this.jdbcTemplate.queryForList(sql_institution.toString());
        map.put("institution_nameList", institution_nameList);

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();

                String month = rs.getString("month");

                Map<String, String> map = new HashMap<String, String>();
                String newmap = "";
                Integer isPeriod = 0;
                if (month != null && month != "") {
                    try {
                        newmap = "[" + getNewMap(month) + "]";

                        String sql_newmap = "update  yx_period set month=? where mid=?";
                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));//清除前几个月的month
                        isPeriod = 1;
                        map = getAll(newmap);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    isPeriod = 0;
                    map = gNull();
                }


                Integer uid = rs.getInt("uid");
                mp.put("l_name", rs.getString("l_name"));
                mp.put("uid", rs.getInt("lid"));
                if (uid == 0 || uid == 22) {
                    mp.put("agent", "");
                } else {
                    String sql_agent = "select name from yx_user where id = ?  ";
                    String agent = jdbcTemplate.queryForObject(sql_agent, String.class, uid);
                    mp.put("agent",agent);
                }

                mp.put("grade", rs.getString("grade"));
                mp.put("assess", rs.getString("assess"));
                mp.put("id", rs.getInt("id"));

                mp.put("name", rs.getString("name"));
                mp.put("month", map);
                mp.put("isPeriod", isPeriod);
                mp.put("number", rs.getString("number"));
                mp.put("photo", rs.getString("photo"));
                mp.put("creat_time", rs.getString("creat_time"));
                mp.put("day", rs.getInt("day"));
                mp.put("trueday", rs.getInt("trueday"));
                mp.put("phone", rs.getString("phone"));
                mp.put("isevaluate", rs.getInt("isevaluate"));
                mp.put("shelf", rs.getInt("shelf"));
                mp.put("isblack", rs.getInt("isblack"));
                mp.put("reportPdf", rs.getString("reportPdf"));
                mp.put("institution_name", rs.getString("institution_name"));
                return mp;
            }
        }, queryList.toArray());
        map.put("list", list);
        return map;

    }


    @Override
    public int updateCollect(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        Integer iscollect = jsonObject.getInteger("iscollect");
        String sql = "update yx_bussiness set iscollect=? where mid=?";
        int state = this.jdbcTemplate.update(sql, iscollect, mid);
        if (state > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<Map<String, Object>> myCollect(String json) {

        List list = null;
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer uid = jsonObject.getInteger("uid");
        StringBuffer sb = new StringBuffer();
        List<Object> queryList = new ArrayList<Object>();

        if (uid == 17 || uid == 2 || uid == 3) {
            sb.append("SELECT yx_matorn.id,yx_matorn.name,yx_matorn.household,yx_matorn.born,yx_matorn.zodiac,yx_matorn.constellation,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.day,yx_bussiness.creat_time,yx_bussiness.assess,yx_contact.phone,");
            sb.append("yx_period.states,yx_bussiness.iscollect,yx_period.month from yx_matorn left JOIN yx_contact on (yx_matorn.id=yx_contact.mid)");
            sb.append(" left JOIN yx_bussiness  on (yx_matorn.id=yx_bussiness.mid) left JOIN yx_period  on (yx_matorn.id=yx_period.mid)  where yx_bussiness.iscollect=1 ");


        } else {
            sb.append("SELECT yx_matorn.id,yx_matorn.name,yx_matorn.household,yx_matorn.born,yx_matorn.zodiac,yx_matorn.constellation,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.day,yx_bussiness.creat_time,yx_bussiness.assess,yx_contact.phone,");
            sb.append("yx_period.states,yx_bussiness.iscollect,yx_period.month from yx_matorn left JOIN yx_contact on (yx_matorn.id=yx_contact.mid)");
            sb.append(" left JOIN yx_bussiness  on (yx_matorn.id=yx_bussiness.mid) left JOIN yx_period  on (yx_matorn.id=yx_period.mid) where  yx_bussiness.iscollect=1 and yx_matorn.uid=?");
            queryList.add(uid);

        }

        sb.append(" order by yx_bussiness.creat_time desc");

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                String birthTime = rs.getString("born");
                Long age = getAge(birthTime);
                mp.put("age", age);

                String month = rs.getString("month");
                Map<String, String> map = new HashMap<String, String>();
                String newmap = "";
                if (month != null && month != "") {

                    try {
                        newmap = "[" + getNewMap(month) + "]";
                        String sql_newmap = "update  yx_period set month=? where mid=?";
                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));
                        map = getAll(newmap);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    map = gNull();
                }

                mp.put("grade", rs.getString("grade"));
                mp.put("rise", 2);
                String household = rs.getString("household");
                String city = "";
                if (household.length() > 3) {
                    String h1 = household.substring(0, 2);
                    String h2 = household.substring(3, 5);
                    city = h1 + h2;
                } else {

                    city = household.substring(0, 2);

                }

                mp.put("id", rs.getInt("id"));
                mp.put("name", rs.getString("name"));
                mp.put("city", city);
                mp.put("month", map);
                mp.put("phone", rs.getString("phone"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("constellation", rs.getString("constellation"));
                mp.put("states", rs.getString("states"));
                mp.put("photo", rs.getString("photo"));
                mp.put("assess", rs.getInt("assess"));
                mp.put("iscollect", rs.getInt("iscollect"));
                return mp;
            }
        }, queryList.toArray());
        return list;
    }

    @Override
    public List<Map<String, Object>> myCollectByName(String json) {
        List list = null;
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer uid = jsonObject.getInteger("uid");
        String name = jsonObject.getString("name");
        StringBuffer sb = new StringBuffer();
        List<Object> queryList = new ArrayList<Object>();

        if (uid == 17 || uid == 2 || uid == 3) {
            sb.append("SELECT yx_matorn.id,yx_matorn.name,yx_matorn.household,yx_matorn.born,yx_matorn.zodiac,yx_matorn.constellation,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.day,yx_bussiness.creat_time,yx_bussiness.assess,yx_contact.phone,");
            sb.append("yx_period.states,yx_bussiness.iscollect,yx_period.month from yx_matorn left JOIN yx_contact on (yx_matorn.id=yx_contact.mid)");
            sb.append(" left JOIN yx_bussiness  on (yx_matorn.id=yx_bussiness.mid) left JOIN yx_period  on (yx_matorn.id=yx_period.mid)  where yx_bussiness.iscollect=1 ");


        } else {
            sb.append("SELECT yx_matorn.id,yx_matorn.name,yx_matorn.household,yx_matorn.born,yx_matorn.zodiac,yx_matorn.constellation,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.day,yx_bussiness.creat_time,yx_bussiness.assess,yx_contact.phone,");
            sb.append("yx_period.states,yx_bussiness.iscollect,yx_period.month from yx_matorn left JOIN yx_contact on (yx_matorn.id=yx_contact.mid)");
            sb.append(" left JOIN yx_bussiness  on (yx_matorn.id=yx_bussiness.mid) left JOIN yx_period  on (yx_matorn.id=yx_period.mid) where  yx_bussiness.iscollect=1 and yx_matorn.uid= ?");
            queryList.add(uid);

        }

        String sql2 = "select name from yx_matorn where name = ? ";
        try {
            List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{name}, String.class);

            if (name.equals(list1.get(0))) {
                sb.append(" and  yx_matorn.name=?  ");
                queryList.add(list1.get(0));
            } else {

            }
        } catch (Exception e) {
            if (name.length() == 1) {
                String xing = name.substring(0, 1);
                sb.append(" and  yx_matorn.name like ?  ");
                queryList.add(xing + "%");
            } else {
                return list;
            }
        }

        sb.append(" order by yx_bussiness.creat_time desc");

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                String birthTime = rs.getString("born");
                Long age = getAge(birthTime);
                mp.put("age", age);

                String month = rs.getString("month");
                Map<String, String> map = new HashMap<String, String>();
                String newmap = "";
                if (month != null && month != "") {

                    try {
                        newmap = "[" + getNewMap(month) + "]";
                        String sql_newmap = "update  yx_period set month=? where mid=?";
                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));
                        map = getAll(newmap);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    map = gNull();
                }

                mp.put("grade", rs.getString("grade"));
                mp.put("rise", 2);
                String household = rs.getString("household");
                String city = "";
                if (household.length() > 3) {
                    String h1 = household.substring(0, 2);
                    String h2 = household.substring(3, 5);
                    city = h1 + h2;
                } else {

                    city = household.substring(0, 2);

                }

                mp.put("id", rs.getInt("id"));
                mp.put("name", rs.getString("name"));
                mp.put("city", city);
                mp.put("month", map);
                mp.put("phone", rs.getString("phone"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("constellation", rs.getString("constellation"));
                mp.put("states", rs.getString("states"));
                mp.put("photo", rs.getString("photo"));
                mp.put("assess", rs.getInt("assess"));
                mp.put("iscollect", rs.getInt("iscollect"));
                return mp;
            }
        }, queryList.toArray());
        return list;
    }
}

