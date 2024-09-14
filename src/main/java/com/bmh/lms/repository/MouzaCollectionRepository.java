package com.bmh.lms.repository;

import com.bmh.lms.model.MouzaCollection;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MouzaCollectionRepository extends MongoRepository<MouzaCollection, String> {
}
