package com.example.dao.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.EmployeesDao;
import com.example.entiy.User;

import javafx.util.BuilderFactory;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import springfox.documentation.spring.web.json.Json;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.BASE64.getPassword;
import static com.example.util.BASE64.maskPhone;
import static com.example.util.PageUtil.getAllPage;

@Repository
public class EmployeesDaoImpl implements EmployeesDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;


    public int countAllEmployees(String somnus) {

        StringBuffer sb = new StringBuffer();

        sb.append("select count(*) from yx_user where  1=1 ");

        if (somnus.equals("all")) {
            sb.append(" and power<>30 and power<>40");
        }
        if (somnus.equals("agent")) {
            sb.append(" and power=1");
        }
        if (somnus.equals("login")) {
            sb.append(" and power=3");
        }
        if (somnus.equals("quality")) {
            sb.append(" and power=2");
        }
        if (somnus.equals("bussiness")) {
            sb.append(" and power=4");
        }
        if (somnus.equals("post")) {
            sb.append(" and power=6");
        }
        if (somnus.equals("mechanism")) {
            sb.append(" and power=30");
        }
        if (somnus.equals("channel")) {
            sb.append(" and power=40");
        }

        return this.jdbcTemplate.queryForObject(sb.toString(), Integer.class);

    }


    @SneakyThrows
    @Override
    public Map<String, Object> getAllEmployees(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer start = jsonObject.getInteger("start");
        String somnus = jsonObject.getString("somnus");
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = null;
        List<Map<String, Object>> powerList = null;
        StringBuffer sb = new StringBuffer();


        int count = countAllEmployees(somnus);//获取总条数
        map.put("count", count);
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        map.put("num", page.get("num"));
        String sql_page = page.get("sql_page").toString();
        map.put("page", page.get("page"));

        sb.append("select id,phone,name,post,power,mechanism_name,creat_time from yx_user where  1=1 ");

        if (somnus.equals("all")) {
            sb.append("and power<>30 and power<>40");
        }
        if (somnus.equals("agent")) {
            sb.append(" and power=1 or power=10");
        }
        if (somnus.equals("login")) {
            sb.append(" and power=3");
        }
        if (somnus.equals("quality")) {
            sb.append(" and power=2");
        }
        if (somnus.equals("bussiness")) {
            sb.append(" and power=4 or power=20");
        }
        if (somnus.equals("post")) {
            sb.append(" and power=6");
        }
        if (somnus.equals("service")) {
            sb.append(" and power=7");
        }
        if (somnus.equals("mechanism")) {
            sb.append(" and power=30");
        }
        if (somnus.equals("channel")) {
            sb.append(" and power=40");
        }

        sb.append(" order by id desc");
        sb.append(sql_page);


        String sql = "select post,power from yx_user group by power order by power ";

        powerList = this.jdbcTemplate.query(sql.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("post", rs.getString("post"));
                mp.put("power", rs.getString("power"));
                return mp;
            }
        });


        map.put("powerList", powerList);

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("id", rs.getInt("id"));

                String phone = rs.getString("phone");
                if (phone.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
                    mp.put("phone", maskPhone(phone));
                } else {
                    mp.put("phone", phone);
                }
                mp.put("name", rs.getString("name"));
                mp.put("post", rs.getString("post"));
                mp.put("power", rs.getString("power"));
                mp.put("mechanism_name", rs.getString("mechanism_name"));
                mp.put("creat_time", rs.getString("creat_time"));
                return mp;
            }
        });

        map.put("list", list);

        return map;
    }

    @Override
    public Integer addEmployees(User user) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_user (phone,password,name,post,power,mechanism_name,creat_time)");
        sb.append("value(?,?,?,?,?,?,?)");
        int states = 0;
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creat_time = time.format(new Date());

        if (user.getPower() == 30) {
            String sql = "select phone from yx_user where power=30 order by id desc limit 1 ";
            String phone = this.jdbcTemplate.queryForObject(sql, String.class);
            int index = phone.indexOf("g");
            String newStr = phone.substring(index + 1);
            Integer number = Integer.valueOf(newStr) + 1;
            String newPhone = null;
            if (number > 100) {
                newPhone = "haoyuexiang" + number.toString();
            } else {
                newPhone = "haoyuexiang" + "0" + number.toString();
            }
            String password = getPassword();
            states = this.jdbcTemplate.update(sb.toString(), newPhone, password, user.getName(), user.getPost(), user.getPower(), user.getMechanism_name(), creat_time);
        }
        if (user.getPower() == 40) {
            String password = getPassword();
            states = this.jdbcTemplate.update(sb.toString(), user.getPhone(), password, user.getName(), user.getPost(), user.getPower(), user.getMechanism_name(), creat_time);
        }

        if (user.getPower() != 30 && user.getPower() != 40) {
            String password = "haoyuexiang123";
            String mechanism_name = "好悦享";
            states = this.jdbcTemplate.update(sb.toString(), user.getPhone(), password, user.getName(), user.getPost(), user.getPower(), mechanism_name, creat_time);
        }

        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public Integer updateEmployees(User user) {

        StringBuffer sb = new StringBuffer();
        sb.append("update yx_user set phone=?,password=?,name=?,post=?,power=?,mechanism_name=? where id=?");
        int states = this.jdbcTemplate.update(sb.toString(), user.getPhone(), user.getPassword(), user.getName(), user.getPost(), user.getPower(), user.getMechanism_name(), user.getId());
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public User findUserOne(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer id = jsonObject.getInteger("id");
        String sql = "select id,phone,password,name,post,power,mechanism_name,creat_time from yx_user where id=?";
        List<User> list = jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper(User.class));
        if (list != null && list.size() > 0) {
            User user = list.get(0);
            return user;
        } else {
            return null;
        }
    }

    @Override
    public int deleteEmployees(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer id = jsonObject.getInteger("id");
        String sql = "delete from yx_user where id=? ";
        int states = this.jdbcTemplate.update(sql, id);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @SneakyThrows
    @Override
    public Map<String, Object> getEmployeesList(String json) {

        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");
        Integer did = jsonObject.getInteger("did");
        Integer pid = jsonObject.getInteger("pid");
        String number = jsonObject.getString("number");
        Integer start = jsonObject.getInteger("start");

        Map<String, Object> map = new HashedMap();
        String sql_department = "select department,did from yx_userType where cid=2 and did in (6,1,2,3,4) group by did ";
        List<Map<String, Object>> departmentList = jdbcTemplate.queryForList(sql_department);


        String sql_department_size = "select count(*) from yx_user where company =2 and department=? ";
        List<Map<String, Object>> department = new ArrayList<>();
        for (int i = 0; i < departmentList.size(); i++) {
            Map<String, Object> departmentMap = new HashedMap();
            Integer count = jdbcTemplate.queryForObject(sql_department_size, Integer.class, departmentList.get(i).get("did"));
            departmentMap.put("name", departmentList.get(i).get("department"));
            departmentMap.put("count", count);
            department.add(departmentMap);
        }
        map.put("department", department);


        StringBuffer sb = new StringBuffer();
        sb.append("select id,phone,name,post,power,password,photo,creat_time,description,company,department,sex from yx_user");
        sb.append(" where power<>30 and power<>40 ");

        StringBuffer sb_count = new StringBuffer();
        sb_count.append(" select count(*) from yx_user where power<>30 and power<>40  ");


        List<String> queryList = new ArrayList<>();
        StringBuffer sb_query = new StringBuffer();
        if (cid != 0) {
            sb_query.append(" and company=? ");
            queryList.add(cid.toString());
            if (did != 0) {
                sb_query.append(" and department=? ");
                queryList.add(did.toString());
                if (pid != 0) {
                    sb_query.append(" and power=? ");
                    queryList.add(pid.toString());
                } else {

                }
            } else {

            }
        } else {

        }

        if (number.equals("null") || number.isEmpty() || number == "" || number == null) {

        } else {
            if (number.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
                if (number.length() > 8) {
                    //电话
                    sb_query.append(" and phone = ? ");
                    queryList.add(number);
                } else {
                    //id
                    sb_query.append(" and id = ? ");
                    queryList.add(number);
                }
            }
            //汉字
            if (number.matches("^[\\u4e00-\\u9fa5]+$")) {
                String sql2 = "select name from yx_user where name =? ";
                try {
                    List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{number}, String.class);
                    if (number.equals(list1.get(0))) {
                        sb_query.append("  and name=?  ");
                        queryList.add(list1.get(0));
                    } else {

                    }
                } catch (Exception e) {
                    if (number.length() == 1) {
                        String name = number.substring(0, 1);
                        sb_query.append(" and name like ?  ");
                        queryList.add(name + "%");
                    } else {
                        return map;
                    }

                }
            }

        }

        sb_query.append(" order by id desc ");

        sb_count.append(sb_query);
        sb.append(sb_query);
        Integer count = this.jdbcTemplate.queryForObject(sb_count.toString(), Integer.class, queryList.toArray());
        List<Map<String, Object>> pageList = new ArrayList<>();
        Map<String, Object> pageMap = new HashMap<>();
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        pageMap.put("count", count);
        String sql_page = page.get("sql_page").toString();
        sb.append(sql_page);
        pageMap.put("page", page.get("page"));
        pageMap.put("num", page.get("num"));
        pageList.add(pageMap);
        map.put("pageList", pageList);

        Map<String, Object> companyMap = employeesCondition();
        List<Map<String, Object>> companyList = (List<Map<String, Object>>) companyMap.get("companyList");
        map.put("companyList", companyList);

        List<Map<String, Object>> list = new ArrayList<>();
        System.out.println("sb = " + sb);
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("photo", rs.getString("photo"));
                mp.put("uid", rs.getInt("id"));
                mp.put("name", rs.getString("name"));
                mp.put("phone", rs.getString("phone"));
                if (rs.getString("sex") != null) {
                    if (rs.getString("sex").equals("0")) {
                        mp.put("sex", "女");
                    }else {
                        mp.put("sex", "男");
                    }
                } else {
                    mp.put("sex", "未知");
                }


                if (rs.getInt("company")!=0){
                    String sql_company = "select company,department,post from yx_userType  where cid=? and did=? and pid=?";
                    Map<String, Object> cMap = jdbcTemplate.queryForMap(sql_company, rs.getInt("company"),rs.getInt("department"),rs.getInt("power"));
                    mp.put("company", cMap.get("company"));
                    mp.put("department", cMap.get("department"));
                    mp.put("post", cMap.get("post"));
                }else {
                    mp.put("company", null);
                    mp.put("department",null);
                    mp.put("post",null);

                }
                mp.put("password", rs.getString("password"));
                mp.put("description", rs.getString("description"));
                mp.put("creat_time", rs.getString("creat_time"));

                return mp;
            }
        },queryList.toArray());
        map.put("list", list);
        return map;
    }

    @Override
    public Map<String, Object> employeesCondition() {
        Map<String, Object> map = new HashedMap();


        String sql_company = "select company,cid from yx_userType group by cid ";
        List<Map<String, Object>> companyList = this.jdbcTemplate.queryForList(sql_company.toString());
        String sql_department = "select department,did from yx_userType where cid=? group by did";
        String sql_post = "select post,pid from yx_userType where cid=? and  did=? group by pid";


//        List<Map<String, Object>> coList = new ArrayList<>();
//        for (int i = 0; i < companyList.size(); i++) {
//
//            List<Map<String, Object>> departmentList = this.jdbcTemplate.queryForList(sql_department.toString(), companyList.get(i).get("cid"));
//
//            Map<String, Object> coMap = new HashedMap();
//            List<Map<String, Object>> deList = new ArrayList<>();
//            for (int j = 0; j < departmentList.size(); j++) {
//                Map<String, Object> deMap = new HashedMap();
//                List<Map<String, Object>> postList = this.jdbcTemplate.queryForList(sql_post, companyList.get(i).get("cid"), departmentList.get(j).get("did"));
//                List<Map<String, Object>> poList = new ArrayList<>();
//                for (int k = 0; k < postList.size(); k++) {
//                    Map<String, Object> poMap = new HashedMap();
//                    poMap.put("name", postList.get(k).get("post"));
//                    poMap.put("id", postList.get(k).get("pid"));
//                    poList.add(poMap);
//                }
//                deMap.put("name", departmentList.get(j).get("department"));
//                deMap.put("id", departmentList.get(j).get("did"));
//                deMap.put("children", poList);
//                deList.add(deMap);
//            }
//            coMap.put("name", companyList.get(i).get("company"));
//            coMap.put("id", companyList.get(i).get("cid"));
//            coMap.put("children", deList);
//            coList.add(coMap);
//        }
//        map.put("companyList", coList);
//


      List<Map<String, Object>> coList = new ArrayList<>();
        for (int i = 0; i < companyList.size(); i++) {

            List<Map<String, Object>> departmentList = this.jdbcTemplate.queryForList(sql_department.toString(), companyList.get(i).get("cid"));
            Map<String, Object> coMap = new HashedMap();
            List<Map<String, Object>> deList = new ArrayList<>();
            for (int j = 0; j < departmentList.size(); j++) {
                Map<String, Object> deMap = new HashedMap();
                List<Map<String, Object>> postList = this.jdbcTemplate.queryForList(sql_post, companyList.get(i).get("cid"), departmentList.get(j).get("did"));
                List<Map<String, Object>> poList = new ArrayList<>();
                for (int k = 0; k < postList.size(); k++) {
                    Map<String, Object> poMap = new HashedMap();
                    poMap.put("name", postList.get(k).get("post"));
                    poMap.put("id", postList.get(k).get("pid"));
                    poList.add(poMap);
                }



                deMap.put("name", departmentList.get(j).get("department"));
                deMap.put("id", departmentList.get(j).get("did"));
                deMap.put("children", poList);
                deList.add(deMap);
            }
            coMap.put("name", companyList.get(i).get("company"));
            coMap.put("id", companyList.get(i).get("cid"));
            coMap.put("children", deList);
            coList.add(coMap);
        }
        map.put("companyList", coList);




        String sql_address = "select address,aid from yx_store group by aid order by aid ";
        List<Map<String, Object>> addressList = this.jdbcTemplate.queryForList(sql_address.toString());
        String sql_store = "select store,sid from yx_store where aid=? group by sid order by sid ";

        List<Map<String, Object>> adList = new ArrayList<>();
        for (int i = 0; i < addressList.size(); i++) {

            //----------门店
            List<Map<String, Object>> storeList = this.jdbcTemplate.queryForList(sql_store.toString(),addressList.get(i).get("aid"));
            Map<String, Object> adMap = new HashedMap();
            List<Map<String, Object>> stList = new ArrayList<>();
            for (int j = 0; j < storeList.size(); j++) {
                Map<String, Object> stMap = new HashedMap();
                stMap.put("name", storeList.get(j).get("store"));
                stMap.put("id", storeList.get(j).get("sid"));
                stList.add(stMap);
            }
            //--------


           //---------部门
            List<Map<String, Object>> departmentList = this.jdbcTemplate.queryForList(sql_department.toString(), companyList.get(i).get("cid"));
            Map<String, Object> coMap = new HashedMap();
            List<Map<String, Object>> deList = new ArrayList<>();
            for (int j = 0; j < departmentList.size(); j++) {
                Map<String, Object> deMap = new HashedMap();
                List<Map<String, Object>> postList = this.jdbcTemplate.queryForList(sql_post, companyList.get(i).get("cid"), departmentList.get(j).get("did"));
                List<Map<String, Object>> poList = new ArrayList<>();
                for (int k = 0; k < postList.size(); k++) {
                    Map<String, Object> poMap = new HashedMap();
                    poMap.put("name", postList.get(k).get("post"));
                    poMap.put("id", postList.get(k).get("pid"));
                    poList.add(poMap);
                }

                deMap.put("name", departmentList.get(j).get("department"));
                deMap.put("id", departmentList.get(j).get("did"));
                deMap.put("children", poList);
                deList.add(deMap);
            }
         //——————————

            Map<String,Object> st_deMap=new HashedMap();
            adMap.put("name", addressList.get(i).get("address"));
            adMap.put("id", addressList.get(i).get("aid"));
            st_deMap.put("company",deList);
            st_deMap.put("store",stList);
            adMap.put("children", st_deMap);
            adList.add(adMap);
        }
        map.put("addressList", adList);




//        List<Map<String, Object>> adList = new ArrayList<>();
//        for (int i = 0; i < addressList.size(); i++) {
//
//            List<Map<String, Object>> storeList = this.jdbcTemplate.queryForList(sql_store.toString(),addressList.get(i).get("aid"));
//            Map<String, Object> adMap = new HashedMap();
//            List<Map<String, Object>> stList = new ArrayList<>();
//            for (int j = 0; j < storeList.size(); j++) {
//                Map<String, Object> stMap = new HashedMap();
//                stMap.put("name", storeList.get(j).get("store"));
//                stMap.put("id", storeList.get(j).get("sid"));
//                stList.add(stMap);
//            }
//
//            adMap.put("name", addressList.get(i).get("address"));
//            adMap.put("id", addressList.get(i).get("aid"));
//            adMap.put("children", stList);
//            adList.add(adMap);
//        }
//        map.put("addressList", adList);

//
//




        return map;

    }

    @Override
    public int addOrUpdateUser(User user) {

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creat_time = time.format(new Date());


//
//        for (int i = 0; i < xxlist.size(); i++) {
//            if (xxlist.get(i).get(0).toString().equals("1")) {
//                storeList.clear();
//                List<Integer> breakList = new ArrayList<>();
//                breakList.add(1);
//                breakList.add(1);
//                storeList.add(breakList);
//                break;
//            } else {
//
//                if (xxlist.get(i).get(1).toString().equals("1")){
//                    for (int j = 0; j <storeList.size() ; j++) {
//                        if (storeList.get(j).get(0)==xxlist.get(i).get(0)){
//                            storeList.remove(j);
//                        }else {
//
//                        }
//                    }
//                    List<Integer> ssList=new ArrayList<>();
//                    ssList.add(Integer.valueOf(xxlist.get(i).get(0).toString()));
//                    ssList.add(1);
//                    storeList.add(ssList);
//
//                }else {
//
//                    List<Integer> sslist=new ArrayList<>();
//                    for (int j = 0; j <storeList.size() ; j++) {
//                        if (storeList.get(j).get(1).toString().equals("1")){
//                            sslist.add(Integer.valueOf(storeList.get(j).get(0).toString()));
//                        }else {
//
//                        }
//                    }
//
//                    if (sslist.contains(xxlist.get(i).get(0))){
//
//                    }else {
//                        List<Integer> aaList=new ArrayList<>();
//                        aaList.add(Integer.valueOf(xxlist.get(i).get(0).toString()));
//                        aaList.add(Integer.valueOf(xxlist.get(i).get(1).toString()));
//                        storeList.add(aaList);
//                    }
//                }
//            }
//
//        }

        List<Integer> xxList = JSONArray.fromObject(user.getStore());
        List<Integer> storeList =new ArrayList<>();
        for (int i = 0; i < xxList.size(); i++) {
            if (xxList.get(i)==1){
                storeList.clear();
                storeList.add(1);
                break;
            }else {
                if (storeList.contains(xxList.get(i))){

                }else {
                    storeList.add(xxList.get(i));
                }

            }
        }

        int states = 0;
        if (user.getId() == 0) {
            //添加
            StringBuffer sb = new StringBuffer();
            sb.append("insert into yx_user (phone,password,name,power,photo,creat_time,description,company,department,sex,store)");
            sb.append("value(?,?,?,?,?,?,?,?,?,?,?)");

            states = jdbcTemplate.update(sb.toString(), user.getPhone(), user.getPassword(), user.getName(),user.getPower(),
                    user.getPhoto(), creat_time, user.getDescription(), user.getCompany(), user.getDepartment(), user.getSex(),storeList.toString());

        } else {
            //修改
            String sql_updates = " update yx_user set phone=?,password=?,name=?,power=?,photo=?,description=?,company=?,department=?,sex=?,store=? where id=?";
            states = jdbcTemplate.update(sql_updates, user.getPhone(), user.getPassword(), user.getName(),user.getPower(),
                    user.getPhoto(), user.getDescription(), user.getCompany(),user.getDepartment() , user.getSex(),storeList.toString(),user.getId());

        }

        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    //     String sql_address="select aid from yx_store where aid<>1 group by aid order by aid";//   String sql_store="select sid from yx_store where sid<>1 and sid is not null and aid=? group by sid order by sid";
//        for (int i = 0; i < xxlist.size(); i++) {
//
//            if (xxlist.get(i).size()==1){
//                //里面有[1] 全部
//                storeList.clear();
//                List<Map<String, Object>> aList=jdbcTemplate.queryForList(sql_address);
//                for (int j = 0; j <aList.size() ; j++) {
//                    List<Map<String, Object>> stList=jdbcTemplate.queryForList(sql_store,aList.get(j).get("aid"));
//                    for (int k = 0; k <stList.size() ; k++) {
//
//                        List<Integer> sList=new ArrayList<>();
//                        sList.add(Integer.valueOf(aList.get(j).get("aid").toString()));
//                        sList.add(Integer.valueOf(stList.get(k).get("sid").toString()));
//                        storeList.add(sList);
//                    }
//
//                }
//                break;
//            }else {
//
//                    if (xxlist.get(i).get(1).toString().equals("1")){
//                        System.out.println("affasdfs = " );
//                        System.out.println("xxlist.get(i).get(0) = " + xxlist.get(i).get(0));
//                       List<Map<String, Object>> stList=jdbcTemplate.queryForList(sql_store,xxlist.get(i).get(0));
//                        for (int j = 0; j <stList.size() ; j++) {
//                            List<Integer> sList=new ArrayList<>();
//                            sList.add(Integer.valueOf(xxlist.get(i).get(0).toString()));
//                            sList.add(Integer.valueOf(stList.get(j).get("sid").toString()));
//                            if (storeList.contains(sList)){
//
//                            }else{
//                                storeList.add(sList);
//                            }
//
//                        }
//                        System.out.println("storeList = " + storeList);
//
//                    }else {
//
//                        if (storeList.contains(xxlist.get(i))){
//
//                        }else {
//                            storeList.add(xxlist.get(i));
//                        }
//                    }
//
//
//            }
//
//        }
//
//
//
//
//
//
//
//


    @Override
    public List<Map<String, Object>> findOneUser(String json) {

        JSONObject jsonObject = JSON.parseObject(json);
        Integer uid = jsonObject.getInteger("uid");
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "select id,phone,password,name,post,power,photo,creat_time,description,company,department,sex,store from yx_user where id=? ";
        list = this.jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashedMap();
                mp.put("name", rs.getString("name"));
                mp.put("id", rs.getInt("id"));
                mp.put("photo", rs.getString("photo"));
                mp.put("phone", rs.getString("phone"));
                mp.put("sex",rs.getString("sex"));

                mp.put("company", rs.getInt("company"));
                mp.put("department", rs.getInt("department"));
                mp.put("power", rs.getInt("power"));
                mp.put("password", rs.getString("password"));
                mp.put("description", rs.getString("description"));


                if (rs.getString("store") != null) {
                    List<Integer> storeList = JSONArray.fromObject(rs.getString("store"));
                    mp.put("store", storeList);
                } else {
                    mp.put("store", null);
                }
                return mp;
            }
        }, uid);
        return list;
    }
}
