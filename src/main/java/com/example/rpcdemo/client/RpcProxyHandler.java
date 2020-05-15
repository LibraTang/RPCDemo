package com.example.rpcdemo.client;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 动态代理的方法被调用后的回调方法
 */
public class RpcProxyHandler implements InvocationHandler {

    /**
     * 记录调用的次数 也作为ID标志
     */
    private static AtomicLong requestTimes = new AtomicLong(0);

    /**
     * 生成随机数
     */
    private Random random = new Random();

    /**
     * 回调方法
     * @param proxy 代理的接口
     * @param method 调用的方法
     * @param args 调用方法的实参
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        request.setRequestId(buildRequestId(method.getName()));
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameters(args);

        // 服务端通过id对应是哪个请求
        RpcRequestNet.requestLockMap.put(request.getRequestId(), request);
        // 向服务端发送请求
        RpcRequestNet.connect().send(request);
        // 调用结束后移除对应的请求实体映射关系
        RpcRequestNet.requestLockMap.remove(request.getRequestId());

        return request.getResult();
    }

    /**
     * 构造请求id
     * @param methodName
     * @return
     */
    private String buildRequestId(String methodName) {
        StringBuilder sb = new StringBuilder();
        sb.append(requestTimes.incrementAndGet());
        sb.append(System.currentTimeMillis());
        sb.append(methodName);
        sb.append(random.nextInt(1000));
        return sb.toString();
    }
}
