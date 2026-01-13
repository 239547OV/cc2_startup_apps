package com.aa.crewcomp.orionjdbc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Component;

@Component
public class OrionDataSourceService {
    @Autowired
    private JdbcTemplate orionDataSource;

    public <EntityClassType> List<EntityClassType> query(PreparedStatementCreator psc, Class<EntityClassType> clazz){
        return this.orionDataSource.query(psc, new BeanPropertyRowMapper<>(clazz));
    }
}
