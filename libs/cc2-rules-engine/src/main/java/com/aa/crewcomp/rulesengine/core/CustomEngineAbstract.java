package com.aa.crewcomp.rulesengine.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public abstract class CustomEngineAbstract<EntityClassType, ParentEntityClassType> {
	private static final Logger logger = LoggerFactory.getLogger(CustomEngineAbstract.class);
    private final String concreteClassName = this.getClass().getName();
    private List<CustomRuleAbstract<EntityClassType, ParentEntityClassType>> customRules;
    private DefaultRulesEngine rulesEngine;
    private Rules rules;
    protected ConcurrentMap<Facts, EntityClassType> mappedFacts;
    protected ParentEntityClassType parentRecord;
    public abstract void addCustomRules();

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    public CustomEngineAbstract() {
        this.customRules = new ArrayList<CustomRuleAbstract<EntityClassType, ParentEntityClassType>>();
        this.rulesEngine = new DefaultRulesEngine();
        this.rules = new Rules();
    }

    @EventListener(ContextRefreshedEvent.class)
    public void init(){
        this.addCustomRules();
		this.createCustomRuleBeansAndRegisterRules();
    }

    public void addCustomRule(CustomRuleAbstract<EntityClassType, ParentEntityClassType> customRule){
        this.customRules.add(customRule);
    }

    public void startEngineWithInputData(Iterable<EntityClassType> inputData) {
        logger.info(concreteClassName + " - Processing input data...");
        this.mappedFacts = new ConcurrentHashMap<Facts, EntityClassType>();
        inputData.forEach(row -> {
            this.doFire(row);
        });
        logger.info(concreteClassName + " has finished.");
    }

    private void createCustomRuleBeansAndRegisterRules(){
        int beanNumber = 0;
        for(CustomRuleAbstract<EntityClassType, ParentEntityClassType> customRule: this.customRules){
            customRule.customEngine = this;
            this.createCustomRuleBean(customRule, beanNumber++);
            this.rules.register(customRule.rule);
        }
    }

    private void createCustomRuleBean(CustomRuleAbstract<EntityClassType, ParentEntityClassType> customRule, int beanNumber) {
        this.applicationContext.getAutowireCapableBeanFactory().autowireBean(customRule);
        this.applicationContext.getBeanFactory().registerSingleton(
                this.getClass().getName() + "_" + customRule.getClass().getName() + "_" + beanNumber,
                customRule);
    }

    private void doFire(EntityClassType row) {
        Facts facts = new Facts();
        new FieldsToFacts<EntityClassType>().fieldsToFacts(facts, row);
        this.mappedFacts.put(facts, row);
        this.rulesEngine.fire(rules, facts);
        this.mappedFacts.remove(facts);
    }
}
