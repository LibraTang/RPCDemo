package com.example.RPCDemo.server;

import com.example.RPCDemo.exception.MethodNotFoundException;

public interface Calculator {
    int add(int a, int b) throws MethodNotFoundException;
}
