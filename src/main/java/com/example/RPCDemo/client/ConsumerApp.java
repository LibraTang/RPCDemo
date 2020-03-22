package com.example.RPCDemo.client;

import com.example.RPCDemo.exception.MethodNotFoundException;
import com.example.RPCDemo.server.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerApp {
    private static Logger log = LoggerFactory.getLogger(ConsumerApp.class);

    public static void main(String[] args) {
        Calculator calculator = new CalculatorRemoteImpl();
        try {
            int result = calculator.add(1000, 2);
            log.info("result is {}", result);
        } catch (MethodNotFoundException e) {
            log.error(e.getMessage());
        }
    }
}
