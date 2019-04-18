package com.springboot.jianyue.api.mapper;

import com.springboot.jianyue.api.entity.Like;
import com.springboot.jianyue.api.entity.vo.LikeVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface LikeMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "fromLike", column = "from_like"),
            @Result(property = "toLike", column = "to_like")
    })
    @Select("SELECT * FROM t_like WHERE from_like = #{fromLike} AND to_like = #{toLike} ")
    Like getLike(@Param("fromLike") int fromLike, @Param("toLike") int toLike);

    @Results({
            @Result(property = "toLike", column = "to_like"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "title", column = "title")
    })
    @Select("SELECT a.to_like,b.title FROM t_like a LEFT JOIN t_article b ON a.to_like = b.id WHERE a.from_like = #{fromLike}  ")
    List<LikeVO> getLikesByUId(int fromLike);

    @Insert("INSERT INTO t_like (from_like,to_like) VALUES (#{fromLike},#{toLike}) ")
    void insertLike(Like like);

    @Delete("DELETE  FROM t_like WHERE from_like = #{fromLike} AND to_like = #{toLike} ")
    void deleteLike(@Param("fromLike") int fromLike, @Param("toLike") int toLIke);
}
