package com.example.service.impl;

import com.example.dao.MeetingDao;
import com.example.entiy.Article;
import com.example.entiy.Picture;
import com.example.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MeetingServiceImpl implements MeetingService {
    @Autowired(required = false)
    private MeetingDao meetingDao;

    @Override
    public int savePicture(Picture picture) {
        return meetingDao.savePicture(picture);
    }

    @Override
    public int saveOrUpdateArticle(Article article) {
        return meetingDao.saveOrUpdateArticle(article);
    }

    @Override
    public   Article getOneArticle (String json) {
        return meetingDao.getOneArticle(json);
    }

    @Override
    public Map<String, Object> getAllArticleOrPicture(String json) {
        return meetingDao.getAllArticleOrPicture(json);
    }

    @Override
    public List<Map<String, Object>> articleType(String json) {

        return meetingDao.articleType(json);
    }

    @Override
    public int deleteArticleOrPicture(String json) {
        return meetingDao.deleteArticleOrPicture(json);
    }

    @Override
    public List<Map<String, Object>> articleList(String json) {
        return meetingDao.articleList(json);
    }

    @Override
    public List<Map<String, Object>> pictureList(String json) {
        return meetingDao.pictureList(json);
    }
}
