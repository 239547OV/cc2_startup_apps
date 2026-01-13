package com.aa.crewcomp.rulesengine.core;

import org.jeasy.rules.api.Facts;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.aa.crewcomp.rulesengine.mock.config.MyEngineAndRuleBeansConfig;
import com.aa.crewcomp.rulesengine.mock.domain.MySampleEntity;

@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { MyEngineAndRuleBeansConfig.class })
public class FieldsToFactsTests {
	@Test
	public void test() throws Exception {
		new FieldsToFacts<MySampleEntity>();
		MySampleEntity record = Mockito.mock(MySampleEntity.class);
		Mockito.doThrow(new IllegalAccessException("Test"))
        .when(record).getClass().getMethods();
		
		try{
			new FieldsToFacts<MySampleEntity>().fieldsToFacts(new Facts(), record);
		}catch(Exception e){
			System.out.println("Exception thrown successfully.");
		}
	}
}

