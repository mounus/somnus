package com.example.dao.impl;


import com.example.dao.CustomDao;
import com.example.entiy.Custom;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.tencentcloudapi.ocr.v20181119.models.FormulaOCRRequest;
import lombok.SneakyThrows;

import net.sf.json.JSONArray;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.daysBetween;
import static com.example.util.MonthUtil.getMonthMap;
import static com.example.util.NumberUtil.getMonth;
import static com.example.util.NumberUtil.getWeek;

import static com.example.util.PageUtil.*;
import static com.example.util.PeriodUtil.*;
import static com.example.util.SearchUtil.searchPeriod;
import static com.example.util.SearchUtil.searchPrice;
import static com.example.util.Year.getAge;

@Repository
public class CustomDaoImpl implements CustomDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;


    public int getForId() {
        String sql = "select max(id) from yx_custom ";
        int id = this.jdbcTemplate.queryForObject(sql, Integer.class);
        return id;
    }

    @Override
    public int save(Custom custom) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_custom (bid,sid,origin,channel,c_name,c_phone,production_situation,nodate,production_date,production_mode,baby_sex,creat_time,update_time,integral,isPartner,isIntention)");
        sb.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creat_time = time.format(new Date());

        int states = this.jdbcTemplate.update(sb.toString(), custom.getBid(), custom.getSid(), custom.getOrigin(), custom.getChannel(), custom.getC_name(), custom.getC_phone(), custom.getProduction_situation(), custom.getNodate(), custom.getProduction_date(), custom.getProduction_mode(), custom.getBaby_sex(), creat_time, creat_time, 0, 0, 1);
        int cid = getForId();
        if (cid > 0 && states > 0) {
            return cid;
        } else {
            return 0;
        }

    }

    @Override
    public int update(Custom custom) {
        StringBuffer sb = new StringBuffer();
        sb.append("update yx_custom set origin=?,channel=?,c_name=?,c_phone=?,production_situation=?,nodate=?,production_date=?,production_mode=?,baby_sex=? where id=?");

        int states = this.jdbcTemplate.update(sb.toString(), custom.getOrigin(), custom.getChannel(), custom.getC_name(), custom.getC_phone(), custom.getProduction_situation(), custom.getNodate(), custom.getProduction_date(), custom.getProduction_mode(), custom.getBaby_sex(), custom.getId());
        if (states > 0) {
            return custom.getId();
        } else {
            return 0;
        }

    }


    @Override
    public Map<String, Object> channel() {
        Map<String, Object> map = new HashMap<String, Object>();
        String sql = "select name from yx_channel where type=1";
        List<String> OnlineList = (List) this.jdbcTemplate.queryForList(sql.toString());
        map.put("OnlineList", OnlineList);
        String sql1 = "select channel from yx_channel where type=2";
        List<Map<String, Object>> channel = new ArrayList<Map<String, Object>>();
        channel = (List) this.jdbcTemplate.queryForList(sql1.toString());

        List<Integer> namelist = new ArrayList<Integer>();
        for (int i = 0; i < channel.size(); i++) {
            namelist.add(Integer.valueOf(channel.get(i).get("channel").toString()));
        }

        String sql_channel = "select  name from yx_channel where channel=?";
        List<Map<String, Object>> channellist = new ArrayList<Map<String, Object>>();
        List<List> UnderlineList = new ArrayList<List>();

        for (int i = 0; i < namelist.size(); i++) {
            channellist = (List) this.jdbcTemplate.queryForList(sql_channel, namelist.get(i));
            UnderlineList.add(channellist);
        }
        map.put("UnderlineList", UnderlineList);
        return map;
    }


    @Transactional
    @Override
    public int chooseMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        Integer oid = jsonObject.getInteger("oid");

        String sql_cid = "select cid from yx_order where id=?";
        Integer cid = this.jdbcTemplate.queryForObject(sql_cid, Integer.class, oid);

        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time.format(new Date());

        //修改时间更新
        String sql_update = "update yx_custom set update_time=? where id=?";
        int states_update = this.jdbcTemplate.update(sql_update, nowTime, cid);

        String sql = "update yx_order set mid=?,states=2 where id=?";
        int states = this.jdbcTemplate.update(sql, mid, oid);

        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb.append("value(?,?,?,?,?)");
        //type=2 面试成功，等待填写派岗地址
        int states_messgage = this.jdbcTemplate.update(sb.toString(), cid, bid, mid, 2, nowTime);
            StringBuffer sb_saleOrder = new StringBuffer();
        sb_saleOrder.append("insert into yx_saleOrder (cid,bid,saleType,confirmIntention,information,isRecommend,age,zodiac,educational,household,recommendNotes,isInterview,interviewType,interviewTime,creat_time)");
        sb_saleOrder.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

        String sql_saleOrder="select * from yx_saleOrder where id=(select max(id) from sql_saleOrder where cid=?)";
        Map<String,Object> saleOrderMap=jdbcTemplate.queryForMap(sql_saleOrder,cid);
        String confirmIntention="签单";

            int states1 = this.jdbcTemplate.update(sb_saleOrder.toString(), cid,bid, saleOrderMap.get("saleType"), confirmIntention, saleOrderMap.get("confirmIntention"),saleOrderMap.get("isRecommend"), saleOrderMap.get("age"),
                    saleOrderMap.get("zodiac"), saleOrderMap.get("educational"), saleOrderMap.get("household"), saleOrderMap.get("recommendNotes"), saleOrderMap.get("isInterview"), saleOrderMap.get("interviewType"),saleOrderMap.get("interviewTime") , nowTime);

        if (states_update > 0 && states > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int updateStates(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        Integer oid = jsonObject.getInteger("oid");
        String sql = "update yx_order set mid=?,states=1 where id=?";
        return this.jdbcTemplate.update(sql, 0, oid);
    }

    @Override
    public List<Map<String, Object>> getOneMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer mid = jsonObject.getInteger("mid");
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
    public Map<String, Object> getStatistics(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer bid = jsonObject.getInteger("bid");
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list_c = new ArrayList<Map<String, Object>>();
        StringBuffer sb = new StringBuffer(); //客户
        StringBuffer sb1 = new StringBuffer();//订单
        StringBuffer sb2 = new StringBuffer();//业绩

        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, bid);

        if (power == 20 || power == 100) {
            String sql_user = "select name,post from yx_user where id=?";
            list = this.jdbcTemplate.query(sql_user.toString(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("name", rs.getString("name"));
                    mp.put("post", rs.getString("post"));
                    return mp;
                }
            }, bid);
            map.put("name", list.get(0).get("name"));
            map.put("post", list.get(0).get("post"));
            //客户数量
            sb.append("SELECT count(*) FROM yx_order_demand left join yx_custom on (yx_order_demand.cid=yx_custom.id) ");
            int custom = this.jdbcTemplate.queryForObject(sb.toString(), Integer.class);
            map.put("custom", custom);
            //订单数量
            sb1.append("select count(distinct yx_order.did) from yx_order where yx_order.type=1");
            int order = this.jdbcTemplate.queryForObject(sb1.toString(), Integer.class);
            map.put("order", order);
            //业绩数量
            sb2.append("select count(distinct yx_order.did) from yx_order_demand left join yx_custom on (yx_order_demand.cid=yx_custom.id)");
            sb2.append(" left join yx_order on (yx_order_demand.id=yx_order.did) where yx_order_demand.work_states=2");
            int achievement = this.jdbcTemplate.queryForObject(sb2.toString(), Integer.class);
            map.put("achievement", achievement);

        } else {
            String sql_user = "select name,post from yx_user where id=?";
            list = this.jdbcTemplate.query(sql_user.toString(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("name", rs.getString("name"));
                    mp.put("post", rs.getString("post"));
                    return mp;
                }
            }, bid);
            map.put("name", list.get(0).get("name"));
            map.put("post", list.get(0).get("post"));

            //客户数量
            sb.append("SELECT count(*) FROM yx_order_demand left join yx_custom on (yx_order_demand.cid=yx_custom.id) ");
            sb.append("where yx_custom.bid=?");
            int custom = this.jdbcTemplate.queryForObject(sb.toString(), Integer.class, bid);
            map.put("custom", custom);

            sb1.append("select count(distinct yx_order.did) from yx_order_demand left join yx_custom on (yx_order_demand.cid=yx_custom.id)");
            sb1.append(" left join yx_order on (yx_order_demand.id=yx_order.did) where yx_order.type=1 and yx_custom.bid=?");
            int order = this.jdbcTemplate.queryForObject(sb1.toString(), Integer.class, bid);
            map.put("order", order);

            sb2.append("select count(distinct yx_order.did) from yx_order_demand left join yx_custom on (yx_order_demand.cid=yx_custom.id)");
            sb2.append(" left join yx_order on (yx_order_demand.id=yx_order.did) where yx_order_demand.work_states=2 and yx_custom.bid=?");
            int achievement = this.jdbcTemplate.queryForObject(sb2.toString(), Integer.class, bid);
            map.put("achievement", achievement);


        }
        return map;
    }

    @SneakyThrows
    @Override
    public List<Map<String, Object>> postList(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        Integer type = jsonObject.getInteger("type");
        Integer start = jsonObject.getInteger("start");
        String cname = jsonObject.getString("cname");
        List list = null;
        StringBuffer sb = new StringBuffer();
        List<Object> queryList = new ArrayList<Object>();
        StringBuffer sb_name = new StringBuffer();


        if (type == 0 || type == 2) {
            if (cname == "" || cname == null || cname.isEmpty()) {
                sb_name.append("");
            } else {
                String sql2 = "select c_name from yx_custom where c_name = ? ";
                try {
                    List<String> list3 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{cname}, String.class);
                    if (cname.equals(list3.get(0))) {
                        sb_name.append("    and  yx_custom.c_name=?  ");
                        queryList.add(list3.get(0));
                    } else {

                    }
                } catch (Exception e) {
                    if (cname.length() == 1) {
                        String xing = cname.substring(0, 1);
                        sb_name.append("  and  yx_custom.c_name like ?  ");
                        queryList.add(xing + "%");
                    } else {

                    }
                }
            }
        } else {
            if (cname == "" || cname == null || cname.isEmpty()) {

                sb_name.append("");
            } else {

                String sql2 = "select name from yx_matorn where name = ? ";
                try {
                    List<String> list3 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{cname}, String.class);

                    if (cname.equals(list3.get(0))) {
                        sb_name.append("    and  yx_matorn.name=?  ");
                        queryList.add(list3.get(0));
                    } else {

                    }
                } catch (Exception e) {
                    if (cname.length() == 1) {
                        String xing = cname.substring(0, 1);
                        sb_name.append("  and  yx_matorn.name like ?  ");
                        queryList.add(xing + "%");
                    } else {

                    }
                }
            }


        }
        if (type == 0) {
            sb.append("select MAX(yx_order.id) as oid,yx_order_demand.cid, yx_custom.bid,yx_custom.c_name,yx_custom.origin,yx_custom.channel,yx_custom.c_phone,yx_order_demand.starttime,yx_order_demand.endtime,yx_order.id,yx_order.o_name,yx_order_demand.timetype,");
            sb.append("yx_order_demand.service_type,yx_order.did,yx_order_demand.service_day,yx_order_demand.level,yx_order_demand.price,yx_order_demand.threematorn,yx_order_demand.remarks,yx_order.mid,yx_order.states from yx_order_demand");
            sb.append(" LEFT JOIN  yx_order on (yx_order_demand.id=yx_order.did) LEFT JOIN yx_custom on (yx_order_demand.cid=yx_custom.id)");
            //有销售工单才显示
            sb.append(" left join yx_saleOrder  on (yx_order_demand.cid=yx_saleOrder.cid) ");
            sb.append(" where yx_saleOrder.isRecommend=1  and  yx_order.type =0  and yx_order.states=0 and yx_order_demand.threestates=0  ");
            sb.append(sb_name);
            sb.append("GROUP BY yx_order.did order by yx_custom.update_time desc");

            Map<String, Object> page = new HashMap<>();
            page = getPage(start);
            String sql_page = page.get("sql_page").toString();
            sb.append(sql_page);
            try {
                list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();

                        mp.put("c_name", rs.getString("c_name"));
                        mp.put("c_phone", rs.getString("c_phone"));
                        mp.put("oid", rs.getString("id"));
                        mp.put("o_name", rs.getString("o_name"));
                        mp.put("did", rs.getString("did"));
                        mp.put("origin", rs.getString("origin"));
                        mp.put("channel", rs.getString("channel"));
                        mp.put("service_type", rs.getString("service_type"));
                        mp.put("service_day", rs.getString("service_day"));
                        mp.put("level", rs.getString("level"));
                        mp.put("price", rs.getString("price"));
                        mp.put("states", rs.getString("states"));
                        mp.put("timetype", rs.getString("timetype"));
                        mp.put("start", rs.getString("starttime"));
                        //     mp.put("remarks", rs.getString("remarks"));

                        String sql_recommendNotes = "select recommendNotes from yx_saleOrder where id=(select max(id) from yx_saleOrder where cid=? and isRecommend=1  )";
                        String recommendNotes = jdbcTemplate.queryForObject(sql_recommendNotes, String.class, rs.getInt("cid"));
                        mp.put("remarks", recommendNotes);
                        //mp.put("threematorn", rs.getInt("threematorn"));list 集合

                        String starttime = "";
                        String endtime = "";
                        String startweek = "";
                        String endweek = "";
                        try {
                            starttime = getMonth(rs.getString("starttime"));
                            endtime = getMonth(rs.getString("endtime"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            startweek = getWeek(rs.getString("starttime"));
                            endweek = getWeek(rs.getString("endtime"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mp.put("endtime", endtime);
                        mp.put("starttime", starttime);
                        mp.put("startweek", startweek);
                        mp.put("endweek", endweek);
                        return mp;
                    }
                }, queryList.toArray());

            } catch (Exception e) {

            }

        }
        if (type == 1) {
            sb.append("select MAX(yx_order.id) as oid, yx_custom.bid,yx_order_demand.starttime,yx_order_demand.service_day,yx_order.o_number,");
            sb.append("yx_order.region,yx_order.o_address,yx_order.mid,yx_order.o_name,yx_matorn.name,yx_contact.phone,yx_bussiness.photo,yx_bussiness.grade,yx_bussiness.isorder");
            sb.append("  from yx_order_demand LEFT JOIN yx_custom ON (yx_order_demand.cid=yx_custom.id) LEFT JOIN yx_order ON (yx_order_demand.id=yx_order.did) ");
            sb.append(" left join yx_matorn on (yx_order.mid=yx_matorn.id) left join yx_bussiness on (yx_order.mid=yx_bussiness.mid)");
            sb.append(" left join yx_contact on (yx_order.mid=yx_contact.mid)");
            //有咨询工单才显示
            sb.append(" left join yx_saleOrder  on (yx_order_demand.cid=yx_saleOrder.cid)");
            sb.append(" where  yx_saleOrder.isRecommend=1 and  yx_order.type=1  and yx_order.states=2  and yx_order.order_states=0  ");
            sb.append(sb_name);
            sb.append("GROUP BY yx_order.did order by yx_custom.update_time desc");

            Map<String, Object> page = new HashMap<>();
            page = getPage(start);
            String sql_page = page.get("sql_page").toString();
            sb.append(sql_page);
            list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("service_day", rs.getString("service_day"));
                    mp.put("o_number", rs.getString("o_number"));
                    mp.put("region", rs.getString("region"));
                    mp.put("o_address", rs.getString("o_address"));
                    mp.put("oid", rs.getString("oid"));
                    mp.put("o_name", rs.getString("o_name"));

                    Integer bid = rs.getInt("bid");
                    String stattime = rs.getString("starttime").replace("-", ".");
                    mp.put("starttime", stattime);

                    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                    String nowtime = time.format(new Date());

                    try {
                        int day = daysBetween(nowtime, rs.getString("starttime"));
                        String startweek = getWeek(rs.getString("starttime"));
                        mp.put("startweek", startweek);
                        mp.put("day", day);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    mp.put("name", rs.getString("name"));
                    mp.put("phone", rs.getString("phone"));
                    mp.put("photo", rs.getString("photo"));
                    mp.put("grade", rs.getString("grade"));
                    mp.put("isorder", rs.getInt("isorder"));
                    String sql_name = "select name from yx_user where id=?";
                    String bname = jdbcTemplate.queryForObject(sql_name, String.class, bid);
                    mp.put("bname", bname);
                    return mp;
                }
            }, queryList.toArray());

        }
        if (type == 2) {
            sb.append("select MAX(yx_order.id) as oid,yx_order_demand.cid, yx_custom.bid,yx_custom.c_name,yx_custom.origin,yx_custom.channel,yx_custom.c_phone,yx_order_demand.starttime,yx_order_demand.endtime,yx_order.id,yx_order.o_name,yx_order_demand.timetype,");
            sb.append("yx_order_demand.service_type,yx_order_demand.post_states,yx_order.did,yx_order_demand.service_day,yx_order_demand.level,yx_order_demand.price,yx_order_demand.threematorn,yx_order_demand.remarks,yx_order.mid,yx_order.states from yx_order_demand");
            sb.append(" LEFT JOIN  yx_order on (yx_order_demand.id=yx_order.did) left JOIN yx_custom on (yx_order_demand.cid=yx_custom.id)");
            //有销售工单才显示
            sb.append(" left join yx_saleOrder  on (yx_order_demand.cid=yx_saleOrder.cid) ");
            sb.append(" where yx_saleOrder.isRecommend=1 and  yx_order.type =1  and yx_order.states=0 and (yx_order_demand.threestates=2 or yx_order_demand.post_states=2) ");
            sb.append(sb_name);
            sb.append("GROUP BY yx_order.did order by yx_custom.update_time desc");

            Map<String, Object> page = new HashMap<>();
            page = getPage(start);
            String sql_page = page.get("sql_page").toString();
            sb.append(sql_page);
            try {

                list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();

                        mp.put("c_name", rs.getString("c_name"));
                        mp.put("c_phone", rs.getString("c_phone"));
                        mp.put("oid", rs.getString("id"));
                        mp.put("o_name", rs.getString("o_name"));
                        mp.put("did", rs.getString("did"));
                        mp.put("origin", rs.getString("origin"));
                        mp.put("channel", rs.getString("channel"));
                        mp.put("service_type", rs.getString("service_type"));
                        mp.put("service_day", rs.getString("service_day"));
                        mp.put("level", rs.getString("level"));
                        mp.put("price", rs.getString("price"));
                        mp.put("states", rs.getString("states"));
                        mp.put("timetype", rs.getString("timetype"));
                        mp.put("start", rs.getString("starttime"));
                        // mp.put("remarks", rs.getString("remarks"));
                        mp.put("post_states", rs.getInt("post_states"));


                        String sql_count = "select count(*) from yx_substitution where cid=?";
                        Integer count = jdbcTemplate.queryForObject(sql_count, Integer.class, rs.getInt("cid"));
                        if (count > 0) {
                            String sql_remarks = "select remarks from yx_substitution where id=(select max(id) from yx_substitution where cid=? )";
                            String remarks = jdbcTemplate.queryForObject(sql_remarks, String.class, rs.getInt("cid"));
                            mp.put("remarks", remarks);
                        } else {
                            mp.put("remarks", null);
                        }


                        String starttime = "";
                        String endtime = "";
                        String startweek = "";
                        String endweek = "";
                        try {
                            starttime = getMonth(rs.getString("starttime"));
                            endtime = getMonth(rs.getString("endtime"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        try {
                            startweek = getWeek(rs.getString("starttime"));
                            endweek = getWeek(rs.getString("endtime"));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        mp.put("endtime", endtime);
                        mp.put("starttime", starttime);
                        mp.put("startweek", startweek);
                        mp.put("endweek", endweek);
                        return mp;
                    }
                }, queryList.toArray());

            } catch (Exception e) {

            }
        }
        return list;
    }

    //派岗推荐月嫂
    @SneakyThrows
    @Override
    public Map<String, Object> recommendMatorn(String json) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        String start = jsonObject.getString("start");
        Integer service_day = jsonObject.getInteger("service_day");
        Integer price = jsonObject.getInteger("price");
        String name = jsonObject.getString("name");
        Integer pageStart = jsonObject.getInteger("pageStart");
        Integer did = jsonObject.getInteger("did");


        Map<String, Object> allMap = new HashMap<String, Object>();
        //   String sql = "select yx_matorn.id,yx_period.period,yx_bussiness.grade,yx_bussiness.isorder from yx_matorn left join yx_period on (yx_matorn.id=yx_period.mid) left join yx_bussiness on (yx_matorn.id=yx_bussiness.mid) where yx_bussiness.shelf=1 and yx_period.period is not null ";

        StringBuffer sb1 = new StringBuffer();
        sb1.append("select yx_matorn.id,yx_period.period,yx_bussiness.grade,yx_bussiness.isorder from yx_matorn left join yx_period on (yx_matorn.id=yx_period.mid)");
        sb1.append(" left join yx_bussiness on (yx_matorn.id=yx_bussiness.mid)");
        sb1.append("where yx_bussiness.shelf=1 and yx_period.period is not null");
        List<Object> queryList = new ArrayList<Object>();
        if (name == "" || name == null || name.isEmpty()) {

        } else {
            String sql2 = "select name from yx_matorn  left join yx_bussiness on (yx_matorn.id=yx_bussiness.mid) where yx_bussiness.shelf=1  and yx_matorn.name = ? ";
            try {
                List<String> list3 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{name}, String.class);

                if (name.equals(list3.get(0))) {
                    sb1.append("    and  yx_matorn.name=?  ");
                    queryList.add(list3.get(0));
                } else {

                }
            } catch (Exception e) {
                if (name.length() == 1) {
                    String xing = name.substring(0, 1);
                    sb1.append("  and  yx_matorn.name like ?  ");
                    queryList.add(xing + "%");
                } else {
                    return allMap;
                }
            }

        }

        list = this.jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("mid", rs.getInt("id"));
                mp.put("period", rs.getString("period"));
                mp.put("isorder", rs.getInt("isorder"));
                return mp;
            }
        }, queryList.toArray());

        List<Map<String, Object>> notOrderList = new ArrayList<Map<String, Object>>();//不在订单
        List<Map<String, Object>> OnOrderList = new ArrayList<Map<String, Object>>();//在订单
        for (int i = 0; i < list.size(); i++) {
            if (Integer.valueOf(list.get(i).get("isorder").toString()) == 0) {
                notOrderList.add(list.get(i));
            } else {
                OnOrderList.add(list.get(i));
            }
        }

        String sql_return_time = "select return_time from yx_order where mid=? and order_states=2 ";
        List<Map<String, Object>> NewOrderList = new ArrayList<Map<String, Object>>();
        //不在订单和在订单的月嫂集合

        for (int i = 0; i < OnOrderList.size(); i++) {
            System.out.println("mid___________" + OnOrderList.get(i).get("mid"));
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

        String sql_time = "select confirm_time from yx_order where order_states=3 and did=? ";
        List<Map<String, Object>> timeList = (List) this.jdbcTemplate.queryForList(sql_time, did);
        if (timeList.size() > 0) {
            //搜索待更换上一个订单的回岗时间  作为新的时间
            start = timeList.get(timeList.size() - 1).get("confirm_time").toString();
        } else {
            //待推荐不变
        }
        List<Integer> periodlist = new ArrayList<Integer>();
        periodlist = searchPeriod(NewOrderList, start);

        StringBuffer sb = new StringBuffer();
        sb.append("select yx_matorn.id,yx_matorn.uid,yx_matorn.name,yx_matorn.born,yx_matorn.household,yx_matorn.zodiac,yx_matorn.constellation,");
        sb.append("yx_bussiness.photo,yx_bussiness.isorder,yx_contact.phone,yx_bussiness.grade from yx_matorn");
        sb.append(" LEFT JOIN yx_bussiness on (yx_matorn.id=yx_bussiness.mid) LEFT JOIN yx_contact on (yx_matorn.id=yx_contact.mid)");
        sb.append(" where yx_matorn.id=? ");
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> alllist = new ArrayList<Map<String, Object>>();

        //分页数据
        allMap.put("num", periodlist.size());
        List<Integer> newMidList = null;
        if (periodlist.size() >= 0 && periodlist.size() <= 10) {
            newMidList = periodlist;
        } else {
            if (periodlist.size() < pageStart * 10) {
                newMidList = periodlist.subList((pageStart - 1) * 10, periodlist.size());
            } else {
                newMidList = periodlist.subList((pageStart - 1) * 10, pageStart * 10);
            }

        }


        for (int i = 0; i < newMidList.size(); i++) {
            list2 = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("min", 0);
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
            }, periodlist.get(i));
            alllist.add(list2.get(0));
        }

        allMap.put("alllist", alllist);
        return allMap;
    }


    @Override
    public int choiceMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer oid = jsonObject.getInteger("oid");
        Integer did = jsonObject.getInteger("did");
        String threematorn = jsonObject.getString("threematorn");

        System.out.println("threematorn = " + threematorn);
        String sql_cid = "select cid from yx_order where id=?";
        Integer cid = this.jdbcTemplate.queryForObject(sql_cid, Integer.class, oid);

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time.format(new Date());

        //修改时间更新
        String sql_update = "update yx_custom set update_time=? where id=?";
        int states_update = this.jdbcTemplate.update(sql_update, nowTime, cid);


        String sql_o = "update yx_order set states=? where id=?";
        int states_o = this.jdbcTemplate.update(sql_o, 1, oid);

        String sql_d = "update yx_order_demand set threematorn=? where id=?";
        int states_d = this.jdbcTemplate.update(sql_d, threematorn, did);


        //获取母婴顾问id
        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);

        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_message (bid,cid,mid,type,creat_time)");
        sb.append("value(?,?,?,?,?)");
        //type=1  派岗推荐月嫂成功，等待母婴顾问选择其中一个
        int states_messgage = this.jdbcTemplate.update(sb.toString(), bid, cid, threematorn, 1, nowTime);

        if (states_d > 0 && states_o > 0 && states_messgage > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Transactional
    @Override
    public int confirmPost(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer oid = jsonObject.getInteger("oid");
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String wages_remarks = jsonObject.getString("wages_remarks");
        String creattime = time1.format(new Date());
        String sql_cid = "select cid,mid from yx_order where id=?";
        List<Map<String, Object>> cList = this.jdbcTemplate.queryForList(sql_cid, oid);
        //修改时间更新
        String sql_update = "update yx_custom set update_time=? where id=?";
        Integer cid = Integer.valueOf(cList.get(0).get("cid").toString());
        int states_update = this.jdbcTemplate.update(sql_update, creattime, cid);

        String sql = "update yx_order set order_states=?,wages_remarks=? where id=?";
        int states = this.jdbcTemplate.update(sql, 1, wages_remarks, oid);

        //获取母婴顾问id
        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);

        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb.append("value(?,?,?,?,?)");
        //type=4 月嫂派岗成功，等待母婴顾问确认月嫂到岗
        int state_message = this.jdbcTemplate.update(sb.toString(), cid, bid, cList.get(0).get("mid"), 4, creattime);


        if (states > 0 && states_update > 0) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public List<Map<String, Object>> PostByName(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String name = jsonObject.getString("name");


        StringBuffer sb = new StringBuffer();
        List list = null;
        sb.append("select yx_matorn.id,yx_matorn.uid,yx_matorn.name,yx_matorn.born,yx_matorn.household,yx_matorn.zodiac,yx_matorn.constellation,");
        sb.append("yx_bussiness.photo,yx_contact.phone,yx_bussiness.grade from yx_matorn");
        sb.append(" LEFT JOIN yx_bussiness on (yx_matorn.id=yx_bussiness.mid) LEFT JOIN yx_contact on (yx_matorn.id=yx_contact.mid) ");
        List<Object> queryList = new ArrayList<Object>();
        String sql2 = "select name from yx_matorn where name = ? ";
        try {
            List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{name}, String.class);

            if (name.equals(list1.get(0))) {
                sb.append("  where  yx_matorn.name=?  ");
                queryList.add(list1.get(0));
            } else {

            }
        } catch (Exception e) {
            if (name.length() == 1) {
                String xing = name.substring(0, 1);
                sb.append("where  yx_matorn.name like ?  ");
                queryList.add(xing + "%");
            } else {
                return list;
            }
        }
        sb.append(" and  yx_bussiness.shelf=1 ");
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("min", 0);
                mp.put("mid", rs.getInt("id"));
                mp.put("name", rs.getString("name"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("constellation", rs.getString("constellation"));
                mp.put("photo", rs.getString("photo"));
                mp.put("grade", rs.getString("grade"));

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
        }, queryList.toArray());

        return list;
    }


    @Override
    public int getMatornNull(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer did = jsonObject.getInteger("did");
        Integer oid = jsonObject.getInteger("oid");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time.format(new Date());

        String sql_d = "update yx_order_demand set threematorn=? where id=?";
        int states_d = this.jdbcTemplate.update(sql_d, null, did);
        String sql_o = "update yx_order set mid=?,states=? where id=?";
        int states_o = this.jdbcTemplate.update(sql_o, 0, 0, oid);

        String sql_cid = "select cid from yx_order where id=?";
        Integer cid = this.jdbcTemplate.queryForObject(sql_cid, Integer.class, oid);

        //获取母婴顾问id
        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);

        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb.append("value(?,?,?,?,?)");
        //type=0  等待派岗推荐月嫂
        int states_messgage = this.jdbcTemplate.update(sb.toString(), cid, bid, null, 0, nowTime);

        if (states_d > 0 && states_o > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @description: 换人之后选择月嫂上岗。
     * @return:
     */
    @Override
    public int newMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        Integer oid = jsonObject.getInteger("oid");
        Integer mid = jsonObject.getInteger("mid");
        Integer post_states = jsonObject.getInteger("post_states");
        String wages_remarks = jsonObject.getString("wages_remarks");

        String sql_cid = "select cid,o_number from yx_order where id=?";
        List<Map<String, Object>> cList = this.jdbcTemplate.queryForList(sql_cid, oid);

        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cList.get(0).get("cid"));

        String sql_three = "select threestates from yx_order_demand where cid=?";
        Integer threestates = this.jdbcTemplate.queryForObject(sql_three, Integer.class, cList.get(0).get("cid"));

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String update_time = time.format(new Date());
        //修改时间更新
        String sql_update = "update yx_custom set update_time=? where id=?";
        int states_update = this.jdbcTemplate.update(sql_update, update_time, cList.get(0).get("cid"));

        //添加流程信息
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb.append("value(?,?,?,?,?)");
        int state_message = 0;

        String number = cList.get(0).get("o_number").toString().substring(10, 13);
        //修改订单状态信息
        String sql = "update yx_order set mid=?,type=?,states=?,order_states=?,wages_remarks=? where id=?";
        int states = 0;
        //修改月嫂是否在订单中状态
        String sql_m = "update yx_bussiness set isorder=? where mid=?";
        int states_m = 0;

        if (number.equals("CAA")) {
            //全程订单换人
            //与普通换人相同，不需要确认到岗，直接服务中，在订单中
            states = this.jdbcTemplate.update(sql, mid, 1, 2, 2, wages_remarks, oid);
            states_m = this.jdbcTemplate.update(sql_m, 1, mid);
            //type=8 月嫂换人到岗，
            state_message = this.jdbcTemplate.update(sb.toString(), cList.get(0).get("cid"), bid, mid, 8, update_time);
        } else {
            //非全程订单
            if (number.equals("CAB")) {
                //转岗订单
                String sql_number = "select o_number from yx_order where cid=?";
                List<Map<String, Object>> numberList = this.jdbcTemplate.queryForList(sql_number, cList.get(0).get("cid"));
                List<String> nList = new ArrayList<>();
                numberList.forEach(stringObjectMap -> {
                    nList.add(stringObjectMap.get("o_number").toString().substring(10, 13));
                });
                //全程订单号次数
                Integer count = Collections.frequency(nList, "CAB");
                if (post_states == 2 && count == 1) {
                    //有一个CAB,
                    //之前全程转岗换人，在进行换人
                    //派岗状态 ，全程转岗换人，下一个月嫂需要确认到岗，不在订单
                    states = this.jdbcTemplate.update(sql, mid, 1, 2, 1, wages_remarks, oid);
                    states_m = this.jdbcTemplate.update(sql_m, 0, mid);
                    //type=12 转岗换人派岗完成,等待月嫂到岗
                    state_message = this.jdbcTemplate.update(sb.toString(), cList.get(0).get("cid"), bid, mid, 12, update_time);

                } else {
                    //有两个CAB，全程转岗换人之后，在换人，(两次换人)
                    // 与普通换人相同，不需要确认到岗，直接服务中，在订单中
                    states = this.jdbcTemplate.update(sql, mid, 1, 2, 2, wages_remarks, oid);
                    states_m = this.jdbcTemplate.update(sql_m, 1, mid);
                    //type=8 月嫂换人到岗，
                    state_message = this.jdbcTemplate.update(sb.toString(), cList.get(0).get("cid"), bid, mid, 8, update_time);
                }
            } else {
                //不是转岗的订单
                // 普通换人，不需要确认到岗，直接服务中，在订单中
                states = this.jdbcTemplate.update(sql, mid, 1, 2, 2, wages_remarks, oid);
                states_m = this.jdbcTemplate.update(sql_m, 1, mid);
                //type=8 月嫂换人到岗，
                state_message = this.jdbcTemplate.update(sb.toString(), cList.get(0).get("cid"), bid, mid, 8, update_time);

            }

        }


        if (states > 0 && state_message > 0 && states_m > 0) {
            return 1;
        } else {
            return 0;
        }

//        if (post_states == 0 || post_states == 1) {
//            //普通换人，不需要确认到岗，直接服务中，在订单中
//            String sql = "update yx_order set mid=?,type=?,states=?,order_states=? where id=?";
//            int states = this.jdbcTemplate.update(sql, mid, 1, 2, 2, oid);
//            String sql_m = "update yx_bussiness set isorder=? where mid=?";
//            int states_m = this.jdbcTemplate.update(sql_m, 1, mid);
//
//            //type=8 月嫂换人到岗，
//            int state_message = this.jdbcTemplate.update(sb.toString(), cid, bList.get(0).get("bid"), mid, 8, update_time);
//
//            if (states > 0 && states_m > 0) {
//                somnus = 1;
//            } else {
//                somnus = 0;
//            }
//        }
//        if (post_states == 2) {
//            //派岗状态 ，全程转岗换人，下一个月嫂需要确认到岗，不在订单
//            String sql = "update yx_order set mid=?,type=?,states=?,order_states=? where id=?";
//            int states = this.jdbcTemplate.update(sql, mid, 1, 2, 1, oid);
//            String sql_m = "update yx_bussiness set isorder=? where mid=?";
//            int states_m = this.jdbcTemplate.update(sql_m, 0, mid);
//
//            //type=12 转岗换人派岗完成,等待月嫂到岗
//            int state_message = this.jdbcTemplate.update(sb.toString(), cid, bList.get(0).get("bid"), mid, 12, update_time);
//
//            if (states > 0 && states_m > 0) {
//                somnus = 1;
//            } else {
//                somnus = 0;
//            }
//        }

    }


    public int countCustom(String somnus) {
        StringBuffer sb = new StringBuffer();
        sb.append("select count(*) from yx_custom");
        if (somnus.equals("all")) {

        }
        if (somnus.equals("production")) {
            //已生产
            sb.append(" where production_situation=1 ");
        }
        if (somnus.equals("unproduction")) {
            //未生产
            sb.append(" where production_situation=0 ");
        }
        if (somnus.equals("unknown")) {
            //未知
            sb.append(" where production_situation=3 ");
        }

        return this.jdbcTemplate.queryForObject(sb.toString(), Integer.class);

    }

    @SneakyThrows
    @Override
    public Map<String, Object> getAllCustom(String json) {

        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String somnus = jsonObject.getString("somnus");
        Integer start = jsonObject.getInteger("start");
        Map<String, Object> map = new HashMap<String, Object>();
        List list = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select * from yx_custom where bid<>0 ");

        if (somnus.equals("all")) {

        }
        if (somnus.equals("production")) {
            //已生产
            sb.append("  and production_situation=1");
        }
        if (somnus.equals("unproduction")) {
            //未生产
            sb.append("  and production_situation=0");
        }
        if (somnus.equals("unknown")) {
            //未知
            sb.append("  and production_situation=3");
        }
        sb.append(" order by id desc");

        Integer count = countCustom(somnus);
        map.put("count", count);
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        String sql_page = page.get("sql_page").toString();
        sb.append(sql_page);
        map.put("page", page.get("page"));
        map.put("num", page.get("num"));
        String sql_name = "select name from yx_user where power=4";
        List<String> bnameList = (List) this.jdbcTemplate.queryForList(sql_name.toString());
        map.put("bnameList", bnameList);

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("id", rs.getInt("id"));

                Integer bid = rs.getInt("bid");
                String sql_b = "select name from yx_user where id=?";
                String bname = jdbcTemplate.queryForObject(sql_b, String.class, bid);

                mp.put("bname", bname);
                mp.put("c_name", rs.getString("c_name"));
                mp.put("c_phone", rs.getString("c_phone"));
                mp.put("production_situation", rs.getInt("production_situation"));
                mp.put("nodate", rs.getString("nodate"));
                mp.put("production_date", rs.getString("production_date"));
                mp.put("production_mode", rs.getString("production_mode"));
                mp.put("baby_sex", rs.getString("baby_sex"));
                mp.put("creat_time", rs.getString("creat_time"));
                return mp;

            }
        });
        map.put("list", list);
        return map;
    }

    @SneakyThrows
    @Override
    public List<Map<String, Object>> orderDynamic(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        List<Map<String, Object>> allLiist = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        List list = null;

        Integer bid = jsonObject.getInteger("bid");
        Integer start = jsonObject.getInteger("start");
        String somnus = jsonObject.getString("somnus");
        StringBuffer sb = new StringBuffer();
        List<Object> queryList = new ArrayList<Object>();

        sb.append("select m.id,m.cid,m.mid,m.type,m.creat_time,m.isRead,c.c_name,c.c_photo,c.followCount,c.production_situation, ");
        sb.append(" c.nodate,c.production_date,c.address,od.threematorn  from yx_message m");
        sb.append(" LEFT join yx_custom c on (m.cid=c.id)");
        sb.append(" LEFT join yx_order_demand od on (m.cid=od.cid) where 1=1");

        StringBuffer sb_count = new StringBuffer();
        sb_count.append("select count(*) from yx_message m where 1=1");
        if (somnus.equals("order")) {
            String sql_id = "select power from yx_user where id=?";
            Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, bid);
            if (power == 100 || power == 20) {
                sb.append("  and m.type in(1,4,8,12) ");
                sb_count.append(" and m.type in(1,4,8,12)");
            } else {
                sb.append(" and m.bid=?  and m.type in(1,4,8,12) ");
                sb_count.append(" and m.bid=?  and m.type in(1,4,8,12)");
                queryList.add(bid);
            }
        }
        if (somnus.equals("post")) {
            sb.append(" and m.type in (0,2,4)");
            sb_count.append(" and m.type in (0,2,4)");
        }
        sb.append(" order by m.creat_time desc");

        sb_count.append(" and m.isRead is null");
        sb.append(" limit 100");

        Integer count = this.jdbcTemplate.queryForObject(sb_count.toString(), Integer.class, queryList.toArray());
        map.put("count", count);

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("id", rs.getString("id"));
                mp.put("cid", rs.getInt("cid"));
                StringBuffer sb_intention = new StringBuffer();
                sb_intention.append("select con.intention from yx_consultingOrder con LEFT JOIN yx_custom c on(con.cid=c.id) ");
                sb_intention.append(" where con.id=(select MAX(con.id) as conid FROM  yx_consultingOrder con where con.cid=c.id ) and con.cid=?");
                String intention = null;
                try {
                    intention = jdbcTemplate.queryForObject(sb_intention.toString(), String.class, rs.getInt("cid"));

                } catch (Exception e) {

                }
                mp.put("intention", intention);
                mp.put("c_name", rs.getString("c_name"));
                mp.put("c_photo", rs.getString("c_photo"));
                mp.put("followCount", rs.getInt("followCount"));
                mp.put("production_situation", rs.getInt("production_situation"));
                mp.put("isRead", rs.getInt("isRead"));
                if (rs.getInt("production_situation") == 0) {
                    mp.put("ptime", rs.getString("nodate"));
                } else {
                    mp.put("ptime", rs.getString("production_date"));
                }
                mp.put("address", rs.getString("address"));
//                if (rs.getInt("mid") != 0) {
//                    String sql_mname = "select name from yx_matorn where id=?";
//                    String m_name = jdbcTemplate.queryForObject(sql_mname, String.class, rs.getInt("mid"));
//                    mp.put("m_name", m_name);
//                } else {
//                    mp.put("m_name", "");
//                }
                mp.put("type", rs.getInt("type"));
                mp.put("time", rs.getString("creat_time"));
                if (rs.getString("mid") != null) {
                    String sql_matorn = "select m.id,m.name,b.photo from yx_matorn m left join yx_bussiness b on (m.id=b.mid) where m.id=?";
                    List<Map<String, Object>> matornList = new ArrayList<>();
                    if (rs.getString("mid").contains("[")) {
                        //推荐月嫂是集合字符串
                        JSONArray jsonArray = JSONArray.fromObject(rs.getString("mid"));
                        List<Integer> listMatorn = (List) jsonArray;

                        for (int i = 0; i < listMatorn.size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql_matorn, listMatorn.get(i));
                            map.put("mid", mapList.get(0).get("id"));
                            map.put("photo", mapList.get(0).get("photo"));
                            map.put("name", mapList.get(0).get("name"));
                            matornList.add(map);
                        }
                        mp.put("matornList", matornList);

                    } else {
                        //单个数字字符串
                        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql_matorn, rs.getString("mid"));
                        Map<String, Object> m_map = new HashMap<>();
                        m_map.put("mid", mapList.get(0).get("id"));
                        m_map.put("photo", mapList.get(0).get("photo"));
                        m_map.put("name", mapList.get(0).get("name"));
                        matornList.add(m_map);
                        mp.put("matornList", matornList);
                    }

                } else {

                }

                return mp;
            }

        }, queryList.toArray());
        map.put("list", list);
        //信息
        allLiist.add(map);

        return allLiist;
    }

    @SneakyThrows
    @Override
    public List<Map<String, Object>> intentionList(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer bid = jsonObject.getInteger("bid");
        String somnus = jsonObject.getString("somnus");
        Integer start = jsonObject.getInteger("start");
        Integer cid = jsonObject.getInteger("cid");

        List<Map<String, Object>> list = new ArrayList<>();
        List<Object> queryList = new ArrayList<Object>();
        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, bid);
        StringBuffer sb = new StringBuffer();

        Map<String, Object> page = new HashMap<>();
        page = getHalfPage(start);
        String sql_page = page.get("sql_page").toString();
        if (somnus.equals("read")) {
            sb.append("select c.id,c.c_name,c.production_situation,c.nodate,c.production_date,c.origin,c.channel,c.c_photo,c.address,c.followCount,con.id,con.intention,con.points");
            sb.append(" from yx_consultingOrder con LEFT JOIN yx_custom c on(con.cid=c.id)");
            sb.append(" WHERE  c.isIntention=1 and con.id=(select MAX(con.id) as conid FROM  yx_consultingOrder con where con.cid=c.id ) ");
            sb.append(" and con.isRead=0 ");
        }
        if (somnus.equals("all")) {
            sb.append("select c.id,c.c_name,c.production_situation,c.nodate,c.production_date,c.origin,c.channel,c.c_photo,c.address,c.followCount");
            sb.append(" from yx_custom c ");
            sb.append(" WHERE  c.isIntention=1  ");
        }

        if (power == 100 || power == 20) {
        } else {
            sb.append(" and c.bid=? ");
            queryList.add(bid);
        }

        if (cid == 0) {
            //全部客户
        } else {
            //单个客户
            sb.append(" and c.id=?");
            queryList.add(cid);
        }


        if (somnus.equals("all")) {
            sb.append(" order by c.update_time desc");
            sb.append(sql_page);
        } else {
            sb.append(" order by con.creat_time desc");
        }

        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("id", rs.getInt("id"));
                mp.put("c_name", rs.getString("c_name"));
                mp.put("address", rs.getString("address"));
                mp.put("followCount", rs.getInt("followCount"));
                mp.put("c_photo", rs.getString("c_photo"));
                String origin = "";
                if (rs.getString("origin") != null) {
                    origin = rs.getString("origin");
                    if (rs.getString("channel") != null) {
                        origin = origin + "-" + rs.getString("channel");
                    } else {

                    }

                } else {

                }
                mp.put("origin", origin);
                mp.put("production_situation", rs.getInt("production_situation"));
                if (rs.getInt("production_situation") == 0) {
                    mp.put("time", rs.getString("nodate"));
                } else {
                    mp.put("time", rs.getString("production_date"));
                }

                if (somnus.equals("read")) {
                    mp.put("intention", rs.getString("intention"));
                    mp.put("points", rs.getString("points"));
                } else {
                    String sql_count = "select count(*) from yx_consultingOrder where cid=?";
                    int count = jdbcTemplate.queryForObject(sql_count, Integer.class, rs.getInt("id"));
                    if (count > 0) {
                        //有咨询工单
                        String sql_intention = "select intention,points from yx_consultingOrder where id=(select max(id) from yx_consultingOrder where cid=? )";
                        Map<String, Object> intentionMap = jdbcTemplate.queryForMap(sql_intention, rs.getInt("id"));
                        mp.put("intention", intentionMap.get("intention"));
                        mp.put("points", intentionMap.get("points"));
                    } else {
                        //没有咨询工单
                        mp.put("intention", "B");
                        mp.put("points", null);
                    }


                }

                return mp;
            }

        }, queryList.toArray());
        return list;
    }

    @Override
    public int updateMessage(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer bid = jsonObject.getInteger("bid");
        Integer id = jsonObject.getInteger("id");
        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, bid);
        if (power == 4) {
            String sql = "update yx_message set isRead=1 where id=? ";
            int states = this.jdbcTemplate.update(sql, id);
            if (states > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 1;
        }

    }
}
