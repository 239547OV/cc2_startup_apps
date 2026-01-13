package com.example.demo.repo.output;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.demo.domain.MyEntity;

public interface MyEntityRepository extends MongoRepository<MyEntity, ObjectId>{

}