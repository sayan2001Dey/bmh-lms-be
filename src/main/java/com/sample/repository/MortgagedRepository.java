package com.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sample.model.Mortgaged;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MortgagedRepository extends JpaRepository<Mortgaged, Long> {
    @Query (value="SELECT * FROM mortgaged WHERE rec_id=? AND modified_type='INSERTED' ORDER BY slno", nativeQuery=true)
    Set<Mortgaged> findAllActive(String recId);

    @Query( value = "SELECT * FROM mortgaged WHERE mort_id=? AND modified_type='INSERTED'" ,nativeQuery=true)
    Optional<Mortgaged> findActiveByMortId(String recId);
}
