package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.ExcelDao;
import com.example.entiy.Question;
import com.example.entiy.Reservation;
import lombok.SneakyThrows;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;
import static com.example.util.NumberUtil.getNumberOnePrice;
import static com.example.util.PageUtil.getAllPage;
import static com.example.util.PeriodUtil.*;
import static com.example.util.PriceUtil.*;
import static com.example.util.Year.getAge;

@Repository
public class ExcelDaoImpl implements ExcelDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;


    @Override
    public List<Map<String, Object>> out_matorn(String somnus, String agent) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        StringBuffer sb = new StringBuffer();
        sb.append("select yx_bussiness.number,yx_matorn.name,yx_contact.phone,yx_matorn.uid,yx_matorn.lid,yx_matorn.born,yx_bussiness.grade,");
        sb.append(" yx_matorn.idcard,yx_contact.bank_card,yx_contact.bank_name");
        sb.append(" from yx_matorn left join yx_bussiness on (yx_matorn.id=yx_bussiness.mid) left join yx_contact on (yx_matorn.id=yx_contact.mid)");
        sb.append(" WHERE 1=1  ");

        if (somnus.equals("all")) {
            sb.append("  and yx_bussiness.isblack = 0 ");
        }
        if (somnus.equals("shelf")) {
            sb.append(" and yx_bussiness.shelf = 1  ");
        }
        if (somnus.equals("black")) {
            sb.append(" and yx_bussiness.isblack = 1");
        }

        List<Object> queryList = new ArrayList<Object>();


        if (agent == null || agent == "" || agent.isEmpty()) {

        } else {
            String sql_u = "select id from yx_user where name=? and power=1";
            Integer id = this.jdbcTemplate.queryForObject(sql_u, Integer.class, agent);
            if (id != 22) {
                sb.append(" and yx_matorn.uid =? ");
                queryList.add(id.toString());
            } else {
                sb.append(" and (yx_matorn.uid=0 or yx_matorn.uid=22 ) ");
            }

        }
        sb.append("  ORDER BY yx_matorn.uid");
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> map = new HashMap<String, Object>();

                map.put("number", rs.getString("number"));
                map.put("phone", rs.getString("phone"));
                map.put("name", rs.getString("name"));
                map.put("grade", rs.getString("grade"));
                map.put("idcard", rs.getString("idcard"));
                map.put("bank_card", rs.getString("bank_card"));
                map.put("bank_name", rs.getString("bank_name"));
                Integer uid = rs.getInt("uid");
                Integer lid = rs.getInt("lid");

                if (uid == 22 || uid == 0) {
                    map.put("agent", "待分配");
                } else {
                    String sql = "select name from yx_user where id=?";
                    String agent = jdbcTemplate.queryForObject(sql, String.class, uid);
                    map.put("agent", agent);
                }

                return map;
            }
        }, queryList.toArray());

        return list;
    }


    @Override
    public List<Reservation> outExcelReservation(Integer uid) {

        StringBuffer sb = new StringBuffer();
//
//        if (time != null && time != "") {
//            // to_days(creattime) = to_days(now()) 当天
//            sb.append("select * from yx_reservation where to_days(time) = to_days(now())");//当天
//        }
        sb.append("select * from yx_reservation ");
        String sql = "select power,mechanism_name from yx_user where id=?";

        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
        list1 = this.jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("power", rs.getInt("power"));
                mp.put("mechanism_name", rs.getString("mechanism_name"));
                return mp;
            }
        }, uid);
        String mechanism_name = list1.get(0).get("mechanism_name").toString();
        List<Reservation> list = null;
        if (list1.get(0).get("power").toString().equals("100")) {
            list = jdbcTemplate.query(sb.toString(), new Object[]{}, new BeanPropertyRowMapper(Reservation.class));
        } else {
            sb.append("  where origin=?");
            list = jdbcTemplate.query(sb.toString(), new Object[]{mechanism_name}, new BeanPropertyRowMapper(Reservation.class));
        }
        return list;

    }

    @Override
    public List<Map<String, Object>> outFiledMatorn(String json) {
        return null;
    }

    @Override
    public List<Map<String, Object>> outFiledCustom(Integer uid, String origin, String origin_channel, String intention, Integer sname, Integer bname, String orderSpeed, String service_type, String timetype, String level, String returnType, String number, String powerFiled, String start_time, String end_time) {
        List<Map<String, Object>> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        List<Object> queryList = new ArrayList<Object>();

        List<Map<String, Object>> powerFiledList = new ArrayList<>();
        powerFiled = powerFiled.replace("[", "{");
        powerFiled = powerFiled.replace("]", "}");
        JSONArray jsonArray = JSONArray.fromObject("[" + powerFiled + "]");
        List<Map<String, Object>> powerFiledList1 = new ArrayList<>();

        powerFiledList1 = (List) jsonArray;
        end_time = getNewEndtime(end_time, -1);
        Map<String, Object> powerFiledMap = new LinkedHashMap();//保证put值有序
        for (int i = 0; i < powerFiledList1.size(); i++) {
            for (String key : powerFiledList1.get(i).keySet()) {
                Integer value = Integer.valueOf(powerFiledList1.get(i).get(key).toString());
                powerFiledMap.put(key, value);
            }
        }
        powerFiledList.add(powerFiledMap);
        String powerSql = " and d.service_type='居家服务' ";

        Integer flag = 0;//判断后面的语句是否需要加分组
        if (service_type.isEmpty() && bname == 0 && orderSpeed.isEmpty() && returnType.isEmpty() && intention.isEmpty() && origin.isEmpty() && sname == 0) {
            //所有条件为空
            if (uid == 173) {
                flag = 1;
                sb.append("select MAX(o.id) as oid,c.id,c.sid,c.bid,c.origin,c.channel,c.c_name,c.w_name,c.c_phone,c.production_situation,c.nodate,c.production_date,c.creat_time,");
                sb.append("c.address ");
                sb.append(" from yx_custom c left join yx_order_demand d on(c.id=d.cid) left join yx_order o on(c.id=o.cid) ");
                sb.append("where 1=1");
                sb.append(powerSql);

            } else {
                flag = 0;
                sb.append("select c.id,c.sid,c.bid,c.origin,c.channel,c.c_name,c.w_name,c.c_phone,c.production_situation,c.nodate,c.production_date,c.creat_time,c.address");
                sb.append(" from yx_custom c where 1=1 ");


            }

        } else {
            //订单类型service_type/timetype/level，intention意向类型,母婴顾问banme，订单进度orderSpeed，回岗类型returnType，
            //条件可空可不空，
            flag = 1;
            if (intention.isEmpty()) {
                //意向类型intention为空
                System.out.println("意向类型intention为空___");
                sb.append("select MAX(o.id) as oid,c.id,c.sid,c.bid,c.origin,c.channel,c.c_name,c.w_name,c.c_phone,c.production_situation,c.nodate,c.production_date,c.creat_time,");
                sb.append("c.address ");
                sb.append(" from yx_custom c left join yx_order_demand d on(c.id=d.cid) left join yx_order o on(c.id=o.cid) ");
                sb.append("where 1=1");

            } else {
                //意向类型intention不为空
                //还有表未创建，
                sb.append("select MAX(o.id) as oid,c.id,c.sid,c.bid,c.origin,c.channel,c.c_name,c.w_name,c.c_phone,c.production_situation,c.nodate,c.production_date,c.creat_time,");
                sb.append("c.address ");
                sb.append(" from yx_custom c left join yx_order_demand d on(c.id=d.cid) left join yx_order o on(c.id=o.cid) ");
                sb.append(" left join yx_saleOrder s on (c.id=s.cid)");
                sb.append(" where  s.id=(SELECT MAX(id) from yx_saleOrder WHERE yx_saleOrder.cid=c.id) ");
                sb.append(" and s.confirmIntention=? ");

                queryList.add(intention);
            }

            if (uid == 173) {
                sb.append(powerSql);

            } else {

            }
            //订单类型orderType可空可不空
            if (service_type.isEmpty()) {
                //订单类型service_type为空
            } else {
                sb.append(" and d.service_type=?");
                queryList.add(service_type);
                if (timetype.isEmpty()) {

                } else {
                    sb.append(" and d.timetype=?");
                    queryList.add(timetype);
                    if (level.isEmpty()) {
                    } else {
                        sb.append(" and d.level=?");
                        queryList.add(level);
                    }
                }
            }
            //订单进度orderSpeed可空可不空
            if (orderSpeed.isEmpty()) {
                //订单进度orderSpeed为空
            } else {
                if (orderSpeed.equals("咨询中")) {
                    sb.append(" and o.states=0 and d.work_states=0");
                }
                if (orderSpeed.equals("匹配中")) {
                    sb.append(" and o.states=1 and d.work_states=0");
                }
                if (orderSpeed.equals("待服务")) {
                    sb.append(" and o.states=2 and d.work_states=0");
                }
                if (orderSpeed.equals("服务中")) {
                    sb.append(" and d.work_states=1");
                }
                if (orderSpeed.equals("已完成")) {
                    sb.append(" and d.work_states=2");
                }
            }
            //回岗类型returnType可空可不空
            if (returnType.isEmpty()) {
                //回岗类型returnType为空
            } else {
                sb.append(" and d.work_states=2 ");
                if (returnType.equals("正常回岗")) {
                    sb.append(" and d.return_time=d.confirm_time");
                }
                if (returnType.equals("续单回岗")) {
                    sb.append(" and d.return_time>d.confirm_time");
                }
                if (returnType.equals("退单回岗回岗")) {
                    sb.append(" and d.return_time<d.confirm_time");
                }
            }
            //来源可空可不空
            if (origin.isEmpty() || origin == null) {
                //来源为空
            } else {
                sb.append(" and c.origin=?");
                queryList.add(origin);
            }
            //渠道可空可不空
            if (origin_channel.isEmpty()) {
                //渠道为空
            } else {
                if (origin_channel.length() > 3) {
                    sb.append(" and c.channel=?");
                    queryList.add(origin);
                } else {
                    sb.append(" and c.channel like ?");
                    queryList.add(origin_channel + "%");
                }

            }

            //咨询顾问可空可不空
            if (sname == 0) {
                //咨询顾问为空
            } else {
                sb.append(" and c.sid=?");
                queryList.add(sname);
            }
            //母婴顾问banme可空可不空
            if (bname == 0) {
                //母婴顾问banme为空
            } else {
                sb.append(" and c.bid=? ");
                queryList.add(bname);
            }


        }


        //开始时间与结束时间
        if (start_time.isEmpty() || start_time.isEmpty()) {

        } else {
            sb.append(" and c.creat_time>=? and c.creat_time<?");
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
                    queryList.add(number);
                } else {
                    //id
//                    String a = number.substring(2, number.length());
//                    Integer aa = Integer.valueOf(a);
                    sb.append(" and c.id = ? ");
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
                        queryList.add(list1.get(0));
                    } else {
                        System.out.println("________TRUE____");
                    }
                } catch (Exception e) {
                    System.out.println("________TASDSDASDS");
                    if (number.length() >= 1) {
                        String name = number.substring(0, 1);
                        sb.append("  and c.c_name like ?   ");
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
                queryList.add(aa);
            }
        }

        String sql_id = "select power from yx_user where id=?";
        Integer power = this.jdbcTemplate.queryForObject(sql_id, Integer.class, uid);
        if (power == 100) {

        }
        if (power == 7) {
            sb.append(" and c.sid=?");
            queryList.add(uid);
        }

        if (flag == 0) {

        } else {
            sb.append(" GROUP BY o.did");
        }
        sb.append(" order by c.creat_time desc ");

        List<Map<String, Object>> finalPowerFiledList = powerFiledList;
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();

                mp.put("cid", rs.getInt("id"));
                if (finalPowerFiledList.get(0).get("c_name") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("c_name").toString()) != 0) {
                        if (rs.getString("c_name") != null) {
                            mp.put("c_name", rs.getString("c_name"));
                        } else {
                            mp.put("c_name", null);
                        }
                    } else {
                    }
                } else {
                }

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

                if (finalPowerFiledList.get(0).get("c_phone") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("c_phone").toString()) != 0) {
                        mp.put("c_phone", rs.getString("c_phone"));
                    } else {

                    }
                } else {

                }

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
                if (finalPowerFiledList.get(0).get("origin") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("origin").toString()) != 0) {
                        mp.put("origin", rs.getString("origin") + rs.getString("channel"));
                    } else {

                    }
                } else {

                }
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
                if (finalPowerFiledList.get(0).get("creat_time") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("creat_time").toString()) != 0) {
                        mp.put("creat_time", rs.getString("creat_time"));
                    } else {

                    }
                } else {

                }

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

                if (finalPowerFiledList.get(0).get("followCount") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("followCount").toString()) != 0) {
                        String sql_followCount = "select followCount from yx_custom where id=?";
                        Integer followCount = jdbcTemplate.queryForObject(sql_followCount, Integer.class, rs.getInt("id"));
                        if (followCount == null) {
                            followCount = 0;
                        } else {

                        }
                        mp.put("followCount", followCount);//更进次数
                    } else {

                    }
                } else {

                }
                if (finalPowerFiledList.get(0).get("orderSpeed") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("orderSpeed").toString()) != 0) {
                        String sql_orderSpeed_count = "select count(*) from yx_order where cid=?";
                        Integer orderSpeed_count = jdbcTemplate.queryForObject(sql_orderSpeed_count, Integer.class, rs.getInt("id"));
                        if (orderSpeed_count > 0) {
                            //判断有没有订单
                            String sql_oid = "select MAX(id) as oid from yx_order where cid=? group by cid";
                            List<Map<String, Object>> oidList = jdbcTemplate.queryForList(sql_oid, rs.getInt("id"));
                            StringBuffer sb_states = new StringBuffer();
                            sb_states.append("select d.work_states,o.states from yx_order o");
                            sb_states.append(" left join yx_order_demand  d on (o.cid=d.cid) where o.cid=? and o.id=?");
                            List<Map<String, Object>> statesList = jdbcTemplate.queryForList(sb_states.toString(), rs.getInt("id"), oidList.get(0).get("oid"));
                            Integer work_states = 0;
                            if (statesList.get(0).get("work_states") != null) {
                                work_states = Integer.valueOf(statesList.get(0).get("work_states").toString());
                            } else {

                            }


                            if (work_states == 0) {
                                if (statesList.get(0).get("states") != null) {
                                    Integer states = Integer.valueOf(statesList.get(0).get("states").toString());
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
                                    mp.put("orderSpeed", "咨询中");
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

                    }
                } else {

                }
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
                if (finalPowerFiledList.get(0).get("returnType") != null) {
                    if (Integer.valueOf(finalPowerFiledList.get(0).get("returnType").toString()) != 0) {
                        String sql_serviceCount = "select count(*) from yx_order where cid=?";
                        Integer sql_count = jdbcTemplate.queryForObject(sql_serviceCount, Integer.class, rs.getInt("id"));
                        if (sql_count > 0) {
                            //判断客户有没有订单表
                            String sql_oid = "select MAX(id) as oid from yx_order where cid=? group by cid";
                            List<Map<String, Object>> oidList = jdbcTemplate.queryForList(sql_oid, rs.getInt("id"));
                            String sql_timeList = "select return_time,confirm_time from yx_order where id=? ";
                            List<Map<String, Object>> timeList = jdbcTemplate.queryForList(sql_timeList, oidList.get(0).get("oid"));
                            if (timeList.get(0).get("return_time") != null && timeList.get(0).get("confirm_time") != null) {
                                //判断客户有订单表莉，预计回岗时间和回岗时间是否有值
                                String return_time = timeList.get(0).get("return_time").toString();
                                String confirm_time = timeList.get(0).get("confirm_time").toString();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date return_ = sdf.parse(return_time);
                                Date confirm_ = sdf.parse(confirm_time);
                                if (return_.before(confirm_)) {
                                    mp.put("returnType", "续单回岗");
                                }
                                if (confirm_.before(return_)) {
                                    mp.put("returnType", "退单回岗");
                                }
                                if (return_.equals(confirm_)) {
                                    mp.put("returnType", "正常回岗");
                                }
                            } else {
                                mp.put("returnType", "未知");
                            }
                        } else {
                            mp.put("returnType", "未知");
                        }
                    } else {
                    }
                } else {

                }

                List<Map<String, Object>> finalPowerFiledList = powerFiledList;
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

                StringBuffer sb_price = new StringBuffer();
                sb_price.append("select o_number,arrival_time,order_day,order_states,od.onePrice,od.service_day,od.price from yx_order_demand od");
                sb_price.append(" left join yx_order o on(od.cid=o.cid) where o.cid=? and o.order_states>=2");

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
        return list;


    }


    @Override
    public int addPDf(Integer mid, String url) {

        String sql = "update yx_bussiness set reportPdf=? where mid=? ";
        int states = this.jdbcTemplate.update(sql, url, mid);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int addQuestion(Question question) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_question (questionType,type,question,A,B,C,D,answer)");
        sb.append("value(?,?,?,?,?,?,?,?)");
        int states = 0;
        if (question.getType() == 1) {
            //单选题
            states = this.jdbcTemplate.update(sb.toString(), question.getQuestionType(), question.getType(), question.getQuestion(), question.getA(),
                    question.getB(), question.getC(), question.getD(), question.getAnswer());
        }
        if (question.getType() == 2) {
            //多选题
            Integer size = question.getAnswer().length();
            for (int i = 0; i < size; i++) {
                System.out.println("i = " + question.getAnswer().substring(i, i + 1));

            }


        }
        if (question.getType() == 2) {
            //判断题


        }
        return 0;
    }


    @Override
    public List<Map<String, Object>> outCustomService(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        StringBuffer sb = new StringBuffer();
        sb.append("select c.id,o.id as oid,c.origin,c.channel,c.c_name,c.c_phone,od.service_day,od.price,c.bid,o.mid,ori.source,m.uid,m.name,o.arrival_time, ");
        sb.append("o.confirm_time,od.oneorder,o.o_number,od.service_type,od.timetype,od.level,o.order_states,od.onePrice from yx_custom c");
        sb.append(" left join yx_order_demand od on (c.id=od.cid) left join yx_order o on (c.id=o.cid) left JOIN yx_origin ori on (o.mid=ori.mid) ");
        sb.append(" left JOIN yx_matorn m on (o.mid=m.id)");
        sb.append(" where od.work_states>=1 and od.service_day<>0 and o.order_states>=2 order by c.creat_time desc,oid ");
        //订单服务中
        System.out.println("sb = " + sb);
        List<Map<String, Object>> list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("cid", rs.getInt("id"));
                mp.put("c_origin", rs.getString("origin") + rs.getString("channel"));
                mp.put("c_name", rs.getString("c_name"));
                mp.put("c_phone", rs.getString("c_phone"));
                mp.put("o_number", rs.getString("o_number"));

                if (rs.getString("service_type").equals("月子会所服务")) {
                    mp.put("order_type", rs.getString("service_type") + rs.getString("timetype"));
                } else {
                    mp.put("order_type", rs.getString("service_type") + rs.getString("timetype") + rs.getString("level"));
                }

                if (rs.getInt("order_states") == 2) {
                    mp.put("order_states", "服务中");
                }
                if (rs.getInt("order_states") == 3) {
                    mp.put("order_states", "已完成");
                }


                mp.put("o_number", rs.getString("o_number"));
                mp.put("service_day", rs.getInt("service_day"));
                mp.put("price", rs.getInt("price"));

                Integer onePrice = 0;

                if (rs.getString("onePrice") != null) {
                    onePrice = Integer.valueOf(rs.getString("onePrice"));
                } else {
                    String number = rs.getString("o_number").substring(10, 13);

                    onePrice = getNumberOnePrice(number, rs.getInt("service_day"), rs.getInt("price"));
                }

                //服务单价
                mp.put("onePrice", onePrice);

                //获取母婴顾问id
                String sql_bname = "select name from yx_user where id=?";
                String bname = jdbcTemplate.queryForObject(sql_bname, String.class, rs.getInt("bid"));
                mp.put("bname", bname);
                mp.put("mid", rs.getInt("mid"));
                mp.put("m_name", rs.getString("name"));
                String sql_m_agent = "select name from yx_user where id=? ";

                String m_agent = jdbcTemplate.queryForObject(sql_m_agent, String.class, rs.getInt("uid"));


                mp.put("m_agent", m_agent);
                mp.put("source", rs.getString("source"));

                Integer unitPrice = 10;
                mp.put("unitPrice", unitPrice);

                mp.put("arrival_time", rs.getString("arrival_time"));
                mp.put("confirm_time", rs.getString("confirm_time"));

                Integer nowMonth_day = nowMonthDay(rs.getString("arrival_time"), rs.getString("confirm_time"));
                mp.put("nowMonth_day", nowMonth_day);//当月服务天数
                mp.put("nowMonth_price", nowMonth_day * onePrice);//当月回款
                mp.put("nowMonth_wages", nowMonth_day * unitPrice);//当月工资

                Integer nextMonth_day = nextMonthday(rs.getString("arrival_time"), rs.getString("confirm_time"));
                mp.put("nextMonth_day", nextMonth_day);//次月服务天数
                mp.put("nextMonth_price", nextMonth_day * onePrice);//次月回款
                mp.put("nextMonth_wages", nextMonth_day * unitPrice);//次月工资


                return mp;
            }
        });

        return list;
    }

    @Override
    public List<Map<String, Object>> outCustomHome(String somnus, String monthFirst, String monthLast, Integer bid, String service_type, String timetype, String origin, String channel, Integer work_states, String number, Integer start) {


        List<String> queryList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        //显示页面语句
        sb.append("select c.id,od.id as did,c.c_name,c.bid,c.origin,c.channel,od.service_type,od.timetype,od.level,od.work_states, ");
        sb.append("od.price,c.creat_time,od.service_day,od.starttime,od.endtime,od.onePrice");
        sb.append(" from yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");
        sb.append(" and od.service_type='居家服务' ");


        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        //当前时间
        String nowTime = time.format(new Date());

        //公共条件语句
        StringBuffer sb_public = new StringBuffer();
        sb_public.append(" and od.work_states>=0  and od.service_day<>0 ");
        //公共条件语句开始

        //时间段搜索
        String sql_days = " and  (   (od.starttime>=?  and od.starttime<=?) or (od.endtime>=? and od.endtime<=?) or (?>=od.starttime and od.endtime>=?)  ) ";
        sb_public.append(sql_days);
        queryList.add(monthFirst);
        queryList.add(monthLast);
        queryList.add(monthFirst);
        queryList.add(monthLast);
        queryList.add(monthFirst);
        queryList.add(monthLast);


        //订单类型

        if (!service_type.equals("null")) {
            sb_public.append(" and od.service_type=? ");
            queryList.add(service_type);
            if (!timetype.equals("null")) {
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
        if (!origin.equals("null")) {
            sb_public.append(" and c.origin=? ");
            queryList.add(origin);
            if (!channel.equals("null")) {
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
        if (!number.equals("null")) {
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


        sb.append(sb_public);//搜索显示语句添加公共条件语句

        sb.append(" order by c.creat_time desc");
        System.out.println("queryList = " + queryList);
        //条数

        System.out.println("sb = " + sb);


        List<Map<String, Object>> list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();

                mp.put("c_origin", rs.getString("origin") + rs.getString("channel"));
                mp.put("cid", rs.getString("id"));
                mp.put("c_name", rs.getString("c_name"));
                mp.put("did", rs.getString("did"));
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

                Integer onePrice = 0;//单价

                if (rs.getInt("onePrice") != 0) {
                    onePrice = rs.getInt("onePrice");
                } else {
                    onePrice = rs.getInt("price") / rs.getInt("service_day");
                }

                Integer workStates = rs.getInt("work_states");
                Map<String, Object> priceMap = new HashMap<>();

                List<String> dayList = getDayList(workStates,monthFirst, monthLast,start_time,end_time,nowTime);
                Integer orderDay=dayList.size();
                Integer truePrice =orderDay*onePrice;
                mp.put("orderDay", orderDay);
                mp.put("truePrice", truePrice);



                mp.put("creat_time", rs.getString("creat_time"));
                return mp;
            }
        }, queryList.toArray());
        return list;

    }

    @Override
    public List<Map<String, Object>> outCustomClub(String somnus, String monthFirst, String monthLast, Integer bid, String service_type, String timetype, String origin, String channel, Integer work_states, String number, Integer start) {


        List<String> queryList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        //显示页面语句
        sb.append("select c.id,od.id as did,c.c_name,c.bid,c.origin,c.channel,od.service_type,od.timetype,od.level,od.work_states, ");
        sb.append("od.price,c.creat_time,od.service_day,od.starttime,od.endtime,od.onePrice");
        sb.append(" from yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");
        sb.append(" and od.service_type='月子会所服务' ");

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        //当前时间
        String nowTime = time.format(new Date());

        //公共条件语句
        StringBuffer sb_public = new StringBuffer();
        sb_public.append(" and od.work_states>=0  and od.service_day<>0 ");
        //公共条件语句开始
        //搜索时间


        //时间段搜索
        String sql_days = " and  (   (od.starttime>=?  and od.starttime<=?) or (od.endtime>=? and od.endtime<=?) or (?>=od.starttime and od.endtime>=?)  ) ";
        sb_public.append(sql_days);
        queryList.add(monthFirst);
        queryList.add(monthLast);
        queryList.add(monthFirst);
        queryList.add(monthLast);
        queryList.add(monthFirst);
        queryList.add(monthLast);

        //订单类型

        if (!service_type.equals("null")) {
            sb_public.append(" and od.service_type=? ");
            queryList.add(service_type);
            if (!timetype.equals("null")) {
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
        if (!origin.equals("null")) {
            sb_public.append(" and c.origin=? ");
            queryList.add(origin);
            if (!channel.equals("null")) {
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
        if (!number.equals("null")) {
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

        String finalMonthFirst = monthFirst;
        String finalMonthLast = monthLast;
        sb.append(sb_public);//搜索显示语句添加公共条件语句

        sb.append(" order by c.creat_time desc");


        List<Map<String, Object>> list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();

                mp.put("c_origin", rs.getString("origin") + rs.getString("channel"));
                mp.put("cid", rs.getString("id"));
                mp.put("c_name", rs.getString("c_name"));
                mp.put("did", rs.getString("did"));
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


                //全程里 医院部分订单
                String sql_hospital = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAA%'";
                List<Map<String, Object>> hospitalList = jdbcTemplate.queryForList(sql_hospital, rs.getInt("id"));
                //全程里 会所部分订单
                String sql_club = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAB%'";
                List<Map<String, Object>> clubList = jdbcTemplate.queryForList(sql_club, rs.getInt("id"));

                Map<String, Object> clubMap = new HashMap<>();

                Integer workStates = rs.getInt("work_states");
                String timeType = rs.getString("timetype");

                List<String> dayList = getDayList(workStates,monthFirst, monthLast,start_time,end_time,nowTime);
                Map<String,Object> priceMap=getClubPrice(timeType,workStates,hospitalList,clubList,dayList);

                mp.put("hospitalDay",priceMap.get("hospitalDay") );
                mp.put("clubDay",priceMap.get("clubDay") );
                mp.put("trueHospitalPrice", priceMap.get("trueHospitalPrice"));
                mp.put("trueClubPrice", priceMap.get("trueClubPrice"));

                mp.put("creat_time", rs.getString("creat_time"));
                return mp;
            }
        }, queryList.toArray());
        return list;
    }

    @Override
    public List<Map<String, Object>> outCustomInfo(String json) {
        JSONObject jsonObject = JSON.parseObject(json);//转换类型

        List<String> queryList = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        sb.append(" select c.id,c.c_name,c.production_situation,c.nodate,c.production_date,c.address,c.c_phone,c.w_name,");
        sb.append("c.origin,c.channel,c.sid,c.followCount,c.creat_time,od.work_states,c.bid,c.sid");
        sb.append(" from yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");
        sb.append(" and c.creat_time>=? ");
        queryList.add("2020-10-01 00:00:00");
        sb.append(" and c.creat_time<? ");
        queryList.add("2020-10-31 23:59:59");
        sb.append(" order by c.creat_time desc ");
        System.out.println("sb = " + sb);
        List<Map<String, Object>> list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashMap<String, Object>();
                mp.put("cid", rs.getInt("id"));
                mp.put("c_name", rs.getString("c_name"));
                if (rs.getInt("production_situation") == 0) {
                    mp.put("production_situation", "未生产");
                    mp.put("production_time", rs.getString("nodate"));
                } else {
                    mp.put("production_situation", "已生产");
                    mp.put("production_time", rs.getString("production_date"));
                }
                mp.put("address", rs.getString("address"));
                mp.put("c_phone", rs.getString("c_phone"));
                mp.put("WeChat", rs.getString("w_name"));
                mp.put("c_origin", (rs.getString("origin") + rs.getString("channel")));

                String sql_bname = "select name from yx_user where id=?";
                String bname = null;
                if (rs.getInt("bid") == 0) {
                    bname = "未知";
                } else {
                    bname = jdbcTemplate.queryForObject(sql_bname, String.class, rs.getInt("bid"));
                }
                mp.put("bname", bname);

                String sql_sname = "select name from yx_user where id=? ";
                String sname = null;
                if (rs.getInt("sid") == 0) {
                    sname = "未知";
                } else {
                    sname = jdbcTemplate.queryForObject(sql_sname, String.class, rs.getInt("sid"));
                }
                mp.put("sname", sname);

                if (rs.getInt("work_states") == 0) {
                    mp.put("order_speed", "待服务");
                }
                if (rs.getInt("work_states") == 1) {
                    mp.put("order_speed", "服务中");
                }
                if (rs.getInt("work_states") == 2) {
                    mp.put("order_speed", "已完成");
                }


                mp.put("followCount", rs.getInt("followCount"));

                mp.put("creat_time", rs.getString("creat_time"));

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
                        mp.put("intention", null);
                    }
                }

                return mp;
            }
        }, queryList.toArray());
        return list;
    }

    @Override
    public List<Map<String, Object>> outNewMatorn(Integer uid, String zodiac, String grade, Integer shelf, Integer bid, String source, String origin, Integer isPrice, String powerFiled, String number) {

        List<Map<String, Object>> list = new ArrayList<>();


        List<Map<String, Object>> powerFiledList = new ArrayList<>();
        powerFiled = powerFiled.replace("[", "{");
        powerFiled = powerFiled.replace("]", "}");
        JSONArray jsonArray = JSONArray.fromObject("[" + powerFiled + "]");

        powerFiledList = (List) jsonArray;
        Map<String, Object> powerFiledMap = new LinkedHashMap();//保证put值有序
        for (int i = 0; i < powerFiledList.size(); i++) {
            for (String key : powerFiledList.get(i).keySet()) {
                Integer value = Integer.valueOf(powerFiledList.get(i).get(key).toString());
                powerFiledMap.put(key, value);
            }
        }

        StringBuffer sb = new StringBuffer();
        sb.append(" select m.id,m.uid,m.lid,m.name,YEAR(from_days(datediff(now(), m.born))) as  age ,m.zodiac,m.household,b.grade,b.trueday,b.shelf,");
        sb.append(" ori.source,ori.institution_name,ori.introducer,ori.other,b.creat_time,b.isDeposit,b.isWarranty,b.number,b.photo,b.isblack,");
        sb.append(" p.month,c.phone,c.bank_card,c.bank_name,m.idcard,b.isorder,u.name as l_name");
        sb.append(" from yx_matorn m left join yx_origin ori on (m.id=ori.mid) left join yx_bussiness b on (m.id=b.mid) ");
        sb.append(" left join yx_period p on (m.id=p.mid) left join yx_contact c on (m.id=c.mid) left join yx_user u on (m.lid=u.id) ");
        sb.append(" where  m.idtype=1 ");

        List<Object> queryList = new ArrayList<Object>();
        if (shelf == 0) {

        } else {
            if (shelf == 1) {
                //黑名单
                sb.append(" and b.isblack=1 ");
            }
            if (shelf == 2) {
                //未上架
                sb.append(" and b.shelf=0 ");
            }
            if (shelf == 3) {
                //已上架
                sb.append(" and b.shelf=1 ");
            }
            if (shelf == 4) {
                //服务中
                sb.append(" and b.isorder=1 ");
            }
        }


        //经理人判断
        if (bid == 0) {
            //为空
        } else {
            if (bid != 22) {
                sb.append(" and m.uid =? ");
                queryList.add(bid.toString());
            } else {
                sb.append(" and (m.uid=0 or m.uid=22 ) ");
            }
        }


        //等级判断
        if (grade.equals("null") || grade.isEmpty() || grade == "" || grade == null) {

        } else {
            sb.append(" and b.grade=? ");
            queryList.add(grade);
        }

        //属相判断
        if (zodiac.equals("null") || zodiac.isEmpty() || zodiac == "" || zodiac == null) {

        } else {
            sb.append(" and m.zodiac=? ");
            queryList.add(zodiac);
        }

        //月嫂来源判断
        if (source.equals("null") || source.isEmpty() || source == "" || source == null) {
            //为空
        } else {
            if (source.equals("培训机构")) {
                sb.append(" and ori.source='培训机构' ");
                if (origin.equals("null") || origin.isEmpty() || origin == "" || origin == null) {
                    //为空
                } else {
                    sb.append(" and ori.mechanism_name=? ");
                    queryList.add(origin);
                }

            }
            if (source.equals("个人介绍")) {
                sb.append(" and ori.source='个人介绍' ");
                if (origin.equals("null") || origin.isEmpty() || origin == "" || origin == null) {
                    //为空
                } else {
                    sb.append(" and ori.introducer=? ");
                    queryList.add(origin);
                }
            }
            if (source.equals("其他")) {
                sb.append(" and ori.source='其他' ");
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
                sb.append(" and ( b.isDeposit is null or b.isDeposit=0  )   and  ( b.isWarranty is null or b.isWarranty=0  ) ");
            }
            if (isPrice == 2) {
                sb.append(" and b.isDeposit=1 and  ( b.isWarranty is null or b.isWarranty=0  ) ");
            }
            if (isPrice == 3) {
                sb.append(" and b.isDeposit=2 and  ( b.isWarranty is null or b.isWarranty=0  )");
            }
            if (isPrice == 4) {
                sb.append(" and ( b.isDeposit is null or b.isDeposit=0  ) and   b.isWarranty=1 ");
            }
            if (isPrice == 5) {
                sb.append(" and ( b.isDeposit is null or b.isDeposit=0  ) and   b.isWarranty=2 ");
            }

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
                String sql2 = "select name from yx_matorn where name =? ";
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
        System.out.println("sb = " + sb);


        sb.append(" order by b.creat_time desc ");
        list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
            @SneakyThrows
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                Map<String, Object> mp = new HashedMap();

                mp.put("mid", rs.getInt("id"));
                mp.put("number", rs.getString("number"));
                mp.put("photo", rs.getString("photo"));
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
                        String sql_service_count = "select  count(DISTINCT(cid)) from yx_order  where mid=? ";
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


//                //服务档期
//                if (powerFiledMap.get("period") != null) {
//                    if (powerFiledMap.get("period").toString().equals("1")) {
//                        String month = rs.getString("month");
//                        Map<String, String> periodMap = new HashMap<String, String>();
//                        String newmap = "";
//                        if (month != null && month != "") {
//                            try {
//                                newmap = "[" + getNewMap(month) + "]";
//                                String sql_newmap = "update  yx_period set month=? where mid=?";
//                                jdbcTemplate.update(sql_newmap, newmap, rs.getInt("id"));
//                                periodMap = getAll(newmap);
//
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            periodMap = gNull();
//                        }
//                        mp.put("period", periodMap);
//                    } else {
//
//                    }
//                } else {
//
//                }


                mp.put("creat_time", rs.getString("creat_time"));
                mp.put("l_name", rs.getString("l_name"));
                if (rs.getInt("uid") == 0 || rs.getInt("uid") == 22) {
                    if (powerFiledMap.get("agent") != null) {
                        if (powerFiledMap.get("agent").toString().equals("1")) {
                            mp.put("agent", "待分配");
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
        return list;
    }


    @Override
    public List<Map<String, Object>> outUser(Integer cid, Integer did, Integer pid, String number) {



        List<Map<String, Object>> list = new ArrayList<>();

        StringBuffer sb = new StringBuffer();
        sb.append("select id,phone,name,post,power,password,photo,creat_time,description,company,department,sex from yx_user");
        sb.append(" where power<>30 and power<>40 ");

        List<String> queryList = new ArrayList<>();
        System.out.println("cid = " + cid);
        if (cid != 0) {
            sb.append(" and company=? ");
            queryList.add(cid.toString());
            if (did != 0) {
                sb.append(" and department=? ");
                queryList.add(did.toString());
                if (pid != 0) {
                    sb.append(" and power=? ");
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
                    sb.append(" and phone = ? ");
                    queryList.add(number);
                } else {
                    //id
                    sb.append(" and id = ? ");
                    queryList.add(number);
                }
            }
            //汉字
            if (number.matches("^[\\u4e00-\\u9fa5]+$")) {
                String sql2 = "select name from yx_user where name =? ";
                try {
                    List<String> list1 = this.jdbcTemplate.queryForList(sql2.toString(), new Object[]{number}, String.class);
                    if (number.equals(list1.get(0))) {
                        sb.append("  and name=?  ");
                        queryList.add(list1.get(0));
                    } else {

                    }
                } catch (Exception e) {
                    if (number.length() == 1) {
                        String name = number.substring(0, 1);
                        sb.append(" and name like ?  ");
                        queryList.add(name + "%");
                    } else {
                        return list;
                    }

                }
            }

        }

        sb.append(" order by id desc ");


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
        return list;
    }
}