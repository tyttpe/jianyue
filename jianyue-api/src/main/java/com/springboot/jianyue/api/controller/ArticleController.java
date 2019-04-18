package com.springboot.jianyue.api.controller;


import com.github.pagehelper.PageHelper;
import com.springboot.jianyue.api.entity.Article;
import com.springboot.jianyue.api.entity.Follow;
import com.springboot.jianyue.api.entity.Img;
import com.springboot.jianyue.api.entity.Like;
import com.springboot.jianyue.api.entity.vo.ArticleVO;
import com.springboot.jianyue.api.entity.vo.CommentVO;
import com.springboot.jianyue.api.service.*;
import com.springboot.jianyue.api.util.MsgConst;
import com.springboot.jianyue.api.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;
    @Resource
    private CommentService commentService;
    @Resource
    private ImgService imgService;
    @Resource
    private FollowService followService;
    @Resource
    private LikeService likeService;

    /*@GetMapping(value = "/list")
    public ResponseResult getAll() {
        List<ArticleVO> articleList = articleService.selectAll();
        return ResponseResult.success(articleList);
    }*/

    @GetMapping(value = "/list")
    public ResponseResult getAll(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = "10") int pageSize) {
        PageHelper.startPage(pageNo,pageSize);
        List<ArticleVO> articleList = articleService.selectAll();
        return ResponseResult.success(articleList);
    }


    @GetMapping(value = "/{aId}")
    public ResponseResult getArticleById(@PathVariable("aId") int aId,@RequestParam("userId") int userId) {
        ArticleVO article = articleService.getArticleById(aId);
        int toUId = article.getuId();
        Map<String, Object> map = new HashMap<>();
        Follow follow = followService.getFollow(userId, toUId);
        if (follow != null) {
            map.put("followed", MsgConst.FOLLOWED);
        } else {
            map.put("followed", MsgConst.NO_FOLLOWED);
        }
        Like like = likeService.getLike(userId,aId);
        if (like != null){
            map.put("liked",MsgConst.LIKED);
        }else {
            map.put("liked",MsgConst.NO_LIKED);
        }
        List<CommentVO> comments = commentService.selectCommentsByAId(aId);
        map.put("article", article);
        map.put("comments", comments);
        return ResponseResult.success(map);
    }

    @GetMapping(value = "/user")
    public ResponseResult getArticleById(@RequestParam("userId") int userId) {
       List<Article> article = articleService.selectArticleById(userId);
        return ResponseResult.success(article);
    }



    @PostMapping("/add")
    public ResponseResult postArticle(@RequestParam("uId") int uId,
                                      @RequestParam("title") String title,
                                      @RequestParam("content") String content) {
        Article article = new Article();
        article.setuId(uId);
        article.setTitle(title);
        article.setContent(content);
        article.setCreateTime(new Date());
        articleService.insertArticle(article);
        //新增文章后，将获取到的自增主键返回给客户端，方便图片地址的写入
        return ResponseResult.success(article.getId());
    }

    @PostMapping("/delete")
    public ResponseResult deleteArticle(@RequestParam("id") int id, @RequestParam("uId") int uId) {
        articleService.deleteArticle(id, uId);
        return ResponseResult.success();
    }


}
