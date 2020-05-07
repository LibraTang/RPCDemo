package com.example.rpcdemo.registry;

public interface DiscoveryService {
    /**
     * 从注册中心订阅服务
     * @param method
     */
    void subscribe(String method);
}
