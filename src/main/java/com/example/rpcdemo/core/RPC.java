package com.example.rpcdemo.core;

import com.example.rpcdemo.client.ClientConfig;
import com.example.rpcdemo.client.RpcProxyHandler;
import com.example.rpcdemo.client.RpcRequest;
import com.example.rpcdemo.server.RpcResponse;
import com.example.rpcdemo.server.RpcResponseNet;
import com.example.rpcdemo.server.ServerConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
import java.lang.reflect.Proxy;

/**
 * RPC工具类
 */
@Slf4j
public class RPC {
    /**
     * 用于json和java对象之间的转换
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static ApplicationContext serverContext;
    public static ApplicationContext clientContext;

    /**
     * 私有构造方法，避免被实例化
     */
    private RPC() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 为抽象接口生成动态代理对象
     * @param cls 抽象接口类类型
     * @return 接口生成的动态代理对象
     */
    public static Object call(Class<?> cls) {
        RpcProxyHandler handler = new RpcProxyHandler();
        return Proxy.newProxyInstance(cls.getClassLoader(), new Class<?>[]{cls}, handler);
    }

    /**
     * 服务端启动rpc服务
     */
    public static void start() {
        log.info("RpcDemo start...");
        RpcResponseNet.connect();
    }

    /**
     * 请求实体转换为json字符串
     * @param request 请求实体
     * @return json字符串
     * @throws JsonProcessingException
     */
    public static String requestEncode(RpcRequest request) throws JsonProcessingException {
        return objectMapper.writeValueAsString(request) + System.getProperty("line.separator");
    }

    /**
     * json字符串转换为请求实体
     * @param json json字符串
     * @return 请求实体
     * @throws IOException
     */
    public static RpcRequest requestDecode(String json) throws IOException {
        return objectMapper.readValue(json, RpcRequest.class);
    }

    /**
     * 响应实体转换为json字符串
     * @param response 响应实体
     * @return json字符串
     * @throws JsonProcessingException
     */
    public static String responseEncode(RpcResponse response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(response) + System.getProperty("line.separator");
    }

    /**
     * json字符串转换为响应实体
     * @param json json字符串
     * @return 响应实体
     * @throws IOException
     */
    public static RpcResponse responseDecode(String json) throws IOException {
        return objectMapper.readValue(json, RpcResponse.class);
    }

    public static ServerConfig getServerConfig() {
        return serverContext.getBean(ServerConfig.class);
    }

    public static ClientConfig getClientConfig() {
        return clientContext.getBean(ClientConfig.class);
    }
}
