package com.kenneth.demo.processor;

import com.kenneth.demo.util.MyWebsocketUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.atmosphere.websocket.WebsocketConstants;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestRequestProcessor implements Processor{

    @Autowired
    private HttpServletRequest request;

    public void process(Exchange exchange) throws Exception {
        System.out.println("Request processor start");
        Map<String,String> map = MyWebsocketUtil.getConnectionKeys().get(0);
        //Map<String,String> map = MyWebsocketUtil.getConnectionKeys().get(1);
        //exchange.getIn().setHeader(WebsocketConstants.CONNECTION_KEY, map.get("connectionKey"));
      //  exchange.getIn().setHeader("breadcrumbId", map.get("breadcrumbId"));
        String key = map.get("connectionKey");
        List<String> list = new ArrayList<String>();
        list.add(map.get("connectionKey"));
        list.add(MyWebsocketUtil.getConnectionKeys().get(1).get("connectionKey"));
       // list.add()
        exchange.getIn().setHeader(WebsocketConstants.CONNECTION_KEY_LIST, list);
        exchange.getIn().setBody("Hello world.");
    }
}
