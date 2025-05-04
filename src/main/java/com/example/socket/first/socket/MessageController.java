package com.example.socket.first.socket;

import com.example.socket.first.dto.MessageInfo;
import com.example.socket.first.dto.ResponseMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.security.Principal;

/**
 * @author Gaurav Regmi
 */
@Controller
public class MessageController {

    @MessageMapping("/message/first")
    @SendTo("/receive/message/first")
    public ResponseMessage getMessage(final MessageInfo messageInfo) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("recived message");
        return new ResponseMessage(HtmlUtils.htmlEscape(messageInfo.getMessageContent()));
    }

    @MessageMapping("/private-message/first")
    @SendToUser("/receive/private-message/first")
    public ResponseMessage getPrivateMessage(final MessageInfo mess, final Principal principal) throws InterruptedException {
        Thread.sleep(1000);
        System.out.println("recived message private" + principal.getName());
        return new ResponseMessage(HtmlUtils.htmlEscape("Sending personal message to user" + principal.getName() + ": " + mess.getMessageContent()));
    }
}
