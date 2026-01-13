package com.aa.crewcomp.orionjdbc.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.aa.crewcomp.orionjdbc.mock.config.MyConfig;

@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { MyConfig.class })
public class OrionDataSourceServiceTests {
    class MyPojo{

    }
    @InjectMocks
    OrionDataSourceService orionDataSourceService;
    
    @Mock
    private JdbcTemplate orionDataSource;

    @Test
    void testExecuteQuery() {
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                return null;
            }
        };
        Mockito.doReturn(new ArrayList<>()).when(this.orionDataSource).query(any(PreparedStatementCreator.class), any(BeanPropertyRowMapper.class));
        this.orionDataSourceService.query(psc, MyPojo.class);
    }
}
