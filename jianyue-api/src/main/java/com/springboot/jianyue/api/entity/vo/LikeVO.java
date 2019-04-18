package com.springboot.jianyue.api.entity.vo;

import lombok.Data;

@Data
public class LikeVO {
    private Integer toLike;
    private String nickname;
    private String title;

    public Integer getToLike() {
        return toLike;
    }

    public void setToLike(Integer toLike) {
        this.toLike = toLike;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
