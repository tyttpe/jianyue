package com.springboot.jianyue.api.controller;

import com.springboot.jianyue.api.entity.Comment;
import com.springboot.jianyue.api.entity.Follow;
import com.springboot.jianyue.api.entity.vo.ArticleVO;
import com.springboot.jianyue.api.entity.vo.CommentVO;
import com.springboot.jianyue.api.service.CommentService;
import com.springboot.jianyue.api.util.MsgConst;
import com.springboot.jianyue.api.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    public ResponseResult addComment(@RequestParam("aId") int aId, @RequestParam("uId") int uId, @RequestParam("content") String content) {
        Comment comment = new Comment();
        comment.setaId(aId);
        comment.setuId(uId);
        comment.setContent(content);
        comment.setCommentTime(new Date());
        commentService.addComment(comment);
        return ResponseResult.success();
    }

}
