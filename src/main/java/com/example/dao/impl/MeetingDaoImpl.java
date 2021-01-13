package com.example.dao.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.MeetingDao;
import com.example.entiy.Article;
import com.example.entiy.Matorn;
import com.example.entiy.Picture;
import com.example.entiy.UsersEntity;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Type;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.example.util.ArticleUtill.articleTypeLabel;
import static com.example.util.PageUtil.getAllPage;

@Repository
public class MeetingDaoImpl implements MeetingDao {

    @Autowired(required = false)
    private JdbcTemplate jdbcTemplate;

    @Override
    public int savePicture(Picture picture) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into yx_picture(name,page,picture_url,url,upload_time)");
        sb.append(" value(?,?,?,?,?)");
        //时间格式
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String upload_time = time1.format(new Date());
        int states = this.jdbcTemplate.update(sb.toString(), picture.getName(),picture.getPage(), picture.getPicture_url(), picture.getUrl(), upload_time);
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public int saveOrUpdateArticle(Article article) {
        //时间格式
        SimpleDateFormat time1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String upload_time = time1.format(new Date());
        int states = 0;

        if (article.getId() == 0) {
            //添加文章
            StringBuffer sb = new StringBuffer();
            sb.append(" insert into yx_article(title,page,type,label,cover,url,upload_time)");
            sb.append("value(?,?,?,?,?,?,?)");
            states = this.jdbcTemplate.update(sb.toString(), article.getTitle(), article.getPage(), article.getType(), article.getLabel(),
                    article.getCover(), article.getUrl(), upload_time);

        } else {
            //修改文章
            String sql = "update yx_article set title=?,page=?,type=?,label=?,cover=?,url=?,upload_time=? where id=?";
            states = this.jdbcTemplate.update(sql, article.getTitle(), article.getPage(), article.getType(), article.getLabel(), article.getCover(),
                    article.getUrl(), upload_time, article.getId());

        }
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public Article getOneArticle(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer id = jsonObject.getInteger("id");
        String sql = "select * from yx_article where id=?";
        List<Article> list = jdbcTemplate.query(sql, new Object[]{id}, new BeanPropertyRowMapper(Article.class));
        if (list != null && list.size() > 0) {
            Article article = list.get(0);
            return article;
        } else {
            return null;
        }
    }

    @SneakyThrows
    @Override
    public Map<String, Object> getAllArticleOrPicture(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer start = jsonObject.getInteger("start");
        String somnus = jsonObject.getString("somnus");
        Map<String, Object> map = new HashMap<String, Object>();

        Map<String, Object> page = new HashMap<String, Object>();
        StringBuffer sb = new StringBuffer();
        if (somnus.equals("article")) {
            String sql_count = "select count(*) from yx_article";
            Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class);
            map.put("count", count);
            page = getAllPage(count, start);
            String sql_page = page.get("sql_page").toString();
            sb.append("select * from yx_article order by upload_time desc");
            sb.append(sql_page);
            map.put("page", page.get("page"));
            map.put("num", page.get("num"));
            List list = this.jdbcTemplate.query(sb.toString(), new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int index) throws SQLException {
                    Map<String, Object> mp = new HashMap<String, Object>();
                    mp.put("id", rs.getInt("id"));
                    mp.put("title", rs.getString("title"));
                    Integer page = rs.getInt("page");
                    Integer type = rs.getInt("type");
                    Integer label = rs.getInt("label");

                    Map<String, Object> articleMap=articleTypeLabel(page,type,label);

                     mp.put("page",articleMap.get("page"));
                    mp.put("type",articleMap.get("type"));
                    mp.put("label",articleMap.get("label"));
                    mp.put("cover", rs.getString("cover"));
                    mp.put("url", rs.getString("url"));
                    mp.put("upload_time", rs.getString("upload_time"));
                    return mp;
                }
            });
            map.put("list", list);
        }
        if (somnus.equals("picture")) {
            String sql_count = "select count(*) from yx_picture";
            Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class);
            map.put("count", count);
            page = getAllPage(count, start);
            String sql_page = page.get("sql_page").toString();
            sb.append("select * from yx_picture order by upload_time desc");
            sb.append(sql_page);
            map.put("page", page.get("page"));
            map.put("num", page.get("num"));

            List<UsersEntity> list = jdbcTemplate.query(sb.toString(), new Object[]{}, new BeanPropertyRowMapper(Picture.class));
            map.put("list", list);
        }

        return map;
    }

    @Override
    public List<Map<String, Object>> articleType(String json) {
        List<Map<String, Object>> paList = new ArrayList<>();

        JSONObject jsonObject = JSON.parseObject(json);
        String somnus = jsonObject.getString("somnus");

        String sql_page = "select page,ptype from yx_article_type where  ptype is not null";
        StringBuffer sb = new StringBuffer();
        sb.append(" select title,type from yx_article_type where  ttype=? ");
        if (somnus.equals("article")) {
            sb.append(" and type<>0 ");
        }
        if (somnus.equals("list")) {

        }
        sb.append(" order by type");
        String sql_count = "select count(*) from yx_article_type where ltype=?";
        String sql_label= "select label,latype from yx_article_type where ltype=?";
        List<Map<String, Object>> pageList = this.jdbcTemplate.queryForList(sql_page.toString());
        //页面类型
        for (int i = 0; i < pageList.size(); i++) {
            Map<String, Object> paMap = new HashMap<>();
            List<Map<String, Object>> titleList = this.jdbcTemplate.queryForList(sb.toString(), pageList.get(i).get("ptype"));
            //标题类型
            List<Map<String, Object>> tiList = new ArrayList<>();
            for (int j = 0; j < titleList.size(); j++) {
                Integer count = this.jdbcTemplate.queryForObject(sql_count, Integer.class, titleList.get(j).get("type"));
                List<Map<String, Object>> labelList = this.jdbcTemplate.queryForList(sql_label, titleList.get(j).get("type"));
                Map<String, Object> tiMap = new HashMap<>();
                if (count > 0) {
                    List<Map<String, Object>> laList = new ArrayList<>();
                    for (int k = 0; k < labelList.size(); k++) {
                        Map<String, Object> labelMap = new HashMap<>();
                        labelMap.put("name", labelList.get(k).get("label"));
                        labelMap.put("type", labelList.get(k).get("latype"));
                        laList.add(labelMap);
                    }
                    tiMap.put("name", titleList.get(j).get("title"));
                    tiMap.put("children", laList);
                    tiMap.put("type", titleList.get(j).get("type"));
                    tiList.add(tiMap);
                } else {
                    tiMap.put("name", titleList.get(j).get("title"));
                    List<Map<String, Object>> laList = new ArrayList<>();
                    tiMap.put("children", laList);
                    tiMap.put("type", titleList.get(j).get("type"));
                    tiList.add(tiMap);
                }

            }

            paMap.put("name", pageList.get(i).get("page"));
            paMap.put("children", tiList);
            paMap.put("type", pageList.get(i).get("ptype"));
            paList.add(paMap);
        }

        return paList;
    }

    @Override
    public int deleteArticleOrPicture(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        String somnus = jsonObject.getString("somnus");
        Integer id = jsonObject.getInteger("id");
        int states = 0;
        if (somnus.equals("article")) {
            //文章
            String sql = "delete from yx_article where id=?";
            states = this.jdbcTemplate.update(sql, id);
        }
        if (somnus.equals("picture")) {
            //图片
            String sql = "delete from yx_picture where id=?";
            states = this.jdbcTemplate.update(sql, id);
        }
        if (states > 0) {
            return 1;
        } else {
            return 0;
        }

    }

    @Override
    public List<Map<String, Object>> articleList(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer page = jsonObject.getInteger("page");
        Integer type = jsonObject.getInteger("type");
        Integer label = jsonObject.getInteger("label");
        Integer start=jsonObject.getInteger("start");
        StringBuffer sb = new StringBuffer();
        sb.append("select * from yx_article where page=?  ");
        List<Object> queryList = new ArrayList<Object>();
        queryList.add(page);
       if (type!=0){
           sb.append(" and type=? ");
           queryList.add(type);
           if (label !=0) {
               sb.append(" and label=? ");
               queryList.add(label);
           } else {

           }
       }else {

       }

        sb.append(" order by upload_time desc limit ?,3 ");
       if (start==1){
           start=0;
       }else {
           start=(start-1)*3;
       }
        queryList.add(start);

        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sb.toString(), queryList.toArray());
        return list;
    }

    @Override
    public List<Map<String, Object>> pictureList(String json) {
        JSONObject jsonObject = JSON.parseObject(json);
        Integer page = jsonObject.getInteger("page");
        String sql = "select picture_url,url from yx_picture where page=? order by upload_time desc ";
        List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql,page);
        return list;
    }
}
