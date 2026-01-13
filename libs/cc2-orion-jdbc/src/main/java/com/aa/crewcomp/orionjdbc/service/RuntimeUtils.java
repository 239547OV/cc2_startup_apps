package com.aa.crewcomp.orionjdbc.service;

import org.springframework.stereotype.Component;

@Component
public class RuntimeUtils {
    public long getFreeMemory() {
        return Runtime.getRuntime().freeMemory();
    }

    public long getTotalMemory() {
        return Runtime.getRuntime().totalMemory();
    }
}
