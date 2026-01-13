package com.aa.crewcomp.orionjdbc.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;
import com.aa.crewcomp.orionjdbc.domain.objects.OrionQueryStats;
import com.aa.crewcomp.orionjdbc.utils.ObjectSizeEstimator;

@Component
public class OrionRecordsExecutor {
    private static final Logger logger = LoggerFactory.getLogger(OrionRecordsExecutor.class);
    
    @Autowired
    private OrionDataSourceService orionDataSourceService;

    @Autowired
    private RuntimeUtils runtimeUtils;

    private PreparedStatement getPreparedStatement(Connection connection, String query, Object... params) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query);
        int i = 1;
        for(Object p: params){
            ps.setObject(i, p);
            i++;
        }
        return ps;
    }

    private PreparedStatement getPreparedStatementCount(Connection connection, String query, Object... params) throws SQLException {
        query = "SELECT COUNT(*) AS COUNT FROM (" + query.replace(";","") + ")";
        PreparedStatement ps = connection.prepareStatement(query);
        int i = 1;
        for(Object p: params){
            ps.setObject(i, p);
            i++;
        }
        return ps;
    }

    public <EntityClassType> List<EntityClassType> executeOrionQuery(String query, Class<EntityClassType> clazz, Object... params) {
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = getPreparedStatement(connection, query, params);
                return ps;
            }
        };
        return this.orionDataSourceService.query(psc, clazz);
    }

    public <EntityClassType> void verifyMemoryAvailability(String query, Class<EntityClassType> clazz, Object... params){
        logger.info("Executing query...");
        logger.info(query);
        logger.info("Params: ");
        for(Object p: params)
            logger.info(p.toString());
        Integer resultsCount = getOrionQueryCount(query, params);
        Integer estResultsSize =  resultsCount * ObjectSizeEstimator.estimateSize(clazz);
        logger.info("Total memory: " + this.runtimeUtils.getTotalMemory() + " B (" + this.runtimeUtils.getTotalMemory() / (1024 * 1024) + " MB)");
        logger.info("Free memory: " + this.runtimeUtils.getFreeMemory() + " B (" +  this.runtimeUtils.getFreeMemory() / (1024 * 1024) + " MB)");
        logger.info("Result set count: " + resultsCount);
        logger.info("Result set size: " + estResultsSize + " B (" + (estResultsSize / (1024 * 1024)) + " MB)");
        if(this.runtimeUtils.getFreeMemory()< estResultsSize)
            logger.warn("Possible Java heap exhaustion. This query result set size is greater than the free memory. Recommended free memory: " + estResultsSize + " B (" + (estResultsSize / (1024 * 1024)) + " MB)");
    }

    public Integer getOrionQueryCount(String query, Object... params) {
        PreparedStatementCreator psc = new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = getPreparedStatementCount(connection, query, params);
                return ps;
            }
        };
        return this.orionDataSourceService.query(psc, OrionQueryStats.class).get(0).getCount();
    }

}
