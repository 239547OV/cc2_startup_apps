package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = { "com.example.demo.service", 
"com.example.demo.repo.output",
"com.example.demo.config",
"com.example.demo.rules",
"com.example.demo.engines",
"com.aa.crewcomp.orionjdbc", 
"com.aa.crewcomp.rulesengine"})
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
