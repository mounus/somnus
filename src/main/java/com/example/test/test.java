package com.example.test;


import org.hibernate.validator.constraints.ParameterScriptAssert;

import java.text.SimpleDateFormat;
import java.util.*;

public class test {
    public static void main(String[] args) {
//
//                JSONObject jsonObject = JSON.parseObject(json);
//        Map<String, Object> map = new HashedMap();
//
//        // Integer year = jsonObject.getInteger("year");
//        Integer year = 2021;
//        Integer month = jsonObject.getInteger("month");
//
//
//        //当前时间
//        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
//        String nowTime = time.format(new Date());
//
//        Integer nowMonth = Integer.valueOf(nowTime.substring(5, 7).toString());
//
//
//        //订单金额统计1
//        List<Map<String, Object>> orderStatistics = new ArrayList<>();
//        //其他订单类型计算语句
//        StringBuffer sb_price = new StringBuffer();
//        sb_price.append(" select c.id,od.work_states,od.starttime,od.endtime,od.onePrice,od.timetype,od.price,od.service_day");
//        sb_price.append("  from  yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");
//
//        String sql_price_club = "  and od.service_type='月子会所服务' ";
//        String sql_price_home = "  and od.service_type='居家服务' ";
//
//        //公共条件语句(一个时间段内)
//        String sql_month = "  and od.work_states>=0  and od.service_day<>0   and  (   (od.starttime>=?  and od.starttime<=?) or (od.endtime>=? and od.endtime<=?) or (?>od.starttime and od.endtime>?) )  ";
//        //公共条件语句(一天内)
//        String sql_day = "  and od.work_states>=0  and od.service_day<>0   and (  ( od.starttime=? ) or ( od.endtime=? ) or ( od.starttime<? and od.endtime>?)   )  ";
//
//
//        //会所语句
//        StringBuffer sb_price_club_month = new StringBuffer();
//        sb_price_club_month.append(sb_price);
//        sb_price_club_month.append(sql_price_club);
//        sb_price_club_month.append(sql_month);
//
//        //需要搜索的月份
//        List<List> monthList = getAllMonth();
//        System.out.println("monthList = " + monthList);
//        /**orderStatistics结果搜索太慢，解决方法
//         * 1.redis永久缓存
//         * 2.存入数据库
//         *阿里云服务器没有安装redis，懒得安装
//         * 选择存入数据库
//         *存入数据库数据为当前时间前一天数据所有数据，当天搜索为数据库数据加上当天一天的数据
//         * 每一天第一次搜索修改数据库数据，前天所有数据加上昨天数据为数据库新数据
//         */
//        String sql_orderStatistics_count = "select count(*) from yx_orderStatistics  ";
//        Integer orderStatistics_count = jdbcTemplate.queryForObject(sql_orderStatistics_count, Integer.class);
//
//        //统计所有金额
//        Integer allTruePrice = 0;
//        //统计所有会所金额
//        Integer allClubTruePrice = 0;
//        //统计所有居家金额
//        Integer allHomeTruePrice = 0;
//
//
//        //会所当前年份12月收益
//        List<Map<String, Object>> monthClubProfitTrend = new ArrayList<>();
//        //居家当前年份12月收益
//        List<Map<String, Object>> monthHomeProfitTrend = new ArrayList<>();
//
//
//        //需要添加到数据库的数据
//        Integer sql_allClubTruePrice = 0;
//        Integer sql_allHomeTruePrice = 0;
//
//
//        //会所某天天语句
//        StringBuffer sb_price_club_day = new StringBuffer();
//        sb_price_club_day.append(sb_price);
//        sb_price_club_day.append(sql_price_club);
//        sb_price_club_day.append(sql_day);
//        //居家某天语句
//        StringBuffer sb_price_home_day = new StringBuffer();
//        sb_price_home_day.append(sb_price);
//        sb_price_home_day.append(sql_price_home);
//        sb_price_home_day.append(sql_day);
//
//        if (orderStatistics_count > 0) {
//            //存入过数据
//            String sql_orderStatistics = "select club,home,allPrice,s_time from yx_orderStatistics where id=(select max(id) from yx_orderStatistics)";
//            Map<String, Object> orderStatisticsMap = jdbcTemplate.queryForMap(sql_orderStatistics);
//            //数据库搜索出的数据
//            allClubTruePrice = Integer.valueOf(orderStatisticsMap.get("club").toString());
//            allHomeTruePrice = Integer.valueOf(orderStatisticsMap.get("home").toString());
//
//            //需要添加到数据库的数据
//             sql_allClubTruePrice = allClubTruePrice;
//             sql_allHomeTruePrice = allHomeTruePrice;
//
//
////            Integer year1 = Integer.valueOf(nowTime.substring(0, 4).toString());
////            Integer month1 = Integer.valueOf(nowTime.substring(5, 7).toString());
//
//
//
//            Integer clubMonthPrice_Count=0;
//            Integer clubMonthPrice_Price =0;
//
//            Integer homeMonthPrice_Count=0;
//            Integer homeMonthPrice_Price=0;
//
//
//            System.out.println("clubMonthPriceMap = " + monthClubProfitTrend);
//
//
//
//            System.out.println("homeMonthPrice_Price =____________________________ " + homeMonthPrice_Price);
//            String s_time = orderStatisticsMap.get("s_time").toString();
//
//            List<String> dayList = getDays(getNewEndtime(s_time, -1), nowTime);
//
//            System.out.println("dayList = " + dayList);
//
//            //缓存更新下一个月的会所这个月缓存(不包括当天)
//            Integer monthClub_oneTruePrice=0;
//            //缓存更新下一个月的居家这个月缓存(不包括当天)
//            Integer monthHome_oneTruePrice=0;
//
//            for (int i = 0; i < dayList.size(); i++) {
//                List<String> queryDayList = new ArrayList<>();
//                queryDayList.add(dayList.get(i).toString());
//                queryDayList.add(dayList.get(i).toString());
//                queryDayList.add(dayList.get(i).toString());
//                queryDayList.add(dayList.get(i).toString());
//
//                int finalI = i;
//
//                String sql_monthPriceMonthYear = "select year,month from yx_monthPrice where type=0 and id=(select max(id) from yx_monthPrice where type=0)  ";
//                Map<String,Object> monthPriceMonthYearMap=jdbcTemplate.queryForMap(sql_monthPriceMonthYear);
//                Integer year1 = Integer.valueOf(monthPriceMonthYearMap.get("year").toString());
//                Integer month1 = Integer.valueOf(monthPriceMonthYearMap.get("month").toString());
//
//                String sql_monthPriceCount = "select count(*) from yx_monthPrice where year=?  ";
//                Integer monthPriceCount = jdbcTemplate.queryForObject(sql_monthPriceCount, Integer.class, year1);
//                if (monthPriceCount > 0) {
//                    //当前年份当前月份已经创建
//                    String sql_clubMonthPrice = "select count,oneTruePrice from yx_monthPrice where type=0 and year=? and month=? ";
//                    monthClubProfitTrend = jdbcTemplate.queryForList(sql_clubMonthPrice, year1, month1);
//                    clubMonthPrice_Count = Integer.valueOf(monthClubProfitTrend.get(monthClubProfitTrend.size()-1).get("count").toString());
//                    clubMonthPrice_Price = Integer.valueOf(monthClubProfitTrend.get(monthClubProfitTrend.size()-1).get("oneTruePrice").toString());
//
//                    String sql_homeMonthPrice = "select count,oneTruePrice from yx_monthPrice where type=1 and year=? and month=? ";
//                    monthHomeProfitTrend = jdbcTemplate.queryForList(sql_homeMonthPrice, year1, month1);
//                    homeMonthPrice_Count = Integer.valueOf(monthHomeProfitTrend.get(monthHomeProfitTrend.size()-1).get("count").toString());
//                    homeMonthPrice_Price = Integer.valueOf(monthHomeProfitTrend.get(monthHomeProfitTrend.size()-1).get("oneTruePrice").toString());
//
//                } else {
//                    //当前年份当前月份没有创建
//                }
//
//
//                List<Map<String, Object>> sql_clublist = jdbcTemplate.query(sb_price_club_day.toString(), new RowMapper<Map<String, Object>>() {
//                    @SneakyThrows
//                    @Override
//                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                        Map<String, Object> mp = new HashMap<String, Object>();
//                        String start_time = rs.getString("starttime");
//                        String end_time = rs.getString("endtime");
//
//                        //全程里 医院部分订单
//                        String sql_hospital = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAA%'";
//                        List<Map<String, Object>> hospitalList = jdbcTemplate.queryForList(sql_hospital, rs.getInt("id"));
//                        //全程里 会所部分订单
//                        String sql_club = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAB%'";
//                        List<Map<String, Object>> clubList = jdbcTemplate.queryForList(sql_club, rs.getInt("id"));
//
//                        Map<String, Object> clubMap = new HashMap<>();
//                        Integer workStates = rs.getInt("work_states");
//                        String timeType = rs.getString("timetype");
//
//                        //某一天
//                        clubMap = clubPriceOneDay(workStates, start_time, dayList.get(finalI), timeType, hospitalList, clubList);
//
//                        mp.put("hospitalDay", clubMap.get("hospitalDay"));
//                        mp.put("clubDay", clubMap.get("clubDay"));
//                        mp.put("trueHospitalPrice", clubMap.get("trueHospitalPrice"));
//                        mp.put("trueClubPrice", clubMap.get("trueClubPrice"));
//                        return mp;
//                    }
//                }, queryDayList.toArray());
//                Integer oneTruePrice = 0;//会所一个天金额
//                for (int j = 0; j < sql_clublist.size(); j++) {
//                    Integer hospitalDay = Integer.valueOf(sql_clublist.get(j).get("hospitalDay").toString());//单个医院天数
//                    Integer trueHospitalPrice = Integer.valueOf(sql_clublist.get(j).get("trueHospitalPrice").toString());//单个医院实收金额
//                    Integer clubDay = Integer.valueOf(sql_clublist.get(j).get("clubDay").toString());//单个会所天数
//                    Integer trueClubPrice = Integer.valueOf(sql_clublist.get(j).get("trueClubPrice").toString());//单个会所实收金额
//
//                    oneTruePrice = oneTruePrice + trueHospitalPrice + trueClubPrice;
//                }
//                //数据库会所金额加上会所一天金额
//                //显示会所全部金额
//                allClubTruePrice = allClubTruePrice + oneTruePrice;
//
//                //数据库某月会所金额加上会所一天金额
//
//                System.out.println("month1 = " + month1);
//                System.out.println("oneTruePrice = " + oneTruePrice);
//                Integer dayYear= Integer.valueOf(dayList.get(i).substring(0,4).toString());
//               Integer dayMonth= Integer.valueOf(dayList.get(i).substring(5,7).toString());
//                System.out.println("dayMonth = " + dayMonth);
//
//                List<Map<String, Object>> sql_homeList = this.jdbcTemplate.query(sb_price_home_day.toString(), new RowMapper<Map<String, Object>>() {
//                    @SneakyThrows
//                    @Override
//                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                        Map<String, Object> mp = new HashMap<String, Object>();
//                        String start_time = rs.getString("starttime");
//                        String end_time = rs.getString("endtime");
//
//                        Integer onePrice = 0;//单价
//                        if (rs.getInt("onePrice") != 0) {
//                            onePrice = rs.getInt("onePrice");
//                        } else {
//                            onePrice = rs.getInt("price") / rs.getInt("service_day");
//                        }
//                        Integer workStates = rs.getInt("work_states");
//                        Map<String, Object> priceMap = new HashMap<>();
//
//                        priceMap = homePriceOneDay(workStates, onePrice, start_time, dayList.get(finalI));
//
//                        mp.put("orderDay", priceMap.get("orderDay"));
//                        mp.put("truePrice", priceMap.get("truePrice"));
//
//                        return mp;
//                    }
//
//                }, queryDayList.toArray());
//
//                Integer oneTruePriceHome = 0;//居家一天金额
//                for (int j = 0; j < sql_homeList.size(); j++) {
//                    Integer orderDay = Integer.valueOf(sql_homeList.get(j).get("orderDay").toString());//单个居家天数
//                    Integer truePrice = Integer.valueOf(sql_homeList.get(j).get("truePrice").toString());//单个居家金额
//                    oneTruePriceHome = oneTruePriceHome + truePrice;
//
//                }
//
//                 //返回显示
//                //居家数量
//                homeMonthPrice_Count = sql_homeList.size() + homeMonthPrice_Count;
//                System.out.println("clubMonthPrice_Count = " + homeMonthPrice_Count);
//                //金额
//                homeMonthPrice_Price = oneTruePriceHome + homeMonthPrice_Price;
//                System.out.println("clubMonthPrice_Price = " + homeMonthPrice_Price);
//
//                //返回显示
//                //会所数量
//                clubMonthPrice_Count = sql_clublist.size() + clubMonthPrice_Count;
//                System.out.println("clubMonthPrice_Count = " + clubMonthPrice_Count);
//                //金额
//                clubMonthPrice_Price = oneTruePrice + clubMonthPrice_Price;
//                System.out.println("clubMonthPrice_Price = " + clubMonthPrice_Price);
//
//                if (monthHomeProfitTrend.size()>0){
//                    monthClubProfitTrend.get(monthClubProfitTrend.size()-1).put("count",clubMonthPrice_Count);
//                    monthClubProfitTrend.get(monthClubProfitTrend.size()-1).put("oneTruePrice",clubMonthPrice_Price);
//
//                    monthHomeProfitTrend.get(monthHomeProfitTrend.size()-1).put("count",homeMonthPrice_Count);
//                    monthHomeProfitTrend.get(monthHomeProfitTrend.size()-1).put("oneTruePrice",homeMonthPrice_Price);
//                }else {
//                    Map<String,Object> monthHomeProfitTrendMap1=new HashedMap();
//                    monthHomeProfitTrendMap1.put("count",homeMonthPrice_Count);
//                    monthHomeProfitTrendMap1.put("oneTruePrice",homeMonthPrice_Price);
//
//                    Map<String,Object> monthClubProfitTrendMap1=new HashedMap();
//                    monthHomeProfitTrendMap1.put("count",clubMonthPrice_Count);
//                    monthHomeProfitTrendMap1.put("oneTruePrice",clubMonthPrice_Price);
//
//                }
//
//
//
//                if (dayList.size() > 1) {
//                    //数据库存入数据时间与当前时间间隔1天以上
//                    //显示数据为数据库数据加上s_time(不包括s_time这天数据)到当前时间的数据
//                    //并且添加新的信息
//                    if (i < dayList.size() - 1) {
//
//                        //订单统计总金额，会所总金额，居家总金额加入数据库
//                        //除了最后一个都加入数据库
//                        String sql_day_count = "select count(*) from yx_orderStatistics where s_time=?";
//                        Integer day_count = jdbcTemplate.queryForObject(sql_day_count, Integer.class, dayList.get(finalI));
//                        if (day_count > 0) {
//                            //dayList.get(finalI)这个时间已经添加过，不需要再添加
//                        } else {
//                            sql_allClubTruePrice = sql_allClubTruePrice + oneTruePrice;
//                            sql_allHomeTruePrice = sql_allHomeTruePrice + oneTruePriceHome;
//                            String sql_add_orderStatistics = "insert into yx_orderStatistics(club,home,allPrice,s_time) values(?,?,?,?)";
//                            Integer sql_allTruePrice = sql_allClubTruePrice + sql_allHomeTruePrice;
//                            int states_add_orderStatistics = jdbcTemplate.update(sql_add_orderStatistics, sql_allClubTruePrice, sql_allHomeTruePrice, sql_allTruePrice, dayList.get(i).toString());
//                        }
//
//
//                       //添加更新每月数据
//                        if (month1.equals(dayMonth)&&year1.equals(dayYear)){
//                            System.out.println("dayList__________11111111111111111111111111 = " + dayList.get(i));
//                            //已经创建
//                             //会所某个月缓存修改更新
//                             String sql_updateClubMonth="update yx_monthPrice set count=?,oneTruePrice=? where type=0 and year=? and month=?";
//                             int states_updateClubMonth=jdbcTemplate.update(sql_updateClubMonth,clubMonthPrice_Count,clubMonthPrice_Price,dayYear,dayMonth);
//
//                             //居家某个月缓存修改更新
//                            String sql_updateHomeMonth="update yx_monthPrice set count=?,oneTruePrice=? where type=1 and year=? and month=?";
//                            int states_updateHomeMonth=jdbcTemplate.update(sql_updateHomeMonth,homeMonthPrice_Count,homeMonthPrice_Price,dayYear,dayMonth);
//
//                        }else {
//                            //没有创建
//                            String sql_addMonth="insert into  yx_monthPrice(type,count,oneTruePrice,year,month) values(?,?,?,?,?)";
//                            //获取循环时间的下一个月
//                            System.out.println("dayList__________2222222222222222222222 = " + dayList.get(i));
//                            monthClub_oneTruePrice=oneTruePrice;
//                            monthHome_oneTruePrice=oneTruePriceHome;
//                            //会所添加
//                          int states_addClubMonth=jdbcTemplate.update(sql_addMonth, 0, 0, monthClub_oneTruePrice, dayYear, dayMonth);
//
//                            //居家添加
//                            int states_addHomeMonth=jdbcTemplate.update(sql_addMonth, 1, 0, monthHome_oneTruePrice, dayYear, dayMonth);
//
//                        }
//
//
//                    } else {
//                        //最后一个数据不加入数据库
//
//                    }
//
//                } else {
//                    //数据库存入数据时间与当前时间相邻
//                    //显示数据为数据库数据加上当天数据
//                }
//
//
//                allHomeTruePrice = allHomeTruePrice + oneTruePriceHome;
//
//
//
//            }//for 结尾括号
//
//
//        } else {
//            //没有存入数据
//
//            List<String> nowQuery=new ArrayList<>();
//            nowQuery.add(nowTime);
//            nowQuery.add(nowTime);
//            nowQuery.add(nowTime);
//            nowQuery.add(nowTime);
//
//            List<Map<String, Object>> sql_nowClublist = jdbcTemplate.query(sb_price_club_day.toString(), new RowMapper<Map<String, Object>>() {
//                @SneakyThrows
//                @Override
//                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                    Map<String, Object> mp = new HashMap<String, Object>();
//                    String start_time = rs.getString("starttime");
//                    String end_time = rs.getString("endtime");
//
//                    //全程里 医院部分订单
//                    String sql_hospital = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAA%'";
//                    List<Map<String, Object>> hospitalList = jdbcTemplate.queryForList(sql_hospital, rs.getInt("id"));
//                    //全程里 会所部分订单
//                    String sql_club = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAB%'";
//                    List<Map<String, Object>> clubList = jdbcTemplate.queryForList(sql_club, rs.getInt("id"));
//
//                    Map<String, Object> clubMap = new HashMap<>();
//                    Integer workStates = rs.getInt("work_states");
//                    String timeType = rs.getString("timetype");
//
//                    clubMap = clubPriceOneDay(workStates, start_time, nowTime, timeType, hospitalList, clubList);
//
//                    mp.put("hospitalDay", clubMap.get("hospitalDay"));
//                    mp.put("clubDay", clubMap.get("clubDay"));
//                    mp.put("trueHospitalPrice", clubMap.get("trueHospitalPrice"));
//                    mp.put("trueClubPrice", clubMap.get("trueClubPrice"));
//                    return mp;
//                }
//            }, nowQuery.toArray());
//
//            //会所当天收益
//            Integer nowClub_oneTruePrice=0;
//            for (int j = 0; j < sql_nowClublist.size(); j++) {
//                Integer trueHospitalPrice = Integer.valueOf(sql_nowClublist.get(j).get("trueHospitalPrice").toString());//单个医院实收金额
//                Integer trueClubPrice = Integer.valueOf(sql_nowClublist.get(j).get("trueClubPrice").toString());//单个会所实收金额
//                nowClub_oneTruePrice = nowClub_oneTruePrice + trueHospitalPrice + trueClubPrice;
//            }
//            System.out.println("nowClub_oneTruePrice = " + nowClub_oneTruePrice);
//
//
//            for (int i = 0; i < monthList.size(); i++) {
//                String monthFirst = monthList.get(i).get(0).toString();
//                String monthLast = monthList.get(i).get(1).toString();
//                System.out.println(" club________________________ " + i);
//                List<String> queryMonthList = new ArrayList<>();
//                queryMonthList.add(monthFirst);
//                queryMonthList.add(monthLast);
//                queryMonthList.add(monthFirst);
//                queryMonthList.add(monthLast);
//                queryMonthList.add(monthFirst);
//                queryMonthList.add(monthLast);
//                List<Map<String, Object>> sql_clublist = jdbcTemplate.query(sb_price_club_month.toString(), new RowMapper<Map<String, Object>>() {
//                    @SneakyThrows
//                    @Override
//                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                        Map<String, Object> mp = new HashMap<String, Object>();
//                        String start_time = rs.getString("starttime");
//                        String end_time = rs.getString("endtime");
//
//                        //全程里 医院部分订单
//                        String sql_hospital = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAA%'";
//                        List<Map<String, Object>> hospitalList = jdbcTemplate.queryForList(sql_hospital, rs.getInt("id"));
//                        //全程里 会所部分订单
//                        String sql_club = " select o_number,arrival_time,confirm_time from yx_order where cid=? and o_number like '%CAB%'";
//                        List<Map<String, Object>> clubList = jdbcTemplate.queryForList(sql_club, rs.getInt("id"));
//
//                        Map<String, Object> clubMap = new HashMap<>();
//                        Integer workStates = rs.getInt("work_states");
//                        String timeType = rs.getString("timetype");
//
//                        //不是当前时间前一天
//                        clubMap = clubPrice(workStates, start_time, end_time, monthFirst, monthLast, nowTime, timeType, hospitalList, clubList);
//
//                        mp.put("hospitalDay", clubMap.get("hospitalDay"));
//                        mp.put("clubDay", clubMap.get("clubDay"));
//                        mp.put("trueHospitalPrice", clubMap.get("trueHospitalPrice"));
//                        mp.put("trueClubPrice", clubMap.get("trueClubPrice"));
//                        return mp;
//                    }
//                }, queryMonthList.toArray());
//
//                Integer oneTruePrice = 0;//一个月金额
//                for (int j = 0; j < sql_clublist.size(); j++) {
//                 //   Integer hospitalDay = Integer.valueOf(sql_clublist.get(j).get("hospitalDay").toString());//单个医院天数
//                    Integer trueHospitalPrice = Integer.valueOf(sql_clublist.get(j).get("trueHospitalPrice").toString());//单个医院实收金额
//                  //  Integer clubDay = Integer.valueOf(sql_clublist.get(j).get("clubDay").toString());//单个会所天数
//                    Integer trueClubPrice = Integer.valueOf(sql_clublist.get(j).get("trueClubPrice").toString());//单个会所实收金额
//                    oneTruePrice = oneTruePrice + trueHospitalPrice + trueClubPrice;
//                }
//
//                //搜索时间的年份
//                Integer yearQuery = Integer.valueOf(queryMonthList.get(0).substring(0, 4).toString());
//                Integer monthQuery = Integer.valueOf(queryMonthList.get(0).substring(5, 7).toString());
//
//                Map<String, Object> monthClubProfitTrendMap = new HashedMap();
//                Integer clubListCount = sql_clublist.size();
//                    Integer sql_oneTruePrice=0;
//                if (yearQuery.equals(year)) {
//                    //取出当前年份的数据
//                    sql_oneTruePrice=oneTruePrice+nowClub_oneTruePrice;
//                    monthClubProfitTrendMap.put("count", clubListCount);
//                    monthClubProfitTrendMap.put("oneTruePrice", oneTruePrice+nowClub_oneTruePrice);
//                    System.out.println("monthClubProfitTrendMap = " + monthClubProfitTrendMap);
//                    monthClubProfitTrend.add(monthClubProfitTrendMap);
//                } else {
//                    sql_oneTruePrice=oneTruePrice;
//                }
//                System.out.println("oneTruePrice =------------------- " + oneTruePrice);
//                String sql_add_clubMonth = "insert into  yx_monthPrice(type,count,oneTruePrice,year,month) values(?,?,?,?,?)";
//                //type=0 会所
//                int states_add_clubMonth = jdbcTemplate.update(sql_add_clubMonth, 0, clubListCount, sql_oneTruePrice, yearQuery, monthQuery);
//                sql_allClubTruePrice = sql_allClubTruePrice + oneTruePrice;
//
//            }
//            System.out.println("allClubTruePrice = " + allClubTruePrice);
//
//            //居家语句
//            StringBuffer sb_price_home_month = new StringBuffer();
//            sb_price_home_month.append(sb_price);
//            sb_price_home_month.append(sql_price_home);
//            sb_price_home_month.append(sql_month);
//
//
//            List<Map<String, Object>> sql_nowHomeList = this.jdbcTemplate.query(sb_price_home_day.toString(), new RowMapper<Map<String, Object>>() {
//                @SneakyThrows
//                @Override
//                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                    Map<String, Object> mp = new HashMap<String, Object>();
//                    String start_time = rs.getString("starttime");
//                    String end_time = rs.getString("endtime");
//
//                    Integer onePrice = 0;//单价
//                    if (rs.getInt("onePrice") != 0) {
//                        onePrice = rs.getInt("onePrice");
//                    } else {
//                        onePrice = rs.getInt("price") / rs.getInt("service_day");
//                    }
//                    Integer workStates = rs.getInt("work_states");
//                    Map<String, Object> priceMap = new HashMap<>();
//
//                    priceMap = homePriceOneDay(workStates, onePrice, start_time, nowTime);
//
//                    mp.put("orderDay", priceMap.get("orderDay"));
//                    mp.put("truePrice", priceMap.get("truePrice"));
//
//                    return mp;
//                }
//
//            }, nowQuery.toArray());
//
//            //居家当天收益
//            Integer nowHome_oneTruePrice=0;
//            for (int j = 0; j < sql_nowHomeList.size(); j++) {
//                Integer orderDay = Integer.valueOf(sql_nowHomeList.get(j).get("orderDay").toString());//单个居家天数
//                Integer truePrice = Integer.valueOf(sql_nowHomeList.get(j).get("truePrice").toString());//单个居家金额
//                nowHome_oneTruePrice = nowHome_oneTruePrice + truePrice;
//            }
//            System.out.println("nowHome_oneTruePrice = " + nowHome_oneTruePrice);
//
//            for (int i = 0; i < monthList.size(); i++) {
//                String monthFirst = monthList.get(i).get(0).toString();
//                String monthLast = monthList.get(i).get(1).toString();
//                System.out.println(" home________________________ " + i);
//                List<String> queryMonthList = new ArrayList<>();
//                queryMonthList.add(monthFirst);
//                queryMonthList.add(monthLast);
//                queryMonthList.add(monthFirst);
//                queryMonthList.add(monthLast);
//                String firstDay = getFirstDay(monthFirst);//获取搜索时间月份第一天
//                String lastDay = getLastDay(monthFirst);//获取搜索时间月份最后一天
//                if (firstDay.equals(monthFirst)&&lastDay.equals(monthLast)){
//                    //小于月头，大于月尾
//                    queryMonthList.add(firstDay);
//                    queryMonthList.add(lastDay);
//                }else {
//                    //小于第一个，大于第二个
//                    queryMonthList.add(monthFirst);
//                    queryMonthList.add(monthLast);
//                }
//
//                System.out.println("sb_price_home_month = " + sb_price_home_month);
//                List<Map<String, Object>> sql_homeList = this.jdbcTemplate.query(sb_price_home_month.toString(), new RowMapper<Map<String, Object>>() {
//                    @SneakyThrows
//                    @Override
//                    public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
//                        Map<String, Object> mp = new HashMap<String, Object>();
//                        String start_time = rs.getString("starttime");
//                        String end_time = rs.getString("endtime");
//
//                        Integer onePrice = 0;//单价
//                        if (rs.getInt("onePrice") != 0) {
//                            onePrice = rs.getInt("onePrice");
//                        } else {
//                            onePrice = rs.getInt("price") / rs.getInt("service_day");
//                        }
//                        Integer workStates = rs.getInt("work_states");
//                        Map<String, Object> priceMap = new HashMap<>();
//
//                        priceMap = homePrice(workStates, onePrice, start_time, end_time, monthFirst, monthLast, nowTime);
//
//                        mp.put("orderDay", priceMap.get("orderDay"));
//                        mp.put("truePrice", priceMap.get("truePrice"));
//
//                        return mp;
//                    }
//
//                }, queryMonthList.toArray());
//
//                Integer oneTruePrice = 0;//一个月金额
//                System.out.println("sql_homeList.size() = " + sql_homeList.size());
//                for (int j = 0; j < sql_homeList.size(); j++) {
//                    Integer orderDay = Integer.valueOf(sql_homeList.get(j).get("orderDay").toString());//单个居家天数
//                    Integer truePrice = Integer.valueOf(sql_homeList.get(j).get("truePrice").toString());//单个居家金额
//                    oneTruePrice = oneTruePrice + truePrice;
//                }
//                //搜索时间的年份
//                Integer yearQuery = Integer.valueOf(queryMonthList.get(0).substring(0, 4).toString());
//                Integer monthQuery = Integer.valueOf(queryMonthList.get(0).substring(5, 7).toString());
//
//                Map<String, Object> monthHomeProfitTrendMap = new HashedMap();
//                Integer homeListCount = sql_homeList.size();
//                //存入数据库
//                Integer sql_homeListCount=0;
//                Integer sql_oneTruePrice=0;
//                if (yearQuery.equals(year)) {
//                    //取出当前年份的数据
//                    sql_oneTruePrice=oneTruePrice;
//                    //显示数据
//                    monthHomeProfitTrendMap.put("count", homeListCount);
//                    monthHomeProfitTrendMap.put("oneTruePrice", sql_oneTruePrice+nowHome_oneTruePrice);
//                    monthHomeProfitTrend.add(monthHomeProfitTrendMap);
//                } else {
//                    sql_oneTruePrice=oneTruePrice;
//                }
//                String sql_add_homeMonth = "insert into  yx_monthPrice(type,count,oneTruePrice,year,month) values(?,?,?,?,?)";
//                //type=1 居家
//                int states_add_homeMonth = jdbcTemplate.update(sql_add_homeMonth, 1, homeListCount, sql_oneTruePrice, yearQuery, monthQuery);
//                //存入数据库居家数据
//                sql_allHomeTruePrice = sql_allHomeTruePrice + oneTruePrice;
//            }
//
//
//            //昨天数据
//            String yesterday = getNewEndtime(nowTime, 7);
//            String sql_add_orderStatistics = "insert into yx_orderStatistics(club,home,allPrice,s_time) values(?,?,?,?)";
//
//            //显示数据
//            //居家显示数据
//            allHomeTruePrice=sql_allHomeTruePrice+nowHome_oneTruePrice;
//            //会所显示数据
//            allClubTruePrice=sql_allClubTruePrice+nowClub_oneTruePrice;
//
//            allTruePrice = allClubTruePrice + allHomeTruePrice;
//
//            int states_add_orderStatistics = jdbcTemplate.update(sql_add_orderStatistics, sql_allClubTruePrice, sql_allHomeTruePrice, sql_allClubTruePrice+sql_allHomeTruePrice, yesterday);
//        }
//
//        Map<String, Object> orderStatisticsMap = new HashedMap();
//        orderStatisticsMap.put("club", allClubTruePrice);
//        orderStatisticsMap.put("home", allHomeTruePrice);
//        orderStatisticsMap.put("all", allTruePrice);
//        orderStatistics.add(orderStatisticsMap);
//        map.put("orderStatistics", orderStatistics);
//
//
//
//        //收益趋势5
//        List<Map<String, Object>> profitTrend = new ArrayList<>();
//        Map<String, Object> profitTrendMap = new HashedMap();
//
//        for (int i = 0; i <12 ; i++) {
//            if (monthHomeProfitTrend.size()==i+1){
//
//            }else {
//                //月份没有全部添0补充
//                Map<String,Object>   monthClubProfitTrendMap=new HashedMap();
//                monthClubProfitTrendMap.put("count",0);
//                monthClubProfitTrendMap.put("oneTruePrice",0);
//                monthClubProfitTrend.add(monthClubProfitTrendMap);
//
//                Map<String,Object>   monthHomeProfitTrendMap=new HashedMap();
//                monthHomeProfitTrendMap.put("count",0);
//                monthHomeProfitTrendMap.put("oneTruePrice",0);
//                monthHomeProfitTrend.add(monthHomeProfitTrendMap);
//            }
//        }
//
//        profitTrendMap.put("club", monthClubProfitTrend);
//        profitTrendMap.put("home", monthHomeProfitTrend);
//        profitTrend.add(profitTrendMap);
//        System.out.println("orderStatisticsMap = " + monthHomeProfitTrend);
//        System.out.println("profitTrend = " + profitTrend);
//        map.put("profitTrend", profitTrend);
//
//
//


    }
}
