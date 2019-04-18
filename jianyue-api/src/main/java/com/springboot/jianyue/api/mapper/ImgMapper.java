package com.springboot.jianyue.api.mapper;

import com.springboot.jianyue.api.entity.Img;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ImgMapper {
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "aId", column = "a_id"),
            @Result(property = "imgUrl", column = "img_url")
    })
    @Select("SELECT * FROM t_img WHERE a_id = #{a_id}")
    List<Img> selectImgsByAId(int aId);

    @Insert("INSERT INTO t_img(a_id,img_url) VALUES (#{aId},#{imgUrl})")
    void insertImg(Img img);
}
