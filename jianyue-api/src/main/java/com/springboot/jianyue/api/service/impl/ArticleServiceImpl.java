package com.springboot.jianyue.api.service.impl;

import com.springboot.jianyue.api.entity.Article;
import com.springboot.jianyue.api.entity.vo.ArticleVO;
import com.springboot.jianyue.api.mapper.ArticleMapper;
import com.springboot.jianyue.api.mapper.CommentMapper;
import com.springboot.jianyue.api.mapper.ImgMapper;
import com.springboot.jianyue.api.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ImgMapper imgMapper;
    @Resource
    private CommentMapper commentMapper;

    @Override
    public List<ArticleVO> selectAll() {
        List<ArticleVO> articles = articleMapper.selectAll();
        for (ArticleVO articleVO:articles){
            articleVO.setImgs(imgMapper.selectImgsByAId(articleVO.getId()));
            articleVO.setComments(commentMapper.selectCommentsByAId(articleVO.getId()).size());
        }
        return articles;
    }

    @Override
    public ArticleVO getArticleById(int aId) {
        return articleMapper.getArticleById(aId);
    }

    @Override
    public void insertArticle(Article article) {
        articleMapper.insertArticle(article);
    }

    @Override
    public List<Article> selectArticleById(int uId) {
        return articleMapper.selectArticleById(uId);
    }

    @Override
    public void deleteArticle(int id, int uId) {
        articleMapper.deleteArticle(id,uId);
    }
}
