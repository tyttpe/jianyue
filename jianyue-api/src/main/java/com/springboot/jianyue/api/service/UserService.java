package com.springboot.jianyue.api.service;

import com.springboot.jianyue.api.entity.User;
import com.springboot.jianyue.api.entity.dto.UserDTO;

public interface UserService {
    //根据手机号获取用户信息
    User getUserByMobile(String mobile);

    //登录方法
    int signIn(UserDTO userDTO);

    User getUserById(int id);

    void updateUser(User user);

    void signUp(UserDTO userDTO);

}
