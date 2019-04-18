package com.springboot.jianyue.api.controller;

import com.springboot.jianyue.api.entity.Like;
import com.springboot.jianyue.api.entity.vo.LikeVO;
import com.springboot.jianyue.api.service.LikeService;
import com.springboot.jianyue.api.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value = "/api/like")
public class LikeController {
    @Resource
    private LikeService likeService;

    @PostMapping("/add")
    public ResponseResult likeUser(@RequestParam("fromLike") int fromLike, @RequestParam("toLike") int toLike) {
        Like like = new Like();
        like.setFromLike(fromLike);
        like.setToLike(toLike);
        likeService.insertLike(like);
        return ResponseResult.success();
    }

    @PostMapping("/cancel")
    public ResponseResult cancelLike(@RequestParam("fromLike") int fromLike, @RequestParam("toLike") int toLike) {
        likeService.deleteLike(fromLike, toLike);
        return ResponseResult.success();
    }

    @GetMapping(value = "/list")
    public ResponseResult getLikesByUId(@RequestParam("fromLike") int fromLike) {
        List<LikeVO> likeVOS = likeService.getLikesByUId(fromLike);
        return ResponseResult.success(likeVOS);
    }

}
