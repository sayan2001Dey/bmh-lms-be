package com.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sample.model.PartlySold;

import java.util.Set;

@Repository
public interface PartlySoldRepository extends JpaRepository<PartlySold, Long> {
    @Query(value="SELECT * FROM partly_sold WHERE rec_id=? AND modified_type='INSERTED' ORDER BY slno", nativeQuery=true)
    Set<PartlySold> findAllActive(String recId);
}
