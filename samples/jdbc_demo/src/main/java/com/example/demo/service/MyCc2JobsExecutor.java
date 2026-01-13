package com.example.demo.service;

import java.util.List;

import com.aa.crewcomp.jobs.abstr.JobsExecutorAbstract;
import com.aa.crewcomp.jobs.impl.Cc2JobRepositoryImpl;
import com.example.demo.domain.DemoCc2Job;
import com.example.demo.domain.MyEntity;
import com.example.demo.impl.OrionConnectionInterfaceImpl;

public class MyCc2JobsExecutor extends JobsExecutorAbstract<DemoCc2Job, MyEntity> {
    private static final Object mainQuery = "TEST"; 
    private static final String pageGroupAttributeName = "EMP_NBR";
    private static final Integer maxNumberOfGroupIdsPerPage = 10;
    public MyCc2JobsExecutor(OrionConnectionInterfaceImpl databaseInterfaceImplementation, Cc2JobRepositoryImpl cc2JobRepository) {
        super(databaseInterfaceImplementation, cc2JobRepository, mainQuery, pageGroupAttributeName, maxNumberOfGroupIdsPerPage);
    }

    @Override
    public void processPageRecords(List<MyEntity> pageRecords) {
        
    }
   
}
