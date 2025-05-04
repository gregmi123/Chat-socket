package com.example.socket.first.service;

import com.example.socket.first.dto.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Gaurav Regmi
 */
@Service
public class WsService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WsService(SimpMessagingTemplate messagingTemplate){
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessage(final String message){
        ResponseMessage res = new ResponseMessage(message);
        messagingTemplate.convertAndSend("/receive/message",res);
    }
    public void sendPrivateMessage(final String message, final String id){
        ResponseMessage res = new ResponseMessage(message);
        messagingTemplate.convertAndSendToUser(id,"/receive/private-message", res);
    }
}
