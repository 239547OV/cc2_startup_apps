package com.aa.crewcomp.jobs.mock;

import com.aa.crewcomp.jobs.abstr.Cc2HashedEntity;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class MyHashedEntity extends Cc2HashedEntity {
    private Integer fieldA;
    private String fieldB;
    private SubObject subObject;

    @Override
    public String hashIdFunction() {
        return this.fieldA + "-" + this.fieldB;
    }

    @Override
    public void modifyNodeBeforeUniqueHashing(ObjectNode node) {
        node.remove("fieldB");
        ((ObjectNode) node.get("subObject")).remove("excludeSubField");
    }

    public Integer getFieldA() {
        return fieldA;
    }

    public void setFieldA(Integer fieldA) {
        this.fieldA = fieldA;
    }

    public String getFieldB() {
        return fieldB;
    }

    public void setFieldB(String fieldB) {
        this.fieldB = fieldB;
    }

    public SubObject getSubObject() {
        return subObject;
    }

    public void setSubObject(SubObject subObject) {
        this.subObject = subObject;
    }

}
