package com.aa.crewcomp.jobs.abstr;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import com.aa.crewcomp.jobs.impl.Cc2JobRepositoryImpl;
import com.aa.crewcomp.jobs.interfaces.InputDatabaseConnectorInterface;
import com.aa.crewcomp.jobs.mock.MyEntity;
import com.aa.crewcomp.jobs.mock.MyJobsExecutor;
import com.aa.crewcomp.jobs.mock.MyJobsExecutorException;

import org.junit.runner.RunWith;

@RunWith(MockitoJUnitRunner.class)
public class JobsExecutorAbstractTests {
    @InjectMocks
    private MyJobsExecutor myJobsExecutor;

    @InjectMocks
    private MyJobsExecutorException myJobsExecutorException;

    @Mock
    private InputDatabaseConnectorInterface<MyEntity> inputDatabaseConnectorImplementation;

    @Mock
    private Cc2JobRepositoryImpl cc2JobRepository;

    @BeforeEach
    public void setUp(){
        this.inputDatabaseConnectorImplementation = mock(InputDatabaseConnectorInterface.class);
        this.cc2JobRepository = mock(Cc2JobRepositoryImpl.class);
        this.myJobsExecutor = new MyJobsExecutor(this.inputDatabaseConnectorImplementation, cc2JobRepository);
        this.myJobsExecutorException = new MyJobsExecutorException(this.inputDatabaseConnectorImplementation, cc2JobRepository);
        
    }


    @Test
    public void testMyJobsExecutor(){
        List<Integer> results = new ArrayList<Integer>();
        results.add(1);
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(), any()) ).thenReturn(results);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(100);
        when(this.inputDatabaseConnectorImplementation.query(any())).thenReturn(new ArrayList<MyEntity>());
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutor.execute();
    }

    @Test
    public void testMyJobsExecutorException(){
        List<Integer> results = new ArrayList<Integer>();
        results.add(1);
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(), any()) ).thenReturn(results);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(100);
        when(this.inputDatabaseConnectorImplementation.query(any())).thenReturn(new ArrayList<MyEntity>());
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutorException.execute();
    }

    @Test
    public void testExecuteWithMultiplePages(){
        List<Integer> results = new ArrayList<Integer>();
        for(int i = 0; i < 25; i++){
            results.add(i);
        }
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(), any())).thenReturn(results);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(250);
        when(this.inputDatabaseConnectorImplementation.query(any())).thenReturn(new ArrayList<MyEntity>());
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutor.execute();
        verify(this.cc2JobRepository, times(3)).saveJob(any());
    }

    @Test
    public void testExecuteWithEmptyGroupIds(){
        List<Integer> results = new ArrayList<Integer>();
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(), any())).thenReturn(results);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(0);
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutor.execute();
        verify(this.cc2JobRepository, times(0)).saveJob(any());
    }

    @Test
    public void testExecuteWithSinglePage(){
        List<Integer> results = new ArrayList<Integer>();
        for(int i = 0; i < 5; i++){
            results.add(i);
        }
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(),any())).thenReturn(results);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(50);
        when(this.inputDatabaseConnectorImplementation.query(any())).thenReturn(new ArrayList<MyEntity>());
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutor.execute();
        verify(this.cc2JobRepository, times(1)).saveJob(any());
        verify(this.cc2JobRepository, times(1)).updateJobEndTime(any(), any());
    }

    @Test
    public void testExecuteWithExactPageSize(){
        List<Integer> results = new ArrayList<Integer>();
        for(int i = 0; i < 10; i++){
            results.add(i);
        }
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(),any())).thenReturn(results);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(100);
        when(this.inputDatabaseConnectorImplementation.query(any())).thenReturn(new ArrayList<MyEntity>());
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutor.execute();
        verify(this.cc2JobRepository, times(1)).saveJob(any());
    }

    @Test
    public void testExecuteWithExceptionVerifiesExceptionUpdate(){
        List<Integer> results = new ArrayList<Integer>();
        results.add(1);
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(), any())).thenReturn(results);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(10);
        when(this.inputDatabaseConnectorImplementation.query(any())).thenReturn(new ArrayList<MyEntity>());
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutorException.execute();
        verify(this.cc2JobRepository, times(1)).updateJobException(any(), any());
    }

    @Test
    public void testExecuteWithMultiplePagesVerifyAllProcessed(){
        List<Integer> results = new ArrayList<Integer>();
        for(int i = 0; i < 30; i++){
            results.add(i);
        }
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(), any())).thenReturn(results);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(300);
        when(this.inputDatabaseConnectorImplementation.query(any())).thenReturn(new ArrayList<MyEntity>());
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutor.execute();
        verify(this.cc2JobRepository, times(3)).saveJob(any());
        verify(this.cc2JobRepository, times(3)).updateJobEndTime(any(), any());
    }

    @Test
    public void testExecuteWithNullGroupIds(){
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(), any())).thenReturn(null);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(0);
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutor.execute();
        verify(this.cc2JobRepository, times(0)).saveJob(any());
        verify(this.cc2JobRepository, times(0)).updateJobStatus(any(), any());
        verify(this.cc2JobRepository, times(0)).updateJobTotalCount(any(), any());
    }

    @Test
    public void testExecuteEarlyReturnWithEmptyGroupIds(){
        List<Integer> results = new ArrayList<Integer>();
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any(), any())).thenReturn(results);
        when(this.inputDatabaseConnectorImplementation.count(any())).thenReturn(0);
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutor.execute();
        verify(this.cc2JobRepository, times(0)).saveJob(any());
        verify(this.cc2JobRepository, times(0)).updateJobStatus(any(), any());
        verify(this.cc2JobRepository, times(0)).updateJobEndTime(any(), any());
    }
}
