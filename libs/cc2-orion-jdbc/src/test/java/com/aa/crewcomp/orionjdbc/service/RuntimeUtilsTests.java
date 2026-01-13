package com.aa.crewcomp.orionjdbc.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.aa.crewcomp.orionjdbc.mock.config.MyConfig;

@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { MyConfig.class })
public class RuntimeUtilsTests {
    @InjectMocks
    private RuntimeUtils runtimeUtils;

    @Test
    public void testGetMemory() throws Exception {
        this.runtimeUtils.getFreeMemory();
        this.runtimeUtils.getTotalMemory();
    }
}