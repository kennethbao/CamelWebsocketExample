package com.kenneth.demo.processor;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class HandshakeInterceptor implements org.springframework.web.socket.server.HandshakeInterceptor {

    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        if (serverHttpRequest instanceof ServletServerHttpRequest) {
            HttpServletRequest servletRequest = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
            String sessionId=servletRequest.getSession().getId();//获取浏览器的sessionid
            String username=(String)servletRequest.getSession().getAttribute("name");
            System.out.println("获取session里面的name-------------------"+username);
            // 使用userName区分WebSocketHandler，以便定向发送消息
            /* String userName = (String)*/
            //session.getAttribute("WEBSOCKET_USERNAME");
            map.put("WEBSOCKET_USERNAME", username);
            servletRequest.getSession().setAttribute("WEBSOCKET_USERNAME", username);
        }
        return true;
    }

    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
