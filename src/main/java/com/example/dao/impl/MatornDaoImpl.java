package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.MatornDao;
import com.example.dto.MatornDto;
import com.example.entiy.Matorn;
import com.example.entiy.Test;
import com.sun.scenario.effect.impl.sw.java.JSWBlend_SRC_OUTPeer;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import net.sf.json.util.JSONUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlInOutParameter;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.util.MonthUtil.*;
import static com.example.util.NumberUtil.*;
import static com.example.util.OssUtil.*;
import static com.example.util.PageUtil.getAllPage;
import static com.example.util.PeriodUtil.*;
import static com.example.util.Year.*;

@Repository
public class MatornDaoImpl implements MatornDao {


    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;
    @Autowired(required = false)
    private RedisTemplate redisTemplate;


    @Override
    public List<Matorn> matonsList() {
        List<Matorn> list = jdbcTemplate.query("select * from yx_matorn ", new Object[]{}, new BeanPropertyRowMapper(Matorn.class));
        return list;

    }

    @Override
    public Matorn findOne(String name) {
        List<Matorn> list = jdbcTemplate.query("select * from yx_matorn where user_name = ?", new Object[]{name}, new BeanPropertyRowMapper(Matorn.class));
        if (list != null && list.size() > 0) {
            Matorn matorn = list.get(0);
            return matorn;
        } else {
            return null;
        }
    }


    @Override
    public int save(Matorn matorn) {
        String date = matorn.getBorn();
        String str = date.substring(0, 4);
        String str1 = date.substring(5, 7);
        String str2 = date.substring(8, 10);
        Integer year = Integer.valueOf(str);
        Integer month = Integer.valueOf(str1);
        Integer day = Integer.valueOf(str2);


        String zodiac = getYear(year);
        String constellation = getConstellation(month, day);

        String sql = "insert into yx_matorn(uid,lid,name, sex, born,nation,idcard,idcard_type,household,marriage,educational,weight,height,address,zodiac,constellation,idtype) " +
                "values(?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
        int states = 0;


        String sql_idcard = "select idcard from yx_matorn where idtype=1 ";
        List<String> idcardlist = this.jdbcTemplate.queryForList(sql_idcard, String.class);

        int states_id = -1;
        for (int i = 0; i < idcardlist.size(); i++) {
            if (matorn.getIdcard().equals(idcardlist.get(i))) {
                states_id = 0;
                break;
            } else {
                states_id = 1;
            }

        }

        if (states_id == 1) {
            //uid=10 默认经理人
            if (matorn.getUid() != 0) {
                states = jdbcTemplate.update(sql, 10, matorn.getUid(), matorn.getName(), 0, matorn.getBorn(), matorn.getNation(), matorn.getIdcard(), matorn.getIdcard_type(), matorn.getHousehold(), matorn.getMarriage(), matorn.getEducational(), matorn.getWeight(), matorn.getHeight(), matorn.getAddress(), zodiac, constellation, 0);
            } else {
                states = jdbcTemplate.update(sql, 10, 16, matorn.getName(), 0, matorn.getBorn(), matorn.getNation(), matorn.getIdcard(), matorn.getIdcard_type(), matorn.getHousehold(), matorn.getMarriage(), matorn.getEducational(), matorn.getWeight(), matorn.getHeight(), matorn.getAddress(), zodiac, constellation, 0);
            }
            int mid = getForId();
            String sql1 = "insert into yx_userinfo(uid,mid) values (?,?)";
            int states1 = jdbcTemplate.update(sql1, matorn.getUid(), mid);
            if (states > 0 && states1 > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1;
        }


    }


    @Override
    public List<Map<String, Object>> getById(Integer id) {

        List list = null;
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT yx_matorn.id,yx_matorn.uid,yx_matorn.lid,yx_matorn.name,yx_matorn.idcard,yx_matorn.idcard_type,yx_matorn.born,yx_matorn.nation,yx_matorn.household,yx_matorn.educational,yx_matorn.marriage,yx_matorn.weight,yx_matorn.height,yx_matorn.address,yx_matorn.zodiac,yx_matorn.constellation,");
        sb.append("yx_contact.phone,yx_contact.bank_card,yx_contact.bank_name,yx_contact.wechat,yx_contact.emergency_person,yx_contact.emergency_phone,yx_origin.source,yx_origin.institution_name,yx_origin.witness,yx_origin.witness_phone,yx_origin.other,yx_origin.introducer,yx_origin.introducer_phone,yx_bussiness.photo,yx_bussiness.charact,yx_bussiness.strength,");
        sb.append("yx_bussiness.work_age,yx_bussiness.num,yx_bussiness.works,yx_bussiness.trains,yx_bussiness.qualification,yx_bussiness.grade,yx_bussiness.creat_time,yx_bussiness.day,yx_bussiness.identity,yx_bussiness.heathly,yx_bussiness.assess");
        sb.append(" from yx_matorn ");
        sb.append(" left JOIN yx_contact on (yx_matorn.id=yx_contact.mid) left JOIN yx_origin ON (yx_matorn.id=yx_origin.mid) left JOIN yx_bussiness on (yx_matorn.id=yx_bussiness.mid) where  yx_bussiness.isquit=0  and yx_matorn.id = ?");

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {

            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {

                Map<String, Object> mp = new HashMap<String, Object>();

                String birthTime = rs.getString("born");

                Long age = getAge(birthTime);//得到年龄

                String household = rs.getString("household");

                String city = "";
                if (household.length() > 3) {


                    String h1 = household.substring(0, 2);
                    String h2 = household.substring(3, 5);
                    city = h1 + h2;
                } else {

                    city = household.substring(0, 2);

                }
                Integer uid = rs.getInt("uid");


                if (uid == 0 || uid == 22) {
                    String old_agent=null;
                    if(uid==0){
                        old_agent = "待分配";
                    }
                    if (uid==22){
                        String sql_old_agent = "select name from yx_user where  and id=?";
                        old_agent = jdbcTemplate.queryForObject(sql_old_agent, String.class, 22);
                    }
                    mp.put("agent", old_agent);
                } else {
                    String sql_agent = "select name from yx_user where id=?";
                    String agent = jdbcTemplate.queryForObject(sql_agent, String.class, uid);
                    mp.put("agent", agent);
                }
                mp.put("id", rs.getInt("id"));
                mp.put("name", rs.getString("name").toString());
                mp.put("idcard", rs.getString("idcard"));
                mp.put("idcard_type", rs.getString("idcard_type"));
                mp.put("age", age);
                mp.put("city", city);

                mp.put("born", rs.getString("born"));
                mp.put("nation", rs.getString("nation"));
                mp.put("household", rs.getString("household"));
                mp.put("educational", rs.getString("educational"));
                mp.put("marriage", rs.getString("marriage"));
                mp.put("weight", rs.getString("weight"));
                mp.put("height", rs.getString("height"));
                mp.put("address", rs.getString("address"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("constellation", rs.getString("constellation"));

                mp.put("phone", rs.getString("phone"));
                mp.put("wechat", rs.getString("wechat"));
                mp.put("emergency_person", rs.getString("emergency_person"));
                mp.put("emergency_phone", rs.getString("emergency_phone"));
                mp.put("bank_card", rs.getString("bank_card"));
                mp.put("bank_name", rs.getString("bank_name"));

                mp.put("source", rs.getString("source"));
                mp.put("institution_name", rs.getString("institution_name"));
                mp.put("witness", rs.getString("witness"));
                mp.put("witness_phone", rs.getString("witness_phone"));
                mp.put("other", rs.getString("other"));
                mp.put("introducer", rs.getString("introducer"));
                mp.put("introducer_phone", rs.getString("introducer_phone"));


                mp.put("assess", rs.getString("assess"));
                mp.put("photo", rs.getString("photo"));
                mp.put("character", rs.getString("charact"));
                mp.put("strength", rs.getString("strength"));
                mp.put("work_age", rs.getString("work_age"));
                mp.put("grade", rs.getString("grade"));


                String creat_time = rs.getString("creat_time");
                int day = rs.getInt("day");
                String grade = rs.getString("grade");
                int a = 0;
                if (grade.equals("P0") && day == 0) {
                    try {
                        a = getNull(creat_time);
                        mp.put("rise", 0);//等待考核状态
                        mp.put("day", a);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    a = getDay(day, grade);
                    if (a != 0) {
                        mp.put("rise", 1);//等待晋级状态
                        mp.put("day", a);
                    } else {
                        mp.put("rise", 2);//不提醒
                    }
                }


                mp.put("num", rs.getString("num"));

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
        }, id);


        return list;

    }


    @Override
    public int updateMatorn(Matorn matorn) {
        String sql = "update yx_matorn set name=?,born=?,nation=?,idcard=?,idcard_type=?,household=?,marriage=?,educational=?,weight=?,height=?,address=?,zodiac=?,constellation=? where id=?";
        String idcard = matorn.getIdcard();
        String born = "";
        if (idcard.length() == 18) {
            born = idcard.substring(6, 10) + "-" + idcard.substring(10, 12) + "-" + idcard.substring(12, 14);

            String str = born.substring(0, 4);
            String str1 = born.substring(5, 7);
            String str2 = born.substring(8, 10);
            Integer year = Integer.valueOf(str);
            Integer month = Integer.valueOf(str1);
            Integer day = Integer.valueOf(str2);
            String zodiac = getYear(year);
            String constellation = getConstellation(month, day);
            return this.jdbcTemplate.update(sql, matorn.getName(), born, matorn.getNation(), matorn.getIdcard(), matorn.getIdcard_type(), matorn.getHousehold(), matorn.getMarriage(), matorn.getEducational(), matorn.getWeight(), matorn.getHeight(), matorn.getAddress(), zodiac, constellation, matorn.getId());
        } else {
            return -2;
        }


    }


    @Override
    public int update(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        String source = jsonObject.getString("source");
        String institution_name = jsonObject.getString("institution_name");
        String witness = jsonObject.getString("witness");
        String witness_phone = jsonObject.getString("witness_phone");


        Integer id = jsonObject.getInteger("id");
        String idcard = jsonObject.getString("idcard");
        String phone = jsonObject.getString("phone");
        String wechat = jsonObject.getString("wechat");
        String emergency_person = jsonObject.getString("emergency_person");
        String emergency_phone = jsonObject.getString("emergency_phone");

        String photo = jsonObject.getString("photo");
        String work_age = jsonObject.getString("work_age");
        String num = jsonObject.getString("num");
        String works = jsonObject.getString("works");
        String trains = jsonObject.getString("trains");
        String qualification = jsonObject.getString("qualification");
        String identity = jsonObject.getString("identity");
        String heathly = jsonObject.getString("heathly");

        String name = jsonObject.getString("name");
        String nation = jsonObject.getString("nation");
        String born = idcard.substring(6, 10) + "-" + idcard.substring(10, 12) + "-" + idcard.substring(12, 14);
        String household = jsonObject.getString("household");
        String str = born.substring(0, 4);
        String str1 = born.substring(5, 7);
        String str2 = born.substring(8, 10);
        Integer year = Integer.valueOf(str);
        Integer month = Integer.valueOf(str1);
        Integer day = Integer.valueOf(str2);

        String address = jsonObject.getString("address");
        String zodiac = getYear(year);
        String constellation = getConstellation(month, day);
        String marriage = jsonObject.getString("marriage");
        String educational = jsonObject.getString("educational");
        String weight = jsonObject.getString("weight");
        String height = jsonObject.getString("height");
        String sql_m = "update yx_matorn set name=?,nation=?,idcard=?,household=?,marriage=?,educational=?,weight=?,height=?,addres =?,zodiac=?,constellation=?  where id = ?";
        int states = this.jdbcTemplate.update(sql_m, name, nation, idcard, household, marriage, educational, weight, height, address, zodiac, constellation, id);

        String sql_o = "update yx_origin set source = ?,institution_name= ?,witness=?, witness_phone=?  where mid = ?";
        int states1 = this.jdbcTemplate.update(sql_o, source, institution_name, witness, witness_phone, id);

        String sql_c = "update yx_contact set  phone= ? ,wechat=?,emergency_person =? ,emergency_phone = ? where mid = ?";
        int states2 = this.jdbcTemplate.update(sql_c, phone, wechat, emergency_person, emergency_phone, id);

        String sql_b = "update yx_bussiness set  photo=?,work_age= ? ,num=?,works =? ,trains = ?,qualification = ?,identity=?,heathly=? where mid = ?";
        int states3 = this.jdbcTemplate.update(sql_b, photo, work_age, num, works, trains, qualification, identity, heathly, id);

        if (states > 0 && states2 > 0 && states3 > 0 && states1 > 0) {
            return 1;
        } else {
            return 0;

        }


    }

    @Override
    public void del(String name) {
//        jdbcTemplate.update("DELETE FROM yx_maton WHERE user_name = ?", name);

    }

    @Override
    public int getForId() {
        String sql = "select max(id ) from yx_matorn ";
        int id = this.jdbcTemplate.queryForObject(sql, Integer.class);
        return id;
    }


    @Override
    public int saveOrUpdate(Matorn matorn) {
        String sql = "select id from yx_matorn where id= ? ";
        Integer id = Integer.valueOf(matorn.getId());
        if (id == 0 || id == null) {
            String date = matorn.getBorn();
            String str = date.substring(0, 4);
            String str1 = date.substring(5, 7);
            String str2 = date.substring(9, 10);
            Integer year = Integer.valueOf(str);
            Integer month = Integer.valueOf(str1);
            Integer day = Integer.valueOf(str2);
            String zodiac = getYear(year);
            String constellation = getConstellation(month, day);
            String sql2 = "insert into yx_matorn(name, sex, born,nation,idcard,idcard_type,household,marriage,educational,weight,height,address,manager,zodiac,constellation) " +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";
            return jdbcTemplate.update(sql2, matorn.getName(), 0, matorn.getBorn(), matorn.getNation(), matorn.getIdcard(), matorn.getIdcard_type(), matorn.getHousehold(), matorn.getMarriage(), matorn.getEducational(), matorn.getWeight(), matorn.getHeight(), matorn.getAddress(), null, zodiac, constellation);

        } else {
            String sql1 = "update yx_matorn set name=?,born=?,nation=?,idcard=?,idcard_type=?,household=?,marriage=?,educational=?,weight=?,height=?,address=? where id=?";
            return this.jdbcTemplate.update(sql1, matorn.getName(), matorn.getBorn(), matorn.getNation(), matorn.getIdcard(), matorn.getIdcard_type(), matorn.getHousehold(), matorn.getMarriage(), matorn.getEducational(), matorn.getWeight(), matorn.getHeight(), matorn.getAddress(), matorn.getId());
        }

    }

    @SneakyThrows
    @Override
    public String getIdentity(String identity) {

        String sql = "select count(*) from yx_matorn where idcard=? and idtype=1  ";
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(identity);
        net.sf.json.JSONObject jsonObject1 = net.sf.json.JSONObject.fromObject(jsonObject.getString("result"));
        String idcard = jsonObject1.getString("code");

        System.out.println("idcard = " + idcard);
        String new_identity = "";

        Integer count = this.jdbcTemplate.queryForObject(sql, Integer.class, idcard);

        if (count > 0) {
            new_identity = "{\"code\":\"100\",\"msg\":\"身份证已添加\"}";
        } else {
            new_identity = identity;
        }
        return new_identity;


    }

    @Override
    public Map<String, Object> serviceRecord(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer mid = jsonObject.getInteger("mid");
        String month = jsonObject.getString("month");

        Map<String, Object> allmap = new HashMap<String, Object>();
        List<Map<String, Object>> list1 = null;
        String sql_mid = "select mid from yx_order where mid =? and order_states=3";
        StringBuffer sb = new StringBuffer();
        List<Object> queryList = new ArrayList<Object>();
        sb.append("select * from yx_order where order_states=3 and id=? ");
        sb.append(" order by id desc");

        try {
            List<Integer> listmid = this.jdbcTemplate.queryForList(sql_mid.toString(), Integer.class, mid);
            if (mid.equals(listmid.get(0))) {
                StringBuffer sb1 = new StringBuffer();
                sb1.append("select id from yx_order where mid =? and order_states=3  ");
                queryList.add(mid);
                if (month == "" || month == null || month.isEmpty()) {

                } else {
                    List<String> monthlist = new ArrayList<String>();
                    monthlist = getFistMonth(month);
                    sb1.append("and creattime>?  ");
                    queryList.add(monthlist.get(0).toString());
                    sb1.append("and creattime<?  ");
                    queryList.add(monthlist.get(1).toString());

                }
                List<Integer> listoid = this.jdbcTemplate.queryForList(sb1.toString(), Integer.class, queryList.toArray());


                List<Map<String, Object>> timelist = new ArrayList<Map<String, Object>>();


                List<Map<String, Object>> orderList = new ArrayList<Map<String, Object>>();

                for (int i = 0; i < listoid.size(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    list1 = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                            Map<String, Object> mp = new HashMap<String, Object>();
                            String sql_bid = "select bid from yx_custom where id=?";
                            Integer bid = jdbcTemplate.queryForObject(sql_bid, Integer.class, rs.getInt("cid"));
                            String sql_agent = "select name from yx_user where id=?";
                            String agent = jdbcTemplate.queryForObject(sql_agent, String.class, bid);

                            mp.put("banme", agent);
                            mp.put("oid", rs.getString("id"));

                            List<Map<String, Object>> three = new ArrayList<Map<String, Object>>();
                            // String sql_q = "select service_day,threestates,post_states from yx_order_demand where id=?";
                            String sql_q = "select yx_custom.c_name,yx_order_demand.service_day,yx_order_demand.threestates,yx_order_demand.post_states from yx_custom left join yx_order_demand on (yx_custom.id=yx_order_demand.cid) where yx_order_demand.id=?";

                            three = jdbcTemplate.queryForList(sql_q, rs.getInt("did"));
                            mp.put("service_day", Integer.valueOf(three.get(0).get("service_day").toString()));
                            mp.put("threestates", Integer.valueOf(three.get(0).get("threestates").toString()));
                            three.get(0).get("post_states");

                            if (three.get(0).get("post_states") != null) {
                                mp.put("post_states", Integer.valueOf(three.get(0).get("threestates").toString()));
                            } else {
                                mp.put("post_states", 0);

                            }
                            mp.put("o_name", three.get(0).get("c_name"));

                            mp.put("o_number", rs.getString("o_number"));
                            mp.put("region", rs.getString("region"));
                            mp.put("o_address", rs.getString("o_address"));
                            mp.put("order_states", rs.getString("order_states"));
                            mp.put("arrival_time", rs.getString("arrival_time"));
                            mp.put("confirm_time", rs.getString("confirm_time"));
                            mp.put("order_day", rs.getInt("order_day"));
                            mp.put("feedback", rs.getString("feedback"));

                            return mp;
                        }

                    }, listoid.get(i));
                    map.put("arrival_time", list1.get(0).get("arrival_time").toString());
                    map.put("confirm_time", list1.get(0).get("confirm_time").toString());
                    timelist.add(map);
                    orderList.add(list1.get(0));
                }

                List<Map<Integer, Object>> alltime = new ArrayList<>();

                for (int i = 0; i < timelist.size(); i++) {
                    Map<Integer, Object> time = new HashMap<Integer, Object>();
                    time = getMonthDay(timelist.get(i).get("arrival_time").toString(), timelist.get(i).get("confirm_time").toString());
                    alltime.add(time);
                }
                Map<Integer, Object> all = new HashMap<Integer, Object>();
                all = getAllMonthDay(alltime);
                allmap.put("all", all);
                allmap.put("orderlist", orderList);


            } else {
                return allmap;
            }

        } catch (Exception e) {
            return allmap;
        }

        return allmap;
    }

    @SneakyThrows
    @Override
    public int addHeathly(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer mid = jsonObject.getInteger("mid");
        String base64sString = jsonObject.getString("base64sString");
        String fileName = jsonObject.getString("fileName");
        String filePath = base64StringToPDF(base64sString, fileName);


        String reportPdf = getOssUrl(filePath + "/" + fileName, fileName);

        System.out.println("filePath________" + filePath + "/" + fileName);

        String sql = "update yx_bussiness set reportPdf=? where mid=?";

        int states = this.jdbcTemplate.update(sql, reportPdf, mid);
        if (states > 0) {
            //删除项目中bsae64转成的pdf
            File dir = new File(filePath);
            boolean a = deleteDir(dir);
            if (a == true) {
                return 1;
            } else {
                return -1;
            }


        } else {
            return 0;
        }

    }

    @SneakyThrows
    @Override
    public Map<String, Object> getAllmatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Map<String, Object> map = new HashedMap();
        Integer uid = jsonObject.getInteger("uid");//用户id
        String zodiac = jsonObject.getString("zodiac");//月嫂属相
        String grade = jsonObject.getString("grade");//月嫂来源
        Integer shelf = jsonObject.getInteger("shelf");//月嫂状态
        Integer bid = jsonObject.getInteger("bid");//月嫂经理人
        String source = jsonObject.getString("source");//月嫂来源1
        String origin = jsonObject.getString("origin");//月嫂来源2
        Integer isPrice = jsonObject.getInteger("isPrice");//押金质保金状态
        Integer start = jsonObject.getInteger("start");//当前页数
        String powerFiled = jsonObject.getString("powerFiled");//权限字段
        String number = jsonObject.getString("number");//月嫂姓名工号id

        List<Map<String, Object>> powerFiledList = new ArrayList<>();
        Map<String, Object> powerFiledMap = new LinkedHashMap();//保证put值有序
        if (powerFiled.isEmpty()) {
            //第一次请求powerFiled为空，查询用户的权限字段
            String sql_power = "select photo,number,name,idcard,age,zodiac,household,phone,bank_card,bank_name,grade,trueday,service_count,shelf,period,agent,origin,isPrice,l_name,creat_time from yx_matornPowerField where lid=? ";
            List<Map<String, Object>> powerFiledList1 = new ArrayList<>();
            powerFiledList1 = this.jdbcTemplate.queryForList(sql_power, uid);
            //通过key    Set<T> key = map.keySet()
            for (String key : powerFiledList1.get(0).keySet()) {
                Integer value = Integer.valueOf(powerFiledList1.get(0).get(key).toString());
                if (value != 0) {
                    powerFiledMap.put(key, value);
                } else {

                }
            }
            powerFiledList.add(powerFiledMap);
        } else {
            //第二次powerFiled
            JSONArray jsonArray = JSONArray.fromObject(powerFiled);
            List<Map<String, Object>> powerFiledList1 = new ArrayList<>();
            powerFiledList1 = (List) jsonArray;
            for (int i = 0; i < powerFiledList1.size(); i++) {
                for (String key : powerFiledList1.get(i).keySet()) {
                    Integer value = Integer.valueOf(powerFiledList1.get(i).get(key).toString());
                    powerFiledMap.put(key, value);
                }
            }
            powerFiledList.add(powerFiledMap);

        }
        System.out.println("powerFiledMap = " + powerFiledMap);
        map.put("powerFiledList", powerFiledList);
        //条件显示的集合
        Map<String, Object> matornCondition = matornCondition();

        //属相条件集合判断是否显示
        if (powerFiledMap.get("zodiac") != null) {
            if (Integer.valueOf(powerFiledMap.get("zodiac").toString()) != 0) {
                List<String> zodiacList = (List<String>) matornCondition.get("zodiacList");
                map.put("zodiacList", zodiacList);
            } else {

            }
        } else {

        }

        //属相条件集合
        if (powerFiledMap.get("grade") != null) {
            if (Integer.valueOf(powerFiledMap.get("grade").toString()) != 0) {
                List<String> gradeList = (List<String>) matornCondition.get("gradeList");
                map.put("gradeList", gradeList);
            } else {

            }
        } else {

        }

        //经理人条件集合
        if (powerFiledMap.get("agent") != null) {
            if (Integer.valueOf(powerFiledMap.get("agent").toString()) != 0) {
                List<Map<String, Object>> bnameList = (List<Map<String, Object>>) matornCondition.get("bnameList");
                map.put("bnameList", bnameList);

            } else {

            }
        } else {

        }

        //月嫂状态条件集合
        if (powerFiledMap.get("shelf") != null) {
            if (Integer.valueOf(powerFiledMap.get("shelf").toString()) != 0) {
                List<Map<String, Object>> shelfList = (List<Map<String, Object>>) matornCondition.get("shelfList");
                map.put("shelfList", shelfList);
            } else {

            }
        } else {

        }

        //月嫂押金质保金状态条件集合
        if (powerFiledMap.get("isPrice") != null) {
            if (Integer.valueOf(powerFiledMap.get("isPrice").toString()) != 0) {
                List<Map<String, Object>> priceList = (List<Map<String, Object>>) matornCondition.get("priceList");
                map.put("priceList", priceList);
            } else {

            }
        } else {

        }

        //月嫂来源状态条件集合
        if (powerFiledMap.get("origin") != null) {
            if (Integer.valueOf(powerFiledMap.get("origin").toString()) != 0) {
                List<Map<String, Object>> originList = (List<Map<String, Object>>) matornCondition.get("originList");
                map.put("originList", originList);
            } else {

            }
        } else {

        }

        StringBuffer sb_matorn = new StringBuffer();
        //月嫂数量查询公共语句
        sb_matorn.append("select count(*) from yx_matorn m left join yx_bussiness b on (m.id=b.mid) where m.idtype=1 and  ");
        List<String> sqlMatornList = new ArrayList<>();
        //本年
        String sql_nowYear = " YEAR(b.creat_time)=YEAR(NOW()) ";
        sqlMatornList.add(sql_nowYear);
        //当天
        String sql_nowDay = " to_days(b.creat_time) = to_days(now()) ";
        sqlMatornList.add(sql_nowDay);
        //本周
        String sql_nowWeek = " YEARWEEK(date_format(b.creat_time,'%Y-%m-%d'),1) = YEARWEEK(now(),1) ";
        sqlMatornList.add(sql_nowWeek);
        //本月
        String sql_nowMonth = " DATE_FORMAT( b.creat_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )";
        sqlMatornList.add(sql_nowMonth);

        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        String sql_custom_count = "select count from yx_matornSet where type=?";

        List<Map<String, Object>> dayList = new ArrayList<>();
        for (int i = 0; i < sqlMatornList.size(); i++) {
            Map<String, Object> dayMap = new HashMap<>();
            StringBuffer sb_init = new StringBuffer();
            sb_init.reverse();//初始化
            sb_init.append(sb_matorn);//添加搜索语句
            sb_init.append(sqlMatornList.get(i));//添加条件语句
            //查询客户条数
            Integer sql_count = jdbcTemplate.queryForObject(sb_init.toString(), Integer.class);
            Integer type = i + 1;
            //设置客户人数
            Integer count = jdbcTemplate.queryForObject(sql_custom_count, Integer.class, type);
            String result = numberFormat.format((float) sql_count / (float) count * 100);
            dayMap.put("count", sql_count);
            dayMap.put("type", type);
            dayMap.put("result", result);
            dayList.add(dayMap);
        }
        map.put("dayList", dayList);

        //显示级别数量

        List<String> gList = (List<String>) matornCondition.get("gradeList");
        String sql_grade = "  b.grade =?";
        sb_matorn.append(sql_grade);
        List<Map<String, Object>> sumGradeList = new ArrayList<>();
        for (int i = 0; i < gList.size(); i++) {
            Map<String, Object> sumGradeMap = new HashMap<>();
            Integer count = jdbcTemplate.queryForObject(sb_matorn.toString(), Integer.class, gList.get(i));
            sumGradeMap.put("grade", gList.get(i));
            sumGradeMap.put("count", count);
            sumGradeList.add(sumGradeMap);
        }
        map.put("sumGradeList", sumGradeList);


        //条件集合
        List<String> queryList = new ArrayList<>();
        //页面显示语句
        StringBuffer sb = new StringBuffer();
        sb.append(" select m.id,m.uid,m.lid,m.name,YEAR(from_days(datediff(now(), m.born))) as  age ,m.zodiac,m.household,b.grade,b.trueday,b.shelf,");
        sb.append(" ori.source,ori.institution_name,ori.introducer,ori.other,b.creat_time,b.isDeposit,b.isWarranty,b.number,b.photo,b.isblack,");
        sb.append(" p.month,c.phone,c.bank_card,c.bank_name,m.idcard,b.isorder,u.name as l_name,b.isevaluate,b.reportPdf");
        sb.append(" from yx_matorn m left join yx_origin ori on (m.id=ori.mid) left join yx_bussiness b on (m.id=b.mid) ");
        sb.append(" left join yx_period p on (m.id=p.mid) left join yx_contact c on (m.id=c.mid) left join yx_user u on (m.lid=u.id) ");
        sb.append(" where  m.idtype=1 ");

        //条数语句
        StringBuffer sb_count = new StringBuffer();
        sb_count.append("select count(*) from  yx_matorn m left join yx_origin ori on (m.id=ori.mid) left join yx_bussiness b on (m.id=b.mid) where m.idtype=1 ");

        //公共条件语句
        StringBuffer sb_query = new StringBuffer();


        if (shelf == 0) {

        } else {
            if (shelf == 1) {
                //黑名单
                sb_query.append(" and b.isblack=1 ");
            }
            if (shelf == 2) {
                //未上架
                sb_query.append(" and b.shelf=0 ");
            }
            if (shelf == 3) {
                //已上架
                sb_query.append(" and b.shelf=1 ");
            }
            if (shelf == 4) {
                //服务中
                sb_query.append(" and b.isorder=1 ");
            }
        }


        //经理人判断
        if (bid == 0) {
            //为空
        } else {
            if (bid != 22) {
                sb_query.append(" and m.uid =? ");
                queryList.add(bid.toString());
            } else {
                sb_query.append(" and (m.uid=0 or m.uid=22 ) ");
            }

        }


        //等级判断
        if (grade.equals("null") || grade.isEmpty() || grade == "" || grade == null) {

        } else {
            sb_query.append(" and b.grade=? ");
            queryList.add(grade);
        }

        //属相判断
        if (zodiac.equals("null") || zodiac.isEmpty() || zodiac == "" || zodiac == null) {

        } else {
            sb_query.append(" and m.zodiac=? ");
            queryList.add(zodiac);
        }

        //月嫂来源判断
        if (source.equals("null") || source.isEmpty() || source == "" || source == null) {
            //为空
        } else {
            if (source.equals("培训机构")) {
                sb_query.append(" and ori.source='培训机构' ");
                if (origin.equals("null") || origin.isEmpty() || origin == "" || origin == null) {
                    //为空
                } else {
                    sb_query.append(" and ori.mechanism_name=? ");
                    queryList.add(origin);
                }

            }
            if (source.equals("个人介绍")) {
                sb_query.append(" and ori.source='个人介绍' ");
                if (origin.equals("null") || origin.isEmpty() || origin == "" || origin == null) {
                    //为空
                } else {
                    sb_query.append(" and ori.introducer=? ");
                    queryList.add(origin);
                }
            }
            if (source.equals("其他")) {
                sb_query.append(" and ori.source='其他' ");
                if (origin.equals("null") || origin.isEmpty() || origin == "" || origin == null) {

                } else {
                    if (origin.equals("未知")) {

                    } else {
                        sb.append(" and ori.other='' ");
                    }

                }
            }
            if (source.equals("未知")) {
                sb.append(" and ori.source is null ");
            }

        }

        //押金质保金判断
        if (isPrice == 0) {

        } else {
            if (isPrice == 1) {
                sb_query.append(" and ( b.isDeposit is null or b.isDeposit=0  )   and  ( b.isWarranty is null or b.isWarranty=0  ) ");
            }
            if (isPrice == 2) {
                sb_query.append(" and b.isDeposit=1 and  ( b.isWarranty is null or b.isWarranty=0  ) ");
            }
            if (isPrice == 3) {
                sb_query.append(" and b.isDeposit=2 and  ( b.isWarranty is null or b.isWarranty=0  )");
            }
            if (isPrice == 4) {
                sb_query.append(" and ( b.isDeposit is null or b.isDeposit=0  ) and   b.isWarranty=1 ");
            }
            if (isPrice == 5) {
                sb_query.append(" and ( b.isDeposit is null or b.isDeposit=0  ) and   b.isWarranty=2 ");
            }

        }

        if (number.equals("null") || number.isEmpty() || number == "" || number == null) {

        } else {
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
                String sql2 = "select name from yx_matorn where name =? ";
                try {
                    List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{number}, String.class);
                    if (number.equals(list1.get(0))) {
                        sb_query.append("  and m.name=?  ");
                        queryList.add(list1.get(0));
                    } else {

                    }
                } catch (Exception e) {
                    if (number.length() == 1) {
                        String name = number.substring(0, 1);
                        sb_query.append(" and m.name like ?  ");
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
        }
        System.out.println("sb_query = " + sb_query);


        sb_query.append(" order by b.creat_time desc ");
        System.out.println("queryList = " + queryList);

        sb_count.append(sb_query);
        System.out.println("sb_count = " + sb_count);
        sb.append(sb_query);

        Integer count = this.jdbcTemplate.queryForObject(sb_count.toString(), Integer.class, queryList.toArray());
        List<Map<String, Object>> pageList = new ArrayList<>();
        Map<String, Object> pageMap = new HashMap<>();
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        pageMap.put("count", count);//搜索出来的客户总人数
        String sql_page = page.get("sql_page").toString();
        sb.append(sql_page);
        pageMap.put("page", page.get("page"));
        pageMap.put("num", page.get("num"));
        pageList.add(pageMap);

        map.put("pageList", pageList);

        System.out.println("sb = " + sb);
        List<Map<String, Object>> list = new ArrayList<>();
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashedMap();

                mp.put("mid", rs.getInt("id"));
                mp.put("number", rs.getString("number"));
                mp.put("photo", rs.getString("photo"));
                mp.put("isevaluate", rs.getInt("isevaluate"));
                mp.put("reportPdf", rs.getString("reportPdf"));
                //姓名
                if (powerFiledMap.get("name") != null) {
                    if (powerFiledMap.get("name").toString().equals("1")) {
                        mp.put("name", rs.getString("name"));
                    } else {

                    }
                } else {

                }

                //身份证号
                if (powerFiledMap.get("idcard") != null) {
                    if (powerFiledMap.get("idcard").toString().equals("1")) {
                        mp.put("idcard", rs.getString("idcard"));
                    } else {

                    }
                } else {

                }

                //年龄
                if (powerFiledMap.get("age") != null) {
                    if (powerFiledMap.get("age").toString().equals("1")) {
                        mp.put("age", rs.getString("age"));
                    } else {

                    }
                } else {

                }

                //属相
                if (powerFiledMap.get("zodiac") != null) {
                    if (powerFiledMap.get("zodiac").toString().equals("1")) {
                        mp.put("zodiac", rs.getString("zodiac"));
                    } else {

                    }
                } else {

                }

                //月嫂籍贯
                if (powerFiledMap.get("household") != null) {
                    if (powerFiledMap.get("household").toString().equals("1")) {
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
                    } else {

                    }
                } else {

                }

                //手机
                if (powerFiledMap.get("phone") != null) {
                    if (powerFiledMap.get("phone").toString().equals("1")) {
                        mp.put("phone", rs.getString("phone"));
                    } else {

                    }
                } else {

                }

                //银行卡号
                if (powerFiledMap.get("bank_card") != null) {
                    if (powerFiledMap.get("bank_card").toString().equals("1")) {
                        mp.put("bank_card", rs.getString("bank_card"));
                    } else {

                    }
                } else {

                }

                //开户行
                if (powerFiledMap.get("bank_name") != null) {
                    if (powerFiledMap.get("bank_name").toString().equals("1")) {
                        mp.put("bank_name", rs.getString("bank_name"));
                    } else {

                    }
                } else {

                }

                //月嫂等级
                if (powerFiledMap.get("grade") != null) {
                    if (powerFiledMap.get("grade").toString().equals("1")) {
                        mp.put("grade", rs.getString("grade"));
                    } else {

                    }
                } else {

                }

                //服务天数
                if (powerFiledMap.get("trueday") != null) {
                    if (powerFiledMap.get("trueday").toString().equals("1")) {
                        mp.put("trueday", rs.getInt("trueday"));
                    } else {

                    }
                } else {

                }

                //服务单数
                if (powerFiledMap.get("service_count") != null) {
                    if (powerFiledMap.get("service_count").toString().equals("1")) {
                        String sql_service_count = "select  count(DISTINCT(cid)) from yx_order  where mid=? and order_states=3 ";
                        Integer service_count = jdbcTemplate.queryForObject(sql_service_count, Integer.class, rs.getInt("id"));
                        mp.put("service_count", service_count);

                    } else {

                    }
                } else {

                }

                //服务状态
                if (powerFiledMap.get("shelf") != null) {
                    if (powerFiledMap.get("shelf").toString().equals("1")) {

                        if (rs.getInt("isblack") == 0) {

                            if (rs.getInt("shelf") != 0) {
                                //上架
                                String sql_isorder = "select isorder from yx_bussiness where mid=?";
                                Integer isorder = jdbcTemplate.queryForObject(sql_isorder, Integer.class, rs.getInt("id"));
                                if (isorder == 1) {
                                    mp.put("shelf", 4);
                                } else {
                                    mp.put("shelf", 3);
                                }

                            } else {
                                mp.put("shelf", 2);
                            }

                        } else {
                            mp.put("shelf", 1);
                        }

                    } else {

                    }
                } else {

                }


                //服务档期
                if (powerFiledMap.get("period") != null) {
                    if (powerFiledMap.get("period").toString().equals("1")) {
                        String month = rs.getString("month");
                        Map<String, String> periodMap = new HashMap<String, String>();
                        String newmap = "";
                        if (month != null && month != "") {
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
                        }
                        mp.put("period", periodMap);
                    } else {

                    }
                } else {

                }

                if (powerFiledMap.get("creat_time") != null) {
                    if (powerFiledMap.get("creat_time").toString().equals("1")) {
                        mp.put("creat_time", rs.getString("creat_time"));
                    } else {

                    }

                } else {

                }


                //登记人
                if (powerFiledMap.get("l_name") != null) {
                    if (powerFiledMap.get("l_name").toString().equals("1")) {
                        mp.put("l_name", rs.getString("l_name"));
                    } else {

                    }

                } else {

                }
                if (rs.getInt("uid") == 0 || rs.getInt("uid") == 22) {
                    if (powerFiledMap.get("agent") != null) {
                        if (powerFiledMap.get("agent").toString().equals("1")) {
                            String sql_old_agent = "select name from yx_user where   id=?";
                            String old_agent = jdbcTemplate.queryForObject(sql_old_agent, String.class, 22);
                            mp.put("agent", old_agent);
                        } else {

                        }

                    } else {

                    }

                } else {
                    if (powerFiledMap.get("agent") != null) {
                        String sql_agent = "select name from yx_user where id = ? ";
                        String agent = jdbcTemplate.queryForObject(sql_agent, String.class, rs.getInt("uid"));
                        if (powerFiledMap.get("agent").toString().equals("1")) {
                            mp.put("agent", agent);
                        } else {

                        }

                    } else {

                    }
                }

                if (powerFiledMap.get("origin") != null) {
                    if (powerFiledMap.get("origin").toString().equals("1")) {
                        if (rs.getString("source") == null) {
                            mp.put("origin", "其他");
                        } else {
                            if (rs.getString("source").equals("培训机构")) {
                                mp.put("origin", rs.getString("institution_name"));
                            }
                            if (rs.getString("source").equals("个人介绍")) {
                                mp.put("origin", rs.getString("introducer"));
                            }
                            if (rs.getString("source").equals("其他")) {
                                mp.put("origin", rs.getString("other"));
                            }
                        }


                    } else {

                    }

                } else {

                }

                if (powerFiledMap.get("isPrice") != null) {
                    if (powerFiledMap.get("isPrice").toString().equals("1")) {

                        Integer isDeposit = rs.getInt("isDeposit");
                        Integer isWarranty = rs.getInt("isWarranty");
                        if (isDeposit == 0 && isWarranty == 0) {
                            //待激活
                            mp.put("isPrice", 1);
                        }
                        if (isDeposit == 1 && isWarranty == 0) {
                            //已交押金
                            mp.put("isPrice", 2);
                        }
                        if (isDeposit == 2 && isWarranty == 0) {
                            //已退押金
                            mp.put("isPrice", 3);
                        }
                        if (isDeposit == 0 && isWarranty == 1) {
                            //已交质保金
                            mp.put("isPrice", 4);
                        }
                        if (isDeposit == 0 && isWarranty == 2) {
                            //已退质保金
                            mp.put("isPrice", 5);
                        }

                    } else {

                    }

                } else {

                }


                return mp;
            }
        }, queryList.toArray());
        map.put("list", list);
        return map;
    }

    @Override
    public Map<String, Object> matornCondition() {
        Map<String, Object> map = new HashMap<>();

        //属相条件
        String[] zodiacString = {"子鼠", "丑牛", "寅虎", "卯兔", "辰龙", "巳蛇", "午马", "未羊",
                "申猴", "酉鸡", "戌狗", "亥猪"};
        List<String> zodiacList = Arrays.asList(zodiacString);
        map.put("zodiacList", zodiacList);

        //等级条件
        String[] gradeString = {"N", "H", "Y1", "Y2", "Y3", "X", "P"};
        List<String> gradeList = Arrays.asList(gradeString);
        map.put("gradeList", gradeList);

        //经理人条件
        String sql_bname = "select id,name from yx_user where power=1";
        List<Map<String, Object>> bnameList = this.jdbcTemplate.queryForList(sql_bname);
        map.put("bnameList", bnameList);

        //服务状态条件
        List<Map<String, Object>> shelfList = new ArrayList<>();
        Map<String, Object> shelfMap = new HashMap<>();
        Map<String, Object> shelfMap1 = new HashMap<>();
        Map<String, Object> shelfMap2 = new HashMap<>();
        Map<String, Object> shelfMap3 = new HashMap<>();

        shelfMap.put("1", "黑名单");
        shelfMap1.put("2", "未上架");
        shelfMap2.put("3", "已上架");
        shelfMap3.put("4", "服务中");
        shelfList.add(shelfMap);
        shelfList.add(shelfMap1);
        shelfList.add(shelfMap2);
        shelfList.add(shelfMap3);
        map.put("shelfList", shelfList);

        //押金质保金
        List<Map<String, Object>> priceList = new ArrayList<>();
        Map<String, Object> priceMap = new HashMap<>();
        Map<String, Object> priceMap1 = new HashMap<>();
        Map<String, Object> priceMap2 = new HashMap<>();
        Map<String, Object> priceMap3 = new HashMap<>();
        Map<String, Object> priceMap4 = new HashMap<>();
        priceMap.put("1", "待激活");
        priceMap1.put("2", "已交押金");
        priceMap2.put("3", "已退押金");
        priceMap3.put("4", "已交质保金");
        priceMap4.put("5", "已退质保金");
        priceList.add(priceMap);
        priceList.add(priceMap1);
        priceList.add(priceMap2);
        priceList.add(priceMap3);
        priceList.add(priceMap4);
        map.put("priceList", priceList);

        List<Map<String, Object>> originList = new ArrayList<>();
        String sql_institution = "select institution_name  from yx_matorn m left join yx_origin ori on (m.id=ori.mid) where m.idtype=1 and ori.source='培训机构'  group by  ori.institution_name order by m.id desc";
        List<Map<String, Object>> institution = jdbcTemplate.queryForList(sql_institution);

        String sql_oneSelf = "select  ori.introducer from yx_matorn m left join yx_origin ori on (m.id=ori.mid) where m.idtype=1 and ori.source='个人介绍'  group by  ori.introducer order by m.id desc ";
        List<Map<String, Object>> oneSelfList = jdbcTemplate.queryForList(sql_oneSelf);

        String sql_other = "select  ori.other from yx_matorn m left join yx_origin ori on (m.id=ori.mid) where m.idtype=1 and ori.source='其他'  and ori.other <>'' group by  ori.other order by m.id desc ";
        List<Map<String, Object>> otherList = jdbcTemplate.queryForList(sql_other);


        List<Map<String, Object>> institutionlist = new ArrayList<>();
        for (int i = 0; i < institution.size(); i++) {
            Map<String, Object> meMap = new HashedMap();
            meMap.put("name", institution.get(i).get("institution_name"));
            institutionlist.add(meMap);
        }
        Map<String, Object> institutionMap = new HashedMap();
        institutionMap.put("name", "培训机构");
        institutionMap.put("children", institutionlist);
        originList.add(institutionMap);

        List<Map<String, Object>> onelist = new ArrayList<>();
        for (int i = 0; i < oneSelfList.size(); i++) {
            Map<String, Object> oneMap = new HashedMap();
            oneMap.put("name", oneSelfList.get(i).get("introducer"));
            onelist.add(oneMap);
        }
        Map<String, Object> oneSelfMap = new HashedMap();
        oneSelfMap.put("name", "个人介绍");
        oneSelfMap.put("children", onelist);
        originList.add(oneSelfMap);

        List<Map<String, Object>> otList = new ArrayList<>();
        for (int i = 0; i < otherList.size(); i++) {
            Map<String, Object> otMap = new HashedMap();
            otMap.put("name", otherList.get(i).get("other"));
            otList.add(otMap);
        }

        Map<String, Object> xMap = new HashedMap();
        xMap.put("name", "未知");
        otList.add(xMap);
        Map<String, Object> otMap = new HashedMap();
        otMap.put("name", "其他");
        otMap.put("children", otList);
        originList.add(otMap);

        List<Map<String, Object>> nullList = new ArrayList<>();
        Map<String, Object> nullMap = new HashedMap();
        nullMap.put("name", "未知");
        List<Map<String, Object>> sList = new ArrayList<>();
        Map<String, Object> sMap = new HashedMap();
        sMap.put("name", "其他");
        sList.add(sMap);
        nullMap.put("children", sList);
        originList.add(nullMap);

        map.put("originList", originList);

        return map;
    }

    @Override
    public int editMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer shelf = jsonObject.getInteger("shelf");//服务状态
        String grade = jsonObject.getString("grade");//等级
        Integer isPrice = jsonObject.getInteger("isPrice");//押金质保金状态
        Integer mid = jsonObject.getInteger("mid");
        Integer bid = jsonObject.getInteger("bid");

        //查询月嫂之前的信息
        StringBuffer sb = new StringBuffer();
        sb.append(" select m.uid,m.lid,b.grade,b.isblack,b.shelf,b.isDeposit,b.isWarranty ");
        sb.append(" from yx_matorn m left join  yx_bussiness b on (m.id=b.mid) ");
        sb.append(" where m.idtype=1 and m.id=?");

        Map<String, Object> bList = jdbcTemplate.queryForMap(sb.toString(), mid);


        //服务这状态修改


        Integer   isBlack = Integer.valueOf(bList.get("isblack").toString());
        Integer isShelf = Integer.valueOf(bList.get("shelf").toString());
        if (shelf != 0) {
            if (shelf == 1) {
                //拉黑
                isBlack = 1;
            }
            if (shelf == 2) {
                //未上架
                if (bList.get("isblack").toString().equals("1")) {
                    // 取消拉黑
                    isBlack = 0;
                    isShelf = 0;
                }
                if (bList.get("shelf").toString().equals("1")) {
                    //未上架
                    isShelf = 0;
                }

            }
            if (shelf == 3) {
                //已上架
                isShelf = 1;
            }

        } else {
        }

        //等级修改
        String old_grade = bList.get("grade").toString();//原来等级
        String new_grade = null;//新等级
        if (grade.isEmpty() || grade == null || grade == "") {
            //不修改
            new_grade = old_grade;
        } else {
            if (old_grade.equals("N") && grade.equals("P")) {
                new_grade = grade;
            } else {
                new_grade = old_grade;
            }

        }
        //押金状态
        Integer isDeposit = 0;
        if (bList.get("isDeposit") != null) {
            isDeposit = Integer.valueOf(bList.get("isDeposit").toString());
        } else {
            isDeposit = 0;
        }
        //质保金状态
        Integer isWarranty = 0;
        if (bList.get("isWarranty") != null) {
            isWarranty = Integer.valueOf(bList.get("isWarranty").toString());
        } else {
            isWarranty = 0;
        }
        if (isPrice != 0) {
            if (isPrice == 1) {
                isDeposit = 0;
                isWarranty = 0;
            }
            if (isPrice == 2) {
                isDeposit = 1;
                isWarranty = 0;
            }
            if (isPrice == 3) {
                isDeposit = 2;
                isWarranty = 0;
            }
            if (isPrice == 4) {
                isDeposit = 0;
                isWarranty = 1;

            }
            if (isPrice == 5) {
                isDeposit = 0;
                isWarranty = 2;
            }
        } else {

        }

        if (bid != 0) {
            //原来经理人id
            Integer uid = Integer.valueOf(bList.get("uid").toString());
            String old_agent = null;//旧的经理人
            if (uid == 0 || uid == 22) {
                if(uid==0){
                    old_agent = "待分配";
                }
                if (uid==22){
                    String sql_old_agent = "select name from yx_user where  and id=?";
                    old_agent = jdbcTemplate.queryForObject(sql_old_agent, String.class, 22);
                }
            } else {
                String sql_old_agent = "select name from yx_user where  id=?";
                old_agent = jdbcTemplate.queryForObject(sql_old_agent, String.class, uid);
            }

            String sql_new_agent = "select name from yx_user where  id=?";
            //新的经理人
            String new_agent = jdbcTemplate.queryForObject(sql_new_agent, String.class, bid);
            String update_txt = old_agent + "," + new_agent;

            //修改经理人
            String sql_matorn = "update yx_matorn set uid=? where id=? ";
            int states_matorn = jdbcTemplate.update(sql_matorn, bid, mid);

            //记录信息
            //type=0 修改经理人
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String update_time = time.format(new Date());
            String sql_update_grade = "insert into yx_update_grade(uid,aid,mid,new_uid,update_time,update_txt,type) values(?,?,?,?,?,?,?)";
            int states_update_grade = this.jdbcTemplate.update(sql_update_grade, bList.get("lid"), 0, mid, bid, update_time, update_txt, 0);

        } else {

        }

        String sql_bussiness = "update yx_bussiness set grade=?,isblack=?,shelf=?,isDeposit=?,isWarranty=? where mid=?";
        int states_bussiness = jdbcTemplate.update(sql_bussiness, new_grade, isBlack, isShelf, isDeposit, isWarranty, mid);

        if (states_bussiness > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int setMatornCount(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        int type = jsonObject.getInteger("type");
        int count = jsonObject.getInteger("count");
        String sql = "update yx_matornSet set count=? where type=?";
        int states = jdbcTemplate.update(sql, count, type);
        if (states > 0) {
            return states;
        } else {
            return 0;
        }
    }

    @Override
    public int addMatornDto(MatornDto matornDto) {

        String date = matornDto.getBorn();
        String str = date.substring(0, 4);
        String str1 = date.substring(5, 7);
        String str2 = date.substring(8, 10);
        Integer year = Integer.valueOf(str);
        Integer month = Integer.valueOf(str1);
        Integer day = Integer.valueOf(str2);

        String zodiac = getYear(year);
        String constellation = getConstellation(month, day);

        String height=matornDto.getWeightHeight().substring(0, matornDto.getWeightHeight().indexOf("/"));
        String weight=matornDto.getWeightHeight().substring(matornDto.getWeightHeight().indexOf("/")+1);;

        //添加月嫂信息
        String sql_addMatorn = "insert into yx_matorn(uid,lid,name, sex, born,nation,idcard,idcard_type,household,marriage,educational,weight,height,address,zodiac,constellation,idtype) " +
                "values(?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

         int states_addMatorn=0;
            //uid=10 默认经理人
            if (matornDto.getUid() != 0) {
                states_addMatorn = jdbcTemplate.update(sql_addMatorn, 10, matornDto.getUid(), matornDto.getName(), 0, matornDto.getBorn(), matornDto.getNation(), matornDto.getIdcard(), matornDto.getIdcard_type(), matornDto.getHousehold(), matornDto.getMarriage(), matornDto.getEducational(),weight, height, matornDto.getAddress(), zodiac, constellation, 1);
            } else {
                states_addMatorn = jdbcTemplate.update(sql_addMatorn, 10, 16, matornDto.getName(), 0, matornDto.getBorn(), matornDto.getNation(), matornDto.getIdcard(), matornDto.getIdcard_type(), matornDto.getHousehold(), matornDto.getMarriage(), matornDto.getEducational(),weight, height, matornDto.getAddress(), zodiac, constellation, 1);
            }
            int mid = getForId();
            String sql1 = "insert into yx_userinfo(uid,mid) values (?,?)";
            int states1 = jdbcTemplate.update(sql1, matornDto.getUid(), mid);

            //添加月嫂联系信息
        String sql_addContact = "insert into yx_contact(mid,phone,bank_card,bank_name,emergency_person,emergency_phone) " +
                "values(?,?,?,?,?,?)";
        int states_addContact=jdbcTemplate.update(sql_addContact,mid,matornDto.getPhone(),matornDto.getBank_card(),matornDto.getBank_name(),matornDto.getEmergency_person(),matornDto.getEmergency_phone());

        //添加月嫂来源信息
        String sql_addOrigin = "insert into yx_origin(mid,source,institution_name,witness,witness_phone,other,introducer,introducer_phone) " +
                "values(?,?,?,?,?,?,?,?)";
        int states_addOrigin=jdbcTemplate.update(sql_addOrigin,mid,matornDto.getInstitution_name(),matornDto.getWitness(),matornDto.getWitness_phone(),matornDto.getOther(),matornDto.getIntroducer(),matornDto.getIntroducer_phone());

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creat_time = time.format(new Date());

        //添加月嫂业务信息
        String sql_addBussiness = "insert into yx_bussiness(mid,photo,charact,work_age,works,trains,qualification,grade,creat_time,number,day,isorder,isblack,shelf,identity,heathly) " +
                "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String grade="N";//默认N
        String number = getNumber(mid);
        int states_addBussiness = this.jdbcTemplate.update(sql_addBussiness, mid, matornDto.getPhoto(), matornDto.getCharacter(),matornDto.getWork_age(), matornDto.getWorks(), matornDto.getTrains(), matornDto.getQualification(), grade, creat_time,number,0,0,0,0,matornDto.getIdentity(),matornDto.getHeathly() );

        String sql_addPeriod = "insert into yx_period(mid,,rest) values(?,?)";
        String rest = "[]";
        int states_addPeriod= this.jdbcTemplate.update(sql_addPeriod, mid, rest);

        if (states_addMatorn>0&states_addBussiness>0&&states_addContact>0&&states_addOrigin>0&&states_addPeriod>0){
            return 1;
        }else {
            return 0;
        }

    }

    @Override
    public List<Map<String, Object>> workAge() {
        String sql="select name from yx_workAge";
        List<Map<String, Object>> list=jdbcTemplate.queryForList(sql);

        return list;
    }
}




