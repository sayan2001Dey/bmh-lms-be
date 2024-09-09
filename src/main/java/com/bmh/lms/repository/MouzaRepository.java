package com.bmh.lms.repository;

import com.bmh.lms.model.Group;
import com.bmh.lms.model.Mouza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MouzaRepository extends JpaRepository<Mouza,Long> {
    @Query(value="SELECT * FROM mouza WHERE modified_type='INSERTED'", nativeQuery = true)
    List<Mouza> findAllActiveByMouzaId();

    @Query(value="SELECT * FROM mouza WHERE mouza_id=? AND modified_type='INSERTED'", nativeQuery = true)
    Optional<Mouza> findByMouzaId(String id);
}
