package com.example.rpcdemo.server;

import com.example.rpcdemo.core.RPC;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * 服务端配置类
 */
@Data
public class ServerConfig implements ApplicationContextAware {
    /**
     * 服务端端口号
     */
    private int port;

    /**
     * 抽象接口及其实现
     */
    private Map<String, String> serverImplMap;

    /**
     * 在运行过程中获取IOC容器
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RPC.serverContext = applicationContext;
    }
}
