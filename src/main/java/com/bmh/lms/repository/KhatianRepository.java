package com.bmh.lms.repository;

import com.bmh.lms.model.Khatian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KhatianRepository extends JpaRepository<Khatian,Long> {
    @Query(value="SELECT * FROM khatian WHERE modified_type='INSERTED'", nativeQuery = true)
    List<Khatian> findAllActiveByKhatianId();

    @Query(value="SELECT * FROM khatian WHERE khatian_id=? AND modified_type='INSERTED'", nativeQuery = true)
    Optional<Khatian> findByKhatianId(String id);
}
