package com.example.demo.engines;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.aa.crewcomp.rulesengine.core.CustomEngineAbstract;
import com.example.demo.domain.MyEntity;
import com.example.demo.rules.MyRule;
import com.example.demo.rules.SaveToMongoDBRule;

@Component
public class MainEngine extends CustomEngineAbstract<MyEntity, Object> {

    @Autowired
    private SaveToMongoDBRule saveToMongoDBRule;

    @Override
    public void addCustomRules() {
        this.addCustomRule(new MyRule("MyRule", "Description of MyRule",    1));
        this.addCustomRule(saveToMongoDBRule);
    }
    
}
