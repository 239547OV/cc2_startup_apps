package com.example.demo.impl;

import java.util.List;

import com.aa.crewcomp.jobs.interfaces.InputDatabaseConnectorInterface;
import com.example.demo.domain.MyEntity;

public class OrionConnectionInterfaceImpl implements InputDatabaseConnectorInterface<MyEntity>{

    @Override
    public List<MyEntity> query(Object query) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'query'");
    }

    @Override
    public List<Integer> queryGroupIds(Object query, String groupIdColumnName) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'queryGroupIds'");
    }

    @Override
    public Object getPageQuery(Object query, String groupIdColumnName, Integer leftId, Integer rightId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPageQuery'");
    }

    @Override
    public Integer count(Object query) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'count'");
    }

    
}
