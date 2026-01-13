package com.aa.crewcomp.orionjdbc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.aa.crewcomp.orionjdbc.mock.config.MyConfig;

@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { MyConfig.class })
public class AppTest{
    public static final String[] args = new String[]{"A", "B"};
    @Test
    public void test() throws InterruptedException{
        new App();
        App.main(args);
    }
}
