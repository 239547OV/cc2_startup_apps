package com.aa.crewcomp.rulesengine.mock.engines;

import org.springframework.stereotype.Component;

import com.aa.crewcomp.rulesengine.core.CustomEngineAbstract;
import com.aa.crewcomp.rulesengine.mock.domain.MyAPlusBSampleEntity;
import com.aa.crewcomp.rulesengine.mock.rules.MyAPlusBRule;

@Component
public class MyAPlusBEngine extends CustomEngineAbstract<MyAPlusBSampleEntity, Object> {
    @Override
    public void addCustomRules() {
        this.addCustomRule(new MyAPlusBRule(1));
    }
}
