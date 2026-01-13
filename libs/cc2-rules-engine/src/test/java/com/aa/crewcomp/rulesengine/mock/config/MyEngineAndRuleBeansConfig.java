package com.aa.crewcomp.rulesengine.mock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;

import com.aa.crewcomp.rulesengine.mock.engines.MyAPlusBEngine;
import com.aa.crewcomp.rulesengine.mock.engines.MyEngine;
import com.aa.crewcomp.rulesengine.mock.service.MySimpleService;

@TestPropertySource(locations = "/application.yml")
@Configuration
public class MyEngineAndRuleBeansConfig {
    @Bean("myEngine")
	public MyEngine getMyEngine(){
		return new MyEngine();
	}

	@Bean("myAPlusBEngine")
	public MyAPlusBEngine getMyAPlusBEngine(){
		return new MyAPlusBEngine();
	}

    @Bean("oneSimpleService")
	public MySimpleService getOneSimpleService(){
		return new MySimpleService();
	}
}
