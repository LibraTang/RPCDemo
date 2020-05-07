package com.example.rpcdemo.server;

import com.example.rpcdemo.registry.Url;

@Deprecated
public interface Calculator {
    int add(int a, int b);
    int add(Url url);
}
