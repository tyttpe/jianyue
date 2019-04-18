package com.springboot.jianyue.api.service.impl;

import com.springboot.jianyue.api.entity.User;
import com.springboot.jianyue.api.entity.dto.UserDTO;
import com.springboot.jianyue.api.service.UserService;
import com.springboot.jianyue.api.util.StatusConst;
import com.springboot.jianyue.api.util.StringUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Resource
    private UserService userService;

    @Test
    public void getUserByMobile() {
        User user = userService.getUserByMobile("13873671595");
        System.out.println(user);
    }

    @Test
    public void getUserById() {
        User user = userService.getUserById(1);
        System.out.println(user);
    }

    @Test
    public void signIn() {
        UserDTO loginUser = new UserDTO();
        loginUser.setMobile("13873671595");
        String base64Pass = StringUtil.getBase64Encoder("123");
        loginUser.setPassword(base64Pass);
        int status = userService.signIn(loginUser);
        assertEquals(StatusConst.SUCCESS, status);
    }

    @Test
    public void updateUser() {
        User user = userService.getUserById( 2 );
        user.setAvatar("1.jpg");
        userService.updateUser(user);
    }

}