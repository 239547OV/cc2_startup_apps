package com.aa.crewcomp.rulesengine.mock.service;

import org.springframework.stereotype.Component;

@Component
public class MySimpleService {
    private final String expectedValue = "Injected Bean is working!";
    public String returnExpectedValue() {
        System.out.println("MySimpleService bean executed returnExpectedValue()");
        return expectedValue;
    }
}
