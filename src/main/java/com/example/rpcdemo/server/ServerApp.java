package com.example.rpcdemo.server;

import com.example.rpcdemo.registry.Url;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

@Deprecated
public class ServerApp {
    private static Logger log = LoggerFactory.getLogger(ServerApp.class);
    private static CalculatorImpl calculator = new CalculatorImpl();

    public static void main(String[] args) throws IOException {
        new ServerApp().run();
    }

    private void run() throws IOException {
        ServerSocket listener = new ServerSocket(9091);
        try {
            while(true) {
                Socket socket = listener.accept();

                try {
                    //将请求反序列化
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Object request = objectInputStream.readObject();

                    log.info("request is {}", request);

                    //调用服务
                    int result = 0;
                    if (request instanceof Url) {
                        Url url = (Url) request;
                        if ("Calculator.add".equals(url.getMethod())) {
                            result = calculator.add(Integer.parseInt(url.getParameters().get("a")), Integer.parseInt(url.getParameters().get("b")));
                        } else {
                            throw new UnsupportedOperationException();
                        }
                    }

                    //返回结果
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(new Integer(result));
                    log.info("result is: {}", result);

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    socket.close();
                }
            }
        } finally {
            listener.close();
        }
    }
}
