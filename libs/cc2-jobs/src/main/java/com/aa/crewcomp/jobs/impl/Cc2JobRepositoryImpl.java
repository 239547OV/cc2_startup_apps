package com.aa.crewcomp.jobs.impl;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.Date;

import com.aa.crewcomp.jobs.domain.Cc2ExceptionWrapper;
import com.aa.crewcomp.jobs.domain.Cc2Job;
import com.aa.crewcomp.jobs.interfaces.Cc2JobRepositoryInterface;

@Component
public class Cc2JobRepositoryImpl implements Cc2JobRepositoryInterface {
    @Autowired
    private MongoTemplate outputMongoTemplate;

    @Override
    public void saveJob(Cc2Job cc2Job) {
        this.outputMongoTemplate.save(cc2Job);
    }

    @Override
    public void updateJobStatus(Object id, String status) {
        Query query = new Query(where("_id").is(id));
        Update update = new Update().set("status", status);
        this.outputMongoTemplate.updateFirst(query, update, Cc2Job.class);
    }

    @Override
    public void updateJobTotalCount(Object id, Integer pageRecordsCount) {
        Query query = new Query(where("_id").is(id));
        Update update = new Update().set("jobTotalInputRecords", pageRecordsCount);
        this.outputMongoTemplate.updateFirst(query, update, Cc2Job.class);
    }

    @Override
    public void updateJobException(Object id, Cc2ExceptionWrapper cc2Exception) {
        Query query = new Query(where("_id").is(id));
        Update update = new Update().set("jobException", cc2Exception);
        this.outputMongoTemplate.updateFirst(query, update, Cc2Job.class);
    }

    @Override
    public void updateJobEndTime(Object id, Date date) {
        Query query = new Query(where("_id").is(id));
        Update update = new Update().set("jobEndTime", date);
        this.outputMongoTemplate.updateFirst(query, update, Cc2Job.class);
    }

    @Override
    public void incrementInsertedCount(ObjectId id, Integer count) {
        Query query = new Query(where("_id").is(id));
        Update update = new Update().inc("jobTotalInsertedRecords", count);
        this.outputMongoTemplate.updateFirst(query, update, Cc2Job.class);
    }

    @Override
    public void incrementIgnoredDuplicateCount(ObjectId id, Integer count) {
        Query query = new Query(where("_id").is(id));
        Update update = new Update().inc("jobTotalIgnoredRecords", count);
        this.outputMongoTemplate.updateFirst(query, update, Cc2Job.class);
    }
}