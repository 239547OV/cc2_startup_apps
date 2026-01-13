package com.aa.crewcomp.jobs.interfaces;

import java.util.List;

public interface InputDatabaseConnectorInterface<InputRecordType> {
    public List<InputRecordType> query(Object query);
    public List<Integer> queryGroupIds(Object query, String groupIdColumnName);
    public Object getPageQuery(Object query, String groupIdColumnName, Integer leftId, Integer rightId);
    public Integer count(Object query);
}
