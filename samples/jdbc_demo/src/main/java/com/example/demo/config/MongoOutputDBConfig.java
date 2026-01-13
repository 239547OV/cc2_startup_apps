package com.example.demo.config;

import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration

@EnableMongoRepositories(basePackages = "com.example.demo.repo.output",
        mongoTemplateRef = "outputMongoTemplate")
public class MongoOutputDBConfig {
	@Autowired
	private ApplicationContext applicationContext;

	@Value("${output-data}")
	private String outputDatabaseName;

	@Primary
	@Bean("outputMongoTemplate")
	public MongoTemplate getOutputMongoTemplate(){
		return new MongoTemplate(applicationContext.getBean("mongoClient", MongoClient.class), this.outputDatabaseName);
	}
}