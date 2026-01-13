package com.aa.crewcomp.jobs.abstr;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
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
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any()) ).thenReturn(results);
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutor.execute();
    }

    @Test
    public void testMyJobsExecutorException(){
        List<Integer> results = new ArrayList<Integer>();
        results.add(1);
        when(this.inputDatabaseConnectorImplementation.queryGroupIds(any()) ).thenReturn(results);
        MockitoAnnotations.openMocks(this);
        this.myJobsExecutorException.execute();
    }
}
