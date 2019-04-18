package com.springboot.jianyue.api.controller;

import com.springboot.jianyue.api.entity.Follow;
import com.springboot.jianyue.api.entity.vo.FollowVO;
import com.springboot.jianyue.api.service.FollowService;
import com.springboot.jianyue.api.util.MsgConst;
import com.springboot.jianyue.api.util.ResponseResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/follow")
public class FollowController {
    @Resource
    private FollowService followService;


    @PostMapping("/add")
    public ResponseResult followUser(@RequestParam("fromUId") int fromUId, @RequestParam("toUId") int toUId) {
        Follow follow = new Follow();
        follow.setFromUId(fromUId);
        follow.setToUId(toUId);
        followService.insertFollow(follow);
        return ResponseResult.success();
    }

    @PostMapping("/cancel")
    public ResponseResult cancelFollow(@RequestParam("fromUId") int fromUId, @RequestParam("toUId") int toUId) {
        followService.deleteFollow(fromUId, toUId);
        return ResponseResult.success();
    }

    @GetMapping(value = "/list")
    public ResponseResult getFollowsByUId(@RequestParam("fromUId") int fromUId) {
        /*FollowVO followVO = followService.getFollowsByUId2(fromUId);
        List<FollowVO> followVOS = followService.getFollowsByUId(fromUId);
        int toUId = followVO.getToUId();
        Map<String, Object> map = new HashMap<>();
        Follow follow = followService.getFollow(fromUId, toUId);
        if (follow != null) {
            map.put("followed", MsgConst.FOLLOWED);
        } else {
            map.put("followed", MsgConst.NO_FOLLOWED);
        }
        map.put("followvo",followVOS);*/
        List<FollowVO> followVOS = followService.getFollowsByUId(fromUId);
        return ResponseResult.success(followVOS);
    }
}
