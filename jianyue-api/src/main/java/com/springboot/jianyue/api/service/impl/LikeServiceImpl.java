package com.springboot.jianyue.api.service.impl;

import com.springboot.jianyue.api.entity.Like;
import com.springboot.jianyue.api.entity.vo.LikeVO;
import com.springboot.jianyue.api.mapper.LikeMapper;
import com.springboot.jianyue.api.service.LikeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class LikeServiceImpl implements LikeService {
    @Resource
    private LikeMapper likeMapper;

    @Override
    public Like getLike(int fromLike, int toLike) {
        return likeMapper.getLike(fromLike, toLike);
    }

    @Override
    public List<LikeVO> getLikesByUId(int fromLike) {
        return likeMapper.getLikesByUId(fromLike);
    }

    @Override
    public void insertLike(Like like) {
         likeMapper.insertLike(like);
    }

    @Override
    public void deleteLike(int fromLike, int toLike) {
          likeMapper.deleteLike(fromLike, toLike);
    }
}
