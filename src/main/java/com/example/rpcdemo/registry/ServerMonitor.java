package com.example.rpcdemo.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMonitor implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(ServerMonitor.class);

    /**
     * 这段代码的try catch写的太难看了……之后换种通信方式
     */
    public void run() {
        //监控服务端的注册消息
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(9092);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            while (true) {
                Socket socket = listener.accept();
                try {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                    Object request = objectInputStream.readObject();
                    log.info("request from server: {}", request);

                    //注册
                    if(request instanceof Url) {
                        Url url = (Url) request;
                        RegistryApp.registry.put(url.getMethod(), url);
                        log.info("注册方法: {}", url.getMethod());

                        objectOutputStream.writeObject("成功注册: " + url.getMethod());
                    } else {
                        objectOutputStream.writeObject("非法的请求");
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
