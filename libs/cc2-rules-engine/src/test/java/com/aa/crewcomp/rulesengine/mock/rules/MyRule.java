package com.aa.crewcomp.rulesengine.mock.rules;

import org.jeasy.rules.api.Facts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aa.crewcomp.rulesengine.core.CustomRuleAbstract;
import com.aa.crewcomp.rulesengine.mock.domain.MySampleEntity;
import com.aa.crewcomp.rulesengine.mock.service.MySimpleService;

@Component
public class MyRule extends CustomRuleAbstract<MySampleEntity, Object> {
    public MyRule(String name, String description, int priority) {
        super(name, description, priority);
    }

    @Autowired
    MySimpleService mySimpleService;

    @Override
    public boolean condition(Facts facts) {
        return true;
    }

    @Override
    public void action(MySampleEntity currentRecord, Object parentRecord) {
        currentRecord.setComputedValue(mySimpleService.returnExpectedValue());
    }

    @Override
    public void handleConditionException(MySampleEntity currentRow, Exception e) {
        RuntimeException re = (RuntimeException)e;
        throw re;
    }

    @Override
    public void handleActionException(MySampleEntity currentRow, Exception e) {
        RuntimeException re = (RuntimeException)e;
        throw re;
    }
}
