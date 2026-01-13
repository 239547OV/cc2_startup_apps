package com.example.demo.rules;

import org.jeasy.rules.api.Facts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.aa.crewcomp.jobs.service.MongoBulkInsertService;
import com.aa.crewcomp.rulesengine.core.CustomRuleAbstract;
import com.example.demo.domain.MyEntity;

class MyBulkInsertServiceImpl extends MongoBulkInsertService<MyEntity>{
        public MyBulkInsertServiceImpl(Class<MyEntity> entityClassType) {
            super(entityClassType);
        }
    }

/**
 * Rule to save MyEntity to MongoDB with hash generation and duplicate handling.
 * 
 * IMPORTANT: Before using this rule, create a unique index in MongoDB on the uniqueHash field:
 * db.my_entities.createIndex({ "uniqueHash": 1 }, { unique: true })
 */
@Component
public class SaveToMongoDBRule extends CustomRuleAbstract<MyEntity, Object> {

    private static final Logger logger = LoggerFactory.getLogger(SaveToMongoDBRule.class);

    @Autowired
    private MyBulkInsertServiceImpl myEntityBulkInsertService;

    public SaveToMongoDBRule() {
        super("SaveToMongoDBRule", "Saves MyEntity to MongoDB with hash regeneration and duplicate handling", 100);
    }

    @Override
    public boolean condition(Facts facts) {
        return true;
    }

    @Override
    @Transactional
    public void action(MyEntity currentRecord, Object parentRecord) {
        currentRecord.refreshHashIdAndUniqueHash();
        this.myEntityBulkInsertService.save(currentRecord.getJobId(), currentRecord);
    }

    @Override
    public void handleConditionException(MyEntity currentRow, Exception e) {
        logger.error("Exception occurred while evaluating condition for entity: {}. Error: {}", 
            currentRow != null ? currentRow.getName() : "null", e.getMessage(), e);
    }

    @Override
    public void handleActionException(MyEntity currentRow, Exception e) {
        logger.error("Exception occurred while saving entity: {} to MongoDB. Error: {}", 
            currentRow != null ? currentRow.getName() : "null", e.getMessage(), e);
        
        // Optionally rethrow if you want to stop processing
        // throw new RuntimeException("Failed to save entity to MongoDB", e);
    }
}
