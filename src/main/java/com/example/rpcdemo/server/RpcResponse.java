package com.example.rpcdemo.server;

import lombok.Data;

/**
 * 服务端响应实体
 */
@Data
public class RpcResponse {
    /**
     * 请求实体的id
     */
    private String requestId;
    /**
     * 服务端执行结果
     */
    private Object result;
}
