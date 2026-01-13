package com.example.demo.repo.output;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.domain.DemoCc2Job;

public interface DemoCc2JobRepository extends MongoRepository<DemoCc2Job, ObjectId> {

}