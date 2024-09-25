package com.bmh.lms.repository;

import com.bmh.lms.model.ChainDeedDataCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecordCollectionRepository extends MongoRepository<ChainDeedDataCollection, String> {
}
