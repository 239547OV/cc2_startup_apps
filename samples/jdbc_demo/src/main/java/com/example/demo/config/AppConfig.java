package com.example.demo.config;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;

@Configuration
public class AppConfig {
    @Value("${mongodb-conn-string}")
	private String connStringKV;
	
	private static final int MAX_CONN_POOL_SIZE = 20;
	private static final int MIN_CONN_POOL_SIZE = 1;
	private static final long MAX_CONN_IDLE_TIME_MINS = 15;
	private static final long MAX_CONN_LIFE_TIME_MINS = 10;
	private static final int CONNECT_TIMEOUT_MS = 30000;
	
	@Bean("mongoClient")
	public MongoClient getMongoClient() {
		ConnectionString connectionString = new ConnectionString(connStringKV);
		
		ConnectionPoolSettings connPoolSettings = ConnectionPoolSettings.builder()
				.applyConnectionString(connectionString)
				.maxSize(MAX_CONN_POOL_SIZE)
				.minSize(MIN_CONN_POOL_SIZE)
				.maxConnectionLifeTime(MAX_CONN_LIFE_TIME_MINS, TimeUnit.MINUTES)
				.maxConnectionIdleTime(MAX_CONN_IDLE_TIME_MINS, TimeUnit.MINUTES)
				.build();
		 MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
		         .applyConnectionString(connectionString)
		         .applyToConnectionPoolSettings(builder -> builder.applySettings(connPoolSettings))
		         .applyToSocketSettings(builder -> {
		        	 builder.connectTimeout(CONNECT_TIMEOUT_MS, TimeUnit.MILLISECONDS)
		                .applyConnectionString(connectionString)
		                .build();
		            })
		         .retryReads(true)
		         .retryWrites(true)
		         .build();
		return MongoClients.create(mongoClientSettings);

	}
}
