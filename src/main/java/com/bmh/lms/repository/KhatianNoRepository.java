package com.bmh.lms.repository;

import com.bmh.lms.model.KhatianNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KhatianNoRepository extends JpaRepository<KhatianNo, Long> {
    @Query(value="SELECT * FROM khatian_no WHERE khatian_id=? AND current=1", nativeQuery = true)
    List<KhatianNo> findKhatianNoList(Long id);
}
