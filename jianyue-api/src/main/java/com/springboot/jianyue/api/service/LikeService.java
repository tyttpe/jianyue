package com.springboot.jianyue.api.service;

import com.springboot.jianyue.api.entity.Like;
import com.springboot.jianyue.api.entity.vo.LikeVO;

import java.util.List;

public interface LikeService {
    Like getLike(int fromLike, int toLike);

    List<LikeVO> getLikesByUId(int fromLike);

    void insertLike(Like like);

    void deleteLike(int fromLike, int toLike);
}
