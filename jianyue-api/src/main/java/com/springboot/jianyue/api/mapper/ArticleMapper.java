package com.springboot.jianyue.api.mapper;

import com.springboot.jianyue.api.entity.Article;
import com.springboot.jianyue.api.entity.vo.ArticleVO;
import org.apache.ibatis.annotations.*;

import java.util.List;


public interface ArticleMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uId", column = "u_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "avatar", column = "avatar")
    })
    @Select("SELECT t_article.*,t_user.`nickname`,t_user.`avatar` " +
            "FROM t_article LEFT JOIN t_user ON t_article.u_id = t_user.id ORDER BY create_time DESC")
    List<ArticleVO> selectAll();


    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uId", column = "u_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "avatar", column = "avatar")
    })
    @Select(" SELECT t_article.*,t_user.nickname,t_user.avatar " +
            " FROM t_article LEFT JOIN t_user ON t_article.u_id = t_user.id " +
            " WHERE t_article.id = #{id} ")
    ArticleVO getArticleById(int aId);

    @Delete("DELETE  FROM t_article WHERE id = #{id} AND u_id = #{uId}")
    void deleteArticle(@Param("id") int id, @Param("uId") int uId);

    @Insert("INSERT INTO t_article (u_id,title,content,create_time) VALUES (#{uId},#{title},#{content},#{createTime}) ")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    void insertArticle(Article article);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "uId", column = "u_id"),
            @Result(property = "title", column = "title"),
            @Result(property = "content", column = "content"),
            @Result(property = "createTime", column = "create_time")
    })
    @Select("SELECT * FROM t_article WHERE u_id = #{uId}")
    List<Article> selectArticleById(int uId);

    @Select("SELECT * FROM t_article WHERE #{newpage},#{count}")
    List<Article> selectArticleByPage(@Param("id") int id,@Param("uId") int count);
}
