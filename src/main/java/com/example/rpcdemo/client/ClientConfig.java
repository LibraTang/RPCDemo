package com.example.rpcdemo.client;

import com.example.rpcdemo.core.RPC;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 客户端配置类
 */
@Data
public class ClientConfig implements ApplicationContextAware {
    /**
     * 调用的ip
     */
    private String host;
    /**
     * 调用的端口号
     */
    private int port;
    /**
     * 调用超时时间
     */
    private long overTime;

    /**
     * 加载Spring配置文件的时候，如果其中的bean实现了 ApplicationContextAware 接口
     * 那么就会自动调用这个接口中的 setApplicationContext 方法
     * 将容器本身作为参数传进去赋予applicationContext，之后就可以通过该applicationContext访问容器本身
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        RPC.clientContext = applicationContext;
    }
}
