package com.aa.crewcomp.orionjdbc.config;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.DefaultAzureCredential;
import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;


@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
public class DataSourceConfigTests {
    @InjectMocks
    private DataSourceConfig dataSourceConfig;

    @Mock
    private SecretClientBuilder secretClientBuilder;

    @Mock
    private SecretClient secretClient;

    @Mock
    private DefaultAzureCredential defaultAzureCredential;

    @Mock
    private KeyVaultSecret keyVaultSecret;

    @Mock
    private DefaultAzureCredentialBuilder defaultAzureCredentialBuilder;

    @Test
    public void test() throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        System.out.println(dataSourceConfig);
        when(secretClientBuilder.vaultUrl(anyString())).thenReturn(secretClientBuilder);
        when(defaultAzureCredentialBuilder.build()).thenReturn(defaultAzureCredential);
        when(secretClientBuilder.buildClient()).thenReturn(secretClient);
        when(secretClientBuilder.credential(any(TokenCredential.class))).thenReturn(secretClientBuilder);
        when(secretClient.getSecret(anyString())).thenReturn(keyVaultSecret);
        when(keyVaultSecret.getValue()).thenReturn("test");
        JdbcTemplate jdbcTemplate  = dataSourceConfig.dataSource();
        Assertions.assertNotNull(jdbcTemplate);
    }

    @BeforeEach
    public void setDataSourceConfig(){
//        dataSourceConfig = new DataSourceConfig();
        ReflectionTestUtils.setField(dataSourceConfig, "keyVaultUrl", "test-keyVault");
        ReflectionTestUtils.setField(dataSourceConfig, "url", "test-databricks-url");
        ReflectionTestUtils.setField(dataSourceConfig, "clientIdKey", "test-id");
        ReflectionTestUtils.setField(dataSourceConfig, "secretKey", "test-sec");
        ReflectionTestUtils.setField(dataSourceConfig, "httpPathKey", "test-url-http-path");
    }

}
