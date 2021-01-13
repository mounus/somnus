package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.UserDao;
import com.example.entiy.User;
import com.example.util.BASE64;
import lombok.SneakyThrows;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {
    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public int getForId(String phone) {
        String sql = "select id from yx_user where phone =?";
        int id = this.jdbcTemplate.queryForObject(sql, Integer.class, phone);

        return id;
    }


    @Override
    public Map<String, Object> getLogin(String phone, String password) {

        Map<String, Object> map = new HashedMap();
        String sql = "select * from yx_user where phone = ?";

        try {

            User user = this.jdbcTemplate.queryForObject(sql, new Object[]{phone}, (rs, rowNum) -> {
                User temp = new User();
                temp.setId(rs.getInt("id"));
                temp.setName(rs.getString("name"));
                temp.setPassword(rs.getString("password"));
                temp.setPhone(rs.getString("phone"));
                temp.setBase(rs.getString("base"));
                temp.setPower(rs.getInt("power"));
                return temp;
            });
            Integer power = user.getPower();


            if (user.getPhone().equals(phone) && user.getPassword().equals(password)) {
                if (power == 1 || power == 3 || power == 10 || power == 30 || power == 100) {
                    String base = BASE64.encryptBASE64(user.getPassword().getBytes());
                    String sql_b = "update  yx_user set base=? where phone=?";
                    jdbcTemplate.update(sql_b, base, user.getPhone());
                    map.put("code", "1");
                    map.put("uid", String.valueOf(user.getId()));
                    map.put("base", user.getBase());
                    map.put("name", user.getName());
                    map.put("power", power);
                    map.put("phone", user.getPhone());
                    return map;//账号密码正确
                } else {
                    map.put("code", "0");
                    return map;//账号密码错误
                }

            } else {
                map.put("code", "0");
                return map;//账号密码错误
            }
        } catch (Exception e) {
            // e.printStackTrace(); // 可以选择打印信息
            map.put("code", "-1");
            return map;//手机不存在
        }
    }

    @Override
    public int saveOrUpdate(String phone, String code) {
        String sql = "select phone from yx_user where phone= ? ";
        String phone1 = this.jdbcTemplate.queryForObject(sql, String.class, phone);
        if (phone.equals(phone1)) {
            String sql1 = "update yx_user set code = ? where phone=?  ";
            return this.jdbcTemplate.update(sql, phone, code);
        } else {
            String sql2 = "insert into  yx_user (phone,code) values (?,?) ";
            return this.jdbcTemplate.update(sql, phone, code);

        }

    }


    @Override
    public int getTrue(String phone, String code) {
        String sql = "select phone from yx_user where phone= ? ";
        String sql1 = "select code from yx_user where phone= ? ";
        String phone1 = this.jdbcTemplate.queryForObject(sql, String.class, phone);
        String code1 = this.jdbcTemplate.queryForObject(sql1, String.class, code);
        if (phone.equals(phone1) && code.equals(code1)) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public Map<String, Object> getStates(String phone, String base) {

        Map<String, Object> map = new HashedMap();
        String sql = "select * from yx_user where phone = ?";
        try {
            User user = this.jdbcTemplate.queryForObject(sql, new Object[]{phone}, (rs, rowNum) -> {
                User temp = new User();
                temp.setId(rs.getInt("id"));
                temp.setName(rs.getString("name"));
                temp.setPassword(rs.getString("password"));
                temp.setPhone(rs.getString("phone"));
                temp.setBase(rs.getString("base"));
                temp.setPower(rs.getInt("power"));
                return temp;
            });
            Integer power = user.getPower();

            if (user.getPhone().equals(phone) && user.getBase().equals(base)) {

                if (power == 1 || power == 3 || power == 10 || power == 30 || power == 100) {
                    map.put("code", "1");
                    map.put("uid", String.valueOf(user.getId()));
                    map.put("phone", user.getPhone());
                    map.put("name", user.getName());
                    map.put("power", user.getPower());
                    return map;
                } else {
                    map.put("code", "0");
                    return map;//账号密码错误
                }

            } else {
                map.put("code", "0");
                return map;//账号密码错误
            }

        } catch (Exception e) {
            map.put("code", "-1");
            return map;//手机不存在
        }


    }

//    @SneakyThrows
//    @Override
//    public Map<String, Object> getAdmin(String phone, String password) {
//
//        Map<String, Object> map = new HashedMap();
//        String sql = "select * from yx_user where phone = ?";
//
//        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
//
//        String sql_count_phone = "select count(*) from yx_user where phone = ? ";
//        int count_phone = this.jdbcTemplate.queryForObject(sql_count_phone, Integer.class, phone);
//        if (count_phone > 0) {
//            //数据库有这个手机号
//            String sql_count_password = "select count(*) from yx_user where phone = ? and password=?";
//            int count_password = this.jdbcTemplate.queryForObject(sql_count_password, Integer.class, phone, password);
//            if (count_password > 0) {
//
//                String sql_user = "select id,name,password,phone,base,power from yx_user where phone =?";
//                Map<String, Object> userMap = this.jdbcTemplate.queryForMap(sql_user, phone);
//                Integer power = Integer.valueOf(userMap.get("power").toString());
//                String sql_count = "select count(*) from yx_powerField where lid=?";
//                Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class, userMap.get("id"));
//                //判断登录用户是否创建权限字段表
//                Integer states_field = 0;
//
//                if (count > 0) {
//
//                } else {
//                    StringBuffer sb = new StringBuffer();
//                    sb.append("insert into yx_powerField(lid,uid,c_name,start,c_phone,w_name,origin,address,creat_time,intention,followCount,");
//                    sb.append("sname,bname,orderSpeed,orderType,service_day,service_count,returnType,satisfied,price)");
//                    sb.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//                    if (power == 7) {
//                        //客服
//                        states_field = this.jdbcTemplate.update(sb.toString(), userMap.get("id"), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
//                                0, 1, 1, 0, 0, 0, 0, 0, 0);
//                    }
//                    if (power == 70) {
//                        //客服主管
//                        states_field = this.jdbcTemplate.update(sb.toString(), userMap.get("id"), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
//                                1, 1, 1, 0, 0, 0, 0, 0, 0);
//                    }
//                    if (power == 4) {
//                        //销售
//                        states_field = this.jdbcTemplate.update(sb.toString(), userMap.get("id"), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
//                                1, 0, 1, 1, 1, 1, 1, 1, 1);
//                    }
//                    if (power == 20) {
//                        //销售主管
//                        states_field = this.jdbcTemplate.update(sb.toString(), userMap.get("id"), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
//                                1, 1, 1, 1, 1, 1, 1, 1, 1);
//                    }
//                    if (power == 100) {
//                        //admin
//                        states_field = this.jdbcTemplate.update(sb.toString(), userMap.get("id"), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
//                                1, 1, 1, 1, 1, 1, 1, 1, 1);
//                    }
//                }
//                String user_phone = userMap.get("phone").toString();
//                String user_password = userMap.get("password").toString();
//
//                if (user_phone.equals(phone) && user_password.equals(password)) {
//                    if (power == 2 || power == 40 || power == 100 || power == 7 || power == 4) {
//                        String logintime = time.format(new Date());
//                        String sql_login = "update yx_user set logintime=? where phone=?";
//                        this.jdbcTemplate.update(sql_login, logintime, phone);
//                        String base = BASE64.encryptBASE64(user_password.getBytes());
//                        String sql_b = "update  yx_user set base=? where phone=?";
//                        jdbcTemplate.update(sql_b, base, user_phone);
//                        map.put("code", "1");
//                        map.put("uid", String.valueOf(userMap.get("id")));
//                        map.put("base", userMap.get("base"));
//                        map.put("name", userMap.get("name"));
//                        map.put("phone", user_phone);
//                        map.put("power", power);
//                        return map;//账号密码正确
//                    } else {
//                        map.put("code", "0");
//                        return map;//账号密码错误
//                    }
//                } else {
//                    map.put("code", "0");
//                    return map;//账号密码错误
//                }
//            } else {
//                map.put("code", "0");
//                return map;//账号密码错误
//            }
//
//        } else {
//            map.put("code", "-1");
//            return map;//手机不存在
//        }
//
//    }


    @Override
    public Map<String, Object> getAdmin(String phone, String password) {

        Map<String, Object> map = new HashedMap();
        String sql = "select * from yx_user where phone = ?";

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

        System.out.println("phone = " + phone);
        System.out.println("password = " + password);
        try {
            User user = this.jdbcTemplate.queryForObject(sql, new Object[]{phone}, (rs, rowNum) -> {
                User temp = new User();
                temp.setId(rs.getInt("id"));
                temp.setName(rs.getString("name"));
                temp.setPassword(rs.getString("password"));
                temp.setPhone(rs.getString("phone"));
                temp.setBase(rs.getString("base"));
                temp.setPower(rs.getInt("power"));
                return temp;
            });
            Integer power = user.getPower();
            String sql_count = "select count(*) from yx_powerField where lid=?";
            Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class, user.getId());
            //判断登录用户是否创建客户权限字段表

            Integer states_field = 0;
            if (count > 0) {

            } else {

                StringBuffer sb = new StringBuffer();
                sb.append("insert into yx_powerField(lid,cid,c_name,start,c_phone,w_name,origin,address,creat_time,intention,followCount,");
                sb.append("sname,bname,orderSpeed,orderType,service_day,service_count,returnType,satisfied,price,orderDay,truePrice)");
                sb.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                if (power == 7) {
                    //客服
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0);
                }
                if (power == 70) {
                    //客服主管
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0);
                }
                if (power == 4) {
                    //销售
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0);
                }
                if (power == 10) {
                    //经理人
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0);
                }
                if (power == 20) {
                    //销售主管
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0);
                }
                if (power == 100) {
                    //admin
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0);
                }
            }


            String sql_matorn_count = "select count(*) from yx_matornPowerField where lid=?";
            Integer matorn_count = this.jdbcTemplate.queryForObject(sql_matorn_count, Integer.class, user.getId());
            //判断登录用户是否创建月嫂权限字段表
            Integer matorn_states_field = 0;

            if (matorn_count > 0) {

            } else {
                StringBuffer sb_matorn = new StringBuffer();
                sb_matorn.append("insert into yx_matornPowerField(lid,photo,number,name,idcard,age,zodiac,household,phone,bank_card,bank_name,");
                sb_matorn.append(" grade,trueday,service_count,shelf,agent,period,origin,isPrice,l_name,creat_time)");
                sb_matorn.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                if (power == 7) {
                    //客服
                    matorn_states_field = this.jdbcTemplate.update(sb_matorn.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                            1, 1, 1, 1, 1, 0, 1, 1, 1, 1);
                }
                if (power == 70) {
                    //客服主管
                    matorn_states_field = this.jdbcTemplate.update(sb_matorn.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                            1, 1, 1, 1, 1, 0, 1, 1, 1, 1);
                }
                if (power == 4) {
                    //销售
                    matorn_states_field = this.jdbcTemplate.update(sb_matorn.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                            1, 1, 1, 1, 1, 0, 1, 1, 1, 1);
                }
                if (power == 10) {
                    //经理人主管
                    matorn_states_field = this.jdbcTemplate.update(sb_matorn.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                            1, 1, 1, 1, 1, 0, 1, 1, 1, 1);
                }
                if (power == 20) {
                    //销售主管
                    matorn_states_field = this.jdbcTemplate.update(sb_matorn.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
                            1, 1, 1, 1, 1, 0, 1, 1, 1, 1);
                }
                if (power == 100) {
                    //admin
                    matorn_states_field = this.jdbcTemplate.update(sb_matorn.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
                }
            }

            if (user.getPhone().equals(phone) && user.getPassword().equals(password)) {
                if (power == 2 ||power == 10|| power == 20||power == 40 || power == 100 || power == 7 || power == 4) {
                    String logintime = time.format(new Date());
                    String sql_login = "update yx_user set logintime=? where phone=?";
                    this.jdbcTemplate.update(sql_login, logintime, phone);
                    String base = BASE64.encryptBASE64(user.getPassword().getBytes());
                    String sql_b = "update  yx_user set base=? where phone=?";
                    jdbcTemplate.update(sql_b, base, user.getPhone());
                    map.put("code", "1");
                    map.put("uid", String.valueOf(user.getId()));
                    map.put("base", user.getBase());
                    map.put("name", user.getName());
                    map.put("phone", user.getPhone());
                    map.put("power", user.getPower());
                    return map;//账号密码正确
                } else {
                    map.put("code", "0");
                    return map;//账号密码错误
                }

            } else {
                map.put("code", "0");
                return map;//账号密码错误
            }
        } catch (Exception e) {
            // e.printStackTrace(); // 可以选择打印信息
            map.put("code", "-1");
            return map;//手机不存在
        }
    }


    @Override
    public Map<String, Object> getAdminStates(String phone, String base) {
        Map<String, Object> map = new HashMap<String, Object>();
        String sql = "select * from yx_user where phone = ?";

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        try {
            User user = this.jdbcTemplate.queryForObject(sql, new Object[]{phone}, (rs, rowNum) -> {
                User temp = new User();
                temp.setId(rs.getInt("id"));
                temp.setName(rs.getString("name"));
                temp.setPassword(rs.getString("password"));
                temp.setPhone(rs.getString("phone"));
                temp.setBase(rs.getString("base"));
                temp.setPower(rs.getInt("power"));
                return temp;
            });
            Integer power = user.getPower();
            String sql_count = "select count(*) from yx_powerField where lid=?";
            Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class, user.getId());
            //判断登录用户是否创建权限字段表
            Integer states_field = 0;
            if (count > 0) {

            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("insert into yx_powerField(lid,cid,c_name,start,c_phone,w_name,origin,address,creat_time,intention,followCount,");
                sb.append("sname,bname,orderSpeed,orderType,service_day,service_count,returnType,satisfied,price,orderDay,truePrice)");
                sb.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                if (power == 7) {
                    //客服
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1);
                }
                if (power == 70) {
                    //客服主管
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 0, 0, 0, 0, 0, 1, 1);
                }
                if (power == 4) {
                    //销售
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1);
                }
                if (power == 10) {
                    //经理人主管
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
                }
                if (power == 20) {
                    //销售主管
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
                }
                if (power == 100) {
                    //admin
                    states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
                }
            }

            String sql_matorn_count = "select count(*) from yx_matornPowerField where lid=?";
            Integer matorn_count = this.jdbcTemplate.queryForObject(sql_matorn_count, Integer.class, user.getId());
            //判断登录用户是否创建月嫂权限字段表
            Integer matorn_states_field = 0;

            if (matorn_count > 0) {

            } else {
                StringBuffer sb = new StringBuffer();
                sb.append("insert into yx_matornPowerField(lid,photo,number,idcard,age,zodiac,household,phone,bank_card,bank_name,grade,");
                sb.append(" trueday,service_count,shelf,agent,origin,isPrice)");
                sb.append("value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                if (power == 7) {
                    //客服
                    matorn_states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1,
                            1, 1, 1, 1, 1, 1);
                }
                if (power == 70) {
                    //客服主管
                    matorn_states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1,
                            1, 1, 1, 1, 1, 1);
                }
                if (power == 4) {
                    //销售
                    matorn_states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1,
                            1, 1, 1, 1, 1, 1);
                }
                if (power == 10) {
                    //经理人主管
                    matorn_states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1,
                            1, 1, 1, 1, 1, 1);
                }
                if (power == 20) {
                    //销售主管
                    matorn_states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1,
                            1, 1, 1, 1, 1, 1);
                }
                if (power == 100) {
                    //admin
                    matorn_states_field = this.jdbcTemplate.update(sb.toString(), user.getId(), 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                            1, 1, 1, 1, 1, 1);
                }
            }


            if (user.getPhone().equals(phone) && user.getBase().equals(base)) {
                if (power == 2 ||power==10||power==20|| power == 40 || power == 100 || power == 7) {
                    String logintime = time.format(new Date());
                    String sql_login = "update yx_user set logintime=? where phone=?";
                    this.jdbcTemplate.update(sql_login, logintime, phone);
                    map.put("code", "1");
                    map.put("uid", String.valueOf(user.getId()));
                    map.put("phone", user.getPhone());
                    map.put("name", user.getName());
                    map.put("power", user.getPower());
                    return map;
                } else {
                    map.put("code", "0");
                    return map;//账号密码错误
                }

            } else {
                map.put("code", "0");
                return map;//账号密码错误
            }

        } catch (Exception e) {
            map.put("code", "-1");
            return map;//手机不存在
        }

    }


    @Override
    public int getExitTime(String phone) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String exittime = time.format(new Date());
        String sql_exit = "update yx_user set exittime=? where phone=?";
        return this.jdbcTemplate.update(sql_exit, exittime, phone);

    }

    @Override
    public Map<String, Object> getMessage(String uid) {
        Map<String, Object> map = new HashMap<String, Object>();

        String sql = "select longintime,exittime from yx_user where id = ?";
        User user = this.jdbcTemplate.queryForObject(sql, new Object[]{uid}, (rs, rowNum) -> {
            User temp = new User();
            temp.setLogintime(rs.getString("logintime"));
            temp.setExittime(rs.getString("exittime"));
            return temp;
        });

        String sql_count = "select count(*) from yx_bussiness where  creat_time  between ? and ?";
        Integer count = this.jdbcTemplate.update(sql_count, Integer.class, user.getExittime(), user.getLogintime());
        map.put("count", count);

        String sql_mid = "select mid from yx_bussiness where creat_time  between ? and ?";
        List<Integer> midlist = (List) this.jdbcTemplate.queryForList(sql_mid, user.getExittime(), user.getLogintime());
        map.put("midlist", midlist);

        return map;
    }


    @Override
    public Map<String, Object> getBussiness(String phone, String password) {
        Map<String, Object> map = new HashedMap();
        String sql = "select * from yx_user where phone = ?";

        try {

            User user = this.jdbcTemplate.queryForObject(sql, new Object[]{phone}, (rs, rowNum) -> {
                User temp = new User();
                temp.setId(rs.getInt("id"));
                temp.setName(rs.getString("name"));
                temp.setPassword(rs.getString("password"));
                temp.setPhone(rs.getString("phone"));
                temp.setBase(rs.getString("base"));
                temp.setPower(rs.getInt("power"));
                temp.setPost(rs.getString("post"));
                temp.setPhoto(rs.getString("photo"));
                return temp;
            });
            Integer power = user.getPower();

            if (user.getPhone().equals(phone) && user.getPassword().equals(password)) {
                if (power == 4 || power == 20 || power == 100) {
                    String base = BASE64.encryptBASE64(user.getPassword().getBytes());
                    String sql_b = "update  yx_user set base=? where phone=?";
                    jdbcTemplate.update(sql_b, base, user.getPhone());
                    map.put("code", "1");
                    map.put("bid", String.valueOf(user.getId()));
                    map.put("base", user.getBase());
                    map.put("name", user.getName());
                    map.put("post", user.getPost());
                    map.put("photo", user.getPhoto());
                    map.put("power", user.getPower());
                    map.put("phone", user.getPhone());
                    return map;//账号密码正确
                } else {
                    map.put("code", "0");
                    return map;//账号密码错误
                }

            } else {
                map.put("code", "0");
                return map;//账号密码错误
            }
        } catch (Exception e) {
            // e.printStackTrace(); // 可以选择打印信息
            map.put("code", "-1");
            return map;//手机不存在
        }
    }

    @Override
    public Map<String, Object> getBussinessStates(String phone, String base) {
        Map<String, Object> map = new HashMap<String, Object>();
        String sql = "select * from yx_user where phone = ?";

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        try {
            User user = this.jdbcTemplate.queryForObject(sql, new Object[]{phone}, (rs, rowNum) -> {
                User temp = new User();
                temp.setId(rs.getInt("id"));
                temp.setName(rs.getString("name"));
                temp.setPassword(rs.getString("password"));
                temp.setPhone(rs.getString("phone"));
                temp.setBase(rs.getString("base"));
                temp.setPower(rs.getInt("power"));
                temp.setPost(rs.getString("post"));
                temp.setPhoto(rs.getString("photo"));
                return temp;
            });
            Integer power = user.getPower();

            if (user.getPhone().equals(phone) && user.getBase().equals(base)) {
                if (power == 4 || power == 20 || power == 100) {
                    String logintime = time.format(new Date());
                    String sql_login = "update yx_user set logintime=? where phone=?";
                    this.jdbcTemplate.update(sql_login, logintime, phone);
                    map.put("code", "1");
                    map.put("bid", String.valueOf(user.getId()));
                    map.put("phone", user.getPhone());
                    map.put("name", user.getName());
                    map.put("post", user.getPost());
                    map.put("photo", user.getPhoto());
                    return map;
                } else {
                    map.put("code", "0");
                    return map;//账号密码错误
                }

            } else {
                map.put("code", "0");
                return map;//账号密码错误
            }

        } catch (Exception e) {
            map.put("code", "-1");
            return map;//手机不存在
        }
    }


    @Override
    public Map<String, Object> getPost(String phone, String password) {
        Map<String, Object> map = new HashedMap();
        String sql = "select * from yx_user where phone = ?";

        try {

            User user = this.jdbcTemplate.queryForObject(sql, new Object[]{phone}, (rs, rowNum) -> {
                User temp = new User();
                temp.setId(rs.getInt("id"));
                temp.setName(rs.getString("name"));
                temp.setPassword(rs.getString("password"));
                temp.setPhone(rs.getString("phone"));
                temp.setBase(rs.getString("base"));
                temp.setPower(rs.getInt("power"));
                temp.setPost(rs.getString("post"));
                temp.setPhoto(rs.getString("photo"));
                return temp;
            });
            Integer power = user.getPower();


            if (user.getPhone().equals(phone) && user.getPassword().equals(password) && power != 1 && power != 2 && power != 3 && power != 10 && power != 4 && power != 5 && power != 20 && power != 30) {

                if (power == 6 || power == 100) {
                    String base = BASE64.encryptBASE64(user.getPassword().getBytes());
                    String sql_b = "update  yx_user set base=? where phone=?";
                    jdbcTemplate.update(sql_b, base, user.getPhone());
                    map.put("code", "1");
                    map.put("pid", String.valueOf(user.getId()));
                    map.put("base", user.getBase());
                    map.put("name", user.getName());
                    map.put("post", user.getPost());
                    map.put("photo", user.getPhoto());
                    map.put("phone", user.getPhone());
                    map.put("power", power);
                    return map;//账号密码正确
                } else {
                    map.put("code", "0");
                    return map;//账号密码错误
                }

            } else {
                map.put("code", "0");
                return map;//账号密码错误
            }
        } catch (Exception e) {
            // e.printStackTrace(); // 可以选择打印信息
            map.put("code", "-1");
            return map;//手机不存在
        }
    }

    @Override
    public Map<String, Object> getPostStates(String phone, String base) {
        Map<String, Object> map = new HashMap<String, Object>();
        String sql = "select * from yx_user where phone = ?";

        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        try {
            User user = this.jdbcTemplate.queryForObject(sql, new Object[]{phone}, (rs, rowNum) -> {
                User temp = new User();
                temp.setId(rs.getInt("id"));
                temp.setName(rs.getString("name"));
                temp.setPassword(rs.getString("password"));
                temp.setPhone(rs.getString("phone"));
                temp.setBase(rs.getString("base"));
                temp.setPower(rs.getInt("power"));
                temp.setPost(rs.getString("post"));
                temp.setPhoto(rs.getString("photo"));
                return temp;
            });
            Integer power = user.getPower();

            if (user.getPhone().equals(phone) && user.getBase().equals(base) && power != 1 && power != 2 && power != 3 && power != 10 && power != 4 && power != 5 && power != 20 && power != 30) {
                if (power == 6 || power == 100) {
                    String logintime = time.format(new Date());
                    String sql_login = "update yx_user set logintime=? where phone=?";
                    this.jdbcTemplate.update(sql_login, logintime, phone);
                    map.put("code", "1");
                    map.put("pid", String.valueOf(user.getId()));
                    map.put("phone", user.getPhone());
                    map.put("name", user.getName());
                    map.put("post", user.getPost());
                    map.put("photo", user.getPhoto());
                    return map;
                } else {
                    map.put("code", "0");
                    return map;//账号密码错误
                }

            } else {
                map.put("code", "0");
                return map;//账号密码错误
            }

        } catch (Exception e) {
            map.put("code", "-1");
            return map;//手机不存在
        }
    }


    @Override
    public int addUser(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String name = jsonObject.getString("name");
        String phone = jsonObject.getString("phone");
        String password = jsonObject.getString("password");


        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_matorn (phone,password,phone)");
        sb.append("values(?,?,?)");

        int states = this.jdbcTemplate.update(sb.toString(), phone, password, phone);

        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }


}
