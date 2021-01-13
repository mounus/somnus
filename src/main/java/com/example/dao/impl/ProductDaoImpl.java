package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.PeriodUtil.*;

@Repository
public class ProductDaoImpl implements ProductDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

//
//    public int countAll() {
//
//        StringBuffer sb = new StringBuffer();
//        sb.append("select count(*) from yx_matorn INNER JOIN yx_contact  on (yx_matorn.id=yx_contact.mid) INNER JOIN yx_bussiness  on (yx_matorn.id=yx_bussiness.mid)");
//        sb.append("INNER JOIN yx_period on (yx_matorn.id=yx_period.mid) where  yx_bussiness.shelf=1");
//
//
//        return this.jdbcTemplate.queryForObject(sb.toString(), Integer.class);
//
//    }
//
//    @Override
//    public Map<String, Object> getAdmin(String json) {
//
//        JSONObject jsonObject = JSON.parseObject(json);
//        Integer start = jsonObject.getInteger("start");
//        List<Map<String, Object>> list = null;
//        StringBuffer sb = new StringBuffer();
//        sb.append("select yx_matorn.id,yx_matorn.uid,yx_matorn.lid,yx_matorn.name,yx_contact.phone,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.number,yx_bussiness.day,yx_period.month,");
//        sb.append("yx_bussiness.creat_time,yx_bussiness.reportPdf,yx_origin.institution_name,yx_bussiness.assess,yx_bussiness.trueday,yx_bussiness.isblack,yx_bussiness.isevaluate,yx_bussiness.shelf  from yx_matorn INNER JOIN yx_contact  on(yx_matorn.id=yx_contact.mid)INNER JOIN yx_bussiness  on(yx_matorn.id=yx_bussiness.mid)  ");
//        sb.append(" INNER JOIN yx_origin on (yx_matorn.id=yx_origin.mid)");
//        sb.append("INNER JOIN yx_period  on (yx_matorn.id=yx_period.mid) where  yx_bussiness.shelf=1   ");
//        List<Object> queryList = new ArrayList<Object>();
//        Map<String, Object> map = new HashMap<String, Object>();
//        Integer num = 10;//每页条数；
//        map.put("num", num);
//        String aa = "";
//
//        if (start.equals(1)) {
//            Integer startnum = 0;
//            aa = "  limit  " + startnum + "," + num;
//        } else {
//            aa = "  limit  " + (start - 1) * num + "," + num;
//        }
//        int count = countAll();//获取总条数
//
//        int page = 0;
//        if (count % num == 0) {
//            page = count / num;
//        } else {
//            page = count / num + 1;
//        }
//
//        //  String sqls="select yx_matorn.id from yx_matorn INNER JOIN yx_contact  on(yx_matorn.id=yx_contact.mid)INNER JOIN yx_bussiness  on(yx_matorn.id=yx_bussiness.mid) order by id desc limit ?,1";
//        // Integer ss=this.jdbcTemplate.queryForObject(sqls,Integer.class,start*num-10);
//        //  System.out.println("ss_____________________"+ss);
//        //  String aa=" and yx_matorn.id<?  ";
//        // queryList.add(ss);
//
//        map.put("page", page);
//        map.put("count", count);
//
//        // sb.append("  and yx_bussiness.isblack =0 order by yx_bussiness.creat_time desc limit 10");
//        sb.append("  order by yx_bussiness.creat_time desc ");
//        sb.append(aa);
//
//
//        String sql_user = "select name from yx_user where power = 1";
//        List<Object> listagent = (List) this.jdbcTemplate.queryForList(sql_user.toString());
//        map.put("listagent", listagent);
//        String sql_institution = "select name from yx_mechanism ";
//        List<Object> institution_nameList = (List) this.jdbcTemplate.queryForList(sql_institution.toString());
//        map.put("institution_nameList", institution_nameList);
//
//        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                Map<String, Object> mp = new HashMap<String, Object>();
//
//                String month = rs.getString("month");
//                Map<String, String> map = new HashMap<String, String>();
//                String newmap = "";
//                if (month != null && month != "") {
//                    try {
//                        newmap = "[" + getNewMap(month) + "]";
//                        String sql_newmap = "update  yx_period set month=? where mid=?";
//                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));//清除前几个月的month
//                        map = getAll(newmap);
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    map = gNull();
//                }
//                mp.put("grade", rs.getString("grade"));
//                mp.put("assess", rs.getString("assess"));
//                mp.put("id", rs.getInt("id"));
//                mp.put("isblack", rs.getInt("isblack"));
//                Integer uid = rs.getInt("uid");
//                String sql_a = "select name from yx_user where id = ? ";
//                String agent = jdbcTemplate.queryForObject(sql_a, String.class, uid);
//
//                Integer id = rs.getInt("id");
//                String sql_lid = "select lid from yx_matorn where id = ? ";
//                try {
//                    Integer lid = jdbcTemplate.queryForObject(sql_lid, Integer.class, id);
//                    if (lid == null || lid == 0 || lid.equals("")) {
//                        mp.put("l_name", agent);
//                        mp.put("uid", rs.getInt("uid"));
//                        mp.put("agent", "");
//                    } else {
//                        String sql_lname = "select name from yx_user where id = ? ";
//                        String l_name = jdbcTemplate.queryForObject(sql_lname, String.class, lid);
//                        mp.put("uid", lid);
//                        mp.put("l_name", l_name);
//                        mp.put("agent", agent);
//                    }
//
//                } catch (Exception e) {
//
//                }
//                mp.put("name", rs.getString("name"));
//                mp.put("month", map);
//                mp.put("number", rs.getString("number"));
//                mp.put("photo", rs.getString("photo"));
//                mp.put("creat_time", rs.getString("creat_time"));
//                mp.put("trueday", rs.getInt("trueday"));
//                mp.put("day", rs.getInt("day"));
//                mp.put("phone", rs.getString("phone"));
//                mp.put("isevaluate", rs.getInt("isevaluate"));
//                mp.put("shelf", rs.getInt("shelf"));
//                mp.put("institution_name", rs.getString("institution_name"));
//                mp.put("reportPdf", rs.getString("reportPdf"));
//
//                return mp;
//            }
//        }, queryList.toArray());
//        map.put("list", list);
//
//        return map;
//
//    }
//
//    public int countNumber(String number) {
//        StringBuffer sb = new StringBuffer();
//        sb.append("select count(*) from yx_matorn INNER JOIN yx_contact  on (yx_matorn.id=yx_contact.mid) INNER JOIN yx_bussiness  on (yx_matorn.id=yx_bussiness.mid)");
//        sb.append("INNER JOIN yx_period on (yx_matorn.id=yx_period.mid) where yx_bussiness.shelf =1 ");
//        List<Object> queryList = new ArrayList<Object>();
//        if (number.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
//            sb.append(" and yx_matorn.id = ? ");
//            queryList.add(number);
//        }
//        if (number.matches("^[\\u4e00-\\u9fa5]+$")) {
//
//            String sql2 = "select name from yx_matorn where name = ? ";
//            try {
//                List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{number}, String.class);
//                if (number.equals(list1.get(0))) {
//
//                    sb.append("  and yx_matorn.name= ?  ");
//
//                    queryList.add(list1.get(0));
//                } else {
//
//                }
//            } catch (Exception e) {
//
//                String name = number.substring(0, 1);
//                sb.append("  and yx_matorn.name like ?   ");
//                queryList.add(name + "%");
//            }
//
//        }
//        if (number.matches(".*\\p{Alpha}.*")) {
//            sb.append(" and yx_bussiness.number= ? ");
//            queryList.add(number);
//        }
//
//        return this.jdbcTemplate.queryForObject(sb.toString(), Integer.class, queryList.get(0));
//    }
//
//
//    @Override
//    public Map<String, Object> getByNumber(String json) {
//        List<Map<String, Object>> list = null;
//        JSONObject jsonObject = JSON.parseObject(json);
//        String number = jsonObject.getString("number");
//        Integer start = jsonObject.getInteger("start");
//
//        StringBuffer sb = new StringBuffer();
//        sb.append("select yx_matorn.id,yx_matorn.id,yx_matorn.uid,yx_matorn.name,yx_contact.phone,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.number,yx_bussiness.day,yx_period.month,");
//        sb.append("yx_bussiness.creat_time,yx_origin.institution_name,yx_bussiness.reportPdf,yx_bussiness.assess,yx_bussiness.trueday,yx_bussiness.isblack,yx_bussiness.isevaluate,yx_bussiness.shelf from yx_matorn INNER JOIN yx_contact  on(yx_matorn.id=yx_contact.mid)INNER JOIN yx_bussiness  on(yx_matorn.id=yx_bussiness.mid)  ");
//        sb.append(" INNER JOIN yx_origin on (yx_matorn.id=yx_origin.mid)");
//        sb.append("INNER JOIN yx_period  on (yx_matorn.id=yx_period.mid) where yx_bussiness.shelf=1 ");;
//
//        List<Object> queryList = new ArrayList<Object>();
//        Map<String, Object> map = new HashMap<String, Object>();
//        if (number.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
//            sb.append(" and yx_matorn.id =? ");
//            queryList.add(Integer.valueOf(number));
//        }
//        if (number.matches("^[\\u4e00-\\u9fa5]+$")) {
//
//            String sql2 = "select name from yx_matorn where name = ? ";
//            try {
//                List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{number}, String.class);
//                if (number.equals(list1.get(0))) {
//
//                    sb.append("  and yx_matorn.name= ?  ");
//
//                    queryList.add(list1.get(0));
//                } else {
//
//                }
//            } catch (Exception e) {
//                if (number.length() == 1) {
//                    String name = number.substring(0, 1);
//                    sb.append("  and yx_matorn.name like ?   ");
//                    queryList.add(name + "%");
//                } else {
//                    return map;
//                }
//
//
//            }
//        }
//        if (number.matches(".*\\p{Alpha}.*")) {
//            sb.append(" and yx_bussiness.number=? ");
//            queryList.add(number);
//        }
//
//        Integer num = 10;//每页条数；
//        map.put("num", num);
//        String aa = "";
//
//        if (start.equals(1)) {
//            Integer startnum = 0;
//            aa = "  limit  " + startnum + "," + num;
//        } else {
//            aa = "  limit  " + (start - 1) * num + "," + num;
//        }
//        int count = countNumber(number);//获取总条数
//
//        int page = 0;
//        if (count % num == 0) {
//            page = count / num;
//        } else {
//            page = count / num + 1;
//        }
//        map.put("page", page);
//        map.put("count", count);
//
//        sb.append(" order by yx_bussiness.creat_time desc");
//        sb.append(aa);
//
//        String sql_user = "select name from yx_user where power = 1";
//        List<Object> listagent = (List) this.jdbcTemplate.queryForList(sql_user.toString());
//        map.put("listagent",listagent);
//        String sql_institution = "select name from yx_mechanism ";
//        List<Object> institution_nameList = (List) this.jdbcTemplate.queryForList(sql_institution.toString());
//        map.put("institution_nameList",institution_nameList);
//
//        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                Map<String, Object> mp = new HashMap<String, Object>();
//
//                String month = rs.getString("month");
//                Map<String, String> map = new HashMap<String, String>();
//                if (month != null && month != "") {
//                    try {
//                        map = getAll(month);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//
//
//                    map = gNull();
//                }
//
//                mp.put("grade", rs.getString("grade"));
//                mp.put("assess", rs.getString("assess"));
//                mp.put("id", rs.getInt("id"));
//                mp.put("uid", rs.getInt("uid"));
//                Integer uid = rs.getInt("uid");
//                String sql_a = "select name from yx_user where id = ? ";
//                String agent = jdbcTemplate.queryForObject(sql_a, String.class, uid);
//                Integer id = rs.getInt("id");
//                String sql_lid = "select lid from yx_matorn where id = ? ";
//                mp.put("isblack", rs.getInt("isblack"));
//                mp.put("shelf", rs.getInt("shelf"));
//                try {
//                    Integer lid = jdbcTemplate.queryForObject(sql_lid, Integer.class, id);
//                    if (lid == null || lid == 0 || lid.equals("")) {
//                        mp.put("l_name", agent);
//                        mp.put("uid", rs.getInt("uid"));
//                        mp.put("agent", "");
//                    } else {
//                        String sql_lname = "select name from yx_user where id = ? ";
//                        String l_name = jdbcTemplate.queryForObject(sql_lname, String.class, lid);
//                        mp.put("uid", lid);
//                        mp.put("l_name", l_name);
//                        mp.put("agent", agent);
//                    }
//
//                } catch (Exception e) {
//
//                }
//                mp.put("name", rs.getString("name"));
//                mp.put("month", map);
//                mp.put("number", rs.getString("number"));
//                mp.put("photo", rs.getString("photo"));
//                mp.put("creat_time", rs.getString("creat_time"));
//                mp.put("day", rs.getInt("day"));
//                mp.put("trueday", rs.getInt("trueday"));
//                mp.put("phone", rs.getString("phone"));
//                mp.put("isevaluate", rs.getInt("isevaluate"));
//                mp.put("shelf", rs.getInt("shelf"));
//                mp.put("isblack", rs.getInt("isblack"));
//                mp.put("reportPdf", rs.getString("reportPdf"));
//                mp.put("institution_name", rs.getString("institution_name"));
//                return mp;
//            }
//        }, queryList.get(0));
//        map.put("list", list);
//        return map;
//    }
//
//    @Override
//    public Map<String, Object> getByGrade(String json) {
//        JSONObject jsonObject = JSON.parseObject(json);//转换类型
//        List<Map<String, Object>> list = null;
//        StringBuffer sb = new StringBuffer();
//        StringBuffer str = new StringBuffer();
//        str.append("select count(*) from yx_matorn INNER JOIN yx_contact  on (yx_matorn.id=yx_contact.mid) INNER JOIN yx_bussiness  on (yx_matorn.id=yx_bussiness.mid)");
//        str.append(" INNER JOIN yx_origin on (yx_matorn.id=yx_origin.mid)");
//        str.append("INNER JOIN yx_period on (yx_matorn.id=yx_period.mid) where yx_bussiness.shelf = 1  ");
//        String grade = jsonObject.getString("grade");
//        String agent = jsonObject.getString("agent");
//        String sort = jsonObject.getString("sort");
//        Integer start = jsonObject.getInteger("start");
//        String institution_name = jsonObject.getString("institution_name");
//        System.out.println("____product______");
//        System.out.println("grade__________"+grade);
//        System.out.println("sort__________"+sort);
//        System.out.println("start__________"+start);
//        System.out.println("agent__________"+agent);
//        System.out.println("institution_name__________"+institution_name);
//        System.out.println("_________________");
//        sb.append("select yx_matorn.id,yx_matorn.uid,yx_matorn.name,yx_contact.phone,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.number,yx_bussiness.day,yx_bussiness.trueday,yx_period.month,yx_bussiness.reportPdf,yx_origin.institution_name,");
//        sb.append("yx_bussiness.creat_time,yx_bussiness.assess,yx_bussiness.isblack,yx_bussiness.isevaluate,yx_bussiness.shelf from yx_matorn INNER JOIN yx_contact  on(yx_matorn.id=yx_contact.mid)INNER JOIN yx_bussiness  on(yx_matorn.id=yx_bussiness.mid)  ");
//        sb.append(" INNER JOIN yx_origin on (yx_matorn.id=yx_origin.mid)");
//        sb.append("INNER JOIN yx_period  on (yx_matorn.id=yx_period.mid) where yx_bussiness.shelf = 1 ");
//        List<Object> queryList = new ArrayList<Object>();
//
//
//        if (grade != "" && grade != null && !grade.isEmpty()&&grade!="业务等级") {
//            sb.append(" and yx_bussiness.grade=?");
//            str.append(" and yx_bussiness.grade=?");
//            queryList.add(grade);
//        } else {
//
//        }
//
//        if (institution_name != null && institution_name != "" && !institution_name.isEmpty()) {
//            sb.append(" and yx_origin.institution_name= ? ");
//            str.append(" and yx_origin.institution_name= ? ");
//            queryList.add(institution_name);
//        } else {
//
//        }
//
//        Integer id = 0;
//        if (agent != "" && agent != null && !agent.isEmpty()&&agent!="经理人") {
//            String sql_u = "select id from yx_user where name=? ";
//
//            id = this.jdbcTemplate.queryForObject(sql_u, Integer.class, agent);
//            if (id != 22) {
//                sb.append("  and yx_matorn.uid=?  and yx_matorn.lid<>0  ");
//                str.append("  and yx_matorn.uid = ?  and yx_matorn.lid<>0  ");
//                queryList.add(id);
//            } else {
////                sb.append("  and yx_matorn.uid not in (10,11,12,13)  and yx_matorn.lid=0  and yx_bussiness.isblack=0 ");
//                sb.append("   and yx_matorn.lid=0   ");//没有分配的月嫂lid=0
//                str.append("   and yx_matorn.lid=0  ");
//            }
//
//        } else {
//
//        }
//
//
//        Map<String, Object> map = new HashMap<String, Object>();
//        int count = this.jdbcTemplate.queryForObject(str.toString(), Integer.class, queryList.toArray());
//        Integer num = 10;//每页条数；
//        map.put("num", num);
//        map.put("count", count);
//        String aa = "";
//        if (start.equals(1)) {
//            Integer startnum = 0;
//            aa = "  limit  " + startnum + "," + num;
//        } else {
//            aa = "  limit  " + (start - 1) * num + "," + num;
//        }
//        Integer page = 0;
//        if (count % num == 0) {
//            page = count / num;
//        } else {
//            page = count / num + 1;
//        }
//        map.put("page", page);
//
//
//        if (sort.equals("desc")) {
//            sb.append("   order by yx_bussiness.day desc");
//            sb.append(aa);
//        } else {
//            sb.append("   order by yx_bussiness.day asc");
//            sb.append(aa);
//
//        }
//        String sql_user = "select name from yx_user where power = 1";
//        List<Object> listagent = (List) this.jdbcTemplate.queryForList(sql_user.toString());
//        map.put("listagent",listagent);
//        String sql_institution = "select name from yx_mechanism ";
//        List<Object> institution_nameList = (List) this.jdbcTemplate.queryForList(sql_institution.toString());
//        map.put("institution_nameList",institution_nameList);
//
//        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
//            @Override
//            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                Map<String, Object> mp = new HashMap<String, Object>();
//
//                String month = rs.getString("month");
//                Map<String, String> map = new HashMap<String, String>();
//                String newmap = "";
//                if (month != null && month != "") {
//                    try {
//                        newmap = "[" + getNewMap(month) + "]";
//
//                        String sql_newmap = "update  yx_period set month=? where mid=?";
//                        jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));//清除前几个月的month
//                        map = getAll(newmap);
//
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    map = gNull();
//                }
//
//                Integer uid = rs.getInt("uid");
//                String sql_a = "select name from yx_user where id = ? ";
//                String agent = jdbcTemplate.queryForObject(sql_a, String.class, uid);
//
//                Integer id = rs.getInt("id");
//                String sql_lid = "select lid from yx_matorn where id = ? ";
//                try {
//                    Integer lid = jdbcTemplate.queryForObject(sql_lid, Integer.class, id);
//                    if (lid == null || lid == 0 || lid.equals("")) {
//                        mp.put("l_name", agent);
//                        mp.put("uid", rs.getInt("uid"));
//                        mp.put("agent", "");
//                    } else {
//                        String sql_lname = "select name from yx_user where id = ? ";
//                        String l_name = jdbcTemplate.queryForObject(sql_lname, String.class, lid);
//                        mp.put("uid", lid);
//                        mp.put("l_name", l_name);
//                        mp.put("agent", agent);
//                    }
//
//                } catch (Exception e) {
//
//                }
//
//                mp.put("grade", rs.getString("grade"));
//                mp.put("assess", rs.getString("assess"));
//                mp.put("id", rs.getInt("id"));
//                mp.put("uid", rs.getInt("uid"));
//                mp.put("name", rs.getString("name"));
//                mp.put("month", map);
//                mp.put("number", rs.getString("number"));
//                mp.put("photo", rs.getString("photo"));
//                mp.put("creat_time", rs.getString("creat_time"));
//                mp.put("day", rs.getInt("day"));
//
//                mp.put("phone", rs.getString("phone"));
//                mp.put("isevaluate", rs.getInt("isevaluate"));
//                mp.put("shelf", rs.getInt("shelf"));
//                mp.put("isblack", rs.getInt("isblack"));
//                mp.put("reportPdf", rs.getString("reportPdf"));
//                mp.put("institution_name", rs.getString("institution_name"));
//                return mp;
//
//            }
//        }, queryList.toArray());
//        map.put("list", list);
//        return map;
//    }


    @Override
    public Map<String, Object> getAdmin(String json) {
        return null;
    }

    @Override
    public Map<String, Object> getByNumber(String json) {
        return null;
    }

    @Override
    public Map<String, Object> getByGrade(String json) {
        return null;
    }
}
