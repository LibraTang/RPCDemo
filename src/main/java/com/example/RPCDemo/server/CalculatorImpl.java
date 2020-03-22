package com.example.RPCDemo.server;

import com.example.RPCDemo.registry.Registry;
import com.example.RPCDemo.registry.RegistryService;
import com.example.RPCDemo.registry.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CalculatorImpl implements Calculator, RegistryService {
    private static Logger log = LoggerFactory.getLogger(CalculatorImpl.class);
    private static int PORT = 9092;
    private static CalculatorImpl instance = new CalculatorImpl();

    public static void main(String[] args) {
        Url url = new Url();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("a", "");
        parameters.put("b", "");

        url.setAddress("127.0.0.1");
        url.setMethod("Calculator.add");
        url.setParameters(parameters);

        instance.register(url);
        log.info("注册服务: {}", url.getMethod());
    }

    public int add(int a, int b) {
        return a + b;
    }

    public void register(Url url) {
//        Registry.registry.put(url.getMethod(), url);
        //todo: 将url传送给注册中心
    }

    public void unregister(Url url) {
        Registry.registry.remove(url.getMethod());
    }
}
