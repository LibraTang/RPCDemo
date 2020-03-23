package com.example.RPCDemo.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

public class Registry {
    private static Logger log = LoggerFactory.getLogger(Registry.class);
    //此处用Hashtable是考虑到线程安全
    public static Map<String, Url> registry = new Hashtable<String, Url>();

    public static void main(String[] args) throws IOException {
        new Registry().run();
    }

    private void run() throws IOException {
        //监控服务端的注册消息
        ServerSocket listener = new ServerSocket(9092);

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
                        registry.put(url.getMethod(), url);
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
        } finally {
            listener.close();
        }
    }
}
