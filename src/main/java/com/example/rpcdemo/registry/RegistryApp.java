package com.example.rpcdemo.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class RegistryApp {
    private static Logger log = LoggerFactory.getLogger(RegistryApp.class);
    //此处用Hashtable是考虑到线程安全
    public static Map<String, Url> registry = new Hashtable<String, Url>();

    public static void main(String[] args) throws IOException {
        Thread serverMonitor = new Thread(new ServerMonitor());
        Thread clientMonitor = new Thread(new ClientMonitor());
        serverMonitor.start();
        log.info("服务端监控启动...");
        clientMonitor.start();
        log.info("客户端监控启动...");
    }
}
