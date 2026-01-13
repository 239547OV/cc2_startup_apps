package com.aa.crewcomp.orionjdbc.config;

import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.databricks.client.jdbc.Driver;

@Configuration
@Import({SecretClientBuilder.class, DefaultAzureCredentialBuilder.class})
public class DataSourceConfig {

    @Value("${databricks-url}")
    private String url;
    @Value("${client-id}")
    private String clientIdKey;
    @Value("${secret-key}")
    private String secretKey;
    @Value("${databricks-url-http-path}")
    private String httpPathKey;
    @Value("${keyvault-url}")
    private String keyVaultUrl;
    private String jdbcUrl;
    @Autowired
    private SecretClientBuilder secretClientBuilder;
    @Autowired
    private DefaultAzureCredentialBuilder defaultAzureCredentialBuilder;

    @Bean
    public JdbcTemplate dataSource() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return getDevDataSource();
    }

    private JdbcTemplate getDevDataSource() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        SecretClient secretClient = secretClientBuilder.vaultUrl(keyVaultUrl)
                .credential(defaultAzureCredentialBuilder.build())
                .buildClient();
        jdbcUrl = secretClient.getSecret(url).getValue();
        String clientId = secretClient.getSecret(clientIdKey).getValue();
        String secret = secretClient.getSecret(secretKey).getValue();
        String httpPath = secretClient.getSecret(httpPathKey).getValue();


        Properties properties = new Properties();
        properties.put("httpPath", httpPath);
        properties.put("AuthMech", "11");
        properties.put("Auth_Flow", "1");
        properties.put("OAuth2ClientId", clientId);
        properties.put("OAuth2Secret", secret);
        properties.put("transportMode", "http");
        properties.put("ssl", "1");
        return getJdbcTemplate(properties);
    }

    public JdbcTemplate getJdbcTemplate(Properties properties) throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Class<?> driverClass = Class.forName("com.databricks.client.jdbc.Driver");
        Driver driver = (Driver) driverClass.getDeclaredConstructor().newInstance();
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource(driver, jdbcUrl, properties);
        return new JdbcTemplate(dataSource);
    }

}
