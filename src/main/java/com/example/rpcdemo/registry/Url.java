package com.example.rpcdemo.registry;

import java.io.Serializable;
import java.util.Map;

public class Url implements Serializable {
    private static final long serialVersionUID = 1L;

    private String address;
    private String method;
    private Map<String, String> parameters;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
