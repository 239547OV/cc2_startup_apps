package com.aa.crewcomp.orionjdbc.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.Assert.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import com.aa.crewcomp.orionjdbc.domain.objects.OrionQueryStats;
import com.aa.crewcomp.orionjdbc.mock.config.MyConfig;


@RunWith(PowerMockRunner.class)
@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { MyConfig.class })
public class OrionRecordsExecutorTests {
    class MyPojo{

    }
    @InjectMocks
    private OrionRecordsExecutor orionRecordsExecutor;

    @Mock
    private OrionDataSourceService orionDataSourceService;

    @Mock
    private RuntimeUtils runtimeUtils;

    @Test
    public void testNoPreviousJobNoResults() throws Exception {
        List<OrionQueryStats> stats= new ArrayList<OrionQueryStats>();
        OrionQueryStats stat = new OrionQueryStats();
        stat.setCount(10);
        stats.add(stat);
        Mockito.when(this.orionDataSourceService.<OrionQueryStats>query(any(PreparedStatementCreator.class), eq(OrionQueryStats.class))).thenReturn(stats);
        Mockito.when(this.orionDataSourceService.<MyPojo>query(any(PreparedStatementCreator.class), eq(MyPojo.class))).thenReturn(new ArrayList<MyPojo>());
        this.orionRecordsExecutor.executeOrionQuery("", Object.class, new Date());

    }

    @Test
    public void testMemoryWarning() throws Exception {
        when(this.runtimeUtils.getFreeMemory()).thenReturn(1L);
        when(this.runtimeUtils.getTotalMemory()).thenReturn(1L);
        List<OrionQueryStats> stats= new ArrayList<OrionQueryStats>();
        OrionQueryStats stat = new OrionQueryStats();
        stat.setCount(100);
        stats.add(stat);
        Mockito.when(this.orionDataSourceService.<OrionQueryStats>query(any(PreparedStatementCreator.class), eq(OrionQueryStats.class))).thenReturn(stats);
        Mockito.when(this.orionDataSourceService.<MyPojo>query(any(PreparedStatementCreator.class), eq(MyPojo.class))).thenReturn(new ArrayList<MyPojo>());
        this.orionRecordsExecutor.verifyMemoryAvailability("", MyPojo.class, new Date());
    }

    @Test
    public void testMemoryAvailable() throws Exception {
        when(this.runtimeUtils.getFreeMemory()).thenReturn(1L);
        when(this.runtimeUtils.getTotalMemory()).thenReturn(1L);
        List<OrionQueryStats> stats= new ArrayList<OrionQueryStats>();
        OrionQueryStats stat = new OrionQueryStats();
        stat.setCount(0);
        stats.add(stat);
        Mockito.when(this.orionDataSourceService.<OrionQueryStats>query(any(PreparedStatementCreator.class), eq(OrionQueryStats.class))).thenReturn(stats);
        Mockito.when(this.orionDataSourceService.<MyPojo>query(any(PreparedStatementCreator.class), eq(MyPojo.class))).thenReturn(new ArrayList<MyPojo>());
        
        this.orionRecordsExecutor.verifyMemoryAvailability("", MyPojo.class, new Date());
    }

    @Test
    public void testPreparedStatement() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
       
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        List<OrionQueryStats> stats= new ArrayList<OrionQueryStats>();
        OrionQueryStats stat = new OrionQueryStats();
        stat.setCount(10);
        stats.add(stat);
        Mockito.when(this.orionDataSourceService.<OrionQueryStats>query(any(PreparedStatementCreator.class), eq(OrionQueryStats.class))).thenReturn(stats);
        Mockito.when(this.orionDataSourceService.<MyPojo>query(any(PreparedStatementCreator.class), eq(MyPojo.class))).thenReturn(new ArrayList<MyPojo>());
        
        ArgumentCaptor<PreparedStatementCreator> captor = ArgumentCaptor.forClass(PreparedStatementCreator.class);
        this.orionRecordsExecutor.executeOrionQuery("", MyPojo.class, new Date());
        
        // Mockito.verify(orionDataSourceService, times(1)).query(captor.capture(), eq(OrionQueryStats.class));
        // PreparedStatementCreator capturedPsc = captor.getValue();
        // PreparedStatement createdPs = capturedPsc.createPreparedStatement(connection);
        // assertNotNull(createdPs);

        Mockito.verify(orionDataSourceService, times(1)).query(captor.capture(), eq(MyPojo.class));
        PreparedStatementCreator capturedPsc2 = captor.getValue();
        PreparedStatement createdPs2 = capturedPsc2.createPreparedStatement(connection);
        assertNotNull(createdPs2);
    }

    @Test
    public void testPreparedStatementStats() throws Exception {
        Connection connection = Mockito.mock(Connection.class);
        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
       
        Mockito.when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        
        List<OrionQueryStats> stats= new ArrayList<OrionQueryStats>();
        OrionQueryStats stat = new OrionQueryStats();
        stat.setCount(10);
        stats.add(stat);
        Mockito.when(this.orionDataSourceService.<OrionQueryStats>query(any(PreparedStatementCreator.class), eq(OrionQueryStats.class))).thenReturn(stats);
        Mockito.when(this.orionDataSourceService.<MyPojo>query(any(PreparedStatementCreator.class), eq(MyPojo.class))).thenReturn(new ArrayList<MyPojo>());
        
        ArgumentCaptor<PreparedStatementCreator> captor = ArgumentCaptor.forClass(PreparedStatementCreator.class);
        this.orionRecordsExecutor.verifyMemoryAvailability("", MyPojo.class, new Date());
        
        Mockito.verify(orionDataSourceService, times(1)).query(captor.capture(), eq(OrionQueryStats.class));
        PreparedStatementCreator capturedPsc = captor.getValue();
        PreparedStatement createdPs = capturedPsc.createPreparedStatement(connection);
        assertNotNull(createdPs);
    }

    

}