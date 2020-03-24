package com.example.RPCDemo.client;

import com.example.RPCDemo.exception.MethodNotFoundException;
import com.example.RPCDemo.registry.DiscoveryService;
import com.example.RPCDemo.registry.Url;
import com.example.RPCDemo.server.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientApp implements DiscoveryService {
    private static Logger log = LoggerFactory.getLogger(ClientApp.class);
    private static final ClientApp instance = new ClientApp();
    private static final int PORT = 9093;
    private static Url url;

    public static void main(String[] args) {
        Calculator calculator = new CalculatorRemoteImpl();
        instance.subscribe("Calculator.add");
    }

    public void subscribe(String method) {
        try {
            Socket socket = new Socket("127.0.0.1", PORT);

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            //将订阅方法发送给注册中心
            objectOutputStream.writeObject(method);
            log.info("订阅方法: {}", method);

            //接收注册中心的响应
            Object response = objectInputStream.readObject();
            log.info("response from registry: {}", response);

            if(response instanceof Url)
                url = (Url) response;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
