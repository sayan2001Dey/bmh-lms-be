package com.bmh.lms.repository;

import com.bmh.lms.model.HistoryChain;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoryChainRepository extends MongoRepository<HistoryChain, ObjectId> {
    @Query("{ 'recId' : ?0 }")
    Optional<HistoryChain> findByRecId(String recId);
}
