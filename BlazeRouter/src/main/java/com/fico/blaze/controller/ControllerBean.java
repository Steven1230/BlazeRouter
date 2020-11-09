package com.fico.blaze.controller;

import org.springframework.stereotype.Component;

@Component
public class ControllerBean {

    public String sayHello(String hello){
        return hello;
    }
}
