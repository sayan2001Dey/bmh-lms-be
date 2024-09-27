package com.bmh.lms.repository;

import com.bmh.lms.model.DeedCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeedCollectionRepository extends MongoRepository<DeedCollection, String> {
}
