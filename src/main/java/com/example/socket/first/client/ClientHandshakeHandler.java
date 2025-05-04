package com.example.socket.first.client;

import com.sun.security.auth.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

/**
 * @author Gaurav Regmi
 */
@Slf4j
public class ClientHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest req, WebSocketHandler weHandler, Map<String, Object> attributes) {
        final String randId = UUID.randomUUID().toString();
        log.info("{}",attributes.get("name"));
        log.info("User opened client unique ID {}, ipAddress {}",randId,req.getRemoteAddress());
        return new UserPrincipal(randId);
    }
}
