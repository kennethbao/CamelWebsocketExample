package com.kenneth.demo.processor;

import com.kenneth.demo.util.MyWebsocketUtil;
import com.kenneth.demo.websocketHandler.SpringWebSocketHandler;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.atmosphere.websocket.WebsocketConstants;
import org.apache.camel.component.atmosphere.websocket.WebsocketConsumer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

public class MyProcessor implements Processor {

   // @Autowired
   // private WebsocketConsumer websocketConsumer;
    @Autowired
    private HttpServletRequest request;

    public void process(Exchange exchange) throws Exception {

        System.out.println("start camel processor" + new Date().toString());
        System.out.println(exchange.getIn().getHeaders());
        String broadId = (String) exchange.getIn().getHeader("breadcrumbId");
        String connectionKey = (String) exchange.getIn().getHeader(WebsocketConstants.CONNECTION_KEY);
       // String eventType = (String)exchange.getIn().getHeader(WebsocketConstants.EVENT_TYPE);
        //int eventType = (int) exchange.getIn().getHeader(WebsocketConstants.EVENT_TYPE);
        String str = (String) exchange.getIn().getBody();
        //request.getSession().setAttribute("connectionKey",connectionKey);
        Map<String,String> connectionInfos = new HashMap<String, String>();
        connectionInfos.put("breadcrumbId", broadId);
        connectionInfos.put("connectionKey", connectionKey);
        MyWebsocketUtil.getConnectionKeys().add(connectionInfos);
        if(StringUtils.isNotEmpty(str)){
            String broadCastId = (String) exchange.getIn().getHeader("breadcrumbId");
            exchange.getIn().setBody(broadCastId + " return info: " + str);
            exchange.getIn().setHeader("connectionKey", connectionKey);
        }else{
            exchange.getIn().setBody("server error");

            /*if(eventType.equals(WebsocketConstants.ONOPEN_EVENT_TYPE)){
                System.out.println("start connection...");
            }else if(eventType.equals(WebsocketConstants.ONCLOSE_EVENT_TYPE)){
                System.out.println("Connection has been closed successfully for next connection key: " + connectionKey);
            }else if(eventType.equals(WebsocketConstants.ONERROR_EVENT_TYPE)){
                System.out.println("An error event has been triggered for next connection key: " + connectionKey);
            }*/
        }
    }
}
