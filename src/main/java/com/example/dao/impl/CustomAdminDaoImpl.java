package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.CustomAdminDao;
import com.example.entiy.*;
import com.example.service.CustomService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import groovy.util.IFileNameFinder;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.ELState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.BinaryClient;

import javax.transaction.Transactional;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.daysBetween;
import static com.example.util.MonthUtil.timeSize;
import static com.example.util.NumberUtil.*;
import static com.example.util.PageUtil.getAllPage;
import static com.example.util.PageUtil.getPage;
import static com.example.util.Year.getAge;

@Repository
public class CustomAdminDaoImpl implements CustomAdminDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;
    @Autowired(required = false)
    private CustomService customService;

    @SneakyThrows
    @Override
    public Map<String, Object> getAllCustom(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        Map<String, Object> map = new HashMap<>();
        Map<String, Object> pageMap = new HashMap<>();
        List<Map<String, Object>> pageList = new ArrayList<>();
        Integer uid = jsonObject.getInteger("uid");//用户id
        String origin = jsonObject.getString("origin");//客户来源
        String origin_channel = jsonObject.getString("origin_channel");//客户渠道
        String intention = jsonObject.getString("intention");//意向类型
        Integer sname = jsonObject.getInteger("sname");//咨询顾问
        Integer bname = jsonObject.getInteger("bname");//母婴顾问
        String orderSpeed = jsonObject.getString("orderSpeed");//订单进度
        String service_type = jsonObject.getString("service_type");//服务类型
        String timetype = jsonObject.getString("timetype");//服务类型
        String level = jsonObject.getString("level");//服务类型
        String returnType = jsonObject.getString("returnType");//回岗类型
        String number = jsonObject.getString("number");//客户手机号，姓名，编号
        Integer start = jsonObject.getInteger("start");//当前页数
        String powerFiled = jsonObject.getString("powerFiled");//权限字段
        String start_time = jsonObject.getString("start_time");//开始时间
        String end_time = jsonObject.getString("end_time");//结束时间
        System.out.println("powerFiled = " + powerFiled);
        end_time = getNewEndtime(end_time, -1);
        System.out.println("start_time = " + start_time);
        System.out.println("end_time = " + end_time);

        List<Map<String, Object>> powerFiledList = new ArrayList<>();
        List<Map<String, Object>> conditionList = new ArrayList<>();//条件列表集合
        Map<String, Object> conditionMap = new HashMap<>();

        if (powerFiled.isEmpty()) {
            //第一次请求powerFiled为空，查询用户的权限字段
            String sql_power = "select cid,c_name,start,c_phone,w_name,origin,address,creat_time,intention,followCount,sname,bname,orderSpeed,orderType,service_day,service_count,returnType,satisfied,price,orderDay,truePrice from yx_powerField where lid=? ";
            List<Map<String, Object>> powerFiledList1 = new ArrayList<>();
            powerFiledList1 = this.jdbcTemplate.queryForList(sql_power, uid);
            System.out.println("powerFiledList1____1______" + powerFiledList1);
            Map<String, Object> powerFiledMap = new LinkedHashMap<>();
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
            System.out.println("powerFiled = " + powerFiled);
            List<Map<String, Object>> powerFiledList1 = new ArrayList<>();
            powerFiledList1 = (List) jsonArray;
            System.out.println("powerFiledList_____2_____" + powerFiledList1);
            Map<String, Object> powerFiledMap = new LinkedHashMap();//保证put值有序
            for (int i = 0; i < powerFiledList1.size(); i++) {
                for (String key : powerFiledList1.get(i).keySet()) {
                    Integer value = Integer.valueOf(powerFiledList1.get(i).get(key).toString());
                    powerFiledMap.put(key, value);
                }
            }
            powerFiledList.add(powerFiledMap);

        }
        map.put("powerFiledList", powerFiledList);
        //条件显示的集合
        Map<String, Object> allCondition = getAllCondition();

        //判断客户来源是否显示
        if (powerFiledList.get(0).get("intention") != null) {
            if (Integer.valueOf(powerFiledList.get(0).get("origin").toString()) != 0) {
                conditionMap.put("originList", allCondition.get("originList"));
            } else {

            }
        } else {

        }
        //判断意向类型是否显示
        if (powerFiledList.get(0).get("intention") != null) {
            if (Integer.valueOf(powerFiledList.get(0).get("intention").toString()) != 0) {
                List<Map<String, Object>> intentionList = new ArrayList<>();
                Map<String, Object> intentionMap1 = new HashMap<>();
                Map<String, Object> intentionMap2 = new HashMap<>();
                Map<String, Object> intentionMap3 = new HashMap<>();
                Map<String, Object> intentionMap4 = new HashMap<>();
                intentionMap1.put("name", "咨询");
                intentionMap2.put("name", "到店");
                intentionMap3.put("name", "签单");
                intentionMap4.put("name", "无效");
                intentionList.add(intentionMap1);
                intentionList.add(intentionMap2);
                intentionList.add(intentionMap3);
                intentionList.add(intentionMap4);
                conditionMap.put("intention", intentionList);
            } else {

            }
        } else {

        }

        //判断咨询顾问是否显示
        if (powerFiledList.get(0).get("sname") != null) {
            if (Integer.valueOf(powerFiledList.get(0).get("sname").toString()) != 0) {
                conditionMap.put("sname", allCondition.get("snameList"));
            } else {

            }
        } else {
        }

        //判断母婴顾问是否显示
        if (powerFiledList.get(0).get("bname") != null) {
            if (Integer.valueOf(powerFiledList.get(0).get("bname").toString()) != 0) {
                conditionMap.put("bname", allCondition.get("bnameList"));
            } else {

            }
        } else {

        }
        //判断订单进度是否显示
        if (powerFiledList.get(0).get("orderSpeed") != null) {
            if (Integer.valueOf(powerFiledList.get(0).get("orderSpeed").toString()) != 0) {
                List<Map<String, Object>> orderSpeedList = new ArrayList<>();
                Map<String, Object> orderSpeedMap1 = new HashMap<>();
                Map<String, Object> orderSpeedMap2 = new HashMap<>();
                Map<String, Object> orderSpeedMap3 = new HashMap<>();
                Map<String, Object> orderSpeedMap4 = new HashMap<>();
                Map<String, Object> orderSpeedMap5 = new HashMap<>();
                orderSpeedMap1.put("name", "咨询中");
                orderSpeedMap2.put("name", "匹配中");
                orderSpeedMap3.put("name", "待服务");
                orderSpeedMap4.put("name", "服务中");
                orderSpeedMap5.put("name", "已完成");
                orderSpeedList.add(orderSpeedMap1);
                orderSpeedList.add(orderSpeedMap2);
                orderSpeedList.add(orderSpeedMap3);
                orderSpeedList.add(orderSpeedMap4);
                orderSpeedList.add(orderSpeedMap5);
                conditionMap.put("orderSpeed", orderSpeedList);
            } else {

            }
        } else {

        }
        //判断订单类型是否显示
        if (powerFiledList.get(0).get("orderType") != null) {
            if (Integer.valueOf(powerFiledList.get(0).get("orderType").toString()) != 0) {
                conditionMap.put("orderTypeList", allCondition.get("orderTypeList"));
            } else {
            }
        } else {

        }
        //判断回岗类型是否显示
        if (powerFiledList.get(0).get("returnType") != null) {
            if (Integer.valueOf(powerFiledList.get(0).get("returnType").toString()) != 0) {
                List<Map<String, Object>> returnTypeList = new ArrayList<>();
                Map<String, Object> returnTypeMap1 = new HashMap<>();
                Map<String, Object> returnTypeMap2 = new HashMap<>();
                Map<String, Object> returnTypeMap3 = new HashMap<>();
                returnTypeMap1.put("name", "正常回岗");
                returnTypeMap2.put("name", "续单回岗");
                returnTypeMap3.put("name", "退单回岗");
                returnTypeList.add(returnTypeMap1);
                returnTypeList.add(returnTypeMap2);
                returnTypeList.add(returnTypeMap3);
                conditionMap.put("returnType", returnTypeList);

            } else {

            }
        } else {

        }
        conditionList.add(conditionMap);
        map.put("conditionList", conditionList);


        List<String> sqlDayList = new ArrayList<>();
        StringBuffer sb_day = new StringBuffer();
        //公共语句
        sb_day.append("select count(*) from yx_custom where ");
        //本年
        String sql_nowYear = " YEAR(creat_time)=YEAR(NOW()) ";
        sqlDayList.add(sql_nowYear);
        //当天
        String sql_nowDay = " to_days(creat_time) = to_days(now()) ";
        sqlDayList.add(sql_nowDay);
        //本周
        String sql_nowWeek = " YEARWEEK(date_format(creat_time,'%Y-%m-%d'),1) = YEARWEEK(now(),1) ";
        sqlDayList.add(sql_nowWeek);
        //本月
        String sql_nowMonth = " DATE_FORMAT( creat_time, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )";
        sqlDayList.add(sql_nowMonth);

        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        String sql_custom_count = "select count from yx_customSet where type=?";

        List<Map<String, Object>> dayList = new ArrayList<>();
        for (int i = 0; i < sqlDayList.size(); i++) {
            Map<String, Object> dayMap = new HashMap<>();
            StringBuffer sb_init = new StringBuffer();
            sb_init.reverse();//初始化
            sb_init.append(sb_day);//添加搜索语句
            sb_init.append(sqlDayList.get(i));//添加条件语句
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
        System.out.println("dayList = " + dayList);
        map.put("dayList", dayList);

        //当前时间
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time1.format(new Date());
        String sql_seven_count = "select count(*) from yx_custom where creat_time like ?";

        List<Map<String, Object>> sevenList = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            Map<String, Object> sevenMap = new HashMap<>();
            String seven_time = getNewEndtime(nowTime, i);
            Integer count = jdbcTemplate.queryForObject(sql_seven_count, Integer.class, seven_time + "%");
            String new_seven_time = seven_time.substring(5).replaceAll("-", "/");
            sevenMap.put("time", new_seven_time);
            sevenMap.put("count", count);
            sevenList.add(sevenMap);
        }
        map.put("sevenList", sevenList);

        StringBuffer sb = new StringBuffer();//sql语句
        StringBuffer sb_count = new StringBuffer();//查询的条数
        List<Object> queryList = new ArrayList<Object>();
        Integer flag = 0;//判断后面的语句是否需要加分组

        String powerSql = " and d.service_type='居家服务' ";


        if (service_type.isEmpty() && bname == 0 && orderSpeed.isEmpty() && returnType.isEmpty() && intention.isEmpty() && origin.isEmpty() && sname == 0) {
            //所有条件为空
            //
            System.out.println(" sb_______1");

            if (uid == 173) {
                flag = 1;
                sb.append("select MAX(o.id) as oid,c.id,c.sid,c.bid,c.origin,c.channel,c.c_name,c.w_name,c.c_phone,c.production_situation,c.nodate,c.production_date,c.creat_time,");
                sb.append("c.address ");
                sb.append(" from yx_custom c left join yx_order_demand d on(c.id=d.cid) left join yx_order o on(c.id=o.cid) ");
                sb.append("where 1=1");
                sb.append(powerSql);

                sb_count.append("select count(DISTINCT(c.id)) from yx_custom c left join yx_order_demand d on(c.id=d.cid) left join yx_order o on(c.id=o.cid) ");
                sb_count.append("where 1=1");
                sb_count.append(powerSql);
            } else {
                flag = 0;

                sb.append("select c.id,c.sid,c.bid,c.origin,c.channel,c.c_name,c.w_name,c.c_phone,c.production_situation,c.nodate,c.production_date,c.creat_time,c.address");
                sb.append(" from yx_custom c where 1=1 ");

                sb_count.append("select count(DISTINCT(c.id)) from yx_custom c where 1=1 ");

            }

        } else {
            //订单类型service_type/timetype/level，母婴顾问banme，订单进度orderSpeed，回岗类型returnType，
            //条件可空可不空，

            flag = 1;
            if (intention.isEmpty()) {
                //意向类型intention为空
                System.out.println("意向类型intention为空___");
                sb.append("select MAX(o.id) as oid,c.id,c.sid,c.bid,c.origin,c.channel,c.c_name,c.w_name,c.c_phone,c.production_situation,c.nodate,c.production_date,c.creat_time,");
                sb.append("c.address ");
                sb.append(" from yx_custom c left join yx_order_demand d on(c.id=d.cid) left join yx_order o on(c.id=o.cid) ");
                sb.append("where 1=1");

                sb_count.append("select count(DISTINCT(c.id)) from yx_custom c left join yx_order_demand d on(c.id=d.cid) left join yx_order o on(c.id=o.cid) ");
                sb_count.append("where 1=1");

            } else {

                sb.append("select MAX(o.id) as oid,c.id,c.sid,c.bid,c.origin,c.channel,c.c_name,c.w_name,c.c_phone,c.production_situation,c.nodate,c.production_date,c.creat_time,");
                sb.append("c.address ");
                sb.append(" from yx_custom c left join yx_order_demand d on(c.id=d.cid) left join yx_order o on(c.id=o.cid) ");
                sb.append(" left join yx_saleOrder s on (c.id=s.cid)");
                sb.append(" where  s.id=(SELECT MAX(id) from yx_saleOrder WHERE yx_saleOrder.cid=c.id) ");
                sb.append(" and s.confirmIntention=? ");

                sb_count.append("select count(DISTINCT(c.id)) from yx_custom c left join yx_order_demand d on(c.id=d.cid) left join yx_order o on(c.id=o.cid) ");
                sb_count.append(" left join yx_saleOrder s on (c.id=s.cid)");
                sb_count.append(" where  s.id=(SELECT MAX(id) from yx_saleOrder WHERE yx_saleOrder.cid=c.id) ");
                sb_count.append(" and s.confirmIntention=? ");

                queryList.add(intention);
            }
            if (uid == 173) {
                sb.append(powerSql);
                sb_count.append(powerSql);
            } else {

            }


            //订单类型orderType可空可不空
            if (service_type.isEmpty()) {
                //订单类型service_type为空
            } else {
                sb.append(" and d.service_type=?");
                sb_count.append(" and d.service_type=?");
                queryList.add(service_type);
                if (timetype.isEmpty()) {

                } else {
                    sb.append(" and d.timetype=?");
                    sb_count.append(" and d.timetype=?");
                    queryList.add(timetype);
                    if (level.isEmpty()) {
                    } else {
                        sb.append(" and d.level=?");
                        sb_count.append(" and d.level=?");
                        queryList.add(level);
                    }
                }
            }
            //订单进度orderSpeed可空可不空
            if (orderSpeed.isEmpty()) {
                //订单进度orderSpeed为空

            } else {
                if (orderSpeed.equals("咨询中")) {
                    sb.append(" and o.states=0 ");
                    sb_count.append(" and o.states=0 ");
                }
                if (orderSpeed.equals("匹配中")) {
                    sb.append(" and o.type=0 and o.states=1 and d.work_states is null ");
                    sb_count.append(" and o.type=0 and o.states=1 and d.work_states is null ");
                }
                if (orderSpeed.equals("待服务")) {
                    sb.append("  and o.states=2   and d.work_states=0   ");
                    sb_count.append("   and o.states=2   and d.work_states=0   ");
                }
                if (orderSpeed.equals("服务中")) {
                    sb.append(" and d.work_states=1");
                    sb_count.append(" and d.work_states=1");
                }
                if (orderSpeed.equals("已完成")) {
                    sb.append(" and d.work_states=2");
                    sb_count.append(" and d.work_states=2");
                }
            }
            //回岗类型returnType可空可不空
            if (returnType.isEmpty()) {
                //回岗类型returnType为空
            } else {
                sb.append(" and d.work_states=2 ");
                sb_count.append(" and d.work_states=2 ");
                if (returnType.equals("正常回岗")) {
                    sb.append(" and d.return_time=d.confirm_time");
                    sb_count.append(" and d.return_time=d.confirm_time");
                }
                if (returnType.equals("续单回岗")) {
                    sb.append(" and d.return_time>d.confirm_time");
                    sb_count.append(" and d.return_time>d.confirm_time");
                }
                if (returnType.equals("退单回岗回岗")) {
                    sb.append(" and d.return_time<d.confirm_time");
                    sb_count.append(" and d.return_time<d.confirm_time");
                }
            }
            //来源可空可不空
            if (origin.isEmpty()) {
                //来源为空
            } else {
                sb.append(" and c.origin=?");
                sb_count.append(" and c.origin=?");
                queryList.add(origin);
            }
            //渠道可空可不空
            if (origin_channel.isEmpty()) {
                //渠道为空
            } else {
                if (origin_channel.length() > 3) {
                    sb.append(" and c.channel=?");
                    sb_count.append(" and c.channel=?");
                    queryList.add(origin);
                } else {
                    sb.append(" and c.channel like ?");
                    sb_count.append(" and c.channel like ?");
                    queryList.add(origin_channel + "%");
                }

            }

            //咨询顾问可空可不空
            if (sname == 0) {
                //咨询顾问为空
            } else {
                sb.append(" and c.sid=?");
                sb_count.append(" and c.sid=?");
                queryList.add(sname);
            }
            //母婴顾问banme可空可不空
            if (bname == 0) {
                //母婴顾问banme为空
            } else {
                sb.append(" and c.bid=? ");
                sb_count.append(" and c.bid=? ");
                queryList.add(bname);
            }


        }


        //开始时间与结束时间
        if (start_time.isEmpty() || start_time.isEmpty()) {

        } else {
            sb.append("  and c.creat_time>=? and c.creat_time<? ");
            sb_count.append("  and c.creat_time>=? and c.creat_time<? ");
            queryList.add(start_time);
            queryList.add(end_time);
        }

        //客户名称搜索
        if (number.isEmpty()) {
            //number为空
        } else {
            //数字id
            if (number.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
                if (number.length() > 8) {
                    //电话
                    sb.append(" and c.c_phone = ? ");
                    sb_count.append(" and c.c_phone = ? ");
                    queryList.add(number);
                } else {
                    //id
//                    String a = number.substring(2, number.length());
//                    Integer aa = Integer.valueOf(a);
                    sb.append(" and c.id = ? ");
                    sb_count.append(" and c.id = ? ");
                    queryList.add(number);
                }
            }
            //汉字
            if (number.matches("^[\\u4e00-\\u9fa5]+$")) {

                String sql2 = "select c_name from yx_custom where c_name = ? ";
                try {
                    List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{number}, String.class);
                    if (number.equals(list1.get(0))) {
                        System.out.println("________TRUE____");
                        sb.append("  and c.c_name= ?  ");
                        sb_count.append(" and c.c_name= ? ");
                        queryList.add(list1.get(0));
                    } else {
                        System.out.println("________TRUE____");
                    }
                } catch (Exception e) {
                    System.out.println("________TASDSDASDS");
                    if (number.length() >= 1) {
                        String name = number.substring(0, 1);
                        sb.append("  and c.c_name like ?   ");
                        sb_count.append(" and c.c_name like ?");
                        queryList.add(name + "%");
                    } else {

                    }

                }
            }
            //字母与数字
            if (number.matches(".*\\p{Alpha}.*")) {
                String a = number.substring(2, number.length());
                Integer aa = Integer.valueOf(a);
                sb.append(" and c.id = ? ");
                sb_count.append(" and c.id = ?");
                queryList.add(aa);
            }
        }
        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, uid);
        if (power == 100) {

        }
        if (power == 7) {
            sb.append(" and c.sid=?");
            sb_count.append("  and c.sid=?");
            queryList.add(uid);
        }

        if (flag == 0) {

        } else {
            sb.append(" GROUP BY o.did");
            // sb_count.append(" GROUP BY o.did");
        }

        System.out.println("queryList.toArray()_____________" + queryList);
        Integer count = this.jdbcTemplate.queryForObject(sb_count.toString(), Integer.class, queryList.toArray());
        List<Map<String, Object>> list = new ArrayList<>();
        sb.append(" order by c.creat_time desc");
        Map<String, Object> page = new HashMap<String, Object>();
        page = getAllPage(count, start);
        pageMap.put("count", count);//搜索出来的客户总人数
        String sql_page = page.get("sql_page").toString();
        sb.append(sql_page);
        pageMap.put("page", page.get("page"));
        pageMap.put("num", page.get("num"));
        pageList.add(pageMap);
        map.put("pageList", pageList);
        List<Map<String, Object>> finalPowerFiledList = powerFiledList;


        System.out.println("sb__________" + sb);
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();

                mp.put("cid", rs.getInt("id"));
                //客户姓名
                if (finalPowerFiledList.get(0).get("c_name") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("c_name").toString()) != 0) {
                        if (rs.getString("c_name") != null) {
                            mp.put("c_name", rs.getString("c_name"));
                        } else {
                            mp.put("c_name", "未知");
                        }
                    } else {
                    }
                } else {
                }
                //客户生产时间
                if (finalPowerFiledList.get(0).get("start") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("start").toString()) != 0) {
                        if (rs.getInt("production_situation") == 0) {
                            mp.put("start", rs.getString("nodate"));
                        } else {
                            mp.put("start", rs.getString("production_date"));
                        }
                    } else {
                    }
                } else {

                }
                //客户手机
                if (finalPowerFiledList.get(0).get("c_phone") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("c_phone").toString()) != 0) {
                        mp.put("c_phone", rs.getString("c_phone"));
                    } else {

                    }
                } else {

                }
                //微信号
                if (finalPowerFiledList.get(0).get("w_name") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("w_name").toString()) != 0) {
                        if (rs.getString("w_name") != null) {
                            mp.put("w_name", rs.getString("w_name"));
                        } else {
                            mp.put("w_name", "未知");
                        }
                    } else {

                    }
                } else {

                }
                //来源
                if (finalPowerFiledList.get(0).get("origin") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("origin").toString()) != 0) {
                        mp.put("origin", rs.getString("origin") + rs.getString("channel"));
                    } else {

                    }
                } else {

                }
                //居住区域
                if (finalPowerFiledList.get(0).get("address") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("address").toString()) != 0) {
                        if (rs.getString("address") != null) {
                            mp.put("address", rs.getString("address"));
                        } else {
                            mp.put("address", "未知");
                        }
                    } else {

                    }
                } else {

                }
                //创建时间
                if (finalPowerFiledList.get(0).get("creat_time") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("creat_time").toString()) != 0) {
                        mp.put("creat_time", rs.getString("creat_time"));
                    } else {

                    }
                } else {

                }
                //意向类型
                if (finalPowerFiledList.get(0).get("intention") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("intention").toString()) != 0) {

                        String sql_sale_count = "select count(*) from yx_saleOrder where cid=?";
                        Integer sale_count = jdbcTemplate.queryForObject(sql_sale_count, Integer.class, rs.getInt("id"));
                        if (sale_count > 0) {
                            //判读有销售工单，已销售工单为主
                            String sql_intention = "select confirmIntention from yx_saleOrder where id=(select MAX(id) from yx_saleOrder where cid=?) ";
                            String confirmIntention = jdbcTemplate.queryForObject(sql_intention, String.class, rs.getInt("id"));
                            mp.put("intention", confirmIntention);
                        } else {
                            //没有销售工单以咨询工单为主
                            String sql_count = "select count(*) from yx_consultingOrder where cid=?";
                            Integer count = jdbcTemplate.queryForObject(sql_count, Integer.class, rs.getInt("id"));
                            if (count > 0) {
                                String sql_intention = "select intention from yx_consultingOrder where id = (select max(id) from  yx_consultingOrder where  cid=?)";
                                String intention = jdbcTemplate.queryForObject(sql_intention, String.class, rs.getInt("id"));
                                mp.put("intention", intention);
                            } else {
                                mp.put("intention", "未知");
                            }
                        }
                    } else {
                        mp.put("intention", "未知");
                    }
                } else {

                }
                //跟进次数
                if (finalPowerFiledList.get(0).get("followCount") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("followCount").toString()) != 0) {
                        String sql_followCount = "select followCount from yx_custom where id=?";
                        Integer followCount = jdbcTemplate.queryForObject(sql_followCount, Integer.class, rs.getInt("id"));
                        mp.put("followCount", followCount);//更进次数
                    } else {

                    }
                } else {

                }
                //订单进度
                if (finalPowerFiledList.get(0).get("orderSpeed") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("orderSpeed").toString()) != 0) {
                        String sql_orderSpeed_count = "select count(*) from yx_order where cid=?";
                        Integer orderSpeed_count = jdbcTemplate.queryForObject(sql_orderSpeed_count, Integer.class, rs.getInt("id"));
                        if (orderSpeed_count > 0) {
                            //判断有没有订单
                            StringBuffer sb_states = new StringBuffer();
                            sb_states.append("select d.work_states,o.states,o.type,o.order_states from yx_order o");
                            sb_states.append(" left join yx_order_demand  d on (o.cid=d.cid) where  o.id=(select MAX(id) as id from yx_order where cid=?)");
                            List<Map<String, Object>> statesList = jdbcTemplate.queryForList(sb_states.toString(), rs.getInt("id"));

                            Integer work_states = 0;
                            if (statesList.get(0).get("work_states") != null) {
                                work_states = Integer.valueOf(statesList.get(0).get("work_states").toString());
                            } else {

                            }
                            Integer states = 0;
                            if (statesList.get(0).get("states") != null) {
                                states = Integer.valueOf(statesList.get(0).get("states").toString());
                            } else {

                            }

                            if (work_states == 0) {
                                if (Integer.valueOf(statesList.get(0).get("states").toString()) == 0) {
                                    //没提交派岗

                                    if (states == 0) {
                                        mp.put("orderSpeed", "咨询中");
                                    }
                                    if (states == 1) {
                                        mp.put("orderSpeed", "匹配中");
                                    }
                                    if (states == 2) {
                                        mp.put("orderSpeed", "待服务");
                                    }
                                } else {
                                    //提交派岗
                                    mp.put("orderSpeed", "待服务");
                                }

                            }
                            if (work_states == 1) {
                                mp.put("orderSpeed", "服务中");
                            }
                            if (work_states == 2) {
                                mp.put("orderSpeed", "已完成");
                            }

                        } else {
                            mp.put("orderSpeed", "未知");
                        }
                    } else {
                        mp.put("orderSpeed", "未知");
                    }
                } else {
                    mp.put("orderSpeed", "未知");
                }
                //母婴顾问
                if (finalPowerFiledList.get(0).get("bname") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("bname").toString()) != 0) {
                        if (rs.getInt("bid") != 0) {
                            String sql_bname = "select name from yx_user where id=?";
                            String bname = jdbcTemplate.queryForObject(sql_bname, String.class, rs.getInt("bid"));
                            mp.put("bname", bname);
                        } else {
                            mp.put("bname", "未知");
                        }
                    } else {

                    }
                } else {

                }
                //咨询顾问
                if (finalPowerFiledList.get(0).get("sname") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("sname").toString()) != 0) {
                        if (rs.getInt("sid") != 0) {
                            String sql_sname = "select name from yx_user where id=?";
                            String sname = jdbcTemplate.queryForObject(sql_sname, String.class, rs.getInt("sid"));
                            mp.put("sname", sname);
                        } else {
                            mp.put("sname", "未知");
                        }
                    } else {
                        mp.put("sname", "未知");
                    }
                } else {

                }
                //订单类型
                if (finalPowerFiledList.get(0).get("orderType") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("orderType").toString()) != 0) {
                        String sql_orderType = "select count(*),service_type from yx_order_demand where cid=?";
                        List<Map<String, Object>> orderTypeList = jdbcTemplate.queryForList(sql_orderType, rs.getInt("id"));
                        if (Integer.valueOf(orderTypeList.get(0).get("count(*)").toString()) > 0) {
                            mp.put("orderType", orderTypeList.get(0).get("service_type"));
                        } else {
                            mp.put("orderType", "未知");
                        }
                    } else {

                    }
                } else {

                }
                //订单天数
                if (finalPowerFiledList.get(0).get("service_day") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("service_day").toString()) != 0) {
                        String sql_service_day = "select count(*),service_day from yx_order_demand where cid=?";
                        List<Map<String, Object>> service_dayList = jdbcTemplate.queryForList(sql_service_day, rs.getInt("id"));
                        if (Integer.valueOf(service_dayList.get(0).get("count(*)").toString()) > 0) {
                            mp.put("service_day", service_dayList.get(0).get("service_day"));
                        } else {
                            mp.put("service_day", "未知");
                        }

                    } else {

                    }
                } else {

                }
                //服务人次
                if (finalPowerFiledList.get(0).get("service_count") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("service_count").toString()) != 0) {
                        String sql_serviceCount = "select count(*) from yx_order where id=?";
                        Integer sql_count = jdbcTemplate.queryForObject(sql_serviceCount, Integer.class, rs.getInt("id"));
                        if (sql_count > 0) {
                            String sql_service_count = "select count(distinct mid) from yx_order where cid=? and mid<>0";
                            Integer service_count = jdbcTemplate.queryForObject(sql_service_count, Integer.class, rs.getInt("id"));
                            mp.put("service_count", service_count);
                        } else {
                            mp.put("service_count", "未知");
                        }
                    } else {
                    }
                } else {

                }
                //回岗类型
                if (finalPowerFiledList.get(0).get("returnType") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("returnType").toString()) != 0) {
                        String sql_serviceCount = "select count(*) from yx_order where cid=?";
                        Integer sql_count = jdbcTemplate.queryForObject(sql_serviceCount, Integer.class, rs.getInt("id"));
                        if (sql_count > 0) {
                            String sql_n = "select confirm_time from yx_order where id=(select max(id) from yx_order where cid=?)";
                            Map<String, Object> nMap = jdbcTemplate.queryForMap(sql_n, rs.getInt("id"));
                            if (nMap.get("confirm_time") != null) {
                                mp.put("returnType", "正常回岗");
                            } else {
                                mp.put("returnType", "未知");
                            }

//                            //判断客户有没有订单表
//                            String sql_oid = "select MAX(id) as oid from yx_order where cid=? group by cid";
//                            List<Map<String, Object>> oidList = jdbcTemplate.queryForList(sql_oid, rs.getInt("id"));
//                            String sql_timeList = "select return_time,confirm_time from yx_order where id=? ";
//                            List<Map<String, Object>> timeList = jdbcTemplate.queryForList(sql_timeList, oidList.get(0).get("oid"));
//                            if (timeList.get(0).get("return_time") != null && timeList.get(0).get("confirm_time") != null) {
//                                //判断客户有订单表莉，预计回岗时间和回岗时间是否有值
//                                String return_time = timeList.get(0).get("return_time").toString();
//                                String confirm_time = timeList.get(0).get("confirm_time").toString();
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                                Date return_ = sdf.parse(return_time);
//                                Date confirm_ = sdf.parse(confirm_time);
//                                if (return_.before(confirm_)) {
//                                    mp.put("returnType", "续单回岗");
//                                }
//                                if (confirm_.before(return_)) {
//                                    mp.put("returnType", "退单回岗");
//                                }
//                                if (return_.equals(confirm_)) {
//                                    mp.put("returnType", "正常回岗");
//                                }
//                            } else {
//                                mp.put("returnType", "未知");
//                            }
                        } else {
                            mp.put("returnType", "未知");
                        }
                    } else {
                    }
                } else {

                }
                //订单金额
                if (finalPowerFiledList.get(0).get("price") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("price").toString()) != 0) {
                        String sql_price = "select count(*),price from yx_order_demand where cid=? and work_states =2";
                        List<Map<String, Object>> priceList = jdbcTemplate.queryForList(sql_price, rs.getInt("id"));

                        if (Integer.valueOf(priceList.get(0).get("count(*)").toString()) > 0) {
                            mp.put("price", priceList.get(0).get("price"));
                        } else {
                            mp.put("price", "未知");
                        }
                    } else {

                    }
                } else {

                }
                //满意度
                if (finalPowerFiledList.get(0).get("satisfied") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("satisfied").toString()) != 0) {
                        Integer satisfied = 0;
                        if (satisfied != 0) {
                            mp.put("satisfied", satisfied);
                        } else {
                            mp.put("satisfied", "未知");
                        }
                    } else {
                    }
                } else {

                }

                String sql_order_countList = "select order_states from yx_order where cid=?";
                List<Map<String, Object>> order_statesList = jdbcTemplate.queryForList(sql_order_countList, rs.getInt("id"));
                //判读是否能删除
                if (order_statesList.size() == 0) {
                    mp.put("isDelete", 0);
                }
                if (order_statesList.size() == 1) {
                    if (order_statesList.get(0).get("order_states") != null) {
                        Integer order_states = Integer.valueOf(order_statesList.get(0).get("order_states").toString());
                        if (order_states == 0 || order_states == 1) {
                            mp.put("isDelete", 0);
                        }
                        if (order_states == 2 || order_states == 3) {
                            mp.put("isDelete", 1);
                        }
                    } else {
                        mp.put("isDelete", 0);
                    }
                }
                if (order_statesList.size() > 1) {
                    mp.put("isDelete", 1);
                }


                StringBuffer sb_price = new StringBuffer();
                sb_price.append("select o_number,arrival_time,order_day,order_states,od.onePrice,od.service_day,od.price from yx_order_demand od");
                sb_price.append(" left join yx_order o on(od.cid=o.cid) where o.cid=? and o.order_states>=2 and od.service_day<>0");

                List<Map<String, Object>> priceList = jdbcTemplate.queryForList(sb_price.toString(), rs.getInt("id"));


                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                //当前时间
                String nowTime = time.format(new Date());
                //服务天数
                if (finalPowerFiledList.get(0).get("orderDay") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("orderDay").toString()) != 0) {
                        Integer orderDay = 0;//服务天数
                        Integer truePrice = 0;//实收金额
                        for (int i = 0; i < priceList.size(); i++) {
                            Integer order_states = Integer.valueOf(priceList.get(i).get("order_states").toString());
                            Integer order_day = 0;//单个订单完成的天数
                            Integer oneTruePrice = 0;//单个小订单实收金额
                            Integer onePrice = 0;//单价
                            String number = priceList.get(i).get("o_number").toString();
                            number = number.substring(10, 13);
                            Integer price = Integer.valueOf(priceList.get(i).get("price").toString());
                            Integer service_day = Integer.valueOf(priceList.get(i).get("service_day").toString());

                            if (order_states == 2) {
                                //服务中

                                order_day = daysBetween(priceList.get(i).get("arrival_time").toString(), nowTime);

                                if (priceList.get(i).get("onePrice") != null) {
                                    onePrice = Integer.valueOf(priceList.get(i).get("onePrice").toString());
                                } else {
                                    onePrice = getNumberOnePrice(number, service_day, price);
                                }

                                oneTruePrice = order_day * onePrice;
                            }
                            if (order_states == 3) {
                                //已完成
                                order_day = Integer.valueOf(priceList.get(i).get("order_day").toString());

                                if (priceList.get(i).get("onePrice") != null) {
                                    //判断有没有填写单价
                                    onePrice = Integer.valueOf(priceList.get(i).get("onePrice").toString());
                                } else {
                                    onePrice = getNumberOnePrice(number, service_day, price);
                                }
                                oneTruePrice = order_day * onePrice;
                            }
                            orderDay = orderDay + order_day;
                            truePrice = truePrice + oneTruePrice;

                        }
                        mp.put("orderDay", orderDay);
                        mp.put("truePrice", truePrice);
                    } else {

                    }
                } else {
                    mp.put("orderDay", "未知");
                    mp.put("truePrice", "未知");
                }


                return mp;
            }
        }, queryList.toArray());
        map.put("list", list);
        return map;
    }

    @Override
    public int setCustomCount(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        int type = jsonObject.getInteger("type");
        int count = jsonObject.getInteger("count");
        String sql = "update yx_customSet set count=? where type=?";
        int states = jdbcTemplate.update(sql, count, type);
        if (states > 0) {
            return states;
        } else {
            return 0;
        }
    }

    @Override
    public Map<String, Object> getAllCondition() {
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
        //客户来源
        //咨询顾问
        String sql_sname = "select id,name from yx_user where power=7";
        List<Map<String, Object>> snameList = this.jdbcTemplate.queryForList(sql_sname);
        map.put("snameList", snameList);


        //母婴顾问
        String sql_bname = "select id,name from yx_user where power=4";
        List<Map<String, Object>> bnameList = this.jdbcTemplate.queryForList(sql_bname);
        map.put("bnameList", bnameList);

        //订单类型
        List<Map<String, Object>> orderTypeList = new ArrayList<>();
        String sql_q1 = "select name,type from yx_orderType where type<>0 and type is not null";
        List<Map<String, Object>> typeList = this.jdbcTemplate.queryForList(sql_q1);
        String sql_q2 = "select name,stype from yx_orderType where stype=? ";
        String sql_q3 = "select name,utype from yx_orderType where utype=? ";
        for (int i = 0; i < typeList.size(); i++) {
            List<Map<String, Object>> stypeList = new ArrayList<>();
            if (Integer.valueOf(typeList.get(i).get("type").toString()) != 1) {
                //居家服务 三级分类
                System.out.println("____2_______________");
                stypeList = this.jdbcTemplate.queryForList(sql_q2, typeList.get(i).get("type"));
                Map<String, Object> unameMap2 = new HashMap<>();
                List<Map<String, Object>> unameList1 = new ArrayList<>();
                for (int j = 0; j < stypeList.size(); j++) {
                    //居家服务下，长期，短期
                    List<Map<String, Object>> utypeList = new ArrayList<>();
                    List<Map<String, Object>> unameList = new ArrayList<>();
                    Map<String, Object> unameMap1 = new HashMap<>();
                    utypeList = this.jdbcTemplate.queryForList(sql_q3, stypeList.get(i).get("stype"));
                    for (int k = 0; k < utypeList.size(); k++) {
                        //长期，短期下u1，u2.........
                        Map<String, Object> unameMap = new HashMap<>();
                        unameMap.put("name", utypeList.get(k).get("name"));
                        unameList.add(unameMap);
                    }
                    unameMap1.put("name", stypeList.get(j).get("name"));
                    unameMap1.put("children", unameList);
                    unameList1.add(unameMap1);
                }
                unameMap2.put("name", typeList.get(i).get("name"));
                unameMap2.put("children", unameList1);
                orderTypeList.add(unameMap2);
            } else {
                //月子会所 两级分类
                System.out.println("-----11------_______");
                stypeList = this.jdbcTemplate.queryForList(sql_q2, typeList.get(i).get("type"));
                Map<String, Object> snameMap1 = new HashMap<>();
                List<Map<String, Object>> unameList = new ArrayList<>();
                for (int j = 0; j < stypeList.size(); j++) {
                    Map<String, Object> snameMap = new HashMap<>();
                    snameMap.put("name", stypeList.get(j).get("name"));
                    unameList.add(snameMap);
                }
                snameMap1.put("name", typeList.get(i).get("name"));
                snameMap1.put("children", unameList);
                orderTypeList.add(snameMap1);
            }
        }
        System.out.println("orderTypeList = " + orderTypeList);
        map.put("orderTypeList", orderTypeList);

        return map;
    }

    @Override
    public Map<String, Object> saveOrUpdateCustom(Custom custom) {
        int states = 0;
        Map<String, Object> map = new HashMap<>();
        if (custom.getId() == 0) {
            //添加
            StringBuffer sb = new StringBuffer();
            sb.append("insert into yx_custom (bid,sid,origin,channel,c_name,w_name,c_phone,production_situation,nodate,production_date,creat_time,update_time,integral,isPartner,address,isIntention)");
            sb.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");
            SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
            String creat_time = time.format(new Date());

            states = this.jdbcTemplate.update(sb.toString(), 0, custom.getSid(), custom.getOrigin(), custom.getChannel(), custom.getC_name(), custom.getW_name(),
                    custom.getC_phone(), custom.getProduction_situation(), custom.getNodate(), custom.getProduction_date(), creat_time, creat_time, 0, 0, custom.getAddress(), 1);

            String sql_cid = "select max(id) from yx_custom";
            int cid = this.jdbcTemplate.queryForObject(sql_cid, Integer.class);
            if (states > 0) {
                map.put("states", 1);
                map.put("cid", cid);
            } else {
                map.put("states", 0);
            }
        } else {
            String sql = "update yx_custom set sid=?,origin=?,channel=?,c_name=?,w_name=?,c_phone=?,production_situation=?,nodate=?,production_date=?,address=? where id=? ";
            states = this.jdbcTemplate.update(sql, custom.getSid(), custom.getOrigin(), custom.getChannel(), custom.getC_name(), custom.getW_name(), custom.getC_phone(), custom.getProduction_situation(),
                    custom.getNodate(), custom.getProduction_date(), custom.getAddress(), custom.getId());
            if (states > 0) {
                map.put("states", 1);
                map.put("cid", custom.getId());
            } else {
                map.put("states", 0);
            }
        }

        return map;

    }

    @Transactional
    @Override
    public Map<String, Object> saveOrUpdateOrder(String json) {
        Map<String, Object> map = new HashMap<>();
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        int did = jsonObject.getInteger("did");
        int cid = jsonObject.getInteger("cid");
        String order_type = jsonObject.getString("order_type");//1
        Integer service_day = jsonObject.getInteger("service_day");//1
        String starttime = jsonObject.getString("starttime");//1
        String endtime = jsonObject.getString("endtime");//1

        String service_type = jsonObject.getString("service_type");
        String timetype = jsonObject.getString("timetype");
        String level = jsonObject.getString("level");
        String price = jsonObject.getString("price");
        String onePrice = jsonObject.getString("onePrice");
        Integer activityType = jsonObject.getInteger("activityType");

        int states = 0;
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creat_time = time.format(new Date());
        if (service_type.isEmpty() && timetype.isEmpty()) {
            //没有填写订单类型，设置默认值
            service_type = "居家服务";
            timetype = "长期";
            price = "9800";
        } else {

        }

        Integer one_price = 0;//单价
        if (service_type.equals("月子会所服务")) {

        } else {
            if (onePrice != null) {
                one_price = Integer.valueOf(onePrice);
            } else {
                one_price = Integer.valueOf(price) / Integer.valueOf(service_day);
            }
        }

        if (did == 0) {
            //没有添加订单需求
            StringBuffer sb = new StringBuffer();
            sb.append("insert into yx_order_demand(cid,order_type,service_type,service_day,starttime,endtime,timetype,level,price,threestates,creat_time,activityType,onePrice)");
            sb.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?)");
            states = this.jdbcTemplate.update(sb.toString(), cid, order_type, service_type, service_day, starttime, endtime, timetype, level, price, 0, creat_time, activityType, one_price);

            String sql_did = "select max(id) from yx_order_demand";
            int newdid = this.jdbcTemplate.queryForObject(sql_did, Integer.class);

            StringBuffer sb1 = new StringBuffer();
            sb1.append("insert into yx_order (did,mid,cid,type,states)");
            sb1.append("value(?,?,?,?,?)");
            int states_order = this.jdbcTemplate.update(sb1.toString(), newdid, 0, cid, 0, 0);
            if (states > 0 && states_order > 0) {
                map.put("states", 1);
                map.put("cid", cid);
            } else {
                map.put("states", 0);
            }

        } else {
            //填写了订单需求
            String sql = "update yx_order_demand set service_type=?,service_day=?,starttime=?,endtime=?,timetype=?,level=?,price=?,onePrice=? where id=? ";
            states = this.jdbcTemplate.update(sql, service_type, service_day, starttime, endtime, timetype, level, price, one_price, did);

            String sql_list = "select id,o_number from yx_order where cid=? ";
            List<Map<String, Object>> orderList = this.jdbcTemplate.queryForList(sql_list, cid);
            if (orderList.get(0).get("o_number") != null) {
                //编号
                //获取新的编号
                String number = getOrderNumber(service_type, timetype, 0);
                //截取其中关键字母
                String num = number.substring(10, 13);
                for (int i = 0; i < orderList.size(); i++) {
                    String o_number = orderList.get(i).get("o_number").toString();
                    String o_num = orderList.get(i).get("o_number").toString().substring(10, 13);

                    if (num.equals(o_num)) {
                        //订单类型不变

                    } else {
                        String sql_number = "update yx_order set o_number=? where id=?";
                        String n1 = o_number.substring(0, 10);
                        String n2 = o_number.substring(13);
                        //新的订单
                        String new_number = n1 + num + n2;
                        int states_number = this.jdbcTemplate.update(sql_number, new_number, orderList.get(i).get("id"));
                    }
                }

            } else {
                //无编号

            }


            if (states > 0) {
                map.put("states", 1);
                map.put("cid", cid);
            } else {
                map.put("states", 0);
            }
        }
        return map;

    }

    /*new*/
    @Override
    public Map<String, Object> getOneCustom(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer cid = jsonObject.getInteger("cid");
        String somnus = jsonObject.getString("somnus");
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> customList = new ArrayList<>();
        String sql_c = "select id,sid,origin,channel,c_name,w_name,c_phone,production_situation,nodate,production_date,creat_time,address from yx_custom where id = ?";
        customList = this.jdbcTemplate.query(sql_c, new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("id", rs.getInt("id"));
                String sql_order = "select o_name,o_phone,region,o_address,order_states from yx_order where cid=? group by cid";
                List<Map<String, Object>> orderList = jdbcTemplate.queryForList(sql_order, rs.getInt("id"));
                System.out.println("orderList = " + orderList);

//                String sql_count= "select count(*) from yx_substitution where cid=?";
//                Integer count_label = jdbcTemplate.queryForObject(sql_count,Integer.class, cid);
//                System.out.println("count_label = " + count_label);
//                if (count_label > 0) {
//                    String sql_ss = "select label from yx_substitution where  id=(select MAX(id) as id from yx_substitution where cid =? ) ";
//                    String label = jdbcTemplate.queryForObject(sql_ss, String.class,cid);
//                    System.out.println("label = " + label);
//                    List<String> labelList = null;
//                    if (label != null) {
//                        labelList = JSONArray.fromObject(label);
//
//                    } else {
//
//                    }
//                } else {
//
//                }

                if (orderList.size() > 0) {

                    if (orderList.get(0).get("o_name") != null) {

                        mp.put("o_name", orderList.get(0).get("o_name"));
                        mp.put("o_phone", orderList.get(0).get("o_phone"));
                        mp.put("region", orderList.get(0).get("region"));
                        mp.put("o_address", orderList.get(0).get("o_address"));
                        //是否添加地址
                        if (orderList.get(0).get("order_states") != null) {
                            mp.put("isAddress", 2);
                        } else {
                            mp.put("isAddress", 1);
                        }
                    } else {
                        mp.put("isAddress", 0);
                    }
                } else {
                    mp.put("isAddress", 0);
                }

                mp.put("sid", rs.getInt("sid"));
                if (rs.getInt("sid") == 0) {
                    mp.put("sname", null);
                } else {
                    String sql_name = "select name from yx_user where id=?";
                    String sname = jdbcTemplate.queryForObject(sql_name, String.class, rs.getInt("sid"));
                    mp.put("sname", sname);
                }
                mp.put("address", rs.getString("address"));
                mp.put("origin", rs.getString("origin"));
                mp.put("channel", rs.getString("channel"));
                mp.put("c_name", rs.getString("c_name"));
                mp.put("w_name", rs.getString("w_name"));
                mp.put("c_phone", rs.getString("c_phone"));
                mp.put("production_situation", rs.getString("production_situation"));
                if (rs.getInt("production_situation") == 0) {
                    mp.put("time", rs.getString("nodate"));
                } else {
                    mp.put("time", rs.getString("production_date"));
                }
                mp.put("creat_time", rs.getString("creat_time"));
                mp.put("address", rs.getString("address"));
                return mp;
            }
        }, cid);
        map.put("custom", customList);

        String sql_count_demand = "select count(*) from yx_order_demand where cid=?";
        List<Map<String, Object>> demandList = new ArrayList<>();
        Integer count_demand = this.jdbcTemplate.queryForObject(sql_count_demand, Integer.class, cid);
        if (count_demand > 0) {
            StringBuffer sb = new StringBuffer();
            sb.append("select c.bid,c.c_name,d.cid,d.service_type,d.service_day,d.starttime,d.endtime,d.timetype,d.level,d.price,d.creat_time,d.activityType,d.threestates, ");
            sb.append("d.post_states,d.creat_time,d.threematorn,o.mid,o.did,o.id,o.order_states,o.type,o.states,o.return_time,o.arrival_time,d.onePrice from yx_custom c");
            sb.append(" left join yx_order_demand d on (c.id=d.cid) left join yx_order o on (c.id=o.cid) ");
            sb.append("where o.id=(select Max(yx_order.id) from yx_order where yx_order.cid=?)");
            demandList = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                @SneakyThrows
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    if (rs.getInt("bid") == 0) {
                        mp.put("bname", null);
                    } else {
                        String sql_name = "select name from yx_user where id=?";
                        String bname = jdbcTemplate.queryForObject(sql_name, String.class, rs.getInt("bid"));
                        mp.put("bname", bname);
                    }
                    mp.put("oid", rs.getInt("id"));
                    mp.put("did", rs.getInt("did"));
                    mp.put("cid", rs.getInt("cid"));
                    mp.put("onePrice", rs.getString("onePrice"));
                    mp.put("c_name", rs.getString("c_name"));
                    mp.put("service_type", rs.getString("service_type"));
                    mp.put("service_day", rs.getInt("service_day"));
                    mp.put("starttime", rs.getString("starttime"));
                    mp.put("endtime", rs.getString("endtime"));
                    mp.put("start", rs.getString("starttime").substring(5, rs.getString("starttime").length()));
                    mp.put("end", rs.getString("endtime").substring(5, rs.getString("endtime").length()));
                    try {
                        String startweek = getWeek(rs.getString("starttime"));
                        String endweek = getWeek(rs.getString("endtime"));
                        mp.put("startweek", startweek);
                        mp.put("endweek", endweek);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                    String nowTime = time.format(new Date());
                    Integer day = 0;
                    if (rs.getInt("type") == 0) {
                        mp.put("states", rs.getInt("states"));
                    } else {
                        mp.put("order_states", rs.getInt("order_states"));
                    }

                    if (rs.getInt("type") == 0 && rs.getInt("states") == 2) {
                        day = daysBetween(nowTime, rs.getString("starttime"));
                    }
                    if (rs.getInt("order_states") == 0 || rs.getInt("order_states") == 1) {
                        day = daysBetween(nowTime, rs.getString("starttime"));
                    }
                    if (rs.getInt("order_states") == 2) {
                        day = daysBetween(rs.getString("arrival_time"), rs.getString("return_time"));
                    }
                    mp.put("day", day);

                    mp.put("timetype", rs.getString("timetype"));
                    mp.put("level", rs.getString("level"));
                    mp.put("price", rs.getString("price"));
                    mp.put("creat_time", rs.getString("creat_time"));
                    mp.put("activityType", rs.getInt("activityType"));
                    mp.put("threestates", rs.getInt("threestates"));
                    mp.put("post_states", rs.getInt("post_states"));

                    StringBuffer sb1 = new StringBuffer();
                    sb1.append("select m.id,m.uid,m.name,m.born,m.household,m.zodiac,m.constellation,b.photo,b.grade,con.phone  from yx_matorn m");
                    sb1.append(" LEFT JOIN yx_bussiness b on (m.id=b.mid) LEFT JOIN yx_contact con on (m.id=con.mid)");
                    sb1.append("where m.id = ?");
                    String threematorn = rs.getString("threematorn");
                    List<Map<String, Object>> matornList = new ArrayList<Map<String, Object>>();
                    if (threematorn != null) {
                        JSONArray jsonArray = JSONArray.fromObject(threematorn);
                        List<Integer> listmatorn = (List) jsonArray;
                        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                        for (int i = 0; i < listmatorn.size(); i++) {
                            list1 = jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
                                @Override
                                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                                    Map<String, Object> mp = new HashMap<String, Object>();
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
                                    String sql_gent = "select name from yx_user u where id=?";
                                    String agent = jdbcTemplate.queryForObject(sql_gent, String.class, rs.getInt("uid"));
                                    mp.put("agent", agent);
                                    mp.put("city", city);
                                    mp.put("name", rs.getString("name"));
                                    mp.put("mid", rs.getInt("id"));
                                    mp.put("zodiac", rs.getString("zodiac"));
                                    mp.put("constellation", rs.getString("constellation"));
                                    mp.put("grade", rs.getString("grade"));
                                    mp.put("phone", rs.getString("phone"));
                                    mp.put("photo", rs.getString("photo"));

                                    return mp;
                                }
                            }, listmatorn.get(i));
                            matornList.add(list1.get(0));
                        }
                        mp.put("matornList", matornList);
                    } else {
                        mp.put("matornList", matornList);
                    }
                    List<Map<String, Object>> oneMatorn = new ArrayList<Map<String, Object>>();
                    if (rs.getInt("mid") != 0) {
                        oneMatorn = jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
                            @Override
                            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                                Map<String, Object> mp = new HashMap<String, Object>();
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
                                String sql_gent = "select name from yx_user u where id=?";
                                String agent = jdbcTemplate.queryForObject(sql_gent, String.class, rs.getInt("uid"));
                                mp.put("agent", agent);
                                mp.put("city", city);
                                mp.put("name", rs.getString("name"));
                                mp.put("mid", rs.getInt("id"));
                                mp.put("zodiac", rs.getString("zodiac"));
                                mp.put("constellation", rs.getString("constellation"));


                                mp.put("grade", rs.getString("grade"));
                                mp.put("phone", rs.getString("phone"));
                                mp.put("photo", rs.getString("photo"));

                                return mp;
                            }
                        }, rs.getInt("mid"));
                        mp.put("oneMatorn", oneMatorn);
                    } else {
                        mp.put("oneMatorn", oneMatorn);
                    }


                    String sql_sale_count = "select count(*) from yx_saleOrder where cid=?";
                    Integer sale_count = jdbcTemplate.queryForObject(sql_sale_count, Integer.class, cid);
                    if (sale_count > 0) {
                        //判读有销售工单，已销售工单为主
                        String sql_intention = "select confirmIntention from yx_saleOrder where id=(select MAX(id) from yx_saleOrder where cid=?) ";
                        String confirmIntention = jdbcTemplate.queryForObject(sql_intention, String.class, cid);
                        mp.put("intention", confirmIntention);
                    } else {
                        //没有销售工单以咨询工单为主
                        String sql_count = "select count(*) from yx_consultingOrder where cid=?";
                        int count = jdbcTemplate.queryForObject(sql_count, Integer.class, cid);
                        if (count > 0) {
                            String sql_intention = "select intention from yx_consultingOrder where id = (select max(id) from  yx_consultingOrder where  cid=?)";
                            String intention = jdbcTemplate.queryForObject(sql_intention, String.class, cid);
                            mp.put("intention", intention);
                        } else {
                            mp.put("intention", "未知");
                        }
                    }


                    return mp;
                }
            }, cid);

        } else {

        }
        map.put("demand", demandList);

        String sql_consulting = "select * from yx_consultingOrder where cid=? order by creat_time desc ";
        List<Map<String, Object>> consultingList = new ArrayList<>();

        consultingList = this.jdbcTemplate.query(sql_consulting.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("id", rs.getInt("id"));

                String sql_bname = "select name from yx_user where id=?";
                String bname = null;
                if (rs.getInt("bid") != 0) {
                    bname = jdbcTemplate.queryForObject(sql_bname, String.class, rs.getInt("bid"));
                } else {
                }

                mp.put("bname", bname);
                String sql_sname = "select name from yx_user where id=?";
                String sname = jdbcTemplate.queryForObject(sql_sname, String.class, rs.getInt("sid"));

                mp.put("sname", sname);
                mp.put("workType", rs.getInt("workType"));
                mp.put("intention", rs.getString("intention"));
                mp.put("points", rs.getString("points"));
                mp.put("creat_time", rs.getString("creat_time"));

                if (somnus.equals("order")) {
                    String sql = "update yx_consultingOrder set isRead=1 where id=?";
                    int states = jdbcTemplate.update(sql, rs.getInt("id"));
                }
                if (somnus.equals("admin")) {

                }
                return mp;
            }
        }, cid);
        map.put("consulting", consultingList);

        String sql_sale = "select * from yx_saleOrder where cid=? order by creat_time desc ";
        List<Map<String, Object>> saleList = new ArrayList<>();
        saleList = this.jdbcTemplate.query(sql_sale.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("id", rs.getInt("id"));
                String sql_bname = "select name from yx_user where id=?";
                String bname = jdbcTemplate.queryForObject(sql_bname, String.class, rs.getInt("bid"));
                mp.put("bname", bname);
                mp.put("saleType", rs.getInt("saleType"));
                mp.put("confirmIntention", rs.getString("confirmIntention"));
                mp.put("information", rs.getString("information"));
                mp.put("isRecommend", rs.getInt("isRecommend"));
                mp.put("age", rs.getString("age"));
                mp.put("zodiac", rs.getString("zodiac"));
                mp.put("educational", rs.getString("educational"));
                mp.put("household", rs.getString("household"));
                mp.put("recommendNotes", rs.getString("recommendNotes"));
                mp.put("isInterview", rs.getInt("isInterview"));
                mp.put("interviewType", rs.getInt("interviewType"));
                mp.put("interviewTime", rs.getString("interviewTime"));
                mp.put("creat_time", rs.getString("creat_time"));
                return mp;
            }
        }, cid);
        map.put("sale", saleList);


        List<Map<String, Object>> orderList = new ArrayList<>();

        String sql_oid = "select id from yx_order where cid=?";
        List<Map<String, Object>> oidList = this.jdbcTemplate.queryForList(sql_oid, cid);
        System.out.println("oidList___________" + oidList);
        StringBuffer sb = new StringBuffer();
        sb.append("select o.id,o.cid,o.mid,o.o_number,o.arrival_time,o.return_time,o.confirm_time,o.order_states,o.order_day,");
        sb.append("o.service_states,o.isContinue,o.wages_remarks from yx_order o");
        sb.append(" where o.id=?");

        for (int i = 0; i < oidList.size(); i++) {

            Integer oidList_oid = Integer.valueOf(oidList.get(i).get("id").toString());

            List<Map<String, Object>> midList = new ArrayList<>();
            int finalI = i;
            midList = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                @SneakyThrows
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("id", rs.getInt("id"));
                    mp.put("cid", rs.getInt("cid"));
                    mp.put("o_number", rs.getString("o_number"));
                    mp.put("mid", rs.getInt("mid"));
                    mp.put("service_states", rs.getInt("service_states"));
                    mp.put("isContinue", rs.getInt("isContinue"));
                    mp.put("wages_remarks", rs.getString("wages_remarks"));

                    if (rs.getInt("order_states") == 0) {
                        mp.put("order_states", "待服务未推荐");
                    }
                    if (rs.getInt("order_states") == 1) {
                        mp.put("order_states", "待服务已推荐");
                    }
                    if (rs.getInt("order_states") == 2) {
                        mp.put("order_states", "服务中");
                    }
                    if (rs.getInt("order_states") == 3) {
                        mp.put("order_states", "已完成");
                    }

                    if (rs.getString("arrival_time") != null) {
                        mp.put("arrival_time", rs.getString("arrival_time"));

                    } else {
                        mp.put("arrival_time", "未知");
                    }

                    //判断有多少月嫂
                    Integer orderSize = oidList.size();
                    if (orderSize == 1) {
                        //只有一个月嫂的情况
                        if (rs.getString("confirm_time") != null) {

                            String return_time = rs.getString("return_time");
                            String confirm_time = rs.getString("confirm_time");

                            mp.put("confirm_time", confirm_time);
                            if (timeSize(return_time, confirm_time) == 1) {
                                // return_time>confirm_time
                                mp.put("returnType", "退单回岗");
                            }
                            if (timeSize(return_time, confirm_time) == 0) {
                                // return_time<confirm_time
                                mp.put("returnType", "续单回岗");
                            }
                            if (return_time.equals(confirm_time)) {
                                mp.put("returnType", "正常回岗");
                            }

                        } else {
                            mp.put("returnType", "未知");
                            mp.put("confirm_time", "未知");
                        }
                    }
                    if (orderSize >= 2) {
                        //两个以上月嫂的情况

                        if (finalI == orderSize - 1) {
                            //订单最后一个
                            if (rs.getString("confirm_time") != null) {
                                mp.put("confirm_time", rs.getString("confirm_time"));
                                mp.put("returnType", "正常回岗");
                            } else {
                                mp.put("returnType", "未知");
                                mp.put("confirm_time", "未知");
                            }
                        } else {
                            //不是最后一单
                            if (rs.getString("confirm_time") != null) {

                                mp.put("confirm_time", rs.getString("confirm_time"));
                                String number = rs.getString("o_number");
                                String newnumber = number.substring(10, 13);

                                String sql_number = "select o_number from yx_order where cid=?";
                                List<Map<String, Object>> numberList = jdbcTemplate.queryForList(sql_number, rs.getInt("cid"));
                                if (finalI + 1 < numberList.size()) {
                                    //下一个订单编号
                                    String nextNumber = numberList.get(finalI + 1).get("o_number").toString().substring(10, 13);
                                    if (nextNumber.equals(newnumber)) {
                                        //订单号相同 换人
                                        mp.put("returnType", "换人");
                                    } else {
                                        //订单号不相同 转岗
                                        mp.put("returnType", "转岗");
                                    }
                                } else {
                                    mp.put("returnType", "未知");
                                }

                            } else {
                                mp.put("returnType", "未知");
                                mp.put("confirm_time", "未知");
                            }
                        }

                    }


                    if (rs.getInt("order_day") != 0) {
                        mp.put("order_day", rs.getInt("order_day"));
                    } else {
                        mp.put("order_day", "未知");
                    }
                    StringBuffer sb1 = new StringBuffer();
                    sb1.append("select  o.mid,m.uid,m.name,m.born,m.household,m.zodiac,m.constellation,b.isorder,b.photo,b.grade,con.phone,o.order_states,o.id,o.o_number,o.type,o.states  from yx_matorn m");
                    sb1.append(" LEFT JOIN yx_bussiness b on (m.id=b.mid) LEFT JOIN yx_contact con on (m.id=con.mid)");
                    sb1.append(" LEFT JOIN yx_order o  on (m.id=o.mid)");
                    sb1.append("where m.id = ? and o.id=?");
                    List<Map<String, Object>> oneMatorn = new ArrayList<Map<String, Object>>();
                    Integer mid = rs.getInt("mid");
                    System.out.println("mid = " + mid);
                    if (mid != 0) {
                        //订单上有月嫂
                        String sql_ma = "select m.name,b.number from  yx_matorn m left join yx_bussiness b on (m.id=b.mid) where m.id=?";
                        List<Map<String, Object>> list_ma = jdbcTemplate.queryForList(sql_ma, mid);

                        mp.put("name", list_ma.get(0).get("name"));
                        mp.put("number", list_ma.get(0).get("number"));

                        oneMatorn = jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
                            @Override
                            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                                Map<String, Object> mp = new HashMap<String, Object>();
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
                                String sql_gent = "select name from yx_user u where id=?";
                                String agent = jdbcTemplate.queryForObject(sql_gent, String.class, rs.getInt("uid"));
                                mp.put("agent", agent);
                                mp.put("city", city);
                                mp.put("name", rs.getString("name"));
                                mp.put("mid", mid);
                                mp.put("oid", rs.getInt("id"));
                                mp.put("zodiac", rs.getString("zodiac"));
                                mp.put("constellation", rs.getString("constellation"));
                                mp.put("grade", "P1");

                                //  mp.put("grade", rs.getString("grade"));
                                mp.put("phone", rs.getString("phone"));
                                mp.put("photo", rs.getString("photo"));
                                mp.put("isorder", rs.getInt("isorder"));
                                if (rs.getInt("type") == 0) {
                                    mp.put("states", rs.getInt("states"));
                                } else {
                                    mp.put("order_states", rs.getInt("order_states"));
                                }
                                String o_number = null;
                                try {
                                    o_number = rs.getString("o_number").substring(10, 13);
                                    if (o_number.equals("CAA")) {
                                        //全程
                                        mp.put("club", 1);
                                    } else {
                                        //非全程
                                        mp.put("club", 0);
                                    }
                                } catch (Exception e) {
                                    mp.put("club", 0);
                                }
                                return mp;
                            }
                        }, rs.getInt("mid"), oidList_oid);
                        mp.put("oneMatorn", oneMatorn);
                    } else {
                        mp.put("name", "未知");
                        mp.put("number", "未知");
                        mp.put("oneMatorn", oneMatorn);
                    }

                    return mp;
                }
            }, oidList.get(i).get("id"));
            if (midList.size() > 0) {
                orderList.add(midList.get(0));
            } else {

            }

        }

        map.put("order", orderList);

        String sql_message = "select mid,type,creat_time from yx_message where cid=?";
        List<Map<String, Object>> meList = new ArrayList<>();

        meList = this.jdbcTemplate.query(sql_message, new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();

                mp.put("type", rs.getInt("type"));
                mp.put("creat_time", rs.getString("creat_time"));
//                if (rs.getInt("mid") != 0) {
//                    String sql_matorn = "select name from yx_matorn where id=?";
//                    String name = jdbcTemplate.queryForObject(sql_matorn, String.class, rs.getInt("mid"));
//                    mp.put("name", name);
//                } else {
//                    mp.put("name", null);
//                }
                if (rs.getString("mid") != null) {
                    String sql_matorn = "select name from  yx_matorn where id=?";
                    List<Map<String, Object>> nameList = new ArrayList<>();
                    if (rs.getString("mid").contains("[")) {
                        //推荐月嫂是集合字符串
                        JSONArray jsonArray = JSONArray.fromObject(rs.getString("mid"));
                        List<Integer> listMatorn = (List) jsonArray;
                        for (int i = 0; i < listMatorn.size(); i++) {
                            Map<String, Object> map = new HashMap<>();
                            List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql_matorn, listMatorn.get(i));
                            map.put("name", mapList.get(0).get("name"));
                            nameList.add(map);
                        }
                        mp.put("nameList", nameList);

                    } else {
                        //单个数字字符串
                        String name = jdbcTemplate.queryForObject(sql_matorn, String.class, rs.getString("mid"));
                        mp.put("name", name);
                    }

                } else {
                    mp.put("name", null);
                }


                return mp;
            }
        }, cid);

        Map<String, Object> messageMap = new HashMap<>();
        if (meList.size() > 0) {
            messageMap.put("messageType", meList.get(meList.size() - 1).get("type"));
        } else {
            messageMap.put("messageType", -1);
        }
        messageMap.put("messageList", meList);
        List<Map<String, Object>> messageList = new ArrayList<>();
        messageList.add(messageMap);
        map.put("messageList", messageList);

        return map;
    }

    @Transactional
    @Override
    public int addConsultingOrder(ConsultingOrder consultingOrder) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_consultingOrder (cid,sid,workType,intention,points,bid,creat_time,isRead)");
        sb.append("value(?,?,?,?,?,?,?,?)");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creat_time = time.format(new Date());
        String intention=null;
        if (consultingOrder.getIntention()==null||consultingOrder.getIntention()==""||consultingOrder.getIntention().isEmpty()){
            intention="咨询";
        }else {
            intention=consultingOrder.getIntention();
        }
        int states = this.jdbcTemplate.update(sb.toString(), consultingOrder.getCid(), consultingOrder.getSid(), consultingOrder.getWorkType(), intention,
                consultingOrder.getPoints(), consultingOrder.getBid(), creat_time, 0);
        String sql = "update yx_custom set bid=?,update_time=? where id=?";
        int states1 = this.jdbcTemplate.update(sql, consultingOrder.getBid(), creat_time, consultingOrder.getCid());
        if (states > 0 && states1 > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int addSaleOrder(SaleOrder saleOrder) {

        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_saleOrder (cid,bid,saleType,confirmIntention,information,isRecommend,age,zodiac,educational,household,recommendNotes,isInterview,interviewType,interviewTime,creat_time)");
        sb.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creat_time = time.format(new Date());

        //判断是否推荐，改变客户isIntention状态
        if (saleOrder.getIsRecommend() == 0) {
            //不推荐
        } else {
            //推荐,改为不是意向客户0
            String sql_intention = "update yx_custom set update_time=?, isIntention=? where id=?";
            int states_intention = this.jdbcTemplate.update(sql_intention, creat_time, 0, saleOrder.getCid());

            String sql_count = "select count(*) from yx_saleOrder where cid=? and isRecommend=1";
            int count = this.jdbcTemplate.queryForObject(sql_count, Integer.class, saleOrder.getCid());
            if (count > 0) {
                //添加过推荐的销售工单
            } else {
                //没有添加过推荐的销售工单
                StringBuffer sb3 = new StringBuffer();
                sb3.append("insert into yx_message (cid,bid,type,creat_time)");
                sb3.append("value(?,?,?,?)");
                //创建销售工单才能，产生流程信息
                //type=0   等待派岗推荐月嫂
                int states_messgage = this.jdbcTemplate.update(sb3.toString(), saleOrder.getCid(), saleOrder.getBid(), 0, creat_time);
            }

        }
        String confirmIntention=null;
        if (saleOrder.getConfirmIntention()==null||saleOrder.getConfirmIntention()==""||saleOrder.getConfirmIntention().isEmpty()){
            confirmIntention="到店";
        }else {
            confirmIntention=saleOrder.getConfirmIntention();
        }

        int states = this.jdbcTemplate.update(sb.toString(), saleOrder.getCid(), saleOrder.getBid(), saleOrder.getSaleType(), confirmIntention, saleOrder.getInformation(), saleOrder.getIsRecommend(), saleOrder.getAge(),
                saleOrder.getZodiac(), saleOrder.getEducational(), saleOrder.getHousehold(), saleOrder.getRecommendNotes(), saleOrder.getIsInterview(), saleOrder.getInterviewType(), saleOrder.getInterviewTime(), creat_time);

        if (states > 0) {
            String sql = "select followCount from yx_custom where id=?";
            Integer followCount = this.jdbcTemplate.queryForObject(sql, Integer.class, saleOrder.getCid());
            if (followCount == null) {
                followCount = 0;
            } else {

            }
            followCount = followCount + 1;
            String sql_update = "update yx_custom set followCount=? where id=?";
            int states_update = this.jdbcTemplate.update(sql_update, followCount, saleOrder.getCid());

            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public int deleteCustom(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");
        String sql_c = "delete from yx_custom where id=?";
        int states_c = this.jdbcTemplate.update(sql_c, cid);

        String sql_d = "delete from yx_order_demand where cid=?";
        int states_d = this.jdbcTemplate.update(sql_d, cid);

        String sql_o = "delete from yx_order where cid=?";
        int states_o = this.jdbcTemplate.update(sql_o, cid);

        String sql_co = "delete from yx_consultingOrder where cid=?";
        int states_co = this.jdbcTemplate.update(sql_co, cid);

        String sql_so = "delete from yx_saleOrder where cid=?";
        int states_so = this.jdbcTemplate.update(sql_so, cid);

        return 1;
    }

    /*new*/
    @SneakyThrows
    @Override
    public Map<String, Object> orderList(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer bid = jsonObject.getInteger("bid");
        String somnus = jsonObject.getString("somnus");
        Integer start = jsonObject.getInteger("start");
        String name = jsonObject.getString("name");
        List list = null;
        StringBuffer sb = new StringBuffer();
        List<Object> queryList = new ArrayList<Object>();
        Map<String, Object> map = new HashMap<>();

        sb.append("select MAX(o.id) as oid,c.id,c.c_name,c.origin,c.channel,c.c_phone,od.starttime,od.endtime,od.work_states,");
        sb.append("od.timetype,o.did,o.order_states,o.return_time,o.type,");
        sb.append("od.service_type,od.service_day,od.level,od.threematorn,o.mid,o.states");
        sb.append("  from yx_order_demand od LEFT JOIN yx_custom c ON (od.cid=c.id) LEFT JOIN yx_order o ON (od.id=o.did)");
        //有销售工单才显示
        sb.append("  left join yx_saleOrder s on (od.cid=s.cid) where 1=1  and  s.isRecommend=1 ");


        StringBuffer sb_count = new StringBuffer();
        //查询全部/待服务/服务中/已完成有多少条
        sb_count.append("select count(distinct(c.id))  from yx_order_demand od LEFT JOIN yx_custom c ON (od.cid=c.id) LEFT JOIN yx_order o ON (od.id=o.did)");
        sb_count.append(" left join yx_saleOrder s on (od.cid=s.cid) where 1=1  and  s.isRecommend=1 ");

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowtime = time.format(new Date());
        String sql_id = "select power from yx_user where id=? ";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, bid);
        StringBuffer sb_name = new StringBuffer();
        //查询客户姓名
        sb_name.append("select c_name from yx_order_demand od  ");
        sb_name.append(" LEFT JOIN yx_custom c ON (od.cid=c.id) LEFT JOIN yx_order o ON (od.id=o.did) ");
        sb_name.append(" left join yx_consultingOrder con on (od.cid=con.cid)");
        sb_name.append(" where c.c_name = ? ");

        Map<String, Object> countMap = new HashMap<>();

        if (power == 20 || power == 100) {

        } else {
            sb.append(" and c.bid =? ");
            sb_count.append(" and c.bid =? ");
            queryList.add(bid);
        }

        String sql_post = " and o.type=0 and o.states<>2 and od.work_states is null  ";
        String sql_before = " and o.states=2   and (od.work_states=0 or od.work_states is null) ";
        String sql_on = " and od.work_states=1  ";
        String sql_after = " and od.work_states=2 ";
        if (somnus.equals("all")) {
            //全部
        }
        if (somnus.equals("post")) {
            //匹配中
            sb.append("  and o.type=0 and o.states<>2 and od.work_states is null ");
            sb_name.append(" and o.type=0 and o.states<>2 and od.work_states is null ");
        }
        if (somnus.equals("before")) {
            //待服务
            sb.append("  and o.states=2   and (od.work_states=0 or od.work_states is null) ");
            sb_name.append(" and o.states=2   and (od.work_states=0 or od.work_states is null) ");
        }
        if (somnus.equals("on")) {
            //服务中
            sb.append(" and od.work_states=1  ");
            sb_name.append("  and od.work_states=1 ");
        }
        if (somnus.equals("after")) {
            //已完成
            sb.append(" and od.work_states=2  ");
            sb_name.append(" and od.work_states=2 ");
        }

        StringBuffer sb_all = new StringBuffer();
        sb_all.append(sb_count);
        StringBuffer sb_post = new StringBuffer();
        sb_post.append(sb_count);
        StringBuffer sb_before = new StringBuffer();
        sb_before.append(sb_count);
        StringBuffer sb_on = new StringBuffer();
        sb_on.append(sb_count);
        StringBuffer sb_after = new StringBuffer();
        sb_after.append(sb_count);


        Integer all = this.jdbcTemplate.queryForObject(sb_count.toString(), Integer.class, queryList.toArray());
        countMap.put("all", all);
        Integer post = this.jdbcTemplate.queryForObject(sb_post.append(sql_post).toString(), Integer.class, queryList.toArray());
        countMap.put("post", post);
        Integer before = this.jdbcTemplate.queryForObject(sb_before.append(sql_before).toString(), Integer.class, queryList.toArray());
        countMap.put("before", before);
        Integer on = this.jdbcTemplate.queryForObject(sb_on.append(sql_on).toString(), Integer.class, queryList.toArray());
        countMap.put("on", on);
        Integer after = this.jdbcTemplate.queryForObject(sb_after.append(sql_after).toString(), Integer.class, queryList.toArray());
        countMap.put("after", after);

        //意向类型客户数量
        String sql_intention = "select count(*)  from  yx_custom c  where  c.isIntention=1";
        Integer intention = jdbcTemplate.queryForObject(sql_intention, Integer.class);
        countMap.put("intention", intention);
        map.put("count", countMap);

        sb_name.append(" GROUP BY o.did ");
        if (name == null || name == "" || name.isEmpty()) {

        } else {
            //手机号
            if (name.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")) {
                if (name.length() > 8) {
                    //电话
                    sb.append(" and c.c_phone = ? ");
                    queryList.add(name);
                } else {
                    //客户id
                    sb.append(" and c.id = ? ");
                    queryList.add(name);
                }
            }

            if (name.matches("^[\\u4e00-\\u9fa5]+$")) {
                //汉字
                try {
                    List<String> list1 = this.jdbcTemplate.queryForList(sb_name.toString(), new Object[]{name}, String.class);
                    if (name.equals(list1.get(0))) {
                        sb.append(" and  c.c_name=? ");
                        queryList.add(list1.get(0));
                    } else {
                    }
                } catch (Exception e) {
                    String xing = name.substring(0, 1);
                    sb.append("  and c.c_name like ?  ");
                    queryList.add(xing + "%");
                }
            }
        }

        sb.append(" GROUP BY o.did  order by c.update_time desc");

        Map<String, Object> page = new HashMap<>();
        page = getPage(start);
        String sql_page = page.get("sql_page").toString();
        //分页
        sb.append(sql_page);
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("cid", rs.getString("id"));
                mp.put("c_name", rs.getString("c_name"));
                mp.put("oid", rs.getString("oid"));
                mp.put("did", rs.getString("did"));
                mp.put("origin", rs.getString("origin"));
                mp.put("channel", rs.getString("channel"));
                mp.put("service_type", rs.getString("service_type"));
                mp.put("service_day", rs.getString("service_day"));
                mp.put("level", rs.getString("level"));
                mp.put("work_states", rs.getString("work_states"));
                mp.put("timetype", rs.getString("timetype"));

                List<Map<String, Object>> photolist = new ArrayList();

                if (rs.getInt("type") == 0) {
                    //没有提交订单
                    String threematorn = rs.getString("threematorn");
                    Integer mid = rs.getInt("mid");
                    Integer states = rs.getInt("states");
                    mp.put("states", states);
                    if (states == 0) {
                        mp.put("photolist", photolist);
                    }
                    if (states == 1) {
                        if (threematorn != null) {
                            JSONArray jsonArray = JSONArray.fromObject(threematorn);
                            List<Integer> listmatorn = (List) jsonArray;
                            List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                            String photo = "";
                            String sql_photo = "select photo from yx_bussiness where mid=?";
                            String name = "";
                            String sql_name = "select name from yx_matorn where id=?";

                            for (int i = 0; i < listmatorn.size(); i++) {
                                Map<String, Object> photoMap = new HashMap<>();

                                try {
                                    photo = jdbcTemplate.queryForObject(sql_photo, String.class, listmatorn.get(i));

                                    if (photo != null && photo != "" && !photo.isEmpty()) {
                                        photoMap.put("photo", photo);
                                    } else {
                                        name = jdbcTemplate.queryForObject(sql_name, String.class, listmatorn.get(i));
                                        photoMap.put("photo", null);
                                        photoMap.put("name", name);
                                    }
                                } catch (Exception e) {

                                }
                                photolist.add(photoMap);
                            }
                            mp.put("photolist", photolist);

                        } else {
                            mp.put("photolist", photolist);
                        }
                    }
                } else {
                    // 提交订单
                    String sql_mid = "select id,mid from yx_order where cid=?";
                    List<Map<String, Object>> midList = jdbcTemplate.queryForList(sql_mid, rs.getString("id"));
                    Integer list_mid = Integer.valueOf(midList.get(midList.size() - 1).get("mid").toString());

                    Integer list_id = Integer.valueOf(midList.get(midList.size() - 1).get("id").toString());
                    Map<String, Object> photoMap = new HashMap<>();
                    List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
                    if (list_mid != 0 && list_id != 0) {
                        //客户最后一个子订单上有月嫂
                        StringBuffer sb1 = new StringBuffer();
                        sb1.append("select m.name,b.isorder,b.photo,o.service_states,o.order_states,o.region,o.o_address from yx_matorn m LEFT JOIN yx_bussiness b on (m.id=b.mid)");
                        sb1.append(" LEFT JOIN yx_order o on(m.id=o.mid)");
                        sb1.append(" where o.id=? ");

                        list1 = jdbcTemplate.query(sb1.toString(), new RowMapper<Map<String, Object>>() {
                            @Override
                            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                                Map<String, Object> mp = new HashMap<String, Object>();
                                mp.put("name", rs.getString("name"));
                                mp.put("photo", rs.getString("photo"));
                                mp.put("isorder", rs.getInt("isorder"));
                                mp.put("service_states", rs.getInt("service_states"));
                                mp.put("order_states", rs.getInt("order_states"));
                                mp.put("o_address", rs.getString("o_address"));
                                mp.put("region", rs.getString("region"));
                                return mp;
                            }
                        }, list_id);

                        photoMap.put("isorder", Integer.valueOf(list1.get(0).get("isorder").toString()));
                        photoMap.put("name", list1.get(0).get("name").toString());
                        photoMap.put("service_states", list1.get(0).get("service_states").toString());
                        // photoMap.put("order_states", list1.get(0).get("order_states"));
                        if (list1.get(0).get("photo") != "" && list1.get(0).get("photo") != null) {
                            photoMap.put("photo", list1.get(0).get("photo").toString());
                        } else {
                            photoMap.put("photo", null);
                        }

                        if (list1.get(0).get("region") != null) {
                            mp.put("isAddress", 1);
                        } else {
                            mp.put("isAddress", 0);
                        }

                        if (rs.getInt("type") == 0) {
                            mp.put("states", rs.getInt("states"));
                        } else {
                            mp.put("order_states", list1.get(0).get("order_states"));
                        }

                    } else {
                        //没有月嫂，等待推荐月嫂
                        String sql_m = "select region,o_address,order_states,service_states from yx_order where id=?";
                        List<Map<String, Object>> list2 = jdbcTemplate.queryForList(sql_m, list_id);
                        photoMap.put("photo", null);
                        photoMap.put("name", "未知");
                        photoMap.put("order_states", list2.get(0).get("order_states"));
                        photoMap.put("service_states", list2.get(0).get("service_states"));
                        if (list2.get(0).get("region") != null && list2.get(0).get("o_address") != null) {
                            mp.put("isAddress", 1);
                        } else {
                            mp.put("isAddress", 0);
                        }

                    }
                    photolist.add(photoMap);
                    mp.put("photolist", photolist);
                }


                String sql_con = "SELECT type FROM yx_message WHERE id=(SELECT MAX(id) as id FROM yx_message WHERE cid=?)";
                //信息类型
                Integer message_type = -1;

                try {
                    message_type = jdbcTemplate.queryForObject(sql_con, Integer.class, rs.getInt("id"));
                } catch (Exception e) {

                }
                mp.put("message_type", message_type);
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
        }, queryList.toArray());

        map.put("list", list);
        return map;
    }

    /*new*/
    @Override
    public int chooseMatorn(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer mid = jsonObject.getInteger("mid");
        Integer oid = jsonObject.getInteger("oid");
        String somnus = jsonObject.getString("somnus");
        String sql_cid = "select cid from yx_order where id=?";
        Integer cid = this.jdbcTemplate.queryForObject(sql_cid, Integer.class, oid);

        //获取母婴顾问id
        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time.format(new Date());
        //修改时间更新
        String sql_update = "update yx_custom set update_time=? where id=?";
        int states_update = this.jdbcTemplate.update(sql_update, nowTime, cid);

        String sql = "update yx_order set mid=?,states=? where id=?";
        int states = 0;

        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb.append("value(?,?,?,?,?)");
        if (somnus.equals("one")) {
            states = this.jdbcTemplate.update(sql, mid, 1, oid);

        }
        if (somnus.equals("two")) {
            states = this.jdbcTemplate.update(sql, mid, 2, oid);
            //type=2 面试成功，等待填写派岗地址
            int states_messgage = this.jdbcTemplate.update(sb.toString(), cid, bid, mid, 2, nowTime);


        }
        if (somnus.equals("three")) {
            String states_order = "update yx_order set mid=?,type=?,states=? where id=?";
            states = this.jdbcTemplate.update(states_order, 0, 0, 1, oid);
            //type=1  派岗推荐月嫂成功，等待母婴顾问选择其中一个
            int states_messgage = this.jdbcTemplate.update(sb.toString(), cid, bid, null, 1, nowTime);
        }

        if (states_update > 0 && states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int updateAddress(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        String o_name = jsonObject.getString("o_name");
        Integer cid = jsonObject.getInteger("cid");
        String o_address = jsonObject.getString("o_address");
        String o_phone = jsonObject.getString("o_phone");
        String region = jsonObject.getString("region");
        String sql = "update yx_order set o_name=?,o_phone=?,region=?,o_address=? where cid=? ";
        int states = this.jdbcTemplate.update(sql, o_name, o_phone, region, o_address, cid);

        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Transactional
    @Override
    public int submitOrder(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer cid = jsonObject.getInteger("cid");
        Integer oid = jsonObject.getInteger("oid");

        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creattime = time1.format(new Date());
        //查询客户的业务员
        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);

        //修改时间更新
        String sql_update = "update yx_custom set update_time=? where id=?";
        int states_update = this.jdbcTemplate.update(sql_update, creattime, cid);
        //查询订单类型，oid，mid
        String sql_mid = "select o.mid,o.cid,od.service_type,od.timetype from yx_order o left join yx_order_demand od on (o.cid=od.cid) where o.id=?";
        List<Map<String, Object>> midList = this.jdbcTemplate.queryForList(sql_mid, oid);

        //查询当天有多少订单
        String sql_count = "select count(*) from yx_order where to_days(creattime) = to_days(now());";
        Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class);
        String number = getOrderNumber(midList.get(0).get("service_type").toString(), midList.get(0).get("timetype").toString(), count);

        //提交订单，修改状态
        String sql = "update yx_order set type=?,o_number=?,creattime=?,order_states=?  where id=?";
        int states_order = this.jdbcTemplate.update(sql, 1, number, creattime, 0, oid);

        //添加步骤信息
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb.append("value(?,?,?,?,?)");
        //type=3 提交派岗，等待派岗确认
        int states_messgage = this.jdbcTemplate.update(sb.toString(), midList.get(0).get("cid"), bid, midList.get(0).get("mid"), 3, creattime);
        if (states_order > 0 && states_messgage > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @SneakyThrows
    @Transactional
    @Override
    public int getArrival(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer oid = jsonObject.getInteger("oid");
        Integer did = jsonObject.getInteger("did");
        Integer service_day = jsonObject.getInteger("service_day");
        String starttime = jsonObject.getString("starttime");


        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        String sql_number = "select mid,cid,o_number from yx_order where id=?";
        list1 = this.jdbcTemplate.queryForList(sql_number, oid);

        String number = list1.get(0).get("o_number").toString();
        String newnumber = number.substring(10, 13);
        Integer mid = Integer.valueOf(list1.get(0).get("mid").toString());
        //修改月嫂订单状态
        String sql_mid = "update yx_bussiness set isorder=? where mid=?";
        int states_mid = this.jdbcTemplate.update(sql_mid, 1, mid);

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String arrival_time = time.format(new Date());//到岗时间


        String sql_cid = "select cid from yx_order where id=?";
        Integer cid = this.jdbcTemplate.queryForObject(sql_cid, Integer.class, oid);

        //查询客户有多少月嫂服务
        String sql_cid_count = "select count(*) from yx_order where cid=?";
        Integer cid_count = this.jdbcTemplate.queryForObject(sql_cid_count, Integer.class, cid);
        int states_order = 0;
        int states_demand = 0;
        if (cid_count == 1) {
            //正常到岗
            String return_time = getNewEndtime(arrival_time, -(service_day));//预计回岗时间
            //  Integer count = getCount(arrival_time, return_time);
            //节假日方法有问题
            Integer count = 0;
            Integer work_day = service_day - count;
            //修改需求开始时间，结束时间
            String sql_demand = "update yx_order_demand set starttime=?,endtime=?,work_day=?,work_states=?,threestates=? where id=?";
            states_demand = this.jdbcTemplate.update(sql_demand, arrival_time, return_time, work_day, 1, 0, did);
            //修改订单上月嫂到岗时间，预计回岗时间
            String sql_order = "update yx_order set arrival_time=?,return_time=?,service_states=?,order_states=? where id=?";
            states_order = this.jdbcTemplate.update(sql_order, arrival_time, return_time, 0, 2, oid);

        } else {
            //转岗换人到岗
            String sql_oneorder = "select oneorder from yx_order_demand where cid=?";
            Integer oneorder = this.jdbcTemplate.queryForObject(sql_oneorder, Integer.class, cid);
            states_demand = 1;
            String return_time = getNewEndtime(arrival_time, -(service_day - oneorder));//预计回岗时间
            //修改订单上月嫂到岗时间，预计回岗时间
            String sql_order = "update yx_order set arrival_time=?,return_time=?,service_states=?,order_states=? where id=?";
            states_order = this.jdbcTemplate.update(sql_order, arrival_time, return_time, 0, 2, oid);
        }


        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String update_time = time1.format(new Date());


        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);
        //修改时间更新
        String sql_update = "update yx_custom set update_time=? where id=?";
        int states_update = this.jdbcTemplate.update(sql_update, update_time, cid);

        StringBuffer sb3 = new StringBuffer();
        sb3.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb3.append("value(?,?,?,?,?)");
        //type=5月嫂到岗，
        int states_messgage = this.jdbcTemplate.update(sb3.toString(), cid, bid, mid, 5, update_time);

        if (states_demand > 0 && states_order > 0 && states_mid > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Transactional
    @Override
    public int substitution(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer oid = jsonObject.getInteger("oid");
        Integer did = jsonObject.getInteger("did");
        Integer mid = jsonObject.getInteger("mid");
        Integer cid = jsonObject.getInteger("cid");
        String somnus = jsonObject.getString("somnus");
        String label = jsonObject.getString("label");
        String age = jsonObject.getString("age");
        String zodiac = jsonObject.getString("zodiac");
        String educational = jsonObject.getString("educational");
        String household = jsonObject.getString("household");
        String remarks = jsonObject.getString("remarks");


        //添加换人原因
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_substitution (cid,label,age,zodiac,educational,household,remarks)");
        sb.append("value (?,?,?,?,?,?,?)");
        int staets_substitution = this.jdbcTemplate.update(sb.toString(), cid, label, age, zodiac, educational, household, remarks);


        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String confirm_time = time.format(new Date());//到岗时间
        //时间格式
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creattime = time1.format(new Date());

        //修改时间更新
        String sql_update = "update yx_custom set update_time=? where id=?";
        int states_update = this.jdbcTemplate.update(sql_update, creattime, cid);

        //查询当前订单中月嫂的等级grade，晋级总天数day,服务天数trueday，
        String sql_sel = "select trueday,grade from yx_bussiness where mid=?";
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql_sel, mid);
        Integer trueday = 0;
        if (list.get(0).get("trueday") != null) {
            trueday = Integer.valueOf(String.valueOf(list.get(0).get("trueday")));
        } else {

        }
        String grade = list.get(0).get("grade").toString();
        //获取订单编号，做判断
        String sql_number = "select arrival_time,stop_time,run_time from  yx_order where id=? ";
        List<Map<String, Object>> numberList = this.jdbcTemplate.queryForList(sql_number, oid);
        String arrival_time = numberList.get(0).get("arrival_time").toString();

        Integer order_day = 0;//月嫂工作天数
        Integer stop_run = 0;//休息总天数
        try {
            order_day = daysBetween(arrival_time, confirm_time);//订单的天数
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (numberList.get(0).get("stop_time") != null && numberList.get(0).get("run_time") != null) {
            //有暂停时
            String stop = numberList.get(0).get("stop_time").toString();
            String run = numberList.get(0).get("run_time").toString();
            List<String> stoplist = new ArrayList<>();
            List<String> runlist = new ArrayList<>();
            stoplist = JSONArray.fromObject(stop);
            runlist = JSONArray.fromObject(run);
            for (int i = 0; i < stoplist.size(); i++) {
                try {
                    stop_run = stop_run + daysBetween(stoplist.get(i), runlist.get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } else {
            //没有暂停
            stop_run = 0;
        }
        order_day = order_day - stop_run;//工作天数减去休息天数
        trueday = trueday + order_day;//月嫂工作天数增加
        //查询需求上的多个订单天数之和
        String sql_day = "select service_day,oneorder from yx_order_demand where id=?";
        List<Map<String, Object>> dayList = this.jdbcTemplate.queryForList(sql_day, did);

        Integer oneorder = 0;//订单需求上已完成的天数
        if (dayList.get(0).get("oneorder") != null) {
            oneorder = Integer.valueOf(dayList.get(0).get("oneorder").toString());
        } else {
            oneorder = 0;
        }
        oneorder = order_day + oneorder;

        //获取新级别
        String newgrade = getNewGrade(grade, trueday);

        //修改月嫂属性
        String sql_b = "update yx_bussiness set grade=?,trueday=?,isorder=? where mid=?";
        int states_b = this.jdbcTemplate.update(sql_b, newgrade, trueday, 0, mid);
        //修改月嫂到岗时间，订单天数，订单状态
        String sql_order = "update yx_order set confirm_time=?,order_day=?,order_states=? where id=?";
        int states_order = this.jdbcTemplate.update(sql_order, confirm_time, order_day, 3, oid);
        //获取上一个月嫂的订单信息
        String sql_o = "select o_name,o_phone,region,o_address,type,o_number from yx_order where id=?";
        List<Map<String, Object>> list_order = this.jdbcTemplate.queryForList(sql_o, oid);
        //查询当天有多少订单
        String sql_count = "select count(*) from yx_order where to_days(creattime) = to_days(now());";
        Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class);

        String number = list_order.get(0).get("o_number").toString().substring(10, 13);
        String new_number = null;
        Integer service_day = Integer.valueOf(dayList.get(0).get("service_day").toString());
        String newconfirm = confirm_time;//前一个回岗，后一个到岗同一天
        String newreturn = getNewEndtime(confirm_time, -(service_day - oneorder));//预计回岗时间

        StringBuffer sb1 = new StringBuffer();
        sb1.append("insert into yx_order (did,mid,cid,o_name,o_phone,region,o_address,type,states,o_number,creattime,arrival_time,return_time,order_states)");
        sb1.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        //添加新的月嫂订单，等待推荐月嫂

        int states_insert = 0;
        int states_demand = 0;
        //流程信息
        StringBuffer sb3 = new StringBuffer();
        sb3.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb3.append("value(?,?,?,?,?)");

        //获取母婴顾问id
        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);

        if (somnus.equals("substitution")) {
            //普通换人
            //type=6
            int states_messgage = this.jdbcTemplate.update(sb3.toString(), cid, bid, mid, 6, creattime);
            //修改订单需求的服务天数，订单需求work_states状态不变，总订单天数
            String sql_demand = "update yx_order_demand set oneorder=?,threestates=? where id=?";
            states_demand = this.jdbcTemplate.update(sql_demand, oneorder, 2, did);
            //结束前一个月嫂的订单
            new_number = newNumber(count, number);//获取新的换人订单号
            states_insert = this.jdbcTemplate.update(sb1.toString(), did, 0, cid, list_order.get(0).get("o_name"), list_order.get(0).get("o_phone"),
                    list_order.get(0).get("region"), list_order.get(0).get("o_address"), 1, 0, new_number, creattime, newconfirm, newreturn, 0);
        }
        if (somnus.equals("post")) {
            //转岗换人
            //type=11 转岗换人，等待派岗重新推荐月嫂
            int states_messgage = this.jdbcTemplate.update(sb3.toString(), cid, bid, mid, 11, creattime);
            new_number = getNewOrder(count);//获取新的转岗订单号
            //修改订单需求的服务天数，订单已完成天数oneorder，转岗状态2-转岗换人
            String sql_demand = "update yx_order_demand set oneorder=?,post_states=? where id=?";
            states_demand = this.jdbcTemplate.update(sql_demand, oneorder, 2, did);
            //添加新的月嫂订单，等待推荐月嫂
            states_insert = this.jdbcTemplate.update(sb1.toString(), did, 0, cid, list_order.get(0).get("o_name"), list_order.get(0).get("o_phone"), list_order.get(0).get("region"),
                    list_order.get(0).get("o_address"), 1, 0, new_number, creattime, newconfirm, newreturn, 0);
        }
        if (states_b > 0 && states_order > 0 && states_demand > 0 && states_insert > 0) {
            return 1;
        } else {
            return 0;
        }

    }


    @Transactional
    @Override
    public int getConfirm(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer oid = jsonObject.getInteger("oid");
        Integer did = jsonObject.getInteger("did");
        Integer mid = jsonObject.getInteger("mid");
        Integer cid = jsonObject.getInteger("cid");
        String somnus = jsonObject.getString("somnus");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String confirm_time = time.format(new Date());//到岗时间
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time1.format(new Date());
        //修改时间更新
        String sql_update = "update yx_custom set update_time=? where id=?";
        int states_update = this.jdbcTemplate.update(sql_update, nowTime, cid);

        //查询当前订单中月嫂的等级grade，晋级总天数day,服务天数trueday，
        String sql_sel = "select trueday,grade from yx_bussiness where mid=?";
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql_sel, mid);

        Integer trueday = 0;
        if (list.get(0).get("trueday") != null) {
            trueday = Integer.valueOf(String.valueOf(list.get(0).get("trueday")));
        } else {

        }
        String grade = list.get(0).get("grade").toString();
        //获取到岗时间，暂停时间，开始时间
        String sql_number = "select o_number,arrival_time,stop_time,run_time from  yx_order where id=? ";
        List<Map<String, Object>> numberList = this.jdbcTemplate.queryForList(sql_number, oid);
        String arrival_time = numberList.get(0).get("arrival_time").toString();

        Integer order_day = 0;//月嫂工作天数
        Integer stop_run = 0;//休息总天数
        try {
            order_day = daysBetween(arrival_time, confirm_time);//订单的天数
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (numberList.get(0).get("stop_time") != null && numberList.get(0).get("run_time") != null) {
            //有暂停时
            String stop = numberList.get(0).get("stop_time").toString();
            String run = numberList.get(0).get("run_time").toString();
            List<String> stoplist = new ArrayList<>();
            List<String> runlist = new ArrayList<>();
            stoplist = JSONArray.fromObject(stop);
            runlist = JSONArray.fromObject(run);
            for (int i = 0; i < stoplist.size(); i++) {
                try {
                    stop_run = stop_run + daysBetween(stoplist.get(i), runlist.get(i));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        } else {
            //没有暂停
            stop_run = 0;
        }
        order_day = order_day - stop_run;//工作天数减去休息天数
        trueday = trueday + order_day;//月嫂工作天数增加
        //查询需求上的多个订单天数之和
        String sql_day = "select service_day,oneorder from yx_order_demand where id=?";
        List<Map<String, Object>> dayList = this.jdbcTemplate.queryForList(sql_day, did);
        Integer oneorder = 0;
        if (dayList.get(0).get("oneorder") != null) {
            oneorder = Integer.valueOf(dayList.get(0).get("oneorder").toString());
        } else {
            oneorder = 0;
        }
        oneorder = order_day + oneorder;


        String newgrade = getNewGrade(grade, trueday);//获取新级别


        String sql_b = "update yx_bussiness set grade=?,trueday=?,isorder=? where mid=?";

        //修改月嫂到岗时间，订单天数，订单状态
        String sql_order = "update yx_order set confirm_time=?,order_day=?,order_states=? where id=?";
        int states_order = this.jdbcTemplate.update(sql_order, confirm_time, order_day, 3, oid);

        int states_demand = 0;
        int states_insert = 0;
        int states_b = 0;
        //添加流程信息
        StringBuffer sb3 = new StringBuffer();
        sb3.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb3.append("value(?,?,?,?,?)");
        //获取母婴顾问id
        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, cid);

        if (somnus.equals("confirm")) {
            //回岗
            //type=20 回岗，
            int states_messgage = this.jdbcTemplate.update(sb3.toString(), cid, bid, mid, 20, nowTime);
            //修改月嫂订单状态
            states_b = this.jdbcTemplate.update(sql_b, newgrade, trueday, 0, mid);
            states_insert = 1;
            //修改订单需求的服务天数，结束时间订单状态work_states，订单已完成天数oneorder
            String sql_demand = "update yx_order_demand set service_day=?,endtime=?,work_states=?,oneorder=? where id=?";
            states_demand = this.jdbcTemplate.update(sql_demand, oneorder, confirm_time, 2, oneorder, did);
        }
        if (somnus.equals("post")) {

            //修改订单已完成的天数oneorder，订单是否有换人的状态threestates，转岗状态post_states-1-转岗，
            String sql_demand = "update yx_order_demand set oneorder=?,threestates=?,post_states=? where id=?";
            states_demand = this.jdbcTemplate.update(sql_demand, oneorder, 0, 1, did);
            //修改月嫂订单状态
            states_b = this.jdbcTemplate.update(sql_b, newgrade, trueday, 1, mid);
            //获取上一个月嫂的订单信息
            String sql_o = "select o_name,o_phone,region,o_address,type,states,o_number from yx_order where id=?";
            List<Map<String, Object>> list_order = this.jdbcTemplate.queryForList(sql_o, oid);
            //查询当天有多少订单
            String sql_count = "select count(*) from yx_order where to_days(creattime) = to_days(now());";
            Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class);
            String new_number = null;

            Integer service_day = Integer.valueOf(dayList.get(0).get("service_day").toString());
            String newconfirm = confirm_time;//前一个回岗，后一个到岗同一天
            String newreturn = getNewEndtime(confirm_time, -(service_day - oneorder));

            StringBuffer sb1 = new StringBuffer();
            sb1.append("insert into yx_order (did,mid,cid,o_name,o_phone,region,o_address,type,states,o_number,creattime,arrival_time,return_time,service_states,order_states)");
            sb1.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

            // type=7 转岗，
            int states_messgage = this.jdbcTemplate.update(sb3.toString(), cid, bid, mid, 7, nowTime);
            new_number = getNewOrder(count);//转岗获取新的订单号
            //转岗   添加新的订单，月嫂不变直接上岗
            states_insert = this.jdbcTemplate.update(sb1.toString(), did, mid, cid, list_order.get(0).get("o_name"), list_order.get(0).get("o_phone"), list_order.get(0).get("region"), list_order.get(0).get("o_address"),
                    list_order.get(0).get("type"), list_order.get(0).get("states"), new_number, nowTime, newconfirm, newreturn, 0, 2);


        }


        if (states_update > 0 && states_b > 0 && states_order > 0 && states_demand > 0 && states_insert > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Transactional
    @Override
    public int continueOrder(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer oid = jsonObject.getInteger("oid");
        Integer did = jsonObject.getInteger("did");
        Integer cid = jsonObject.getInteger("cid");
        Integer price = jsonObject.getInteger("price");//续岗总金额
        Integer continue_onePrice = jsonObject.getInteger("continue_onePrice");//续岗单价金额
        Integer continue_day = jsonObject.getInteger("continue_day");//续岗天数
        String service_type = jsonObject.getString("service_type");
        String timetype = jsonObject.getString("timetype");

        String sql_demand = "select service_day,oneorder from yx_order_demand where id=?";
        Map<String, Object> demandMap = this.jdbcTemplate.queryForMap(sql_demand, did);

        //已经完成的天数
        Integer oneorder = 0;
        if (demandMap.get("oneorder") != null) {
            oneorder = Integer.valueOf(demandMap.get("oneorder").toString());
        } else {

        }

        String sql_time = "select stop_time,run_time from  yx_order where cid=? ";
        List<Map<String, Object>> timeList = this.jdbcTemplate.queryForList(sql_time, cid);
        Integer sum_stop = 0;//休息总天数
        Integer stop_run = 0;
        for (int i = 0; i < timeList.size(); i++) {
            if (timeList.get(i).get("stop_time") != null && timeList.get(i).get("run_time") != null) {
                //有暂停时
                String stop = timeList.get(i).get("stop_time").toString();
                String run = timeList.get(i).get("run_time").toString();
                List<String> stoplist = new ArrayList<>();
                List<String> runlist = new ArrayList<>();
                stoplist = JSONArray.fromObject(stop);
                runlist = JSONArray.fromObject(run);
                for (int j = 0; j < stoplist.size(); j++) {
                    try {
                        stop_run = stop_run + daysBetween(stoplist.get(j), runlist.get(j));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                sum_stop = sum_stop + stop_run;
            } else {
                //没有暂停
                sum_stop = sum_stop + 0;
            }

        }


        //剩下需要的天数(需要加上所有休息总天数)
        Integer needDay = continue_day - oneorder + sum_stop;

        String sql_order = "select arrival_time,o_number from yx_order where id=?";
        Map<String, Object> orderMap = this.jdbcTemplate.queryForMap(sql_order, oid);

        //当前月嫂到岗时间
        String arrival_time = orderMap.get("arrival_time").toString();
        String endtime = getNewEndtime(arrival_time, -needDay);


        String number = getOrderNumber(service_type, timetype, 0);
        String num = number.substring(10, 13);

        String sql_list = "select id,o_number from yx_order where cid=? ";
        List<Map<String, Object>> orderList = this.jdbcTemplate.queryForList(sql_list, cid);

        for (int i = 0; i < orderList.size(); i++) {
            String o_number = orderList.get(i).get("o_number").toString();
            String o_num = orderList.get(i).get("o_number").toString().substring(10, 13);

            if (num.equals(o_num)) {
                //订单类型不变

            } else {
                String sql_number = "update yx_order set o_number=? where id=?";
                String n1 = o_number.substring(0, 10);
                String n2 = o_number.substring(13);
                //新的订单
                String new_number = n1 + num + n2;
                int states_number = this.jdbcTemplate.update(sql_number, new_number, orderList.get(i).get("id"));
            }

        }


        Integer truePrice = 0;//订单金额
        Integer onePrice = 0;//订单单价
        if (continue_onePrice == 0 && price == 0) {
            //会所续岗
            String num1 = orderList.get(0).get("o_number").toString().substring(10, 13);
            if (num1.equals("CCC")) {
                //月子会所
                truePrice = 200 * continue_day;
            }
            if (num1.equals("CCP")) {
                //医院
                truePrice = 220 * continue_day;
            }
        } else {
            //居家续岗
            if (price != 0) {
                truePrice = price;
                onePrice = price / continue_day;
            } else {
                //单价
                truePrice = continue_day * continue_onePrice;
                onePrice = continue_onePrice;
            }

        }


        //修改订单需求服务类型，天数，结束时间，服务单价
        String sql_update_demand = "update yx_order_demand set service_type=?,service_day=?,endtime=?,timetype=?,price=?,onePrice=? where id=? ";
        int states_update_demand = this.jdbcTemplate.update(sql_update_demand, service_type, continue_day, endtime, timetype, truePrice, onePrice, did);

        String sql_update_order = "update yx_order set return_time=?,isContinue=? where id=?";
        int states_update_order = this.jdbcTemplate.update(sql_update_order, endtime, 1, oid);


        if (states_update_demand > 0 && states_update_order > 0) {
            System.out.println("1 = " + 1);
            return 1;
        } else {
            System.out.println("0 = " + 0);
            return 0;
        }

    }

    @Override
    public int getMatornNull(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型
        Integer did = jsonObject.getInteger("did");
        Integer oid = jsonObject.getInteger("oid");
        //推荐月嫂清空
        String sql_d = "update yx_order_demand set threematorn=? where id=?";
        int states_d = this.jdbcTemplate.update(sql_d, null, did);
        //选定月嫂清空,等待重新推荐月嫂
        String sql_o = "update yx_order set states=? where id=?";
        int states_o = this.jdbcTemplate.update(sql_o, 0, oid);
        if (states_d > 0 && states_o > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @SneakyThrows
    @Override
    public int runService(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer oid = jsonObject.getInteger("oid");
        Integer did = jsonObject.getInteger("did");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String run_time = time.format(new Date());

        String sql_s = "select mid,cid,return_time,stop_time,run_time from yx_order where id=?";
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql_s, oid);
        String stop = list.get(0).get("stop_time").toString();

        List<String> stoplist = new ArrayList<>();
        List<String> runlist = new ArrayList<>();
        if (list.get(0).get("run_time") != null) {
            runlist = JSONArray.fromObject(list.get(0).get("run_time").toString());
            runlist.add(run_time);
        } else {
            runlist.add(run_time);
        }

        stoplist = JSONArray.fromObject(stop);
        String stop_time = stoplist.get(stoplist.size() - 1);//最近的一个暂停时间
        Integer day = daysBetween(stop_time, run_time);//暂停天数
        System.out.println("day = " + day);
        String newreturn = getNewEndtime(list.get(0).get("return_time").toString(), -day);//新的预计回岗时间
        //修改订单上预计回岗时间，暂停状态，开启服务时间
        String sql_o = "update  yx_order set return_time=?,service_states=?,run_time=? where id=?";
        int states_o = this.jdbcTemplate.update(sql_o, newreturn, 0, runlist.toString(), oid);
        //修改订单需求上的结束时间
        String sql_demand = "update yx_order_demand set endtime=? where id=? ";
        int states_demand = this.jdbcTemplate.update(sql_demand, newreturn, did);

        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time1.format(new Date());
        //添加流程信息
        StringBuffer sb3 = new StringBuffer();
        sb3.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb3.append("value(?,?,?,?,?)");
        //获取母婴顾问id
        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, list.get(0).get("cid"));
        //type=10 开启服务，
        int states_messgage = this.jdbcTemplate.update(sb3.toString(), list.get(0).get("cid"), bid, list.get(0).get("mid"), 10, nowTime);

        if (states_o > 0 && states_demand > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int stopService(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer oid = jsonObject.getInteger("oid");
        Integer did = jsonObject.getInteger("did");
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String stop_time = time.format(new Date());

        //查询订单中有没有暂停时间
        String sql_time = "select mid,cid,stop_time from yx_order where id=?";
        List<Map<String, Object>> timeList = this.jdbcTemplate.queryForList(sql_time, oid);

        List<String> stoplist = new ArrayList<>();
        String sql = "update yx_order set service_states=?,stop_time=? where id=?";
        if (timeList.get(0).get("stop_time") != null) {
            //有暂停
            stoplist = JSONArray.fromObject(timeList.get(0).get("stop_time").toString());
            stoplist.add(stop_time);
        } else {
            stoplist.add(stop_time);
        }

        int states = this.jdbcTemplate.update(sql, 1, stoplist.toString(), oid);
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowTime = time1.format(new Date());
        //添加流程信息
        StringBuffer sb3 = new StringBuffer();
        sb3.append("insert into yx_message (cid,bid,mid,type,creat_time)");
        sb3.append("value(?,?,?,?,?)");
        //获取母婴顾问id
        String sql_bid = "select bid from yx_custom where id=?";
        Integer bid = this.jdbcTemplate.queryForObject(sql_bid, Integer.class, timeList.get(0).get("cid"));

        //type=9 暂停服务，
        int states_messgage = this.jdbcTemplate.update(sb3.toString(), timeList.get(0).get("cid"), bid, timeList.get(0).get("mid"), 9, nowTime);

        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public Map<String, Object> updateBussiness(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String photo = jsonObject.getString("photo");
        String name = jsonObject.getString("name");
        String phone = jsonObject.getString("phone");
        Integer bid = jsonObject.getInteger("bid");

        Map<String, Object> map = new HashMap<>();

        String sql = "update yx_user set phone=?,name=?,photo=? where id=?";
        int states = this.jdbcTemplate.update(sql, phone, name, photo, bid);
        if (states > 0) {
            map.put("states", states);
            map.put("phone", phone);
            map.put("name", name);
            map.put("photo", photo);
        } else {
            map.put("states", states);
        }
        return map;

    }


}
