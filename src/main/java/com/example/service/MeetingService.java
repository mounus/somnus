package com.example.service;

import com.example.entiy.Article;
import com.example.entiy.Picture;

import java.util.List;
import java.util.Map;

public interface MeetingService {
    /**
     * @description: 添加头部图片
     * @param:
     * @return: void
     */
    int savePicture(Picture picture);
    /**
     * @description: 添加/修改文章
     * @param:
     * @return: void
     */
    int saveOrUpdateArticle(Article article);
    /**
     * @description: 查询单个文章内容
     * @param:
     * @return: void
     */
    Article getOneArticle(String json);
    /**
     * @description: 后台全部查询文章内容
     * @param:
     * @return: void
     */
    Map<String,Object> getAllArticleOrPicture(String json);
    /**
     * @description: 文章的分类及其标签
     * @param:
     * @return: void
     */
    List<Map<String,Object>> articleType(String json);
    /**
     * @description: 删除文章/图片
     * @param:
     * @return: void
     */
    int deleteArticleOrPicture(String json);
    /**
     * @description: 文章列表
     * @param:
     * @return: void
     */
    List<Map<String,Object>> articleList(String json);
    /**
     * @description: 图片轮播图
     * @param:
     * @return: void
     */
    List<Map<String,Object>> pictureList(String json);
}
