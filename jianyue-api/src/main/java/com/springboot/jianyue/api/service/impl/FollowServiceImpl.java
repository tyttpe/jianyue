package com.springboot.jianyue.api.service.impl;

import com.springboot.jianyue.api.entity.Follow;
import com.springboot.jianyue.api.entity.vo.FollowVO;
import com.springboot.jianyue.api.mapper.FollowMapper;
import com.springboot.jianyue.api.service.FollowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class FollowServiceImpl implements FollowService {
    @Resource
    private FollowMapper followMapper;

    @Override
    public Follow getFollow(int fromUId, int toUId) {
        return followMapper.getFollow(fromUId, toUId);
    }

    @Override
    public List<FollowVO> getFollowsByUId(int fromUId) {
        return followMapper.getFollowsByUId(fromUId);
    }

    @Override
    public FollowVO getFollowsByUId2(int fromUId) {
        return followMapper.getFollowsByUId2(fromUId);
    }

    @Override
    public void insertFollow(Follow follow) {
         followMapper.insertFollow(follow);
    }

    @Override
    public void deleteFollow(int fromUId, int toUId) {
          followMapper.deleteFollow(fromUId,toUId);
    }
}
