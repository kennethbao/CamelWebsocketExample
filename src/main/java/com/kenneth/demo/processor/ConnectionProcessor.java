package com.kenneth.demo.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class ConnectionProcessor implements Processor {

    public void process(Exchange exchange) throws Exception {
        System.out.println("start connector: "+exchange.getIn().getHeaders());
    }
}
