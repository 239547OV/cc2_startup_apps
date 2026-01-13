package com.aa.crewcomp.jobs.domain;

import java.util.Date;
import lombok.Data;

@Data
public class Cc2Job {
    private Object id;
    private Date containerStartTimestamp;
    private Integer totalInputRecords;
    private Integer page;
    private Integer pages;
    private String groupIdColumnName;
    private Integer groupIdNbrGte;
    private Integer groupIdNbrLt;
    private Date jobStartTime;
    private Date jobEndTime;
    private Integer jobTotalInputRecords;
    private Integer jobTotalInsertedRecords;
    private Integer jobTotalIgnoredDuplicateRecords;
    private Cc2ExceptionWrapper jobException;
    private String status;

    public Cc2Job(Date containerTimestamp, Integer page, Integer pages, String groupIdColumnName,
            Integer groupIdNbrGte, Integer groupIdNbrLt, Integer totalInputRecords) {
        this.containerStartTimestamp = containerTimestamp;
        this.page = page;
        this.pages = pages;
        this.groupIdColumnName = groupIdColumnName;
        this.groupIdNbrGte = groupIdNbrGte;
        this.groupIdNbrLt = groupIdNbrLt;
        this.totalInputRecords = totalInputRecords;
        this.jobTotalInsertedRecords = 0;
        this.jobTotalIgnoredDuplicateRecords = 0;
        this.status = Cc2JobStatusEnum.STARTED.toString();
    }
}
