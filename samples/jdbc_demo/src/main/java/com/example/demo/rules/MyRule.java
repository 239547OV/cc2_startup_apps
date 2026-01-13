package com.example.demo.rules;

import java.util.HashMap;
import java.util.Objects;

import org.jeasy.rules.api.Facts;

import com.aa.crewcomp.jobs.domain.Cc2ExceptionWrapper;
import com.aa.crewcomp.rulesengine.core.CustomRuleAbstract;
import com.example.demo.domain.MyEntity;


public class MyRule extends CustomRuleAbstract<MyEntity, Object>{

    public MyRule(String name, String description, int priority) {
        super(name, description, priority);
    }

    @Override
    public void action(MyEntity currentRecord, Object parentRecord) {
        throw new UnsupportedOperationException("Unimplemented method 'action'");
    }

    @Override
    public boolean condition(Facts facts) {
        throw new UnsupportedOperationException("Unimplemented method 'condition'");
    }
@Override
    public void handleConditionException(MyEntity currentRow, Exception e) {
        this.setCurrentRecordExceptions(currentRow, e);
    }

    @Override
    public void handleActionException(MyEntity currentRow, Exception e) {
        this.setCurrentRecordExceptions(currentRow, e);
    }

    private void setCurrentRecordExceptions(MyEntity currentRow, Exception e) {
        if(Objects.isNull(currentRow.getRulesExceptions()))
            currentRow.setRulesExceptions(new HashMap<String, Cc2ExceptionWrapper>());
        currentRow.getRulesExceptions().put(concreteClassName.replaceAll("\\.", "_"), new Cc2ExceptionWrapper(e));
    }
}
