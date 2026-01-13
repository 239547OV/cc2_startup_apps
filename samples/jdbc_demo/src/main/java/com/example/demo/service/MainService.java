package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MainService {

    @Autowired
    private MyCc2JobsExecutor myCc2JobsExecutor;

    @EventListener(ApplicationReadyEvent.class)
    void execute() {
        this.myCc2JobsExecutor.execute();
    }
    
}
