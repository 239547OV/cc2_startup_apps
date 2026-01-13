package com.example.demo.domain;

import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.aa.crewcomp.jobs.abstr.Cc2HashedEntity;
import com.aa.crewcomp.jobs.domain.Cc2ExceptionWrapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.Data;

@Data
@Document(collection = "my_entities")
public class MyEntity extends Cc2HashedEntity {
    private String name;
    private Integer budget;
    private Integer expenses;
    private ObjectId jobId;
    private Map<String, Cc2ExceptionWrapper> rulesExceptions;
    
    @Override
    public String hashIdFunction() {
        return this.name;
    }
    @Override
    public void modifyNodeBeforeUniqueHashing(ObjectNode node) {
        node.remove("budget");
        node.remove("expenses");
        node.remove("jobId");
        node.remove("rulesExceptions");
    }
    
}
