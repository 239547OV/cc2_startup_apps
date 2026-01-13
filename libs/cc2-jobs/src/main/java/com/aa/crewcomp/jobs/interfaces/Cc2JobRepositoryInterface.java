package com.aa.crewcomp.jobs.interfaces;

import java.util.Date;
import org.bson.types.ObjectId;
import com.aa.crewcomp.jobs.domain.Cc2ExceptionWrapper;
import com.aa.crewcomp.jobs.domain.Cc2Job;

public interface Cc2JobRepositoryInterface {
    void saveJob(Cc2Job cc2Job);
    void updateJobStatus(Object id, String string);
    void updateJobTotalCount(Object id, Integer pageRecordsCount);
    void updateJobException(Object id, Cc2ExceptionWrapper cc2Exception);
    void updateJobEndTime(Object id, Date date);
    void incrementInsertedCount(ObjectId jobId, Integer insertedCount);
    void incrementIgnoredDuplicateCount(ObjectId jobId, Integer duplicates);
}
