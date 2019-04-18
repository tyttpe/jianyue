package com.springboot.jianyue.api.controller;

import com.springboot.jianyue.api.mapper.UserMapper;
import com.springboot.jianyue.api.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.List;

@Controller
public class ImportController {

    @RequestMapping("/index")
    public String index(){
        return "index";
    }
    @Autowired
    private ImportService importService;
/*    @Autowired
    private  User user;
    @Autowired
    private UserMapper userMapper;*/
    @Resource
    private UserMapper userMapper;

    @RequestMapping(value="/upload",method= RequestMethod.POST)
    public  @ResponseBody String  uploadExcel(HttpServletRequest request) throws Exception {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        InputStream inputStream =null;
        List<List<Object>> list = null;
        MultipartFile file = multipartRequest.getFile("filename");
        if(file.isEmpty()){
            return "文件不能为空";
        }
        inputStream = file.getInputStream();
        list = importService.getBankListByExcel(inputStream,file.getOriginalFilename());
        inputStream.close();
//连接数据库部分
        for (int i = 0; i < list.size(); i++) {
            List<Object> lo = list.get(i);
            userMapper.insert2(String.valueOf(lo.get(0)),String.valueOf(lo.get(1)),String.valueOf(lo.get(2)));
            //调用mapper中的insert方法
        }
        return "上传成功";
    }
}
