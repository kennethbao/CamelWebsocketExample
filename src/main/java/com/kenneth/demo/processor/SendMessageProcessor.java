package com.kenneth.demo.processor;

import com.kenneth.demo.websocketHandler.SpringWebSocketHandler;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.atmosphere.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.TextMessage;

import javax.annotation.Resource;

public class SendMessageProcessor implements Processor {
    @Resource
    private WebSocketHandler websockt;

    public void process(Exchange exchange) throws Exception {
        String str = (String) exchange.getIn().getBody();
        String camelResultInfo = "持续输出信息给前端：" + str;
        System.out.println(camelResultInfo);
       // websockt.sendMessageToUsers(new TextMessage(camelResultInfo));
    }
}
