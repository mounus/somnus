package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.OrderDao;
import com.example.entiy.Order;
import com.example.entiy.Order_demand;
import com.example.entiy.Reservation;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.apache.taglibs.standard.lang.jstl.NullLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import sun.font.DelegatingShape;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.*;
import static com.example.util.MonthUtil.*;
import static com.example.util.MonthUtil.isMonth;
import static com.example.util.NumberUtil.*;

import static com.example.util.PageUtil.getAllPage;
import static com.example.util.PageUtil.getPage;
import static com.example.util.PriceUtil.*;
import static com.example.util.SearchUtil.searchPeriod;


@Repository
public class OrderDaoImpl implements OrderDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;


    public int getForId() {
        String sql = "select max(id ) from yx_order_demand ";
        int id = this.jdbcTemplate.queryForObject(sql, Integer.class);
        return id;
    }

    public int getOid() {
        String sql = "select max(id ) from yx_order ";
        int oid = this.jdbcTemplate.queryForObject(sql, Integer.class);
        return oid;
    }

    @Transactional
    @Override
    public int saveDemand(Order_demand order_demand) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time.format(new Date());

        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_order_demand (cid,order_type,service_type,service_day,starttime,endtime,timetype,level,twins,preemie,remarks,price,work_day,threestates,creat_time,activityType,onePrice)");
        sb.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        String starttime = order_demand.getStarttime();
        String endtime = order_demand.getEndtime();
        // Integer count = getCount(starttime, endtime);
        //节假日方法有问题
        Integer count = 0;
        Integer work_day = order_demand.getService_day() - count;
        Integer price = 0;
        System.out.println("order_demand = " + order_demand.getPrice());
        if (order_demand.getPrice() != null) {
            price = order_demand.getPrice();
        } else {
            price = 9800;
        }

        Integer onePrice = 0;
        if (order_demand.getService_type().equals("月子会所服务")) {

        } else {
            if (order_demand.getOnePrice() != null) {
                onePrice = order_demand.getOnePrice();
            } else {
                onePrice = price / order_demand.getService_day();
            }
        }


        int states = this.jdbcTemplate.update(sb.toString(), order_demand.getCid(), order_demand.getOrder_type(), order_demand.getService_type(), order_demand.getService_day(), starttime, endtime,
                order_demand.getTimetype(), order_demand.getLevel(), order_demand.getTwins(), order_demand.getPreemie(), order_demand.getRemarks(), price, work_day, 0, nowTime, 0, onePrice);
        int did = getForId();
        StringBuffer sb1 = new StringBuffer();
        sb1.append("insert into yx_order (did,mid,cid,type,states)");
        sb1.append("values(?,?,?,?,?)");
        int states1 = this.jdbcTemplate.update(sb1.toString(), did, 0, order_demand.getCid(), 0, 0);
        Integer oid = getOid();


        if (states > 0 && states1 > 0) {
            return oid;
        } else {
            return 0;
        }

    }

    @Override
    public int updateDemand(Order_demand order_demand) {
        StringBuffer sb = new StringBuffer();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        sb.append("update  yx_order_demand set service_type=?,service_day=?,starttime=?,endtime=?,timetype=?,level=?,price=?,work_day=?,activityType=?,onePrice=? where id=?");

        String starttime = order_demand.getStarttime();
        String endtime = order_demand.getEndtime();

        // Integer count = getCount(starttime, endtime);
        //  Integer work_day = order_demand.getService_day() - count;
        Integer price = 0;
        if (order_demand.getPrice() != null) {
            price = order_demand.getPrice();
        } else {
            price = 9800;
        }
        int staets = this.jdbcTemplate.update(sb.toString(), order_demand.getService_type(), order_demand.getService_day(), order_demand.getStarttime(), endtime, order_demand.getTimetype(),
                order_demand.getLevel(), price, order_demand.getService_day(), order_demand.getActivityType(), order_demand.getOnePrice(), order_demand.getId());
        if (staets > 0) {
            return 1;
        } else {
            return 0;
        }
    }


    @SneakyThrows
    @Override
    public List<Map<String, Object>> managerOrder(String json) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        JSONObject jsonObject = JSON.parseObject(json);

        Integer uid = jsonObject.getInteger("uid");
        String somnus = jsonObject.getString("somnus");
        String other = jsonObject.getString("other");
        String name = jsonObject.getString("name");
        Integer start = jsonObject.getInteger("start");

        List<Object> queryList = new ArrayList<Object>();
        StringBuffer sb = new StringBuffer();

        sb.append("select yx_order.o_number,yx_order_demand.service_day,yx_order_demand.starttime,yx_order_demand.work_states,yx_order.order_day,");
        sb.append("yx_order.mid,yx_order.feedback,yx_order.isfeedback,");
        sb.append("yx_order.o_address,yx_order.cid,yx_order.region,yx_order.return_time,yx_order.confirm_time,yx_order.id,yx_order.arrival_time,yx_order.mid,yx_order.order_states from yx_order ");
        sb.append("  left JOIN  yx_order_demand on (yx_order.did=yx_order_demand.id)  left JOIN  yx_matorn on (yx_order.mid=yx_matorn.id)");
        sb.append(" where 1=1 ");

        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, uid);

        if (power == 10 || power == 100) {

        } else {
            sb.append(" and yx_matorn.uid=?");
            queryList.add(uid);
        }

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowtime = time.format(new Date());
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        if (somnus.equals("all")) {
            sb.append(" and yx_order.order_states<>0  ");
        }
        if (somnus.equals("before")) {
            sb.append(" and yx_order.order_states=1  ");
        }
        if (somnus.equals("work")) {
            StringBuffer sb1 = new StringBuffer();
            sb1.append("select yx_order.arrival_time,yx_order.return_time,yx_order.id from yx_order");
            sb1.append(" left JOIN  yx_order_demand on (yx_order.did=yx_order_demand.id)");
            sb1.append(" where yx_order.type=1  and yx_order.order_states=2  and  arrival_time <?  and  ?<return_time");
            //有问题yx_order_demand

            list1 = this.jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("arrival_time", rs.getString("arrival_time"));
                    mp.put("return_time", rs.getString("return_time"));
                    mp.put("oid", rs.getString("id"));
                    return mp;
                }
            }, nowtime, nowtime);


            List<Object> onlist = new ArrayList<Object>();
            List<Object> staylist = new ArrayList<Object>();
            for (int i = 0; i < list1.size(); i++) {

                try {
                    int day = daysBetween(nowtime, list1.get(i).get("return_time").toString());
                    if (day > 3) {
                        onlist.add(list1.get(i).get("oid"));
                    } else {
                        staylist.add(list1.get(i).get("oid"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

            sb.append(" and yx_order.order_states=2  and arrival_time <?  ");
            queryList.add(nowtime);
            sb.append(" and  ?<return_time   ");
            queryList.add(nowtime);

            if (other.equals("on")) {
                if (onlist.size() != 0) {
                    sb.append(" and  yx_order.id in (");//上岗中
                    for (int i = 0; i < onlist.size(); i++) {
                        sb.append(onlist.get(i));
                        if (i < onlist.size() - 1) {
                            sb.append(",");
                        }
                    }
                    sb.append(")");
                } else {

                }

            }
            if (other.equals("stay")) {
                if (staylist.size() != 0) {
                    sb.append(" and  yx_order.id in (");
                    for (int i = 0; i < staylist.size(); i++) {
                        sb.append(staylist.get(i));
                        if (i < staylist.size() - 1) {
                            sb.append(",");
                        }
                    }
                    sb.append(")");
                } else {

                }
            }
        }
        if (somnus.equals("after")) {
            sb.append(" and yx_order.order_states=3  ");
        }


        if (name == null || name == "" || name.isEmpty()) {

        } else {
            String sql2 = "select name from yx_matorn where name = ? ";

            try {
                List<String> list_name = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{name}, String.class);

                if (name.equals(list_name.get(0))) {
                    sb.append("  and  yx_matorn.name=?  ");
                    queryList.add(list_name.get(0));
                } else {

                }
            } catch (Exception e) {
                if (name.length() == 1) {
                    String xing = name.substring(0, 1);
                    sb.append("and  yx_matorn.name like ?  ");
                    queryList.add(xing + "%");
                } else {
                    return list;
                }
            }
        }


        Map<String, Object> page = new HashMap<>();
        page = getPage(start);
        String sql_page = page.get("sql_page").toString();

        sb.append("  order by yx_order.id desc ");
        sb.append(sql_page);
        System.out.println("sb = " + sb);
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("oid", rs.getString("id"));
                mp.put("o_number", rs.getString("o_number"));
                mp.put("region", rs.getString("region"));
                mp.put("o_address", rs.getString("o_address"));
                mp.put("service_day", rs.getString("service_day"));
                mp.put("work_states", rs.getString("work_states"));
                mp.put("order_states", rs.getString("order_states"));
                mp.put("mid", rs.getInt("mid"));
                Integer cid = rs.getInt("cid");
                String sql_bid = "select bid from yx_custom where id=?";
                Integer bid = jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);
                List<Map<String, Object>> list3 = new ArrayList<>();
                String sql_a = "select name,phone from yx_user where id=?";
                list3 = jdbcTemplate.queryForList(sql_a, bid);

                mp.put("bname", list3.get(0).get("name"));
                mp.put("p_phone", list3.get(0).get("phone"));


                String arrival_time = rs.getString("arrival_time");
                String return_time = rs.getString("return_time");


                Integer order_states = rs.getInt("order_states");

                Integer mid = rs.getInt("mid");
                List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                StringBuffer sb1 = new StringBuffer();
                sb1.append("select yx_matorn.name,yx_contact.phone,yx_bussiness.photo,yx_bussiness.grade from yx_matorn ");
                sb1.append("  left JOIN yx_contact on (yx_matorn.id=yx_contact.mid) ");
                sb1.append("  left JOIN yx_bussiness on (yx_matorn.id=yx_bussiness.mid)");
                sb1.append(" where yx_matorn.id=?");
                list1 = jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        mp.put("name", rs.getString("name"));
                        mp.put("phone", rs.getString("phone"));
                        mp.put("photo", rs.getString("photo"));
                        mp.put("grade", rs.getString("grade"));
                        return mp;
                    }
                }, mid);
                mp.put("name", list1.get(0).get("name"));
                mp.put("phone", list1.get(0).get("phone"));
                mp.put("photo", list1.get(0).get("photo"));
                mp.put("grade", list1.get(0).get("grade"));
                try {
                    Date now = time.parse(nowtime);
                    Date start = time.parse(rs.getString("starttime"));

                    if (order_states == 1) {
                        //小于开始时间

                        int day = daysBetween(nowtime, rs.getString("starttime"));
                        mp.put("day", day);
                        mp.put("starttime", rs.getString("starttime"));
                        try {
                            String startweek = getWeek(rs.getString("starttime"));
                            mp.put("startweek", startweek);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    if (order_states == 2) {
                        //大于开始时间  小于结束时间
                        Date end = time.parse(return_time);
                        int day = daysBetween(nowtime, return_time);

                        if (day > 3) {
                            int day1 = daysBetween(arrival_time, nowtime);
                            mp.put("day", day1);
                            mp.put("other", 0);
                            mp.put("arrival_time", arrival_time);
                            try {
                                String arrivalweek = getWeek(arrival_time);
                                mp.put("arrivalweek", arrivalweek);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else {
                            int day1 = daysBetween(nowtime, return_time);
                            mp.put("day", day1);
                            mp.put("other", 1);
                            mp.put("arrival_time", arrival_time);
                            try {
                                String arrivalweek = getWeek(arrival_time);
                                mp.put("arrivalweek", arrivalweek);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                    // if (end.before(now) && rs.getInt("work_states") == 2) {
                    if (order_states == 3) {
                        mp.put("arrival_time", arrival_time);
                        mp.put("confirm_time", rs.getString("confirm_time"));
                        mp.put("order_day", rs.getInt("order_day"));
                        mp.put("feedback", rs.getString("feedback"));
                        mp.put("isfeedback", rs.getInt("isfeedback"));
                        try {
                            String arrivalweek = getWeek(arrival_time);

                            String confirmweek = getWeek(rs.getString("confirm_time"));
                            mp.put("arrivalweek", arrivalweek);
                            mp.put("confirmweek", confirmweek);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return mp;
            }
        }, queryList.toArray());
        return list;
    }

    @SneakyThrows
    @Override
    public Map<String, Object> allOrder(String json) {

        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String somnus = jsonObject.getString("somnus");
        String monthFirst = jsonObject.getString("monthFirst");
        String monthLast = jsonObject.getString("monthLast");
        Integer bid = jsonObject.getInteger("bid");
        String service_type = jsonObject.getString("service_type");
        String timetype = jsonObject.getString("timetype");
        String origin = jsonObject.getString("origin");
        String channel = jsonObject.getString("channel");
        Integer work_states = jsonObject.getInteger("work_states");
        String number = jsonObject.getString("number");
        Integer start = jsonObject.getInteger("start");

        List<String> queryList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();

        //显示页面语句
        sb.append("select c.id,od.id as did,c.c_name,c.bid,c.origin,c.channel,od.service_type,od.timetype,od.level,od.work_states, ");
        sb.append("od.price,c.creat_time,od.service_day,od.starttime,od.endtime,od.onePrice");
        sb.append(" from yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");


        //显示页面总条数
        StringBuffer sb_count = new StringBuffer();
        sb_count.append(" select count(*) from  yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1");

        //其他订单类型计算语句
        StringBuffer sb_price = new StringBuffer();
        sb_price.append(" select c.id,od.work_states,od.starttime,od.endtime,od.onePrice,od.timetype,od.price,od.service_day");
        sb_price.append("  from  yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");




        //公共条件语句
        StringBuffer sb_public = new StringBuffer();
        //时间段搜索
        String sql_days = " and  (   (od.starttime>=?  and od.starttime<=?) or (od.endtime>=? and od.endtime<=?) or (?>=od.starttime and od.endtime>=?)  ) ";
        sb_public.append(sql_days);


        queryList.add(monthFirst);
        queryList.add(monthLast);
        queryList.add(monthFirst);
        queryList.add(monthLast);
        queryList.add(monthFirst);
        queryList.add(monthLast);


        if (somnus.equals("月子会所服务")) {
            sb_public.append("  and od.service_type='月子会所服务' ");
        }
        if (somnus.equals("居家服务")) {
            sb_public.append("  and od.service_type='居家服务' ");
        }

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        //当前时间
        String nowTime = time.format(new Date());

        sb_public.append(" and od.work_states>=0  and od.service_day<>0 ");


        //订单类型
        if (service_type != null) {
            sb_public.append(" and od.service_type=? ");
            queryList.add(service_type);
            if (timetype != null) {
                sb_public.append(" and od.timetype=? ");
                queryList.add(timetype);
            } else {
            }
        } else {

        }
        //母婴顾问
        if (bid != 0) {
            sb_public.append("  and c.bid=? ");
            queryList.add(bid.toString());
        } else {

        }
        //订单来源
        if (origin != null) {
            sb_public.append(" and c.origin=? ");
            queryList.add(origin);
            if (channel != null) {
                sb_public.append(" and c.channel=? ");
                queryList.add(channel);
            } else {

            }
        }
        //订单进度
        if (work_states != 10) {
            sb_public.append(" and od.work_states=? ");
            queryList.add(work_states.toString());
        } else {

        }

        //客户手机号，姓名，id
        if (number != null) {
            //数字id或手机号
            if (number.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
                if (number.length() > 8) {
                    //电话
                    sb_public.append(" and c.c_phone = ? ");
                    queryList.add(number);
                } else {
                    sb_public.append(" and od.id = ? ");
                    queryList.add(number);
                }
            }
            //汉字
            if (number.matches("^[\\u4e00-\\u9fa5]+$")) {

                String sql2 = "select c_name from yx_custom c left join yx_order_demand od on (c.id=od.cid) where c.c_name = ?  and od.work_states>=0 and od.service_day<>0  ";
                try {
                    List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{number}, String.class);
                    if (number.equals(list1.get(0))) {
                        sb_public.append("  and c.c_name= ?  ");
                        queryList.add(list1.get(0));
                    } else {

                    }
                } catch (Exception e) {
                    if (number.length() >= 1) {
                        String name = number.substring(0, 1);
                        sb_public.append("  and c.c_name like ?   ");
                        queryList.add(name + "%");
                    } else {

                    }

                }
            }

        } else {

        }
        //公共条件语句结束


        //会所类型语句
        List<String> clubList = new ArrayList<>();

        String club_all_club = "";//会所全部
        clubList.add(club_all_club);
        String club_hospital = " and od.timetype='医院服务' ";//会所医院
        clubList.add(club_hospital);
        String club_club = " and od.timetype='月子会所服务' ";//会所会所
        clubList.add(club_club);
        String club_all = " and od.timetype='全程' ";//会所全程
        clubList.add(club_all);
         List<Map<String,Object>> club = new ArrayList<>();

        for (int i = 0; i <clubList.size() ; i++) {
            StringBuffer sb_init = new StringBuffer();
            sb_init.reverse();//初始化
            sb_init.append(sb_price);//添加搜索语句
            sb_init.append(sb_public);//添加条件语句
            sb_init.append(clubList.get(i));

            List<Map<String, Object>> sql_clubList = this.jdbcTemplate.query(sb_init.toString(), new RowMapper<Map<String, Object>>() {
                    @SneakyThrows
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        String start_time = rs.getString("starttime");
                        String end_time = rs.getString("endtime");

                        //全程里 医院部分订单
                        String sql_hospital = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAA%'";
                        List<Map<String, Object>> hospitalList = jdbcTemplate.queryForList(sql_hospital, rs.getInt("id"));
                        //全程里 会所部分订单
                        String sql_club = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAB%'";
                        List<Map<String, Object>> clubList = jdbcTemplate.queryForList(sql_club, rs.getInt("id"));

                        Integer workStates = rs.getInt("work_states");
                        String timeType = rs.getString("timetype");

                        List<String> dayList = getDayList(workStates,monthFirst, monthLast,start_time,end_time,nowTime);
                        Map<String,Object> priceMap=getClubPrice(timeType,workStates,hospitalList,clubList,dayList);

                        mp.put("hospitalDay",priceMap.get("hospitalDay") );
                        mp.put("clubDay",priceMap.get("clubDay") );
                        mp.put("trueHospitalPrice", priceMap.get("trueHospitalPrice"));
                        mp.put("trueClubPrice", priceMap.get("trueClubPrice"));
                        return mp;
                    }
                }, queryList.toArray());

            Integer sumTruePrice = 0;//一个时间段金额
            Integer sumOrderDay = 0;//一个时间段天数
            for (int j = 0; j <sql_clubList.size() ; j++) {
                    Integer hospitalDay = Integer.valueOf(sql_clubList.get(j).get("hospitalDay").toString());//单个医院天数
                    Integer trueHospitalPrice = Integer.valueOf(sql_clubList.get(j).get("trueHospitalPrice").toString());//单个医院实收金额
                    Integer clubDay = Integer.valueOf(sql_clubList.get(j).get("clubDay").toString());//单个会所天数
                    Integer trueClubPrice = Integer.valueOf(sql_clubList.get(j).get("trueClubPrice").toString());//单个会所实收金额

                sumTruePrice = trueHospitalPrice + trueClubPrice + sumTruePrice;
                sumOrderDay = hospitalDay + clubDay + sumOrderDay;
            }
            Map<String,Object> clubMap=new HashedMap();
            clubMap.put("sumTruePrice", sumTruePrice);
            clubMap.put("sumOrderDay", sumOrderDay);
            clubMap.put("count",sql_clubList.size());
            club.add(clubMap);
        }
        map.put("club",club);


        //居家类型语句
        List<String> homeList = new ArrayList<>();
        String home_all = "";//居家全部
        homeList.add(home_all);
        String home_short = " and od.timetype like '短期%' ";
        homeList.add(home_short);
        String home_long = " and od.timetype='长期' ";//居家长期
        homeList.add(home_long);

        //居家价格，天数，订单数

        //返回数据显示
        List<Map<String, Object>> home = new ArrayList<>();
        for (int i = 0; i <homeList.size() ; i++) {
            StringBuffer sb_init = new StringBuffer();
            sb_init.reverse();//初始化
            sb_init.append(sb_price);//添加搜索语句
            sb_init.append(sb_public);//添加条件语句
            sb_init.append(homeList.get(i));
                List<Map<String, Object>> sql_homeList = this.jdbcTemplate.query(sb_init.toString(), new RowMapper<Map<String, Object>>() {
                    @SneakyThrows
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        String start_time = rs.getString("starttime");
                        String end_time = rs.getString("endtime");

                        Integer onePrice = 0;//单价
                        if (rs.getInt("onePrice") != 0&&!rs.getString("timetype").equals("长期")) {
                            onePrice = rs.getInt("onePrice");
                        } else {
                            onePrice = rs.getInt("price") / rs.getInt("service_day");
                        }
                        Integer workStates = rs.getInt("work_states");

                        List<String> dayList = getDayList(workStates,monthFirst, monthLast,start_time,end_time,nowTime);
                        Integer orderDay=dayList.size();
                        Integer truePrice =orderDay*onePrice;
                        mp.put("orderDay", orderDay);
                        mp.put("truePrice", truePrice);

                        return mp;
                    }

                }, queryList.toArray());

                Integer sumTruePrice = 0;//一个时间段金额
                Integer sumOrderDay = 0;//一个时间段天数
            for (int j = 0; j <sql_homeList.size() ; j++) {
                    //单个实收金额
                    Integer truePrice = Integer.valueOf(sql_homeList.get(j).get("truePrice").toString());//单个实收金额
                    //单个天数
                    Integer orderDay = Integer.valueOf(sql_homeList.get(j).get("orderDay").toString());
                sumTruePrice = truePrice + sumTruePrice;
                sumOrderDay = orderDay + sumOrderDay;
            }

            Map<String,Object> homeMap=new HashedMap();
            homeMap.put("sumTruePrice", sumTruePrice);
            homeMap.put("sumOrderDay", sumOrderDay);
            homeMap.put("count",sql_homeList.size());
            home.add(homeMap);
        }

        map.put("home",home);

        sb.append(sb_public);//搜索显示语句添加公共条件语句
        sb_count.append(sb_public);//显示条数添加公共条件语句

        sb.append(" order by c.creat_time desc");
        System.out.println("queryList = " + queryList);
        //条数
        Integer count = this.jdbcTemplate.queryForObject(sb_count.toString(), Integer.class, queryList.toArray());
        Map<String, Object> page = new HashMap<String, Object>();
        List<Map<String, Object>> pageList = new ArrayList<>();
        Map<String, Object> pageMap = new HashMap<String, Object>();
        page = getAllPage(count, start);
        pageMap.put("count", count);//搜索出来的客户总人数
        String sql_page = page.get("sql_page").toString();
        sb.append(sql_page);
        pageMap.put("page", page.get("page"));
        pageMap.put("num", page.get("num"));

        System.out.println("sb = " + sb);
        System.out.println("queryList = " + queryList);

        List<Map<String, Object>> list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();

                mp.put("c_origin", rs.getString("origin") + rs.getString("channel"));
                mp.put("cid", rs.getString("id"));
                mp.put("did", rs.getString("did"));
                mp.put("c_name", rs.getString("c_name"));

                String sql_bname = "select name from yx_user where id=?";
                String bname = null;
                if (rs.getInt("bid") == 0) {
                    bname = "未知";
                } else {
                    bname = jdbcTemplate.queryForObject(sql_bname, String.class, rs.getInt("bid"));
                }
                mp.put("bname", bname);

                mp.put("c_origin", (rs.getString("origin") + rs.getString("channel")));

                if (rs.getString("service_type").equals("月子会所服务")) {
                    mp.put("order_type", rs.getString("service_type") + rs.getString("timetype"));
                } else {
                    mp.put("order_type", rs.getString("service_type") + rs.getString("timetype") + rs.getString("level"));
                }

                if (rs.getInt("work_states") == 0) {
                    mp.put("order_speed", "待服务");
                }
                if (rs.getInt("work_states") == 1) {
                    mp.put("order_speed", "服务中");
                }
                if (rs.getInt("work_states") == 2) {
                    mp.put("order_speed", "已完成");
                }

                mp.put("service_day", rs.getInt("service_day"));
                mp.put("price", rs.getInt("price"));

                String sql_service_count = "select COUNT(DISTINCT(mid)) from yx_order where mid<>0 and cid=?";
                Integer service_count = jdbcTemplate.queryForObject(sql_service_count, Integer.class, rs.getInt("id"));
                mp.put("service_count", service_count);

                String start_time = rs.getString("starttime");
                String end_time = rs.getString("endtime");
                mp.put("start_time", start_time);
                mp.put("end_time", end_time);

//                String sql_time="select stop_time,run_time from yx_order where cid=? and stop_time is not null ";
//                List<Map<String,Object>> timeList=jdbcTemplate.queryForList(sql_time,rs.getInt("id"));
//                //订单暂停天数没有计算在内
//                Integer stopDay=0;//暂停时间
//                if (timeList.size()>0){
//
//                    for (int i = 0; i <timeList.size() ; i++) {
//                        Object stop_time=timeList.get(i).get("stop_time");
//                        Object run_time=timeList.get(i).get("run_time");
//                        if (stop_time!=null&&run_time!=null){
//                            //有暂停，开启时间集合
//                            String stop = stop_time.toString();
//                            String run = run_time.toString();
//                            List<String> stoplist = new ArrayList<>();
//                            List<String> runlist = new ArrayList<>();
//                            stoplist = JSONArray.fromObject(stop);
//                            runlist = JSONArray.fromObject(run);
//                            for (int j = 0; j <stoplist.size() ; j++) {
//                                if (isMonth(stoplist.get(j),nowTime)==1&&isMonth(stoplist.get(j),nowTime)==1){
//
//                                }
//
//                            }
//
//                        }
//                        if (stop_time!=null&&run_time==null){
//                            //有暂停时间，没有开启时间
//                        }
//
//                    }
//
//                }else {
//
//                }

                Integer workStates = rs.getInt("work_states");
                List<String> dayList = getDayList(workStates,monthFirst, monthLast,start_time,end_time,nowTime);

                //月子会所服务
                if (somnus.equals("月子会所服务")) {

                    String timeType = rs.getString("timetype");
                    //全程里 医院部分订单
                    String sql_hospital = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAA%'";
                    List<Map<String, Object>> hospitalList = jdbcTemplate.queryForList(sql_hospital, rs.getInt("id"));
                    //全程里 会所部分订单
                    String sql_club = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAB%'";
                    List<Map<String, Object>> clubList = jdbcTemplate.queryForList(sql_club, rs.getInt("id"));

                    Map<String,Object> priceMap=getClubPrice(timeType,workStates,hospitalList,clubList,dayList);

                    mp.put("hospitalDay",priceMap.get("hospitalDay") );
                    mp.put("clubDay",priceMap.get("clubDay") );
                    mp.put("trueHospitalPrice", priceMap.get("trueHospitalPrice"));
                    mp.put("trueClubPrice", priceMap.get("trueClubPrice"));
                }
                if (somnus.equals("居家服务")) {
                    Integer onePrice = 0;//单价
                    if (rs.getInt("onePrice") != 0&&!rs.getString("timetype").equals("长期")) {
                        onePrice = rs.getInt("onePrice");
                    } else {
                        onePrice = rs.getInt("price") / rs.getInt("service_day");
                    }
                 
                    Integer orderDay=dayList.size();
                    Integer truePrice =orderDay*onePrice;
                    mp.put("orderDay", orderDay);
                    mp.put("truePrice", truePrice);
                }

                mp.put("creat_time", rs.getString("creat_time"));
                return mp;
            }
        }, queryList.toArray());
        pageMap.put("list", list);
        pageList.add(pageMap);

        map.put("pageList", pageList);
        return map;
    }


    @Override
    public Map<String, Object> orderCondition() {
        Map<String, Object> map = new HashMap<>();

        //客户来源
        List<Map<String, Object>> originList = new ArrayList<>();
        Map<String, Object> OnlineMap = new HashMap<>();
        String sql_OnlineList = "select name from yx_channel where type=1";
        List<Map<String, Object>> OnlineList = this.jdbcTemplate.queryForList(sql_OnlineList);
        OnlineMap.put("name", "线上");
        OnlineMap.put("children", OnlineList);
        Map<String, Object> UnderlineMap = new HashMap<>();
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
        List<Map<String, Object>> childrenList = new ArrayList<>();
        for (int i = 0; i < namelist.size(); i++) {
            channellist = (List) this.jdbcTemplate.queryForList(sql_channel, namelist.get(i));
            Map<String, Object> childrenMap = new HashMap<>();
            if (channellist.size() > 1) {
                //取channellist的第一个最二级分类
                childrenMap.put("name", channellist.get(0).get("name"));
                List<Map<String, Object>> zeroList = new ArrayList<Map<String, Object>>();
                zeroList.add(childrenMap);
                channellist.removeAll(zeroList);
                childrenMap.put("children", channellist);
                childrenList.add(childrenMap);
            } else {
                //channellist.size()为1时，没有下一级
                childrenMap.put("name", channellist.get(0).get("name"));
                childrenList.add(childrenMap);
            }

        }
        UnderlineMap.put("name", "线下");
        UnderlineMap.put("children", childrenList);
        originList.add(OnlineMap);
        originList.add(UnderlineMap);
        map.put("originList", originList);

        //母婴顾问
        String sql_bname = "select id,name from yx_user where power=4";
        List<Map<String, Object>> bnameList = this.jdbcTemplate.queryForList(sql_bname);
        map.put("bnameList", bnameList);

        //居家订单类型
        String sql_home = "select id,name from yx_orderType where stype=2";
        List<Map<String, Object>> hoList = this.jdbcTemplate.queryForList(sql_home);
        List<Map<String, Object>> homeList = new ArrayList<>();
        for (int i = 0; i < hoList.size(); i++) {
            Map<String, Object> homeMap = new HashMap<>();
            homeMap.put("id", hoList.get(i).get("id").toString());
            homeMap.put("name", "居家服务-" + hoList.get(i).get("name"));
            homeList.add(homeMap);
        }
        map.put("homeList", homeList);

        //月子会所服务订单类型
        String sql_club = "select id,name from yx_orderType where stype=1";
        List<Map<String, Object>> cuList = this.jdbcTemplate.queryForList(sql_club);
        List<Map<String, Object>> culbList = new ArrayList<>();
        for (int i = 0; i < cuList.size(); i++) {
            Map<String, Object> culbMap = new HashMap<>();
            culbMap.put("id", cuList.get(i).get("id").toString());
            culbMap.put("name", "月子会所服务-" + cuList.get(i).get("name"));
            culbList.add(culbMap);
        }
        map.put("culbList", culbList);

        //订单进度
        List<Map<String, Object>> orderSpeedList = new ArrayList<>();
        Map<String, Object> orderSpeed = new HashMap<>();
        Map<String, Object> orderSpeed1 = new HashMap<>();
        Map<String, Object> orderSpeed2 = new HashMap<>();
        orderSpeed.put("id", 0);
        orderSpeed.put("name", "待服务");
        orderSpeed1.put("id", 1);
        orderSpeed1.put("name", "服务中");
        orderSpeed2.put("id", 2);
        orderSpeed2.put("name", "已完成");
        orderSpeedList.add(orderSpeed);
        orderSpeedList.add(orderSpeed1);
        orderSpeedList.add(orderSpeed2);
        map.put("orderSpeedList", orderSpeedList);
        return map;

    }


//    @Override
//    public Map<String, Object> orderCondition() {
//        Map<String, Object> map = new HashedMap();
//        Integer cid = 1444;
//        String sql_time = "select stop_time,run_time from  yx_order where cid=? ";
//        List<Map<String, Object>> timeList = this.jdbcTemplate.queryForList(sql_time, cid);
//        Integer sum_stop = 0;//休息总天数
//        Integer   stop_run=0;
//        for (int i = 0; i < timeList.size(); i++) {
//            if (timeList.get(i).get("stop_time") != null && timeList.get(i).get("run_time") != null) {
//                //有暂停时
//                String stop = timeList.get(i).get("stop_time").toString();
//                String run = timeList.get(i).get("run_time").toString();
//                List<String> stoplist = new ArrayList<>();
//                List<String> runlist = new ArrayList<>();
//                stoplist = JSONArray.fromObject(stop);
//                runlist = JSONArray.fromObject(run);
//                for (int j = 0; j <stoplist.size() ; j++) {
//                    try {
//                        stop_run = stop_run + daysBetween(stoplist.get(j), runlist.get(j));
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                }
//                sum_stop=sum_stop+stop_run;
//            } else {
//                //没有暂停
//                sum_stop = sum_stop + 0;
//            }
//
//
//        }
//
//        System.out.println("stop_run = " + stop_run);
//        return null;
//    }


    @Override
    public Map<String, Object> homeAndClub(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer bid = jsonObject.getInteger("bid");
        String somnus = jsonObject.getString("somnus");

        Map<String, Object> map = new HashedMap();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowTime = time.format(new Date());//当前时间

        String monthFirst = getFirstDay(nowTime);//当前时间这个月第一天


        StringBuffer sb = new StringBuffer();
        sb.append(" select c.id,od.work_states,od.starttime,od.endtime,od.onePrice,od.timetype,od.price,od.service_day");
        sb.append("  from  yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");
        sb.append(" and od.work_states>=0  and od.service_day<>0 ");

        //总条件
        List<List> queryList = new ArrayList<>();

        List<String> queryList1=new ArrayList<>();
        List<String> queryList2=new ArrayList<>();

        String sql_id = "select power from yx_user where id=? ";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, bid);
        if (power == 20 || power == 100) {

        } else {
            sb.append(" and c.bid =? ");
            queryList1.add(bid.toString());
            queryList2.add(bid.toString());
        }
        Integer type = 0;
        if (somnus.equals("home")) {
            type = 0;
            sb.append("  and od.service_type='居家服务' ");
        }
        if (somnus.equals("club")) {
            type = 1;
            sb.append("  and od.service_type='月子会所服务' ");
        }


        // 居家本月
        sb.append( " and (  (od.starttime>=? and od.starttime<=?) or (od.endtime>=? and od.endtime<=?)  or (?>=od.starttime and od.endtime>=?)  ) ");


        queryList1.add(monthFirst);
        queryList1.add(nowTime);
        queryList1.add(monthFirst);
        queryList1.add(nowTime);
        queryList1.add(monthFirst);
        queryList1.add(nowTime);
        queryList.add(queryList1);

        //当前时间的昨天
        String yesterdayTime = getNewEndtime(nowTime, 1);

        queryList2.add(yesterdayTime);
        queryList2.add(yesterdayTime);
        queryList2.add(yesterdayTime);
        queryList2.add(yesterdayTime);
        queryList2.add(yesterdayTime);
        queryList2.add(yesterdayTime);
        queryList.add(queryList2);

        if (type == 0) {
            //居家价格，天数，订单数
       
            List<Map<String,Object>> home=new ArrayList<>();
            //居家价格，天数，订单数
            System.out.println("home = " + home);
            System.out.println("queryList = " + queryList.size());
            for (int i = 0; i <queryList.size(); i++) {
               
                StringBuffer sb_init = new StringBuffer();
                sb_init.reverse();//初始化
                sb_init.append(sb);//添加搜索语句
                int finalI = i;

                List<Map<String, Object>> sql_homeList = this.jdbcTemplate.query(sb_init.toString(), new RowMapper<Map<String, Object>>() {
                    @SneakyThrows
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        String start_time = rs.getString("starttime");
                        String end_time = rs.getString("endtime");

                        Integer onePrice = 0;//单价
                        if (rs.getInt("onePrice") != 0&&!rs.getString("timetype").equals("长期")) {
                            onePrice = rs.getInt("onePrice");
                        } else {
                            onePrice = rs.getInt("price") / rs.getInt("service_day");
                        }
                        Integer workStates = rs.getInt("work_states");

                        String m_first= queryList.get(finalI).get(queryList.get(finalI).size()-2).toString();
                        String m_last= queryList.get(finalI).get(queryList.get(finalI).size()-1).toString();

                        List<String> dayList = getDayList(workStates,m_first, m_last,start_time,end_time,nowTime);
                        Integer orderDay=dayList.size();
                        Integer truePrice=orderDay*onePrice;
                        mp.put("orderDay", orderDay);
                        mp.put("truePrice", truePrice);

                        return mp;
                    }
                }, queryList.get(i).toArray());
                Integer sumTruePrice = 0;//一个时间段金额
                Integer sumOrderDay = 0;//一个时间段天数
                for (int j = 0; j <sql_homeList.size() ; j++) {
                    //单个实收金额
                    Integer truePrice = Integer.valueOf(sql_homeList.get(j).get("truePrice").toString());//单个实收金额
                    //单个天数
                    Integer orderDay = Integer.valueOf(sql_homeList.get(j).get("orderDay").toString());
                    sumTruePrice = truePrice + sumTruePrice;
                    sumOrderDay = orderDay + sumOrderDay;
                }

                Map<String,Object> homeMap=new HashedMap();
                homeMap.put("sumTruePrice", sumTruePrice);
                homeMap.put("sumOrderDay", sumOrderDay);
                homeMap.put("count",sql_homeList.size());
                home.add(homeMap);
            }
            map.put("home",home);
        }
        if (type == 1) {

            //会所价格，天数，订单数
            List<Map<String,Object>> club=new ArrayList<>();
            for (int i = 0; i <queryList.size(); i++) {
                int finalI = i;
                StringBuffer sb_init = new StringBuffer();
                sb_init.reverse();//初始化
                sb_init.append(sb);//添加搜索语句
                List<Map<String, Object>> sql_clubList = this.jdbcTemplate.query(sb_init.toString(), new RowMapper<Map<String, Object>>() {
                    @SneakyThrows
                    @Override
                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                        Map<String, Object> mp = new HashMap<String, Object>();
                        String start_time = rs.getString("starttime");
                        String end_time = rs.getString("endtime");

                        //全程里 医院部分订单
                        String sql_hospital = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAA%'";
                        List<Map<String, Object>> hospitalList = jdbcTemplate.queryForList(sql_hospital, rs.getInt("id"));
                        //全程里 会所部分订单
                        String sql_club = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAB%'";
                        List<Map<String, Object>> clubList = jdbcTemplate.queryForList(sql_club, rs.getInt("id"));

                        Integer workStates = rs.getInt("work_states");
                        String timeType = rs.getString("timetype");

                        String m_first= queryList.get(finalI).get(queryList.get(finalI).size()-2).toString();
                        String m_last= queryList.get(finalI).get(queryList.get(finalI).size()-1).toString();

                        List<String> dayList = getDayList(workStates,m_first, m_last,start_time,end_time,nowTime);
                        Map<String,Object> priceMap=getClubPrice(timeType,workStates,hospitalList,clubList,dayList);

                        mp.put("hospitalDay",priceMap.get("hospitalDay") );
                        mp.put("clubDay",priceMap.get("clubDay") );
                        mp.put("trueHospitalPrice", priceMap.get("trueHospitalPrice"));
                        mp.put("trueClubPrice", priceMap.get("trueClubPrice"));
                        return mp;
                    }
                }, queryList.get(i).toArray());

                Integer sumTruePrice = 0;//一个时间段金额
                Integer sumOrderDay = 0;//一个时间段天数
                for (int j = 0; j <sql_clubList.size() ; j++) {
                    Integer hospitalDay = Integer.valueOf(sql_clubList.get(j).get("hospitalDay").toString());//单个医院天数
                    Integer trueHospitalPrice = Integer.valueOf(sql_clubList.get(j).get("trueHospitalPrice").toString());//单个医院实收金额
                    Integer clubDay = Integer.valueOf(sql_clubList.get(j).get("clubDay").toString());//单个会所天数
                    Integer trueClubPrice = Integer.valueOf(sql_clubList.get(j).get("trueClubPrice").toString());//单个会所实收金额

                    sumTruePrice = trueHospitalPrice + trueClubPrice + sumTruePrice;
                    sumOrderDay = hospitalDay + clubDay + sumOrderDay;
                }
                Map<String,Object> clubMap=new HashedMap();
                clubMap.put("sumTruePrice", sumTruePrice);
                clubMap.put("sumOrderDay", sumOrderDay);
                clubMap.put("count",sql_clubList.size());
                club.add(clubMap);

            }
           map.put("club",club);
        }

        return map;
    }

}
