package com.bmh.lms.repository;

import com.bmh.lms.model.Company;
import com.bmh.lms.model.Deed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeedRepository extends JpaRepository<Deed,Long> {
    @Query(value="SELECT * FROM deed_master WHERE modified_type='INSERTED'", nativeQuery = true)
    List<Deed> findAllActiveByDeedId();

    @Query(value="SELECT * FROM deed_master WHERE deed_id=? AND modified_type='INSERTED'", nativeQuery = true)
    Optional<Deed> findByDeedId(String id);
}
