package com.kenneth.demo.websocketHandler;

import com.kenneth.demo.processor.SendMessageProcessor;
import org.apache.camel.*;
import org.apache.camel.api.management.mbean.ManagedCamelContextMBean;
import org.apache.camel.api.management.mbean.ManagedProcessorMBean;
import org.apache.camel.api.management.mbean.ManagedRouteMBean;
import org.apache.camel.builder.ErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultProducerTemplate;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.RoutesDefinition;
import org.apache.camel.model.rest.RestDefinition;
import org.apache.camel.model.rest.RestsDefinition;
import org.apache.camel.spi.*;
import org.apache.camel.util.LoadPropertiesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SpringWebSocketHandler extends TextWebSocketHandler {

    @Resource
    private CamelContext camelA;
    //@Autowired
    //private ProducerTemplate producerTemplate;

    private static Logger logger = LoggerFactory.getLogger(SpringWebSocketHandler.class);

    private static final Map<String, WebSocketSession> users;  //Map来存储WebSocketSession，key用USER_ID 即在线用户列表

    //用户标识
    private static final String USER_ID = "WEBSOCKET_USERID";   //对应监听器从的key


    static {
        users =  new HashMap<String, WebSocketSession>();
    }

    public SpringWebSocketHandler() {
    }

    /**
     * 连接成功时候，会触发页面上onopen方法
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("成功建立websocket连接!");
        String userId = (String) session.getAttributes().get(USER_ID);
        users.put(userId,session);
        System.out.println("当前线上用户数量:"+users.size());

        //这块会实现自己业务，比如，当用户登录后，会把离线消息推送给用户
        //TextMessage returnMessage = new TextMessage("成功建立socket连接，你将收到的离线");
        //session.sendMessage(returnMessage);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
    }

    /**
     * js调用websocket.send时候，会调用该方法
     */
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        //String result = (String) exchange.getIn().getBody();
       // System.out.println("camel return result:" + result);

        //CamelContext context = new DefaultCamelContext();
        //context.getEndpoint("direct://testCamelDirect").createConsumer(new SendMessageProcessor());

        ProducerTemplate producerTemplate = new DefaultProducerTemplate(camelA);
        producerTemplate.send(camelA.getEndpoint("direct://testCamelDirect"), new SendMessageProcessor());

       /* context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct://testCamelDirect1").to("direct://testCamelDirect").process("sendMessageProcessor");
            }
        });*/

        /**
         * 收到消息，自定义处理机制，实现业务
         */
        System.out.println("服务器收到消息："+message);

        if(message.getPayload().startsWith("#anyone#")){ //单发某人

            sendMessageToUser((String)session.getAttributes().get(USER_ID), new TextMessage("服务器单发：" +message.getPayload())) ;

        }else if(message.getPayload().startsWith("#everyone#")){

            sendMessageToUsers(new TextMessage("服务器群发：" +message.getPayload()));

        }else{

        }

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        logger.debug("传输出现异常，关闭websocket连接... ");
        String userId= (String) session.getAttributes().get(USER_ID);
        users.remove(userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        logger.debug("关闭websocket连接");
        String userId= (String) session.getAttributes().get(USER_ID);
        System.out.println("用户"+userId+"已退出！");
        users.remove(userId);
        System.out.println("剩余在线用户"+users.size());

    }

    /**
     * 给某个用户发送消息
     *
     * @param userId
     * @param message
     */
    public void sendMessageToUser(String userId, TextMessage message) {
        for (String id : users.keySet()) {
            if (id.equals(userId)) {
                try {
                    if (users.get(id).isOpen()) {
                        users.get(id).sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (String userId : users.keySet()) {
            try {
                if (users.get(userId).isOpen()) {
                    users.get(userId).sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
