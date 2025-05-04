package com.example.socket.first.controller;

import com.example.socket.first.dto.MessageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;


/**
 * @author Gaurav Regmi
 */
@RestController
public class JmsProducer {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("notificationWebSecond")
    Queue notificationWebSecondQueue;

    @PostMapping("send/message/tosecond")
    public void sendNotification(@RequestBody MessageInfo messageInfo) {
        jmsTemplate.convertAndSend(notificationWebSecondQueue, messageInfo);
    }
}
