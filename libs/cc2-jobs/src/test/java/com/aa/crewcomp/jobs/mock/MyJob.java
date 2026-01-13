package com.aa.crewcomp.jobs.mock;

import java.util.Date;
import com.aa.crewcomp.jobs.domain.Cc2Job;

public class MyJob extends Cc2Job {
    public MyJob(Date containerTimestamp, Integer page, Integer pages, String groupIdColumnName, Integer groupIdNbrGte,
            Integer groupIdNbrLt, Integer totalInputRecords) {
        super(containerTimestamp, page, pages, groupIdColumnName, groupIdNbrGte, groupIdNbrLt, totalInputRecords);
    }
}
