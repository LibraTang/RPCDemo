package com.example.RPCDemo.server;

import com.example.RPCDemo.exception.MethodNotFoundException;
import com.example.RPCDemo.registry.Url;

public interface Calculator {
    int add(int a, int b);
    int add(Url url);
}
