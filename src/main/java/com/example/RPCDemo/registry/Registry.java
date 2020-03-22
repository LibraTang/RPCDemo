package com.example.RPCDemo.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Hashtable;
import java.util.Map;

public class Registry {
    private static Logger log = LoggerFactory.getLogger(Registry.class);
    public static Map<String, Url> registry = new Hashtable<String, Url>();

    public static void main(String[] args) throws IOException {
        new Registry().run();
    }

    private void run() throws IOException {
        //监控服务端的注册消息
        ServerSocket listener = new ServerSocket(9092);

        try {
            while (true) {
                //todo:监控服务端发过来的url，将此url注册到Hashtable中
            }
        } finally {
            listener.close();
        }
    }
}
