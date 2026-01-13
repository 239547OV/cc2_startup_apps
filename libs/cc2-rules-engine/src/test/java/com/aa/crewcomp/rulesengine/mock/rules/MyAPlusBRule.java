package com.aa.crewcomp.rulesengine.mock.rules;

import org.jeasy.rules.api.Facts;
import org.springframework.stereotype.Component;
import com.aa.crewcomp.rulesengine.core.CustomRuleAbstract;
import com.aa.crewcomp.rulesengine.mock.domain.MyAPlusBSampleEntity;

@Component
public class MyAPlusBRule extends CustomRuleAbstract<MyAPlusBSampleEntity, Object> {
    public MyAPlusBRule(int priority) {
        super("Sum if A is pair", "If A is pair then compute A + B", priority);
    }

    @Override
    public boolean condition(Facts facts) {
        if( ((Integer)facts.get("a")).equals(999))
            throw new RuntimeException("TEST CONDITION EXCEPTION");
        return (Integer)facts.get("a") % 2 == 0;
    }

    @Override
    public void action(MyAPlusBSampleEntity currentRecord, Object parentRecord) {
        if( currentRecord.getB().equals(999))
            throw new RuntimeException("TEST ACTION EXCEPTION");
        currentRecord.setComputedAPlusB(currentRecord.getA() + currentRecord.getB());
    }

    @Override
    public void handleConditionException(MyAPlusBSampleEntity currentRow, Exception e) {
        return;
    }

    @Override
    public void handleActionException(MyAPlusBSampleEntity currentRow, Exception e) {
        return;
    }
}
