package com.example.rpcdemo.client;

import com.example.rpcdemo.registry.Url;
import com.example.rpcdemo.server.Calculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

@Deprecated
public class CalculatorRemoteImpl implements Calculator {
    public static final int PORT = 9091;
    private static Logger log = LoggerFactory.getLogger(CalculatorRemoteImpl.class);

    public int add(Url url) {
        try {
            Socket socket = new Socket(url.getAddress(), PORT);

            //将请求发送给服务提供方
            //如果将objectInputStream的初始化放在这里将导致卡在writeObject，还不知道为啥……
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(url);

            //将响应反序列化
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object response = objectInputStream.readObject();
            log.info("response is {}", response);

            if(response instanceof Integer) {
                return (Integer)response;
            } else {
                throw new InternalError();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new InternalError();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalError();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new InternalError();
        }
    }

    public int add(int a, int b) {
        return a + b;
    }
}
