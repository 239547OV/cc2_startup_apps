package com.aa.crewcomp.orionjdbc.mock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.TestPropertySource;


@TestPropertySource(locations = "/application.yml")
@Configuration
public class MyConfig {
}
