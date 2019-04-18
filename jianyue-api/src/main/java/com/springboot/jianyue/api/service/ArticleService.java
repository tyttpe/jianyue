package com.springboot.jianyue.api.service;

import com.springboot.jianyue.api.entity.Article;
import com.springboot.jianyue.api.entity.vo.ArticleVO;

import java.util.List;

public interface ArticleService {
     //查询所有文章
    List<ArticleVO> selectAll();

    //根据文章id查询所有文章
    ArticleVO getArticleById(int aId);

    void insertArticle(Article article);

    List<Article> selectArticleById(int uId);

    void deleteArticle (int id,int uId);

}
