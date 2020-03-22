package com.example.RPCDemo.registry;

public interface RegistryService {
    /**
     * 向注册中心注册服务
     * @param url
     */
    void register(Url url);

    /**
     * 从注册中心摘除服务
     * @param url
     */
    void unregister(Url url);
}
