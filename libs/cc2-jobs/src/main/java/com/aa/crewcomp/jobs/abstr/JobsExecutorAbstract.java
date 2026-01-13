package com.aa.crewcomp.jobs.abstr;

import com.aa.crewcomp.jobs.domain.Cc2ExceptionWrapper;
import com.aa.crewcomp.jobs.domain.Cc2Job;
import com.aa.crewcomp.jobs.domain.Cc2JobStatusEnum;
import com.aa.crewcomp.jobs.impl.Cc2JobRepositoryImpl;
import com.aa.crewcomp.jobs.interfaces.InputDatabaseConnectorInterface;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JobsExecutorAbstract<JobType extends Cc2Job, InputRecordType> {
    private static final Logger logger = LoggerFactory.getLogger(JobsExecutorAbstract.class);
    public abstract void processPageRecords(List<InputRecordType> pageRecords);
    private String pageGroupAttributeName;
    private Integer maxNumberOfGroupIdsPerPage;
    private Date startTime;
    private Object mainQuery;
    private List<Integer> groupIds;
    private Integer pages;
    private Integer totalCount;

    private InputDatabaseConnectorInterface<InputRecordType> inputDatabaseConnectorImplementation;
    private Cc2JobRepositoryImpl cc2JobRepository;

    public JobsExecutorAbstract(InputDatabaseConnectorInterface<InputRecordType> inputDatabaseConnectorImplementation, 
        Cc2JobRepositoryImpl cc2JobRepository,
        Object mainQuery, String pageGroupAttributeName, Integer maxNumberOfGroupIdsPerPage) {
        this.inputDatabaseConnectorImplementation = inputDatabaseConnectorImplementation;
        this.cc2JobRepository = cc2JobRepository;
        this.mainQuery = mainQuery;
        this.pageGroupAttributeName = pageGroupAttributeName;
        this.maxNumberOfGroupIdsPerPage = maxNumberOfGroupIdsPerPage;
    }

    public void execute() {
        logger.info("Started new process.");
        this.totalCount = this.inputDatabaseConnectorImplementation.count(this.mainQuery);
        this.groupIds = this.inputDatabaseConnectorImplementation.queryGroupIds(
                this.inputDatabaseConnectorImplementation.getAllGroupIdsQuery(this.mainQuery));
        if(this.groupIds == null || this.groupIds.isEmpty()) {
            logger.info("No data to process. Finishing app...");
            return;
        }
        int leftIndx = 0;
        int rightIndx = this.maxNumberOfGroupIdsPerPage >= groupIds.size() ? groupIds.size() - 1
                : this.maxNumberOfGroupIdsPerPage;
        int page = 0;
        this.pages = (int) Math.ceil(Double.valueOf(groupIds.size()) / Double.valueOf(this.maxNumberOfGroupIdsPerPage));
        do {
            logger.info("Processing page " + (page + 1) + " of " + this.pages + "...");
            Integer leftIndxValue = this.groupIds.get(leftIndx);
            Integer rightIndxValue = this.groupIds.get(rightIndx);
            Object pageQuery = this.inputDatabaseConnectorImplementation.getPageQuery(
                    this.mainQuery,
                    leftIndxValue,
                    rightIndxValue);
            this.executePageJob(pageQuery, page, leftIndxValue,
                    rightIndxValue);
            leftIndx += this.maxNumberOfGroupIdsPerPage;
            rightIndx = leftIndx + this.maxNumberOfGroupIdsPerPage;
            if (rightIndx > groupIds.size() - 1)
                rightIndx = groupIds.size() - 1;
            page += 1;
        } while (leftIndx < groupIds.size());
    }

    private void executePageJob(Object pageQuery, Integer page, Integer leftIndxValue,
            Integer rightIndxValue) {
        Cc2Job cc2Job = new Cc2Job(this.startTime, page, this.pages, this.pageGroupAttributeName, leftIndxValue,
                rightIndxValue, this.totalCount);
        cc2Job.setJobStartTime(new Date());
        this.cc2JobRepository.saveJob(cc2Job);
        this.cc2JobRepository.updateJobStatus(cc2Job.getId(), Cc2JobStatusEnum.QUERYING.toString());
        logger.info("Querying page " + (page + 1) + " of " + this.pages + "...");
        Integer pageRecordsCount = this.inputDatabaseConnectorImplementation.count(pageQuery);
        this.cc2JobRepository.updateJobTotalCount(cc2Job.getId(), pageRecordsCount);
        logger.info("Query returned " + pageRecordsCount + " records. Processing page...");
        List<InputRecordType> pageRecords = this.inputDatabaseConnectorImplementation.query(pageQuery);
        try {
            this.cc2JobRepository.updateJobStatus(cc2Job.getId(), Cc2JobStatusEnum.EXECUTING_RULES_ENGINE.toString());
            this.processPageRecords(pageRecords);
        } catch (Exception e) {
            logger.warn("An error occurred while processing page " + (page + 1) + " of " + this.pages + ".", e);
            this.cc2JobRepository.updateJobException(cc2Job.getId(), new Cc2ExceptionWrapper(e));
        } finally {
            this.cc2JobRepository.updateJobStatus(cc2Job.getId(), Cc2JobStatusEnum.QUERYING.toString());
            logger.info("Finished processing page " + (page + 1) + " of " + this.pages + ".");
            this.cc2JobRepository.updateJobEndTime(cc2Job.getId(), new Date());
        }
    }
}
