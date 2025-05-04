package com.example.socket.first.jms;

import com.example.socket.first.dto.MessageInfo;
import com.example.socket.first.service.WsService;
import com.example.socket.first.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import javax.jms.TextMessage;

/**
 * @author Gaurav Regmi
 */
@Slf4j
@Component
public class JmsConsumer {

    @Autowired
    private WsService wsService;

    private static final String NOTIFICATION_WEB_FIRST_QUEUE = "bankxp.queue.first.notification.web";
    private static final String NOTIFICATION_WEB_FACTORY = "notificationWebFactory";

    @JmsListener(destination = NOTIFICATION_WEB_FIRST_QUEUE, containerFactory = NOTIFICATION_WEB_FACTORY)
    public void consumeMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;

            MessageInfo messageInfo = JacksonUtil.getObject(textMessage.getText(), MessageInfo.class);

            wsService.sendMessage(messageInfo.getMessageContent());

            System.out.println("Consumed message: " + messageInfo);
        } catch (Exception e) {
            log.error("Error : ", e);
        }
    }
}
