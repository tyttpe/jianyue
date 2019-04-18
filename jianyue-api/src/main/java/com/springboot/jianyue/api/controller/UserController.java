package com.springboot.jianyue.api.controller;

import com.aliyun.oss.OSSClient;
import com.springboot.jianyue.api.entity.User;
import com.springboot.jianyue.api.entity.dto.UserDTO;
import com.springboot.jianyue.api.service.UserService;
import com.springboot.jianyue.api.service.RedisService;
import com.springboot.jianyue.api.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private RedisService redisService;

    @PostMapping(value = "/sign_in")
    public ResponseResult signIn(@RequestBody UserDTO userDTO) {
        User user = userService.getUserByMobile(userDTO.getMobile());
        if (user == null) {
            return ResponseResult.error(StatusConst.USER_MOBILE_NOT_FOUND, MsgConst.USER_MOBILE_NO_FOUND);
        } else {
            //手机号存在，将明文密码转成Base64密文后进行登录
            userDTO.setPassword(StringUtil.getBase64Encoder(userDTO.getPassword()));
            int status = userService.signIn(userDTO);
            if (status == StatusConst.SUCCESS) {
                return ResponseResult.success(user);
            } else if (status == StatusConst.PASSWORD_ERROR) {
                return ResponseResult.error(status, MsgConst.PASSWORD_ERROR);
            } else {
                return ResponseResult.error(status, MsgConst.USER_STATUS_ERROR);
            }
        }
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ResponseResult getUserById(@PathVariable("id") int id){
        User user = userService.getUserById(id);
        return ResponseResult.success(user);
    }

    @RequestMapping(value = "/update",method = RequestMethod.PUT)
    public void update(@RequestBody String nickname,int id){
        User user = userService.getUserById(id);
        user.setNickname(nickname);
        userService.updateUser(user);
    }

    @RequestMapping(value = "/updatepwd",method = RequestMethod.PUT)
    public void updatepwd(@RequestBody String password,int id){
            User user = userService.getUserById(id);
            user.setPassword(StringUtil.getBase64Encoder(password));
            userService.updateUser(user);
    }

    @PostMapping("/avatar")
    public String ossUpload(@RequestParam("file") MultipartFile sourceFile, @RequestParam("userId") int userId) {
        System.out.println(userId);
        String endpoint = "http://oss-cn-hangzhou.aliyuncs.com";
        String accessKeyId = "LTAIRf6ykeToGFIY";
        String accessKeySecret = "pK2iLWVVrPApJ3JTWxaBuqtfUfVmJ0";
        String bucketName = "tyttpe";
        String filedir = "avatar/";
        // 获取文件名
        String fileName = sourceFile.getOriginalFilename();
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //uuid生成主文件名
        String prefix = UUID.randomUUID().toString();
        String newFileName = prefix + suffix;
        File tempFile = null;
        try {
            //创建临时文件
            tempFile = File.createTempFile(prefix, prefix);
            // MultipartFile to File
            sourceFile.transferTo(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        ossClient.putObject(bucketName, filedir + newFileName, tempFile);
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        // 生成URL
        URL url = ossClient.generatePresignedUrl(bucketName, filedir + newFileName, expiration);
        ossClient.shutdown();
        User user = userService.getUserById(userId);
        user.setAvatar(url.toString());
        userService.updateUser(user);
        return url.toString();
    }

    @PostMapping(value = "/verify")
    public ResponseResult getVerifyCode(@RequestParam("mobile") String mobile) {
        User user = userService.getUserByMobile(mobile);
        //手机号已经被注册
        if (user != null) {
            return ResponseResult.error(StatusConst.MOBILE_EXIST, MsgConst.MOBILE_EXIST);
        } else {
            //发送验证码
            String verifyCode = SMSUtil.send(mobile);
//            String verifyCode = StringUtil.getVerifyCode();
            System.out.println(verifyCode);
            //手机号和验证码作为键值对存入redis中
            redisService.set(mobile, verifyCode);
            return ResponseResult.success();
        }
    }

    @PostMapping(value = "/check")
    public ResponseResult checkVerifyCode(@RequestParam("mobile") String mobile, @RequestParam("verifyCode") String verifyCode) {


        System.out.println(verifyCode);

        String code = null;
        try{
            code=redisService.get(mobile).toString();
        }catch (NullPointerException e){
            return ResponseResult.error(StatusConst.VERIFYCODE_FAIL,MsgConst.VERIFYCODE_FAIL);

        }
            if (code.equals(verifyCode)) {
                return ResponseResult.success();
            } else {
                return ResponseResult.error(StatusConst.VERIFYCODE_ERROR, MsgConst.VERIFYCODE_ERROR);
            }


    }

    @PostMapping(value = "/sign_up")
    public ResponseResult signUp(@RequestBody UserDTO userDTO) {
        userService.signUp(userDTO);
        return ResponseResult.success();
    }
}