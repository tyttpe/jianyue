package com.springboot.jianyue.api.util;

import com.springboot.jianyue.api.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
//@Async
public class TaskService {
    @Autowired
    private MailService mailService;

    @Scheduled(cron = "0 32 9 ? * *")
    public void proces(){
        mailService.sendMail("16422802@qq.com","田寅","定时9点32发送");
    }
}
