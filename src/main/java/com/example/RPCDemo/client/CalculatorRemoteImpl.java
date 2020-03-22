package com.example.RPCDemo.client;

import com.example.RPCDemo.exception.MethodNotFoundException;
import com.example.RPCDemo.registry.DiscoveryService;
import com.example.RPCDemo.registry.Registry;
import com.example.RPCDemo.registry.Url;
import com.example.RPCDemo.server.Calculator;
import com.example.RPCDemo.request.CalculateRpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CalculatorRemoteImpl implements Calculator, DiscoveryService{
    public static final int PORT = 9091;
    private static Logger log = LoggerFactory.getLogger(CalculatorRemoteImpl.class);
    private static Url url = new Url();

    public int add(int a, int b) throws MethodNotFoundException {
        String method = "Calculator.add";
        subscribe(method);
        if(null == url || "".equals(url))
            throw new MethodNotFoundException("没有在注册中心找到方法" + method);

        List<String> addressList = lookupProviders("Calculator.add");
        String address = chooseProvider(addressList);
        try {
            Socket socket = new Socket(address, PORT);

            //将请求序列化
//            CalculateRpcRequest calculateRpcRequest = generateRequest(a, b);
            url = generateUrl(a, b);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            //将请求发送给服务提供方
            objectOutputStream.writeObject(url);

            //将响应反序列化
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            Object response = objectInputStream.readObject();

            log.info("response is {}", response);

            if(response instanceof Integer) {
                return (Integer)response;
            } else {
                throw new InternalError();
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new InternalError();
        } catch (IOException e) {
            e.printStackTrace();
            throw new InternalError();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new InternalError();
        }
    }

    /**
     * 寻找要调用的服务的实例列表，由于这里不考虑服务注册中心和负载均衡，因此写死127.0.0.1
     * @param name 服务的实例名称
     * @return
     */
    private List<String> lookupProviders(String name) {
        List<String> providers = new ArrayList<String>();
        providers.add("127.0.0.1");
        return providers;
    }

    /**
     * 选择一个服务注册中心
     * @param providers 服务的实例列表
     * @return
     */
    private String chooseProvider(List<String> providers) {
        if(providers == null || providers.isEmpty())
            throw new IllegalArgumentException();
        return providers.get(0);
    }

    /**
     * 生成序列化的请求
     * @param a
     * @param b
     * @return
     */
    private CalculateRpcRequest generateRequest(int a, int b) {
        CalculateRpcRequest calculateRpcRequest = new CalculateRpcRequest();
        calculateRpcRequest.setA(a);
        calculateRpcRequest.setB(b);
        calculateRpcRequest.setMethod("add");
        return calculateRpcRequest;
    }

    /**
     * 生成请求url
     * @param a
     * @param b
     * @return
     */
    private Url generateUrl(int a, int b) {
        Map<String, String> parameters = url.getParameters();
        parameters.put("a", String.valueOf(a));
        parameters.put("b", String.valueOf(b));
        return url;
    }

    /**
     * 发现服务
     * @param method 方法名
     */
    public void subscribe(String method) {
        url = Registry.registry.get(method);
    }
}
