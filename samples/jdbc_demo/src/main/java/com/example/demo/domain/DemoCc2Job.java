package com.example.demo.domain;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.aa.crewcomp.jobs.domain.Cc2Job;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "demo_cc2_jobs")
public class DemoCc2Job extends Cc2Job {
    @Id
    private ObjectId id;

    DemoCc2Job(Date containerTimestamp, Integer page, Integer pages, String groupIdColumnName,
            Integer groupIdNbrGte, Integer groupIdNbrLt, Integer totalInputRecords){
        super(containerTimestamp, page, pages, groupIdColumnName, groupIdNbrGte, groupIdNbrLt, totalInputRecords);
    }
    
}
