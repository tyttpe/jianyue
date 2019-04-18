package com.springboot.jianyue.api.mapper;

import com.springboot.jianyue.api.entity.User;
import org.apache.ibatis.annotations.*;

public interface UserMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "mobile", column = "mobile"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "status", column = "status"),
            @Result(property = "regtime", column = "regtime"),
            @Result(property = "token", column = "token")
    })
    @Select("SELECT * FROM t_user WHERE mobile = #{mobile}")
    User getUserByMobile(String mobile);

    @Update("UPDATE t_user SET password = #{password}, nickname = #{nickname}," +
            "avatar = #{avatar},status = #{status},token = #{token} WHERE id = #{id}")
    void update(User user);

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "mobile", column = "mobile"),
            @Result(property = "password", column = "password"),
            @Result(property = "nickname", column = "nickname"),
            @Result(property = "avatar", column = "avatar"),
            @Result(property = "status", column = "status"),
            @Result(property = "regtime", column = "regtime"),
            @Result(property = "token", column = "token")
    })
    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User getUserById(int id);

    @Insert("INSERT INTO t_user(mobile,password,nickname,avatar,regtime,status)" +
            " VALUES(#{mobile}, #{password}, #{nickname},#{avatar},#{regtime},#{status}) ")
    void insert(User user);

    @Insert("INSERT INTO t_user(mobile,password,nickname) VALUES(#{mobile}, #{password}, #{nickname})")
    int insert2(@Param("mobile") String mobile, @Param("password") String password, @Param("nickname") String nickname);

}
