package com.bmh.lms.repository;

import com.bmh.lms.model.KhatianNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface KhatianNoRepository extends JpaRepository<KhatianNo, Long> {
    @Query(value="SELECT * FROM khatian_no WHERE khatian_id=? AND current=1", nativeQuery = true)
    List<KhatianNo> findKhatianNoList(Long id);

    @Query(value="SELECT * FROM khatian_no WHERE khatian_no_id=?", nativeQuery = true)
    Optional<KhatianNo> findKhatianNoByKNID(String kn_id);
}
