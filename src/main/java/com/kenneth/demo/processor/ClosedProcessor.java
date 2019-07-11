package com.kenneth.demo.processor;

import com.kenneth.demo.util.MyWebsocketUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ClosedProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        System.out.println("closed connector: " + exchange.getIn().getHeaders());
        MyWebsocketUtil.getConnectionKeys().clear();
    }
}
