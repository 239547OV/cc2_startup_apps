package com.aa.crewcomp.jobs.mock;

import java.util.List;

import org.springframework.stereotype.Component;
import com.aa.crewcomp.jobs.abstr.JobsExecutorAbstract;
import com.aa.crewcomp.jobs.impl.Cc2JobRepositoryImpl;
import com.aa.crewcomp.jobs.interfaces.InputDatabaseConnectorInterface;

@Component
public class MyJobsExecutor extends JobsExecutorAbstract<MyJob, MyEntity> {
    private static final Object mainQuery = "TEST"; 
    private static final String pageGroupAttributeName = "EMP_NBR";
    private static final Integer maxNumberOfGroupIdsPerPage = 10;
    public MyJobsExecutor(InputDatabaseConnectorInterface<MyEntity> databaseInterfaceImplementation, Cc2JobRepositoryImpl cc2JobRepository) {
        super(databaseInterfaceImplementation, cc2JobRepository, mainQuery, pageGroupAttributeName, maxNumberOfGroupIdsPerPage);
    }

    @Override
    public void processPageRecords(List<MyEntity> pageRecords) {
        
    }
   
}
