package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.CustomAppletDao;
import com.example.entiy.Custom;
import com.example.entiy.Matorn;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STSourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import redis.clients.jedis.BinaryClient;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.util.BASE64.getUserInfo;
import static com.example.util.BASE64.maskPhone;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.NumberUtil.*;
import static com.example.util.NumberUtil.getWeek;
import static com.example.util.PageUtil.getHalfPage;
import static com.example.util.PageUtil.getPage;
import static com.example.util.PeriodUtil.getReturnPeriod;
import static com.example.util.SearchUtil.searchPeriod;
import static com.example.util.Year.*;

@Repository
public class CustomAppletDaoImpl implements CustomAppletDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;
    @Autowired(required = false)
    private RedisTemplate redisTemplate;

    @Transactional
    @Override
    public Map<String, Object> getCustomAppletLogin(String json) {

        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(json);
        String encryptedData = jsonObject.getString("encryptedData");
        String iv = jsonObject.getString("iv");
        String sessionKey = jsonObject.getString("sessionKey");
        Integer rid = jsonObject.getInteger("rid");
        Integer fid = jsonObject.getInteger("fid");
        Integer gid = jsonObject.getInteger("gid");

        System.out.println("rid_________" + rid);
        System.out.println("fid_________" + fid);
        System.out.println("gid_________" + gid);
        JSONObject jsonObject1 = getUserInfo(encryptedData, sessionKey, iv);
        String phone = jsonObject1.getString("phoneNumber");

        //查询客户手机号是否添加
        String sql_phone = "select count(*) from yx_custom where c_phone=?";
        int count = this.jdbcTemplate.queryForObject(sql_phone, Integer.class, phone);

//        String sql_count_demand="select count(*) from yx_order_demand where cid=?";
//        Integer count_demand=this.jdbcTemplate.queryForObject(sql_count_demand, Integer.class, cid);
        if (rid != 0) {
            //有推荐人id
            if (count > 0) {
                //有推荐人id注册过
                String sql1 = "select id,c_name,c_phone,production_situation,nodate,production_date,constellation,isPartner from yx_custom where c_phone=?";
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                list = this.jdbcTemplate.query(sql1.toString(), new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        mp.put("cid", rs.getInt("id"));
                        mp.put("phone", rs.getString("c_phone"));
                        mp.put("cname", rs.getString("c_name"));
                        mp.put("production_situation", rs.getInt("production_situation"));
                        if (rs.getInt("production_situation") == 0) {
                            mp.put("time", rs.getString("nodate"));
                        } else {
                            mp.put("time", rs.getString("production_date"));
                        }
                        mp.put("constellation", rs.getString("constellation"));
                        mp.put("isPartner", rs.getInt("isPartner"));
                        return mp;
                    }
                }, phone);
                map.put("phone", list.get(0).get("phone"));
                map.put("cname", list.get(0).get("cname"));
                map.put("cid", list.get(0).get("cid"));
                map.put("production_situation", list.get(0).get("production_situation"));
                map.put("time", list.get(0).get("time"));
                map.put("constellation", list.get(0).get("constellation"));
                String sql_list = "select rid,fid,gid from yx_recommend where cid=?";
                List<Map<String, Object>> idList = this.jdbcTemplate.queryForList(sql_list, rid);
                if (idList.size() > 0) {
                    //推荐人的id
                    rid = rid;
                    //推荐人的推荐人id，二级推荐人id
                    if (idList.get(0).get("rid") != null) {
                        fid = Integer.valueOf(idList.get(0).get("rid").toString());

                    } else {
                        fid = 0;
                    }
                    //推荐人的推荐人推荐人的id，三级级推荐人id
                    if (idList.get(0).get("fid") != null) {
                        gid = Integer.valueOf(idList.get(0).get("fid").toString());
                    } else {
                        gid = 0;
                    }
                } else {
                    rid = 0;
                    fid = 0;
                    gid = 0;
                }
                System.out.println("cid____1_______" + list.get(0).get("cid"));
                System.out.println("rid____1_______" + rid);
                System.out.println("fid____1_______" + fid);
                System.out.println("gid____1_______" + gid);
                map.put("rid", rid);
                map.put("fid", fid);
                map.put("gid", gid);
                String sql_count_demand = "select count(*) from yx_order_demand where cid=?";
                Integer count_demand = this.jdbcTemplate.queryForObject(sql_count_demand, Integer.class, list.get(0).get("cid"));
                Integer isAdd = 0;
                if (count_demand > 0) {
                    isAdd = 1;
                } else {
                    isAdd = 0;
                }
                //客户是填写需求订单
                map.put("isAdd", isAdd);
                map.put("isRegister", 1);
                //判断这个客户是否有完成的订单，有完成的是合伙人，没有不是
                map.put("isPartner", list.get(0).get("isPartner"));
                return map;
            } else {
                //有推荐人id未注册

                //未添加
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String creat_time = time.format(new Date());

                StringBuffer sb = new StringBuffer();
                sb.append("insert into yx_custom (bid,origin,channel,c_phone,creat_time,integral,isPartner)");
                sb.append("value(?,?,?,?,?,?,?)");
                //添加客户表
                int states = this.jdbcTemplate.update(sb.toString(), 0, "线上", "合伙人", phone, creat_time, 0, 0);
                String sql = "select max(id) from yx_custom";
                int cid = this.jdbcTemplate.queryForObject(sql, Integer.class);

                //添加推荐表，注册没有测一测的用户
                StringBuffer sb1 = new StringBuffer();
                sb1.append("insert into yx_recommend(cid,rid,fid,gid,isLogin,creat_time)");
                sb1.append("value(?,?,?,?,?,?)");
                int states_insert = this.jdbcTemplate.update(sb1.toString(), cid, rid, fid, gid, 1, creat_time);

                if (states > 0 && states_insert > 0) {
                    map.put("phone", phone);
                    map.put("isPartner", 0);//不是合伙人
                    System.out.println("cid____3_______" + cid);
                    System.out.println("rid____3_______" + rid);
                    System.out.println("fid____3_______" + fid);
                    System.out.println("gid____3_______" + gid);
                    map.put("cid", cid);
                    map.put("rid", rid);
                    map.put("fid", fid);
                    map.put("gid", gid);
                    map.put("isAdd", 0);
                    map.put("isRegister", 0);
                    return map;
                } else {
                    return map;
                }
            }

        } else {
            //没有推荐人id
            if (count > 0) {
                //添加过
                String sql1 = "select id,c_name,c_phone,production_situation,nodate,production_date,constellation,isPartner from yx_custom where c_phone=?";
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                list = this.jdbcTemplate.query(sql1.toString(), new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        mp.put("cid", rs.getInt("id"));
                        mp.put("phone", rs.getString("c_phone"));
                        mp.put("cname", rs.getString("c_name"));
                        mp.put("production_situation", rs.getInt("production_situation"));
                        if (rs.getInt("production_situation") == 0) {
                            mp.put("time", rs.getString("nodate"));
                        } else {
                            mp.put("time", rs.getString("production_date"));
                        }
                        mp.put("constellation", rs.getString("constellation"));
                        mp.put("isPartner", rs.getInt("isPartner"));
                        return mp;
                    }
                }, phone);
                map.put("isPartner", list.get(0).get("isPartner"));
                map.put("phone", list.get(0).get("phone"));
                map.put("cid", list.get(0).get("cid"));
                map.put("cname", list.get(0).get("cname"));
                map.put("production_situation", list.get(0).get("production_situation"));
                map.put("time", list.get(0).get("time"));
                map.put("constellation", list.get(0).get("constellation"));
                String sql_count_demand = "select count(*) from yx_order_demand where cid=?  ";
                Integer count_demand = this.jdbcTemplate.queryForObject(sql_count_demand, Integer.class, list.get(0).get("cid"));
                Integer isAdd = 0;
                if (count_demand > 0) {
                    isAdd = 1;
                } else {
                    isAdd = 0;
                }
                String sql_list = "select rid,fid,gid from yx_recommend where cid=?";
                List<Map<String, Object>> idList = this.jdbcTemplate.queryForList(sql_list, list.get(0).get("cid"));
                if (idList.size() > 0) {
                    if (idList.get(0).get("rid") != null) {
                        rid = Integer.valueOf(idList.get(0).get("rid").toString());
                    } else {
                        rid = 0;
                    }
                    if (idList.get(0).get("fid") != null) {
                        fid = Integer.valueOf(idList.get(0).get("fid").toString());
                    } else {
                        fid = 0;
                    }
                    if (idList.get(0).get("gid") != null) {
                        gid = Integer.valueOf(idList.get(0).get("gid").toString());
                    } else {
                        gid = 0;
                    }
                } else {
                    rid = 0;
                    fid = 0;
                    gid = 0;
                }
                System.out.println("cid____2_______" + list.get(0).get("cid"));
                System.out.println("rid____2_______" + rid);
                System.out.println("fid____2_______" + fid);
                System.out.println("gid____2_______" + gid);
                map.put("rid", rid);
                map.put("fid", fid);
                map.put("gid", gid);
                map.put("isAdd", isAdd);
                map.put("isRegister", 1);
                return map;
            } else {
                //未添加
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String creat_time = time.format(new Date());
                StringBuffer sb = new StringBuffer();
                sb.append("insert into yx_custom (bid,origin,channel,c_phone,creat_time,integral,isPartner)");
                sb.append("value(?,?,?,?,?,?,?)");
                //添加客户表
                int states = this.jdbcTemplate.update(sb.toString(), 0, "线上", "合伙人", phone, creat_time, 0, 0);
                String sql = "select max(id) from yx_custom";
                int cid = this.jdbcTemplate.queryForObject(sql, Integer.class);

                if (states > 0) {
                    map.put("phone", phone);
                    map.put("cid", cid);
                    map.put("isPartner", 0);//不是合伙人
                    map.put("rid", 0);
                    map.put("fid", 0);
                    map.put("gid", 0);
                    map.put("isAdd", 0);
                    map.put("isRegister", 0);
                    return map;
                } else {
                    return map;
                }
            }

        }


    }

    @Override
    public int addCustomPhoto(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");
        String c_photo = jsonObject.getString("c_photo");
        String c_name = jsonObject.getString("c_name");
        String sql = "update yx_custom set c_name=?,c_photo=? where id=?";

        int states = this.jdbcTemplate.update(sql, c_name, c_photo, cid);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Transactional
    @SneakyThrows
    @Override
    public List<Map<String, Object>> recommendMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
        String start = jsonObject.getString("start");
        Integer cid = jsonObject.getInteger("cid");

        String constellation = jsonObject.getString("constellation");
        String born = jsonObject.getString("born");
        String c_name = jsonObject.getString("c_name");
        Integer isAdd = jsonObject.getInteger("isAdd");
        System.out.println("isAdd = " + isAdd);
        Integer rid = jsonObject.getInteger("rid");

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowTime = time.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = sdf.parse(nowTime);
        Date start_ = sdf.parse(start);
        Integer order_type = 0;
        Integer production_situation = 0;
        String times = null;
        String sql_update = null;
        if (now.before(start_)) {
            //表示now小于start_
            times = start;
            order_type = 0;
            production_situation = 0;
            sql_update = "update yx_custom set c_name=?,production_situation=?,nodate=?,constellation=?,update_time=?,born=? where id=?";

        } else {
            times = nowTime;
            order_type = 1;
            production_situation = 1;
            sql_update = "update yx_custom set c_name=?,production_situation=?,production_date=?,constellation=?,update_time=?,born=? where id=?";
        }
        String endtime = getNewEndtime(times, -26);
        Integer states_update = 0;
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String update_time = time1.format(new Date());
        if (rid != 0) {
            //有推荐人id
            if (isAdd > 0) {
                //有推荐人id添加过订单需求表

                //修改时间更新
                states_update = this.jdbcTemplate.update(sql_update, c_name, production_situation, start, constellation, update_time, born, cid);

                String sql_demand = "update yx_order_demand set order_type=?,service_type=?,service_day=?,starttime=?,endtime=?,timetype=?,level=?,price=? where cid=?";
                int states_demand = this.jdbcTemplate.update(sql_demand, order_type, "居家服务", 26, times, endtime, "长期", "9800", "9800", cid);
            } else {
                //修改时间更新
                states_update = this.jdbcTemplate.update(sql_update, c_name, production_situation, start, constellation, update_time, born, cid);

                //没有添加订单需求表
                StringBuffer sb2 = new StringBuffer();
                sb2.append("insert into yx_order_demand(cid,order_type,service_type,service_day,starttime,endtime,timetype,level,price,threestates)");
                sb2.append("value(?,?,?,?,?,?,?,?,?,?)");
                //添加订单需求表
                int states_demand = this.jdbcTemplate.update(sb2.toString(), cid, order_type, "居家服务", 26, times, endtime, "长期", "9800", "9800", 0);
                String sql_did = "select max(id) from yx_order_demand";
                int did = this.jdbcTemplate.queryForObject(sql_did, Integer.class);
                //添加订单表
                StringBuffer sb3 = new StringBuffer();
                sb3.append("insert into yx_order (did,mid,cid,type,states)");
                sb3.append("value(?,?,?,?,?)");
                int states_order = this.jdbcTemplate.update(sb3.toString(), did, 0, cid, 0, 0);
                //修改推荐表，添加，用户注册并测一测的用户
                StringBuffer sb4 = new StringBuffer();
                String sql_re = "update yx_recommend set isTest=? where cid=?";
                int states_re = this.jdbcTemplate.update(sql_re, 1, cid);
                //添加积分

            }
        } else {
            //没有推荐人id
            if (isAdd > 0) {
                //添加过

                //修改时间更新
                states_update = this.jdbcTemplate.update(sql_update, c_name, production_situation, start, constellation, update_time, born, cid);
                //修改订单需求表
                String sql_demand = "update yx_order_demand set order_type=?,service_type=?,service_day=?,starttime=?,endtime=?,timetype=?,level=?,price=? where cid=?";
                int states_demand = this.jdbcTemplate.update(sql_demand, order_type, "居家服务", 26, times, endtime, "长期", "9800", "9800", cid);
            } else {
                //没有添加

                //修改时间更新
                states_update = this.jdbcTemplate.update(sql_update, c_name, production_situation, start, constellation, update_time, born, cid);

                //没有添加订单需求表
                StringBuffer sb2 = new StringBuffer();
                sb2.append("insert into yx_order_demand(cid,order_type,service_type,service_day,starttime,endtime,timetype,level,price,threestates)");
                sb2.append("value(?,?,?,?,?,?,?,?,?,?)");
                //添加订单需求表
                int states_demand = this.jdbcTemplate.update(sb2.toString(), cid, order_type, "居家服务", 26, times, endtime, "长期", "9800", "9800", 0);
                String sql_did = "select max(id) from yx_order_demand";
                int did = this.jdbcTemplate.queryForObject(sql_did, Integer.class);
                //添加订单表
                StringBuffer sb3 = new StringBuffer();
                sb3.append("insert into yx_order (did,mid,cid,type,states)");
                sb3.append("value(?,?,?,?,?)");
                int states_order = this.jdbcTemplate.update(sb3.toString(), did, 0, cid, 0, 0);
            }
        }


        List<String> constellationList = getConstellation(constellation);
        StringBuffer sb = new StringBuffer();
        sb.append("select yx_matorn.id,yx_period.period,yx_bussiness.grade,yx_bussiness.isorder from yx_matorn left join yx_period on (yx_matorn.id=yx_period.mid)");
        sb.append(" left join yx_bussiness on (yx_matorn.id=yx_bussiness.mid)");
        sb.append(" where yx_bussiness.shelf=1 and yx_period.period is not null");
        sb.append(" and  yx_matorn.constellation in (");//客户对应的三个月嫂星座
        for (int i = 0; i < constellationList.size(); i++) {
            sb.append("'");
            sb.append(constellationList.get(i));
            sb.append("'");
            if (i < constellationList.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");

        list1 = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("mid", rs.getInt("id"));
                mp.put("period", rs.getString("period"));
                mp.put("isorder", rs.getInt("isorder"));
                return mp;
            }
        });

        List<Map<String, Object>> notOrderList = new ArrayList<Map<String, Object>>();//不在订单
        List<Map<String, Object>> OnOrderList = new ArrayList<Map<String, Object>>();//在订单
        for (int i = 0; i < list1.size(); i++) {
            if (Integer.valueOf(list1.get(i).get("isorder").toString()) == 0) {
                notOrderList.add(list1.get(i));
            } else {
                OnOrderList.add(list1.get(i));
            }
        }

        String sql_return_time = "select return_time from yx_order where mid=? and order_states=2 ";
        List<Map<String, Object>> NewOrderList = new ArrayList<Map<String, Object>>();
        //不在订单和在订单的月嫂集合

        for (int i = 0; i < OnOrderList.size(); i++) {
            String return_time = this.jdbcTemplate.queryForObject(sql_return_time, String.class, Integer.valueOf(OnOrderList.get(i).get("mid").toString()));
            String period = null;
            try {
                period = getReturnPeriod(OnOrderList.get(i).get("period").toString(), return_time);
                OnOrderList.get(i).put("period", "");
                if (period.equals("{}")) {

                } else {
                    OnOrderList.get(i).put("period", period);
                    NewOrderList.add(OnOrderList.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        NewOrderList.addAll(notOrderList);//两个集合合并
        List<Integer> periodlist = new ArrayList<Integer>();
        periodlist = searchPeriod(NewOrderList, start);
        String sql = "select mid from yx_collect_browse where cid=? and isBrowse=1";
        List<Map<String, Object>> browse = this.jdbcTemplate.queryForList(sql, cid);
        List<Integer> browseList = new ArrayList<>();
        //浏览记录
        if (browse.size() != 0) {
            for (int i = 0; i < browse.size(); i++) {
                browseList.add(Integer.valueOf(browse.get(i).get("mid").toString()));
            }
        } else {
        }
        System.out.println("_________" + browseList);
        periodlist.removeAll(browseList);//推荐集合去除浏览记录集合
        Random rand = new Random();
        List<Integer> threeList = new ArrayList<Integer>();
        if (periodlist.size() > 0) {
            for (int i = 0; i < 3; i++) {
                Integer num = periodlist.get(rand.nextInt(periodlist.size()));
                threeList.add(num);
                periodlist.remove(num);
            }
            StringBuffer sb1 = new StringBuffer();
            sb1.append("select yx_matorn.id,yx_matorn.uid,yx_matorn.name,yx_matorn.born,yx_matorn.household,yx_matorn.zodiac,yx_matorn.constellation,");
            sb1.append("yx_bussiness.photo,yx_bussiness.isorder,yx_contact.phone,yx_bussiness.grade from yx_matorn");
            sb1.append(" LEFT JOIN yx_bussiness on (yx_matorn.id=yx_bussiness.mid) LEFT JOIN yx_contact on (yx_matorn.id=yx_contact.mid)");
            sb1.append(" where yx_matorn.id=? ");

            for (int i = 0; i < threeList.size(); i++) {

                list2 = this.jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();

                        mp.put("mid", rs.getInt("id"));
                        mp.put("name", rs.getString("name"));
                        mp.put("zodiac", rs.getString("zodiac"));
                        mp.put("constellation", rs.getString("constellation"));
                        mp.put("photo", rs.getString("photo"));
                        mp.put("grade", rs.getString("grade"));
                        mp.put("isorder", rs.getInt("isorder"));
                        String sql_agent = "select name FROM  yx_user where id=?";
                        String agent = jdbcTemplate.queryForObject(sql_agent, String.class, rs.getInt("uid"));
                        mp.put("agent", agent);
                        String birthTime = rs.getString("born");
                        Long age = getAge(birthTime);//得到年龄
                        mp.put("age", age);
                        String household = rs.getString("household");
                        String city = "";
                        if (household.length() > 3) {
                            String h1 = household.substring(0, 2);
                            String h2 = household.substring(3, 5);
                            city = h1 + h2;
                        } else {
                            city = household.substring(0, 2);
                        }
                        mp.put("city", city);
                        return mp;
                    }
                }, Integer.valueOf(threeList.get(i).toString()));
                list.add(list2.get(0));
            }
            return list;
        } else {
            return list;
        }

    }


    @SneakyThrows
    @Override
    public Map<String, Object> getCustomConstellation(String json) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer cid = jsonObject.getInteger("cid");


        String sql = "select c_name,production_situation,nodate,production_date,born,constellation from yx_custom where id=?";
        List<Map<String, Object>> allList = this.jdbcTemplate.queryForList(sql, cid);
        String times = null;
        Integer production_situation = 0;
        if (allList.get(0).get("production_situation") != null) {
            production_situation = Integer.valueOf(allList.get(0).get("production_situation").toString());
            if (production_situation == 0) {
                times = allList.get(0).get("nodate").toString();
            } else {
                times = allList.get(0).get("production_date").toString();
            }
            map.put("production_situation", production_situation);
            map.put("start", times);
        } else {
            map.put("production_situation", null);
            map.put("start", null);
        }

        if (allList.get(0).get("born") != null) {
            map.put("born", allList.get(0).get("born").toString());
        } else {
            map.put("born", null);
        }

        map.put("c_name", allList.get(0).get("c_name").toString());

        if (allList.get(0).get("constellation")!=null){
            map.put("constellation", allList.get(0).get("constellation").toString());
        }else {
            map.put("constellation", null);
        }

        String sql_count = "select count(*) from yx_order_demand where cid=?";
        Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class, cid);

        if (count > 0) {
            //客户已填写需求表
            map.put("isAdd", 1);
        } else {
            //没有填写需求表
            map.put("isAdd", 0);

        }

        return map;
    }

    @Override
    public int addCollect(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        Integer cid = jsonObject.getInteger("cid");
        Integer isCustomCollect = jsonObject.getInteger("isCustomCollect");

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String collectTime = time.format(new Date());
        int states = 0;
        if (isCustomCollect != 0) {
            //收藏
            String sql = "select count(*) from yx_collect_browse where cid=? and mid=?";
            Integer count = this.jdbcTemplate.queryForObject(sql, Integer.class, cid, mid);
            if (count > 0) {
                //有mid,修改收藏时间，
                String sql_update = "update yx_collect_browse set isCollect=1,collectTime=? where cid=? and mid=?";
                states = this.jdbcTemplate.update(sql_update, collectTime, cid, mid);
            } else {
                //没有，添加mid
                StringBuffer sb = new StringBuffer();
                sb.append("insert into yx_collect_browse(cid,mid,isCollect,collectTime)");
                sb.append("value(?,?,?,?)");
                states = this.jdbcTemplate.update(sb.toString(), cid, mid, 1, collectTime);
            }

        } else {
            //取消收藏
            String sql_update = "update yx_collect_browse set isCollect=0,collectTime=? where cid=? and mid=?";
            states = this.jdbcTemplate.update(sql_update, collectTime, cid, mid);
        }

        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @SneakyThrows
    @Override
    public Map<String, Object> myCollectBrowse(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");
        String somnus = jsonObject.getString("somnus");
        Integer start = jsonObject.getInteger("start");
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sb = new StringBuffer();
        List<Integer> collectBrowseList = new ArrayList<Integer>();
        List<Integer> collectList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        //面试月嫂
        String sql3 = "select appointment from yx_custom where id=?";
        Integer appointment = this.jdbcTemplate.queryForObject(sql3, Integer.class, cid);
        if (somnus.equals("collect")) {
            //收藏
            String sql = "select mid from yx_collect_browse where cid=? and isCollect=1 order by collectTime desc";
            List<Map<String, Object>> midList = this.jdbcTemplate.queryForList(sql, cid);
            if (midList != null) {
                for (int i = 0; i < midList.size(); i++) {
                    collectBrowseList.add(Integer.valueOf(midList.get(i).get("mid").toString()));
                    collectList.add(Integer.valueOf(midList.get(i).get("mid").toString()));
                }
                sb.append("select yx_matorn.id,yx_matorn.name,yx_matorn.born,yx_matorn.household,yx_matorn.zodiac,yx_matorn.constellation,yx_bussiness.photo");
                sb.append(" from yx_matorn left join yx_bussiness on (yx_matorn.id=yx_bussiness.mid)");
                sb.append("where yx_matorn.id=?");
            } else {
                map.put("page", 0);
                map.put("list", list);
                return map;
            }
        }
        if (somnus.equals("browse")) {
            String sql = "select mid from yx_collect_browse where cid=? and isCollect=1 order by collectTime desc";
            List<Map<String, Object>> midList1 = this.jdbcTemplate.queryForList(sql, cid);
            //收藏
            if (midList1.size() != 0) {
                //后面是否收藏判断
                for (int i = 0; i < midList1.size(); i++) {
                    collectList.add(Integer.valueOf(midList1.get(i).get("mid").toString()));
                }
            } else {
            }
            //浏览记录
            String sql1 = "select mid from yx_collect_browse where cid=? and isBrowse=1 order by browseTime desc";
            List<Map<String, Object>> midList2 = this.jdbcTemplate.queryForList(sql1, cid);
            System.out.println("midList2__________" + midList2);
            if (midList2.size() != 0) {
                for (int i = 0; i < midList2.size(); i++) {
                    collectBrowseList.add(Integer.valueOf(midList2.get(i).get("mid").toString()));
                }
                sb.append("select yx_matorn.id,yx_matorn.name,yx_matorn.born,yx_matorn.household,yx_matorn.zodiac,yx_matorn.constellation,yx_bussiness.photo");
                sb.append(" from yx_matorn left join yx_bussiness on (yx_matorn.id=yx_bussiness.mid)");
                sb.append("where yx_matorn.id=?");
            } else {
                map.put("page", 0);
                map.put("list", list);
                return map;
            }

        }
        map.put("page", collectBrowseList.size());
        //分页，五条一页
        if (collectBrowseList.size() < start * 5) {
            collectBrowseList = collectBrowseList.subList((start - 1) * 5, collectBrowseList.size());
        } else {
            collectBrowseList = collectBrowseList.subList((start - 1) * 5, start * 5);
        }

        Integer flag = 0;
        Integer flag1 = 0;
        for (int i = 0; i < collectBrowseList.size(); i++) {
            //判断记录里哪一个被收藏
            if (collectList.size() > 0) {
                if (collectList.contains(collectBrowseList.get(i))) {
                    flag = 1;
                } else {
                    flag = 0;
                }
            } else {
                flag = 0;
            }
            Integer iscollect = flag;
            Integer mid = collectBrowseList.get(i);
            // 判断记录里哪一个月嫂面试
            if (appointment.equals(mid)) {
                flag1 = 1;
            } else {
                flag1 = 0;
            }
            Integer isAppointment = flag1;

            List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
            list1 = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("mid", rs.getInt("id"));
                    mp.put("name", rs.getString("name"));
                    mp.put("iscollect", iscollect);//收藏状态
                    mp.put("isAppointment", isAppointment);//面试状态
                    String birthTime = rs.getString("born");
                    Long age = getAge(birthTime);//得到年龄
                    mp.put("age", age);
                    String household = rs.getString("household");
                    String city = "";
                    if (household.length() > 3) {
                        String h1 = household.substring(0, 2);
                        String h2 = household.substring(3, 5);
                        city = h1 + h2;
                    } else {
                        city = household.substring(0, 2);
                    }
                    mp.put("city", city);
                    mp.put("zodiac", rs.getString("zodiac"));
                    mp.put("constellation", rs.getString("constellation"));
                    mp.put("photo", rs.getString("photo"));

                    String sql_comment = "select comment from yx_score where mid=? ";
                    String comment = null;
                    try {
                        comment = jdbcTemplate.queryForObject(sql_comment, String.class, mid);
                        if (comment != null) {
                            mp.put("comment", comment);
                        } else {
                            mp.put("comment", comment);
                        }
                    } catch (Exception e) {
                        mp.put("comment", null);
                    }
                    return mp;
                }
            }, mid);

            list.add(list1.get(0));
        }
        map.put("list", list);

        return map;

    }

    @Transactional
    @Override
    public List<Map<String, Object>> getOneMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer mid = jsonObject.getInteger("mid");
        Integer cid = jsonObject.getInteger("cid");

        String sql_browse = "select  count(*) from yx_collect_browse where cid=? and mid=?";
        Integer count = this.jdbcTemplate.queryForObject(sql_browse, Integer.class, cid, mid);

        System.out.println("count = " + count);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String browseTime = time.format(new Date());
        if (count > 0) {
            //添加过mid的记录
            String sql_update = "update yx_collect_browse set browseTime=? where cid=? and mid=?";
            int states = this.jdbcTemplate.update(sql_update, browseTime, cid, mid);
        } else {
            //没有添加mid记录
            StringBuffer sb = new StringBuffer();
            sb.append("insert into yx_collect_browse(cid,mid,isBrowse,browseTime)");
            sb.append("value(?,?,?,?)");
            int states = this.jdbcTemplate.update(sb.toString(), cid, mid, 1, browseTime);
        }
        List list = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select yx_matorn.id,yx_matorn.uid,yx_matorn.name,yx_matorn.born,yx_matorn.household,yx_matorn.zodiac,yx_matorn.nation,yx_matorn.constellation,yx_bussiness.photo,yx_bussiness.grade,yx_contact.phone,yx_bussiness.number,");
        sb.append(" yx_matorn.nation,yx_matorn.household,yx_matorn.marriage,yx_matorn.weight,yx_matorn.height,yx_matorn.educational,yx_bussiness.work_age,yx_bussiness.identity,yx_bussiness.heathly,yx_bussiness.reportPdf,yx_bussiness.vedio,yx_bussiness.qualification from yx_matorn ");
        sb.append(" LEFT JOIN yx_bussiness on (yx_matorn.id=yx_bussiness.mid) LEFT JOIN yx_contact on (yx_matorn.id=yx_contact.mid)");
        sb.append(" where yx_matorn.id = ?");

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                List photolist = new ArrayList();

                String birthTime = rs.getString("born");
                Long age = getAge(birthTime);//得到年龄
                mp.put("age", age);
                String household = rs.getString("household");
                String city = "";
                if (household.length() > 3) {
                    String h1 = household.substring(0, 2);
                    String h2 = household.substring(3, 5);
                    city = h1 + h2;
                } else {
                    city = household.substring(0, 2);
                }
                Integer uid=rs.getInt("uid");
                if (uid==0||uid==22){
                    String old_agent=null;
                    if(uid==0){
                        old_agent = "待分配";
                    }
                    if (uid==22){
                        String sql_old_agent = "select name from yx_user where  and id=?";
                        old_agent = jdbcTemplate.queryForObject(sql_old_agent, String.class, 22);
                    }
                    mp.put("agent", old_agent);
                }else {
                    String sql_agent = "select name FROM  yx_user where id=?";
                    String agent = jdbcTemplate.queryForObject(sql_agent, String.class, rs.getInt("uid"));
                    mp.put("agent", agent);
                }

                mp.put("nation", rs.getString("nation"));
                mp.put("household", rs.getString("household"));
                mp.put("marriage", rs.getString("marriage"));
                mp.put("weight", rs.getString("weight"));
                mp.put("height", rs.getString("height"));
                mp.put("educational", rs.getString("educational"));
                mp.put("work_age", rs.getString("work_age"));

                mp.put("name", rs.getString("name"));
                mp.put("city", city);
                mp.put("mid", rs.getInt("id"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("constellation", rs.getString("constellation"));
                mp.put("born", rs.getString("born"));
                mp.put("photo", rs.getString("photo"));
                mp.put("grade", rs.getString("grade"));
                mp.put("phone", rs.getString("phone"));
                mp.put("number", rs.getString("number"));
                mp.put("reportPdf", rs.getString("reportPdf"));

                Integer mid = rs.getInt("id");
                List identity = null;
                List heathly = null;
                List qualification = null;
                // String vedio = "i3042ay0jxu";

                if (rs.getString("vedio") != null && rs.getString("vedio") != "") {
                    mp.put("vedio", rs.getString("vedio"));
                } else {
                    // mp.put("vedio", vedio);
                }

                if (rs.getString("identity") != null && rs.getString("identity") != "") {
                    identity = JSONArray.fromObject(rs.getString("identity"));
                    mp.put("identity", identity);
                    for (int i = 0; i < identity.size(); i++) {
                        photolist.add(identity.get(i));
                    }
                } else {

                }
                if (rs.getString("heathly") != null && rs.getString("heathly") != "") {
                    heathly = JSONArray.fromObject(rs.getString("heathly"));
                    mp.put("heathly", heathly);
                    for (int i = 0; i < heathly.size(); i++) {
                        photolist.add(heathly.get(i));
                    }
                } else {

                }

                if (rs.getString("qualification") != null && rs.getString("qualification") != "") {
                    qualification = JSONArray.fromObject(rs.getString("qualification"));
                    mp.put("qualification", qualification);
                    for (int i = 0; i < qualification.size(); i++) {
                        photolist.add(qualification.get(i));
                    }
                } else {

                }
                mp.put("photolist", photolist);
                String sql = "select * from yx_evaluate where mid =? order by id desc";
                String sql_mid = "select mid from yx_evaluate where mid =?";
                String sql_count = "select count(*) from yx_evaluate where mid= ?";
                List<Map<String, Object>> listevaluate = null;
                try {
                    NumberFormat ns = NumberFormat.getNumberInstance();
                    ns.setMaximumFractionDigits(1);
                    Map<String, Object> map = new HashMap<>();
                    List<Integer> listmid = jdbcTemplate.queryForList(sql_mid.toString(), Integer.class, mid);
                    if (mid.equals(listmid.get(0))) {
                        Integer count = jdbcTemplate.queryForObject(sql_count.toString(), Integer.class, mid);
                        map.put("count", count);
                        String sql_sum = "select sum(praise) from yx_evaluate where mid=?";
                        Double sum = jdbcTemplate.queryForObject(sql_sum.toString(), Double.class, mid);
                        Double aa = sum / count;

                        Double average = Double.parseDouble(ns.format(aa));

                        map.put("average", average);
                        listevaluate = jdbcTemplate.query(sql.toString(), new RowMapper<Map<String, Object>>() {
                            @Override
                            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                                Map<String, Object> mp = new HashMap<String, Object>();
                                mp.put("mid", rs.getInt("id"));
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
                                    mp.put("page", JSONArray.fromObject(evaluate_photo).size());
                                } else {
                                    mp.put("evaluate_photo", null);
                                }
                                return mp;
                            }
                        }, mid);
                        map.put("evaluate", listevaluate);
                        mp.put("allevaluatel", map);
                    } else {
                        mp.put("allevaluatel", null);
                    }
                } catch (Exception e) {
                    mp.put("allevaluatel", null);
                }
                return mp;
            }
        }, mid);

        return list;
    }


    @Override
    public Map<String, Object> appointmentBusiness(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");

        String sql = "select id,phone,name from yx_user where power=4 ";
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        list = this.jdbcTemplate.query(sql.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("id", rs.getInt("id"));
                mp.put("name", rs.getString("name"));
                mp.put("phone", rs.getString("phone"));
                StringBuffer sb = new StringBuffer();
                sb.append("select count(distinct yx_order.did) from yx_order_demand left join yx_custom on (yx_order_demand.cid=yx_custom.id)");
                sb.append(" left join yx_order on (yx_order_demand.id=yx_order.did) where yx_order_demand.work_states=2 and yx_custom.bid=?  ");
                int achievement = jdbcTemplate.queryForObject(sb.toString(), Integer.class, rs.getInt("id"));
                mp.put("achievement", achievement);
                return mp;
            }
        });

        String key = "appointmentBusiness" + cid.toString();
        ValueOperations<String, List<Map<String, Object>>> operations = redisTemplate.opsForValue();
        boolean hasKey = redisTemplate.hasKey(key);

        Map<String, Object> map = new HashMap<>();
        Random rand = new Random();
        List<Map<String, Object>> redisList = new ArrayList<Map<String, Object>>();
        //缓存数据
        if (hasKey) {

            redisList = operations.get(key);
            if (redisList.size() < list.size()) {
                //缓存集合小于list
                for (int i = 0; i < redisList.size(); i++) {
                    for (int j = 0; j < list.size(); j++) {
                        if (redisList.get(i).get("id").toString().equals(list.get(j).get("id").toString())) {
                            list.remove(redisList.get(i));
                        }
                    }
                }
                Integer num = rand.nextInt(list.size());
                map = list.get(num);
                redisList.add(map);
                operations.set(key, redisList, 2, TimeUnit.HOURS);
            } else {
                //缓存等于list，清除redisList
                Integer num = rand.nextInt(list.size());
                Iterator<Map<String, Object>> iterator = redisList.iterator();
                while (iterator.hasNext()) {
                    Map<String, Object> map1 = iterator.next();
                    iterator.remove();
                }
                map = list.get(num);
                redisList.add(map);
                operations.set(key, redisList, 2, TimeUnit.HOURS);
            }
        } else {
            //第一次推荐业务员，没有缓存

            Integer num = rand.nextInt(list.size());
            map = list.get(num);
            redisList.add(map);
            operations.set(key, redisList, 2, TimeUnit.HOURS);
        }

        return map;
    }

    @Transactional
    @Override
    public int appointment(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        //Integer bid = jsonObject.getInteger("bid");
        Integer cid = jsonObject.getInteger("cid");
        Integer mid = jsonObject.getInteger("mid");
        String sql_order = "update yx_order set mid=?,type=?,states=? where cid=?";
        int states_order = this.jdbcTemplate.update(sql_order, mid, 0, 2, cid);

        String sql = "update yx_custom set appointment=? where id=?";
        int states = this.jdbcTemplate.update(sql, mid, cid);

        if (states > 0 && states_order > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @SneakyThrows
    @Transactional
    @Override
    public Map<String, Object> addInformation(String json) {

        JSONObject jsonObject = JSON.parseObject(json);
        Integer rid = jsonObject.getInteger("rid");
        Integer cid = jsonObject.getInteger("cid");
        Integer production_situation = jsonObject.getInteger("production_situation");
        String c_name = jsonObject.getString("c_name");
        String nodate = jsonObject.getString("nodate");
        String production_date = jsonObject.getString("production_date");
        String born = jsonObject.getString("born");

        String str1 = born.substring(5, 7);
        String str2 = born.substring(8, 10);
        Integer month = Integer.valueOf(str1);
        Integer day = Integer.valueOf(str2);
        String constellation = getConstellation(month, day);//星座


        String sql_count = "select count(*) from yx_order_demand where cid=?";
        Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class, cid);

        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String update_time = time1.format(new Date());

        String times = null;
        Integer order_type = 0;
        String sql_custom = null;
        if (production_situation == 0) {
            order_type = 0;
            times = nodate;
            sql_custom = "update yx_custom set sid=?, c_name=?,production_situation=0,nodate=?,born=?,constellation=? where id=? ";
        } else {
            order_type = 1;
            times = production_date;
            sql_custom = "update yx_custom set sid=?, c_name=?,production_situation=1,production_date=?,born=?,constellation=? where id=? ";
        }
        //173 客服彭静id
        int states_time = this.jdbcTemplate.update(sql_custom, 173,c_name, times, born, constellation, cid);
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowTime = time.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = sdf.parse(nowTime);
        Date start_ = sdf.parse(times);
        if (now.before(start_)) {
            //表示now小于start_
            production_situation = 0;
            nowTime = times;
        } else {
            production_situation = 1;
        }

        Map<String, Object> map = new HashMap<>();
        String endtime = getNewEndtime(nowTime, -26);

        int states_update = 0;
        Integer isAdd = 0;
        if (rid != 0) {
            //有推荐人id
            if (count > 0) {
                //有推荐人id添加过订单需求表

                //修改时间更新
                String sql_update = "update yx_custom set update_time=? where id=?";
                states_update = this.jdbcTemplate.update(sql_update, update_time, cid);
                String sql_demand = "update yx_order_demand set order_type=?,service_type=?,service_day=?,starttime=?,endtime=?,timetype=?,level=?,price=? where cid=?";
                int states_demand = this.jdbcTemplate.update(sql_demand, order_type, "居家服务", 26, nowTime, endtime, "长期", "9800", "9800", cid);
                isAdd = 1;
            } else {

                //修改时间更新
                String sql_update = "update yx_custom set update_time=? where id=?";
                states_update = this.jdbcTemplate.update(sql_update, update_time, cid);

                //没有添加订单需求表
                StringBuffer sb2 = new StringBuffer();
                sb2.append("insert into yx_order_demand(cid,order_type,service_type,service_day,starttime,endtime,timetype,level,price,threestates)");
                sb2.append("value(?,?,?,?,?,?,?,?,?,?)");
                //添加订单需求表
                int states_demand = this.jdbcTemplate.update(sb2.toString(), cid, order_type, "居家服务", 26, nowTime, endtime, "长期", "9800", "9800", 0);
                if (states_demand > 0) {
                    isAdd = 1;
                } else {
                    isAdd = 0;
                }
                String sql_did = "select max(id) from yx_order_demand";
                int did = this.jdbcTemplate.queryForObject(sql_did, Integer.class);
                //添加订单表
                StringBuffer sb3 = new StringBuffer();
                sb3.append("insert into yx_order (did,mid,cid,type,states)");
                sb3.append("value(?,?,?,?,?)");
                int states_order = this.jdbcTemplate.update(sb3.toString(), did, 0, cid, 0, 0);
                //修改推荐表，添加，用户注册并测一测的用户
                StringBuffer sb4 = new StringBuffer();
                String sql_re = "update yx_recommend set isTest=? where cid=?";
                int states_re = this.jdbcTemplate.update(sql_re, 1, cid);
                //添加积分

            }
        } else {
            //没有推荐人id
            if (count > 0) {
                //添加过

                //修改时间更新
                String sql_update = "update yx_custom set update_time=? where id=?";
                states_update = this.jdbcTemplate.update(sql_update, update_time, cid);

                //修改订单需求表
                String sql_demand = "update yx_order_demand set order_type=?,service_type=?,service_day=?,starttime=?,endtime=?,timetype=?,level=?,price=? where cid=?";
                int states_demand = this.jdbcTemplate.update(sql_demand, order_type, "居家服务", 26, times, endtime, "长期", "9800", "9800", cid);
                isAdd = 1;
            } else {
                //没有添加

                //修改时间更新
                String sql_update = "update yx_custom set update_time=? where id=?";
                states_update = this.jdbcTemplate.update(sql_update, update_time, cid);

                //没有添加订单需求表
                StringBuffer sb2 = new StringBuffer();
                sb2.append("insert into yx_order_demand(cid,order_type,service_type,service_day,starttime,endtime,timetype,level,price,threestates)");
                sb2.append("value(?,?,?,?,?,?,?,?,?,?)");
                //添加订单需求表
                int states_demand = this.jdbcTemplate.update(sb2.toString(), cid, order_type, "居家服务", 26, times, endtime, "长期", "9800", "9800", 0);
                if (states_demand > 0) {
                    isAdd = 1;
                } else {
                    isAdd = 0;
                }
                String sql_did = "select max(id) from yx_order_demand";
                int did = this.jdbcTemplate.queryForObject(sql_did, Integer.class);
                //添加订单表
                StringBuffer sb3 = new StringBuffer();
                sb3.append("insert into yx_order (did,mid,cid,type,states)");
                sb3.append("value(?,?,?,?,?)");
                int states_order = this.jdbcTemplate.update(sb3.toString(), did, 0, cid, 0, 0);
            }
        }


        map.put("isAdd", isAdd);
        if (states_update > 0) {
            map.put("start", times);
            map.put("constellation", constellation);
            return map;
        } else {
            return map;
        }
    }

    @Override
    public int deleteBrowse(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");
        String sql = "update yx_collect_browse set isBrowse=? where cid=?";
        int states = this.jdbcTemplate.update(sql, 0, cid);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public List<Map<String, Object>> myOrder(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");
        List list = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select yx_custom.bid,yx_order_demand.starttime,yx_order_demand.endtime,yx_order_demand.service_day,yx_order.type,");
        sb.append("yx_order.states,yx_order.order_states,yx_order.id,yx_order.did,yx_order_demand.threematorn,yx_order.mid,yx_order_demand.work_states");
        sb.append(" from yx_order_demand");
        sb.append(" LEFT JOIN  yx_order on (yx_order_demand.id=yx_order.did) LEFT JOIN yx_custom on (yx_order_demand.cid=yx_custom.id)");
        sb.append("where yx_custom.id=? and yx_custom.bid<>0");

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                String starttime = getMonth(rs.getString("starttime"));
                String endtime = getMonth(rs.getString("endtime"));
                mp.put("starttime", starttime);
                mp.put("endtime", endtime);
                mp.put("service_day", rs.getInt("service_day"));
                Integer oid = rs.getInt("id");
                Integer did = rs.getInt("did");
                String sql = "select  phone,name from yx_user where id=?";
                List<Map<String, Object>> bList = jdbcTemplate.queryForList(sql, rs.getInt("bid"));
                mp.put("banme", bList.get(0).get("name").toString());
                mp.put("bid", rs.getInt("bid"));
                mp.put("phone", bList.get(0).get("phone").toString());
                List<Map<String, Object>> matornList = new ArrayList<>();
                mp.put("states", rs.getInt("states"));
                if (rs.getInt("type") == 0) {
                    //订单状态0不在订单1在订单
                    if (rs.getInt("states") == 0) {
                        //月嫂状态 0推荐
                        mp.put("matornList", matornList);
                    }
                    if (rs.getInt("states") == 1) {
                        //月嫂状态 1面试中
                        String threematorn = rs.getString("threematorn");
                        if (threematorn != null) {
                            JSONArray jsonArray = JSONArray.fromObject(threematorn);
                            List<Integer> listmatorn = (List) jsonArray;
                            List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                            StringBuffer sb1 = new StringBuffer();
                            sb1.append("select yx_matorn.id,yx_matorn.name,yx_matorn.born,yx_matorn.zodiac,yx_matorn.constellation,yx_matorn.household,");
                            sb1.append("yx_bussiness.photo from yx_matorn left join yx_bussiness on (yx_matorn.id=yx_bussiness.mid)");
                            sb1.append("where yx_matorn.id=?");
                            for (int i = 0; i < listmatorn.size(); i++) {

                                Integer mid = listmatorn.get(i);
                                list1 = jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
                                    @SneakyThrows
                                    @Override
                                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                                        Map<String, Object> mp = new HashMap<String, Object>();

                                        String household = rs.getString("household");
                                        String city = "";
                                        if (household.length() > 3) {
                                            String h1 = household.substring(0, 2);
                                            String h2 = household.substring(3, 5);
                                            city = h1 + h2;
                                        } else {
                                            city = household.substring(0, 2);
                                        }
                                        mp.put("mid", rs.getInt("id"));
                                        mp.put("city", city);
                                        String birthTime = rs.getString("born");
                                        Long age = getAge(birthTime);
                                        mp.put("age", age);
                                        mp.put("zodiac", rs.getString("zodiac"));
                                        mp.put("constellation", rs.getString("constellation"));
                                        mp.put("photo", rs.getString("photo"));
                                        mp.put("name", rs.getString("name"));
                                        String sql_count = "select count(*) from yx_score where mid=? ";
                                        Integer count = jdbcTemplate.queryForObject(sql_count, Integer.class, mid);
                                        String comment = null;
                                        if (count > 0) {
                                            String sql_comment = "select comment from yx_score where mid=?";
                                            comment = jdbcTemplate.queryForObject(sql_comment, String.class, mid);
                                            mp.put("comment", comment);
                                        } else {
                                            mp.put("comment", comment);
                                        }

                                        return mp;
                                    }
                                }, listmatorn.get(i));
                                matornList.add(list1.get(0));
                            }
                            mp.put("matornList", matornList);
                        } else {
                            mp.put("matornList", matornList);
                        }
                    }

                    if (rs.getInt("states") == 2) {
                        //月嫂状态 2选中
                        Integer flag = 0;
                        String threematorn = rs.getString("threematorn");
                        if (threematorn != null) {
                            flag = 1;//有推荐月嫂的状态
                        } else {
                            flag = 0;//没有推荐状态，直接占卜选择月嫂
                        }
                        Integer isThreeMatorn = flag;
                        StringBuffer sb2 = new StringBuffer();
                        sb2.append("select yx_matorn.id,yx_matorn.name,yx_matorn.born,yx_matorn.zodiac,yx_matorn.constellation,yx_matorn.household,");
                        sb2.append("yx_bussiness.photo from yx_matorn left join yx_bussiness on (yx_matorn.id=yx_bussiness.mid)");
                        sb2.append("where yx_matorn.id=?");
                        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                        Integer mid = rs.getInt("mid");
                        list1 = jdbcTemplate.query(sb2.toString(), new RowMapper<Map<String, Object>>() {
                            @SneakyThrows
                            @Override
                            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                                Map<String, Object> mp = new HashMap<String, Object>();

                                String household = rs.getString("household");
                                String city = "";
                                if (household.length() > 3) {
                                    String h1 = household.substring(0, 2);
                                    String h2 = household.substring(3, 5);
                                    city = h1 + h2;
                                } else {
                                    city = household.substring(0, 2);
                                }
                                mp.put("mid", rs.getInt("id"));
                                mp.put("city", city);
                                String birthTime = rs.getString("born");
                                Long age = getAge(birthTime);
                                mp.put("age", age);
                                mp.put("zodiac", rs.getString("zodiac"));
                                mp.put("constellation", rs.getString("constellation"));
                                mp.put("photo", rs.getString("photo"));
                                mp.put("name", rs.getString("name"));
                                mp.put("oid", oid);
                                mp.put("did", did);
                                mp.put("isThreeMatorn", isThreeMatorn);

                                String sql_count = "select count(*) from yx_score where mid=? ";
                                Integer count = jdbcTemplate.queryForObject(sql_count, Integer.class, mid);
                                String comment = null;
                                if (count > 0) {
                                    String sql_comment = "select comment from yx_score where mid=?";
                                    comment = jdbcTemplate.queryForObject(sql_comment, String.class, mid);
                                    mp.put("comment", comment);
                                } else {
                                    mp.put("comment", comment);
                                }
                                return mp;
                            }
                        }, mid);
                        matornList.add(list1.get(0));
                        mp.put("matornList", matornList);
                    }
                } else {

                    //订单状态1在订单
                    StringBuffer sb3 = new StringBuffer();
                    sb3.append("select MAX(yx_order.id) as oid, yx_custom.bid,yx_custom.c_name,yx_custom.origin,yx_custom.channel,yx_custom.c_phone,yx_order_demand.starttime,yx_order_demand.endtime,yx_order.id,yx_order_demand.work_states,");
                    sb3.append("yx_order_demand.timetype,yx_order.did,yx_custom.bid,yx_order.order_states,yx_order.return_time,");
                    sb3.append("yx_order_demand.service_type,yx_order_demand.service_day,yx_order_demand.level,yx_order_demand.price,yx_order_demand.threematorn,yx_order.mid,yx_order.states");
                    sb3.append("  from yx_order_demand LEFT JOIN yx_custom ON (yx_order_demand.cid=yx_custom.id) LEFT JOIN yx_order ON (yx_order_demand.id=yx_order.did) ");
                    sb3.append(" where yx_order.cid=?");
                    sb3.append(" GROUP BY yx_order.did  order by  yx_order.id desc");
                    matornList = jdbcTemplate.query(sb3.toString(), new RowMapper<Map<String, Object>>() {
                        @Override
                        public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                            Map<String, Object> mp = new HashMap<String, Object>();
                            mp.put("c_name", rs.getString("c_name"));
                            mp.put("oid", rs.getString("oid"));
                            mp.put("c_phone", rs.getString("c_phone"));
                            mp.put("did", rs.getString("did"));
                            mp.put("origin", rs.getString("origin"));
                            mp.put("channel", rs.getString("channel"));
                            mp.put("service_type", rs.getString("service_type"));
                            mp.put("service_day", rs.getString("service_day"));
                            mp.put("level", rs.getString("level"));
                            mp.put("price", rs.getString("price"));
                            mp.put("work_states", rs.getString("work_states"));
                            mp.put("timetype", rs.getString("timetype"));
                            // mp.put("order_states", rs.getString("order_states"));
                            mp.put("mid", rs.getString("mid"));


                            String sql_mid = "select mid from yx_order where did=?";


                            List<Integer> midlist = (List<Integer>) jdbcTemplate.queryForList(sql_mid, Integer.class, rs.getString("did"));

                            Integer mid = 0;
                            if (midlist.size() == 1) {
                                mid = midlist.get(0);
                            } else {
                                mid = midlist.get(midlist.size() - 1);

                            }
                            if (mid != 0) {
                                StringBuffer sb1 = new StringBuffer();
                                sb1.append("select yx_matorn.name,yx_bussiness.isorder,yx_bussiness.photo,yx_order.service_states,yx_order.order_states from yx_matorn LEFT JOIN yx_bussiness on (yx_matorn.id=yx_bussiness.mid)");
                                sb1.append(" left join yx_order on(yx_matorn.id=yx_order.mid)");
                                sb1.append(" where yx_order.id in (select Max(yx_order.id) from yx_order where yx_matorn.id=? group by yx_order.mid) ");
                                List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                                list1 = jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
                                    @Override
                                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                                        Map<String, Object> mp = new HashMap<String, Object>();
                                        mp.put("name", rs.getString("name"));
                                        mp.put("photo", rs.getString("photo"));
                                        mp.put("isorder", rs.getInt("isorder"));
                                        mp.put("service_states", rs.getInt("service_states"));
                                        mp.put("order_states", rs.getInt("order_states"));
                                        return mp;
                                    }
                                }, mid);

                                mp.put("isorder", Integer.valueOf(list1.get(0).get("isorder").toString()));
                                mp.put("name", list1.get(0).get("name").toString());
                                mp.put("service_states", list1.get(0).get("service_states").toString());
                                mp.put("order_states", list1.get(0).get("order_states"));
                                if (list1.get(0).get("photo") != "" && list1.get(0).get("photo") != null) {
                                    mp.put("photo", list1.get(0).get("photo").toString());
                                } else {
                                    mp.put("photo", null);
                                }
                            } else {

                                mp.put("name", "未知");
                            }

                            if (rs.getString("return_time") != null) {
                                mp.put("return_time", rs.getString("return_time"));
                            }

                            try {
                                String starttime = getMonth(rs.getString("starttime"));
                                String endtime = getMonth(rs.getString("endtime"));
                                mp.put("starttime", starttime);
                                mp.put("endtime", endtime);
                                String startweek = getWeek(rs.getString("starttime"));
                                String endweek = getWeek(rs.getString("endtime"));
                                mp.put("startweek", startweek);
                                mp.put("endweek", endweek);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            return mp;
                        }
                    }, cid);

                    mp.put("matornList", matornList);
//                    if (rs.getInt("work_states") == 0 && rs.getInt("order_states") == 0) {
//                        //0待服务未派岗
//
//
//                    }
//                    if (rs.getInt("work_states") == 0 && rs.getInt("order_states") == 1) {
//                        //1待服务已派岗
//
//                    }
//                    if (rs.getInt("order_states") == 2) {
//                        //2服务中
//
//                    }
//                    if (rs.getInt("order_states") == 2) {
//                        //3服务结束
//                    }

                }
                return mp;
            }
        }, cid);

        return list;

    }

    @Override
    public int newChoiceMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer oid = jsonObject.getInteger("oid");

        String sql_o = "update yx_order set states=? where id=?";
        int states_o = this.jdbcTemplate.update(sql_o, 1, oid);
        if (states_o > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int addAddress(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String starttime = jsonObject.getString("starttime");
        Integer service_day = jsonObject.getInteger("service_day");
        String o_phone = jsonObject.getString("o_phone");
        String region = jsonObject.getString("region");
        String o_address = jsonObject.getString("o_address");
        Integer oid = jsonObject.getInteger("oid");
        Integer did = jsonObject.getInteger("did");

        String endtime = getNewEndtime(starttime, -service_day);

        String sql_demand = "update yx_order_demand set service_day=?,starttime=?,endtime=?,work_states=? where id=?";
        int states_demand = this.jdbcTemplate.update(sql_demand, service_day, starttime, endtime, 0, did);

        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creattime = time1.format(new Date());

        //查询当天有多少订单
        String sql_count = "select count(*) from yx_order where to_days(creattime) = to_days(now());";
        Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class);
        String servie_type = "居家服务";
        String timetype = "长期";
        String number = getOrderNumber(servie_type, timetype, count);
        String sql = "update yx_order set o_phone=?,region=?,o_address=?,type=?,o_number=?,creattime=?,order_states=?  where id=?";
        int states_order = this.jdbcTemplate.update(sql, o_phone, region, o_address, 1, number, creattime, 0, oid);

        if (states_demand > 0 && states_order > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int newRecommend(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");

        String sql_d = "update yx_order_demand set threematorn=? where cid=?";
        int states_d = this.jdbcTemplate.update(sql_d, null, cid);

        String sql_o = "update yx_order set states=? where cid=?";
        int states_o = this.jdbcTemplate.update(sql_o, 0, cid);

        if (states_d > 0 && states_o > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public List<Map<String, Object>> myRecommend(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");
        List<Map<String, Object>> list = new ArrayList<>();

        //客户相关的所有推荐人，一级推荐人，二级推荐人，三级推荐人
        List<Map<String, Object>> allList = new ArrayList<>();

        //客户作为一级推荐人推荐注册的客户
        String sql_r = "select cid from yx_recommend where isLogin=1 and rid=?";
        List<Map<String, Object>> r_list = this.jdbcTemplate.queryForList(sql_r, cid);
        for (int i = 0; i < r_list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("cid", r_list.get(i).get("cid"));
            map.put("level", 1);//一级推荐
            allList.add(map);
        }
        //客户作为二级推荐人推荐注册的客户
        String sql_f = "select cid from yx_recommend where isLogin=1 and fid=?";
        List<Map<String, Object>> f_list = this.jdbcTemplate.queryForList(sql_f, cid);

        for (int i = 0; i < f_list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("cid", f_list.get(i).get("cid"));
            map.put("level", 2);//二级级推荐
            allList.add(map);
        }
        //客户作为三级推荐人推荐注册的客户
        String sql_g = "select cid from yx_recommend where isLogin=1 and gid=?";
        List<Map<String, Object>> g_list = this.jdbcTemplate.queryForList(sql_g, cid);
        for (int i = 0; i < g_list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("cid", g_list.get(i).get("cid"));
            map.put("level", 3);//三级级推荐
            allList.add(map);
        }

        StringBuffer sb = new StringBuffer();
        sb.append("select c_name,c_photo,isPartner from yx_custom where id=?");
        List<Map<String, Object>> list1 = new ArrayList<>();
        for (int i = 0; i < allList.size(); i++) {
            Integer level = Integer.valueOf(allList.get(i).get("level").toString());
            Integer id = Integer.valueOf(allList.get(i).get("cid").toString());
            list1 = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("c_name", rs.getString("c_name"));
                    mp.put("c_photo", rs.getString("c_photo"));
                    mp.put("level", level);
                    Integer isPartner = rs.getInt("isPartner");
                    Integer integral = 0;
                    if (level == 1 && isPartner == 1) {
                        integral = 1;
                    }
                    if (level == 2 && isPartner == 1) {
                        integral = 2;
                    }
                    if (level == 3 && isPartner == 1) {
                        integral = 2;
                    }
                    if (isPartner == 0) {
                        integral = 0;
                    }
                    mp.put("integral", integral);

                    return mp;

                }
            }, id);
            System.out.println("________" + list1);
            list.add(list1.get(0));
        }
        return list;
    }


    @Override
    public Map<String, Object> myRank(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");
        Map<String, Object> map = new HashMap<>();
        String sql_count = "select count(*) from yx_recommend where isLogin=1 and rid=? ";
        Integer recommend = this.jdbcTemplate.queryForObject(sql_count, Integer.class, cid);
        //客户直接推荐人数
        map.put("recommend", recommend);

        String sql_integral = "select integral from yx_custom where id=? ";
        Integer integral = this.jdbcTemplate.queryForObject(sql_integral, Integer.class, cid);
        map.put("integral", integral);

        //一级推荐人成单数
        String sql_r = "select count(*) from yx_recommend where isOrder=1 and rid=?";
        int order_r = this.jdbcTemplate.queryForObject(sql_r, Integer.class, cid);
        //二级推荐人成单数
        String sql_f = "select count(*) from yx_recommend where isOrder=1 and fid=?";
        int order_f = this.jdbcTemplate.queryForObject(sql_f, Integer.class, cid);
        //三级推荐人成单数
        String sql_g = "select count(*) from yx_recommend where isOrder=1 and gid=?";
        int order_g = this.jdbcTemplate.queryForObject(sql_g, Integer.class, cid);

        //与客户相关所有完成订单数
        Integer order = order_r + order_f + order_g;
        map.put("order", order);
        Integer rank = 0;
        if (integral != 0) {
            //客户有积分
            String sql_rank = "select * from (SELECT c.id,(@rank:=@rank+1) as rank FROM yx_custom c,(select (@rank:=0)) b ORDER BY c.integral desc) t where t.id=?";
            List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql_rank, cid);
            //客户积分排名
            rank = Double.valueOf(list.get(0).get("rank").toString()).intValue();

        } else {
            //客户没有积分
            StringBuffer sb = new StringBuffer();
            sb.append("select * from (");
            sb.append("select c.id,c.count,(@rank:=@rank+1) as rank FROM ");
            sb.append("(SELECT COUNT(*) AS count,yx_custom.id FROM yx_custom LEFT JOIN yx_recommend on (yx_custom.id=yx_recommend.rid) WHERE yx_recommend.isLogin=1 and integral=0  GROUP BY yx_custom.id)");
            sb.append("c,(select (@rank:=0)) b ORDER BY c.count desc");
            sb.append(") t where t.id=?");
            List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sb.toString(), cid);
            //客户推荐人数排名
            Integer rank_recommend = 0;
            if (list.size() > 0) {
                //客户有推荐其他客户
                rank_recommend = Double.valueOf(list.get(0).get("rank").toString()).intValue();
            } else {
                //客户没有推荐其他客户
                rank_recommend = 100;
            }
            //有积分的客户的数量
            String sql_count_integral = "select count(*) from yx_custom where integral<>0";
            Integer count_integral = this.jdbcTemplate.queryForObject(sql_count_integral, Integer.class);

            System.out.println("_____________" + count_integral);
            //先有积分客户排名，在根据客户推荐人数排名
            rank = count_integral + rank_recommend;

        }
        map.put("rank", rank);
        return map;
    }

}