package com.bmh.lms.repository;

import com.bmh.lms.model.HistoryChain;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryChainRepository extends MongoRepository<HistoryChain, ObjectId> {
    HistoryChain findByRecId(String recId);
}
