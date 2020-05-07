package com.example.rpcdemo.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientMonitor implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(ClientMonitor.class);

    public void run() {
        ServerSocket listener = null;
        try {
            listener = new ServerSocket(9093);
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
                    log.info("request from client: {}", request);

                    if(request instanceof String) {
                        String method = (String) request;
                        Url url = RegistryApp.registry.get(method);
                        if(null == url || "".equals(url)) {
                            objectOutputStream.writeObject("没有在注册中心找到方法: " + method);
//                            throw new MethodNotFoundException("没有在注册中心找到方法: " + method);
                        }
                        else {
                            //给客户端返回该方法的url
                            objectOutputStream.writeObject(url);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                listener.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
