package com.bmh.lms.repository;

import com.bmh.lms.model.Uom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UomRepository extends JpaRepository<Uom, Long> {
    @Query(value="SELECT * FROM uom WHERE modified_type='INSERTED'", nativeQuery = true)
    List<Uom> findAllActive();

    @Query(value="SELECT * FROM uom WHERE classification=? AND modified_type='INSERTED'", nativeQuery = true)
    List<Uom> findAllActiveByClassification(String classification);

    @Query(value="SELECT * FROM uom WHERE uom_id=? AND modified_type='INSERTED'", nativeQuery = true)
    Optional<Uom> findByUomId(String id);
}

