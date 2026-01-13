package com.aa.crewcomp.orionjdbc.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.aa.crewcomp.orionjdbc.mock.config.MyConfig;

import lombok.Data;

@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { MyConfig.class })
public class ObjectSizeEstimatorTests {
    @Data
    class MyPojo{
        private boolean a;
        private char b;
        private int c;
        private long d;
        private byte e;
        private short f;
        private float g;
        private double h;
        private String i;
    }
    
    @Test
    public void testSizeEstimator() throws Exception {
        new ObjectSizeEstimator();
        ObjectSizeEstimator.estimateSize(MyPojo.class);
    }
    
}
