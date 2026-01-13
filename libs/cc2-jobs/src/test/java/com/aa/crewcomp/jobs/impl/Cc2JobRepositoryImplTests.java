package com.aa.crewcomp.jobs.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.aa.crewcomp.jobs.domain.Cc2ExceptionWrapper;
import com.aa.crewcomp.jobs.domain.Cc2Job;

@RunWith(MockitoJUnitRunner.class)
public class Cc2JobRepositoryImplTests {
    
    @InjectMocks
    private Cc2JobRepositoryImpl cc2JobRepository;

    @Mock
    private MongoTemplate outputMongoTemplate;

    @BeforeEach
    public void setUp() {
        this.outputMongoTemplate = mock(MongoTemplate.class);
        this.cc2JobRepository = new Cc2JobRepositoryImpl();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveJob() {
        Cc2Job cc2Job = new Cc2Job(new Date(), 1, 10, "groupId", 0, 100, 1000);
        this.cc2JobRepository.saveJob(cc2Job);
        verify(this.outputMongoTemplate, times(1)).save(cc2Job);
    }

    @Test
    public void testUpdateJobStatus() {
        ObjectId id = new ObjectId();
        String status = "COMPLETED";
        this.cc2JobRepository.updateJobStatus(id, status);
        verify(this.outputMongoTemplate, times(1)).updateFirst(any(), any(), eq(Cc2Job.class));
    }

    @Test
    public void testUpdateJobTotalCount() {
        ObjectId id = new ObjectId();
        Integer pageRecordsCount = 500;
        this.cc2JobRepository.updateJobTotalCount(id, pageRecordsCount);
        verify(this.outputMongoTemplate, times(1)).updateFirst(any(), any(), eq(Cc2Job.class));
    }

    @Test
    public void testUpdateJobException() {
        ObjectId id = new ObjectId();
        Exception exception = new RuntimeException("Test exception");
        Cc2ExceptionWrapper cc2Exception = new Cc2ExceptionWrapper(exception);
        this.cc2JobRepository.updateJobException(id, cc2Exception);
        verify(this.outputMongoTemplate, times(1)).updateFirst(any(), any(), eq(Cc2Job.class));
    }

    @Test
    public void testUpdateJobEndTime() {
        ObjectId id = new ObjectId();
        Date endTime = new Date();
        this.cc2JobRepository.updateJobEndTime(id, endTime);
        verify(this.outputMongoTemplate, times(1)).updateFirst(any(), any(), eq(Cc2Job.class));
    }

    @Test
    public void testIncrementInsertedCount() {
        ObjectId id = new ObjectId();
        Integer count = 50;
        this.cc2JobRepository.incrementInsertedCount(id, count);
        verify(this.outputMongoTemplate, times(1)).updateFirst(any(), any(), eq(Cc2Job.class));
    }

    @Test
    public void testIncrementIgnoredDuplicateCount() {
        ObjectId id = new ObjectId();
        Integer count = 25;
        this.cc2JobRepository.incrementIgnoredDuplicateCount(id, count);
        verify(this.outputMongoTemplate, times(1)).updateFirst(any(), any(), eq(Cc2Job.class));
    }
}