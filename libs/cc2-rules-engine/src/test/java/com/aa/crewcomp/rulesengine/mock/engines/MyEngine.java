package com.aa.crewcomp.rulesengine.mock.engines;

import org.springframework.stereotype.Component;

import com.aa.crewcomp.rulesengine.core.CustomEngineAbstract;
import com.aa.crewcomp.rulesengine.mock.domain.MySampleEntity;
import com.aa.crewcomp.rulesengine.mock.rules.MyRule;

@Component
public class MyEngine extends CustomEngineAbstract<MySampleEntity, Object> {
    @Override
    public void addCustomRules() {
        this.addCustomRule(new MyRule("My rule name", "my rule description", 1));
        this.addCustomRule(new MyRule("My rule name same", "my rule description", 1));
        this.addCustomRule(new MyRule("My rule name 2", "my rule description 2", 2));
    }
}
