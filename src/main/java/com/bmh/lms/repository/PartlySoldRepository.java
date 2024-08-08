package com.bmh.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bmh.lms.model.PartlySold;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PartlySoldRepository extends JpaRepository<PartlySold, Long> {
    @Query(value="SELECT * FROM partly_sold WHERE rec_id=? AND modified_type='INSERTED'", nativeQuery=true)
    Set<PartlySold> findAllActive(String recId);

    @Query( value = "SELECT * FROM partly_sold  WHERE part_id=? AND modified_type='INSERTED'" ,nativeQuery=true)
    Optional<PartlySold> findActiveByPartId(String recId);
}
