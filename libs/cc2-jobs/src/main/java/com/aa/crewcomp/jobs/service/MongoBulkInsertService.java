package com.aa.crewcomp.jobs.service;

import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.BulkOperationException;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.aa.crewcomp.jobs.interfaces.Cc2JobRepositoryInterface;
import com.mongodb.bulk.BulkWriteError;
import com.mongodb.bulk.BulkWriteResult;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Date;

public class MongoBulkInsertService<EntityClassType> {
    private static final Logger logger = LoggerFactory.getLogger(MongoBulkInsertService.class);
    private final Class<EntityClassType> entityClassType;
    @Autowired
    private MongoTemplate outputMongoTemplate;

    @Autowired
    private Cc2JobRepositoryInterface cc2JobRepository;

    public final int CHUNK_SIZE = 5000;
    private List<EntityClassType> buffer;
    private long minBulkAliveDurationMillis = 10000L;
    private long bulkStartMillis = new Date().getTime();

    public MongoBulkInsertService(Class<EntityClassType> entityClassType) {
        this.entityClassType = entityClassType;
        this.buffer = new ArrayList<EntityClassType>();
    }

    public void save(ObjectId jobId, EntityClassType input) {
        if (new Date().getTime() - bulkStartMillis > minBulkAliveDurationMillis) {
            this.flush(jobId);
            bulkStartMillis = new Date().getTime();
        }
        buffer.add(input);
        if (buffer.size() >= CHUNK_SIZE)
            this.performBulkInsert(jobId);
    }

    public void flush(ObjectId jobId) {
        if (buffer.size() == 0)
            return;
        this.performBulkInsert(jobId);
    }

    private void performBulkInsert(ObjectId jobId) {
        try {
            BulkOperations bulkOps = this.outputMongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED,
                    entityClassType);
            synchronized (buffer) {
                bulkOps.insert(buffer);
                BulkWriteResult results = bulkOps.execute();
                this.cc2JobRepository.incrementInsertedCount(jobId, results.getInsertedCount());
            }
        } catch (BulkOperationException e) {
            logger.warn("BulkOperationException was thrown.");
            logger.warn("If this is a reprocessing job, duplicates will be ignored.");
            this.cc2JobRepository.incrementInsertedCount(jobId, e.getResult().getInsertedCount());
            this.cc2JobRepository.incrementIgnoredDuplicateCount(jobId, e.getErrors().size());
            Map<Integer, Long> errorCodeCounts = e.getErrors().stream()
                .collect(Collectors.groupingBy(BulkWriteError::getCode, Collectors.counting()));
            logger.warn("Distinct BulkOperationException codes count {code=count}: ");
            logger.warn(errorCodeCounts.toString());
            logger.warn("Note: code 11000 is a duplicate key error.");
        }finally{
            buffer = new ArrayList<EntityClassType>();
        }
    }
}
