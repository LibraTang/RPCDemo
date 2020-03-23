package com.example.RPCDemo.server;

import com.example.RPCDemo.registry.Registry;
import com.example.RPCDemo.registry.RegistryService;
import com.example.RPCDemo.registry.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
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

        log.info("注册服务: {}", url.getMethod());
        instance.register(url);
    }

    public int add(int a, int b) {
        return a + b;
    }

    public void register(Url url) {
        try {
            Socket socket = new Socket("127.0.0.1", PORT);

            //将url发送给注册中心
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(url);

            //接收注册中心的响应消息
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object response = objectInputStream.readObject();

            log.info("response from registry: {}", response);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void unregister(Url url) {
        Registry.registry.remove(url.getMethod());
    }
}
