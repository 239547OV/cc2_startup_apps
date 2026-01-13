package com.aa.crewcomp.rulesengine.core;

import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.RuleBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public abstract class CustomRuleAbstract<EntityClassType, ParentEntityClassType> {
	private static final Logger logger = LoggerFactory.getLogger(CustomRuleAbstract.class);
    protected final String concreteClassName = this.getClass().getName();
    public abstract boolean condition(Facts facts);
    public abstract void action(EntityClassType currentRecord, ParentEntityClassType parentRecord);
    protected CustomEngineAbstract<EntityClassType, ParentEntityClassType> customEngine;
    protected Rule rule;

    public abstract void handleConditionException(EntityClassType currentRow, Exception e);
    public abstract void handleActionException(EntityClassType currentRow, Exception e);
    
    public CustomRuleAbstract(String name, String description, int priority){
        this.rule =  new RuleBuilder()
                .name(name)
				.description(description)
				.priority(priority)
                .when(new CustomCondition(this))
                .then(new CustomAction(this))
                .build();
	}

    public class CustomCondition implements Condition {
        private CustomRuleAbstract<EntityClassType, ParentEntityClassType> customRule;
		
        @Override
		public boolean evaluate(Facts facts){
            logger.info(concreteClassName + " - condition - Evaluating condition for record: " + this.customRule.customEngine.mappedFacts.get(facts).toString());
			try{
                return customRule.condition(facts);
            }catch(Exception e){
                logger.error(concreteClassName + " - condition - ERROR FOR RECORD: " + this.customRule.customEngine.mappedFacts.get(facts).toString());
                customRule.handleConditionException(this.customRule.customEngine.mappedFacts.get(facts), e);
                throw e;
            }
        }
        
        public CustomCondition(CustomRuleAbstract<EntityClassType, ParentEntityClassType> customRule){
            this.customRule = customRule;
        }
	}

	public class CustomAction implements Action {
        private CustomRuleAbstract<EntityClassType, ParentEntityClassType> customRule;

		@Override
		public void execute(Facts facts){
            logger.info(concreteClassName + " - action - Executing action for record (before): " + this.customRule.customEngine.mappedFacts.get(facts).toString());
            try{
            this.customRule.action(this.customRule.customEngine.mappedFacts.get(facts), this.customRule.customEngine.parentRecord);
            }catch(Exception e){
                logger.error(concreteClassName + " - action - ERROR FOR RECORD: " + this.customRule.customEngine.mappedFacts.get(facts).toString());
                customRule.handleActionException(this.customRule.customEngine.mappedFacts.get(facts), e);
                throw e;
            }
            new FieldsToFacts<EntityClassType>().fieldsToFacts(facts, this.customRule.customEngine.mappedFacts.get(facts));
            logger.info(concreteClassName + " - action - Action executed for record (after): " + this.customRule.customEngine.mappedFacts.get(facts).toString());
		}

		public CustomAction(CustomRuleAbstract<EntityClassType, ParentEntityClassType> customRule){
            this.customRule = customRule;
		}
	}

}
