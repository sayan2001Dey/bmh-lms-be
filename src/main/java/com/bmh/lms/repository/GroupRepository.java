package com.bmh.lms.repository;

import com.bmh.lms.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query(value="SELECT * FROM group_master WHERE modified_type='INSERTED'", nativeQuery = true)
    List<Group> findAllActiveByGroupId();

    @Query(value="SELECT * FROM group_master WHERE group_id=? AND modified_type='INSERTED'", nativeQuery = true)
    Optional<Group> findByGroupId(String id);
}
