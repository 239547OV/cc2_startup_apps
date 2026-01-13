package com.aa.crewcomp.rulesengine.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import com.aa.crewcomp.rulesengine.mock.config.MyEngineAndRuleBeansConfig;
import com.aa.crewcomp.rulesengine.mock.domain.MyAPlusBSampleEntity;
import com.aa.crewcomp.rulesengine.mock.domain.MySampleEntity;
import com.aa.crewcomp.rulesengine.mock.engines.MyAPlusBEngine;
import com.aa.crewcomp.rulesengine.mock.engines.MyEngine;

@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { MyEngineAndRuleBeansConfig.class })
public class CustomEnginesAndCustomRulesTests {
    private final String beanInjectExpectedValue = "Injected Bean is working!";
    @Autowired
    private MyEngine myEngine;

    @Autowired
    private MyAPlusBEngine myAPlusBEngine;

    /***
     * The myEngine engine is a bean whose rule autowires a component.
     * This method tests the bean injection.
     * @throws Exception
     */
    @Test
    public void testBeanInjection() throws Exception {
        assertNotNull(this.myEngine);
        List<MySampleEntity> input = new ArrayList<MySampleEntity>();
        MySampleEntity testRecord = new MySampleEntity(beanInjectExpectedValue);
        input.add(testRecord);
        this.myEngine.startEngineWithInputData(input);
        assertEquals(testRecord.getExpectedValue(), testRecord.getComputedValue());
    }

    /***
     * The myAPlusBEngine engine is a bean that computes A + B if A is pair.
     * This method tests the rule condition and action methods to be consistent for several records.
     * @throws Exception
     */
    @Test
    
    public void testAPlusB() throws Exception {
        assertNotNull(this.myAPlusBEngine);
        List<MyAPlusBSampleEntity> input = new ArrayList<MyAPlusBSampleEntity>();
        MyAPlusBSampleEntity testRecord1Plus3 = new MyAPlusBSampleEntity(1,3);
        input.add(testRecord1Plus3);
        MyAPlusBSampleEntity testRecord2Plus2 = new MyAPlusBSampleEntity(2,2);
        input.add(testRecord2Plus2);
        MyAPlusBSampleEntity testRecord3Plus7 = new MyAPlusBSampleEntity(3,7);
        input.add(testRecord3Plus7);
        MyAPlusBSampleEntity testRecord8Plus9 = new MyAPlusBSampleEntity(8,9);
        input.add(testRecord8Plus9);
        this.myAPlusBEngine.startEngineWithInputData(input);
        assertNull(testRecord1Plus3.getComputedAPlusB());
        assertEquals(testRecord2Plus2.getComputedAPlusB(), (Integer)4);
        assertNull(testRecord3Plus7.getComputedAPlusB());
        assertEquals(testRecord8Plus9.getComputedAPlusB(), (Integer)17);
    }

    @Test
    
    public void testExceptions() throws Exception {
        assertNotNull(this.myAPlusBEngine);
        List<MyAPlusBSampleEntity> input = new ArrayList<MyAPlusBSampleEntity>();
        MyAPlusBSampleEntity test1 = new MyAPlusBSampleEntity(999,3);//999 throws exception
        input.add(test1);
        MyAPlusBSampleEntity test2 = new MyAPlusBSampleEntity(2,999);//999 throws exception
        input.add(test2);
        this.myAPlusBEngine.startEngineWithInputData(input);
    }
}
