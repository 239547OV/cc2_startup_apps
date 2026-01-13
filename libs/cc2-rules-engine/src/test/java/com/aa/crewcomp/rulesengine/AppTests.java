package com.aa.crewcomp.rulesengine;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.aa.crewcomp.rulesengine.mock.config.MyEngineAndRuleBeansConfig;

@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { MyEngineAndRuleBeansConfig.class })
public class AppTests {
    public static final String[] args = new String[]{"A", "B"};
    @Test
    public void test() throws InterruptedException{
        new App();
        App.main(args);
    }
}
