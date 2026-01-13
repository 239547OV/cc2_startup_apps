package com.aa.crewcomp.jobs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes={App.class})
public class AppTest{
    public static final String[] args = new String[]{"A", "B"};
    @Test
    public void test() throws InterruptedException{
        new App();
        App.main(args);
    }
}
