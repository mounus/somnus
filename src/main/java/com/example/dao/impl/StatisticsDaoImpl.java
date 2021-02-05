package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.StatisticsDao;
import lombok.SneakyThrows;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.HolidyUtil.getDays;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;
import static com.example.util.PriceUtil.*;
import static com.example.util.TimeUtil.*;

@Repository
public class StatisticsDaoImpl implements StatisticsDao {


    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Map<String, Object>> getComplete() {


        List<Map<String, Object>> returnList = new ArrayList<Map<String, Object>>();


        String sql = "select id from yx_user where power=1";

        List<Map<String, Integer>> uidList = (List) this.jdbcTemplate.queryForList(sql);


        for (int i = 0; i < uidList.size(); i++) {

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

            List<Integer> list1 = new ArrayList<>();
            List<Integer> list2 = new ArrayList<>();
            List<Integer> list3 = new ArrayList<>();
            List<Integer> list4 = new ArrayList<>();
            List<Integer> list5 = new ArrayList<>();
            List<Integer> list6 = new ArrayList<>();
            List<Integer> list7 = new ArrayList<>();
            List<Integer> allList = new ArrayList<>();
            StringBuffer sb = new StringBuffer();
            sb.append("select yx_matorn.id,yx_matorn.uid,yx_matorn.lid,yx_matorn.address,yx_bussiness.photo,yx_bussiness.works,yx_bussiness.heathly,yx_bussiness.qualification,yx_bussiness.grade ");
            sb.append(" from yx_matorn  left JOIN yx_bussiness on (yx_matorn.id=yx_bussiness.mid) left JOIN yx_period on (yx_matorn.id=yx_period.mid)");
            sb.append(" where  yx_bussiness.isblack=0 ");

            Integer number = 0;
            if (uidList.get(i).get("id") == 22) {
                sb.append(" and yx_matorn.lid=?");
                number = 0;
            } else {
                sb.append(" and yx_matorn.lid<>0  and yx_matorn.uid=?");
                number = uidList.get(i).get("id");
            }


            Map<String, Object> mp = new HashMap<String, Object>();
            list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {

                    Integer mid = rs.getInt("id");
                    Integer lid = rs.getInt("lid");
                    String address = rs.getString("address");
                    String photo = rs.getString("photo");
                    String works = rs.getString("works");
                    String heathly = rs.getString("heathly");
                    String qualification = rs.getString("qualification");
                    String grade = rs.getString("grade");

                    if (lid == 0) {
                        mp.put("agent", "待分配");
                    } else {
                        String sql_name = "select name from yx_user where id=?";
                        String agent = jdbcTemplate.queryForObject(sql_name, String.class, rs.getInt("uid"));
                        mp.put("agent", agent);
                    }


                    String sql_photo = "select evaluate_photo from yx_evaluate where mid=?";
                    List<String> evaluate_photo = null;

                    try {
                        evaluate_photo = (List) jdbcTemplate.queryForList(sql_photo, mid);


                    } catch (Exception e) {

                    }


                    //if (address!=null&&address!="点击选择省市区"&&photo!=null&&photo!=""&&works!=null&&works!="[]"&&heathly!=null&&heathly!="[]"&&qualification!=null&&qualification!="[]"&&grade!="待定级"){


                    if (address == null || address.equals("点击选择省市区")) {

                    } else {
                        list1.add(mid);
                    }
                    if (photo == null || photo.equals("")) {

                    } else {
                        list2.add(mid);
                    }
                    if (works == null || works.equals("[]")) {

                    } else {
                        list3.add(mid);
                    }
                    if (heathly == null || heathly.equals("[]")) {

                    } else {
                        list4.add(mid);
                    }

                    if (qualification == null || qualification.equals("[]")) {

                    } else {
                        list5.add(mid);
                    }
                    if (grade.equals("待定级")) {

                    } else {
                        list6.add(mid);
                    }
                    if (evaluate_photo.size() == 0) {

                    } else {
                        list7.add(mid);
                    }

                    if (address == null || address.equals("点击选择省市区") || photo == null || photo.equals("") || works == null || works.equals("[]") || heathly == null || heathly.equals("[]") || qualification == null || qualification.equals("[]") || grade.equals("待定级") || evaluate_photo.size() == 0) {

                    } else {
                        allList.add(mid);
                    }


                    mp.put("address", list1.size());
                    mp.put("photo", list2.size());
                    mp.put("works", list3.size());
                    mp.put("heathly", list4.size());
                    mp.put("qualification", list5.size());
                    mp.put("grade", list6.size());
                    mp.put("evaluate_photo", list7.size());
                    mp.put("allfield", allList.size());

                    return mp;

                }
            }, number);

            mp.put("total", list.size());
            returnList.add(list.get(0));

        }


        Integer total = 0;
        Integer address = 0;
        Integer photo = 0;
        Integer works = 0;
        Integer heathly = 0;
        Integer qualification = 0;
        Integer grade = 0;
        Integer evaluate_photo = 0;
        Integer allfield = 0;

        for (int i = 0; i < returnList.size(); i++) {
            total = Integer.valueOf(returnList.get(i).get("total").toString()) + total;
            address = Integer.valueOf(returnList.get(i).get("address").toString()) + address;
            photo = Integer.valueOf(returnList.get(i).get("photo").toString()) + photo;
            works = Integer.valueOf(returnList.get(i).get("works").toString()) + works;
            heathly = Integer.valueOf(returnList.get(i).get("heathly").toString()) + heathly;
            qualification = Integer.valueOf(returnList.get(i).get("qualification").toString()) + qualification;
            grade = Integer.valueOf(returnList.get(i).get("grade").toString()) + grade;
            evaluate_photo = Integer.valueOf(returnList.get(i).get("evaluate_photo").toString()) + evaluate_photo;
            allfield = Integer.valueOf(returnList.get(i).get("allfield").toString()) + allfield;

        }

        Map<String, Object> newmap = new HashMap<String, Object>();

        newmap.put("agent", "总计");
        newmap.put("total", total);
        newmap.put("address", address);
        newmap.put("photo", photo);
        newmap.put("works", works);
        newmap.put("heathly", heathly);
        newmap.put("qualification", qualification);
        newmap.put("grade", grade);
        newmap.put("evaluate_photo", evaluate_photo);
        newmap.put("allfield", allfield);
        returnList.add(newmap);
        for (int i = 0; i < returnList.size(); i++) {
            System.out.println("____" + returnList.get(i));
        }
        return returnList;
    }

    public Map<String, Integer> clubPriceCount(String sql, String firstTime, String lastTime, String nowTime, List<String> queryList, String sql_count, List<String> countQueryList) {
        Map<String, Integer> map = new HashedMap();
        List<Map<String, Object>> sql_nowClubList = jdbcTemplate.query(sql.toString(), new RowMapper<Map<String, Object>>() {
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

                List<String> dayList = getDayList(workStates, firstTime, lastTime, start_time, end_time, nowTime);
                Map<String, Object> priceMap = getClubPrice(timeType, workStates, hospitalList, clubList, dayList);

                mp.put("hospitalDay", priceMap.get("hospitalDay"));
                mp.put("clubDay", priceMap.get("clubDay"));
                mp.put("trueHospitalPrice", priceMap.get("trueHospitalPrice"));
                mp.put("trueClubPrice", priceMap.get("trueClubPrice"));
                return mp;
            }
        }, queryList.toArray());

        Integer clubPrice = 0;
        for (int j = 0; j < sql_nowClubList.size(); j++) {
            Integer trueHospitalPrice = Integer.valueOf(sql_nowClubList.get(j).get("trueHospitalPrice").toString());//单个医院实收金额
            Integer trueClubPrice = Integer.valueOf(sql_nowClubList.get(j).get("trueClubPrice").toString());//单个会所实收金额
            clubPrice = trueHospitalPrice + trueClubPrice + clubPrice;
        }
        map.put("clubPrice", clubPrice);

        //会所数量
        Integer clubCount = jdbcTemplate.queryForObject(sql_count.toString(), Integer.class, countQueryList.toArray());
        map.put("clubCount", clubCount);

        return map;
    }

    public Map<String, Integer> homePriceCount(String sql, String firstTime, String lastTime, String nowTime, List<String> queryList, String sql_count, List<String> countQueryList) {

        Map<String, Integer> map = new HashedMap();
        List<Map<String, Object>> sql_nowHomeList = this.jdbcTemplate.query(sql.toString(), new RowMapper<Map<String, Object>>() {
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

                List<String> dayList = getDayList(workStates, firstTime, lastTime, start_time, end_time, nowTime);
                Integer orderDay = dayList.size();
                Integer truePrice = orderDay * onePrice;
                mp.put("orderDay", orderDay);
                mp.put("truePrice", truePrice);

                return mp;
            }

        }, queryList.toArray());

        Integer homePrice = 0;
        for (int j = 0; j < sql_nowHomeList.size(); j++) {
            //单个实收金额
            Integer truePrice = Integer.valueOf(sql_nowHomeList.get(j).get("truePrice").toString());//单个实收金额
            //单个天数
            homePrice = truePrice + homePrice;
        }
        map.put("homePrice", homePrice);

        //会所数量
        Integer homeCount = jdbcTemplate.queryForObject(sql_count.toString(), Integer.class, countQueryList.toArray());
        map.put("homeCount", homeCount);
        return map;

    }



    @Override
    public Map<String, Object> allStatistics(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Map<String, Object> map = new HashedMap();

        Integer year = jsonObject.getInteger("year");
        Integer month = jsonObject.getInteger("month");


        //订单金额统计，收益趋势
        Map<String,Object> orderAndProfitList=orderAndProfitStatistics(year,month);

        //订单金额统计1
        List<Map<String, Object>> orderStatistics= (List<Map<String, Object>>) orderAndProfitList.get("orderStatistics");
        map.put("orderStatistics", orderStatistics);


        //收益趋势5
        List<Map<String, Object>> profitTrend= (List<Map<String, Object>>) orderAndProfitList.get("profitTrend");
        map.put("profitTrend", profitTrend);


        //除了订单金额统计，收益趋势意外的数据统计
        Map<String,Object> statisticsList=statisticsList();

        //客户统计 2
        List<Map<String, Object>> customStatistics= (List<Map<String, Object>>) statisticsList.get("customStatistics");
        map.put("customStatistics", customStatistics);

        //月嫂统计 3
        List<Map<String, Object>> matornStatistics= (List<Map<String, Object>>) statisticsList.get("matornStatistics");
        map.put("matornStatistics", matornStatistics);

        //用户统计4
        List<Map<String, Object>> userStatistics= (List<Map<String, Object>>) statisticsList.get("userStatistics");
        map.put("userStatistics", userStatistics);

        //月嫂概览8
        List<Map<String, Object>> matornOverview= (List<Map<String, Object>>) statisticsList.get("matornOverview");
        map.put("matornOverview", matornOverview);

        //客户概览6
        List<Map<String, Object>> customOverview= (List<Map<String, Object>>) statisticsList.get("customOverview");
        map.put("customOverview", customOverview);

        // 订单概览7
        List<Map<String, Object>> orderOverview= (List<Map<String, Object>>) statisticsList.get("orderOverview");
        map.put("orderOverview", orderOverview);
        return map;


    }


    public  Map<String,Object> orderAndProfitStatistics(Integer year,Integer month){
        Map<String,Object> map=new HashedMap();

        //当前时间
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String nowTime = time.format(new Date());

        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String creat_time = time1.format(new Date());
        //当前时间的年月
        Integer nowMonth = Integer.valueOf(nowTime.substring(5, 7).toString());
        Integer nowYear = Integer.valueOf(nowTime.substring(0, 4).toString());

        //订单金额统计1
        List<Map<String, Object>> orderStatistics = new ArrayList<>();
        //其他订单类型计算语句
        StringBuffer sb_price = new StringBuffer();
        sb_price.append(" select c.id,od.work_states,od.starttime,od.endtime,od.onePrice,od.timetype,od.price,od.service_day");
        sb_price.append("  from  yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1 ");

        StringBuffer sb_count = new StringBuffer();
        sb_count.append(" select count(*) from yx_custom c left join yx_order_demand od on (c.id=od.cid) where 1=1  ");
        sb_count.append(" and od.work_states>=0  and od.service_day<>0 and od.starttime>=?  and od.starttime<=? ");

        String sql_price_club = "  and od.service_type='月子会所服务' ";
        String sql_price_home = "  and od.service_type='居家服务' ";

        StringBuffer sb_clubCount = new StringBuffer();
        sb_clubCount.append(sb_count);
        sb_clubCount.append(sql_price_club);

        StringBuffer sb_homeCount = new StringBuffer();
        sb_homeCount.append(sb_count);
        sb_homeCount.append(sql_price_home);

        //公共条件语句(一个时间段内)
        String sql_month = "  and od.work_states>=0  and od.service_day<>0   and  (   (od.starttime>=?  and od.starttime<=?) or (od.endtime>=? and od.endtime<=?) or (?>=od.starttime and od.endtime>=?) )  ";


        //会所语句
        StringBuffer sb_price_club_month = new StringBuffer();
        sb_price_club_month.append(sb_price);
        sb_price_club_month.append(sql_price_club);
        sb_price_club_month.append(sql_month);

        //居家语句
        StringBuffer sb_price_home_month = new StringBuffer();
        sb_price_home_month.append(sb_price);
        sb_price_home_month.append(sql_price_home);
        sb_price_home_month.append(sql_month);

        //需要搜索的月份
        List<List> monthList = getAllMonth();
        System.out.println("monthList = " + monthList);

        /**orderStatistics结果搜索太慢，解决方法
         * 1.redis永久缓存
         * 2.存入数据库
         *阿里云服务器没有安装redis，懒得安装
         * 选择存入数据库
         *存入数据库数据为当前时间前一天数据所有数据，当天搜索为数据库数据加上当天一天的数据
         * 每一天第一次搜索修改数据库数据，前天所有数据加上昨天数据为数据库新数据
         */
        String sql_orderStatistics_count = "select count(*) from yx_orderStatistics  ";
        Integer orderStatistics_count = jdbcTemplate.queryForObject(sql_orderStatistics_count, Integer.class);

        //统计所有金额
        Integer allTruePrice = 0;
        //统计所有会所金额
        Integer allClubTruePrice = 0;
        //统计所有居家金额
        Integer allHomeTruePrice = 0;

        //昨天之前所有金额(缓存)
        Integer sql_allTruePrice = 0;
        //昨天之前所有会所金额(缓存)
        Integer sql_allClubTruePrice = 0;
        //昨天之前所有居家金额(缓存)
        Integer sql_allHomeTruePrice = 0;


        //会所当前年份12月收益
        List<Map<String, Object>> monthClubProfitTrend = new ArrayList<>();
        //居家当前年份12月收益
        List<Map<String, Object>> monthHomeProfitTrend = new ArrayList<>();

        //没有存入数据


        //当天收益条件
        List<String> queryDayList = new ArrayList<>();
        System.out.println("nowTime _______________= " + nowTime);
        queryDayList.add(nowTime);
        queryDayList.add(nowTime);
        queryDayList.add(nowTime);
        queryDayList.add(nowTime);
        queryDayList.add(nowTime);
        queryDayList.add(nowTime);

        List<String> queryCountListCount = new ArrayList<>();
        queryCountListCount.add(nowTime);
        queryCountListCount.add(nowTime);

        //会所当天收益
        Map<String, Integer> clubNowDayMap = clubPriceCount(sb_price_club_month.toString(), nowTime, nowTime, nowTime, queryDayList, sb_clubCount.toString(), queryCountListCount);
        Integer club_oneSumTruePrice = clubNowDayMap.get("clubPrice");
        Integer club_nowListCount = clubNowDayMap.get("clubCount");

        //当天居家收益
        Map<String, Integer> homeNowDayMap = homePriceCount(sb_price_club_month.toString(), nowTime, nowTime, nowTime, queryDayList, sb_homeCount.toString(), queryCountListCount);
        Integer home_oneSumTruePrice = homeNowDayMap.get("homePrice");
        Integer home_nowListCount = homeNowDayMap.get("homeCount");


        if (orderStatistics_count > 0) {
            //存入过数据

            String sql_orderStatistics = "select club,home,allPrice,s_time from yx_orderStatistics where id=(select max(id) from yx_orderStatistics)";
            Map<String, Object> orderStatisticsMap = jdbcTemplate.queryForMap(sql_orderStatistics);
            //数据库搜索全部金额的数据
            Integer sel_allClubTruePrice = Integer.valueOf(orderStatisticsMap.get("club").toString());
            Integer sel_allHomeTruePrice = Integer.valueOf(orderStatisticsMap.get("home").toString());

            //需要添加到数据库的数据
            sql_allClubTruePrice = sel_allClubTruePrice;
            sql_allHomeTruePrice = sel_allHomeTruePrice;

            //显示数据
            allClubTruePrice = sel_allClubTruePrice;
            allHomeTruePrice = sel_allHomeTruePrice;

            Integer clubMonthPrice_Count = 0;
            Integer clubMonthPrice_Price = 0;

            Integer homeMonthPrice_Count = 0;
            Integer homeMonthPrice_Price = 0;

            String s_time = orderStatisticsMap.get("s_time").toString();

            List<String> dayList = getDays(getNewEndtime(s_time, -1), nowTime);
            System.out.println("dayList = " + dayList);

            //缓存更新下一个月的会所这个月缓存(不包括当天)
            Integer monthClub_oneTruePrice = 0;
            //缓存更新下一个月的居家这个月缓存(不包括当天)
            Integer monthHome_oneTruePrice = 0;

            for (int i = 0; i < dayList.size(); i++) {
                List<String> queryDaysList = new ArrayList<>();

                queryDaysList.add(dayList.get(i));
                queryDaysList.add(dayList.get(i));
                queryDaysList.add(dayList.get(i));
                queryDaysList.add(dayList.get(i));
                queryDaysList.add(dayList.get(i));
                queryDaysList.add(dayList.get(i));


                List<String> queryCountList = new ArrayList<>();
                queryCountList.add(dayList.get(i));
                queryCountList.add(dayList.get(i));

                Integer dayYear = Integer.valueOf(dayList.get(i).substring(0, 4));
                Integer dayMonth = Integer.valueOf(dayList.get(i).substring(5, 7));

                if (nowYear.equals(year)) {
                    //搜索年份是本年
                    if (month==13){
                        String sql_monthPriceCount = "select count(*) from yx_monthPrice where year=? and  month=? ";
                        Integer monthPriceCount = jdbcTemplate.queryForObject(sql_monthPriceCount, Integer.class, dayYear, dayMonth);
                        if (monthPriceCount > 0) {
                            //搜索时间这个月已经创建
                            String sql_clubMonthPrice = "select count,oneTruePrice from yx_monthPrice where type=0 and year=? and month=? ";
                            monthClubProfitTrend = jdbcTemplate.queryForList(sql_clubMonthPrice, dayYear, dayMonth);
                            clubMonthPrice_Count = Integer.valueOf(monthClubProfitTrend.get(monthClubProfitTrend.size() - 1).get("count").toString());
                            clubMonthPrice_Price = Integer.valueOf(monthClubProfitTrend.get(monthClubProfitTrend.size() - 1).get("oneTruePrice").toString());

                            String sql_homeMonthPrice = "select count,oneTruePrice from yx_monthPrice where type=1 and year=? and month=? ";
                            monthHomeProfitTrend = jdbcTemplate.queryForList(sql_homeMonthPrice, dayYear, dayMonth);
                            homeMonthPrice_Count = Integer.valueOf(monthHomeProfitTrend.get(monthHomeProfitTrend.size() - 1).get("count").toString());
                            homeMonthPrice_Price = Integer.valueOf(monthHomeProfitTrend.get(monthHomeProfitTrend.size() - 1).get("oneTruePrice").toString());

                        } else {
                            //搜索时间这个月没有创建
                        }
                    }else {
                        String sql_monthPriceCount = "select count(*) from yx_monthPrice where year=? and  month=? ";
                        Integer monthPriceCount = jdbcTemplate.queryForObject(sql_monthPriceCount, Integer.class, dayYear, dayMonth);
                        if (monthPriceCount > 0) {
                            //搜索时间这个月已经创建
                            String sql_clubMonthPrice = "select count,oneTruePrice from yx_monthPrice where type=0 and year=? and month=? ";
                            monthClubProfitTrend = jdbcTemplate.queryForList(sql_clubMonthPrice, dayYear, dayMonth);
                            clubMonthPrice_Count = Integer.valueOf(monthClubProfitTrend.get(monthClubProfitTrend.size() - 1).get("count").toString());
                            clubMonthPrice_Price = Integer.valueOf(monthClubProfitTrend.get(monthClubProfitTrend.size() - 1).get("oneTruePrice").toString());

                            String sql_homeMonthPrice = "select count,oneTruePrice from yx_monthPrice where type=1 and year=? and month=? ";
                            monthHomeProfitTrend = jdbcTemplate.queryForList(sql_homeMonthPrice, dayYear, dayMonth);
                            homeMonthPrice_Count = Integer.valueOf(monthHomeProfitTrend.get(monthHomeProfitTrend.size() - 1).get("count").toString());
                            homeMonthPrice_Price = Integer.valueOf(monthHomeProfitTrend.get(monthHomeProfitTrend.size() - 1).get("oneTruePrice").toString());

                        } else {
                            //搜索时间这个月没有创建
                        }

                    }


                } else {
                    //不是本年
                    //直接搜索数据库里的数据
                }


                //会所当天收益
                Map<String, Integer> clubNowMonthMap = clubPriceCount(sb_price_club_month.toString(), dayList.get(i), dayList.get(i), nowTime, queryDayList, sb_clubCount.toString(), queryCountList);
                //一个时间段金额
                Integer club_sumTruePrice = clubNowMonthMap.get("clubPrice");
                Integer clubListCount = clubNowMonthMap.get("clubCount");

                //会所显示数据
                allClubTruePrice = allClubTruePrice + club_sumTruePrice;


                //当天居家收益
                Map<String, Integer> homeNowMonthMap = homePriceCount(sb_price_club_month.toString(), nowTime, nowTime, nowTime, queryDayList, sb_homeCount.toString(), queryCountListCount);
                Integer home_sumTruePrice = homeNowMonthMap.get("homePrice");
                Integer homeListCount = homeNowMonthMap.get("homeCount");
                //居家显示数据
                allHomeTruePrice = allHomeTruePrice + home_sumTruePrice;

                if (nowYear.equals(year) && month == 13) {
                    //返回显示
                    //会所数量
                    clubMonthPrice_Count = clubListCount + clubMonthPrice_Count;
                    //金额
                    clubMonthPrice_Price = club_oneSumTruePrice + clubMonthPrice_Price;

                    //返回显示
                    //居家数量
                    homeMonthPrice_Count = homeListCount + homeMonthPrice_Count;
                    //金额
                    homeMonthPrice_Price = home_oneSumTruePrice + homeMonthPrice_Price;
                }
                if (!nowYear.equals(year) && month == 13) {
                    //不是本年，

                }


                if (dayList.size() > 1) {
                    //数据库存入数据时间与当前时间间隔1天以上
                    //显示数据为数据库数据加上s_time(不包括s_time这天数据)到当前时间的数据
                    //并且添加新的信息
                    if (i < dayList.size() - 1) {
                        //订单统计总金额，会所总金额，居家总金额加入数据库
                        //除了最后一个都加入数据库
                        String sql_day_count = "select count(*) from yx_orderStatistics where s_time=?";
                        Integer day_count = jdbcTemplate.queryForObject(sql_day_count, Integer.class, dayList.get(i));

                        //总金额的更新
                        if (day_count > 0) {
                            //dayList.get(finalI)这个时间已经添加过，不需要再添加
                        } else {
                            sql_allClubTruePrice = sql_allClubTruePrice + club_sumTruePrice;
                            sql_allHomeTruePrice = sql_allHomeTruePrice + home_sumTruePrice;
                            String sql_add_orderStatistics = "insert into yx_orderStatistics(club,home,allPrice,s_time,creat_time) values(?,?,?,?,?)";
                            sql_allTruePrice = sql_allClubTruePrice + sql_allHomeTruePrice;
                            int states_add_orderStatistics = jdbcTemplate.update(sql_add_orderStatistics, sql_allClubTruePrice, sql_allHomeTruePrice, sql_allTruePrice, dayList.get(i).toString(),creat_time);
                        }

                        if (nowYear.equals(year) && month == 13) {
                            //添加更新每月数据
                            String sql_monthCount = "select count(*) from yx_monthPrice where year=? and month=? ";

                            Integer count_monthCount = jdbcTemplate.queryForObject(sql_monthCount, Integer.class, dayYear, dayMonth);
                            if (count_monthCount > 0) {
                                //已经创建
                                //会所某个月缓存修改更新
                                String sql_updateClubMonth = "update yx_monthPrice set count=?,oneTruePrice=? where type=0 and year=? and month=?";
                                int states_updateClubMonth = jdbcTemplate.update(sql_updateClubMonth, clubMonthPrice_Count, clubMonthPrice_Price, dayYear, dayMonth);

                                //居家某个月缓存修改更新
                                String sql_updateHomeMonth = "update yx_monthPrice set count=?,oneTruePrice=? where type=1 and year=? and month=?";
                                int states_updateHomeMonth = jdbcTemplate.update(sql_updateHomeMonth, homeMonthPrice_Count, homeMonthPrice_Price, dayYear, dayMonth);

                            } else {
                                //没有创建
                                String sql_addMonth = "insert into  yx_monthPrice(type,count,oneTruePrice,year,month,creat_time) values(?,?,?,?,?,?)";
                                //获取循环时间的下一个月

                                //会所添加
                                int states_addClubMonth = jdbcTemplate.update(sql_addMonth, 0, clubMonthPrice_Count, clubMonthPrice_Price, dayYear, dayMonth,creat_time);
                                //居家添加
                                int states_addHomeMonth = jdbcTemplate.update(sql_addMonth, 1, homeMonthPrice_Count, homeMonthPrice_Price, dayYear, dayMonth,creat_time);

                            }

                            //添加更新每天数据
                            String sql_day_Month = "insert into  yx_dayPrice(type,count,oneTruePrice,year,month,day,creat_time) values(?,?,?,?,?,?,?)";
                            Integer day = Integer.valueOf(dayList.get(i).substring(8));

                            int states_dayClub = jdbcTemplate.update(sql_day_Month, 0, clubListCount, club_sumTruePrice, dayYear, dayMonth, day,creat_time);

                            int states_dayHome = jdbcTemplate.update(sql_day_Month, 1, homeListCount, home_sumTruePrice, dayYear, dayMonth, day,creat_time);


                        }
                        if (!nowYear.equals(year) && month == 13) {
                            //不是本年 且月份为空
                        }


                    } else {
                        //最后一个数据不加入数据库
                    }
                } else {

                    //数据库存入数据时间与当前时间相邻
                    //显示数据为数据库数据加上当天数据
                }


            }//for结尾

            if (year.equals(nowYear)) {
                //搜索年份是本年
                if (month != 13) {
                    //搜索月份不为空
                    String sql_count_dayPrice = "select count(*) from yx_dayPrice where type=0 and  year=? and month=?";
                    int states_count = jdbcTemplate.queryForObject(sql_count_dayPrice, Integer.class, year, month);

                    Integer daySize =0;
                    Integer type=0;
                    if (month.equals(nowMonth)){
                        //搜索时间 是当前月份
                        List<String> yearMonthList=new ArrayList<>();
                        String firstTime=getYearMonth(year, month).get(0);
                        yearMonthList.add(firstTime);
                        yearMonthList.add(nowTime);
                        daySize=yearMonthList.size()-1;
                        if (states_count==daySize){
                            //已经创建
                            type=1;
                        }else {
                            type=0;
                            String sql_delete="delete from yx_dayPrice where year=? and month=?";
                            int states_delete=jdbcTemplate.update(sql_delete,year,month);

                        }


                    }else {
                        //搜索时间 不是当前月份
                        daySize=  dayByMonth(year, month);
                        if (states_count==daySize){
                            //已经创建
                            type=1;
                        }else {
                            type=0;
                            String sql_delete="delete from yx_dayPrice where year=? and month=?";
                            int states_delete=jdbcTemplate.update(sql_delete,year,month);

                        }
                    }

                    if (type > 0) {
                        //已经创建

                        if (nowMonth.equals(month)) {
                            //搜索年份是当前年份本月
                            String sql_clubDayPrice = "select count,oneTruePrice from yx_dayPrice where type=0 and year=? and month=? ";
                            monthClubProfitTrend = jdbcTemplate.queryForList(sql_clubDayPrice, year, month);
                            Map<String,Object> monthClubMap=new HashedMap();
                            monthClubMap.put("count",club_nowListCount);
                            monthClubMap.put("oneTruePrice",club_oneSumTruePrice);
                            monthClubProfitTrend.add(monthClubMap);

                            String sql_homeDayPrice = "select count,oneTruePrice from yx_dayPrice where type=1 and year=? and month=? ";
                            monthHomeProfitTrend = jdbcTemplate.queryForList(sql_homeDayPrice, year, month);

                            Map<String,Object> monthHomeMap=new HashedMap();
                            monthHomeMap.put("count",home_nowListCount);
                            monthHomeMap.put("oneTruePrice",home_oneSumTruePrice);
                            monthHomeProfitTrend.add(monthHomeMap);

                        } else {
                            //搜索年份不是是当前年份本月

                            String sql_clubDayPrice = "select count,oneTruePrice from yx_dayPrice where type=0 and year=? and month=? ";
                            monthClubProfitTrend = jdbcTemplate.queryForList(sql_clubDayPrice, year, month);

                            String sql_homeDayPrice = "select count,oneTruePrice from yx_dayPrice where type=1 and year=? and month=? ";
                            monthHomeProfitTrend = jdbcTemplate.queryForList(sql_homeDayPrice, year, month);
                        }

                    } else {
                        //没有创建

                        if (month > nowMonth) {
                            //搜索时间月份 大于 当前时间月份
                            monthClubProfitTrend.clear();
                            monthHomeProfitTrend.clear();
                        } else {
                            //month<=nowMonth
                            List<String> yearMonthList = new ArrayList<>();
                            if (month==nowMonth){
                                String firstTime=getYearMonth(year, month).get(0);
                                yearMonthList.add(firstTime);
                                yearMonthList.add(nowTime);
                            }else {
                                yearMonthList= getYearMonth(year, month);
                            }
                            List<String> moList = getDays(yearMonthList.get(0), yearMonthList.get(1));
                            String sql_day_Month = "insert into  yx_dayPrice(type,count,oneTruePrice,year,month,day,creat_time) values(?,?,?,?,?,?,?)";
                            for (int i = 0; i < moList.size(); i++) {

                                List<String> queryMoList = new ArrayList<>();
                                queryMoList.add(moList.get(i));
                                queryMoList.add(moList.get(i));
                                queryMoList.add(moList.get(i));
                                queryMoList.add(moList.get(i));
                                queryMoList.add(moList.get(i));
                                queryMoList.add(moList.get(i));
                                Integer day = Integer.valueOf(moList.get(i).substring(8));

                                List<String> count_queryMoList = new ArrayList<>();
                                count_queryMoList.add(moList.get(i));
                                count_queryMoList.add(moList.get(i));

                                Map<String, Integer> clubNowMonthMap = clubPriceCount(sb_price_club_month.toString(), moList.get(i), moList.get(i), nowTime, queryMoList, sb_clubCount.toString(), count_queryMoList);
                                //一个时间段金额
                                Integer club_sumTruePrice = clubNowMonthMap.get("clubPrice");
                                Integer clubListCount = clubNowMonthMap.get("clubCount");



                                Map<String, Integer> homeNowMonthMap = homePriceCount(sb_price_home_month.toString(), moList.get(i), moList.get(i), nowTime, queryMoList, sb_homeCount.toString(), count_queryMoList);
                                //一个时间段金额
                                Integer home_sumTruePrice = homeNowMonthMap.get("homePrice");
                                Integer homeListCount = homeNowMonthMap.get("homeCount");

                                if (month==nowMonth){
                                    if (i<moList.size()-1){
                                        int states_dayClub = jdbcTemplate.update(sql_day_Month, 0, clubListCount, club_sumTruePrice, year, month, day,creat_time);
                                        int states_dayHome = jdbcTemplate.update(sql_day_Month, 1, homeListCount, home_sumTruePrice, year, month, day,creat_time);
                                    }else {

                                    }

                                }else {

                                    int states_dayClub = jdbcTemplate.update(sql_day_Month, 0, clubListCount, club_sumTruePrice, year, month, day,creat_time);

                                    int states_dayHome = jdbcTemplate.update(sql_day_Month, 1, homeListCount, home_sumTruePrice, year, month, day,creat_time);
                                }


                                Map<String, Object> monthClubProfitTrendMap = new HashedMap();
                                monthClubProfitTrendMap.put("count", clubListCount);
                                monthClubProfitTrendMap.put("oneTruePrice", club_sumTruePrice);
                                monthClubProfitTrend.add(monthClubProfitTrendMap);

                                Map<String, Object> monthHomeProfitTrendMap = new HashedMap();
                                monthHomeProfitTrendMap.put("count", homeListCount);
                                monthHomeProfitTrendMap.put("oneTruePrice", home_sumTruePrice);
                                monthHomeProfitTrend.add(monthHomeProfitTrendMap);

                            }

                        }

                    }


                } else {
                    //搜索月份为空
                    monthClubProfitTrend.get(monthClubProfitTrend.size() - 1).put("count", clubMonthPrice_Count);
                    monthClubProfitTrend.get(monthClubProfitTrend.size() - 1).put("oneTruePrice", clubMonthPrice_Price);

                    monthHomeProfitTrend.get(monthHomeProfitTrend.size() - 1).put("count", homeMonthPrice_Count);
                    monthHomeProfitTrend.get(monthHomeProfitTrend.size() - 1).put("oneTruePrice", homeMonthPrice_Price);
                }

            }
            if (!year.equals(nowYear)) {
                //不是本年 且搜索月份为空
                String sql_day_Month = "insert into  yx_dayPrice(type,count,oneTruePrice,year,month,day,creat_time) values(?,?,?,?,?,?,?)";

                if (month != 13) {
                    //搜索月份不为空
                    String sql_count_dayPrice = "select count(*) from yx_dayPrice where type =0 and year=? and month=?";
                    int states_count = jdbcTemplate.queryForObject(sql_count_dayPrice, Integer.class, year, month);

                    Integer daySize = dayByMonth(year, month);
                    System.out.println("day = " + daySize);
                    Integer type=0;
                    if (states_count==daySize){
                        //已经创建
                        type=1;
                    }else {
                        type=0;
                        String sql_delete="delete from yx_dayPrice where year=? and month=?";
                        int states_delete=jdbcTemplate.update(sql_delete,year,month);

                    }

                    if (type > 0) {
                        //已经创建
                        String sql_clubDayPrice = "select count,oneTruePrice from yx_dayPrice where type=0 and year=? and month=? ";
                        monthClubProfitTrend = jdbcTemplate.queryForList(sql_clubDayPrice, year, month);

                        String sql_homeDayPrice = "select count,oneTruePrice from yx_dayPrice where type=1 and year=? and month=? ";
                        monthHomeProfitTrend = jdbcTemplate.queryForList(sql_homeDayPrice, year, month);



                    } else {
                        List<String> yearMonthList = getYearMonth(year, month);
                        List<String> moList = getDays(yearMonthList.get(0), yearMonthList.get(1));

                        for (int i = 0; i < moList.size(); i++) {

                            List<String> queryMoList = new ArrayList<>();
                            queryMoList.add(moList.get(i));
                            queryMoList.add(moList.get(i));
                            queryMoList.add(moList.get(i));
                            queryMoList.add(moList.get(i));
                            queryMoList.add(moList.get(i));
                            queryMoList.add(moList.get(i));
                            Integer day = Integer.valueOf(moList.get(i).substring(8));

                            List<String> count_queryMoList = new ArrayList<>();
                            count_queryMoList.add(moList.get(i));
                            count_queryMoList.add(moList.get(i));

                            Map<String, Integer> clubNowMonthMap = clubPriceCount(sb_price_club_month.toString(), moList.get(i), moList.get(i), nowTime, queryMoList, sb_clubCount.toString(), count_queryMoList);
                            //一个时间段金额
                            Integer club_sumTruePrice = clubNowMonthMap.get("clubPrice");
                            Integer clubListCount = clubNowMonthMap.get("clubCount");


                            int states_dayClub = jdbcTemplate.update(sql_day_Month, 0, clubListCount, club_sumTruePrice, year, month, day,creat_time);


                            Map<String, Integer> homeNowMonthMap = homePriceCount(sb_price_home_month.toString(), moList.get(i), moList.get(i), nowTime, queryMoList, sb_homeCount.toString(), count_queryMoList);
                            //一个时间段金额
                            Integer home_sumTruePrice = homeNowMonthMap.get("homePrice");
                            Integer homeListCount = homeNowMonthMap.get("homeCount");

                            int states_dayHome = jdbcTemplate.update(sql_day_Month, 1, homeListCount, home_sumTruePrice, year, month, day,creat_time);

                            Map<String, Object> monthClubProfitTrendMap = new HashedMap();
                            monthClubProfitTrendMap.put("count", clubListCount);
                            monthClubProfitTrendMap.put("oneTruePrice", club_sumTruePrice);
                            monthClubProfitTrend.add(monthClubProfitTrendMap);

                            Map<String, Object> monthHomeProfitTrendMap = new HashedMap();
                            monthHomeProfitTrendMap.put("count", homeListCount);
                            monthHomeProfitTrendMap.put("oneTruePrice", home_sumTruePrice);
                            monthHomeProfitTrend.add(monthHomeProfitTrendMap);

                        }
                    }


                } else {
                    String sql_clubMonthPrice = "select count,oneTruePrice from yx_monthPrice where type=0 and year=?  ";
                    monthClubProfitTrend = jdbcTemplate.queryForList(sql_clubMonthPrice, year);
                    System.out.println("22222222222222222222____________________= " + monthClubProfitTrend);

                    String sql_homeMonthPrice = "select count,oneTruePrice from yx_monthPrice where type=1 and year=?  ";
                    monthHomeProfitTrend = jdbcTemplate.queryForList(sql_homeMonthPrice, year);

                    System.out.println("333333333333333333333" + monthHomeProfitTrend);
                }

            }


            //显示全部金额
            allTruePrice = allClubTruePrice + allHomeTruePrice;
            System.out.println("allClubTruePrice = " + allClubTruePrice);
            System.out.println("allHomeTruePrice = " + allHomeTruePrice);
            System.out.println("allTruePrice = " + allTruePrice);


        } else {

            for (int i = 0; i < monthList.size(); i++) {
                String monthFirst = monthList.get(i).get(0).toString();
                String monthLast = monthList.get(i).get(1).toString();
                List<String> queryMonthList = new ArrayList<>();
                queryMonthList.add(monthFirst);
                queryMonthList.add(monthLast);
                queryMonthList.add(monthFirst);
                queryMonthList.add(monthLast);
                queryMonthList.add(monthFirst);
                queryMonthList.add(monthLast);

                Integer yearQuery = Integer.valueOf(queryMonthList.get(0).substring(0, 4).toString());
                Integer monthQuery = Integer.valueOf(queryMonthList.get(0).substring(5, 7).toString());
                //存入数据库
                //这个月产生的订单数
                List<String> queryCountList = new ArrayList<>();
                queryCountList.add(monthFirst);
                queryCountList.add(monthLast);

                Map<String, Integer> clubMonthMap = clubPriceCount(sb_price_club_month.toString(), monthFirst, monthLast, nowTime, queryMonthList, sb_clubCount.toString(), queryCountList);
                //一个时间段金额
                Integer sumTruePrice = clubMonthMap.get("clubPrice");
                //一个时间段添加的订单
                Integer clubListCount = clubMonthMap.get("clubCount");


                Integer sql_oneTruePrice = 0;
                Map<String, Object> monthClubProfitTrendMap = new HashedMap();
                if (yearQuery.equals(year)) {
                    //取出当前年份的数据
                    sql_oneTruePrice = sumTruePrice;
                    if (monthQuery.equals(nowMonth)) {
                        //当前月份要加上当天的缓存
                        sql_oneTruePrice = sql_oneTruePrice + club_oneSumTruePrice;
                        clubListCount = clubListCount + club_nowListCount;
                    } else {

                    }
                    monthClubProfitTrendMap.put("count", clubListCount);
                    monthClubProfitTrendMap.put("oneTruePrice", sql_oneTruePrice);
                    System.out.println("monthClubProfitTrendMap = " + monthClubProfitTrendMap);
                    monthClubProfitTrend.add(monthClubProfitTrendMap);
                } else {

                }

                String sql_add_homeMonth = "insert into  yx_monthPrice(type,count,oneTruePrice,year,month,creat_time) values(?,?,?,?,?,?)";
                //type=0 会所
                int states_add_clubMonth = jdbcTemplate.update(sql_add_homeMonth, 0, clubListCount, sumTruePrice, yearQuery, monthQuery,creat_time);
                sql_allClubTruePrice = sql_allClubTruePrice + sumTruePrice;

            }


            //昨天之前的居家收益
            for (int i = 0; i < monthList.size(); i++) {
                String monthFirst = monthList.get(i).get(0).toString();
                String monthLast = monthList.get(i).get(1).toString();
                //收益条件
                List<String> queryMonthList = new ArrayList<>();
                queryMonthList.add(monthFirst);
                queryMonthList.add(monthLast);
                queryMonthList.add(monthFirst);
                queryMonthList.add(monthLast);
                queryMonthList.add(monthFirst);
                queryMonthList.add(monthLast);

                Integer yearQuery = Integer.valueOf(queryMonthList.get(0).substring(0, 4).toString());
                Integer monthQuery = Integer.valueOf(queryMonthList.get(0).substring(5, 7).toString());

                //查询数量条件
                List<String> queryCountList = new ArrayList<>();
                queryCountList.add(monthFirst);
                queryCountList.add(monthLast);

                //存入数据库
                //这个月产生的订单数

                Map<String, Integer> homeMonthMap = homePriceCount(sb_price_home_month.toString(), monthFirst, monthLast, nowTime, queryMonthList, sb_homeCount.toString(), queryCountList);
                //金额
                Integer sumTruePrice = homeMonthMap.get("homePrice");
                //数量
                Integer homeListCount = homeMonthMap.get("homeCount");

                //当前年份一个月的收益
                Integer sql_oneTruePrice = 0;
                Map<String, Object> monthHomeProfitTrendMap = new HashedMap();
                if (yearQuery.equals(year)) {
                    //取出当前年份的数据
                    sql_oneTruePrice = sumTruePrice;
                    if (monthQuery.equals(nowMonth)) {
                        sql_oneTruePrice = sql_oneTruePrice + home_oneSumTruePrice;
                        homeListCount = homeListCount + home_nowListCount;
                    } else {

                    }
                    //显示数据
                    monthHomeProfitTrendMap.put("count", homeListCount);
                    monthHomeProfitTrendMap.put("oneTruePrice", sql_oneTruePrice);
                    monthHomeProfitTrend.add(monthHomeProfitTrendMap);
                } else {

                }

                String sql_add_homeMonth = "insert into  yx_monthPrice(type,count,oneTruePrice,year,month,creat_time) values(?,?,?,?,?,?)";
                //type=1 居家
                int states_add_homeMonth = jdbcTemplate.update(sql_add_homeMonth, 1, homeListCount, sumTruePrice, yearQuery, monthQuery,creat_time);
                //存入数据库居家收益
                sql_allHomeTruePrice = sql_allHomeTruePrice + sumTruePrice;

            }


            System.out.println("allHomeTruePrice = " + allHomeTruePrice);
            //昨天数据添加数据库
            String yesterday = getNewEndtime(nowTime, 1);
            sql_allTruePrice = sql_allClubTruePrice + sql_allHomeTruePrice;

            String sql_add_orderStatistics = "insert into yx_orderStatistics(club,home,allPrice,s_time,creat_time) values(?,?,?,?,?)";
            int states_add_orderStatistics = jdbcTemplate.update(sql_add_orderStatistics, sql_allClubTruePrice, sql_allHomeTruePrice, sql_allTruePrice, yesterday,creat_time);

            //会所显示收益
            allClubTruePrice = sql_allClubTruePrice + club_oneSumTruePrice;
            allHomeTruePrice = sql_allHomeTruePrice + home_oneSumTruePrice;

        }


        Map<String, Object> orderStatisticsMap = new HashedMap();
        orderStatisticsMap.put("club", allClubTruePrice);
        orderStatisticsMap.put("home", allHomeTruePrice);
        orderStatisticsMap.put("all", allTruePrice);
        orderStatistics.add(orderStatisticsMap);
        map.put("orderStatistics", orderStatistics);


        //收益趋势5
        List<Map<String, Object>> profitTrend = new ArrayList<>();
        Map<String, Object> profitTrendMap = new HashedMap();

        if (month != 13) {
            //搜索月份不为空
            Integer day = dayByMonth(year, month);

            if (day == monthHomeProfitTrend.size()) {

            } else {
                for (int i = 0; i < day; i++) {
                    if ( i + 1<=monthHomeProfitTrend.size() ) {
                    } else {
                        //月份没有全部添0补充
                        Map<String, Object> monthClubProfitTrendMap = new HashedMap();
                        monthClubProfitTrendMap.put("count", 0);
                        monthClubProfitTrendMap.put("oneTruePrice", 0);
                        monthClubProfitTrend.add(monthClubProfitTrendMap);

                        Map<String, Object> monthHomeProfitTrendMap = new HashedMap();
                        monthHomeProfitTrendMap.put("count", 0);
                        monthHomeProfitTrendMap.put("oneTruePrice", 0);
                        monthHomeProfitTrend.add(monthHomeProfitTrendMap);
                    }
                }
            }


        } else {

            if (monthHomeProfitTrend.size() == 12) {

            } else {
                for (int i = 0; i < 12; i++) {
                    if (i + 1<=monthHomeProfitTrend.size() ) {
                    } else {
                        //月份没有全部添0补充
                        Map<String, Object> monthClubProfitTrendMap = new HashedMap();
                        monthClubProfitTrendMap.put("count", 0);
                        monthClubProfitTrendMap.put("oneTruePrice", 0);
                        monthClubProfitTrend.add(monthClubProfitTrendMap);

                        Map<String, Object> monthHomeProfitTrendMap = new HashedMap();
                        monthHomeProfitTrendMap.put("count", 0);
                        monthHomeProfitTrendMap.put("oneTruePrice", 0);
                        monthHomeProfitTrend.add(monthHomeProfitTrendMap);
                    }
                }
            }


        }


        profitTrendMap.put("club", monthClubProfitTrend);
        profitTrendMap.put("home", monthHomeProfitTrend);
        profitTrend.add(profitTrendMap);

        map.put("profitTrend", profitTrend);

        return map;

    }

    /**
     * 数据中心除了（订单金额统计，收益趋势）之外的数据显示
     * @return
     */
    public  Map<String,Object> statisticsList(){

        Map<String,Object> map=new HashedMap();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
        String nowTime = time.format(new Date());

        //客户统计 2
        List<Map<String, Object>> customStatistics = new ArrayList<>();
        Map<String, Object> customStatisticsMap = new HashedMap();
        String sql_custom_all = "select count(*) from yx_custom  ";
        Integer all_count = jdbcTemplate.queryForObject(sql_custom_all, Integer.class);
        customStatisticsMap.put("all", all_count);
        String sql_custom_deal = "select count(*) from yx_custom c left join yx_order_demand od on (c.id=od.cid) where od.work_states=2 ";
        Integer deal_count = jdbcTemplate.queryForObject(sql_custom_deal, Integer.class);
        customStatisticsMap.put("deal", deal_count);
        // 创建一个数值格式化对象
        NumberFormat numberFormat = NumberFormat.getInstance();
        // 设置精确到小数点后2位
        numberFormat.setMaximumFractionDigits(0);
        String result = numberFormat.format((float) deal_count / (float) all_count * 100);
        customStatisticsMap.put("result", result);
        customStatistics.add(customStatisticsMap);
        map.put("customStatistics", customStatistics);

        StringBuffer sb_matorn = new StringBuffer();
        //月嫂数量查询公共语句
        sb_matorn.append("select count(*) from yx_matorn m left join yx_bussiness b on (m.id=b.mid) where m.idtype=1   ");

        //月嫂统计 3
        List<List> sql_matornList = new ArrayList<>();
        List<String> sql_list = new ArrayList<>();
        sql_list.add("all");
        sql_list.add(" ");
        List<String> sql_list1 = new ArrayList<>();
        sql_list1.add("order");
        sql_list1.add(" and b.isorder=1 ");
        List<String> sql_list2 = new ArrayList<>();
        sql_list2.add("black");
        sql_list2.add(" and b.isblack=1 ");
        sql_matornList.add(sql_list);
        sql_matornList.add(sql_list1);
        sql_matornList.add(sql_list2);

        List<Map<String, Object>> matornStatistics = new ArrayList<>();
        for (int i = 0; i < sql_matornList.size(); i++) {
            Map<String, Object> matornMap = new HashMap<>();
            StringBuffer sb_matornStatistics = new StringBuffer();
            sb_matornStatistics.reverse();//初始化
            sb_matornStatistics.append(sb_matorn);
            sb_matornStatistics.append(sql_matornList.get(i).get(1));
            Integer count = jdbcTemplate.queryForObject(sb_matornStatistics.toString(), Integer.class);
            matornMap.put(sql_matornList.get(i).get(0).toString(), count);
            matornStatistics.add(matornMap);
        }
        map.put("matornStatistics", matornStatistics);


        //用户统计4
        StringBuffer sb_user = new StringBuffer();
        sb_user.append("select count(*) from yx_user where 1=1 ");
        List<List> sql_userList = new ArrayList<>();
        List<String> sql_uList = new ArrayList<>();
        sql_uList.add("all");
        sql_uList.add(" and power<>40");
        List<String> sql_uList1 = new ArrayList<>();
        sql_uList1.add("company");
        sql_uList1.add(" and power<>30 and power<>40 ");
        List<String> sql_uList2 = new ArrayList<>();
        sql_uList2.add("mechanism");
        sql_uList2.add(" and power=30 ");
        sql_userList.add(sql_uList);
        sql_userList.add(sql_uList1);
        sql_userList.add(sql_uList2);
        List<Map<String, Object>> userStatistics = new ArrayList<>();
        for (int i = 0; i < sql_userList.size(); i++) {
            Map<String, Object> userMap = new HashMap<>();
            StringBuffer sb_userStatistics = new StringBuffer();

            sb_userStatistics.reverse();//初始化
            sb_userStatistics.append(sb_user);
            sb_userStatistics.append(sql_userList.get(i).get(1));
            Integer count = jdbcTemplate.queryForObject(sb_userStatistics.toString(), Integer.class);
            userMap.put(sql_userList.get(i).get(0).toString(), count);
            userStatistics.add(userMap);
        }
        map.put("userStatistics", userStatistics);


        //月嫂概览8
        String[] gradeString = {"N", "H", "Y1", "Y2", "Y3", "X", "P"};
        List<String> gradeList = Arrays.asList(gradeString);
        //服务中
        String sql_order = " and b.isorder =1  and b.grade=?";
        String sql_isOrder = " and b.isorder =0 and b.grade=? ";

        List<Map<String, Object>> matornOverview = new ArrayList<>();
        for (int i = 0; i < gradeList.size(); i++) {
            Map<String, Object> matornMap = new HashMap<>();
            matornMap.put("grade", gradeList.get(i));
            //服务中
            StringBuffer sb_order = new StringBuffer();
            sb_order.reverse();//初始化
            sb_order.append(sb_matorn);
            sb_order.append(sql_order);
            Integer order_count = jdbcTemplate.queryForObject(sb_order.toString(), Integer.class, gradeList.get(i));
            matornMap.put("order", order_count);
            //未服务中
            StringBuffer sb_isOrder = new StringBuffer();
            sb_isOrder.reverse();//初始化
            sb_isOrder.append(sb_matorn);
            sb_isOrder.append(sql_isOrder);
            Integer isOrder_count = jdbcTemplate.queryForObject(sb_isOrder.toString(), Integer.class, gradeList.get(i));
            matornMap.put("isOrder", isOrder_count);
            matornOverview.add(matornMap);
        }
        map.put("matornOverview", matornOverview);


        //客户概览6
        List<Map<String, Object>> customOverview = new ArrayList<>();
        //会所
        Map<String, Object> customClubMap = new HashedMap();
        String sql_custom_club_all = "select count(*) from yx_custom c left join yx_order_demand od on (c.id=od.cid) where od.service_type='月子会所服务' ";
        //会所全部客户
        Integer custom_club_all_count = jdbcTemplate.queryForObject(sql_custom_club_all, Integer.class);
        String sql_custom_club_deal = "select count(*) from yx_custom c left join yx_order_demand od on (c.id=od.cid) where od.service_type='月子会所服务' and od.work_states is not null and od.work_states<>0 ";
        //会所成交客户
        Integer custom_club_deal_count = jdbcTemplate.queryForObject(sql_custom_club_deal, Integer.class);
        customClubMap.put("deal", custom_club_deal_count);
        //会所意向客户
        Integer custom_club_intention_count = custom_club_all_count - custom_club_deal_count;
        customClubMap.put("intention", custom_club_intention_count);
        customOverview.add(customClubMap);

        //居家
        Map<String, Object> customHomeMap = new HashedMap();
        String sql_custom_home_all = "select count(*) from yx_custom c left join yx_order_demand od on (c.id=od.cid) where od.service_type='居家服务' ";
        //居家全部客户
        Integer custom_home_all_count = jdbcTemplate.queryForObject(sql_custom_home_all, Integer.class);
        String sql_custom_home_deal = "select count(*) from yx_custom c left join yx_order_demand od on (c.id=od.cid) where od.service_type='居家服务' and od.work_states is not null and od.work_states<>0 ";
        //居家成交客户
        Integer custom_home_deal_count = jdbcTemplate.queryForObject(sql_custom_home_deal, Integer.class);
        customHomeMap.put("deal", custom_home_all_count);
        Integer custom_home_intention_count = custom_home_all_count - custom_home_deal_count;
        customHomeMap.put("intention", custom_home_intention_count);
        customOverview.add(customHomeMap);
        map.put("customOverview", customOverview);


        // 订单概览7
        StringBuffer sb_order = new StringBuffer();
        sb_order.append("select count(*) from yx_custom c left join yx_order_demand od on (c.id=od.cid) where od.work_states=1 ");
        String sql_club = " and od.service_type='月子会所服务' and od.starttime<?  ";
        String sql_home = " and od.service_type='居家服务' and od.starttime<? ";
        List<Map<String, Object>> orderOverview = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            Map<String, Object> orderMap = new HashMap<>();
            String seven_time = getNewEndtime(nowTime, i);
            String s_time = getNewEndtime(nowTime, i - 1);
            String new_seven_time = seven_time.substring(5).replaceAll("-", "/");
            orderMap.put("time", new_seven_time);
            StringBuffer sb_club = new StringBuffer();
            sb_club.reverse();//初始化
            sb_club.append(sb_order);
            sb_club.append(sql_club);

            Integer club_count = jdbcTemplate.queryForObject(sb_club.toString(), Integer.class, s_time);
            orderMap.put("club", club_count);

            StringBuffer sb_home = new StringBuffer();
            sb_home.reverse();//初始化
            sb_home.append(sb_order);
            sb_home.append(sql_home);
            Integer home_count = jdbcTemplate.queryForObject(sb_home.toString(), Integer.class, s_time);
            orderMap.put("home", home_count);
            orderOverview.add(orderMap);
        }
        map.put("orderOverview", orderOverview);

        return map;
    }

    @Override
    public Map<String, Object> statisticsCondition() {

        Map<String, Object> map = new HashMap<>();
//        //等级条件
//        String[] gradeString = {"N", "H", "Y1", "Y2", "Y3", "X", "P"};
//        List<String> gradeList = Arrays.asList(gradeString);
//        map.put("gradeList", gradeList);

        List<Integer> yearList = new ArrayList<>();
        //当前时间
        SimpleDateFormat time = new SimpleDateFormat("yyyy");//设置日期格式
        Integer year = Integer.valueOf(time.format(new Date()));
        //其实年份
        Integer startYear = 2020;
        Integer size = year - startYear;
        for (int i = startYear + size; i > 2019; i--) {
            yearList.add(i);
        }
        map.put("yearList", yearList);

        List<String> monthList = new ArrayList<>();
        for (int i = 1; i < 13; i++) {
            monthList.add(String.valueOf(i) + "月");
        }
        map.put("monthList", monthList);


        return map;
    }

    @Override
    public void statisticsTiming() {
        SimpleDateFormat timeS = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式

        // 一天的毫秒数
       // long daySpan = 24 * 60 * 60 * 1000;

        //10天
        long daySpan = 10 *24 * 60 * 60 * 1000;
        // 规定的每天时间02:00:00运行
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 02:00:00");
        // 首次运行时间
        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
            // 如果今天的已经过了 首次运行时间就改为明天
            if (System.currentTimeMillis() > startTime.getTime()){
                startTime = new Date(startTime.getTime() + daySpan);
            }
            Timer t = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    System.out.println("定时器执行");
                    //删除数据中心订单金额统计
                    String sql_delete_orderStatistics="delete from yx_orderStatistics";
                    int states_delete_orderStatistics=jdbcTemplate.update(sql_delete_orderStatistics);

                    //删除数据中心收益趋势某一年会所
                    String sql_delete_monthPrice="delete from yx_monthPrice";
                    int states_delete_monthPrice=jdbcTemplate.update(sql_delete_monthPrice);

                    //删除数据中心收益趋势每一天
                    String sql_delete_dayPrice="delete from yx_dayPrice";
                    int states_delete_dayPrice=jdbcTemplate.update(sql_delete_dayPrice);

                   String nowTime= timeS.format(new Date());
                   //当前年份
                   Integer year=Integer.valueOf(nowTime.substring(0, 4).toString());
                   //默认13 月份为空
                   Integer month=13;
                   //清空所有数据，第一次请求添加
                    Map<String,Object> oneList=orderAndProfitStatistics(year,month);

                   //第二次请求
                   //更新所有月份的数据，每一天数据
                      List<List> yearMonthList=new ArrayList<>();
                    for (int i = 0; i <yearMonthList.size() ; i++) {
                        Integer year1=Integer.valueOf(yearMonthList.get(i).get(0).toString());
                        Integer month1=Integer.valueOf(yearMonthList.get(i).get(1).toString());
                        Map<String,Object> twoList=orderAndProfitStatistics(year1,month1);
                    }


                }
            };
            // 以每10天执行一次
            t.schedule(task, startTime, daySpan);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




}
