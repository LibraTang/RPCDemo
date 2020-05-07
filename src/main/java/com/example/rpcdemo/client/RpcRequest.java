package com.example.rpcdemo.client;

import lombok.Data;

/**
 * 客户端的请求实体类
 * 也作为回调中同步等待的对象锁
 */
@Data
public class RpcRequest {
    /**
     * 请求实体的id，唯一标识
     */
    private String requestId;
    /**
     * 调用方法所属的接口名称
     */
    private String className;
    /**
     * 调用方法名称
     */
    private String methodName;
    /**
     * 调用方法的实参
     */
    private Object[] parameters;
    /**
     * 调用方法返回的结果
     */
    private Object result;
}
