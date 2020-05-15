package com.example.rpcdemo.server;

import com.example.rpcdemo.client.RpcRequest;
import com.example.rpcdemo.core.RPC;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 服务端反射调用方法工具类
 */
@Slf4j
public class InvokeUtil {

    /**
     * 反射调用相应的实现类并返回执行结果
     * @return
     */
    public static Object invoke(RpcRequest request) {
        Object result = null;
        // 实现类的类名
        String implClassName = RPC.getServerConfig().getServerImplMap().get(request.getClassName());

        try {
            // 实现类
            Class implClass = Class.forName(implClassName);
            // 实参
            Object[] parameters = request.getParameters();
            // 实参个数
            int parameterNum = parameters.length;
            // 参数类型
            Class[] parameterTypes = new Class[parameterNum];
            for (int i = 0; i < parameterNum; i++) {
                parameterTypes[i] = parameters[i].getClass();
            }

            // 反射调用相应方法
            Method method = implClass.getMethod(request.getMethodName(), parameterTypes);
            result = method.invoke(implClass.newInstance(), parameters);
        } catch (ClassNotFoundException e) {
            log.info("未找到相应的实现类");
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            log.info("未找到相应的方法实现");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }

}
