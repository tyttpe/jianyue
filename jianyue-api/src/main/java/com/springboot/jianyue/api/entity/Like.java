package com.springboot.jianyue.api.entity;

import lombok.Data;

@Data
public class Like {
    private Integer id;
    private Integer fromLike;
    private Integer toLike;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFromLike() {
        return fromLike;
    }

    public void setFromLike(Integer fromLike) {
        this.fromLike = fromLike;
    }

    public Integer getToLike() {
        return toLike;
    }

    public void setToLike(Integer toLike) {
        this.toLike = toLike;
    }
}
