package com.example.dao.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.TestDao;
import com.example.entiy.Matorn;

import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.example.util.AddressUtil.getNativePlace;
import static com.example.util.HolidyUtil.getNewEndtime;
import static com.example.util.MonthUtil.*;
import static com.example.util.NumberUtil.getNumber;
import static com.example.util.Year.*;

@Repository
public class TestDaoImpl implements TestDao {

}
